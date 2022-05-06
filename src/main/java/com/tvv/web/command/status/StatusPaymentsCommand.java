package com.tvv.web.command.status;

import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAOImpl;
import com.tvv.db.dao.PaymentDAO;
import com.tvv.db.dao.PaymentDAOImpl;
import com.tvv.db.entity.Payment;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.AccountService;
import com.tvv.service.PaymentService;
import com.tvv.service.exception.AppException;
import com.tvv.utils.PDFCreator;
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
 * Update status command for status payment (single page application)
 */
public class StatusPaymentsCommand extends Command {

    private static final Logger log = Logger.getLogger(StatusPaymentsCommand.class);

    private PaymentDAO paymentDAO;
    private PaymentService service;

    public StatusPaymentsCommand() {
        paymentDAO = new PaymentDAOImpl();
        service = new PaymentService(new AccountService(new AccountDAOImpl()), new AccountDAOImpl(), paymentDAO);
    }

    public void setUp(PaymentDAO pDAO, PaymentService pService) {
        this.paymentDAO = pDAO;
        this.service = pService;
    }

    /**
     * Execute GET function for Controller. This function doesn't have GET request, and redirect to error page
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UtilCommand.bedGETRequest(request, response);
    }

    /**
     * Execute POST function for Controller. This function use JSON data from request, parse it, and send response for
     * single page application. Function can change status payment and delete only for user
     *
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
        String currentLanguage = (String) session.getAttribute("currentLanguage");
        if (userRole != Role.ADMIN && userRole != Role.USER) {
            response.sendRedirect(request.getContextPath() + Path.COMMAND__START_PAGE);
            return;
        }

        /**
         * Start JSON parsing request
         */
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
            action = (String) jsonParameters.get("action");
            paymentId = (Integer) jsonParameters.get("paymentId");
            paymentById = paymentDAO.findPaymentById(paymentId.longValue());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        /**
         * Check payment owner and select action from request
         */
        if (paymentById != null || currentUser != null || paymentById.getUser().getId().equals(currentUser.getId())) {
            switch (action) {
                /**
                 * Change payment status and calculate money
                 */
                case "status":
                    try {
                        if (userRole == Role.ADMIN) innerObject =
                                UtilCommand.errorMessageJSON("Current user doesn't have rules for this action");
                        else innerObject = service.changeStatusPayment(paymentById);
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
                        innerObject = service.deletePayment(paymentById);
                    } catch (AppException e) {
                        innerObject = UtilCommand.errorMessageJSON(e.getMessage());
                        log.error(e.getMessage());
                    }
                    break;
                /**
                 * Save payment to PDF
                 */
                case "pdf":
                    if (!"Submitted".equals(paymentById.getStatus())) {
                        innerObject = UtilCommand.errorMessageJSON("Payment is not submitted");
                        break;
                    } else {
                        UtilCommand.sendPDFData(response,
                                PDFCreator.createPDF(paymentById, request.getServletContext().getRealPath("fonts"), currentLanguage));
                        return;
                    }
            }
        } else {
            innerObject = UtilCommand.errorMessageJSON("Current user is not owner for this payment");
            log.error("Current user is not owner for this payment or payment does not exist");
        }
        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response, innerObject);

        log.trace("End POST command");
    }
}
