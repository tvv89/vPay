package com.tvv.web.command.update;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mysql.cj.util.Util;
import com.tvv.db.dao.CardDAO;
import com.tvv.db.entity.Card;
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
 * Update list command for single page application include pagination and sorting
 */
public class UpdateListCardsCommand extends Command {

    private static final Logger log = Logger.getLogger(UpdateListCardsCommand.class);

    private CardDAO cardDAO;
    public UpdateListCardsCommand() {
        cardDAO = new CardDAO();
    }
    public void setUp(CardDAO cardDAO){
        this.cardDAO = cardDAO;
    }
    /**
     * Comparator for sorting by card name
     */
    private static class CompareByName implements Comparator<Card>, Serializable {

        @Override
        public int compare(Card u1, Card u2) {
            return u1.getName().compareTo(u2.getName());
        }
    }
    /**
     * Comparator for sorting by card number
     */
    private static class CompareByNumber implements Comparator<Card>, Serializable {

        @Override
        public int compare(Card u1, Card u2) {
            return u1.getNumber().compareTo(u2.getNumber());
        }
    }
    /**
     * Comparator for sorting by card owner login
     */
    private static class CompareByOwner implements Comparator<Card>, Serializable {

        @Override
        public int compare(Card u1, Card u2) {
            return u1.getUser().getLogin().compareTo(u2.getUser().getLogin());
        }
    }

    private static Comparator<Card> compareByName = new CompareByName();
    private static Comparator<Card> compareByNumber = new CompareByNumber();
    private static Comparator<Card> compareByOwner = new CompareByOwner();

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
     * single page application. Function has different ways for USER and ADMIN
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start POST command "+ this.getClass().getName());
        /**
         * Check user role
         */
        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("userRole");
        User currentUser = (User) session.getAttribute("currentUser");
        if (userRole!=Role.ADMIN && userRole!=Role.USER)
        {
            response.sendRedirect(request.getContextPath()+ Path.COMMAND__START_PAGE);
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
            log.error(e.getMessage());
        }

        /**
         * Create pagination and sorting
         */
        Integer currentPage = null;
        Integer itemPerPage = null;
        Integer sorting = null;

        try {
            currentPage = (Integer)(jsonParameters.get("currentPage"));
            itemPerPage = (Integer)(jsonParameters.get("items"));
            sorting = (Integer)(jsonParameters.get("sorting"));
        }
        catch (Exception e) {
            currentPage = 1;
            itemPerPage = 5;
            sorting = 1;
            log.error("Bad input data");
        }
        /**
         * Select and show card list
         */
        List<Card> list = new ArrayList<>();
        try {
            if (userRole == Role.ADMIN) list = cardDAO.findAllCards();
            else if (userRole == Role.USER) list = cardDAO.findCardsByUser(currentUser.getId());
            else list = null;
            log.debug("Create list of cards");
        }
        catch (AppException ex) {
            UtilCommand.sendJSONData(response,UtilCommand.errorMessageJSON(ex.getMessage()));
            log.error(ex.getMessage());
            return;
        }
        /**
         * Sorting
         *
         */
        log.debug("Sorting parameter: "+ sorting);
        switch (sorting){
            case 1:
                Collections.sort(list, compareByName);
                break;
            case 2:
                Collections.sort(list, compareByNumber);
                break;
            case 3:
                Collections.sort(list, compareByOwner);
                break;
        }

        List<Card> listX;
        int pages;
        /**
         * Create response with JSON files
         */
        if (itemPerPage==null) itemPerPage = 5;
        if (itemPerPage>0) {
            if (currentPage==null) currentPage=1;
            pages = PaginationList.getPages(list, itemPerPage);
            listX = PaginationList.getListPage(list, currentPage, itemPerPage);
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("page", new Gson().toJsonTree(currentPage));
            innerObject.add("pages", new Gson().toJsonTree(pages));
            innerObject.add("list", new Gson().toJsonTree(listX));
            log.debug("Create data for sending "+ itemPerPage +" item per page, " + currentPage+" is curren page");
        } else if (itemPerPage==-1) {
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("page", new Gson().toJsonTree(1));
            innerObject.add("pages", new Gson().toJsonTree(1));
            innerObject.add("list", new Gson().toJsonTree(list));
            log.debug("Create data for sending All Items");
        } else {
            innerObject.add("status", new Gson().toJsonTree("ERROR"));
            innerObject.add("message", new Gson().toJsonTree("Bad input data"));
            log.debug("Bad input data");
        }
        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response,innerObject);
        log.trace("End POST command "+ this.getClass().getName());

    }
}
