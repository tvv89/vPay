package com.tvv.web.command;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAO;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class UpdateAccountBalanceCommand extends Command {

    private static final Logger log = Logger.getLogger(UpdateAccountBalanceCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

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
        Integer accountId = null;
        Double accountCoin = null;
        Account accountById = null;
        JsonObject innerObject = new JsonObject();

        try {
            accountId = (Integer)jsonParameters.get("accountId");
            accountCoin = Double.parseDouble(jsonParameters.get("coin").toString());
            accountById = AccountDAO.findAccountById(accountId.longValue());
        }
        catch (Exception e) {
            log.error("Bad input value");
        }


        try {
            if (accountById != null && userRole == Role.USER) {
                if (accountById.getCard() != null) {
                    AccountDAO.updateAccountBalance(accountById.getId(), accountById.getBalance() + accountCoin);
                    accountById = AccountDAO.findAccountById(accountId.longValue());
                    innerObject.add("status", new Gson().toJsonTree("OK"));
                    innerObject.add("account", new Gson().toJsonTree(accountById));
                } else {
                    innerObject = UtilCommand.errorMessageJSON("Account doesn't have card. Please add card before top up balance.");
                }
            } else {
                innerObject = UtilCommand.errorMessageJSON("Cannot change account balance");
            }
        }
        catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }

        if (userRole!=Role.ADMIN && userRole!=Role.USER) response.sendRedirect(request.getContextPath()+ Path.COMMAND__START_PAGE);
        else UtilCommand.sendJSONData(response,innerObject);

    }
}
