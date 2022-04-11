package com.tvv.web.command.update;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.utils.PaginationList;
import com.tvv.web.command.Command;
import com.tvv.web.command.UtilCommand;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Update list of users command for single page application include pagination and sorting
 */
public class UpdateListUsersCommand extends Command {

    private static final Logger log = Logger.getLogger(UpdateListUsersCommand.class);

    private UserDAO userDAO;
    private void init(){
        userDAO = new UserDAO();
    }
    /**
     * Comparator for sorting by login
     */
    private static class CompareByLogin implements Comparator<User>, Serializable {

        @Override
        public int compare(User u1, User u2) {
            return u1.getLogin().compareTo(u2.getLogin());
        }
    }
    /**
     * Comparator for sorting by first name
     */
    private static class CompareByFN implements Comparator<User>, Serializable {

        @Override
        public int compare(User u1, User u2) {
            return u1.getFirstName().compareTo(u2.getFirstName());
        }
    }
    /**
     * Comparator for sorting by last name
     */
    private static class CompareByLN implements Comparator<User>, Serializable {

        @Override
        public int compare(User u1, User u2) {
            return u1.getLastName().compareTo(u2.getLastName());
        }
    }

    private static Comparator<User> compareByLogin = new CompareByLogin();
    private static Comparator<User> compareByLN = new CompareByLN();
    private static Comparator<User> compareByFN = new CompareByFN();

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
     * single page application. Function is enabled ADMIN and show list of users
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
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
            log.debug("User role is not correct for this command");
            return;
        }

        /**
         * Start JSON parsing request
         */
        Map<String, Object> jsonParameters = new HashMap<>();
        try {
            jsonParameters = UtilCommand.parseRequestJSON(request);
        } catch (AppException e) {
            e.printStackTrace();
        }

        /**
         * Create pagination and sorting
         */
        Integer currentPage = null;
        Integer itemPerPage = null;
        Integer sorting = null;

        try {
            currentPage = (Integer) (jsonParameters.get("currentPage"));
        } catch (Exception e) {
            currentPage = 1;
        }
        try {
            itemPerPage = (Integer) (jsonParameters.get("items"));
        } catch (Exception e) {
            itemPerPage = 5;
        }
        try {
            sorting = (Integer) (jsonParameters.get("sorting"));
        } catch (Exception e) {
            sorting = 1;
        }

        try {
            List<User> list = userDAO.findAllUsers();
            list.stream().forEach(l -> l.setPassword(""));
            switch (sorting) {
                case 1:
                    Collections.sort(list, compareByLogin);
                    break;
                case 2:
                    Collections.sort(list, compareByFN);
                    break;
                case 3:
                    Collections.sort(list, compareByLN);
                    break;
            }

            List<User> listX;
            /**
             * Select and show user list
             */
            if (itemPerPage > 0) {
                int pages = PaginationList.getPages(list, itemPerPage);
                listX = PaginationList.getListPage(list, currentPage, itemPerPage);
                innerObject.add("status", new Gson().toJsonTree("OK"));
                innerObject.add("page", new Gson().toJsonTree(currentPage));
                innerObject.add("pages", new Gson().toJsonTree(pages));
                innerObject.add("list", new Gson().toJsonTree(listX));
            } else if (itemPerPage == -1) {
                innerObject.add("status", new Gson().toJsonTree("OK"));
                innerObject.add("page", new Gson().toJsonTree(1));
                innerObject.add("pages", new Gson().toJsonTree(1));
                innerObject.add("list", new Gson().toJsonTree(list));
            } else {
                innerObject = UtilCommand.errorMessageJSON("Bad input data");
            }
        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }
        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response, innerObject);
        log.trace("End POST command " + this.getClass().getName());

    }
}
