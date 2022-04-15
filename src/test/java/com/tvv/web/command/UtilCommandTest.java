package com.tvv.web.command;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mysql.cj.util.Util;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.servlet.GenericFilter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtilCommandTest {
    private HttpServletResponse response;
    private HttpServletRequest request;
    private HttpSession session;
    private PrintWriter out;
    private User currentUser;

    @BeforeEach
    private void init() throws IOException {
        response = mock(HttpServletResponse.class);

        session = mock(HttpSession.class);

        request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void testCurrentUser() throws IOException {
        User user = new User();
        when(session.getAttribute("currentUser")).thenReturn(user);
        assertEquals(user, UtilCommand.currentUser(request,response));
    }

    @Test
    void testSendJSONData() throws IOException {
        String inputString = "{\"action\":\"status\", \"userId\":1}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));

        JsonObject innerObject = new JsonObject();
        innerObject.add("status", new Gson().toJsonTree("OK"));

        out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);
        UtilCommand.sendJSONData(response,innerObject);
        String sendJSON = innerObject.toString();
        verify(out).print(sendJSON);

    }

    @Test
    void testParseRequestJSON() throws IOException, AppException {
        String inputString = "{\"action\":\"status\", \"userId\":1}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));
        Map<String, Object> asserMap = new HashMap<>();
        asserMap.put("action", "status");
        asserMap.put("userId",1);

        assertEquals(asserMap, UtilCommand.parseRequestJSON(request));

    }

    @Test
    void testParseRequestJSONException() throws IOException, AppException {
        String inputString = "\"action\":\"status\", \"userId\":1}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));

        assertThrows(AppException.class, ()-> {
            Map <String, Object> map = UtilCommand.parseRequestJSON(request);});

    }

    @Test
    void testBedGETRequest() throws ServletException, IOException {
        when(request.getMethod()).thenReturn("POST");
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/page.access.denied.jsp")).thenReturn(rd);
        UtilCommand.bedGETRequest(request,response);
        verify(rd).forward(request,response);

    }

}