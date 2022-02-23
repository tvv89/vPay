package com.tvv.service;

import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.CardDAO;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Card;
import com.tvv.db.entity.Payment;
import com.tvv.service.exception.AppException;
import com.tvv.web.webutil.ErrorMessageEN;
import com.tvv.web.webutil.ErrorString;


public class PaymentService {
    public static void preparePayment(Payment payment) {

    }

    public static void submitPayment(Payment payment) throws AppException {
        String method = payment.getRecipientType();
        ErrorString errorString = new ErrorMessageEN();
        switch (method) {
            case ("card"):
                Card card = CardDAO.findCardById(payment.getRecipientId());
                Account accountByCard = AccountDAO.findAccountById(card.getAccount().getId());
                if (checkBalance(accountByCard, payment.getTotal())) {
                    Double newBalance = accountByCard.getBalance() - payment.getTotal();
                    AccountDAO.updateAccountBalance(accountByCard.getId(), newBalance);
                } else throw new AppException(errorString.notEnoughMoney(), new IllegalArgumentException());
                break;
            case ("account"):
                Account accountById = AccountDAO.findAccountById(payment.getRecipientId());
                if (checkBalance(accountById, payment.getTotal())) {
                    Double newBalance = accountById.getBalance() - payment.getTotal();
                    AccountDAO.updateAccountBalance(accountById.getId(), newBalance);
                } else throw new AppException("You have not enough money on Your account", new IllegalArgumentException());
                break;
        }
    }

    private static boolean checkBalance (Account account, double pay) {
        Double balance = account.getBalance();
        return (balance>=pay);
    }
}
