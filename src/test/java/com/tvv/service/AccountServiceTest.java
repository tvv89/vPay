package com.tvv.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.AccountDAOImpl;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Card;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.utils.SystemParameters;
import com.tvv.web.command.UtilCommand;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;

class AccountServiceTest {
    private AccountService service;
    private Account accountFrom;
    private Account accountTo;
    private AccountDAO accountDAO;

    @BeforeEach
    private void setUp() {
        accountDAO = mock(AccountDAOImpl.class);
        service = new AccountService(accountDAO);

        accountFrom = new Account();
        accountFrom.setId(1L);
        accountFrom.setIban("qweRTYuiop12345ASdfGHjklz");
        accountFrom.setIpn("");
        accountFrom.setName("AccountFrom");
        accountFrom.setCurrency("EUR");
        accountFrom.setBalance(1000D);
        User userFrom = new User();
        userFrom.setId(1L);
        accountFrom.setOwnerUser(userFrom);
        accountFrom.setStatus("Enabled");
        Card cardFrom = new Card();
        cardFrom.setId(1L);
        cardFrom.setNumber("Card1");
        cardFrom.setNumber("1111222233334444");
        cardFrom.setExpDate("11/22");
        accountFrom.setCard(cardFrom);

        accountTo = new Account();
        accountTo.setId(2L);
        accountTo.setIban("qweRTYuiop98765ASdfGHjklz");
        accountTo.setIpn("");
        accountTo.setName("AccountTo");
        accountTo.setCurrency("USD");
        accountTo.setBalance(500D);
        User userTo = new User();
        accountTo.setOwnerUser(userTo);
        accountTo.setStatus("Enabled");
        Card cardTo = new Card();
        accountFrom.setCard(cardTo);
    }


    @Test
    void testDepositAccount() throws AppException {
        Double addBalanceFrom = 990D;
        //when(accountDAO.updateAccountsBalance(accountFrom.getId(),newBalance)).thenReturn(true);
        Double addBalanceTo = 990D;
        when(accountDAO.updateAccountsBalance(accountFrom.getId(), accountTo.getId(), addBalanceFrom, addBalanceTo)).thenReturn(true);

        double newBalanceFrom = accountFrom.getBalance() - addBalanceFrom;
        double newBalanceTo = accountTo.getBalance() + addBalanceTo;
        service.depositAccount(accountFrom, accountTo, addBalanceFrom, addBalanceTo);
        verify(accountDAO, times(1))
                .updateAccountsBalance(accountFrom.getId(), accountTo.getId(), newBalanceFrom, newBalanceTo);

    }

    @Test
    void testDepositAccountToNull() throws AppException {
        Double newBalance = 990D;
        when(accountDAO.updateAccountBalance(accountFrom.getId(), newBalance)).thenReturn(true);
        Double newBalanceTo = 990D;

        service.depositAccount(accountFrom, null, newBalance, newBalanceTo);
        verify(accountDAO, times(1)).updateAccountBalance(Mockito.any(), Mockito.any());

    }

    @Test
    void testDepositAccountSameAccount() throws AppException {
        Double newBalance = 990D;
        when(accountDAO.updateAccountBalance(accountFrom.getId(), newBalance)).thenReturn(true);
        Double newBalanceTo = 990D;

        service.depositAccount(accountFrom, accountFrom, newBalance, newBalanceTo);
        verify(accountDAO, times(0)).updateAccountBalance(Mockito.any(), Mockito.any());

    }

    @Test
    void testCreateAccount() throws AppException {

        Map<String, String> data = new HashMap<>();
        User user = new User();
        data.put("ipn", "");
        data.put("bankCode", "");
        data.put("name", "AAAAABBBBB");
        data.put("currency", "EUR");

        when(accountDAO.insertAccount(accountTo)).thenReturn(accountTo);
        service.createAccount(data, user);
        verify(accountDAO, times(1)).insertAccount(Mockito.any());

    }

    @Test
    void testCreateAccountLongName() throws AppException {
        Map<String, String> data = new HashMap<>();
        User user = new User();
        data.put("ipn", "");
        data.put("bankCode", "");
        data.put("name", "AAAAABBBBB12345678901234567");
        data.put("currency", "EUR");

        assertThrows(AppException.class, () -> {
            service.createAccount(data, user);
        });

    }

