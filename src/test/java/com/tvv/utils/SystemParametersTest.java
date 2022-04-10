package com.tvv.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class SystemParametersTest {

    @Test
    void getLocaleEnTest() {
        try {
            ResourceBundle rb = SystemParameters.getLocale("en");
            String keyCardNumber = rb.getString("card_create_from.card_number");
            String keyCurrency = rb.getString("form_create_payment.recipient.currency");
            String keyAccount = rb.getString("pdf.body.sender.account");
            assertEquals("Card number",keyCardNumber);
            assertEquals("Currency",keyCurrency);
            assertEquals("Account",keyAccount);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void getLocaleUkTest() {
        try {
            ResourceBundle rb = SystemParameters.getLocale("uk");
            String keyCardNumber = rb.getString("card_create_from.card_number");
            String keyCurrency = rb.getString("form_create_payment.recipient.currency");
            String keyAccount = rb.getString("pdf.body.sender.account");
            assertEquals("Номер карти",keyCardNumber);
            assertEquals("Валюта",keyCurrency);
            assertEquals("Рахунок",keyAccount);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void getLocaleNullTest() {
        try {
            ResourceBundle rb = SystemParameters.getLocale(null);
            String keyCardNumber = rb.getString("card_create_from.card_number");
            String keyCurrency = rb.getString("form_create_payment.recipient.currency");
            String keyAccount = rb.getString("pdf.body.sender.account");
            assertEquals("Card number",keyCardNumber);
            assertEquals("Currency",keyCurrency);
            assertEquals("Account",keyAccount);

            rb = SystemParameters.getLocale("");
            keyCardNumber = rb.getString("card_create_from.card_number");
            keyCurrency = rb.getString("form_create_payment.recipient.currency");
            keyAccount = rb.getString("pdf.body.sender.account");
            assertEquals("Card number",keyCardNumber);
            assertEquals("Currency",keyCurrency);
            assertEquals("Account",keyAccount);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void jsLanguagePackEnTest() {
        Map<String,String> assertMap = SystemParameters.jsLanguagePack("en");
        long keysCount = assertMap.keySet().stream().filter(e->e.contains("javascript")).count();
        assertEquals(assertMap.size(),keysCount);

        String keyPrevious = assertMap.get("javascript_pagination_previous");
        String keyDisabled = assertMap.get("javascript_general_disabled");
        String keyReady = assertMap.get("javascript_payment_status_ready");
        assertEquals("Previous",keyPrevious);
        assertEquals("Disabled",keyDisabled);
        assertEquals("Ready",keyReady);

    }

    @Test
    void jsLanguagePackUkTest() {
        Map<String,String> assertMap = SystemParameters.jsLanguagePack("uk");
        long keysCount = assertMap.keySet().stream().filter(e->e.contains("javascript")).count();
        assertEquals(assertMap.size(),keysCount);

        String keyPrevious = assertMap.get("javascript_pagination_previous");
        String keyDisabled = assertMap.get("javascript_general_disabled");
        String keyReady = assertMap.get("javascript_payment_status_ready");
        assertEquals("Попередня",keyPrevious);
        assertEquals("Не активний",keyDisabled);
        assertEquals("Підготовлений",keyReady);

    }

    @Test
    void jsLanguagePackNullTest() {
        Map<String,String> assertMap = SystemParameters.jsLanguagePack(null);
        long keysCount = assertMap.keySet().stream().filter(e->e.contains("javascript")).count();
        assertEquals(assertMap.size(),keysCount);

        String keyPrevious = assertMap.get("javascript_pagination_previous");
        String keyDisabled = assertMap.get("javascript_general_disabled");
        String keyReady = assertMap.get("javascript_payment_status_ready");

        assertEquals("Previous",keyPrevious);
        assertEquals("Disabled",keyDisabled);
        assertEquals("Ready",keyReady);


    }
}