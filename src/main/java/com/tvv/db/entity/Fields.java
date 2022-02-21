package com.tvv.db.entity;

public final class Fields {

	public static final String ENTITY__ID = "id";
	
	public static final String USER__LOGIN = "login";
	public static final String USER__PASSWORD = "password";
	public static final String USER__ROLE = "role";
	public static final String USER__STATUS = "statususer";
	public static final String USER__FIRST_NAME = "firstname";
	public static final String USER__LAST_NAME = "lastname";
	public static final String USER__DATE_OF_BIRTH = "dateofbirth";
	public static final String USER__SEX = "sex";
	public static final String USER__GENDER = "gender";
	public static final String USER__PHOTO = "photo";

	public static final String ACCOUNT__IBAN = "iban";
	public static final String ACCOUNT__IPN = "ipn";
	public static final String ACCOUNT__BANK_CODE = "bankCode";
	public static final String ACCOUNT__NAME = "name";
	public static final String ACCOUNT__CURRENCY = "currency";
	public static final String ACCOUNT__BALANCE = "balance";
	public static final String ACCOUNT__USER_ID = "ownerUser";
	public static final String ACCOUNT__STATUS = "statusAccount";

	public static final String PAYMENT__GUID = "guid";
	public static final String PAYMENT__USER = "ownerUser";
	public static final String PAYMENT__SENDER_TYPE = "senderType";
	public static final String PAYMENT__SENDER_ID = "senderId";
	public static final String PAYMENT__RECIPIENT_TYPE = "recipientType";
	public static final String PAYMENT__RECIPIENT_ID = "recipientId";
	public static final String PAYMENT__TIME_OF_LOG = "datetimeOfLog";
	public static final String PAYMENT__CURRENCY = "currency";
	public static final String PAYMENT__COMMISSION = "commission";
	public static final String PAYMENT__TOTAL = "total";
	public static final String PAYMENT__STATUS = "statusPayment";

	public static final String CARD__NAME = "name";
	public static final String CARD__NUMBER = "number";
	public static final String CARD__EXPIRATION_DATE = "expDate";
	public static final String CARD__ACCOUNT = "ownerAccount";
	public static final String CARD__STATUS = "statusCard";


}