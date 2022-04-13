package com.tvv.web.command.info;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.PaymentDAO;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Payment;
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
 * This command will be used in the future for show account information via send JSON response
 */
public class InfoPaymentCommand extends Command {

    private static final Logger log = Logger.getLogger(InfoPaymentCommand.class);

    private PaymentDAO paymentDAO;

    public InfoPaymentCommand() {
        this.paymentDAO = new PaymentDAO();
    }

    public void init(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }
    /**
     * Function for GET request. This command class don't use GET method, and redirect to block page
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
     * single page application. Function can show general information about Payment info.
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

        Integer paymentId = null;
        try {
            Map<String, Object> jsonParameters =
                    UtilCommand.parseRequestJSON(request);
            paymentId = (Integer) jsonParameters.get("paymentId");
            log.trace("Read payment id for info: " + paymentId);
        }
        catch (Exception e) {
            log.error("Can't read correct data from request, because "+ e.getMessage());
        }

        /**
         * Find payment and send JSON data
         */
        try {
            Payment paymentById = paymentDAO.findPaymentById(paymentId.longValue());
            log.trace("Info for payment: " + paymentById);
            if (paymentById != null) {
                innerObject.add("status", new Gson().toJsonTree("OK"));
                innerObject.add("payment", new Gson().toJsonTree(paymentById));

            } else {
                innerObject = UtilCommand.errorMessageJSON("Cannot find payment");
                log.error("Can't find payment by id");
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
