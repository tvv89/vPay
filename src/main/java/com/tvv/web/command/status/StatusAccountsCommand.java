package com.tvv.web.command.status;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAO;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.command.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class StatusAccountsCommand extends Command {

    private static final Logger log = Logger.getLogger(StatusAccountsCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UtilCommand.bedGETRequest(request,response);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        Role userRole = (Role) request.getSession().getAttribute("userRole");
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        Map<String, Object> jsonParameters = null;
        try {
            jsonParameters = UtilCommand.parseRequestJSON(request);
        } catch (AppException e) {
            e.printStackTrace();
        }

        JsonObject innerObject = null;
        if (jsonParameters.get("action").equals("change")) {
            innerObject = processChangeStatus(request, jsonParameters);
        }
        if (jsonParameters.get("action").equals("delete") && userRole==Role.USER) {
            innerObject = processDeleteAccount(request, jsonParameters);
        }
        if (innerObject!=null) UtilCommand.sendJSONData(response,innerObject);
        else throw new AppException("Not request", new IllegalArgumentException());

    }

    private JsonObject processChangeStatus(HttpServletRequest request, Map<String, Object> jsonParameters) throws IOException, AppException {
        JsonObject innerObject = new JsonObject();
        Role userRole = (Role) request.getSession().getAttribute("userRole");
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        try {
            jsonParameters = UtilCommand.parseRequestJSON(request);
        } catch (AppException e) {
            e.printStackTrace();
        }
        Integer accountId = null;
        Account accountById = null;

        try {

            accountId = (Integer) jsonParameters.get("accountId");
            accountById = AccountDAO.findAccountById(accountId.longValue());
            if (userRole == Role.USER) {
                if (accountById.getOwnerUser().getId() != currentUser.getId()) {
                    throw new AppException("Incorrect user account id", new IllegalArgumentException());
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }


        if (accountById!=null) {
            String accountStatus;
            if (userRole == Role.ADMIN) {
                if (accountById.getStatus().equals("Disabled") || accountById.getStatus().equals("Idle"))
                    accountStatus = "Enabled";
                else accountStatus = "Disabled";
            } else {
                if (accountById.getStatus().equals("Enabled")) accountStatus = "Idle";
                else accountStatus = accountById.getStatus();
            }
            AccountDAO.updateStatusAccountById(Long.valueOf(accountId),accountStatus);
            accountById = AccountDAO.findAccountById(accountId.longValue());
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("userRole", new Gson().toJsonTree(userRole));
            innerObject.add("account", new Gson().toJsonTree(accountById));
        }
        else {
            innerObject.add("status", new Gson().toJsonTree("ERROR"));
            innerObject.add("message", new Gson().toJsonTree("Cannot change account status"));

        }
        return innerObject;
    }

    private JsonObject processDeleteAccount(HttpServletRequest request, Map<String, Object> jsonParameters) throws IOException, AppException {
        JsonObject innerObject = new JsonObject();
        Role userRole = (Role) request.getSession().getAttribute("userRole");
        Integer accountId = null;
        Account accountById = null;
        try {

            accountId = (Integer) jsonParameters.get("accountId");
            accountById = AccountDAO.findAccountById(accountId.longValue());

        } catch (Exception e) {
            innerObject.add("status", new Gson().toJsonTree("ERROR"));
            innerObject.add("message", new Gson().toJsonTree("Cannot change account status"));
            return innerObject;
        }
        if (accountById != null) {
            if (userRole == Role.USER) {
                AccountDAO.deleteAccount(accountById);
                innerObject.add("status", new Gson().toJsonTree("OK"));
            } else {
                innerObject.add("status", new Gson().toJsonTree("ERROR"));
                innerObject.add("message", new Gson().toJsonTree("Cannot change account status"));
            }
        } else {
            innerObject.add("status", new Gson().toJsonTree("ERROR"));
            innerObject.add("message", new Gson().toJsonTree("Cannot change account status"));

        }
        return innerObject;
    }
}
