<!DOCTYPE html>
<html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <title><fmt:message key="list_accounts.title"/></title>
    <script src="js/account.util.js"></script>

</head>
<body>
<div>
    <%@ include file="/WEB-INF/jspf/navigation.jspf" %>
    <div>
        <select class="uk-select uk-width-1-4@s" id="sortAccountsOption" name="items" form-field onchange="changeSort()">
            <option value=1><fmt:message key="list_accounts.sort.by_name"/></option>
            <option value=2><fmt:message key="list_accounts.sort.by_balance"/></option>
            <option value=3><fmt:message key="list_accounts.sort.by_uid"/></option>
        </select>

        <%@ include file="/WEB-INF/jspf/item.per.page.jspf" %>

        <c:if test="${sessionScope.userRole=='USER'}">
            <button class="uk-button uk-button-primary" href="#modal-add-account"
                    uk-toggle><fmt:message key="list_accounts.add_account"/>
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
                    <th><fmt:message key="list_accounts.table.header.name"/></th>
                    <th><fmt:message key="list_accounts.table.header.uid"/></th>
                    <th><fmt:message key="list_accounts.table.header.currency"/></th>
                    <th><fmt:message key="list_accounts.table.header.balance"/></th>
                    <th><fmt:message key="list_accounts.table.header.owner"/></th>
                    <th><fmt:message key="list_accounts.table.header.status"/></th>
                    <c:if test="${sessionScope.userRole=='USER'}">
                        <th><fmt:message key="list_accounts.table.header.action"/></th>
                        <th><fmt:message key="list_accounts.table.header.card"/></th>
                    </c:if>
                </tr>
                </thead>
                <tbody id="table">

                </tbody>
            </table>
        </div>
    </div>
    <div id="cardinfo" uk-modal>

    </div>

    <c:if test="${sessionScope.userRole=='USER'}">
        <%@ include file="/WEB-INF/jspf/account.create.form.jspf" %>
    </c:if>

</div>
<script>

</script>
</body>
<%@ include file="/WEB-INF/jspf/javascript.language.pack.jspf" %>
</html>