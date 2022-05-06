package com.tvv.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.AccountDAOImpl;
import com.tvv.db.dao.CardDAOImpl;
import com.tvv.db.entity.*;
import com.tvv.service.exception.AppException;
import com.tvv.utils.SystemParameters;
import com.tvv.utils.UtilsGenerator;
import com.tvv.web.command.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Business logic for Accounts
 */
public class AccountService {
    private static final Logger log = Logger.getLogger(AccountService.class);

    private final AccountDAO accountDAO;

    private String locale;
    private ResourceBundle message;

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
        this.locale = "";
        try {
            this.message = SystemParameters.getLocale(locale);
        } catch (IOException e) {
            log.error("Locale error");
        }
    }

    public void setUpLocale(String locale) {
        this.locale = locale;
        try {
            this.message = SystemParameters.getLocale(locale);
        } catch (IOException e) {
            log.error("Locale error");
        }
    }

    /**
     * Add money to accountTo balance and subtract money form accountFrom balance
     *
     * @param accountFrom debit account object
     * @param accountTo   deposit account object
     * @param valueFrom   subtract value from accountFrom
     * @param valueTo     add value to accountTo
     * @return successful operation
     * @throws AppException
     */
    public boolean depositAccount(Account accountFrom, Account accountTo, Double valueFrom, Double valueTo) throws AppException {
        boolean result = false;
        if (accountTo != null && accountFrom.getIban().equals(accountTo.getIban())) return true;
        Double balanceFrom = accountFrom.getBalance();
        DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));
        Double newBalanceFrom = Double.valueOf(df.format(balanceFrom - valueFrom));
        accountDAO.updateAccountBalance(accountFrom.getId(), newBalanceFrom);
        if (accountTo != null) {
            Double balanceTo = accountTo.getBalance();
            Double newBalanceTo = Double.valueOf(df.format(balanceTo + valueTo));
            result = accountDAO.updateAccountsBalance(accountFrom.getId(),accountTo.getId(),newBalanceFrom,newBalanceTo);
        }
        else result = accountDAO.updateAccountBalance(accountFrom.getId(), newBalanceFrom);

        return result;
    }

    /**
     * Will be used in the future. Lock account
     *
     * @param account
     * @throws AppException
     */
    public void lockAccount(Account account) throws AppException {
        if (account != null) {
            account.setStatus("disable");
            accountDAO.updateStatusAccountById(account.getId(), account.getStatus());
        } else throw new AppException(message.getString("exception.service.account.lock"), new NullPointerException());
    }

    /**
     * Create account with checking field. Checking will be developed in the future
     *
     * @param accountData Map - account data
     * @param user        account owner user
     * @throws AppException
     */
    public void createAccount(Map<String, String> accountData, User user) throws AppException {
        StringBuilder errorMessage = new StringBuilder();
        if (accountData.get("name").length() > 25)
            errorMessage.append(message.getString("exception.service.account.create.name"));
        if (errorMessage.length() == 0) {

            Account account = new Account();
            account.setId(1L);
            account.setIban(UtilsGenerator.getAccountUID());
            account.setIpn(accountData.get("ipn"));
            account.setBankCode(accountData.get("bankCode"));
            account.setName(accountData.get("name"));
            account.setCurrency(accountData.get("currency"));
            account.setBalance(0D);
            account.setOwnerUser(user);
            account.setStatus("Enabled");

            accountDAO.insertAccount(account);
        } else throw new AppException(errorMessage.toString(), new IllegalArgumentException());
    }

    /**
     * Function for delete account
     *
     * @param request        controller servlet request
     * @param jsonParameters parameters from JSON request, primary: use delete
     * @return JsonObject object, which will be sent with response
     * @throws AppException custom application exception
     */
    public JsonObject processDeleteAccount(HttpServletRequest request, Map<String, Object> jsonParameters) throws AppException {
        JsonObject innerObject = new JsonObject();
        Role userRole = (Role) request.getSession().getAttribute("userRole");
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        Integer accountId = null;
        Account accountById = null;
        try {
            accountId = (Integer) jsonParameters.get("accountId");
            accountById = accountDAO.findAccountById(accountId.longValue());

        } catch (Exception e) {
            innerObject = UtilCommand.errorMessageJSON(message.getString("exception.service.account.delete"));
            return innerObject;
        }
        if (accountById != null && userRole == Role.USER) {

            accountDAO.deleteAccount(accountById);
            innerObject.add("status", new Gson().toJsonTree("OK"));
        } else {
            innerObject = UtilCommand.errorMessageJSON(message.getString("exception.service.account.delete"));
        }
        return innerObject;
    }

    /**
     * Function for change account status: Enabled, Idle, Disabled
     *
     * @param request        controller servlet request
     * @param jsonParameters parameters from JSON request, primary: use change
     * @return JsonObject object, which will be sent with response
     * @throws AppException custom application exception
     */
    public JsonObject processChangeStatus(HttpServletRequest request, Map<String, Object> jsonParameters) throws AppException {
        JsonObject innerObject = new JsonObject();
        Role userRole = (Role) request.getSession().getAttribute("userRole");
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        Integer accountId = null;
        Account accountById = null;

        try {
            accountId = (Integer) jsonParameters.get("accountId");
            accountById = accountDAO.findAccountById(accountId.longValue());
            if (userRole == Role.USER && accountById != null) {
                if (accountById.getOwnerUser().getId() != currentUser.getId()) {
                    throw new AppException(message.getString("exception.service.account.change.incorrect_user"),
                            new IllegalArgumentException());
                }
            }
        } catch (Exception e) {
            throw new AppException(message.getString("exception.service.account.change.not_found"), e);
        }

        if (accountById != null) {
            String accountStatus;
            if (userRole == Role.ADMIN) {
                if (accountById.getStatus().equals("Disabled") || accountById.getStatus().equals("Idle"))
                    accountStatus = "Enabled";
                else accountStatus = "Disabled";
            } else {
                if (accountById.getStatus().equals("Enabled")) accountStatus = "Idle";
                else accountStatus = accountById.getStatus();
            }
            accountDAO.updateStatusAccountById(Long.valueOf(accountId), accountStatus);
            accountById = accountDAO.findAccountById(accountId.longValue());
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("userRole", new Gson().toJsonTree(userRole));
            innerObject.add("account", new Gson().toJsonTree(accountById));
        } else {
            innerObject = UtilCommand.errorMessageJSON(message.getString("exception.service.account.change.not_change"));
        }
        return innerObject;
    }

    /**
     * Function sends JSON data with card information
     *
     * @param accountId Account id from request
     * @return JsonObject with card info Name, Number, Date
     * @throws AppException
     */
    public JsonObject processCardInfo(Integer accountId) throws AppException {
        JsonObject innerObject = new JsonObject();
        CardDAOImpl cardDAO = new CardDAOImpl();
        Account accountById = accountDAO.findAccountById(Long.valueOf(accountId));
        log.trace("Info for account: " + accountById);
        if (accountById.getCard() != null) {
            Card cardById = cardDAO.findCardById(accountById.getCard().getId());
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("cardname", new Gson().toJsonTree(cardById.getName()));
            innerObject.add("cardnumber", new Gson().toJsonTree(cardById.getNumber()));
            innerObject.add("expdate", new Gson().toJsonTree(cardById.getExpDate()));
        } else {
            innerObject = UtilCommand.errorMessageJSON(message.getString("exception.service.account.change.no_card"));
            log.error("Account does not have card");
        }
        return innerObject;
    }

    /**
     * Function sends JSON data with card information
     *
     * @param accountId Account id from request
     * @param cardId    Card id from request, card will be added to account
     * @return JsonObject with card info Name, Number, Date
     * @throws AppException
     */
    public JsonObject processCardSelect(Integer accountId, Integer cardId) throws AppException {
        JsonObject innerObject = new JsonObject();
        Account accountById = accountDAO.findAccountById(Long.valueOf(accountId));
        log.trace("Info for account: " + accountById);
        if (accountById != null) {
            accountDAO.updateAccountCard(Long.valueOf(accountId), cardId);
            innerObject.add("status", new Gson().toJsonTree("OK"));
        } else {
            innerObject = UtilCommand.errorMessageJSON(message.getString("exception.service.account.change.not_found"));
            log.error("Account does not have card");
        }
        return innerObject;
    }
}
