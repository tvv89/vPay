package com.tvv.web.command.update;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.PaymentDAO;
import com.tvv.db.entity.*;
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
 * Update list command for single page application include pagination and sorting for showing payment list
 */
public class UpdateListPaymentsCommand extends Command {

    private static final Logger log = Logger.getLogger(UpdateListPaymentsCommand.class);

    private PaymentDAO paymentDAO;

    public UpdateListPaymentsCommand() {
        paymentDAO = new PaymentDAO();
    }

    public void setUp(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    /**
     * Comparator for sorting by payment GUID
     */
    private static class CompareByGuid implements Comparator<Payment>, Serializable {

        @Override
        public int compare(Payment p1, Payment p2) {
            return p1.getGuid().compareTo(p2.getGuid());
        }
    }

    /**
     * Comparator for sorting by total
     */
    private static class CompareByTotal implements Comparator<Payment>, Serializable {

        @Override
        public int compare(Payment p1, Payment p2) {
            int result = p1.getCurrency().compareTo(p2.getCurrency());
            if (result == 0) return p1.getTotal().compareTo(p2.getTotal());
            return result;
        }
    }

    /**
     * Comparator for sorting by time of log ascending
     */
    private static class CompareByAscTime implements Comparator<Payment>, Serializable {

        @Override
        public int compare(Payment p1, Payment p2) {
            return p1.getTimeOfLog().compareTo(p2.getTimeOfLog());
        }
    }

    /**
     * Comparator for sorting by time of log descending
     */
    private static class CompareByDesTime implements Comparator<Payment>, Serializable {

        @Override
        public int compare(Payment p1, Payment p2) {
            return -p1.getTimeOfLog().compareTo(p2.getTimeOfLog());
        }
    }


    private static Comparator<Payment> compareByGuid = new CompareByGuid();
    private static Comparator<Payment> compareByTotal = new CompareByTotal();
    private static Comparator<Payment> compareByAscTime = new CompareByAscTime();
    private static Comparator<Payment> compareByDesTime = new CompareByDesTime();

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
     * single page Payment. Function has different ways for USER and ADMIN
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start POST command");
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

        /**
         * Select and show payment list
         */
        List<Payment> list = new ArrayList<>();
        try {
            if (userRole == Role.ADMIN) list = paymentDAO.findAllPayments();
            else if (userRole == Role.USER) list = paymentDAO.findPaymentsByUser(currentUser.getId());
            else list = null;
        } catch (AppException ex) {
            log.error(ex.getMessage());
        }

        /**
         * Sorting
         */
        switch (sorting) {
            case 1:
                Collections.sort(list, compareByGuid);
                break;
            case 2:
                Collections.sort(list, compareByTotal);
                break;
            case 3:
                Collections.sort(list, compareByAscTime);
                break;
            case 4:
                Collections.sort(list, compareByDesTime);
                break;
        }

        /**
         * Create response with JSON files
         */
        int pages;
        if (itemPerPage > 0) {
            pages = PaginationList.getPages(list, itemPerPage);
            List<Payment> listX = PaginationList.getListPage(list, currentPage, itemPerPage);
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("page", new Gson().toJsonTree(currentPage));
            innerObject.add("pages", new Gson().toJsonTree(pages));
            innerObject.add("list", new Gson().toJsonTree(listX));
            log.debug("Create data for sending " + itemPerPage + " item per page, " + currentPage + " is curren page");
        } else if (itemPerPage == -1) {
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("page", new Gson().toJsonTree(1));
            innerObject.add("pages", new Gson().toJsonTree(1));
            innerObject.add("list", new Gson().toJsonTree(list));
        } else {
            innerObject.add("status", new Gson().toJsonTree("ERROR"));
            innerObject.add("message", new Gson().toJsonTree("Bad input data"));
            log.debug("Bad input data");
        }

        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response, innerObject);
        log.trace("End POST command " + this.getClass().getName());

    }
}
