package com.tvv.web.command.info;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.CardDAO;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Card;
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

public class InfoCardCommand extends Command {

    private static final Logger log = Logger.getLogger(InfoCardCommand.class);

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
                    UtilCommand.parseRequestJSON(request);
            accountId = (Integer) jsonParameters.get("accountId");
            String action = (String) jsonParameters.get("action");
            log.trace("Read account id for card info: " + accountId);
            if ("info".equals(action)) {
                innerObject = processCardInfo(accountId);
            }
            if ("select".equals(action)) {
                Integer cardId = Integer.parseInt((String) jsonParameters.get("card"));
                innerObject = processCardSelect(accountId, cardId);
            }
        }
        catch (Exception e) {
            log.error("Can't read correct data from request, because "+ e.getMessage());
            innerObject = UtilCommand.errorMessageJSON(e.getMessage());
        }

        if (userRole!=Role.ADMIN && userRole!=Role.USER) response.sendRedirect(request.getContextPath()+ Path.COMMAND__START_PAGE);
        else UtilCommand.sendJSONData(response,innerObject);

    }

    private JsonObject processCardInfo(Integer accountId) throws AppException {
        JsonObject innerObject = new JsonObject();
        Account accountById = AccountDAO.findAccountById(Long.valueOf(accountId));
        log.trace("Info for account: " + accountById);
        if (accountById.getCard()!=null)
        {
            Card cardById = CardDAO.findCardById(accountById.getCard().getId());
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("cardname", new Gson().toJsonTree(cardById.getName()));
            innerObject.add("cardnumber", new Gson().toJsonTree(cardById.getNumber()));
            innerObject.add("expdate", new Gson().toJsonTree(cardById.getExpDate()));
        }
        else {
            innerObject.add("status", new Gson().toJsonTree("ERROR"));
            innerObject.add("message", new Gson().toJsonTree("Account does not have card"));
            log.error("Account does not have card");
        }
        return innerObject;
    }

    private JsonObject processCardSelect(Integer accountId, Integer cardId) throws AppException {
        JsonObject innerObject = new JsonObject();
        Account accountById = AccountDAO.findAccountById(Long.valueOf(accountId));

        log.trace("Info for account: " + accountById);
        if (accountById!=null)
        {
            AccountDAO.updateAccountCard(Long.valueOf(accountId),cardId);
            innerObject.add("status", new Gson().toJsonTree("OK"));

        }
        else {
            innerObject.add("status", new Gson().toJsonTree("ERROR"));
            innerObject.add("message", new Gson().toJsonTree("Account does not find"));
            log.error("Account does not have card");
        }
        return innerObject;
    }
}
