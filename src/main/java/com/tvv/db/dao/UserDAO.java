package com.tvv.db.dao;


import com.tvv.db.DBManager;
import com.tvv.db.entity.LoadEntity;
import com.tvv.db.entity.Fields;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO functions for CRUD operation User
 */
public class UserDAO {

    public static final String SQL__FIND_ALL_USERS =
            "SELECT * FROM users";

    private static final String SQL__FIND_USER_BY_LOGIN =
            "SELECT * FROM users WHERE login=?";

    private static final String SQL__FIND_USER_BY_ACCOUNT_UID =
            "select u.id, u.lastname, u.firstname from users u JOIN accounts a on u.id = a.ownerUser where a.iban = ?";

    private static final String SQL__FIND_USER_BY_ID =
            "SELECT * FROM users WHERE id=?";

    private static final String SQL__INSERT_USER =
            "insert into users " +
                    "(id, login, password, statususer, " +
                    "role, firstname, lastname, " +
                    "dateofbirth, sex, gender, photo, email,local) "+
                    "values (default, ?, ?, ?," +
                    "?, ?, ?," +
                    "?, ?, ?, ?, ?, ?);";

    private static final String SQL_UPDATE_STATUS_USER =
            "UPDATE users SET statususer=?"+
                    "	WHERE id=?";

    private static final String SQL_UPDATE_ROLE_USER =
            "UPDATE users SET role=?"+
                    "	WHERE id=?";

    private static final String SQL_UPDATE_LOCALE_USER =
            "UPDATE users SET local=?"+
                    "	WHERE id=?";


    private DBManager dbManager;

    public UserDAO() {
        this.dbManager = DBManager.getInstance();
    }


    /**
     * Insert user into DB
     * @param user current user
     * @return result User
     * @throws AppException
     */
    public User insertUser (User user) throws AppException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL__INSERT_USER);
            pstmt.setString(1, user.getLogin());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.isStatus() ? 1 : 0);
            pstmt.setLong(4, user.getRole());
            pstmt.setString(5, user.getFirstName());
            pstmt.setString(6, user.getLastName());
            pstmt.setDate(7, Date.valueOf(user.getDayOfBirth()));
            pstmt.setString(8, user.getSex());
            pstmt.setString(9, user.getGender());
            pstmt.setString(10, user.getPhoto());
            pstmt.setString(11, user.getEmail());
            pstmt.setString(12, user.getLocal());
            pstmt.execute();
            pstmt.close();

        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not inserted user to DB",ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return user;
    }

    /**
     * Find user by id
     * @param id User id
     * @return object USer
     * @throws AppException
     */
    public User findUserById(Long id) throws AppException {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            UserLoad mapper = new UserLoad();
            pstmt = con.prepareStatement(SQL__FIND_USER_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next())
                user = mapper.loadRow(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not found user by id in DB",ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return user;
    }

    /**
     * Find user: id, first name, last name by account UID
     * @param accountUID string account UID
     * @return Map(String, String) with key id, lastname, firstname
     * @throws AppException
     */
    public Map<String,String> findUserByAccountUID(String accountUID) throws AppException {
        Map<String,String> user = new HashMap<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL__FIND_USER_BY_ACCOUNT_UID);
            pstmt.setString(1, accountUID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                user.put("id",rs.getString(Fields.ENTITY__ID));
                user.put("firstName", rs.getString(Fields.USER__FIRST_NAME));
                user.put("lastName", rs.getString(Fields.USER__LAST_NAME));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not found user by account UID in DB",ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return user;
    }

    /**
     * Find all user
     * @return List of User
     * @throws AppException
     */
    public List<User> findAllUsers() throws AppException {
        List<User> users = new ArrayList<>();
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
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
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not found users in DB",ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return users;
    }

    /**
     * Find user by login
     * @param login String login
     * @return object User
     * @throws AppException
     */
    public User findUserByLogin(String login) throws AppException {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            UserLoad mapper = new UserLoad();
            pstmt = con.prepareStatement(SQL__FIND_USER_BY_LOGIN);
            pstmt.setString(1, login);
            rs = pstmt.executeQuery();
            if (rs.next())
                user = mapper.loadRow(rs);
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not found user by login in DB",ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return user;
    }

    /**
     * Check unic login. Will be used in the future
     * @param login String login
     * @return boolean result (yes or no)
     * @throws AppException
     */
    public boolean checkUniqueLogin(String login) throws AppException {
        User user = findUserByLogin(login);
        return (user==null);
    }

    /**
     * Update status user by id
     * @param id User id
     * @param status new status (true or false; 1 or 0)
     * @return successful operation
     * @throws AppException
     */
    public boolean updateStatusUserById(Long id, int status) throws AppException {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            UserLoad mapper = new UserLoad();
            pstmt = con.prepareStatement(SQL_UPDATE_STATUS_USER);
            pstmt.setLong(1, status);
            pstmt.setLong(2, id);
            pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not updated status user by id in DB",ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return result;
    }

    /**
     * Update user role by id
     * @param id User id
     * @param role new user role
     * @return successful operation
     * @throws AppException
     */
    public boolean updateRoleUserById(Long id, int role) throws AppException {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            UserLoad mapper = new UserLoad();
            pstmt = con.prepareStatement(SQL_UPDATE_ROLE_USER);
            pstmt.setLong(1, role);
            pstmt.setLong(2, id);
            pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not updated user role by id in DB",ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return result;
    }

    /**
     * Update user locale by id
     * @param id User id
     * @param local new user local
     * @return successful operation
     * @throws AppException
     */
    public boolean updateLocalUserById(Long id, String local) throws AppException {
        boolean result = false;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_LOCALE_USER);
            pstmt.setString(1, local);
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            dbManager.rollbackCloseConnection(con);
            ex.printStackTrace();
            throw new AppException("Not updated user locale by id in DB",ex);
        } finally {
            dbManager.commitCloseConnection(con);
        }
        return result;
    }

    /**
     * Class for load object from DB
     */
    private static class UserLoad implements LoadEntity<User> {
        /**
         * Load object from ResultSet
         * @param rs ResultSet
         * @return Payment object
         * @throws AppException
         */
        @Override
        public User loadRow(ResultSet rs) throws AppException {
            try {
                User user = new User();
                user.setId(rs.getLong(Fields.ENTITY__ID));
                user.setLogin(rs.getString(Fields.USER__LOGIN));
                user.setPassword(rs.getString(Fields.USER__PASSWORD));
                user.setRole(rs.getInt(Fields.USER__ROLE));
                user.setFirstName(rs.getString(Fields.USER__FIRST_NAME));
                user.setLastName(rs.getString(Fields.USER__LAST_NAME));
                user.setStatus(rs.getBoolean(Fields.USER__STATUS));
                user.setDayOfBirth(rs.getString(Fields.USER__DATE_OF_BIRTH));
                user.setSex(rs.getString(Fields.USER__SEX));
                user.setGender(rs.getString(Fields.USER__GENDER));
                user.setPhoto(rs.getString(Fields.USER__PHOTO));
                user.setEmail(rs.getString(Fields.USER__EMAIL));
                user.setLocal(rs.getString(Fields.USER__LOCAL));
                return user;
            } catch (SQLException e) {
                throw new AppException("Not loaded row for user from DB",e);
            }
        }
    }


}
