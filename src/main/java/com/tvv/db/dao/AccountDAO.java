package com.tvv.db.dao;

import com.tvv.db.DBManager;
import com.tvv.db.entity.*;
import com.tvv.service.exception.AppException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    private static final String SQL__FIND_ALL_ACCOUNT =
            "SELECT * FROM accounts";

    private static final String SQL__FIND_ACCOUNT_BY_USER =
            "SELECT * FROM accounts WHERE ownerUser=?";

    private static final String SQL__FIND_ACCOUNT_BY_ID =
            "SELECT * FROM accounts WHERE id=?";

    private static final String SQL__FIND_ACCOUNT_BY_UID =
            "SELECT * FROM accounts WHERE iban=?";

    private static final String SQL__UPDATE_BALANCE =
            "UPDATE accounts SET balance=? WHERE id=?";

    private static final String SQL__UPDATE_CARD =
            "UPDATE accounts SET card_id=? WHERE id=?";

    private static final String SQL_UPDATE_STATUS_ACCOUNT =
            "UPDATE accounts SET statusAccount=?"+
                    "	WHERE id=?";

    private static final String SQL__INSERT_ACCOUNT =
            "insert into accounts " +
                    "(id, IBAN, IPN, bankCode, " +
                    "name, currency, balance, " +
                    "ownerUser, statusAccount, card_id) "+
                    "values (default, ?, ?, ?, " +
                    "?, ?, ?," +
                    "?, ?, ?);";

    private static final String SQL__UPDATE_GENERAL =
                    "UPDATE accounts SET IBAN=?, IPN=?, " +
                    "bankCode=?, name=? WHERE id=?";

    private static final String SQL_DELETE_ACCOUNT =
            "delete from accounts WHERE id=?";

    public static Account updateAccount (Account account) throws AppException {
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL__UPDATE_GENERAL);
            pstmt.setString(1, account.getIban());
            pstmt.setString(2, account.getIpn());
            pstmt.setString(3, account.getBankCode());
            pstmt.setString(4, account.getName());
            pstmt.setLong(5, account.getId());
            pstmt.execute();
            pstmt.close();

        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            throw new AppException("Can't update account",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return account;
    }

    public static Account insertAccount (Account account) throws AppException {
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL__INSERT_ACCOUNT);
            pstmt.setString(1, account.getIban());
            pstmt.setString(2, account.getIpn());
            pstmt.setString(3, account.getBankCode());
            pstmt.setString(4, account.getName());
            pstmt.setString(5, account.getCurrency());
            pstmt.setDouble(6, account.getBalance());
            pstmt.setLong(7, account.getOwnerUser().getId());
            pstmt.setString(8, account.getStatus());

            Long cardId = -1L;
            if (account.getCard()!=null) cardId = account.getCard().getId();
            pstmt.setLong(9, cardId);

            pstmt.execute();
            pstmt.close();

        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            throw new AppException("Can't insert account",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return account;
    }

    public static boolean deleteAccount (Account account) throws AppException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean result = false;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_DELETE_ACCOUNT);
            pstmt.setLong(1, account.getId());
            pstmt.execute();
            pstmt.close();
            result = true;
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            throw new AppException("Can't delet account",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return result;
    }

    public static List<Account> findAllAccount() throws AppException {
        List<Account> accounts = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            AccountLoad mapper = new AccountLoad();
            con.createStatement();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL__FIND_ALL_ACCOUNT);
            while (rs.next()) accounts.add(mapper.loadRow(rs));
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            throw new AppException("Can't find all accounts",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return accounts;
    }

    public static Account findAccountById (Long id) throws AppException {
        Account account = new Account();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            AccountLoad mapper = new AccountLoad();
            pstmt = con.prepareStatement(SQL__FIND_ACCOUNT_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next())
                account = mapper.loadRow(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            throw new AppException("Can't find account by id",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return account;
    }

    public static boolean updateAccountBalance (Long id, Double newBalance) throws AppException {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL__UPDATE_BALANCE);
            pstmt.setDouble(1, newBalance);
            pstmt.setLong(2, id);
            pstmt.execute();
            pstmt.close();
            result = true;
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            throw new AppException("Can't update account balance",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return result;
    }

    public static boolean updateAccountCard (Long id, Integer cardId) throws AppException {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL__UPDATE_CARD);
            pstmt.setDouble(1, cardId);
            pstmt.setLong(2, id);
            pstmt.execute();
            pstmt.close();
            result = true;
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            throw new AppException("Can't update account card",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return result;
    }

    public static List<Account> findAccountByUserId (Long id) throws AppException {
        List<Account> accounts = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            AccountLoad mapper = new AccountLoad();
            pstmt = con.prepareStatement(SQL__FIND_ACCOUNT_BY_USER);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            while (rs.next())
                accounts.add(mapper.loadRow(rs));
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            throw new AppException("Can't find accounts for this user",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return accounts;

    }

    public static boolean updateStatusAccountById(Long id, String status) throws AppException {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            AccountDAO.AccountLoad mapper = new AccountLoad();
            pstmt = con.prepareStatement(SQL_UPDATE_STATUS_ACCOUNT);
            pstmt.setString(1, status);
            pstmt.setLong(2, id);
            pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            throw new AppException("Can't update account status",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return result;
    }

    public static Account findAccountByUID(String accountNumber) throws AppException {
        Account account = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            AccountLoad mapper = new AccountLoad();
            pstmt = con.prepareStatement(SQL__FIND_ACCOUNT_BY_UID);
            pstmt.setString(1, accountNumber);
            rs = pstmt.executeQuery();
            if (rs.next())
                account = mapper.loadRow(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException | AppException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            throw new AppException("Can't find account by UID",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return account;
    }

    private static class AccountLoad implements LoadEntity<Account> {
        @Override
        public Account loadRow(ResultSet rs) throws AppException {
            try {
                Account account = new Account();
                account.setId(rs.getLong(Fields.ENTITY__ID));
                account.setIban(rs.getString(Fields.ACCOUNT__IBAN));
                account.setIpn(rs.getString(Fields.ACCOUNT__IPN));
                account.setBankCode(rs.getString(Fields.ACCOUNT__BANK_CODE));
                account.setName(rs.getString(Fields.ACCOUNT__NAME));
                account.setCurrency(rs.getString(Fields.ACCOUNT__CURRENCY));
                account.setBalance(rs.getDouble(Fields.ACCOUNT__BALANCE));

                User user = UserDAO.findUserById(rs.getLong(Fields.ACCOUNT__USER_ID));
                account.setOwnerUser(user);

                Card card = null;
                if (rs.getLong(Fields.ACCOUNT__CARD)>0) card = CardDAO.findCardById(rs.getLong(Fields.ACCOUNT__CARD));
                account.setCard(card);

                account.setStatus(rs.getString(Fields.ACCOUNT__STATUS));
                return account;
            } catch (Exception e) {
                throw new AppException("Can't read data from DB, table: account",e);
            }
        }
    }
}