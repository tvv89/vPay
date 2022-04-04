package com.tvv.web.command.info;

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
 * This command show user information via send JSON response
 */
public class InfoUserCommand extends Command {

    private static final Logger log = Logger.getLogger(InfoUserCommand.class);

    /**
     * Function for GET request. This command class don't use GET method
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
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        log.trace("Start POST method "+this.getClass().getSimpleName());
        JsonObject innerObject = new JsonObject();
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
         * User info can read only ADMIN
         */
        if (userRole!=Role.ADMIN)
        {
            response.sendRedirect(request.getContextPath()+ Path.COMMAND__LIST_ACCOUNTS);
            return;
        }

        Integer userId = null;
        try {
            Map<String, Object> jsonParameters =
                    UtilCommand.parseRequestJSON(request);
            userId = (Integer) jsonParameters.get("userId");
            log.trace("Read user id for info: " + userId);
        }
        catch (Exception e) {
            innerObject = UtilCommand.errorMessageJSON( "Can't read correct data from request, because "+e.getMessage());
            log.error("Can't read correct data from request, because "+ e.getMessage());
        }

        try {
            User userById = UserDAO.findUserById(userId.longValue());
            userById.setPassword("");
            log.trace("Info for user: " + userById);
            if (userById != null) {
                innerObject.add("status", new Gson().toJsonTree("OK"));
                innerObject.add("user", new Gson().toJsonTree(userById));

            } else {
                innerObject.add("status", new Gson().toJsonTree("ERROR"));
                innerObject.add("message", new Gson().toJsonTree("Cannot find user"));
                log.error("Can't find user by id");
            }
        }
        catch (AppException ex)
        {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }

        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response,innerObject);
        log.trace("End POST method "+this.getClass().getSimpleName());

    }
}
