package com.tvv.web.command.load;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.PaymentDAO;
import com.tvv.db.entity.Payment;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.web.command.info.InfoPaymentCommand;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoadListCardsCommandTest {

    @Test
    void testExecuteGetUser() throws ServletException, IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userRole")).thenReturn(Role.USER);
        User user = mock(User.class);
        when(session.getAttribute("currentUser")).thenReturn(user);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);


        RequestDispatcher disp = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/list.cards.jsp")).thenReturn(disp);

        new LoadListCardsCommand().executeGet(request,response);
        verify(request).getRequestDispatcher("/list.cards.jsp");

    }

    @Test
    void testExecuteGetNoUser() throws ServletException, IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userRole")).thenReturn(null);
        User user = mock(User.class);
        when(session.getAttribute("currentUser")).thenReturn(user);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("contextPath");

        new LoadListCardsCommand().executeGet(request,response);
        verify(response).sendRedirect("contextPath");

    }
}