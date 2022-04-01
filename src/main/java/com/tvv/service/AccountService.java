package com.tvv.service;

import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.CardDAO;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Card;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.utils.UtilsGenerator;
import com.tvv.web.webutil.ErrorMessageEN;
import com.tvv.web.webutil.ErrorString;

import java.util.Map;
import java.util.Random;

public class AccountService {
    public static boolean depositAccount(Account accountFrom, Account accountTo, Double valueFrom, Double valueTo) throws AppException {
        Double balanceFrom = accountFrom.getBalance();
        AccountDAO.updateAccountBalance(accountFrom.getId(),balanceFrom-valueFrom);
        if (accountTo!=null) {
            Double balanceTo = accountTo.getBalance();
            AccountDAO.updateAccountBalance(accountTo.getId(), balanceTo + valueTo);
        }
        return true;
    }

    public static void lockAccount(Account account) throws AppException {
        if (account!=null) {
            account.setStatus("disable");
            AccountDAO.updateStatusAccountById(account.getId(), account.getStatus());
        }
        else throw new AppException("Can not find account for locking", new NullPointerException());
    }

    public static void createAccount(Map<String,String> accountData, User user) throws AppException {
        StringBuilder errorMessage = new StringBuilder();
        ErrorString error = new ErrorMessageEN();

        if (errorMessage.length()==0) {

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

            AccountDAO.insertAccount(account);
        }
        else throw new AppException(errorMessage.toString(), new IllegalArgumentException());
    }
}
