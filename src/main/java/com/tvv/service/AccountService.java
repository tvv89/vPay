package com.tvv.service;

import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.CardDAO;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Card;
import com.tvv.service.exception.AppException;

public class AccountService {
    public static void depositAccount(String type, Long id, Double amount) throws AppException {
        switch (type){
            case ("card"):
                Card card = CardDAO.findCardById(id);
                Account accountByCard = AccountDAO.findAccountById(card.getAccount().getId());
                Double newBalanceCard = accountByCard.getBalance() + amount;
                AccountDAO.updateAccountBalance(accountByCard.getId(), newBalanceCard);
                break;
            case ("account"):
                Account accountById = AccountDAO.findAccountById(id);
                Double newBalanceAccount = accountById.getBalance() + amount;
                AccountDAO.updateAccountBalance(accountById.getId(), newBalanceAccount);
                break;
        }
    }

    public static void lockAccount(Account account) throws AppException {
        if (account!=null) {
            account.setStatus(false);
            AccountDAO.updateAccountStatus(account.getId(), account.isStatus());
        }
        else throw new AppException("Can not find account for locking", new NullPointerException());
    }
}
