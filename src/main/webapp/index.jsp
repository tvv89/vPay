
<!DOCTYPE html>
<html lang="en-gb" dir="ltr" class="uk-height-1-1">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Login vPay</title>
  <link rel="stylesheet" href="css/uikit.min.css">
  <script src="js/uikit.min.js"></script>
</head>

<body class="uk-height-1-1">

<div class="uk-align-center uk-position-center uk-text-center uk-height-1-1">
  <div class="uk-align-center" style="width: 250px;">
    <h1>vPay</h1>
    <form class="uk-panel uk-panel-box uk-form" method="post" action="controller">
      <div class="uk-form-row">
        <input type="hidden" name="command" value="login"/>
        <input class="uk-width-1-1 uk-form-large" type="text" id="login" name="login" placeholder="Username">
      </div>
      <div class="uk-form-row">
        <input class="uk-width-1-1 uk-form-large" type="password" id="password" name="password" placeholder="Password">
      </div>
      <div class="uk-form-row">
        <input class="uk-width-1-1 uk-button uk-button-primary uk-button-large" type="submit" value="Login"/>
      </div>
      <div class="uk-form-row">
        <a href="controller?command=registration">Register</a>
      </div>
    </form>

  </div>
</div>

</body>

</html>