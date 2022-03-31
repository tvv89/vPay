package com.tvv.web.command.info;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAO;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.command.UtilCommand;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class InfoPaymentCommand extends Command {

    private static final Logger log = Logger.getLogger(InfoPaymentCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UtilCommand.bedGETRequest(request,response);
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        JsonObject innerObject = new JsonObject();
        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("userRole");
        User currentUser = (User) session.getAttribute("currentUser");

        Integer accountId = null;
        try {
            Map<String, Object> jsonParameters =
                    UtilCommand.parseRequestJSON(request,"accountId");
            accountId = (Integer) jsonParameters.get("accountId");
            log.trace("Read user id for info: " + accountId);
        }
        catch (Exception e) {
            log.error("Can't read correct data from request, because "+ e.getMessage());
        }

        try {
            Account accountById = AccountDAO.findAccountById(accountId.longValue());
            log.trace("Info for user: " + accountById);
            if (accountById != null) {
                innerObject.add("status", new Gson().toJsonTree("OK"));
                innerObject.add("account", new Gson().toJsonTree(accountById));

            } else {
                innerObject = UtilCommand.errorMessageJSON("Cannot find account");
                log.error("Can't find user by id");
            }
        }
        catch (AppException ex)
        {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
            log.error(ex.getMessage());
        }

        if (userRole!=Role.ADMIN && userRole!=Role.USER) response.sendRedirect(request.getContextPath()+ Path.COMMAND__START_PAGE);
        else UtilCommand.sendJSONData(response,innerObject);

    }
}
