package com.tvv.db.dao;

import com.tvv.db.DBManager;
import com.tvv.db.entity.Card;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardDAOTest {

    @Test
    void testInsertCard() throws SQLException, AppException {
        PreparedStatement pstmt = Mockito.mock(PreparedStatement.class);
        when(pstmt.execute())
                .thenReturn(true);

        Connection con = Mockito.mock(Connection.class);
        when(con.prepareStatement("insert into cards (id, name, number, expDate, user_id, statusCard) "+
                "values (default,?,?,?,?,?)"))
                .thenReturn(pstmt);

        DBManager instance = Mockito.mock(DBManager.class);
        when(instance.getConnection())
                .thenReturn(con);

        UserDAO uDAO = mock(UserDAO.class);
        CardDAO cardDAO = new CardDAO();
        cardDAO.setUp(instance, uDAO);

        Card insertCard = new Card();
        insertCard.setId(Long.valueOf(1L));
        insertCard.setName("Card" + 1);
        insertCard.setNumber("Number" + 1);
        insertCard.setExpDate("0" + 1 + "/23");
        User user2 = new User();
        user2.setId(1L);
        insertCard.setUser(user2);
        insertCard.setStatus(true);

        Card card = cardDAO.insertCard(insertCard);
        verify(pstmt,times(1)).setString(1,insertCard.getName());
        verify(pstmt).close();

        assertEquals(insertCard.toString(),card.toString());
    }

    @Test
    void testFindAllCards() throws SQLException, AppException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        when(rs.getLong("id"))
                .thenReturn(1L)
                .thenReturn(2L)
                .thenReturn(3L);
        when(rs.getString("name"))
                .thenReturn("Card1")
                .thenReturn("Card2")
                .thenReturn("Card3");
        when(rs.getString("number"))
                .thenReturn("Number1")
                .thenReturn("Number2")
                .thenReturn("Number3");
        when(rs.getString("expDate"))
                .thenReturn("01/23")
                .thenReturn("02/23")
                .thenReturn("03/23");
        when(rs.getLong("user_id"))
                .thenReturn(1L)
                .thenReturn(2L)
                .thenReturn(1L);
        when(rs.getBoolean("statusCard"))
                .thenReturn(false)
                .thenReturn(true)
                .thenReturn(false);
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        UserDAO uDAO = mock(UserDAO.class);
        when(uDAO.findUserById(1L)).thenReturn(user1);
        when(uDAO.findUserById(2L)).thenReturn(user2);

        Statement stmt = Mockito.mock(Statement.class);
        when(stmt.executeQuery("SELECT * FROM cards"))
                .thenReturn(rs);

        Connection con = Mockito.mock(Connection.class);
        when(con.createStatement())
                .thenReturn(stmt);

        DBManager instance = Mockito.mock(DBManager.class);
        when(instance.getConnection())
                .thenReturn(con);


        List<Card> assertList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Card card = new Card();
            card.setId(Long.valueOf(i));
            card.setName("Card"+i);
            card.setNumber("Number"+i);
            card.setExpDate("0"+i+"/23");
            if (i%2!=0) {
                card.setUser(user1);
                card.setStatus(false);
            }
            else {
                card.setUser(user2);
                card.setStatus(true);
            }
            assertList.add(card);
        }

        CardDAO cardDAO = new CardDAO();
        cardDAO.setUp(instance, uDAO);
        List<Card> cards = cardDAO.findAllCards();

        assertEquals(assertList.toString(),cards.toString());

    }

    @Test
    void testFindCardById() throws SQLException, AppException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.next())
                .thenReturn(true)
                .thenReturn(false);
        when(rs.getLong("id"))
                .thenReturn(1L);
        when(rs.getString("name"))
                .thenReturn("Card1");
        when(rs.getString("number"))
                .thenReturn("Number1");
        when(rs.getString("expDate"))
                .thenReturn("01/23");
        when(rs.getLong("user_id"))
                .thenReturn(1L);
        when(rs.getBoolean("statusCard"))
                .thenReturn(true);
        User user1 = new User();
        user1.setId(1L);
        UserDAO uDAO = mock(UserDAO.class);
        when(uDAO.findUserById(1L)).thenReturn(user1);

        PreparedStatement pstmt = Mockito.mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        Connection con = Mockito.mock(Connection.class);
        when(con.prepareStatement("SELECT * FROM cards WHERE id=?")).thenReturn(pstmt);

        DBManager instance = Mockito.mock(DBManager.class);
        when(instance.getConnection())
                .thenReturn(con);

        Card assertCard = new Card();
        assertCard.setId(Long.valueOf(1L));
        assertCard.setName("Card" + 1);
        assertCard.setNumber("Number" + 1);
        assertCard.setExpDate("0" + 1 + "/23");
        User user2 = new User();
        user2.setId(1L);
        assertCard.setUser(user2);
        assertCard.setStatus(true);

        CardDAO cardDAO = new CardDAO();
        cardDAO.setUp(instance, uDAO);
        Card card = cardDAO.findCardById(1L);

        assertEquals(assertCard.toString(), card.toString());

    }

    @Test
    void testFindCardsByUser() throws SQLException, AppException {
        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        when(rs.getLong("id"))
                .thenReturn(1L)
                .thenReturn(2L)
                .thenReturn(3L);
        when(rs.getString("name"))
                .thenReturn("Card1")
                .thenReturn("Card2")
                .thenReturn("Card3");
        when(rs.getString("number"))
                .thenReturn("Number1")
                .thenReturn("Number2")
                .thenReturn("Number3");
        when(rs.getString("expDate"))
                .thenReturn("01/23")
                .thenReturn("02/23")
                .thenReturn("03/23");
        when(rs.getLong("user_id"))
                .thenReturn(1L)
                .thenReturn(2L)
                .thenReturn(1L);
        when(rs.getBoolean("statusCard"))
                .thenReturn(false)
                .thenReturn(true)
                .thenReturn(false);
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        UserDAO uDAO = mock(UserDAO.class);
        when(uDAO.findUserById(1L)).thenReturn(user1);
        when(uDAO.findUserById(2L)).thenReturn(user2);

        PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
        when(stmt.executeQuery())
                .thenReturn(rs);

        Connection con = Mockito.mock(Connection.class);
        when(con.prepareStatement("SELECT *\n" +
                "FROM cards \n" +
                "WHERE user_id=?"))
                .thenReturn(stmt);

        DBManager instance = Mockito.mock(DBManager.class);
        when(instance.getConnection())
                .thenReturn(con);

        List<Card> assertList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Card card = new Card();
            card.setId(Long.valueOf(i));
            card.setName("Card"+i);
            card.setNumber("Number"+i);
            card.setExpDate("0"+i+"/23");
            if (i%2!=0) {
                card.setUser(user1);
                card.setStatus(false);
            }
            else {
                card.setUser(user2);
                card.setStatus(true);
            }
            assertList.add(card);
        }

        CardDAO cardDAO = new CardDAO();
        cardDAO.setUp(instance, uDAO);
        List<Card> cards = cardDAO.findCardsByUser(1L);

        assertEquals(assertList.toString(),cards.toString());
    }

    @Test
    void testUpdateStatusCardById() throws SQLException, AppException {

        PreparedStatement pstmt = Mockito.mock(PreparedStatement.class);
        when(pstmt.execute())
                .thenReturn(true);

        Connection con = Mockito.mock(Connection.class);
        when(con.prepareStatement("UPDATE cards SET statusCard=? WHERE id=?"))
                .thenReturn(pstmt);

        DBManager instance = Mockito.mock(DBManager.class);
        when(instance.getConnection())
                .thenReturn(con);

        UserDAO uDAO = mock(UserDAO.class);
        CardDAO cardDAO = new CardDAO();
        cardDAO.setUp(instance, uDAO);

        assertTrue(cardDAO.updateStatusCardById(1L,1));
        verify(pstmt,times(1)).close();
    }

}