package com.tvv.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.PaymentDAO;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Payment;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.UtilCommand;
import com.tvv.web.webutil.ErrorMessageEN;
import com.tvv.web.webutil.ErrorString;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Business logic for Payment
 */
public class PaymentService {
    /**
     * Currency
     */
    private enum CurrencyExchange {
        /**
         * Euro with coefficient to USD
         */
        EUR (1.15),
        /**
         * Dollar with coefficient to USD
         */
        USD (1),
        /**
         * Gryvnya with coefficient to USD
         */
        UAH (0.033);

        private final double rate;

        /**
         * @param k coefficient of currency
         */
        CurrencyExchange(double k) {
            this.rate = k;
        }

        /**
         * Get coefficient by enum value
         * @return double value of coefficient
         */
        public double getRate() {
            return rate;
        }

    }

    /**
     * Get coefficient of enum by name
     * @param name
     * @return
     */
    public static double getRateByName(String name) {
        return CurrencyExchange.valueOf(name).getRate();
    }

    /**
     * Currency exchange
     * @param value value of currency from
     * @param currencyFrom currency from (string name of enum)
     * @param currencyTo currency to (string name of enum)
     * @return result value of currency to
     */
    public static double currencyExchange (Double value, String currencyFrom, String currencyTo) {
        double res = value*(getRateByName(currencyFrom)/getRateByName(currencyTo));
        DecimalFormat df = new DecimalFormat("#.##",new DecimalFormatSymbols(Locale.ENGLISH));
        res = Double.valueOf(df.format(res));
        return res;
    }

    /**
     * Create payment function
     * @param payment object Payment
     * @return successful operation
     * @throws AppException
     */
    public static boolean createPayment(Payment payment) throws AppException {
        return PaymentDAO.insertPayment(payment);
    }

    /**
     * Change status payment and deposit account/s
     * @param payment current payment
     * @return JSON result request with status of payment
     * @throws AppException
     */
    public static JsonObject changeStatusPayment(Payment payment) throws AppException {
        JsonObject innerObject = new JsonObject();
        if ("Ready".equals(payment.getStatus())) {
            Account accountFrom = AccountDAO.findAccountById(payment.getSenderId().getId());
            Double totalPaymentFrom = payment.getTotal();
            if (totalPaymentFrom > accountFrom.getBalance())
                return UtilCommand.errorMessageJSON("Not enough funds in the account");
            if ("Account".equals(payment.getRecipientType())) {
                Account accountTo = AccountDAO.findAccountByUID(payment.getRecipientId());
                Double totalPaymentTo = PaymentService.currencyExchange(payment.getSum(), payment.getCurrencySum(), accountTo.getCurrency());
                AccountService.depositAccount(accountFrom, accountTo, totalPaymentFrom, totalPaymentTo);
                payment.setStatus("Submitted");
                PaymentDAO.updatePaymentStatus(payment.getId(),"Submitted");
                innerObject.add("status", new Gson().toJsonTree("OK"));
                innerObject.add("id", new Gson().toJsonTree(payment.getId()));
                innerObject.add("statusPayment", new Gson().toJsonTree(payment.getStatus()));

                return innerObject;

            }
            if ("Card".equals(payment.getRecipientType())) {
                AccountService.depositAccount(accountFrom, null, totalPaymentFrom, 0D);
                payment.setStatus("Submitted");
                PaymentDAO.updatePaymentStatus(payment.getId(),"Submitted");
                innerObject.add("status", new Gson().toJsonTree("OK"));
                innerObject.add("id", new Gson().toJsonTree(payment.getId()));
                innerObject.add("statusPayment", new Gson().toJsonTree(payment.getStatus()));

                return innerObject;
            }
        }
        else {
            return UtilCommand.errorMessageJSON("Payment had been submitted before");
        }
        return UtilCommand.errorMessageJSON("Payment can not be submitted.");
    }

    /**
     * Function for "delete" payment, go to archive
     * @param payment current payment
     * @return JSON result request with status operation (OK or Error)
     * @throws AppException
     */
    public static JsonObject deletePayment(Payment payment) throws AppException {
        JsonObject innerObject = new JsonObject();

        if (PaymentDAO.deletePaymentById(payment.getId()))
            innerObject.add("status", new Gson().toJsonTree("OK"));
        else
            innerObject= UtilCommand.errorMessageJSON("Payment can not be deleted.");

        return innerObject;

    }

}
