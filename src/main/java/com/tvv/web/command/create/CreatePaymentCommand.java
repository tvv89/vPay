package com.tvv.web.command.create;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.*;
import com.tvv.service.AccountService;
import com.tvv.service.CardService;
import com.tvv.service.PaymentService;
import com.tvv.service.UserService;
import com.tvv.service.exception.AppException;
import com.tvv.utils.FieldsChecker;
import com.tvv.utils.SystemParameters;
import com.tvv.utils.UtilsGenerator;
import com.tvv.web.command.Command;
import com.tvv.web.command.UtilCommand;
import com.tvv.web.webutil.ErrorMessageEN;
import com.tvv.web.webutil.ErrorString;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Command for create card. Card can be created only by USER role
 */
public class CreatePaymentCommand extends Command {

    private static final Logger log = Logger.getLogger(CreatePaymentCommand.class);

    /**
     * Execute POST function for Controller. This function use JSON data from request, parse it, and send response to
     * page. Create payment for current user and redirect to payment list
     * User can create payment with one of types recipient: card and account
     * Payment status can be ready or submitted. Ready status don't transfer money.
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executePost(HttpServletRequest request,
                            HttpServletResponse response) throws IOException, ServletException {
        log.debug("Start create account POST command " + this.getClass().getSimpleName());

        request.setCharacterEncoding("UTF-8");

        /**
         * Check user role
         */
        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("userRole");
        User currentUser = (User) session.getAttribute("currentUser");
        if (userRole!=Role.ADMIN && userRole!=Role.USER)
        {
            response.sendRedirect(request.getContextPath()+ Path.COMMAND__START_PAGE);
            return;
        }

        /**
         * Parse parameter for create payment
         */
        String action = "noAction";
        JsonObject innerObject = new JsonObject();
        Map<String, Object> jsonParameters = new HashMap<>();
        try {
            jsonParameters = UtilCommand.parseRequestJSON(request);
            log.debug("Parse parameter: "+jsonParameters);
            action = (String) jsonParameters.get("action");
            log.trace("Read command action payment: " + action);
        } catch (Exception e) {
            log.error("Can't read correct data from request, because " + e.getMessage());
        }
        try {
            switch (action) {
                /**
                 * Show information about sender account
                 */
                case "selectAccount":
                    Integer accountId = -1;
                    accountId = (Integer) jsonParameters.get("accountId");
                    Account accountById = AccountDAO.findAccountById(accountId.longValue());
                    log.trace("Info for account: " + accountById);
                    if (accountById != null) {
                        innerObject.add("status", new Gson().toJsonTree("OK"));
                        innerObject.add("account", new Gson().toJsonTree(accountById));

                    } else {
                        innerObject = UtilCommand.errorMessageJSON("Can't find account");
                        log.error("Can't find account by id");
                    }
                    break;
                /**
                 * Check Name (and hide) of user for recipient account
                 */
                case "checkAccount":
                    String accountType = (String) jsonParameters.get("accountType");
                    String accountNumber = (String) jsonParameters.get("accountNumber");
                    Map<String, String> user = UserDAO.findUserByAccountUID(accountNumber);
                    Account accountByUID = AccountDAO.findAccountByUID(accountNumber);
                    log.trace("Info for user: " + user.get("firstName") + " " + user.get("lastName"));
                    if (!user.isEmpty()) {
                        innerObject.add("status", new Gson().toJsonTree("OK"));
                        String hideUser = UserService.hideUserName(user.get("firstName"), user.get("lastName"));
                        innerObject.add("name", new Gson().toJsonTree(hideUser));
                        innerObject.add("currency", new Gson().toJsonTree(accountByUID.getCurrency()));
                    } else {
                        innerObject.add("status", new Gson().toJsonTree("OK"));
                        innerObject.add("name", new Gson().toJsonTree("Unknown user"));
                        innerObject.add("currency", new Gson().toJsonTree("USD"));
                        log.error("Can't find account by id");
                    }
                    break;
                /**
                 * Calculate payment
                 */
                case "calculatePayment":
                    innerObject = calculatePayment(jsonParameters);
                    break;
                /**
                 * Create payment
                 */
                case "createPayment":
                    innerObject = createPayment(jsonParameters, currentUser);
                    break;
            }
        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
            log.error(ex.getMessage());
        }

        /**
         * send JSON data
         */
        UtilCommand.sendJSONData(response, innerObject);

