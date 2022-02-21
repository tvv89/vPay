package com.tvv.db.dao;

import com.tvv.db.DBManager;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.LoadEntity;
import com.tvv.db.entity.Fields;
import com.tvv.db.entity.User;

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

    private static final String SQL__UPDATE_BALANCE =
            "UPDATE accounts SET balance=? WHERE id=?";

    private static final String SQL__UPDATE_STATUS =
            "UPDATE accounts SET statusaccount=? WHERE id=?";

    private static final String SQL__UPDATE_GENERAL =
            "UPDATE accounts SET IBAN=?, IPN=?, bankCode=?, name=?, currency=? WHERE id=?";

    public static List<Account> findAllAccount() {
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
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return accounts;
    }


    public static Account findAccountById (Long id){
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
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return account;
    }

    public static List<Account> findAccountByUserId (Long id)
    {
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
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return accounts;

    }


    private static class AccountLoad implements LoadEntity<Account> {

        @Override
        public Account loadRow(ResultSet rs) {
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

                account.setStatus(String.valueOf(rs.getBoolean(Fields.ACCOUNT__STATUS)));
                return account;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}