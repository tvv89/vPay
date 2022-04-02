package com.tvv.db;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Database manager. Create instance. Use connection pool from tomcat configuration
 */
public class DBManager {

    private static final Logger log = Logger.getLogger(DBManager.class);

    private static DBManager instance;

    public static synchronized DBManager getInstance() {
        if (instance == null)
            instance = new DBManager();
        return instance;
    }

    public Connection getConnection() throws SQLException {
        Connection con = null;
        try {
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource)envContext.lookup("jdbc/vPay");
            con = ds.getConnection();
            con.setAutoCommit(false);
        } catch (NamingException ex) {
            log.error("No connection in pool", ex);
        }
        return con;
    }

    private DBManager() {
    }

    /**
     * Function for commit changes in DB
     * @param con connection
     */
    public void commitCloseConnection(Connection con) {
        try {
            con.commit();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
    }

    /**
     * Function for rollback changes in DB
     * @param con connection
     */
    public void rollbackCloseConnection(Connection con) {
        try {
            con.rollback();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
    }

}
