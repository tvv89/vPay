<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title><fmt:message key="error_page.title"/></title>
    <link rel="stylesheet" href="css/uikit.css">
</head>
<body>
<div></div>
<div class="uk-align-center uk-position-center">
    <img class="uk-preserve-width uk-border-pill uk-align-center"
         src="images/_error.png"
         width="300" alt="">
    <h1 class="uk-align-center">${sessionScope.errorHeader}</h1>
    <p class="uk-align-center">${sessionScope.errorMessage}</p>

</div>
</body>
</html>