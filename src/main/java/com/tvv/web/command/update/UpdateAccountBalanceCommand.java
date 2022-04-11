package com.tvv.web.command.update;


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

/**
 * Update account balance command for single page application
 */
public class UpdateAccountBalanceCommand extends Command {

    private static final Logger log = Logger.getLogger(UpdateAccountBalanceCommand.class);

    private AccountDAO accountDAO;
    private void init() {
        accountDAO = new AccountDAO();
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
     * single page application. Function has access for USER role
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start POST command "+ this.getClass().getSimpleName());
        init();
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
        }
        Integer accountId = null;
        Double accountCoin = null;
        Account accountById = null;

        /**
         * Find account for change balance
         */
        try {
            accountId = (Integer)jsonParameters.get("accountId");
            accountCoin = Double.parseDouble(jsonParameters.get("coin").toString());
            accountById = accountDAO.findAccountById(accountId.longValue());
        }
        catch (Exception e) {
            log.error("Bad input value");
        }

        /**
         * Create response with JSON files
         */
        try {
            if (accountById != null && userRole == Role.USER && accountById.getOwnerUser().getId().equals(currentUser.getId())) {
                if (accountById.getCard() != null) {
                    if (accountById.getCard().getStatus()) {
                        accountDAO.updateAccountBalance(accountById.getId(), accountById.getBalance() + accountCoin);
                        accountById = accountDAO.findAccountById(accountId.longValue());
                        innerObject.add("status", new Gson().toJsonTree("OK"));
                        innerObject.add("account", new Gson().toJsonTree(accountById));
                    } else innerObject = UtilCommand.errorMessageJSON("Card is locked. Please select another card.");
                } else {
                    innerObject = UtilCommand.errorMessageJSON("Account doesn't have card. Please add card before top up balance.");
                }
            } else {
                innerObject = UtilCommand.errorMessageJSON("Cannot change account balance");
            }
        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
        }

        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response,innerObject);
        log.trace("End POST command "+ this.getClass().getName());

    }
}
