package com.tvv.web.command.info;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.PaymentDAO;
import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Payment;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.PaymentService;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.create.CreatePaymentCommand;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InfoPaymentCommandTest {

    @Test
    void testExecutePost() throws AppException, IOException, ServletException {
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userRole")).thenReturn(Role.USER);
        User user = mock(User.class);
        when(session.getAttribute("currentUser")).thenReturn(user);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);

        String inputString = "{\"paymentId\":1}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));

        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        PaymentDAO paymentDAO = mock(PaymentDAO.class);
        Payment paymentById = new Payment();
        when(paymentDAO.findPaymentById(1L))
                .thenReturn(paymentById);

        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));
        assertJSON.add("payment", new Gson().toJsonTree(paymentById));

        InfoPaymentCommand command = new InfoPaymentCommand();
        command.init(paymentDAO);
        command.executePost(request,response);

        String sendData = new Gson().toJson(assertJSON);
        verify(out).print(sendData);
    }
}