package com.tvv.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.AccountDAOImpl;
import com.tvv.db.dao.PaymentDAO;
import com.tvv.db.dao.PaymentDAOImpl;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Payment;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.utils.FieldsChecker;
import com.tvv.utils.SystemParameters;
import com.tvv.utils.UtilsGenerator;
import com.tvv.web.command.UtilCommand;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

/**
 * Business logic for Payment
 */
public class PaymentService {

    private static final Logger log = Logger.getLogger(PaymentService.class);

    private final AccountService aService;
    private final AccountDAO accountDAO;
    private final PaymentDAO paymentDAO;

    public PaymentService(AccountService aService, AccountDAO accountDAO, PaymentDAO paymentDAO) {
        this.aService = aService;
        this.accountDAO = accountDAO;
        this.paymentDAO = paymentDAO;
    }

    /**
     * Currency
     */
    private enum CurrencyExchange {
        /**
         * Euro with coefficient to USD
         */
        EUR(33),
        /**
         * Dollar with coefficient to USD
         */
        USD(30),
        /**
         * Gryvnya with coefficient to USD
         */
        UAH(1);

        private final double rate;

        /**
         * @param k coefficient of currency
         */
        CurrencyExchange(double k) {
            this.rate = k;
        }

        /**
         * Get coefficient by enum value
         *
         * @return double value of coefficient
         */
        public double getRate() {
            return rate;
        }

    }

    /**
     * Get coefficient of enum by name
     *
     * @param name
     * @return
     */
    public static double getRateByName(String name) {
        return CurrencyExchange.valueOf(name).getRate();
    }

