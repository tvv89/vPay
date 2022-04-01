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


public class CreatePaymentCommand extends Command {

    private static final Logger log = Logger.getLogger(CreatePaymentCommand.class);

    @Override
    public void executePost(HttpServletRequest request,
                            HttpServletResponse response) throws IOException, ServletException {
        log.debug("Start create account POST command");

        request.setCharacterEncoding("UTF-8");

        JsonObject innerObject = new JsonObject();
        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("userRole");
        User currentUser = (User) session.getAttribute("currentUser");
        String action = "noAction";
        Map<String, Object> jsonParameters = new HashMap<>();
        try {
            jsonParameters =
                    UtilCommand.parseRequestJSON(request);
            action = (String) jsonParameters.get("action");
            log.trace("Read command action payment: " + action);
        } catch (Exception e) {
            log.error("Can't read correct data from request, because " + e.getMessage());
        }
        try {
            switch (action) {
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
                case "calculatePayment":
                    innerObject = calculatePayment(jsonParameters);
                    break;
                case "createPayment":
                    innerObject = createPayment(jsonParameters, currentUser);
                    break;
            }
        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
            log.error(ex.getMessage());
        }

        if (userRole != Role.ADMIN && userRole != Role.USER)
            response.sendRedirect(request.getContextPath() + Path.COMMAND__START_PAGE);
        else UtilCommand.sendJSONData(response, innerObject);

        log.debug("Finish create account POST command.");
    }

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start load command with method" + request.getMethod());
        HttpSession session = request.getSession();
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        Role userRole = (Role) session.getAttribute("userRole");
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
        if (userRole != Role.ADMIN && userRole != Role.USER)
            response.sendRedirect(request.getContextPath() + Path.COMMAND__START_PAGE);
        else {
            RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__CREATE_PAYMENT);
            disp.forward(request, response);
        }
        log.trace("Forward to: " + Path.PAGE__CREATE_PAYMENT);
    }

    private JsonObject calculatePayment(Map<String, Object> jsonParameters) {
        JsonObject innerObject = new JsonObject();
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

        if ("Account".equals(accountType)) {
            try {
                Account account = AccountDAO.findAccountByUID(accountNumber);
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
                    return UtilCommand.errorMessageJSON("Account not found");
                }


            } catch (AppException e) {
                return UtilCommand.errorMessageJSON(e.getMessage());
            }
        }
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

    private JsonObject createPayment(Map<String, Object> jsonParameters, User currentUser) {
        JsonObject innerObject = new JsonObject();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        String datetime = ldt.format(formatter);

        Double commissionPercent = SystemParameters.COMMISSION_PERCENT;

        Double value;
        String accountToType = (String) jsonParameters.get("accountType");
        String accountToNumber = (String) jsonParameters.get("accountNumber");
        Long accountFromId = (Long) jsonParameters.get("accountFromId");
        String currencyFrom = (String) jsonParameters.get("currencyFrom");
        String currencyTo = (String) jsonParameters.get("currencyTo");
        String statusPayment = (String) jsonParameters.get("status");
        if (FieldsChecker.checkBalanceDouble((String) jsonParameters.get("value"))) {
            value = Double.valueOf((String) jsonParameters.get("value"));
            if (value < SystemParameters.MIN_PAYMENT_SUM)
                return UtilCommand.errorMessageJSON("Sum must be more then " + SystemParameters.MIN_PAYMENT_SUM + " " + currencyTo);
            if (value > SystemParameters.MAX_PAYMENT_SUM)
                return UtilCommand.errorMessageJSON("Sum must be less then " + SystemParameters.MAX_PAYMENT_SUM + " " + currencyTo);
        } else return UtilCommand.errorMessageJSON("Incorrect sum value");

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
                    Double totalPaymentTo = PaymentService.currencyExchange(value, accountTo.getCurrency(), currencyTo);
                    AccountService.depositAccount(accountFrom, accountTo, totalPaymentFrom, totalPaymentTo);
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
                    payment.setTotal(totalPaymentTo);
                    payment.setSum(value);
                    payment.setCurrencySum(currencyTo);
                    payment.setStatus(statusPayment);
                    PaymentService.createPayment(payment);
                    innerObject.add("status", new Gson().toJsonTree("OK"));

                } else {
                    return UtilCommand.errorMessageJSON("Account not found");
                }

            } catch (AppException e) {
                return UtilCommand.errorMessageJSON(e.getMessage());
            }
        }
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
                AccountService.depositAccount(accountFrom, null, totalPaymentFrom, 0D);
                Payment payment = new Payment();
                payment.setId(1L);
                payment.setGuid(UtilsGenerator.getGUID());
                payment.setUser(currentUser);
                payment.setSenderId(accountFrom);
                payment.setRecipientType(accountToType);
                payment.setRecipientId(accountToNumber);
                payment.setTimeOfLog(datetime);
                payment.setCurrency(currencyFrom);
                payment.setCommission(commissionPayment);
                payment.setTotal(totalPaymentFrom);
                payment.setSum(value);
                payment.setCurrencySum(currencyTo);
                payment.setStatus(statusPayment);
                PaymentService.createPayment(payment);
                innerObject.add("status", new Gson().toJsonTree("OK"));
            } catch (AppException ex) {
                return UtilCommand.errorMessageJSON(ex.getMessage());
            }
        }

        return innerObject;

    }
}