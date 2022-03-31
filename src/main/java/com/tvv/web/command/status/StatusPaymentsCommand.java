package com.tvv.web.command.status;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.PaymentDAO;
import com.tvv.db.entity.Payment;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.command.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        Map<String, Object> jsonParameters =
                null;
        try {
            jsonParameters = UtilCommand.parseRequestJSON(request,"paymentId");
        } catch (AppException e) {
            e.printStackTrace();
        }
        Integer paymentId = null;
        Payment paymentById = null;

        try {
            paymentId = (Integer)jsonParameters.get("paymentId");
            paymentById = PaymentDAO.findPaymentById(paymentId.longValue());
        }
        catch (Exception e) {
            System.out.println(e);
        }

        JsonObject innerObject = new JsonObject();
        if (paymentById!=null) {
            String newStatus = paymentById.getStatus()=="1" ? "1" : "2";
            PaymentDAO.updateStatusPaymentById(Long.valueOf(paymentId),newStatus);
            paymentById = PaymentDAO.findPaymentById(paymentId.longValue());
            innerObject.add("status", new Gson().toJsonTree("OK"));
            innerObject.add("account", new Gson().toJsonTree(paymentById));
        }
        else {
            innerObject.add("status", new Gson().toJsonTree("ERROR"));
            innerObject.add("message", new Gson().toJsonTree("Cannot change account status"));

        }
        UtilCommand.sendJSONData(response,innerObject);



    }
}
