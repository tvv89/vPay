<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html class="uk-height-1-1">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title><fmt:message key="form_create_user.title"/></title>
  <link rel="stylesheet" href="css/uikit.min.css">
  <script src="js/uikit.min.js"></script>
</head>

<body class="uk-height-1-1">
<div class="uk-align-left@m uk-text-center uk-height-1-1">
    <h1><fmt:message key="form_create_user.user.title"/></h1>
    <form class="uk-form-horizontal" method="post" action="controller" enctype="multipart/form-data">
      <input type="hidden" name="command" value="createUser"/>
      <div class="uk-margin">
        <label class="uk-form-label" for="photofile"><fmt:message key="form_create_user.user.photo"/></label>
        <div uk-form-custom="target: true">
          <input type="file" id="photofile" name="photofile" accept="image/jpeg">
          <input class="uk-input uk-form-width-large" type="text"
                 placeholder="<fmt:message key="form_create_user.user.photo"/>" disabled>
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="login"><fmt:message key="form_create_user.user.login"/></label>
        <div class="uk-form-controls">
          <input class="uk-input uk-form-width-large"
                 id="login" name="login" type="text"
                 placeholder="<fmt:message key="form_create_user.user.login"/>" form-field>
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="password"><fmt:message key="form_create_user.user.password"/></label>
        <div class="uk-form-controls">
          <input class="uk-input" id="password" type="password" name="password"
                 placeholder="<fmt:message key="form_create_user.user.password"/>" form-field>
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="confirmpassword"><fmt:message key="form_create_user.user.confirm_password"/></label>
        <div class="uk-form-controls">
          <input class="uk-input" id="confirmpassword" type="password" name="confirmpassword"
                 placeholder="<fmt:message key="form_create_user.user.confirm_password"/>" form-field>
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="email"><fmt:message key="form_create_user.user.email"/></label>
        <div class="uk-form-controls">
          <input class="uk-input uk-form-width-large" id="email" name="email" type="text"
                 placeholder="<fmt:message key="form_create_user.user.email"/>" form-field>
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="firstname"><fmt:message key="form_create_user.user.first_name"/></label>
        <div class="uk-form-controls">
          <input class="uk-input" id="firstname" type="text" name="firstname"
                 placeholder="<fmt:message key="form_create_user.user.first_name"/>" form-field>
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="lastname"><fmt:message key="form_create_user.user.last_name"/></label>
        <div class="uk-form-controls">
          <input class="uk-input" id="lastname" type="text" name="lastname"
                 placeholder="<fmt:message key="form_create_user.user.last_name"/>" form-field>
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="dateofbirth"><fmt:message key="form_create_user.user.date_of_birth"/></label>
        <div class="uk-form-controls">
          <input type="date" id="dateofbirth" name="dateofbirth" form-field>
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="sex"><fmt:message key="form_create_user.user.sex"/></label>
        <div class="uk-form-controls">
          <select class="uk-select" id="sex" name="sex" form-field>
            <option value="Male"><fmt:message key="form_create_user.user.sex.male"/></option>
            <option value="Female"><fmt:message key="form_create_user.user.sex.female"/></option>
            <option value="Other"><fmt:message key="form_create_user.user.sex.other"/></option>
          </select>
        </div>
      </div>
      <div>
        <input class="uk-width-1-1 uk-button uk-button-primary uk-button-default"
               type="submit" value="<fmt:message key="form_create_user.user.submit"/>"/>
      </div>
    </form>
  </div>
</div>

</body>
<%@ include file="/WEB-INF/jspf/javascript.language.pack.jspf" %>
</html>