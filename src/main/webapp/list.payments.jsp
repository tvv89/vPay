<!DOCTYPE html>
<html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <title><fmt:message key="list_payments.title"/></title>
    <script src="js/payment.util.js"></script>
</head>
<body>
<div>
    <%@ include file="/WEB-INF/jspf/navigation.jspf" %>
    <div>
        <select class="uk-select uk-width-1-4@s" id="sortPaymentsOption" name="items" form-field onchange="changeSort()">
            <option value=1><fmt:message key="list_payments.sort.by_guid"/></option>
            <option value=2><fmt:message key="list_payments.sort.by_total"/></option>
            <option value=3><fmt:message key="list_payments.sort.by_acs_time"/></option>
            <option value=4 selected><fmt:message key="list_payments.sort.by_des_time"/></option>
        </select>

        <%@ include file="/WEB-INF/jspf/item.per.page.jspf" %>

        <c:if test="${sessionScope.userRole=='USER'}">
            <button class="uk-button uk-button-primary"
                    onclick="location.href = 'controller?command=createPayment'">
                <fmt:message key="list_payments.create_payment"/>
            </button>
        </c:if>

    </div>
    <div>
        <ul class="uk-pagination" id="pagination">
        </ul>
    </div>
    <div class="uk-grid" data-uk-grid-margin>
        <div class="uk-width-medium-1-1">
            <table class="uk-table uk-table-hover uk-table-middle uk-table-divider uk-table-striped">
                <thead>
                <tr>
                    <th><fmt:message key="list_payments.table.header.datetime"/></th>
                    <th><fmt:message key="list_payments.table.header.guid"/></th>
                    <th><fmt:message key="list_payments.table.header.user"/></th>
                    <th><fmt:message key="list_payments.table.header.sender"/></th>
                    <th><fmt:message key="list_payments.table.header.recipient"/></th>
                    <th><fmt:message key="list_payments.table.header.currency"/></th>
                    <th><fmt:message key="list_payments.table.header.commission"/></th>
                    <th><fmt:message key="list_payments.table.header.total"/></th>
                    <th><fmt:message key="list_payments.table.header.status"/></th>
                    <th><fmt:message key="list_payments.table.header.action"/></th>
                </tr>
                </thead>
                <tbody id="table">

                </tbody>
            </table>
        </div>
    </div>
    <div id="paymentinfo" uk-modal>

    </div>
</div>

</body>
<%@ include file="/WEB-INF/jspf/javascript.language.pack.jspf" %>
</html>