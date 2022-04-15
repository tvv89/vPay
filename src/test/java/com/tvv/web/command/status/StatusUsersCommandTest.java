package com.tvv.web.command.status;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.PaymentDAO;
import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.Payment;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.PaymentService;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.UtilCommand;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatusUsersCommandTest {

    @Test
    void testExecutePostCorrect() throws IOException, AppException, ServletException {
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

        String inputString = "{\"action\":\"status\", \"userId\":1}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));

        JsonObject assertJSON = new JsonObject();

        UserDAO userDAO = mock(UserDAO.class);
        User userById1 = new User();
        userById1.setId(1L);
        userById1.setStatus(true);
        User userById2 = new User();
        userById2.setId(1L);
        userById2.setStatus(false);
        when(userDAO.findUserById(1L)).thenReturn(userById1).thenReturn(userById2);
        when(userDAO.updateStatusUserById(1L,0)).thenReturn(true);

        assertJSON.add("status", new Gson().toJsonTree("OK"));
        assertJSON.add("user", new Gson().toJsonTree(userById2));

        StatusUsersCommand status = new StatusUsersCommand();

        status.setUp(userDAO);
        status.executePost(request,response);

        String sendData = new Gson().toJson(assertJSON);
        verify(out).print(sendData);
    }

    @Test
    void testExecutePostNoFoundUser() throws IOException, AppException, ServletException {
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

        String inputString = "{\"action\":\"status\", \"userId\":5}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));

        JsonObject assertJSON = UtilCommand.errorMessageJSON("Cannot change user status");

        UserDAO userDAO = mock(UserDAO.class);
        User userById1 = null;
        when(userDAO.findUserById(5L)).thenReturn(userById1);
        when(userDAO.updateStatusUserById(5L,0)).thenReturn(true);

        StatusUsersCommand status = new StatusUsersCommand();

        status.setUp(userDAO);
        status.executePost(request,response);

        String sendData = new Gson().toJson(assertJSON);
        verify(out).print(sendData);
    }

    @Test
    void testExecutePostException() throws IOException, AppException, ServletException {
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

        String inputString = "{\"action\":\"status\", \"userId\":5}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));

        JsonObject assertJSON = UtilCommand.errorMessageJSON("Exception message");

        UserDAO userDAO = mock(UserDAO.class);
        User userById1 = new User();
        userById1.setStatus(true);
        when(userDAO.findUserById(5L)).thenReturn(userById1);
        when(userDAO.updateStatusUserById(5L,0))
                .thenThrow(new AppException("Exception message", new IllegalArgumentException()));

        StatusUsersCommand status = new StatusUsersCommand();

        status.setUp(userDAO);
        status.executePost(request,response);

        String sendData = new Gson().toJson(assertJSON);
        verify(out).print(sendData);
    }
}