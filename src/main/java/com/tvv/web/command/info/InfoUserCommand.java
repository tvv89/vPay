package com.tvv.web.command.info;

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

public class InfoUserCommand extends Command {

    private static final Logger log = Logger.getLogger(InfoUserCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        request.setCharacterEncoding("UTF-8");

        Integer userId = null;
        try {
            Map<String, Object> jsonParameters =
                    UtilCommand.parseRequestJSON(request,"userId");
            userId = (Integer) jsonParameters.get("userId");
            log.trace("Read user id for info: " + userId);
        }
        catch (Exception e) {
            log.error("Can't read correct data from request, because "+ e.getMessage());
        }

        Gson jsonData = new Gson();
        JsonObject innerObject = new JsonObject();
        User userById = UserDAO.findUserById(userId.longValue());
        log.trace("Info for user: " + userById);
        if (userById!=null) {
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("user", new Gson().toJsonTree(userById));

        }
        else {
            innerObject.add("status", new Gson().toJsonTree("ERROR"));
            innerObject.add("message", new Gson().toJsonTree("Cannot find user"));
            log.error("Can't find user by id");
        }

        UtilCommand.sendJSONData(response,innerObject);

    }
}
