<!DOCTYPE html>
<html>
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
        <div class="uk-navbar-left">
            <button class="uk-navbar-brand uk-hidden-small uk-button">vPay</button>
            <ul class="uk-navbar-nav uk-hidden-small">
                <c:if test="${userRole=='ADMIN'}">
                    <li <c:if test="${currentPage=='users'}">class="uk-active" </c:if>>
                        <a href="controller?command=listUsers">Users</a>
                    </li>
                </c:if>
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
                            <img class="uk-preserve-width uk-border-circle"
                                 src="images/${sessionScope.currentUser.photo}"
                                 width="80" alt="">
                            <p>Login: ${sessionScope.currentUser.login}</p>
                            <p>First name: ${sessionScope.currentUser.firstName}</p>
                            <p>Last name: ${sessionScope.currentUser.lastName}</p>
                            <p>Day of birth: ${sessionScope.currentUser.dayOfBirth}</p>
                            <button class="uk-button-default" onclick="location.href = 'controller?command=logout'">
                                Logout
                            </button>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </nav>
    <div>
        <p uk-margin>
            <button class="uk-button uk-button-primary">Sort by balance</button>
            <button class="uk-button uk-button-primary">Sort by name</button>
        </p>
    </div>
    <div class="uk-grid" data-uk-grid-margin>
        <div class="uk-width-medium-1-1">
            <table class="uk-table uk-table-hover uk-table-middle uk-table-divider uk-table-striped">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Status</th>
                    <th>Name</th>
                    <th>Currency</th>
                    <th>Balance</th>
                    <th>User Owner</th>
                    <th>Details</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="account" items="${accountList}">
                    <tr>
                        <td>${account.id}</td>
                        <td><c:choose>
                            <c:when test="${account.status=='true'}">
                                <span uk-icon="unlock"></span>
                            </c:when>
                            <c:otherwise>
                                <span uk-icon="lock"></span>
                            </c:otherwise>
                        </c:choose>
                        </td>
                        <td>${account.name}</td>
                        <td>${account.currency}</td>
                        <td>${account.balance}</td>
                        <td>${account.ownerUser.firstName} ${account.ownerUser.lastName} <br/>
                            <c:if test="${userRole=='ADMIN'}">
                                <button class="uk-button uk-button-default" type="button">User Details</button>
                            </c:if>
                        </td>
                        <td>
                            <button class="uk-button uk-button-default" type="button">Account Details</button>
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