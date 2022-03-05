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
            <button class="uk-button uk-button-primary">Sort by name</button>
            <button class="uk-button uk-button-primary">Sort by number</button>
            <button class="uk-button uk-button-primary">Sort by account</button>
        </p>
    </div>
    <div class="uk-grid" data-uk-grid-margin>
        <ul class="uk-pagination">
            <li><a href="?page=${pageView-1}"><span class="uk-margin-small-right" uk-pagination-previous></span> Previous</a></li>
            <li class="uk-disabled"><span>Page ${pageView} of ${pages}</span></li>
            <li class="uk-margin-auto-left"><a href="?page=${pageView+1}">Next <span class="uk-margin-small-left" uk-pagination-next></span></a></li>
        </ul>
        <div class="uk-width-medium-1-1">
            <table class="uk-table uk-table-hover uk-table-small uk-table-middle uk-table-divider uk-table-striped">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Card number</th>
                    <th>Expiration date</th>
                    <th>Account</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="card" items="${cardList}">
                    <tr>
                        <td>${card.id}</td>
                        <td>${card.name}</td>
                        <td>${card.number}</td>
                        <td>${card.expDate}</td>
                        <td>${card.account.name}</td>
                        <td>
                        <c:choose>
                            <c:when test="${card.status=='true'}">
                                <span uk-icon="unlock"></span>
                            </c:when>
                            <c:otherwise>
                                <span uk-icon="lock"></span>
                            </c:otherwise>
                        </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>



    <script>

        UIkit.util.on('#js-modal-dialog', 'click', function (e) {
            e.preventDefault();
            e.target.blur();
            UIkit.modal.dialog('<p class="uk-modal-body">UIkit dialog!</p>');
        });

        UIkit.util.on('#js-modal-alert', 'click', function (e) {
            e.preventDefault();
            e.target.blur();
            UIkit.modal.alert('UIkit alert!').then(function () {
                console.log('Alert closed.')
            });
        });

        UIkit.util.on('#js-modal-confirm', 'click', function (e) {
            e.preventDefault();
            e.target.blur();
            UIkit.modal.confirm('UIkit confirm!').then(function () {
                console.log('Confirmed.')
            }, function () {
                console.log('Rejected.')
            });
        });

        UIkit.util.on('#js-modal-prompt', 'click', function (e) {
            e.preventDefault();
            e.target.blur();
            UIkit.modal.prompt('Name:', 'Your name').then(function (name) {
                console.log('Prompted:', name)
            });
        });

    </script>


</body>
</html>