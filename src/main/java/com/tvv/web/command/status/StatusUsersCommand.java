package com.tvv.web.command.status;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
 * Update status command for status user (single page application)
 */
public class StatusUsersCommand extends Command {

    private static final Logger log = Logger.getLogger(StatusUsersCommand.class);

    private UserDAO userDAO;

    public StatusUsersCommand() {
        userDAO = new UserDAO();
    }
    public void setUp(UserDAO userDAO){
        this.userDAO = userDAO;
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
     * single page application. Function can change status user
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
            log.debug("User role is not correct");
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
            log.error(e.getMessage());
        }
        Integer userId = null;
        User userById = null;

        try {
            userId = (Integer) jsonParameters.get("userId");
            userById = userDAO.findUserById(userId.longValue());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        /**
         * Check payment owner and select action from request
         */
        try {
            if (userById != null && currentUser != null) {
                int newStatus = userById.isStatus() ? 0 : 1;
                userDAO.updateStatusUserById(Long.valueOf(userId), newStatus);
                userById = userDAO.findUserById(userId.longValue());
                innerObject.add("status", new Gson().toJsonTree("OK"));
                innerObject.add("user", new Gson().toJsonTree(userById));
            } else {
                innerObject.add("status", new Gson().toJsonTree("ERROR"));
                innerObject.add("message", new Gson().toJsonTree("Cannot change user status"));
            }
        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }

        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response, innerObject);

        log.trace("End POST command " + this.getClass().getSimpleName());

    }
}