    /**
     * Currency exchange
     *
     * @param value        value of currency from
     * @param currencyFrom currency from (string name of enum)
     * @param currencyTo   currency to (string name of enum)
     * @return result value of currency to
     */
    public static double currencyExchange(Double value, String currencyFrom, String currencyTo) {
        double res = value * (getRateByName(currencyFrom) / getRateByName(currencyTo));
        DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));
        res = Double.valueOf(df.format(res));
        return res;
    }

    /**
     * Create payment function
     *
     * @param payment object Payment
     * @return successful operation
     * @throws AppException
     */
    public boolean insertPaymentToDB(Payment payment) throws AppException {
        if (payment == null)
            throw new AppException("Please, check payment value, payment can't be null",
                    new IllegalArgumentException());
        return paymentDAO.insertPayment(payment);
    }

    /**
     * Change status payment and deposit account/s
     *
     * @param payment current payment
     * @return JSON result request with status of payment
     * @throws AppException
     */
    public JsonObject changeStatusPayment(Payment payment) throws AppException {
        JsonObject innerObject = new JsonObject();
        if (payment == null) return UtilCommand.errorMessageJSON("Payment can not be submitted.");
        if ("Ready".equals(payment.getStatus())) {
            Account accountFrom = accountDAO.findAccountById(payment.getSenderId().getId());
            Double totalPaymentFrom = payment.getTotal();
            if (totalPaymentFrom > accountFrom.getBalance())
                return UtilCommand.errorMessageJSON("Not enough funds in the account");
            if ("Account".equals(payment.getRecipientType())) {
                Account accountTo = accountDAO.findAccountByUID(payment.getRecipientId());
                Double totalPaymentTo = currencyExchange(payment.getSum(), payment.getCurrencySum(), accountTo.getCurrency());
                boolean deposite = aService.depositAccount(accountFrom, accountTo, totalPaymentFrom, totalPaymentTo);
                return getJsonObject(payment, innerObject, deposite);

            }
            if ("Card".equals(payment.getRecipientType())) {
                boolean deposite = aService.depositAccount(accountFrom, null, totalPaymentFrom, 0D);
                return getJsonObject(payment, innerObject, deposite);
            }
        } else {
            return UtilCommand.errorMessageJSON("Payment had been submitted before");
        }
        return UtilCommand.errorMessageJSON("Payment can not be submitted.");
    }

    private JsonObject getJsonObject(Payment payment, JsonObject innerObject, boolean deposite) throws AppException {
        if (deposite) {
            payment.setStatus("Submitted");
            paymentDAO.updatePaymentStatus(payment.getId(), "Submitted");
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("id", new Gson().toJsonTree(payment.getId()));
            innerObject.add("statusPayment", new Gson().toJsonTree(payment.getStatus()));
            return innerObject;
        }
        else return UtilCommand.errorMessageJSON("Something was happened, try again");
    }

    /**
     * Function for "delete" payment, go to archive
     *
     * @param payment current payment
     * @return JSON result request with status operation (OK or Error)
     * @throws AppException
     */
    public JsonObject deletePayment(Payment payment) throws AppException {
        JsonObject innerObject = new JsonObject();

        if (paymentDAO.deletePaymentById(payment.getId()))
            innerObject.add("status", new Gson().toJsonTree("OK"));
        else
            innerObject = UtilCommand.errorMessageJSON("Payment can not be deleted.");

        return innerObject;

    }

    /**
     * Calculating sum for payment
     *
     * @param jsonParameters parameters from JSON request
     * @return
     */
    public JsonObject calculatePayment(Map<String, Object> jsonParameters) {
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
        if (FieldsChecker.checkBalanceDouble((String) jsonParameters.get("value"))) {
            value = Double.valueOf((String) jsonParameters.get("value"));
            if (value < SystemParameters.MIN_PAYMENT_SUM || value > SystemParameters.MAX_PAYMENT_SUM)
                return UtilCommand.errorMessageJSON("Sum must be more than " +
                        SystemParameters.MIN_PAYMENT_SUM +" and less than " +
                        SystemParameters.MAX_PAYMENT_SUM +" " +currencyTo);
        } else return UtilCommand.errorMessageJSON("Incorrect sum value");

        /**
         * Check payment type: account or card (account payment doesn't have commission)
         */
        if ("Account".equals(accountType)) {
            try {
                Account account = accountDAO.findAccountByUID(accountNumber);
                /**
                 * Use '.' for double value
                 */
                DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));
                if (account != null) {
                    commissionPercent = 0D;
                    commissionValue = 0D;
                    totalPayment = Double.valueOf(df.format(
                            (1 + commissionPercent) * currencyExchange(value, currencyTo, currencyFrom)));
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
                    commissionPercent * currencyExchange(value, currencyTo, currencyFrom)));
            totalPayment = Double.valueOf(df.format(
                    (1 + commissionPercent) * currencyExchange(value, currencyTo, currencyFrom)));
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("currency", new Gson().toJsonTree(currencyFrom));
            innerObject.add("commissionValue", new Gson().toJsonTree(commissionValue));
            innerObject.add("totalPayment", new Gson().toJsonTree(totalPayment));
        }
        return innerObject;

    }

    /**
     * Function for create payment
     *
     * @param jsonParameters parameters from JSON request
     * @param currentUser    current user, who creates payment
     * @return
     */
    public JsonObject createPayment(Map<String, Object> jsonParameters, User currentUser) {
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
            if (value < SystemParameters.MIN_PAYMENT_SUM || value > SystemParameters.MAX_PAYMENT_SUM)
                return UtilCommand.errorMessageJSON("Sum must be more than " +
                        SystemParameters.MIN_PAYMENT_SUM +" and less than " +
                        SystemParameters.MAX_PAYMENT_SUM +" " +currencyTo);
        } else return UtilCommand.errorMessageJSON("Incorrect sum value");

        /**
         * Check payment type: account or card (account payment doesn't have commission)
         */
        if ("Account".equals(accountToType)) {
            try {
                Account accountTo = accountDAO.findAccountByUID(accountToNumber);
                DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));
                if (accountTo != null) {
                    commissionPercent = 0D;
                    Double totalPaymentFrom = Double.valueOf(df.format(
                            (1 + commissionPercent) * currencyExchange(value, currencyTo, currencyFrom)));
                    Account accountFrom = accountDAO.findAccountById(accountFromId);
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
                    /**
                     * Use service for transfer money
                     */
                    if ("Submitted".equals(statusPayment)){
                       boolean result =  aService.depositAccount(accountFrom, accountTo, totalPaymentFrom, totalPaymentTo);
                       if (!result) return UtilCommand.errorMessageJSON("Something was happened, try again");
                    }
                    insertPaymentToDB(payment);
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
                        commissionPercent * currencyExchange(value, currencyTo, currencyFrom)));
                Double totalPaymentFrom = Double.valueOf(df.format(
                        (1 + commissionPercent) * currencyExchange(value, currencyTo, currencyFrom)));
                Account accountFrom = accountDAO.findAccountById(accountFromId);
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
                /**
                 * Use service for transfer money
                 */
                if ("Submitted".equals(statusPayment))
                    aService.depositAccount(accountFrom, null, totalPaymentFrom, 0D);
                insertPaymentToDB(payment);
                innerObject.add("status", new Gson().toJsonTree("OK"));

            } catch (AppException ex) {
                return UtilCommand.errorMessageJSON(ex.getMessage());
            }
        }

        return innerObject;

    }
}
