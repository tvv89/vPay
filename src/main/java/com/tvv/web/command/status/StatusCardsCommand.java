package com.tvv.web.command.status;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.CardDAO;
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

/**
 * Status cards command change status card from single page
 */
public class StatusCardsCommand extends Command {

    private static final Logger log = Logger.getLogger(StatusCardsCommand.class);

    private CardDAO cardDAO;

    public StatusCardsCommand() {
        this.cardDAO = new CardDAO();
    }

    public void setUp(CardDAO cardDAO){
        this.cardDAO = cardDAO;
    }
    /**
     * Execute GET function for Controller. This function doesn't have GET request, and redirect to error page
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UtilCommand.bedGETRequest(request,response);
    }

    /**
     * Execute POST function for Controller. This function use JSON data from request, parse it, and send response for
     * single page application. Function has list only for user role
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start POST command");
        JsonObject innerObject = new JsonObject();
        /**
         * Check user role
         */
        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("userRole");
        User currentUser = (User) session.getAttribute("currentUser");
        if (userRole!=Role.ADMIN && userRole!=Role.USER)
        {
            response.sendRedirect(request.getContextPath()+ Path.COMMAND__START_PAGE);
            log.debug("User role is not correct");
            return;
        }
        /**
         * Start JSON parsing request
         */
        Map<String, Object> jsonParameters = null;
        try {
            jsonParameters = UtilCommand.parseRequestJSON(request);
        } catch (AppException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        Integer cardId = null;
        Card cardIdById = null;

        try {
            cardId = (Integer)jsonParameters.get("cardId");
            cardIdById = cardDAO.findCardById(cardId.longValue());
            log.debug("Find card: "+ cardIdById.toString());
        }
        catch (Exception e) {
            innerObject = UtilCommand.errorMessageJSON(e.getMessage());
            log.debug(e.getMessage());
        }
        /**
         * Check card and check owner user
         */
        try {
            if (cardIdById != null && cardIdById.getUser().getId().equals(currentUser.getId())) {
                int newStatus = cardIdById.getStatus() ? 0 : 1;
                cardDAO.updateStatusCardById(Long.valueOf(cardId), newStatus);
                cardIdById = cardDAO.findCardById(cardId.longValue());
                innerObject.add("status", new Gson().toJsonTree("OK"));
                cardIdById.getUser().setPassword("");
                innerObject.add("card", new Gson().toJsonTree(cardIdById));
                log.debug("Create JSON data for Card status");
            } else {
                innerObject.add("status", new Gson().toJsonTree("ERROR"));
                innerObject.add("message", new Gson().toJsonTree("Cannot change card status"));
                log.error("Cannot change card status");
            }
        }
        catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
            log.error(ex.getMessage());
        }
        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response,innerObject);

        log.trace("End POST command");
    }
}
