<!DOCTYPE html>
<html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <title><fmt:message key="form_create_payment.title"/></title>
    <script src="js/payment.create.util.js"></script>
</head>
<body>
<div>
    <%@ include file="/WEB-INF/jspf/navigation.jspf" %>
    <div class="uk-grid-medium uk-child-width-expand@s" uk-grid>
        <div>
            <legend class="uk-legend uk-heading-line"><fmt:message key="form_create_payment.sender.title"/>:</legend>
            <div class="uk-margin">
                <select class="uk-select" id="currentPaymentAccount" onchange="changeAccount()">
                    <c:forEach items="${accountsPayment}" var="item">
                        <option value=${item.id}>${item.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div>
                <label class="uk-form-label uk-text-large" id="accountName" >${accountsPayment[0].name}</label><br>
                <label class="uk-form-label uk-text-large" id="accountStatus">${accountsPayment[0].status}</label><br>
                <label class="uk-form-label uk-text-large" id="accountUID">${accountsPayment[0].iban}</label><br>
                <label class="uk-form-label uk-text-large" id="accountBalance">${accountsPayment[0].balance}</label>
                <label class="uk-form-label uk-text-large" id="accountCurrency">  ${accountsPayment[0].currency}</label><br>
            </div>
        </div>
        <div>
            <legend class="uk-legend uk-heading-line"><fmt:message key="form_create_payment.recipient.title"/>:</legend>
            <div class="uk-margin">
                <select class="uk-select" id="recipientType">
                    <option value="Card"><fmt:message key="form_create_payment.recipient.type.card"/></option>
                    <option value="Account"><fmt:message key="form_create_payment.recipient.type.account"/></option>
                </select>
            </div>
            <div class="uk-margin">
                <input class="uk-input uk-width-1-2" type="text" id="recipientNumber" placeholder="">
                <button class="uk-button uk-button-secondary uk-width-1-3"
                        onclick="checkAccount()">
                    <fmt:message key="form_create_payment.recipient.check"/>
                </button>
                </br>
                <label class="uk-form-label" id="accountOwner"><fmt:message key="form_create_payment.recipient.unknown_user"/></label>

            </div>
            <div>
                <div class="uk-margin uk-grid-small uk-child-width-auto uk-grid">
                    <label><input class="uk-radio"
                                  type="radio"
                                  name="statusPayment"
                                  value="Ready" checked>
                        <fmt:message key="form_create_payment.recipient.status.ready"/>
                    </label>
                    <label><input class="uk-radio"
                                  type="radio"
                                  name="statusPayment"
                                  value="Submitted">
                        <fmt:message key="form_create_payment.recipient.status.submitted"/>
                    </label>
                </div>
            </div>
            <div>
                <label class="uk-form-label uk-width-1-4 uk-text-uppercase">
                    <fmt:message key="form_create_payment.recipient.currency"/>:
                </label>
                </br>
                <select class="uk-select uk-width-1-4" id="currencyOfPayment">
                    <option value="EUR">EUR</option>
                    <option value="UAH">UAH</option>
                    <option value="USD">USD</option>
                </select>
            </div>
            </br>
            <div>
                <label class="uk-form-label uk-width-1-4 uk-text-uppercase">
                    <fmt:message key="form_create_payment.recipient.sum"/>:
                </label>
                </br>
                <input class="uk-input uk-width-1-4" type="text" id="valueOfPayment">
                <button class="uk-button uk-button-secondary uk-width-1-4" onclick="calculatePayment()">
                    <fmt:message key="form_create_payment.recipient.calculate"/>
                </button>
            </div>
            <div>
                <div class="uk-grid-small" uk-grid>
                    <div class="uk-width-expand uk-text-large" uk-leader>
                        <fmt:message key="form_create_payment.recipient.commission"/>:
                    </div>
                    <div class="uk-text-large" id="commission"></div>
                </div>
                <br>
                <div class="uk-grid-small" uk-grid>
                    <div class="uk-width-expand uk-text-large" uk-leader>
                        <fmt:message key="form_create_payment.recipient.total"/>:
                    </div>
                    <div class="uk-text-large" id="total"></div>
                </div>
                <br>
                <div>
                    <button class="uk-button uk-button-primary uk-width-1-1" onclick="submitPayment()">
                        <fmt:message key="form_create_payment.recipient.submit"/>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
<%@ include file="/WEB-INF/jspf/javascript.language.pack.jspf" %>
</html>
