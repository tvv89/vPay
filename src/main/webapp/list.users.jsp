<!DOCTYPE html>
<html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <title><fmt:message key="list_users.title"/></title>
    <script src="js/user.util.js"></script>

</head>
<body>
<div>
    <%@ include file="/WEB-INF/jspf/navigation.jspf" %>
    <div>
        <select class="uk-select uk-width-1-4@s" id="sortUsersOption" name="items" form-field onchange="changeSort()">
            <option value=1><fmt:message key="list_users.sort.by_login"/></option>
            <option value=2><fmt:message key="list_users.sort.by_first_name"/></option>
            <option value=3><fmt:message key="list_users.sort.by_last_name"/></option>
        </select>

        <%@ include file="/WEB-INF/jspf/item.per.page.jspf" %>

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
                    <th class="uk-table-shrink"><fmt:message key="list_users.table.header.photo"/></th>
                    <th><fmt:message key="list_users.table.header.login"/></th>
                    <th><fmt:message key="list_users.table.header.first_name"/></th>
                    <th><fmt:message key="list_users.table.header.last_name"/></th>
                    <th><fmt:message key="list_users.table.header.role"/></th>
                    <th><fmt:message key="list_users.table.header.date_of_birth"/></th>
                    <th><fmt:message key="list_users.table.header.sex"/></th>
                    <th><fmt:message key="list_users.table.header.status"/></th>
                </tr>
                </thead>
                <tbody id="table">

                </tbody>
            </table>
        </div>
    </div>
    <div id="userinfo" uk-modal>

    </div>
</div>
</body>
<%@ include file="/WEB-INF/jspf/javascript.language.pack.jspf" %>
</html>