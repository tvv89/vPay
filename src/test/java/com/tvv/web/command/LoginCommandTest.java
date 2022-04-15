package com.tvv.web.command;

import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginCommandTest {

    private HttpServletResponse response;
    private HttpServletRequest request;
    private HttpSession session;
    private UserDAO userDAO;

    @BeforeEach
    private void init() throws IOException {
        response = mock(HttpServletResponse.class);

        session = mock(HttpSession.class);
        when(session.getAttribute("userRole")).thenReturn(Role.USER);


        request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);

        userDAO = mock(UserDAO.class);
    }

    @Test
    void testExecutePostUserException() throws AppException, ServletException, IOException {

        when(request.getParameter("login")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getContextPath()).thenReturn("");

        when(userDAO.findUserByLogin("admin"))
                .thenThrow(new AppException("Error current user", new IllegalArgumentException()));

        LoginCommand loginCMD = new LoginCommand();
        loginCMD.setUp(userDAO);
        loginCMD.executePost(request,response);
        verify(session, times(1))
                .setAttribute("errorMessage","Login/password cannot be empty");

    }

    @Test
    void testExecutePostUserEmpty() throws AppException, ServletException, IOException {

        when(request.getParameter("login")).thenReturn("");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getContextPath()).thenReturn("");

        when(userDAO.findUserByLogin(""))
                .thenThrow(new AppException("Error current user", new IllegalArgumentException()));

        LoginCommand loginCMD = new LoginCommand();
        loginCMD.setUp(userDAO);
        loginCMD.executePost(request,response);
        verify(session, times(1))
                .setAttribute("errorMessage","Login/password cannot be empty");

    }

    @Test
    void testExecutePostCurrentUserNull() throws AppException, ServletException, IOException {

        when(request.getParameter("login")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getContextPath()).thenReturn("");

        when(userDAO.findUserByLogin("admin"))
                .thenReturn(null);

        LoginCommand loginCMD = new LoginCommand();
        loginCMD.setUp(userDAO);
        loginCMD.executePost(request,response);
        verify(session, times(1))
                .setAttribute("errorMessage","Can't find user with this login");

    }

    @Test
    void testExecutePostCurrentUserIsLocked() throws AppException, ServletException, IOException {

        when(request.getParameter("login")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getContextPath()).thenReturn("");

        User currentUser = new User();
        currentUser.setStatus(false);
        when(userDAO.findUserByLogin("admin"))
                .thenReturn(currentUser);

        LoginCommand loginCMD = new LoginCommand();
        loginCMD.setUp(userDAO);
        loginCMD.executePost(request,response);
        verify(session, times(1))
                .setAttribute("errorMessage","User is locked, please, contact to administrator");

    }

    @Test
    void testExecutePostCurrentUserBadPassword() throws AppException, ServletException, IOException {

        when(request.getParameter("login")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getContextPath()).thenReturn("");

        User currentUser = new User();
        currentUser.setStatus(true);
        currentUser.setPassword("4e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
        when(userDAO.findUserByLogin("admin"))
                .thenReturn(currentUser);

        LoginCommand loginCMD = new LoginCommand();
        loginCMD.setUp(userDAO);
        loginCMD.executePost(request,response);
        verify(session, times(1))
                .setAttribute("errorMessage","Bad password");

    }

    @Test
    void testExecutePostCurrentUserAccept() throws AppException, ServletException, IOException {

        when(request.getParameter("login")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getContextPath()).thenReturn("");

        User currentUser = new User();
        currentUser.setStatus(true);
        currentUser.setPassword("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
        currentUser.setLocal("uk");
        when(userDAO.findUserByLogin("admin"))
                .thenReturn(currentUser);

        LoginCommand loginCMD = new LoginCommand();
        loginCMD.setUp(userDAO);
        loginCMD.executePost(request,response);
        verify(session, times(1))
                .setAttribute("currentUser",currentUser);
        verify(session,times(1))
                .setAttribute("currentLanguage", "uk");
    }

}