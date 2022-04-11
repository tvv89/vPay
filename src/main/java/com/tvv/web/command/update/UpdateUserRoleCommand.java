package com.tvv.web.command.update;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mysql.cj.util.Util;
import com.tvv.db.dao.UserDAO;
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
 * Update role of user command for single page application
 */
public class UpdateUserRoleCommand extends Command {

    private static final Logger log = Logger.getLogger(UpdateUserRoleCommand.class);

    private UserDAO userDAO;
    private void init(){
        userDAO = new UserDAO();
    }
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

    /**
     * Execute POST function for Controller. This function use JSON data from request, parse it, and send response for
     * single page application. Function is enabled for ADMIN and change role of user
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        log.trace("Start POST command " + this.getClass().getName());
        init();
        JsonObject innerObject = new JsonObject();
        /**
         * Check user role
         */
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("userRole");
        User currentUser = (User) session.getAttribute("currentUser");
        if (userRole != Role.ADMIN && userRole != Role.USER) {
            response.sendRedirect(request.getContextPath() + Path.COMMAND__START_PAGE);
            log.debug("User role is not correct");
            return;
        }
        if (userRole == Role.USER) {
            response.sendRedirect(request.getContextPath() + Path.COMMAND__LIST_ACCOUNTS);
            log.debug("User can not change role");
            return;
        }

        /**
         * Start JSON parsing request
         */
        Map<String, Object> jsonParameters = null;
        try {
            jsonParameters = UtilCommand.parseRequestJSON(request);
        } catch (AppException e) {
            e.printStackTrace();
        }
        Integer userId = null;
        User userById = null;

        try {
            userId = (Integer)jsonParameters.get("userId");
            userById = userDAO.findUserById(userId.longValue());
        }
        catch (AppException e) {
            innerObject = UtilCommand.errorMessageJSON(e.getMessage());
        }
        try {
            if (userById != null) {
                int newRole = (userById.getRole() == 1) ? 0 : 1;
                userDAO.updateRoleUserById(Long.valueOf(userId), newRole);
                userById = userDAO.findUserById(userId.longValue());
                userById.setPassword("");
                innerObject.add("status", new Gson().toJsonTree("OK"));
                innerObject.add("user", new Gson().toJsonTree(userById));
            } else {
                innerObject = UtilCommand.errorMessageJSON("Cannot change user status");
            }
        }
        catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }
        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response, innerObject);
        log.trace("End POST command " + this.getClass().getName());

    }
}
