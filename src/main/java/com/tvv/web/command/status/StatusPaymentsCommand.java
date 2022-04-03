package com.tvv.web.command.status;

import com.google.gson.JsonObject;
import com.tvv.db.dao.PaymentDAO;
import com.tvv.db.entity.Payment;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.PaymentService;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

/**
 * Update status command for status payment (single page application)
 */
public class StatusPaymentsCommand extends Command {

    private static final Logger log = Logger.getLogger(StatusPaymentsCommand.class);

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
     * single page application. Function can change status payment and delete only for user
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start POST command");

        request.setCharacterEncoding("UTF-8");

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
         * Start JSON parsing request
         */
        JsonObject innerObject = new JsonObject();
        Map<String, Object> jsonParameters = null;
        try {
            jsonParameters = UtilCommand.parseRequestJSON(request);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Bad parsing input data");
        }

        Integer paymentId = null;
        Payment paymentById = null;
        String action = "noAction";

        try {
            action = (String)jsonParameters.get("action");
            paymentId = (Integer)jsonParameters.get("paymentId");
            paymentById = PaymentDAO.findPaymentById(paymentId.longValue());
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
        /**
         * Check payment owner and select action from request
         */
        if (paymentById!=null || currentUser!=null || paymentById.getUser().getId().equals(currentUser.getId())) {
            switch (action) {
                /**
                 * Change payment status and calculate money
                 */
                case "status":
                    try {
                        innerObject = PaymentService.changeStatusPayment(paymentById);
                    } catch (AppException e) {
                        innerObject = UtilCommand.errorMessageJSON(e.getMessage());
                        log.error(e.getMessage());
                    }
                    break;
                /**
                 * Delete payment status (payment will not be deleted, will be marked archive)
                 */
                case "delete":
                    try {
                        innerObject = PaymentService.deletePayment(paymentById);
                    } catch (AppException e) {
                        innerObject = UtilCommand.errorMessageJSON(e.getMessage());
                        log.error(e.getMessage());
                    }
                    break;
            }
        } else {
            innerObject = UtilCommand.errorMessageJSON("Current user is not owner for this payment");
            log.error("Current user is not owner for this payment or payment does not exist");
        }
        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response,innerObject);

        log.trace("End POST command");
    }
}
