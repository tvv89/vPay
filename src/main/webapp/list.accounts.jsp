<!DOCTYPE html>
<html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <title>Account page</title>
    <script src="js/account.util.js"></script>

</head>
<body>
<div>
    <%@ include file="/WEB-INF/jspf/navigation.jspf" %>
    <div>
        <select class="uk-select uk-width-1-4@s" id="sortAccountsOption" name="items" form-field onchange="changeSort()">
            <option value=1>Sort by Name</option>
            <option value=2>Sort by Balance</option>
            <option value=3>Sort by IBAN</option>
        </select>

        <%@ include file="/WEB-INF/jspf/item.per.page.jspf" %>

        <c:if test="${sessionScope.userRole=='USER'}">
            <button class="uk-button uk-button-primary" href="#modal-add-account"
                    uk-toggle>Add account
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
                    <th>Name</th>
                    <th>Currency</th>
                    <th>Balance</th>
                    <th>Owner</th>
                    <th>Status</th>
                    <c:if test="${sessionScope.userRole=='USER'}">
                        <th>Action</th>
                        <th>Card</th>
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
</html>