package com.tvv.utils;

import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class FieldsCheckerTest {

    @Test
    void testCheckNameField() {
        assertEquals(true,FieldsChecker.checkNameField("Smith"));
        assertEquals(true,FieldsChecker.checkNameField("john"));
        assertEquals(true,FieldsChecker.checkNameField("Віньямін"));
        assertEquals(true,FieldsChecker.checkNameField("Криївний"));
        assertEquals(false,FieldsChecker.checkNameField("Криїв1ний"));
        assertEquals(false,FieldsChecker.checkNameField("Smith."));
        assertEquals(false,FieldsChecker.checkNameField("John_Smith"));

    }

    @Test
    void testCheckEMailAddress() {
        assertEquals(true, FieldsChecker.checkEMailAddress("email@site.com"));
        assertEquals(true, FieldsChecker.checkEMailAddress("ema_123il@site.com"));
        assertEquals(false, FieldsChecker.checkEMailAddress("email_site.com"));
        assertEquals(false, FieldsChecker.checkEMailAddress("email@.com"));
    }

    @Test
    void testCheckPasswordField() {
        assertEquals(true, FieldsChecker.checkPasswordField("Admin123."));
        assertEquals(true, FieldsChecker.checkPasswordField("uSer.15"));
        assertEquals(false, FieldsChecker.checkPasswordField("A1."));
        assertEquals(false, FieldsChecker.checkPasswordField("Admin"));
    }

    @Test
    void testCheckCardNumber() {
        assertEquals(true, FieldsChecker.checkCardNumber("1234 1234 1234 1234"));
        assertEquals(true, FieldsChecker.checkCardNumber("1234123412341234"));
        assertEquals(false, FieldsChecker.checkCardNumber("123412341234123"));
        assertEquals(false, FieldsChecker.checkCardNumber("12341234O2341234"));
    }

    @Test
    void testCheckBalanceDouble() {
        assertEquals(true, FieldsChecker.checkBalanceDouble("1.2"));
        assertEquals(true, FieldsChecker.checkBalanceDouble("10000.38"));
        assertEquals(false, FieldsChecker.checkBalanceDouble("1,2"));
        assertEquals(false, FieldsChecker.checkBalanceDouble("100000.381"));
        assertEquals(false, FieldsChecker.checkBalanceDouble("-1.2"));

    }

    @Test
    void testCheckEqualsPassword() {
        assertEquals(true, FieldsChecker.checkEqualsPassword("passWord1","passWord1"));
        assertEquals(true, FieldsChecker.checkEqualsPassword("12345","12345"));
        assertEquals(false, FieldsChecker.checkEqualsPassword("passWord1","password1"));
        assertEquals(false, FieldsChecker.checkEqualsPassword("passWord","passWord1"));
    }

    @Test
    void testCheckAge18YearsOld() {
        LocalDate date = LocalDate.parse("2000-05-07");
        assertEquals(true, FieldsChecker.checkAge18YearsOld(date));
        date = LocalDate.parse("1990-01-01");
        assertEquals(true, FieldsChecker.checkAge18YearsOld(date));
        date = LocalDate.parse("2020-05-07");
        assertEquals(false, FieldsChecker.checkAge18YearsOld(date));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        date = LocalDate.parse("11.02.1992",formatter);
        assertEquals(true, FieldsChecker.checkAge18YearsOld(date));
    }
}