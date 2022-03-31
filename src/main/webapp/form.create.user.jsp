<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html class="uk-height-1-1">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Register vPay</title>
  <link rel="stylesheet" href="css/uikit.min.css">
  <script src="js/uikit.min.js"></script>
</head>

<body class="uk-height-1-1">
<div class="uk-align-left@m uk-text-center uk-height-1-1">
    <h1>Registration form</h1>
    <form class="uk-form-horizontal" method="post" action="controller" enctype="multipart/form-data">
      <input type="hidden" name="command" value="createUser"/>
      <div class="uk-margin">
        <label class="uk-form-label" for="photofile">Photo</label>
        <div uk-form-custom="target: true">
          <input type="file" id="photofile" name="photofile" accept="image/jpeg">
          <input class="uk-input uk-form-width-large" type="text" placeholder="Photo" disabled>
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="login">Login</label>
        <div class="uk-form-controls">
          <input class="uk-input uk-form-width-large" id="login" name="login" type="text" placeholder="Login" form-field>
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="password">Password</label>
        <div class="uk-form-controls">
          <input class="uk-input" id="password" type="password" name="password" placeholder="Password" form-field>
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="confirmpassword">Confirm password</label>
        <div class="uk-form-controls">
          <input class="uk-input" id="confirmpassword" type="password" name="confirmpassword" placeholder="Confirm password" form-field>
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="email">Email</label>
        <div class="uk-form-controls">
          <input class="uk-input uk-form-width-large" id="email" name="email" type="text" placeholder="Email" form-field>
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="firstname">First name</label>
        <div class="uk-form-controls">
          <input class="uk-input" id="firstname" type="text" name="firstname" placeholder="First name" form-field>
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="lastname">Last name</label>
        <div class="uk-form-controls">
          <input class="uk-input" id="lastname" type="text" name="lastname" placeholder="Last name" form-field>
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="dateofbirth">Date of birth</label>
        <div class="uk-form-controls">
          <input type="date" id="dateofbirth" name="dateofbirth" form-field>
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="sex">Sex</label>
        <div class="uk-form-controls">
          <select class="uk-select" id="sex" name="sex" form-field>
            <option>Male</option>
            <option>Female</option>
            <option>Other</option>
          </select>
        </div>
      </div>
      <div>
        <input class="uk-width-1-1 uk-button uk-button-primary uk-button-default" type="submit" value="Register"/>
      </div>
    </form>
  </div>
</div>

</body>

</html>