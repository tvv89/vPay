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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class UpdateListPaymentsCommand extends Command {

    private static final Logger log = Logger.getLogger(UpdateListPaymentsCommand.class);

    private static class CompareByGuid implements Comparator<Payment>, Serializable {

        @Override
        public int compare(Payment p1, Payment p2) {
            return p1.getGuid().compareTo(p2.getGuid());
        }
    }
    private static class CompareByTotal implements Comparator<Payment>, Serializable {

        @Override
        public int compare(Payment p1, Payment p2) {
            int result = p1.getCurrency().compareTo(p2.getCurrency());
            if (result==0) return p1.getTotal().compareTo(p2.getTotal());
            return result;
        }
    }

    private static class CompareByAscTime implements Comparator<Payment>, Serializable {

        @Override
        public int compare(Payment p1, Payment p2) {
            return p1.getTimeOfLog().compareTo(p2.getTimeOfLog());
        }
    }

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

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UtilCommand.bedGETRequest(request,response);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("userRole");
        User currentUser = (User) session.getAttribute("currentUser");

        request.setCharacterEncoding("UTF-8");
        Map<String, Object> jsonParameters =
                null;

        try {
            jsonParameters = UtilCommand.parseRequestJSON(request);
        } catch (AppException e) {
            e.printStackTrace();
        }

        Integer currentPage = null;
        Integer itemPerPage = null;
        Integer sorting = null;

        try {
            currentPage = (Integer)(jsonParameters.get("currentPage"));
        }
        catch (Exception e) {
            currentPage =1;
        }
        try {
            itemPerPage = (Integer)(jsonParameters.get("items"));
        }
        catch (Exception e) {
            itemPerPage = 5;
        }
        try {
            sorting = (Integer)(jsonParameters.get("sorting"));
        }
        catch (Exception e) {
            sorting = 1;
        }

        List<Payment> list;
        if (userRole==Role.ADMIN) list = PaymentDAO.findAllPayments();
        else if (userRole==Role.USER) list = PaymentDAO.findPaymentsByUser(currentUser.getId());
        else list = null;

        switch (sorting){
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

        int pages;
        JsonObject innerObject = new JsonObject();
        if (itemPerPage>0) {
            pages = PaginationList.getPages(list, itemPerPage);
            List<Payment> listX = PaginationList.getListPage(list, currentPage, itemPerPage);
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("page", new Gson().toJsonTree(currentPage));
            innerObject.add("pages", new Gson().toJsonTree(pages));
            innerObject.add("list", new Gson().toJsonTree(listX));
        } else if (itemPerPage==-1) {
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("page", new Gson().toJsonTree(1));
            innerObject.add("pages", new Gson().toJsonTree(1));
            innerObject.add("list", new Gson().toJsonTree(list));
        } else {
            innerObject.add("status", new Gson().toJsonTree("ERROR"));
            innerObject.add("message", new Gson().toJsonTree("Bad input data"));
        }

        if (userRole!=Role.ADMIN && userRole!=Role.USER) response.sendRedirect(request.getContextPath()+ Path.COMMAND__START_PAGE);
        else UtilCommand.sendJSONData(response,innerObject);

    }
}
