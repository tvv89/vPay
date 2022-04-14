package com.tvv.web.command.status;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.AccountService;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.UtilCommand;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatusAccountsCommandTest {

    @Test
    void testExecutePostChange() throws IOException, AppException, ServletException {
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

        String inputString = "{\"action\":\"change\", \"accountId\":1}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));

        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));
        assertJSON.add("userRole", new Gson().toJsonTree(Role.USER));
        Account account = new Account();
        account.setStatus("Enabled");
        assertJSON.add("account", new Gson().toJsonTree(account));

        Map<String,Object> data = new HashMap<>();
        data.put("action", "change");
        data.put("accountId", 1);
        AccountService service = mock(AccountService.class);
        when(service.processChangeStatus(request, data)).thenReturn(assertJSON);

        StatusAccountsCommand status = new StatusAccountsCommand();
        status.setUp(service);
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
        when(session.getAttribute("userRole")).thenReturn(Role.USER);
        User currentUser = mock(User.class);
        when(currentUser.getId()).thenReturn(2L);

        when(session.getAttribute("currentUser")).thenReturn(currentUser);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);

        String inputString = "{\"action\":\"delete\", \"accountId\":1}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));

        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));

        Map<String,Object> data = new HashMap<>();
        data.put("action", "delete");
        data.put("accountId", 1);
        AccountService service = mock(AccountService.class);
        when(service.processDeleteAccount(request, data)).thenReturn(assertJSON);

        StatusAccountsCommand status = new StatusAccountsCommand();
        status.setUp(service);
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

        String inputString = "{\"action\":\"delete\", \"accountId\":1}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));

        JsonObject assertJSON = UtilCommand.errorMessageJSON("Some exception");

        Map<String,Object> data = new HashMap<>();
        data.put("action", "delete");
        data.put("accountId", 1);
        AccountService service = mock(AccountService.class);
        when(service.processDeleteAccount(request, data))
                .thenThrow(new AppException("Some exception", new IllegalArgumentException()));

        StatusAccountsCommand status = new StatusAccountsCommand();
        status.setUp(service);
        status.executePost(request,response);

        String sendData = new Gson().toJson(assertJSON);
        verify(out).print(sendData);

    }
}