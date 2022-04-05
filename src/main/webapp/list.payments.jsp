<!DOCTYPE html>
<html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <title>Payment page</title>
    <script src="js/payment.util.js"></script>
</head>
<body>
<div>
    <%@ include file="/WEB-INF/jspf/navigation.jspf" %>
    <div>
        <select class="uk-select uk-width-1-4@s" id="sortPaymentsOption" name="items" form-field onchange="changeSort()">
            <option value=1>Sort by Guid</option>
            <option value=2>Sort by Total</option>
            <option value=3>Sort by Asc Time</option>
            <option value=4 selected>Sort by Des Time</option>
        </select>

        <%@ include file="/WEB-INF/jspf/item.per.page.jspf" %>

        <c:if test="${sessionScope.userRole=='USER'}">
            <button class="uk-button uk-button-primary"
                    onclick="location.href = 'controller?command=createPayment'">
                    Create payment
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
                    <th>Date</th>
                    <th>GUID</th>
                    <th>User</th>
                    <th>Sender</th>
                    <th>Recipient</th>
                    <th>Currency</th>
                    <th>Commission</th>
                    <th>Total</th>
                    <th>Status</th>
                    <th>Action</th>
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
</html>