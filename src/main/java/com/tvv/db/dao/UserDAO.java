package com.tvv.db.dao;


import com.tvv.db.DBManager;
import com.tvv.db.entity.LoadEntity;
import com.tvv.db.entity.Fields;
import com.tvv.db.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public static final String SQL__FIND_ALL_USERS =
            "SELECT * FROM users";

    private static final String SQL__FIND_USER_BY_LOGIN =
            "SELECT * FROM users WHERE login=?";

    private static final String SQL__FIND_USER_BY_ID =
            "SELECT * FROM users WHERE id=?";

    private static final String SQL__INSERT_USER =
            "insert into users " +
                    "(id, login, password, statususer, " +
                    "role, firstname, lastname, " +
                    "dateofbirth, sex, gender, photo, email) "+
                    "values (default, ?, ?, ?," +
                    "?, ?, ?," +
                    "?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE_USER =
            "UPDATE users SET password=?, firstname=?, lastname=?"+
                    "	WHERE id=?";

    public static User insertUser (User user) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL__INSERT_USER);
            pstmt.setString(1, user.getLogin());
            pstmt.setString(2, user.getPassword());
            pstmt.setBoolean(3, user.isStatus());
            pstmt.setLong(4, user.getRole());
            pstmt.setString(5, user.getFirstName());
            pstmt.setString(6, user.getLastName());
            pstmt.setDate(7, user.getDayOfBirth());
            pstmt.setString(8, user.getSex());
            pstmt.setString(9, user.getGender());
            pstmt.setString(10, user.getPhoto());
            pstmt.setString(11, user.getEmail());

            DAOUtils.getInsertEntityGenerateId(pstmt,rs,user);

        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return user;
    }

    public static User findUserById(Long id) {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            UserLoad mapper = new UserLoad();
            pstmt = con.prepareStatement(SQL__FIND_USER_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next())
                user = mapper.loadRow(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return user;
    }

    public static List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            UserLoad mapper = new UserLoad();
            pstmt = con.prepareStatement(SQL__FIND_ALL_USERS);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                user = mapper.loadRow(rs);
                users.add(user);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return users;
    }

    public User findUserByLogin(String login) {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DBManager.getInstance().getConnection();
            UserLoad mapper = new UserLoad();
            pstmt = con.prepareStatement(SQL__FIND_USER_BY_LOGIN);
            pstmt.setString(1, login);
            rs = pstmt.executeQuery();
            if (rs.next())
                user = mapper.loadRow(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            DBManager.getInstance().rollbackAndClose(con);
            ex.printStackTrace();
        } finally {
            DBManager.getInstance().commitAndClose(con);
        }
        return user;
    }

    public boolean checkUniqueLogin(String login){
        User user = findUserByLogin(login);
        return (user==null);
    }


    private static class UserLoad implements LoadEntity<User> {

        @Override
        public User loadRow(ResultSet rs) {
            try {
                User user = new User();
                user.setId(rs.getLong(Fields.ENTITY__ID));
                user.setLogin(rs.getString(Fields.USER__LOGIN));
                user.setPassword(rs.getString(Fields.USER__PASSWORD));
                user.setRole(rs.getInt(Fields.USER__ROLE));
                user.setFirstName(rs.getString(Fields.USER__FIRST_NAME));
                user.setLastName(rs.getString(Fields.USER__LAST_NAME));
                user.setStatus(rs.getBoolean(Fields.USER__STATUS));
                user.setDayOfBirth(rs.getDate(Fields.USER__DATE_OF_BIRTH));
                user.setSex(rs.getString(Fields.USER__SEX));
                user.setGender(rs.getString(Fields.USER__GENDER));
                user.setPhoto(rs.getString(Fields.USER__PHOTO));
                user.setEmail(rs.getString(Fields.USER__EMAIL));
                return user;
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }


}
