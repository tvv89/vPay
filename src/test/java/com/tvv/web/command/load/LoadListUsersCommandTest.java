package com.tvv.web.command.load;

import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoadListUsersCommandTest {

    @Test
    void testExecuteGetUserAdmin() throws ServletException, IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userRole")).thenReturn(Role.ADMIN);
        User user = mock(User.class);
        when(session.getAttribute("currentUser")).thenReturn(user);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);

        RequestDispatcher disp = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/list.users.jsp")).thenReturn(disp);

        new LoadListUsersCommand().executeGet(request,response);
        verify(request).getRequestDispatcher("/list.users.jsp");

    }

    @Test
    void testExecuteGetUserUser() throws ServletException, IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userRole")).thenReturn(Role.USER);
        User user = mock(User.class);
        when(session.getAttribute("currentUser")).thenReturn(user);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);

        RequestDispatcher disp = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/page.access.denied.jsp")).thenReturn(disp);

        new LoadListUsersCommand().executeGet(request,response);
        verify(request).getRequestDispatcher("/page.access.denied.jsp");

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

        new LoadListUsersCommand().executeGet(request,response);
        verify(response).sendRedirect("contextPath");

    }
}