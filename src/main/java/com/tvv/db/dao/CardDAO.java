package com.tvv.db.dao;

import com.tvv.db.DBManager;
import com.tvv.db.entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardDAO {
    private static final String SQL__FIND_ALL_CARDS =
            "SELECT * FROM cards";

    private static final String SQL__FIND_CARDS_BY_ACCOUNT =
            "SELECT * FROM cards WHERE ownerAccount=?";

    private static final String SQL__FIND_CARD_BY_ID =
            "SELECT * FROM cards WHERE id=?";

    private static final String SQL__INSERT_CARD =
            "insert into cards (id, name, number, expDate, ownerAccount, statusCard) "+
            "values (default,?,?,?,?,?)";

    public static Card insertCard (Card card) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL__INSERT_CARD);
            pstmt.setString(1, card.getName());
            pstmt.setString(2, card.getNumber());
            pstmt.setString(3, card.getExpDate());
            pstmt.setLong(4, card.getAccount().getId());
            pstmt.setBoolean(5, card.getStatus());

            DAOUtils.getInsertEntityGenerateId(pstmt,rs,card);

        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return card;
    }
    public static List<Card> findAllCards() {
        List<Card> cards = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            CardLoad mapper = new CardLoad();
            con.createStatement();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL__FIND_ALL_CARDS);
            while (rs.next()) cards.add(mapper.loadRow(rs));
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return cards;
    }

    public static Card findCardById(Long id) {
        Card card = new Card();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            CardLoad mapper = new CardLoad();
            pstmt = con.prepareStatement(SQL__FIND_CARD_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next())
                card = mapper.loadRow(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return card;
    }

    public static List<Card> findCardByAccount(Long accountId) {
        List<Card> cards = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            CardLoad mapper = new CardLoad();
            pstmt = con.prepareStatement(SQL__FIND_CARDS_BY_ACCOUNT);
            pstmt.setLong(1, accountId);
            rs = pstmt.executeQuery();
            while (rs.next()) cards.add(mapper.loadRow(rs));
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return cards;
    }


    private static class CardLoad implements LoadEntity<Card> {

        @Override
        public Card loadRow(ResultSet rs) {
            try {
                Card card = new Card();
                card.setId(rs.getLong(Fields.ENTITY__ID));
                card.setName(rs.getString(Fields.CARD__NAME));
                card.setNumber(rs.getString(Fields.CARD__NUMBER));
                card.setExpDate(rs.getString(Fields.CARD__EXPIRATION_DATE));

                Account account = AccountDAO.findAccountById(rs.getLong(Fields.CARD__ACCOUNT));
                card.setAccount(account);

                card.setStatus(rs.getBoolean(Fields.CARD__STATUS));
                return card;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}