        log.debug("Finish create account POST command " + this.getClass().getSimpleName());
    }

    /**
     * Load page "Create payment" with parameter: list of current user account
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start load command with method" + request.getMethod());
        /**
         * Check user role
         */
        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("userRole");
        User currentUser = (User) session.getAttribute("currentUser");
        if (userRole != Role.ADMIN && userRole != Role.USER) {
            response.sendRedirect(request.getContextPath() + Path.COMMAND__START_PAGE);
            return;
        }
        List<Account> list = null;
        if (userRole == Role.USER) {
            try {
                list = AccountDAO.findAccountByUserId(currentUser.getId());
                request.setAttribute("accountsPayment", list);
            } catch (AppException e) {
                UtilCommand.bedGETRequest(request, response);
                log.error(e.getMessage());
                return;
            }
        }

        RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__CREATE_PAYMENT);
        disp.forward(request, response);

        log.trace("Forward to: " + Path.PAGE__CREATE_PAYMENT);
    }

    /**
     * Calculating sum for payment
     * @param jsonParameters parameters from JSON request
     * @return
     */
    private JsonObject calculatePayment(Map<String, Object> jsonParameters) {
        JsonObject innerObject = new JsonObject();
        /**
         * Commission parameters
         */
        Double commissionPercent = SystemParameters.COMMISSION_PERCENT;
        Double commissionValue = 0D;
        Double totalPayment = 0D;
        Double value;
        String accountType = (String) jsonParameters.get("accountType");
        String accountNumber = (String) jsonParameters.get("accountNumber");
        String currencyFrom = (String) jsonParameters.get("currencyFrom");
        String currencyTo = (String) jsonParameters.get("currencyTo");
        if (FieldsChecker.checkBalanceDouble((String) jsonParameters.get("value")))
        {
            value = Double.valueOf((String) jsonParameters.get("value"));
            if (value<1) return UtilCommand.errorMessageJSON("Sum must be more then 1 "+ currencyTo);
        }
        else return UtilCommand.errorMessageJSON("Incorrect sum value");

        /**
         * Check payment type: account or card (account payment doesn't have commission)
         */
        if ("Account".equals(accountType)) {
            try {
                Account account = AccountDAO.findAccountByUID(accountNumber);
                /**
                 * Use '.' for double value
                 */
                DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));
                if (account != null) {
                    commissionPercent = 0D;
                    commissionValue = 0D;
                    totalPayment = Double.valueOf(df.format(
                            (1 + commissionPercent) * PaymentService.currencyExchange(value, currencyTo, currencyFrom)));
                    innerObject.add("status", new Gson().toJsonTree("OK"));
                    innerObject.add("currency", new Gson().toJsonTree(currencyFrom));
                    innerObject.add("commissionValue", new Gson().toJsonTree(commissionValue));
                    innerObject.add("totalPayment", new Gson().toJsonTree(totalPayment));
                } else {
                    log.debug("Account not found");
                    return UtilCommand.errorMessageJSON("Account not found");
                }
            } catch (AppException e) {
                log.debug(e.getMessage());
                return UtilCommand.errorMessageJSON(e.getMessage());
            }
        }
        /**
         * Check payment type: account or card (card payment has commission)
         */
        if ("Card".equals(accountType)) {
            if (!FieldsChecker.checkCardNumber(accountNumber.replace(" ", "")))
                return UtilCommand.errorMessageJSON("Bad card number");
            DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));
            commissionValue = Double.valueOf(df.format(
                    commissionPercent * PaymentService.currencyExchange(value, currencyTo, currencyFrom)));
            totalPayment = Double.valueOf(df.format(
                    (1 + commissionPercent) * PaymentService.currencyExchange(value, currencyTo, currencyFrom)));
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("currency", new Gson().toJsonTree(currencyFrom));
            innerObject.add("commissionValue", new Gson().toJsonTree(commissionValue));
            innerObject.add("totalPayment", new Gson().toJsonTree(totalPayment));

        }

        return innerObject;

    }

    /**
     * Function for create payment
     * @param jsonParameters parameters from JSON request
     * @param currentUser current user, who creates payment
     * @return
     */
    private JsonObject createPayment(Map<String, Object> jsonParameters, User currentUser) {
        JsonObject innerObject = new JsonObject();

        /**
         * Date time format and create time (now) for payment
         */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        String datetime = ldt.format(formatter);

        Double commissionPercent = SystemParameters.COMMISSION_PERCENT;

        Double value;
        String accountToType = (String) jsonParameters.get("accountType");
        String accountToNumber = (String) jsonParameters.get("accountNumber");
        Long accountFromId = ((Integer) jsonParameters.get("accountFromId")).longValue();
        String currencyFrom = (String) jsonParameters.get("currencyFrom");
        String currencyTo = (String) jsonParameters.get("currencyTo");
        String statusPayment = (String) jsonParameters.get("status");

        /**
         * Check correct double value for money (format: #.##)
         */
        if (FieldsChecker.checkBalanceDouble((String) jsonParameters.get("value"))) {
            value = Double.valueOf((String) jsonParameters.get("value"));
            if (value < SystemParameters.MIN_PAYMENT_SUM)
                return UtilCommand.errorMessageJSON("Sum must be more then " + SystemParameters.MIN_PAYMENT_SUM + " " + currencyTo);
            if (value > SystemParameters.MAX_PAYMENT_SUM)
                return UtilCommand.errorMessageJSON("Sum must be less then " + SystemParameters.MAX_PAYMENT_SUM + " " + currencyTo);
        } else return UtilCommand.errorMessageJSON("Incorrect sum value");

        /**
         * Check payment type: account or card (account payment doesn't have commission)
         */
        if ("Account".equals(accountToType)) {
            try {
                Account accountTo = AccountDAO.findAccountByUID(accountToNumber);
                DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));
                if (accountTo != null) {
                    commissionPercent = 0D;
                    Double totalPaymentFrom = Double.valueOf(df.format(
                            (1 + commissionPercent) * PaymentService.currencyExchange(value, currencyTo, currencyFrom)));
                    Account accountFrom = AccountDAO.findAccountById(accountFromId);
                    if (!"Enabled".equals(accountFrom.getStatus()))
                        return UtilCommand.errorMessageJSON("Your account is locked. You can't create payment.");

                    Double accountFromBalance = accountFrom.getBalance();
                    if (accountFromBalance < totalPaymentFrom)
                        return UtilCommand.errorMessageJSON("Not enough funds in the account");
                    Double totalPaymentTo = Double.valueOf(df.format(
                            PaymentService.currencyExchange(value, currencyTo, accountTo.getCurrency())));
                    /**
                     * Create payment object from parameters
                     */
                    Payment payment = new Payment();
                    payment.setId(1L);
                    payment.setGuid(UtilsGenerator.getGUID());
                    payment.setUser(currentUser);
                    payment.setSenderId(accountFrom);
                    payment.setRecipientType(accountToType);
                    payment.setRecipientId(accountToNumber);
                    payment.setTimeOfLog(datetime);
                    payment.setCurrency(currencyFrom);
                    payment.setCommission(0D);
                    payment.setTotal(totalPaymentFrom);
                    payment.setSum(value);
                    payment.setCurrencySum(currencyTo);
                    payment.setStatus(statusPayment);
                    PaymentService.createPayment(payment);
                    /**
                     * Use service for transfer money
                     */
                    if ("Submitted".equals(statusPayment))
                        AccountService.depositAccount(accountFrom, accountTo, totalPaymentFrom, totalPaymentTo);
                    innerObject.add("status", new Gson().toJsonTree("OK"));

                } else {
                    return UtilCommand.errorMessageJSON("Account not found");
                }

            } catch (AppException e) {
                return UtilCommand.errorMessageJSON(e.getMessage());
            }
        }
        /**
         * Check payment type: account or card (card payment has commission)
         */
        if ("Card".equals(accountToType)) {
            try {
                if (!FieldsChecker.checkCardNumber(accountToNumber.replace(" ", "")))
                    return UtilCommand.errorMessageJSON("Bad card number");
                DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));
                Double commissionPayment = Double.valueOf(df.format(
                        commissionPercent * PaymentService.currencyExchange(value, currencyTo, currencyFrom)));
                Double totalPaymentFrom = Double.valueOf(df.format(
                        (1 + commissionPercent) * PaymentService.currencyExchange(value, currencyTo, currencyFrom)));
                Account accountFrom = AccountDAO.findAccountById(accountFromId);
                if (!"Enabled".equals(accountFrom.getStatus()))
                    return UtilCommand.errorMessageJSON("Your account is locked. You can't create payment.");
                Double accountFromBalance = accountFrom.getBalance();
                if (accountFromBalance < totalPaymentFrom)
                    return UtilCommand.errorMessageJSON("Not enough funds in the account");
                /**
                 * Create payment object from parameters
                 */
                Payment payment = new Payment();
                payment.setId(1L);
                payment.setGuid(UtilsGenerator.getGUID());
                payment.setUser(currentUser);
                payment.setSenderId(accountFrom);
                payment.setRecipientType(accountToType);
                payment.setRecipientId(CardService.formatCard(accountToNumber));
                payment.setTimeOfLog(datetime);
                payment.setCurrency(currencyFrom);
                payment.setCommission(commissionPayment);
                payment.setTotal(totalPaymentFrom);
                payment.setSum(value);
                payment.setCurrencySum(currencyTo);
                payment.setStatus(statusPayment);
                PaymentService.createPayment(payment);
                /**
                 * Use service for transfer money
                 */
                if ("Submitted".equals(statusPayment))
                    AccountService.depositAccount(accountFrom, null, totalPaymentFrom, 0D);
                innerObject.add("status", new Gson().toJsonTree("OK"));

            } catch (AppException ex) {
                return UtilCommand.errorMessageJSON(ex.getMessage());
            }
        }

        return innerObject;

    }
}