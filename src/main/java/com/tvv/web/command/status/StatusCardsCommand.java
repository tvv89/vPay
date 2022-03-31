package com.tvv.web.command.status;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.CardDAO;
import com.tvv.db.entity.Card;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.command.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class StatusCardsCommand extends Command {

    private static final Logger log = Logger.getLogger(StatusCardsCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
        Map<String, Object> jsonParameters =
                null;
        try {
            jsonParameters = UtilCommand.parseRequestJSON(request,"cardId");
        } catch (AppException e) {
            e.printStackTrace();
        }
        Integer cardId = null;
        Card cardIdById = null;

        try {
            cardId = (Integer)jsonParameters.get("cardId");
            cardIdById = CardDAO.findCardById(cardId.longValue());
        }
        catch (Exception e) {
            System.out.println(e);
        }

        JsonObject innerObject = new JsonObject();

        if (cardIdById!=null) {
            int newStatus = cardIdById.getStatus() ? 0 : 1;
            CardDAO.updateStatusCardById(Long.valueOf(cardId),newStatus);
            cardIdById = CardDAO.findCardById(cardId.longValue());
            innerObject.add("status", new Gson().toJsonTree("OK"));
            cardIdById.getUser().setPassword("");
            innerObject.add("card", new Gson().toJsonTree(cardIdById));
        }
        else {
            innerObject.add("status", new Gson().toJsonTree("ERROR"));
            innerObject.add("message", new Gson().toJsonTree("Cannot change card status"));

        }
        UtilCommand.sendJSONData(response,innerObject);



    }
}
