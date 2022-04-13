package com.tvv.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.PaymentDAO;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Payment;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.UtilCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentService service;
    private AccountService accountService;
    private AccountDAO accountDAO;
    private PaymentDAO paymentDAO;

    @BeforeEach
    private void setUp(){
        accountService = mock(AccountService.class);
        accountDAO = mock(AccountDAO.class);
        paymentDAO = mock(PaymentDAO.class);
        service = new PaymentService(accountService, accountDAO,paymentDAO);
    }

    @Test
    void testInsertPaymentToDB() throws AppException {
        service.insertPaymentToDB(new Payment());
        verify(paymentDAO, times(1)).insertPayment(Mockito.any());
    }

    @Test
    void testInsertPaymentToDBNull() {
        assertThrows(AppException.class, ()->{
            service.insertPaymentToDB(null);
        });
    }

    @Test
    void testStatusPaymentAccountReady() throws AppException {
        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));
        assertJSON.add("id", new Gson().toJsonTree(1));
        assertJSON.add("statusPayment", new Gson().toJsonTree("Submitted"));

        Payment payment = mock(Payment.class);
        when(payment.getStatus()).thenReturn("Ready").thenReturn("Submitted");
        Account accountFrom = mock(Account.class);
        when(accountFrom.getBalance()).thenReturn(1000D);
        when(accountFrom.getId()).thenReturn(1L);
        when(accountDAO.findAccountById(1L)).thenReturn(accountFrom);
        when(payment.getId()).thenReturn(1L);
        when(payment.getSenderId()).thenReturn(accountFrom);
        when(payment.getRecipientId()).thenReturn("aaaaaBBBBB11111rrrrrTTTTT");
        when(payment.getTotal()).thenReturn(100D);
        when(payment.getRecipientType()).thenReturn("Account");
        when(payment.getSum()).thenReturn(100D);
        when(payment.getCurrencySum()).thenReturn("EUR");

        Account accountTo = mock(Account.class);
        when(accountTo.getCurrency()).thenReturn("EUR");
        when(accountDAO.findAccountByUID("aaaaaBBBBB11111rrrrrTTTTT")).thenReturn(accountTo);
        when(accountService.depositAccount(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(true);

        when(paymentDAO.updatePaymentStatus(1L,"Submitted")).thenReturn(true);

        JsonObject resultJSON = service.changeStatusPayment(payment);
        assertEquals(assertJSON.toString(),resultJSON.toString());
        when(accountDAO.findAccountById(1L)).thenReturn(accountFrom);

    }

    @Test
    void testStatusPaymentCardReady() throws AppException {
        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));
        assertJSON.add("id", new Gson().toJsonTree(1));
        assertJSON.add("statusPayment", new Gson().toJsonTree("Submitted"));

        Payment payment = mock(Payment.class);
        when(payment.getStatus()).thenReturn("Ready").thenReturn("Submitted");
        Account accountFrom = mock(Account.class);
        when(accountFrom.getBalance()).thenReturn(1000D);
        when(accountFrom.getId()).thenReturn(1L);
        when(accountDAO.findAccountById(1L)).thenReturn(accountFrom);
        when(payment.getId()).thenReturn(1L);
        when(payment.getSenderId()).thenReturn(accountFrom);
        when(payment.getRecipientId()).thenReturn("aaaaaBBBBB11111rrrrrTTTTT");
        when(payment.getTotal()).thenReturn(100D);
        when(payment.getRecipientType()).thenReturn("Card");
        when(payment.getSum()).thenReturn(100D);
        when(payment.getCurrencySum()).thenReturn("EUR");
        when(accountService.depositAccount(accountFrom,null,100D,0D)).thenReturn(true);
        when(paymentDAO.updatePaymentStatus(1L,"Submitted")).thenReturn(true);

        JsonObject resultJSON = service.changeStatusPayment(payment);
        assertEquals(assertJSON.toString(),resultJSON.toString());

    }

    @Test
    void testStatusPaymentNoReady() throws AppException {
        JsonObject assertJSON = new JsonObject();
        assertJSON = UtilCommand.errorMessageJSON("Payment had been submitted before");

        Payment payment = mock(Payment.class);
        when(payment.getStatus()).thenReturn("Submitted");

        JsonObject resultJSON = service.changeStatusPayment(payment);
        assertEquals(assertJSON.toString(),resultJSON.toString());

    }

    @Test
    void testStatusPaymentNull() throws AppException {
        Payment payment = null;
        JsonObject assertJSON = new JsonObject();
        assertJSON = UtilCommand.errorMessageJSON("Payment can not be submitted.");
        assertEquals(assertJSON.toString(), service.changeStatusPayment(payment).toString());
    }

    @Test
    void testDeletePaymentTrue() throws AppException {
        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));

        Payment payment = mock(Payment.class);
        when(payment.getId()).thenReturn(1L);
        when(paymentDAO.deletePaymentById(1L)).thenReturn(true);
        assertEquals(assertJSON.toString(), service.deletePayment(payment).toString());
    }

    @Test
    void testDeletePaymentFalse() throws AppException {
        JsonObject assertJSON = UtilCommand.errorMessageJSON("Payment can not be deleted.");

        Payment payment = mock(Payment.class);
        when(payment.getId()).thenReturn(1L);
        when(paymentDAO.deletePaymentById(1L)).thenReturn(false);
        assertEquals(assertJSON.toString(), service.deletePayment(payment).toString());
    }

    @Test
    void testCalculatePaymentForAccount() throws AppException {
        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));
        assertJSON.add("currency", new Gson().toJsonTree("UAH"));
        assertJSON.add("commissionValue", new Gson().toJsonTree(0.0));
        assertJSON.add("totalPayment", new Gson().toJsonTree(100.0));
        Map<String, Object> data = new HashMap<>();
        data.put("accountType", "Account");
        data.put("accountNumber", "aaaaaBBBBB11111rrrrrTTTTT");
        data.put("currencyFrom", "UAH");
        data.put("currencyTo", "UAH");
        data.put("value", "100");

        Account account = mock(Account.class);

        when(accountDAO.findAccountByUID((String) data.get("accountNumber")))
                .thenReturn(account);

        assertEquals(assertJSON.toString(),service.calculatePayment(data).toString());

    }

    @Test
    void testCalculatePaymentForCard() throws AppException {
        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));
        assertJSON.add("currency", new Gson().toJsonTree("UAH"));
        assertJSON.add("commissionValue", new Gson().toJsonTree(5.0));
        assertJSON.add("totalPayment", new Gson().toJsonTree(105.0));
        Map<String, Object> data = new HashMap<>();
        data.put("accountType", "Card");
        data.put("accountNumber", "1111222233334444");
        data.put("currencyFrom", "UAH");
        data.put("currencyTo", "UAH");
        data.put("value", "100");

        Account account = mock(Account.class);

        when(accountDAO.findAccountByUID((String) data.get("accountNumber")))
                .thenReturn(account);

        assertEquals(assertJSON.toString(),service.calculatePayment(data).toString());

    }

    @Test
    void testCalculatePaymentForAccountNull() throws AppException {
        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));
        assertJSON.add("currency", new Gson().toJsonTree("UAH"));
        assertJSON.add("commissionValue", new Gson().toJsonTree(0));
        assertJSON.add("totalPayment", new Gson().toJsonTree(100));
        Map<String, Object> data = new HashMap<>();
        data.put("accountType", "Account");
        data.put("accountNumber", "aaaaaBBBBB11111rrrrrTTTTT");
        data.put("currencyFrom", "UAH");
        data.put("currencyTo", "UAH");
        data.put("value", "100");

        Account account = null;
        when(accountDAO.findAccountByUID((String) data.get("accountNumber")))
                .thenReturn(account);
        assertEquals(UtilCommand.errorMessageJSON("Account not found").toString(),
                service.calculatePayment(data).toString());

    }

    @Test
    void testCreatePaymentJSONAccount() throws AppException {
        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));

        Map<String, Object> data = new HashMap<>();
        data.put("accountType", "Account");
        data.put("accountNumber", "aaaaaBBBBB11111rrrrrTTTTT");
        data.put("accountFromId", 1);
        data.put("currencyFrom", "UAH");
        data.put("currencyTo", "UAH");
        data.put("value", "100");

        User user = mock(User.class);
        Account accountTo = mock(Account.class);
        when(accountTo.getCurrency()).thenReturn("UAH");
        Account accountFrom = mock(Account.class);
        when(accountFrom.getStatus()).thenReturn("Enabled");
        when(accountFrom.getBalance()).thenReturn(1000D);

        when(accountDAO.findAccountByUID((String) data.get("accountNumber")))
                .thenReturn(accountTo);
        when(accountDAO.findAccountById(1L))
                .thenReturn(accountFrom);
        when(accountService.depositAccount(accountFrom,accountTo,100D,100D)).thenReturn(true);

        assertEquals(assertJSON.toString(), service.createPayment(data,user).toString());

    }

    @Test
    void testCreatePaymentJSONCard() throws AppException {
        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));

        Map<String, Object> data = new HashMap<>();
        data.put("accountType", "Card");
        data.put("accountNumber", "2222 3333 4444 5555");
        data.put("accountFromId", 1);
        data.put("currencyFrom", "UAH");
        data.put("currencyTo", "UAH");
        data.put("value", "100");

        User user = mock(User.class);
        Account accountFrom = mock(Account.class);
        when(accountFrom.getStatus()).thenReturn("Enabled");
        when(accountFrom.getBalance()).thenReturn(1000D);

        when(accountDAO.findAccountById(1L))
                .thenReturn(accountFrom);
        when(accountService.depositAccount(accountFrom, null,100D,0D)).thenReturn(true);

        assertEquals(assertJSON.toString(), service.createPayment(data,user).toString());

    }

}