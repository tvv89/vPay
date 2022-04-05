package com.tvv.web.listener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

/**
 * Context listener for start initialize procedures
 */
@WebListener
public class ContextListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

    private static final Logger log = Logger.getLogger(ContextListener.class);

    public void contextDestroyed(ServletContextEvent event) {

    }

    /**
     * Initialization services
     * @param event
     */
    public void contextInitialized(ServletContextEvent event) {
        log("Initialization services starts");

        ServletContext servletContext = event.getServletContext();
        initLog4J(servletContext);
        initCommandContainer();
        initPhotoParameters(servletContext);
        log("Init photo path: "+ servletContext.getInitParameter(""));
        log("Initialization services finished");
    }

    /**
     * Initialization photo path service. Don't use
     * @param servletContext
     */
    private void initPhotoParameters(ServletContext servletContext) {
        log.debug("Photo path initialization started");
        String photoPath = servletContext.getInitParameter("photoPath");
        servletContext.setAttribute("photoPath",photoPath);
        log.info("Photo path for user pictures is: " + photoPath);
        log.debug("Photo path initialization finished");
    }

    /**
     * Initialization logging. Use Log4j
     * @param servletContext
     */
    private void initLog4J(ServletContext servletContext) {
        log("Log4J initialization started");
        try {
            /**
             * Read general properties from WEB-INF/log4j.properties
             */
            PropertyConfigurator.configure(servletContext.getRealPath(
                    "WEB-INF/log4j.properties"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        log("Log4J initialization finished");
    }

    /**
     * Initialization command container
     */
    private void initCommandContainer() {
        log.debug("Command container initialization started");

        try {
            Class.forName("com.tvv.web.command.CommandCollection");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        log.debug("Command container initialization finished");
    }

    /**
     * Console logging
     * @param msg string message
     */
    private void log(String msg) {
        System.out.println("[ContextListener] " + msg);
    }
}
