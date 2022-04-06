<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title><fmt:message key="access_denied.title"/></title>
    <link rel="stylesheet" href="css/uikit.css">
</head>
<body>
<div></div>
<div class="uk-align-center uk-position-center">
    <img class="uk-preserve-width uk-border-pill uk-align-center"
         src="images/_lock.png"
         width="300" alt="">
    <h1 class="uk-align-center"><fmt:message key="access_denied.header"/></h1>
    <p class="uk-align-center"><fmt:message key="access_denied.message"/></p>
</div>
</body>
</html>
