<!DOCTYPE html>
<html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <title><fmt:message key="list_cards.title"/></title>
    <script src="js/card.util.js"></script>
</head>
<body>

<div>
    <%@ include file="/WEB-INF/jspf/navigation.jspf" %>
    <div>

    </div>
    <div>
        <select class="uk-select uk-width-1-4@s" id="sortCardOption" name="items" form-field onchange="changeSort()">
            <option value=1><fmt:message key="list_cards.sort.by_name"/></option>
            <option value=2><fmt:message key="list_cards.sort.by_number"/></option>
            <option value=3><fmt:message key="list_cards.sort.by_account"/></option>
        </select>

        <%@ include file="/WEB-INF/jspf/item.per.page.jspf" %>

        <c:if test="${sessionScope.userRole=='USER'}">
            <button class="uk-button uk-button-primary" href="#modal-add-card"
                    uk-toggle><fmt:message key="list_cards.add_card"/>
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
                    <th><fmt:message key="list_cards.table.header.name"/></th>
                    <th><fmt:message key="list_cards.table.header.number"/></th>
                    <th><fmt:message key="list_cards.table.header.exp_date"/></th>
                    <th><fmt:message key="list_cards.table.header.owner"/></th>
                    <th><fmt:message key="list_cards.table.header.status"/></th>
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
<%@ include file="/WEB-INF/jspf/javascript.language.pack.jspf" %>
</html>