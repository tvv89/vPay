package com.tvv.web.command.status;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.PaymentDAO;
import com.tvv.db.entity.Payment;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.AccountService;
import com.tvv.service.PaymentService;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.UtilCommand;
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

class StatusPaymentsCommandTest {

    @Test
    void testExecutePostStatusUser() throws IOException, AppException, ServletException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userRole")).thenReturn(Role.USER);
        User currentUser = mock(User.class);
        when(currentUser.getId()).thenReturn(2L);

        when(session.getAttribute("currentUser")).thenReturn(currentUser);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);

        String inputString = "{\"action\":\"status\", \"paymentId\":1}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));

        JsonObject assertJSON = new JsonObject();

        PaymentDAO paymentDAO = mock(PaymentDAO.class);
        Payment payment = new Payment();
        payment.setId(1L);
        User user = new User();
        user.setId(2L);
        payment.setUser(user);
        when(paymentDAO.findPaymentById(1L)).thenReturn(payment);
        PaymentService service = mock(PaymentService.class);
        when(service.changeStatusPayment(payment))
                .thenReturn(assertJSON);

        StatusPaymentsCommand status = new StatusPaymentsCommand();

        status.setUp(paymentDAO,service);
        status.executePost(request,response);

        String sendData = new Gson().toJson(assertJSON);
        verify(out).print(sendData);
    }

    @Test
    void testExecutePostStatusAdmin() throws IOException, AppException, ServletException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userRole")).thenReturn(Role.ADMIN);
        User currentUser = mock(User.class);
        when(currentUser.getId()).thenReturn(2L);

        when(session.getAttribute("currentUser")).thenReturn(currentUser);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);

        String inputString = "{\"action\":\"status\", \"paymentId\":1}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));

        JsonObject assertJSON = new JsonObject();
        assertJSON = UtilCommand.errorMessageJSON("Current user doesn't have rules for this action");
        PaymentDAO paymentDAO = mock(PaymentDAO.class);
        Payment payment = new Payment();
        payment.setId(1L);
        User user = new User();
        user.setId(2L);
        payment.setUser(user);
        when(paymentDAO.findPaymentById(1L)).thenReturn(payment);
        PaymentService service = mock(PaymentService.class);
        when(service.changeStatusPayment(payment))
                .thenReturn(assertJSON);

        StatusPaymentsCommand status = new StatusPaymentsCommand();

        status.setUp(paymentDAO,service);
        status.executePost(request,response);

        String sendData = new Gson().toJson(assertJSON);
        verify(out).print(sendData);
    }

    @Test
    void testExecutePostDelete() throws IOException, AppException, ServletException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userRole")).thenReturn(Role.ADMIN);
        User currentUser = mock(User.class);
        when(currentUser.getId()).thenReturn(2L);

        when(session.getAttribute("currentUser")).thenReturn(currentUser);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);

        String inputString = "{\"action\":\"delete\", \"paymentId\":1}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));

        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));

        PaymentDAO paymentDAO = mock(PaymentDAO.class);
        Payment payment = new Payment();
        payment.setId(1L);
        User user = new User();
        user.setId(2L);
        payment.setUser(user);
        when(paymentDAO.findPaymentById(1L)).thenReturn(payment);
        PaymentService service = mock(PaymentService.class);
        when(service.deletePayment(payment))
                .thenReturn(assertJSON);

        StatusPaymentsCommand status = new StatusPaymentsCommand();

        status.setUp(paymentDAO,service);
        status.executePost(request,response);

        String sendData = new Gson().toJson(assertJSON);
        verify(out).print(sendData);
    }

    @Test
    void testExecutePostDeleteException() throws IOException, AppException, ServletException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userRole")).thenReturn(Role.ADMIN);
        User currentUser = mock(User.class);
        when(currentUser.getId()).thenReturn(2L);

        when(session.getAttribute("currentUser")).thenReturn(currentUser);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);

        String inputString = "{\"action\":\"delete\", \"paymentId\":1}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));

        JsonObject assertJSON = UtilCommand.errorMessageJSON("Error message");

        PaymentDAO paymentDAO = mock(PaymentDAO.class);
        Payment payment = new Payment();
        payment.setId(1L);
        User user = new User();
        user.setId(2L);
        payment.setUser(user);
        when(paymentDAO.findPaymentById(1L)).thenReturn(payment);
        PaymentService service = mock(PaymentService.class);
        when(service.deletePayment(payment))
                .thenThrow(new AppException("Error message", new IllegalArgumentException()));

        StatusPaymentsCommand status = new StatusPaymentsCommand();

        status.setUp(paymentDAO,service);
        status.executePost(request,response);

        String sendData = new Gson().toJson(assertJSON);
        verify(out).print(sendData);
    }
}