package com.tvv.web.command.status;


import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAOImpl;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.AccountService;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.command.UtilCommand;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Update status command for status account (single page application)
 */
public class StatusAccountsCommand extends Command {

    private static final Logger log = Logger.getLogger(StatusAccountsCommand.class);

    /**
     * service for account command
     */
    private AccountService service;

    /**
     * init service
     */
    public StatusAccountsCommand() {
        service = new AccountService(new AccountDAOImpl());
    }

    public void setUp(AccountService aService) {
        service = aService;
    }

    /**
     * Execute GET function for Controller. This function doesn't have GET request, and redirect to error page
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UtilCommand.bedGETRequest(request, response);
    }

    /**
     * Execute POST function for Controller. This function use JSON data from request, parse it, and send response for
     * single page application. change account status for user and admin
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start POST command " + this.getClass().getSimpleName());
        /**
         * Check user role
         */
        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("userRole");
        User currentUser = (User) session.getAttribute("currentUser");

        if (userRole != Role.ADMIN && userRole != Role.USER) {
            response.sendRedirect(request.getContextPath() + Path.COMMAND__START_PAGE);
            return;
        }

        /**
         * Start JSON parsing request
         */
        JsonObject innerObject = new JsonObject();
        Map<String, Object> jsonParameters = null;
        try {
            jsonParameters = UtilCommand.parseRequestJSON(request);
        } catch (AppException e) {
            e.printStackTrace();
            log.error("Bad parsing input data");
        }

        /**
         * Check action in request. Select action: change || delete
         */
        try {
            if (jsonParameters.get("action").equals("change")) {
                innerObject = service.processChangeStatus(request, jsonParameters);
                log.debug("Action is <change>, response: " + innerObject);
            }
            if (jsonParameters.get("action").equals("delete") && userRole == Role.USER) {
                innerObject = service.processDeleteAccount(request, jsonParameters);
                log.debug("Action is <delete>, response: " + innerObject);
            }
        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
            log.error(ex.getMessage());
        }
        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response, innerObject);

        log.trace("End POST command " + this.getClass().getSimpleName());

    }


}
