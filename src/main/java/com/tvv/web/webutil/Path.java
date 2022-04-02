package com.tvv.web.webutil;

public final class Path {

	public static final String PAGE__LOGIN = "/index.jsp";
	public static final String PAGE__ERROR_PAGE = "/error.page.jsp";
	public static final String PAGE__LIST_USERS = "/list.users.jsp";
	public static final String PAGE__LIST_ACCOUNTS = "/list.accounts.jsp";
	public static final String PAGE__LIST_PAYMANT = "/list.payments.jsp";
	public static final String PAGE__LIST_CARDS = "/list.cards.jsp";
	public static final String PAGE__REGISTRATION = "/form.create.user.jsp";
	public static final String PAGE__ACCESS_DENIED = "/page.access.denied.jsp";
	public static final String PAGE__CREATE_PAYMENT = "/form.create.payment.jsp";

	public static final String COMMAND__START_PAGE = "";
	public static final String COMMAND__LIST_USERS = "controller?command=listUsers";
	public static final String COMMAND__LIST_ACCOUNTS = "controller?command=listAccounts";
	public static final String COMMAND__LIST_PAYMENTS = "controller?command=listPayments";
    public static final String COMMAND__LIST_CARDS = "controller?command=listCards";

}