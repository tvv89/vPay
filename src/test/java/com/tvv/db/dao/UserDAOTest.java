package com.tvv.db.dao;

import com.tvv.db.DBManager;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    @Test
    void findAllUsers() throws SQLException, AppException {
       /* ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        Mockito.when(rs.getLong("id"))
                .thenReturn(1L)
                .thenReturn(1L)
                .thenReturn(1L);
        Mockito.when(rs.getString("login"))
                .thenReturn("admin")
                .thenReturn("operator")
                .thenReturn("operator2");

        Statement stmt = Mockito.mock(Statement.class);
        Mockito.when(stmt.executeQuery(UserDAO.SQL__FIND_ALL_USERS))
                .thenReturn(rs);

        Connection con = Mockito.mock(Connection.class);
        Mockito.when(con.createStatement())
                .thenReturn(stmt);

        DBManager instance = Mockito.mock(DBManager.class);
        Mockito.when(instance.getConnection())
                .thenReturn(con);

        MockedStatic<DBManager> mockedStatic =
                Mockito.mockStatic(DBManager.class);

        mockedStatic.when(DBManager::getInstance);

        List<User> users = UserDAO.findAllUsers();

        System.out.println(users);*/

    }
}