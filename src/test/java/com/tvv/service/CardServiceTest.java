package com.tvv.service;

import com.tvv.db.dao.CardDAO;
import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.Card;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTest {


    @Test
    void testCreateCard() throws AppException {
        Map<String,String> data = new HashMap<>();
        data.put("name", "name");
        data.put("cardnumber","1111222233334444");
        data.put("expMM","11");
        data.put("expYY","23");
        data.put("ownerUser","1");

        UserDAO uDAO = mock(UserDAO.class);
        CardDAO cDAO = mock(CardDAO.class);
        when(uDAO.findUserById(1L)).thenReturn(new User());
        when(cDAO.insertCard(Mockito.any())).thenReturn(new Card());

        CardService service = new CardService();
        service.setDAO(uDAO,cDAO);

        service.createCard(data);
        verify(uDAO,times(1)).findUserById(Mockito.any());
        verify(cDAO,times(1)).insertCard(Mockito.any());

    }


    @Test
    void testCreateCardBadCardNumber() {
        Map<String,String> data = new HashMap<>();
        data.put("cardnumber","1234567890123");
        assertThrows(AppException.class, ()-> {
            new CardService().createCard(data);
        });

    }

    @Test
    void testFormatCard() {
        String assertCard = "1111 2222 3333 4444";
        assertEquals(assertCard, CardService.formatCard("1111222233334444"));
        assertEquals(assertCard, CardService.formatCard("1111 2222 3333 4444"));
        assertEquals(assertCard, CardService.formatCard(" 1111 222233334444 "));
        assertEquals("", CardService.formatCard(null));

    }
}