package com.tvv.web.command.create;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.PaymentDAO;
import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.*;
import com.tvv.service.AccountService;
import com.tvv.service.CardService;
import com.tvv.service.PaymentService;
import com.tvv.service.UserService;
import com.tvv.service.exception.AppException;
import com.tvv.utils.FieldsChecker;
import com.tvv.utils.SystemParameters;
import com.tvv.utils.UtilsGenerator;
import com.tvv.web.command.Command;
import com.tvv.web.command.UtilCommand;
import com.tvv.web.webutil.ErrorMessageEN;
import com.tvv.web.webutil.ErrorString;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Command for create card. Card can be created only by USER role
 */
public class CreatePaymentCommand extends Command {

    private static final Logger log = Logger.getLogger(CreatePaymentCommand.class);

    private PaymentService service;
    private AccountDAO accountDAO;
    private UserDAO userDAO;

    private void init() {
        userDAO = new UserDAO();
        accountDAO = new AccountDAO();
        service = new PaymentService(new AccountService(accountDAO),
                accountDAO,
                new PaymentDAO());

    }
    /**
     * Execute POST function for Controller. This function use JSON data from request, parse it, and send response to
     * page. Create payment for current user and redirect to payment list
     * User can create payment with one of types recipient: card and account
     * Payment status can be ready or submitted. Ready status don't transfer money.
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executePost(HttpServletRequest request,
                            HttpServletResponse response) throws IOException, ServletException {
        log.debug("Start create account POST command " + this.getClass().getSimpleName());
        init();

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

        /**
         * Parse parameter for create payment
         */
        String action = "noAction";
        JsonObject innerObject = new JsonObject();
        Map<String, Object> jsonParameters = new HashMap<>();
        try {
            jsonParameters = UtilCommand.parseRequestJSON(request);
            log.debug("Parse parameter: "+jsonParameters);
            action = (String) jsonParameters.get("action");
            log.trace("Read command action payment: " + action);
        } catch (Exception e) {
            log.error("Can't read correct data from request, because " + e.getMessage());
        }
        try {
            switch (action) {
                /**
                 * Show information about sender account
                 */
                case "selectAccount":
                    Integer accountId = -1;
                    accountId = (Integer) jsonParameters.get("accountId");
                    Account accountById = accountDAO.findAccountById(accountId.longValue());
                    log.trace("Info for account: " + accountById);
                    if (accountById != null) {
                        innerObject.add("status", new Gson().toJsonTree("OK"));
                        innerObject.add("account", new Gson().toJsonTree(accountById));

                    } else {
                        innerObject = UtilCommand.errorMessageJSON("Can't find account");
                        log.error("Can't find account by id");
                    }
                    break;
                /**
                 * Check Name (and hide) of user for recipient account
                 */
                case "checkAccount":
                    String accountNumber = (String) jsonParameters.get("accountNumber");
                    Map<String, String> user = userDAO.findUserByAccountUID(accountNumber);
                    Account accountByUID = accountDAO.findAccountByUID(accountNumber);
                    log.trace("Info for user: " + user.get("firstName") + " " + user.get("lastName"));
                    if (!user.isEmpty()) {
                        innerObject.add("status", new Gson().toJsonTree("OK"));
                        String hideUser = UserService.hideUserName(user.get("firstName"), user.get("lastName"));
                        innerObject.add("name", new Gson().toJsonTree(hideUser));
                        innerObject.add("currency", new Gson().toJsonTree(accountByUID.getCurrency()));
                    } else {
                        innerObject.add("status", new Gson().toJsonTree("OK"));
                        innerObject.add("name", new Gson().toJsonTree("Unknown user"));
                        innerObject.add("currency", new Gson().toJsonTree("USD"));
                        log.error("Can't find account by id");
                    }
                    break;
                /**
                 * Calculate payment
                 */
                case "calculatePayment":
                    innerObject = service.calculatePayment(jsonParameters);
                    break;
                /**
                 * Create payment
                 */
                case "createPayment":
                    innerObject = service.createPayment(jsonParameters, currentUser);
                    break;
            }
        } catch (AppException ex) {
            innerObject = UtilCommand.errorMessageJSON(ex.getMessage());
            log.error(ex.getMessage());
        }

        /**
         * send JSON data
         */
        UtilCommand.sendJSONData(response, innerObject);

        log.debug("Finish create account POST command " + this.getClass().getSimpleName());
    }

    /**
     * Load page "Create payment" with parameter: list of current user account
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start load command with method" + request.getMethod());
        /**
         * Check user role
         */
        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("userRole");
        User currentUser = (User) session.getAttribute("currentUser");
        if (userRole != Role.ADMIN && userRole != Role.USER) {
            response.sendRedirect(request.getContextPath() + Path.COMMAND__START_PAGE);
            return;
        }
        List<Account> list = null;
        if (userRole == Role.USER) {
            try {
                list = accountDAO.findAccountByUserId(currentUser.getId());
                request.setAttribute("accountsPayment", list);
            } catch (AppException e) {
                UtilCommand.bedGETRequest(request, response);
                log.error(e.getMessage());
                return;
            }
        }

        RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__CREATE_PAYMENT);
        disp.forward(request, response);

        log.trace("Forward to: " + Path.PAGE__CREATE_PAYMENT);
    }


}