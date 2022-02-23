<!DOCTYPE html>
<html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>vPay</title>
    <link rel="stylesheet" href="css/uikit.min.css">
    <script src="../vendor/jquery.js"></script>
    <script src="js/uikit.min.js"></script>
    <script src="js/uikit-icons.min.js"></script>
</head>

<body>

<div class="uk-container uk-container-left uk-margin-top uk-margin-large-bottom" uk-img>

    <nav class="uk-navbar uk-margin-large-bottom">
        <button class="uk-navbar-brand uk-hidden-small uk-button">vPay</button>
        <ul class="uk-navbar-nav uk-hidden-small">
            <li class="uk-active">
                <a href="controller?command=listUsers">Users</a>
            </li>
            <li>
                <a href="controller?command=listAccounts">Accounts</a>
            </li>
            <li>
                <a href="controller?command=listPayments">Payments</a>
            </li>
            <li>
                <a href="controller?command=listCards">Cards</a>
            </li>
            <li>
                <a href="controller?command=logout">Logout</a>
            </li>

        </ul>

    </nav>



    <div>
        <p uk-margin>
            <button class="uk-button uk-button-primary">Button</button>
            <button class="uk-button uk-button-primary">Disable User</button>
        </p>
    </div>
    <div class="uk-grid" data-uk-grid-margin>
        <div class="uk-width-medium-1-1">
            <table class="uk-table uk-table-hover uk-table-middle uk-table-divider uk-table-striped">
                <thead>
                <tr>
                    <th class="uk-table-shrink">Photo</th>
                    <th>Status</th>
                    <th>Логін</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Date of birth</th>
                    <th>Sex</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${usersList}">
                    <tr>
                        <td><img class="uk-preserve-width uk-border-circle" src="images/${user.photo}" width="40" alt=""></td>
                        <td>${user.id}</td>
                        <td><c:choose>
                                <c:when test="${user.status=='true'}">
                                    <span uk-icon="unlock"></span>
                                </c:when>
                                <c:otherwise>
                                    <span uk-icon="lock"></span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${user.login}</td>
                        <td>${user.firstName}</td>
                        <td>${user.lastName}</td>
                        <td>${user.dayOfBirth}</td>
                        <td>${user.sex}</td>
                        <td>
                            <form method="post" >
                            <button class="uk-button uk-button-default" type="button" name="user">Disable User</button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>



</body>
</html>