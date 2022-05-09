package com.tvv.web.command.update;


import com.google.gson.JsonObject;
import com.tvv.service.AccountService;
import com.tvv.web.command.Command;
import com.tvv.web.command.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Update account balance command for single page application
 */
public class UpdateAccountBalanceCommand extends Command {

    private static final Logger log = Logger.getLogger(UpdateAccountBalanceCommand.class);

    private AccountService accountService;
    public UpdateAccountBalanceCommand() {
        accountService = new AccountService();
    }
    public void setUp(AccountService accountService) {
        this.accountService = accountService;
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
        JsonObject innerObject = new JsonObject();
        innerObject = accountService.getUpdateAccountJsonObject(request, response, innerObject);
        if (innerObject == null) return;

        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response,innerObject);
        log.trace("End POST command "+ this.getClass().getName());

    }

}
