package com.tvv.db.dao;

import com.tvv.db.DBManager;
import com.tvv.db.entity.*;
import com.tvv.service.exception.AppException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO functions for CRUD operation Card
 */
public class CardDAO {
    private static final String SQL__FIND_ALL_CARDS =
            "SELECT * FROM cards";

    private static final String SQL__FIND_CARDS_BY_ACCOUNT =
            "SELECT * FROM cards WHERE ownerAccount=?";

    private static final String SQL__FIND_CARD_BY_ID =
            "SELECT * FROM cards WHERE id=?";

    private static final String SQL__UPDATE_CARD_STATUS =
            "UPDATE cards SET statusCard=? WHERE id=?";

    private static final String SQL__FIND_CARDS_BY_USER_ID =
            "SELECT *\n" +
                    "FROM cards \n" +
                    "WHERE user_id=?";

    private static final String SQL__INSERT_CARD =
            "insert into cards (id, name, number, expDate, user_id, statusCard) "+
            "values (default,?,?,?,?,?)";

    /**
     * Insert card to DB
     * @param card object Card
     * @return object Card
     * @throws AppException
     */
    public static Card insertCard (Card card) throws AppException {
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL__INSERT_CARD);
            pstmt.setString(1, card.getName());
            pstmt.setString(2, card.getNumber());
            pstmt.setString(3, card.getExpDate());
            pstmt.setLong(4, card.getUser().getId());
            pstmt.setBoolean(5, card.getStatus());
            pstmt.execute();
            pstmt.close();

        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Can not insert card to DB",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return card;
    }

    /**
     * Find all cards in DB
     * @return List of Cards
     * @throws AppException
     */
    public static List<Card> findAllCards() throws AppException {
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
        } catch (Exception ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Can not find any cards in DB",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return cards;
    }

    /**
     * Find card by id
     * @param id Card id
     * @return object Card
     * @throws AppException
     */
    public static Card findCardById(Long id) throws AppException {
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
        } catch (Exception ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Can not find card by id in DB",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return card;
    }

    /**
     * Find card by account. Not used, will be developed in the future
     * @param accountId account id
     * @return List of Cards (can be changed in the future)
     * @throws AppException
     */
    public static List<Card> findCardByAccount(Long accountId) throws AppException {
        return getCards(accountId, SQL__FIND_CARDS_BY_ACCOUNT);
    }

    /**
     * FInd cards by User id
     * @param userId User id
     * @return List of card, owner - user by id
     * @throws AppException
     */
    public static List<Card> findCardsByUser(Long userId) throws AppException {
        return getCards(userId, SQL__FIND_CARDS_BY_USER_ID);
    }

    /**
     * Function for find card
     * @param userId - object id
     * @param sqlRequest - SQL request for find Cards
     * @return List of Cards
     * @throws AppException
     */
    private static List<Card> getCards(Long userId, String sqlRequest) throws AppException {
        List<Card> cards = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            CardLoad mapper = new CardLoad();
            pstmt = con.prepareStatement(sqlRequest);
            pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) cards.add(mapper.loadRow(rs));
            rs.close();
            pstmt.close();
        } catch (Exception ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Can not find cards in DB",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return cards;
    }

    /**
     * Update card status by card id
     * @param id Card id
     * @param newStatus new status
     * @return successful operation
     * @throws AppException
     */
    public static boolean updateStatusCardById(Long id, int newStatus) throws AppException {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL__UPDATE_CARD_STATUS);
            pstmt.setLong(1, newStatus);
            pstmt.setLong(2, id);
            pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Can not update status card by id in DB",ex);
        } finally {
            DBManager.getInstance().commitCloseConnection(con);
        }
        return result;
    }

    /**
     * Class for load object from DB
     */
    private static class CardLoad implements LoadEntity<Card> {
        /**
         * Load object from ResultSet
         * @param rs ResultSet
         * @return Card object
         * @throws AppException
         */
        @Override
        public Card loadRow(ResultSet rs) throws AppException {
            try {
                Card card = new Card();
                card.setId(rs.getLong(Fields.ENTITY__ID));
                card.setName(rs.getString(Fields.CARD__NAME));
                card.setNumber(rs.getString(Fields.CARD__NUMBER));
                card.setExpDate(rs.getString(Fields.CARD__EXPIRATION_DATE));

                User user = UserDAO.findUserById(rs.getLong(Fields.CARD__USER));
                card.setUser(user);

                card.setStatus(rs.getBoolean(Fields.CARD__STATUS));
                return card;
            } catch (SQLException e) {
                throw new AppException("Can not load card from DB",e);
            }
        }
    }
}