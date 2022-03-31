package com.tvv.web.command.update;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.CardDAO;
import com.tvv.db.entity.Account;
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

public class UpdateListAccountsCommand extends Command {

    private static final Logger log = Logger.getLogger(UpdateListAccountsCommand.class);

    private static class CompareByName implements Comparator<Account>, Serializable {

        @Override
        public int compare(Account u1, Account u2) {
            return u1.getName().compareTo(u2.getName());
        }
    }
    private static class CompareByBalance implements Comparator<Account>, Serializable {

        @Override
        public int compare(Account u1, Account u2) {
            return u1.getBalance().compareTo(u2.getBalance());
        }
    }
    private static class CompareByIBAN implements Comparator<Account>, Serializable {

        @Override
        public int compare(Account u1, Account u2) {
            return u1.getIban().compareTo(u2.getIban());
        }
    }

    private static Comparator<Account> compareByName = new CompareByName();
    private static Comparator<Account> compareByBalance = new CompareByBalance();
    private static Comparator<Account> compareByIBAN = new CompareByIBAN();

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
        Map<String, Object> jsonParameters = null;
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

        JsonObject innerObject = new JsonObject();
        try {
            List<Account> list;
            List<Card> cards = new ArrayList<>();
            if (userRole == Role.ADMIN) list = AccountDAO.findAllAccount();
            else if (userRole == Role.USER) {
                list = AccountDAO.findAccountByUserId(currentUser.getId());
                cards = CardDAO.findCardsByUser(currentUser.getId());
                try {
                    cards.stream().forEach(c -> c.getUser().setPassword(""));
                } catch (Exception e) {
                    log.error("Error: can not find cards or cards in null. " + e.getMessage());
                }

            } else list = null;

            switch (sorting) {
                case 1:
                    Collections.sort(list, compareByName);
                    break;
                case 2:
                    Collections.sort(list, compareByBalance);
                    break;
                case 3:
                    Collections.sort(list, compareByIBAN);
                    break;
            }

            int pages;
            if (itemPerPage == null) itemPerPage = 5;
            if (itemPerPage > 0) {
                if (currentPage == null) currentPage = 1;
                pages = PaginationList.getPages(list, itemPerPage);
                List<Account> listX = PaginationList.getListPage(list, currentPage, itemPerPage);
                innerObject.add("status", new Gson().toJsonTree("OK"));
                innerObject.add("userRole", new Gson().toJsonTree(userRole));
                innerObject.add("page", new Gson().toJsonTree(currentPage));
                innerObject.add("pages", new Gson().toJsonTree(pages));
                innerObject.add("list", new Gson().toJsonTree(listX));
                innerObject.add("cards", new Gson().toJsonTree(cards));
            } else if (itemPerPage == -1) {
                innerObject.add("status", new Gson().toJsonTree("OK"));
                innerObject.add("page", new Gson().toJsonTree(1));
                innerObject.add("pages", new Gson().toJsonTree(1));
                innerObject.add("list", new Gson().toJsonTree(list));
                innerObject.add("cards", new Gson().toJsonTree(cards));
            } else {
                innerObject.add("status", new Gson().toJsonTree("ERROR"));
                innerObject.add("message", new Gson().toJsonTree("Bad input data"));
            }
        }
        catch (AppException ex) {
            innerObject.add("status", new Gson().toJsonTree("ERROR"));
            innerObject.add("message", new Gson().toJsonTree(ex.getMessage()));
        }
        if (userRole!=Role.ADMIN && userRole!=Role.USER) response.sendRedirect(request.getContextPath()+ Path.COMMAND__START_PAGE);
        else UtilCommand.sendJSONData(response,innerObject);

    }
}
