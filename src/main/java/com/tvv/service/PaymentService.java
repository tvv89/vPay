package com.tvv.service;

import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.CardDAO;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Card;
import com.tvv.db.entity.Payment;
import com.tvv.service.exception.AppException;
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

    public static boolean createPayment() {

        return true;
    }

    public static boolean changeStatusPayment() {

        return true;
    }


    public static void preparePayment(Payment payment) {

    }


    private static void spUtil(Account account, Payment payment) throws AppException {
        ErrorString errorString = new ErrorMessageEN();
        if (checkBalance(account, payment.getTotal())) {
            Double newBalance = account.getBalance() - payment.getTotal();
            AccountDAO.updateAccountBalance(account.getId(), newBalance);
        } else throw new AppException(errorString.notEnoughMoney(), new IllegalArgumentException());
    }

    private static boolean checkBalance (Account account, double pay) {
        Double balance = account.getBalance();
        return (balance>=pay);
    }

}