    @Test
    void testProcessDeleteAccount() throws AppException {
        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));

        Map<String, Object> data = new HashMap<>();
        data.put("accountId", 1);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute("userRole")).thenReturn(Role.USER);
        when(request.getSession().getAttribute("currentUser")).thenReturn(Mockito.any());
        when(accountDAO.findAccountById(1L)).thenReturn(accountFrom);
        when(accountDAO.deleteAccount(accountFrom)).thenReturn(true);

        JsonObject resultJSON = service.processDeleteAccount(request, data);

        assertEquals(assertJSON.toString(), resultJSON.toString());

    }

    @Test
    void testProcessDeleteNullAccount() throws AppException, IOException {
        ResourceBundle message;
        message = SystemParameters.getLocale("en");
        JsonObject assertJSON = UtilCommand.errorMessageJSON(message.getString("exception.service.account.delete"));

        Map<String, Object> data = new HashMap<>();
        data.put("accountId", 1);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute("userRole")).thenReturn(Role.USER);
        when(request.getSession().getAttribute("currentUser")).thenReturn(Mockito.any());
        when(accountDAO.findAccountById(1L)).thenReturn(null);

        JsonObject resultJSON = service.processDeleteAccount(request, data);

        assertEquals(assertJSON.toString(), resultJSON.toString());

    }

    @Test
    void testProcessDeleteAccountFromAdmin() throws AppException, IOException {
        ResourceBundle message;
        message = SystemParameters.getLocale("en");
        JsonObject assertJSON = UtilCommand.errorMessageJSON(message.getString("exception.service.account.delete"));

        Map<String, Object> data = new HashMap<>();
        data.put("accountId", 1);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute("userRole")).thenReturn(Role.ADMIN);
        when(request.getSession().getAttribute("currentUser")).thenReturn(Mockito.any());
        when(accountDAO.findAccountById(1L)).thenReturn(accountFrom);

        JsonObject resultJSON = service.processDeleteAccount(request, data);

        assertEquals(assertJSON.toString(), resultJSON.toString());

    }

    @Test
    void testProcessChangeStatus() throws AppException {
        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));
        assertJSON.add("userRole", new Gson().toJsonTree(Role.ADMIN));
        accountFrom.setStatus("Enabled");
        assertJSON.add("account", new Gson().toJsonTree(accountFrom));

        Map<String, Object> data = new HashMap<>();
        data.put("accountId", 1);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        User user = mock(User.class);

        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute("userRole")).thenReturn(Role.ADMIN);
        when(request.getSession().getAttribute("currentUser")).thenReturn(Mockito.any());

        when(accountDAO.findAccountById(1L)).thenReturn(accountFrom);
        accountFrom.setStatus("Disabled");

        when(accountDAO.updateStatusAccountById(1L, "Enabled")).thenReturn(true);
        accountFrom.setStatus("Enabled");
        when(accountDAO.findAccountById(1L)).thenReturn(accountFrom);

        JsonObject resultJSON = service.processChangeStatus(request, data);

        assertEquals(assertJSON.toString(), resultJSON.toString());
    }

    @Test
    void testProcessChangeStatusException() throws AppException {
        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));
        assertJSON.add("userRole", new Gson().toJsonTree(Role.USER));
        accountFrom.setStatus("Enabled");
        assertJSON.add("account", new Gson().toJsonTree(accountFrom));

        Map<String, Object> data = new HashMap<>();
        data.put("accountId", 1);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);

        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute("userRole")).thenReturn(Role.USER);
        when(request.getSession().getAttribute("currentUser")).thenReturn(user);

        Account account = mock(Account.class);
        when(accountDAO.findAccountById(1L)).thenReturn(account);
        when(account.getOwnerUser()).thenReturn(mock(User.class));
        when(account.getOwnerUser().getId()).thenReturn(2L);


        AccountService service = new AccountService(accountDAO);
        assertThrows(AppException.class, () -> {
            service.processChangeStatus(request, data);
        });

    }

    @Test
    void testProcessCardSelect() throws AppException {
        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("OK"));
        Account account = mock(Account.class);
        when(accountDAO.findAccountById(1L)).thenReturn(account);
        JsonObject resultJSON = service.processCardSelect(1, Mockito.any());
        assertEquals(assertJSON.toString(), resultJSON.toString());

    }

    @Test
    void testProcessCardSelectAccountNull() throws AppException, IOException {
        ResourceBundle message;
        message = SystemParameters.getLocale("en");
        JsonObject assertJSON = new JsonObject();
        assertJSON.add("status", new Gson().toJsonTree("ERROR"));
        assertJSON.add("message", new Gson().toJsonTree(message.getString("exception.service.account.change.not_found")));

        when(accountDAO.findAccountById(1L)).thenReturn(null);
        JsonObject resultJSON = service.processCardSelect(1, Mockito.any());
        assertEquals(assertJSON.toString(), resultJSON.toString());

    }
}