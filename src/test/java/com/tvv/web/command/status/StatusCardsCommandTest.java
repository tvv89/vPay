package com.tvv.web.command.status;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.CardDAOImpl;
import com.tvv.db.entity.Card;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.UtilCommand;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;

import static org.mockito.Mockito.*;

class StatusCardsCommandTest {

    @Test
    void testExecutePost() throws IOException, AppException, ServletException {
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userRole")).thenReturn(Role.USER);
        User currentUser = mock(User.class);
        when(currentUser.getId()).thenReturn(1L);

        when(session.getAttribute("currentUser")).thenReturn(currentUser);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);

        String inputString = "{\"cardId\":1}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));

        CardDAOImpl cardDAO = mock(CardDAOImpl.class);
        Card cardById = mock(Card.class);
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(cardById.getUser()).thenReturn(user);
        when(cardById.getStatus()).thenReturn(false);
        when(cardDAO.updateStatusCardById(1L,1)).thenReturn(true);
        Card findCard = new Card();
        findCard.setUser(new User());
        when(cardDAO.findCardById(1L)).thenReturn(cardById).thenReturn(findCard);

        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        StatusCardsCommand service = new StatusCardsCommand();
        service.setUp(cardDAO);
        service.executePost(request,response);

        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));
        assertJSON.add("card", new Gson().toJsonTree(findCard));
        String sendData = new Gson().toJson(assertJSON);
        verify(out).print(sendData);

    }

    @Test
    void testExecutePostNoCard() throws IOException, AppException, ServletException {
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("userRole")).thenReturn(Role.USER);
        User currentUser = mock(User.class);
        when(currentUser.getId()).thenReturn(2L);

        when(session.getAttribute("currentUser")).thenReturn(currentUser);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);

        String inputString = "{\"cardId\":1}";
        Reader readerString = new StringReader(inputString);
        when(request.getReader()).thenReturn(new BufferedReader(readerString));

        CardDAOImpl cardDAO = mock(CardDAOImpl.class);
        Card cardById = mock(Card.class);
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(cardById.getUser()).thenReturn(user);
        when(cardById.getStatus()).thenReturn(false);
        when(cardDAO.findCardById(1L)).thenReturn(cardById);

        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        StatusCardsCommand service = new StatusCardsCommand();
        service.setUp(cardDAO);
        service.executePost(request,response);

        JsonObject assertJSON = UtilCommand.errorMessageJSON("Cannot change card status");
        String sendData = new Gson().toJson(assertJSON);
        verify(out).print(sendData);

    }
}