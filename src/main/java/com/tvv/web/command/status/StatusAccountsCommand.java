package com.tvv.web.command.status;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAO;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
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
     * Execute GET function for Controller. This function doesn't have GET request, and redirect to error page
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UtilCommand.bedGETRequest(request,response);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start POST command "+this.getClass().getSimpleName());
        request.setCharacterEncoding("UTF-8");

        /**
         * Check user role
         */
        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("userRole");
        User currentUser = (User) session.getAttribute("currentUser");

        if (userRole!=Role.ADMIN && userRole!=Role.USER)
        {
            response.sendRedirect(request.getContextPath()+ Path.COMMAND__START_PAGE);
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
                innerObject = processChangeStatus(request, jsonParameters);
                log.debug("Action is <change>, response: "+ innerObject);
            }
            if (jsonParameters.get("action").equals("delete") && userRole == Role.USER) {
                innerObject = processDeleteAccount(request, jsonParameters);
                log.debug("Action is <delete>, response: "+ innerObject);
            }
        }
        catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
            log.error(ex.getMessage());
        }
        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response,innerObject);

        log.trace("End POST command "+this.getClass().getSimpleName());

    }

    /**
     * Function for change account status: Enabled, Idle, Disabled
     * @param request controller servlet request
     * @param jsonParameters parameters from JSON request, primary: use change
     * @return JsonObject object, which will be sent with response
     * @throws AppException custom application exception
     */
    private JsonObject processChangeStatus(HttpServletRequest request, Map<String, Object> jsonParameters) throws AppException {
        JsonObject innerObject = new JsonObject();
        Role userRole = (Role) request.getSession().getAttribute("userRole");
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        Integer accountId = null;
        Account accountById = null;

        try {
            accountId = (Integer) jsonParameters.get("accountId");
            accountById = AccountDAO.findAccountById(accountId.longValue());
            if (userRole == Role.USER && accountById!=null) {
                if (accountById.getOwnerUser().getId() != currentUser.getId()) {
                    throw new AppException("Incorrect user account id", new IllegalArgumentException());
                }
            }
        }
        catch (Exception e) {
            throw new AppException("Not found account by id", e);
        }

        if (accountById!=null) {
            String accountStatus;
            if (userRole == Role.ADMIN) {
                if (accountById.getStatus().equals("Disabled") || accountById.getStatus().equals("Idle"))
                    accountStatus = "Enabled";
                else accountStatus = "Disabled";
            } else {
                if (accountById.getStatus().equals("Enabled")) accountStatus = "Idle";
                else accountStatus = accountById.getStatus();
            }
            AccountDAO.updateStatusAccountById(Long.valueOf(accountId),accountStatus);
            accountById = AccountDAO.findAccountById(accountId.longValue());
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("userRole", new Gson().toJsonTree(userRole));
            innerObject.add("account", new Gson().toJsonTree(accountById));
        }
        else {
            innerObject = UtilCommand.errorMessageJSON("Cannot change account status");
        }
        return innerObject;
    }

    /**
     * Function for delete account
     * @param request controller servlet request
     * @param jsonParameters parameters from JSON request, primary: use delete
     * @return JsonObject object, which will be sent with response
     * @throws AppException custom application exception
     */
    private JsonObject processDeleteAccount(HttpServletRequest request, Map<String, Object> jsonParameters) throws AppException {
        JsonObject innerObject = new JsonObject();
        Role userRole = (Role) request.getSession().getAttribute("userRole");
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        Integer accountId = null;
        Account accountById = null;
        try {
            accountId = (Integer) jsonParameters.get("accountId");
            accountById = AccountDAO.findAccountById(accountId.longValue());

        } catch (Exception e) {
            innerObject = UtilCommand.errorMessageJSON("Cannot change account status");
            return innerObject;
        }
        if (accountById != null) {
            if (userRole == Role.USER) {
                AccountDAO.deleteAccount(accountById);
                innerObject.add("status", new Gson().toJsonTree("OK"));
            } else {
                innerObject = UtilCommand.errorMessageJSON("Cannot change account status");
            }
        } else {
            innerObject = UtilCommand.errorMessageJSON("Cannot change account status");
        }
        return innerObject;
    }
}
