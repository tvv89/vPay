<!DOCTYPE html>
<html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <title>User page</title>
    <script src="js/user.util.js"></script>

</head>
<body>
<div>
    <%@ include file="/WEB-INF/jspf/navigation.jspf" %>
    <div>
        <select class="uk-select uk-width-1-4@s" id="sortUsersOption" name="items" form-field onchange="changeSort()">
            <option value=1>Sort by Login</option>
            <option value=2>Sort by First Name</option>
            <option value=3>Sort by Last Name</option>
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
                    <th class="uk-table-shrink">Photo</th>
                    <th>Login</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Role</th>
                    <th>Date of birth</th>
                    <th>Sex</th>
                    <th>Status</th>
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
<script>

</script>
</body>
</html>