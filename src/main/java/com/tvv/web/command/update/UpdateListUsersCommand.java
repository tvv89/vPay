package com.tvv.web.command.update;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.utils.PaginationList;
import com.tvv.web.command.Command;
import com.tvv.web.command.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class UpdateListUsersCommand extends Command {

    private static final Logger log = Logger.getLogger(UpdateListUsersCommand.class);

    private static class CompareByLogin implements Comparator<User>, Serializable {

        @Override
        public int compare(User u1, User u2) {
            return u1.getLogin().compareTo(u2.getLogin());
        }
    }
    private static class CompareByFN implements Comparator<User>, Serializable {

        @Override
        public int compare(User u1, User u2) {
            return u1.getFirstName().compareTo(u2.getFirstName());
        }
    }

    private static class CompareByLN implements Comparator<User>, Serializable {

        @Override
        public int compare(User u1, User u2) {
            return u1.getLastName().compareTo(u2.getLastName());
        }
    }

    private static Comparator<User> compareByLogin = new CompareByLogin();
    private static Comparator<User> compareByLN = new CompareByLN();
    private static Comparator<User> compareByFN = new CompareByFN();

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {

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

        List<User> list = UserDAO.findAllUsers();
        list.stream().forEach(l->l.setPassword(""));
        switch (sorting){
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

        UtilCommand.sendJSONData(response,innerObject);

    }
}
