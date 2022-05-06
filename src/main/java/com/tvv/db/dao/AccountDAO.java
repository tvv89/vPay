package com.tvv.db.dao;

import com.tvv.db.entity.Account;
import com.tvv.service.exception.AppException;

import java.util.List;

public interface AccountDAO {
    Account updateAccount (Account account) throws AppException;

    Account insertAccount(Account account) throws AppException;

    boolean deleteAccount(Account account) throws AppException;

    List<Account> findAllAccount() throws AppException;

    Account findAccountById(Long id) throws AppException;

    boolean updateAccountBalance(Long id, Double newBalance) throws AppException;

    boolean updateAccountsBalance(Long formId, Long toId, Double newBalanceFrom, Double newBalanceTo) throws AppException;

    boolean updateAccountCard(Long id, Integer cardId) throws AppException;

    List<Account> findAccountByUserId(Long id) throws AppException;

    boolean updateStatusAccountById(Long id, String status) throws AppException;

    Account findAccountByUID(String accountNumber) throws AppException;
}
