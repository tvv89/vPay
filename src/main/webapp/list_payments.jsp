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
        <button class="uk-navbar-brand uk-hidden-small uk-button">vPay</button>
        <ul class="uk-navbar-nav uk-hidden-small">
            <li>
                <a href="controller?command=listUsers">Users</a>
            </li>
            <li>
                <a href="controller?command=listAccounts">Accounts</a>
            </li>
            <li class="uk-active">
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
            <button class="uk-button uk-button-primary">Disable Account</button>
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
                    <th>Date</th>
                    <th>GUID</th>
                    <th>Sender: User</th>
                    <th>Payment type</th>
                    <th>Recipient type</th>
                    <th>Currency</th>
                    <th>Commission</th>
                    <th>Total</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="payment" items="${paymentList}">
                    <tr>
                        <td>${payment.id}</td>
                        <td type="date-time">${payment.timeOfLog}</td>
                        <td>${payment.guid}</td>
                        <td>${payment.user.firstName} ${payment.user.lastName}</td>
                        <td>${payment.senderType}</td>
                        <td>${payment.recipientType}</td>
                        <td type="text">${payment.currency}</td>
                        <td type="double">${payment.commission}</td>
                        <td type="double">${payment.total}</td>
                        <td>
                        <c:choose>
                            <c:when test="${payment.status=='0'}">
                                <span uk-icon="clock"></span>
                            </c:when>
                            <c:otherwise>
                                <span uk-icon="check"></span>
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