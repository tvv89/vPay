package com.tvv.web.command.status;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.UserDAO;
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

public class StatusUsersCommand extends Command {

    private static final Logger log = Logger.getLogger(StatusUsersCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
        Map<String, Object> jsonParameters =
                null;
        try {
            jsonParameters = UtilCommand.parseRequestJSON(request,"userId");
        } catch (AppException e) {
            e.printStackTrace();
        }
        Integer userId = null;
        User userById = null;

        try {
            userId = (Integer)jsonParameters.get("userId");
            userById = UserDAO.findUserById(userId.longValue());
        }
        catch (Exception e) {
            System.out.println(e);
        }

        JsonObject innerObject = new JsonObject();
        if (userById!=null) {
            int newStatus = userById.isStatus() ? 0 : 1;
            UserDAO.updateStatusUserById(Long.valueOf(userId),newStatus);
            userById = UserDAO.findUserById(userId.longValue());
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("user", new Gson().toJsonTree(userById));
        }
        else {
            innerObject.add("status", new Gson().toJsonTree("ERROR"));
            innerObject.add("message", new Gson().toJsonTree("Cannot change user status"));

        }
        UtilCommand.sendJSONData(response,innerObject);



    }
}
