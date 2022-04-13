package com.tvv.service;

import com.tvv.db.dao.UserDAO;
import com.tvv.service.exception.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService service;
    private UserDAO userDAO;

    @BeforeEach
    private void setUp(){
        service = new UserService();
        userDAO = mock(UserDAO.class);
        service.setUp(userDAO);
    }

    @Test
    void testCreateUser() throws AppException, IOException {
        Map<String,String> data = new HashMap<>();
        data.put("login","admin");
        data.put("password","Password1.");
        data.put("confirmpassword","Password1.");
        data.put("email","email@mail.com");
        data.put("firstname","Name");
        data.put("lastname","Family");
        data.put("dateofbirth","2000-01-01");
        data.put("sex","Man");
        data.put("photofile","_blank.png");

        service.createUser(data,"en");
        verify(userDAO, times(1)).insertUser(Mockito.any());

    }

    @Test
    void testCreateUserBadPassword() {
        Map<String,String> data = new HashMap<>();
        data.put("login","admin");
        data.put("password","passw0rd");
        data.put("confirmpassword","passw0rd");
        data.put("email","email@mail.com");
        data.put("firstname","Name");
        data.put("lastname","Family");
        data.put("dateofbirth","2000-01-01");
        data.put("sex","Man");
        data.put("photofile","_blank.png");

        AppException ex = assertThrows(AppException.class,
                ()-> service.createUser(data,"en"));

        assertTrue(ex.getMessage().contains("Password must have"));

    }

    @Test
    void testCreateUserBadDate() {
        Map<String,String> data = new HashMap<>();
        data.put("login","admin");
        data.put("password","passw0rd");
        data.put("confirmpassword","passw0rd");
        data.put("email","email@mail.com");
        data.put("firstname","Name");
        data.put("lastname","Family");
        data.put("dateofbirth","2022-01-01");
        data.put("sex","Man");
        data.put("photofile","_blank.png");

        AppException ex = assertThrows(AppException.class,
                ()-> service.createUser(data,"en"));

        assertTrue(ex.getMessage().contains("You have to be 18 years old"));

    }

    @Test
    void testCreateUserBadName() {
        Map<String,String> data = new HashMap<>();
        data.put("login","admin");
        data.put("password","passw0rd");
        data.put("confirmpassword","passw0rd");
        data.put("email","email@mail.com");
        data.put("firstname","Name1");
        data.put("lastname","Family");
        data.put("dateofbirth","2000-01-01");
        data.put("sex","Man");
        data.put("photofile","_blank.png");

        AppException ex = assertThrows(AppException.class,
                ()-> service.createUser(data,"en"));

        assertTrue(ex.getMessage().contains("First name incorrect"));

    }

    @Test
    void testHideUserName() {
        assertEquals(String.format("TES**** *****SSION ", Locale.ROOT),
                UserService.hideUserName("testing","commission"));
        assertEquals(String.format("FIRS***** ****NAME ", Locale.ROOT),
                UserService.hideUserName("FirstName","LastName"));
        assertEquals(String.format(" *****SSION ", Locale.ROOT),
                UserService.hideUserName("","commission"));
        assertEquals(String.format(" *****SSION ", Locale.ROOT),
                UserService.hideUserName(null,"commission"));
    }
}