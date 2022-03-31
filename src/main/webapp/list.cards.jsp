<!DOCTYPE html>
<html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <title>Card page</title>
    <script src="js/card.util.js"></script>
</head>
<body>

<div>
    <%@ include file="/WEB-INF/jspf/navigation.jspf" %>
    <div>

    </div>
    <div>
        <select class="uk-select uk-width-1-4@s" id="sortCardOption" name="items" form-field onchange="changeSort()">
            <option value=1>Sort by Name</option>
            <option value=2>Sort by Number</option>
            <option value=3>Sort by Account name</option>
        </select>

        <%@ include file="/WEB-INF/jspf/item.per.page.jspf" %>

        <c:if test="${sessionScope.userRole=='USER'}">
            <button class="uk-button uk-button-primary" href="#modal-add-card"
                    uk-toggle>Add payment card
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
                    <th>Number</th>
                    <th>Expiration date</th>
                    <th>Owner</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody id="table">

                </tbody>
            </table>
        </div>
    </div>

    <c:if test="${sessionScope.userRole=='USER'}">
        <%@ include file="/WEB-INF/jspf/card.create.form.jspf" %>
    </c:if>
</div>
<script>

</script>
</body>
</html>