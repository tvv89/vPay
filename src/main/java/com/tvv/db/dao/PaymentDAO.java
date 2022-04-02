package com.tvv.db.dao;

import com.tvv.db.DBManager;
import com.tvv.db.entity.*;
import com.tvv.service.exception.AppException;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class PaymentDAO {
    private static final String SQL__FIND_ALL_PAYMENT =
            "SELECT * FROM payments where archive!=1";

    private static final String SQL__FIND_PAYMENT_BY_USER_ID =
            "SELECT * FROM payments WHERE ownerUser=? AND archive!=1";

    private static final String SQL__FIND_PAYMENT_BY_ID =
            "SELECT * FROM payments WHERE id=? AND archive!=1";

    private static final String SQL__UPDATE_STATUS_PAYMENT =
            "UPDATE payments SET statusPayment=? WHERE id=? AND archive!=1";

    private static final String SQL__INSERT_PAYMENT =
            "insert into payments (id, guid, " +
                    "ownerUser, account_id, recipientType, recipientId, " +
                    "datetimeOfLog, currency, commission, total, statusPayment, sum, currencysum, archive)\n" +
                    "values (default,?," +
                    "        ?,?,?,?," +
                    "        ?,?,?,?,?,?,?,0);";

    private static final String SQL_SET_ARCHIVE =
            "UPDATE payments SET archive=1"+
                    "	WHERE id=?";

    public static List<Payment> findAllPayments() throws AppException {
        List<Payment> payments = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            PaymentLoad mapper = new PaymentLoad();
            con.createStatement();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL__FIND_ALL_PAYMENT);
            while (rs.next()) payments.add(mapper.loadRow(rs));
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Payments not found in DB",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return payments;
    }

    public static Payment findPaymentById (Long id) throws AppException {
        Payment payment = new Payment();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            PaymentLoad mapper = new PaymentLoad();
            pstmt = con.prepareStatement(SQL__FIND_PAYMENT_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next())
                payment = mapper.loadRow(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Payment not found by ID",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return payment;
    }

    public static List<Payment> findPaymentsByUser (Long id) throws AppException {
        List<Payment> payments = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            PaymentLoad mapper = new PaymentLoad();
            pstmt = con.prepareStatement(SQL__FIND_PAYMENT_BY_USER_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) payments.add(mapper.loadRow(rs));
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Payment not found by User",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return payments;
    }

    public static boolean updatePaymentStatus(Long id, String status) throws AppException {
        boolean result = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL__UPDATE_STATUS_PAYMENT);
            pstmt.setString(1, status);
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
            pstmt.close();
            result = true;
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Can not update status payment",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return result;
    }

    public static boolean insertPayment (Payment payment) throws AppException {
        PreparedStatement pstmt = null;
        Connection con = null;
        boolean result = false;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL__INSERT_PAYMENT);
            pstmt.setString(1, payment.getGuid());
            pstmt.setLong(2, payment.getUser().getId());
            pstmt.setLong(3, payment.getSenderId().getId());
            pstmt.setString(4, payment.getRecipientType());
            pstmt.setString(5, payment.getRecipientId());
            pstmt.setString(6, payment.getTimeOfLog());
            pstmt.setString(7, payment.getCurrency());
            pstmt.setDouble(8, payment.getCommission());
            pstmt.setDouble(9, payment.getTotal());
            pstmt.setString(10, payment.getStatus());
            pstmt.setDouble(11, payment.getSum());
            pstmt.setString(12, payment.getCurrencySum());
            pstmt.execute();
            pstmt.close();
            result = true;
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Can not insert payment in DB",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return result;
    }

    public static boolean deletePaymentById(Long id) throws AppException {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            PaymentDAO.PaymentLoad mapper = new PaymentLoad();
            pstmt = con.prepareStatement(SQL_SET_ARCHIVE);
            pstmt.setLong(1, id);
            pstmt.execute();
            pstmt.close();
            result = true;
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Payment wasn't deleted",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return result;
    }

    private static class PaymentLoad implements LoadEntity<Payment> {

        @Override
        public Payment loadRow(ResultSet rs) throws AppException {
            try {
                Payment payment = new Payment();
                payment.setId(rs.getLong(Fields.ENTITY__ID));
                payment.setGuid(rs.getString(Fields.PAYMENT__GUID));

                User user = UserDAO.findUserById(rs.getLong(Fields.PAYMENT__USER));
                payment.setUser(user);
                Account sender = null;
                try {
                    sender = AccountDAO.findAccountById(rs.getLong(Fields.PAYMENT__SENDER_ID));
                }
                catch (AppException ex) {
                    System.out.println("Bad parse account");
                }
                payment.setSenderId(sender);
                payment.setRecipientType(rs.getString(Fields.PAYMENT__RECIPIENT_TYPE));
                payment.setRecipientId(rs.getString(Fields.PAYMENT__RECIPIENT_ID));

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime ldt = LocalDateTime.parse(rs.getString(Fields.PAYMENT__TIME_OF_LOG),formatter);
                    payment.setTimeOfLog(ldt.format(formatter));
                }
                catch (Exception ex) {
                    System.out.println("Bad parse datetime");
                }

                payment.setCurrency(rs.getString(Fields.PAYMENT__CURRENCY));
                payment.setCommission(rs.getDouble(Fields.PAYMENT__COMMISSION));
                payment.setTotal(rs.getDouble(Fields.PAYMENT__TOTAL));
                payment.setCurrencySum(rs.getString(Fields.PAYMENT__CURRENCY_SUM));
                payment.setSum(rs.getDouble(Fields.PAYMENT__SUM));
                payment.setStatus(rs.getString(Fields.PAYMENT__STATUS));

                return payment;
            } catch (SQLException e) {
                throw new AppException("Can nor read payment from DB",e);
            }
        }
    }

}
