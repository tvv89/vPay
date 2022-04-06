<!DOCTYPE html>
<html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title><fmt:message key="index.login_page.title"/></title>
  <link rel="stylesheet" href="css/uikit.min.css">
  <script src="js/uikit.min.js"></script>
  <script src="js/uikit-icons.js"></script>
</head>

<body class="uk-height-1-1">
<c:choose>
  <c:when test="${sessionScope.userRole=='ADMIN'}">
    <%
      String redirectURL = "controller?command=listUsers";
      response.sendRedirect(redirectURL);
    %>
  </c:when>
  <c:when test="${sessionScope.userRole=='USER'}">
    <%
      String redirectURL = "controller?command=listAccounts";
      response.sendRedirect(redirectURL);
    %>
  </c:when>
  <c:otherwise>
    <div class="uk-align-center uk-position-center uk-text-center uk-height-1-1">
      <div class="uk-align-center" style="width: 250px;">
        <h1>vPay</h1>
        <form class="uk-panel uk-panel-box uk-form" method="post" action="controller">
          <div class="uk-form-row">
            <input type="hidden" name="command" value="login"/>
            <input class="uk-width-1-1 uk-form-large" type="text"
                   id="login" name="login" placeholder="<fmt:message key="index.login_page.login"/>">
          </div>
          <div class="uk-form-row">
            <input class="uk-width-1-1 uk-form-large" type="password"
                   id="password" name="password" placeholder="<fmt:message key="index.login_page.password"/>">
          </div>
          <div class="uk-form-row">
            <input class="uk-width-1-1 uk-button uk-button-primary uk-button-large"
                   type="submit" value="<fmt:message key="index.login_page.submit"/>"/>
          </div>
          <div class="uk-form-row">
            <a href="controller?command=registration"><fmt:message key="index.login_page.registration"/></a>
          </div>
        </form>
      </div>
    </div>
  </c:otherwise>
</c:choose>

</body>

</html>
