package com.tvv.web.command.update;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class UpdateListCardsCommand extends Command {

    private static final Logger log = Logger.getLogger(UpdateListCardsCommand.class);

    private static class CompareByName implements Comparator<Card>, Serializable {

        @Override
        public int compare(Card u1, Card u2) {
            return u1.getName().compareTo(u2.getName());
        }
    }
    private static class CompareByNumber implements Comparator<Card>, Serializable {

        @Override
        public int compare(Card u1, Card u2) {
            return u1.getNumber().compareTo(u2.getNumber());
        }
    }

    private static class CompareByOwner implements Comparator<Card>, Serializable {

        @Override
        public int compare(Card u1, Card u2) {
            return u1.getUser().getLogin().compareTo(u2.getUser().getLogin());
        }
    }

    private static Comparator<Card> compareByName = new CompareByName();
    private static Comparator<Card> compareByNumber = new CompareByNumber();
    private static Comparator<Card> compareByOwner = new CompareByOwner();

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

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
            System.out.println(e);
        }
        try {
            itemPerPage = (Integer)(jsonParameters.get("items"));
        }
        catch (Exception e) {
            System.out.println(e);
        }
        try {
            sorting = (Integer)(jsonParameters.get("sorting"));
        }
        catch (Exception e) {
            System.out.println(e);
        }

        List<Card> list;
        if (userRole==Role.ADMIN) list = CardDAO.findAllCards();
        else if (userRole==Role.USER) list = CardDAO.findCardsByUser(currentUser.getId());
        else list = null;


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
        JsonObject innerObject = new JsonObject();
        if (itemPerPage==null) itemPerPage = 5;
        if (itemPerPage>0) {
            if (currentPage==null) currentPage=1;
            pages = PaginationList.getPages(list, itemPerPage);
            listX = PaginationList.getListPage(list, currentPage, itemPerPage);
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
