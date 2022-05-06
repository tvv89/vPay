package com.tvv.web.command.info;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.AccountDAOImpl;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.command.UtilCommand;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * This command will be used in the future for show account information via send JSON response
 */
public class InfoAccountCommand extends Command {

    private static final Logger log = Logger.getLogger(InfoAccountCommand.class);

    private AccountDAO accountDAO;
    public InfoAccountCommand(){
        accountDAO = new AccountDAOImpl();
    }
    public void setUp(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    /**
     * Function for GET request. This command class don't use GET method, and redirect to list account page
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start load command with method" + request.getMethod());
        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("userRole");
        if (userRole!= Role.ADMIN && userRole!=Role.USER) response.sendRedirect(request.getContextPath() + Path.COMMAND__START_PAGE);
        else {
            request.getSession().setAttribute("currentPage", "accounts");
            RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__LIST_ACCOUNTS);
            disp.forward(request, response);
        }
        log.trace("Forward to: " + Path.PAGE__LIST_ACCOUNTS);
    }

    /**
     * Execute POST function for Controller. This function use JSON data from request, parse it, and send response for
     * single page application. Function can show general information about Account info.
     * This is function will be used in the future
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start POST method "+this.getClass().getSimpleName());
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
            return;
        }

        Integer accountId = null;
        try {
            Map<String, Object> jsonParameters =
                    UtilCommand.parseRequestJSON(request);
            accountId = (Integer) jsonParameters.get("accountId");
            log.trace("Read user id for info: " + accountId);
        }
        catch (Exception e) {
            log.error("Can't read correct data from request, because "+ e.getMessage());
        }

        try {
            /**
             * will add checking for owner
             */
            Account accountById = accountDAO.findAccountById(accountId.longValue());
            log.trace("Info for user: " + accountById);
            accountById.getOwnerUser().setPassword("");
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

        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response,innerObject);
        log.trace("End POST method "+this.getClass().getSimpleName());
    }
}
