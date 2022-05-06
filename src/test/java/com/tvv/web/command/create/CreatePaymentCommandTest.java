package com.tvv.web.command.create;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAOImpl;
import com.tvv.db.dao.PaymentDAOImpl;
import com.tvv.db.dao.UserDAOImpl;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class CreatePaymentCommandTest {

    @Test
    void testExecutePostUser() throws IOException, AppException, ServletException {

        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userRole")).thenReturn(Role.USER);
        User user = mock(User.class);
        when(session.getAttribute("currentUser")).thenReturn(user);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        String inputString = "{\"action\":\"checkAccount\",\"accountType\":\"Account\",\"accountNumber\":\"ouzgJ73o1e6UYd4WpZB2wtEMj\"}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));
        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        Map<String, String> userMap = new HashMap<>();
        userMap.put("firstName", "Володимир");
        userMap.put("lastName","Тимчук");
        userMap.put("id", "41");

        UserDAOImpl userDAO = mock(UserDAOImpl.class);
        when(userDAO.findUserByAccountUID("ouzgJ73o1e6UYd4WpZB2wtEMj")).thenReturn(userMap);

        AccountDAOImpl accountDAO = mock(AccountDAOImpl.class);
        Account accountByUID = mock(Account.class);
        when(accountDAO.findAccountByUID("ouzgJ73o1e6UYd4WpZB2wtEMj"))
                .thenReturn(accountByUID);
        when(accountByUID.getCurrency()).thenReturn("USD");

        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));
        assertJSON.add("name", new Gson().toJsonTree("ВОЛО***** ***ЧУК "));
        assertJSON.add("currency", new Gson().toJsonTree("USD"));

        CreatePaymentCommand command = new CreatePaymentCommand();
        command.setUp(accountDAO,new PaymentDAOImpl(),userDAO);
        command.executePost(request,response);

        String sendData = new Gson().toJson(assertJSON);
        verify(out).print(sendData);

    }

    @Test
    void testExecutePostUserUnknown() throws IOException, AppException, ServletException {

        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userRole")).thenReturn(Role.USER);
        User user = mock(User.class);
        when(session.getAttribute("currentUser")).thenReturn(user);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        String inputString = "{\"action\":\"checkAccount\",\"accountType\":\"Account\",\"accountNumber\":\"ouzgJ73o1e6UYd4WpZB2wtEMj\"}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));
        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        Map<String, String> userMap = new HashMap<>();

        UserDAOImpl userDAO = mock(UserDAOImpl.class);
        when(userDAO.findUserByAccountUID("ouzgJ73o1e6UYd4WpZB2wtEMj")).thenReturn(userMap);

        AccountDAOImpl accountDAO = mock(AccountDAOImpl.class);
        Account accountByUID = mock(Account.class);
        when(accountDAO.findAccountByUID("ouzgJ73o1e6UYd4WpZB2wtEMj"))
                .thenReturn(accountByUID);
        when(accountByUID.getCurrency()).thenReturn("USD");

        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));
        assertJSON.add("name", new Gson().toJsonTree("Unknown user"));
        assertJSON.add("currency", new Gson().toJsonTree("USD"));

        CreatePaymentCommand command = new CreatePaymentCommand();
        command.setUp(accountDAO,new PaymentDAOImpl(),userDAO);
        command.executePost(request,response);

        String sendData = new Gson().toJson(assertJSON);
        verify(out).print(sendData);

    }
}