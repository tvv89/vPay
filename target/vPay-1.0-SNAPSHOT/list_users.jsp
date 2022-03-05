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
    <script src="js/uikit.min.js"></script>
    <script src="js/uikit-icons.min.js"></script>
</head>

<body>

<div class="uk-container uk-container-left uk-margin-top uk-margin-large-bottom" uk-img>

    <nav class="uk-navbar uk-margin-large-bottom">
        <div class="uk-navbar-left">
            <button class="uk-navbar-brand uk-hidden-small uk-button">vPay</button>
            <ul class="uk-navbar-nav uk-hidden-small">
                <li <c:if test="${currentPage=='users'}">class="uk-active" </c:if>>
                    <a href="controller?command=listUsers">Users</a>
                </li>
                <li <c:if test="${currentPage=='accounts'}">class="uk-active" </c:if>>
                    <a href="controller?command=listAccounts">Accounts</a>
                </li>
                <li <c:if test="${currentPage=='payments'}">class="uk-active" </c:if>>
                    <a href="controller?command=listPayments">Payments</a>
                </li>
                <li <c:if test="${currentPage=='cards'}">class="uk-active" </c:if>>
                    <a href="controller?command=listCards">Cards</a>
                </li>
                <li>
                    <a href="controller?command=logout">Logout</a>
                </li>

            </ul>
        </div>
        <div class="uk-navbar-right">
            <ul class="uk-navbar-nav">
                <li>
                    <img class="uk-preserve-width uk-border-circle" src="images/${sessionScope.currentUser.photo}"
                         width="40" alt="" uk-toggle="target: #offcanvas-flip">
                    ${sessionScope.currentUser.login}
                    <div id="offcanvas-flip" uk-offcanvas="flip: true; overlay: true">
                        <div class="uk-offcanvas-bar">
                            <button class="uk-offcanvas-close" type="button" uk-close></button>
                            <h3>Information</h3>
                            <img class="uk-preserve-width uk-border-circle" src="images/${sessionScope.currentUser.photo}"
                                 width="80" alt="">
                            <p>Login:  ${sessionScope.currentUser.login}</p>
                            <p>First name:  ${sessionScope.currentUser.firstName}</p>
                            <p>Last name:  ${sessionScope.currentUser.lastName}</p>
                            <p>Day of birth:  ${sessionScope.currentUser.dayOfBirth}</p>
                            <button class="uk-button-default" onclick="location.href = 'controller?command=logout'">Logout</button>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </nav>

    <div>
        <p uk-margin>
            <button class="uk-button uk-button-primary">Sort by login</button>
            <button class="uk-button uk-button-primary">Sort by first name</button>
            <button class="uk-button uk-button-primary">Sort by second name</button>
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
                        <td><img class="uk-preserve-width uk-border-circle" src="photoPath/${user.photo}" width="40"
                                 alt=""></td>
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
                            <form method="post">
                                <button class="uk-button uk-button-default" type="button" name="user">Disable User
                                </button>
                            </form>
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