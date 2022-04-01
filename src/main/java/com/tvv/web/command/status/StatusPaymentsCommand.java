package com.tvv.web.command.status;


import com.google.gson.Gson;
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
import java.util.Map;

public class StatusPaymentsCommand extends Command {

    private static final Logger log = Logger.getLogger(StatusPaymentsCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        JsonObject innerObject = new JsonObject();
        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("userRole");
        User currentUser = (User) session.getAttribute("currentUser");
        if (userRole!=Role.ADMIN && userRole!=Role.USER)
        {
            response.sendRedirect(request.getContextPath()+ Path.COMMAND__START_PAGE);
            return;
        }

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

        switch (action){
            case "status":
                try {
                    innerObject = PaymentService.changeStatusPayment(paymentById);
                } catch (AppException e) {
                    innerObject = UtilCommand.errorMessageJSON(e.getMessage());
                }
                break;
            case "delete":
                try {
                    innerObject = PaymentService.deletePayment(paymentById);
                } catch (AppException e) {
                    innerObject = UtilCommand.errorMessageJSON(e.getMessage());
                }
                break;
        }

        UtilCommand.sendJSONData(response,innerObject);

    }
}
