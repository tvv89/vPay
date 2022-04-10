package com.tvv.web.servlet;

import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControllerTest {

    @Test
    void testDoGetBadPasswordTest() throws ServletException, IOException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("controller")).thenReturn("login");
        when(request.getParameter("login")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("admin");

        when(request.getContextPath()).thenReturn("");

        new Controller().doGet(request,response);

        verify(response,times(1)).sendRedirect("/error.page.jsp");
    }

    @Test
    void testDoGetBadLoginTest() throws ServletException, IOException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("controller")).thenReturn("login");
        when(request.getParameter("login")).thenReturn("admin1");
        when(request.getParameter("password")).thenReturn("admin");

        when(request.getContextPath()).thenReturn("");

        new Controller().doGet(request,response);

        verify(response,times(1)).sendRedirect("/error.page.jsp");
    }

    @Test
    void testDoPost() throws ServletException, IOException {


        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("controller")).thenReturn("login");
        when(request.getParameter("login")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("admin");

        when(request.getContextPath()).thenReturn("");
        //when(session.getAttribute("errorHeader")).thenReturn("404");
        //when(session.getAttribute("errorMessage")).thenReturn("Bad password");

        new Controller().doPost(request,response);

        verify(response,times(1)).sendRedirect("/error.page.jsp");
    }
}