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


public class PaymentService {

    private enum CurrencyExchange {
        EUR (1.15),
        USD (1),
        UAH (0.033);

        private final double rate;

        CurrencyExchange(double k) {
            this.rate = k;
        }

        public double getRate() {
            return rate;
        }

    }

    public static double getRateByName(String name) {
        return CurrencyExchange.valueOf(name).getRate();
    }

    public static double currencyExchange (Double value, String currencyFrom, String currencyTo) {
        double res = value*(getRateByName(currencyFrom)/getRateByName(currencyTo));
        DecimalFormat df = new DecimalFormat("#.##",new DecimalFormatSymbols(Locale.ENGLISH));
        res = Double.valueOf(df.format(res));
        return res;
    }

    public static boolean createPayment(Payment payment) throws AppException {

        return PaymentDAO.insertPayment(payment);
    }

    public static JsonObject changeStatusPayment(Payment payment) throws AppException {
        JsonObject innerObject = new JsonObject();
        if ("Ready".equals(payment.getStatus())) {
            Account accountFrom = AccountDAO.findAccountById(payment.getSenderId().getId());
            Double totalPaymentFrom = payment.getTotal();
            if (totalPaymentFrom > accountFrom.getBalance())
                return UtilCommand.errorMessageJSON("Not enough funds in the account");
            if ("Account".equals(payment.getRecipientType())) {
                Account accountTo = AccountDAO.findAccountByUID(payment.getRecipientId());
                Double totalPaymentTo = PaymentService.currencyExchange(payment.getSum(), accountTo.getCurrency(), payment.getCurrencySum());
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

    public static JsonObject deletePayment(Payment payment) throws AppException {
        JsonObject innerObject = new JsonObject();

        if (PaymentDAO.deletePaymentById(payment.getId()))
            innerObject.add("status", new Gson().toJsonTree("OK"));
        else
            innerObject= UtilCommand.errorMessageJSON("Payment can not be deleted.");

        return innerObject;

    }

}
