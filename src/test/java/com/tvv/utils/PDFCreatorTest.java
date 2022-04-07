package com.tvv.utils;

import com.tvv.db.dao.PaymentDAO;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Card;
import com.tvv.db.entity.Payment;
import com.tvv.db.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PDFCreatorTest {
    Payment payment;
    PDFCreatorTest () {
        User user = new User();
        user.setId(1L);
        user.setPassword("");
        user.setEmail("qqq@qqq.qqq");
        user.setDayOfBirth("1989-05-18");
        user.setLogin("login");
        user.setStatus(true);
        user.setFirstName("Vova");
        user.setLastName("Тимчук");
        user.setRole(1);
        user.setSex("Man");
        user.setPhoto("_blank.png");

        Account account = new Account();
        account.setBalance(1000D);
        account.setBankCode("");
        account.setIpn("");
        account.setStatus("Enabled");
        account.setId(1L);
        account.setName("Premium");
        account.setCard(new Card());
        account.setCurrency("UAH");
        account.setOwnerUser(user);

        payment = new Payment();
        payment.setId(1L);
        payment.setGuid("1112223334");
        payment.setUser(user);
        payment.setSenderId(account);
        payment.setRecipientType("Account");
        payment.setRecipientId("jhGSJgaJjhgSLJAGD");
        payment.setTimeOfLog("2022-04-04 12:30:42");
        payment.setCurrency("UAH");
        payment.setCommission(5.0);
        payment.setTotal(105.0);
        payment.setStatus("Ready");
        payment.setSum(14D);
        payment.setCurrencySum("USD");
    }

    @Test
    void createPDF() {
        String local = "en";


    }
}