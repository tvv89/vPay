package com.tvv.db.dao;

import com.tvv.db.DBManager;
import com.tvv.db.entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PaymentDAO {
    private static final String SQL__FIND_ALL_PAYMENT =
            "SELECT * FROM payments";

    private static final String SQL__FIND_PAYMENT_BY_USER_ID =
            "SELECT * FROM payments WHERE ownerUser=?";

    private static final String SQL__FIND_PAYMENT_BY_ID =
            "SELECT * FROM payments WHERE id=?";

    private static final String SQL__UPDATE_STATUS_PAYMENT =
            "UPDATE payments SET statusPayment=? WHERE id=?";

    private static final String SQL__INSERT_PAYMENT =
            "insert into payments (id, guid, " +
                    "ownerUser, senderType, senderId, recipientType, recipientId, " +
                    "datetimeOfLog, amount, commission, total, statusPayment, currency)\n" +
                    "values (default,?," +
                    "        ?,?,?,?,?," +
                    "        ?,?,?,?,?,?);";

    public static List<Payment> findAllPayments() {
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
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return payments;
    }


    public static Payment findPaymentById (Long id){
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
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return payment;
    }

    public static List<Payment> findPaymentsByUser (Long id){
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
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return payments;
    }

    public boolean updatePaymentStatus(Long id, String status)
    {
        boolean result = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL__UPDATE_STATUS_PAYMENT);
            pstmt.setString(1, status);
            pstmt.setLong(2, id);
            pstmt.executeQuery();
            rs.close();
            pstmt.close();
            result = true;
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return result;
    }

    public static Payment insertPayment (Payment payment) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL__INSERT_PAYMENT);
            pstmt.setString(1, payment.getGuid());
            pstmt.setLong(2, payment.getUser().getId());
            pstmt.setString(3, payment.getSenderType());
            pstmt.setLong(4, payment.getSenderId());
            pstmt.setString(5, payment.getRecipientType());
            pstmt.setLong(6, payment.getRecipientId());
            pstmt.setDate(7, (Date) payment.getTimeOfLog());
            pstmt.setString(8, "");
            pstmt.setDouble(9, payment.getCommission());
            pstmt.setDouble(10, payment.getTotal());
            pstmt.setString(11, payment.getStatus());
            pstmt.setString(12, payment.getCurrency());

            DAOUtils.getInsertEntityGenerateId(pstmt,rs,payment);
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return payment;
    }

    private static class PaymentLoad implements LoadEntity<Payment> {

        @Override
        public Payment loadRow(ResultSet rs) {
            try {
                Payment payment = new Payment();
                payment.setId(rs.getLong(Fields.ENTITY__ID));
                payment.setGuid(rs.getString(Fields.PAYMENT__GUID));

                User user = UserDAO.findUserById(rs.getLong(Fields.PAYMENT__USER));
                payment.setUser(user);

                payment.setSenderType(rs.getString(Fields.PAYMENT__SENDER_TYPE));
                payment.setSenderId(rs.getLong(Fields.PAYMENT__SENDER_ID));
                payment.setRecipientType(rs.getString(Fields.PAYMENT__RECIPIENT_TYPE));
                payment.setRecipientId(rs.getLong(Fields.PAYMENT__RECIPIENT_ID));
                payment.setTimeOfLog(rs.getDate(Fields.PAYMENT__TIME_OF_LOG));
                payment.setCurrency(rs.getString(Fields.PAYMENT__CURRENCY));
                payment.setCommission(rs.getDouble(Fields.PAYMENT__COMMISSION));
                payment.setTotal(rs.getDouble(Fields.PAYMENT__TOTAL));
                payment.setStatus(rs.getString(Fields.PAYMENT__STATUS));

                return payment;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
