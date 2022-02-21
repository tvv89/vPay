
<!DOCTYPE html>
<html class="uk-height-1-1">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Login vPay</title>
  <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon">
  <link rel="apple-touch-icon-precomposed" href="images/apple-touch-icon.png">
  <link rel="stylesheet" href="css/uikit.min.css">
  <script src="js/uikit.min.js"></script>
</head>

<body class="uk-height-1-1">

<div class="uk-align-left uk-text-center uk-height-1-1">
    <h1>Registration form</h1>
    <form class="uk-form-stacked">

      <div class="js-upload" uk-form-custom>
        <input type="file" name="photofile">
        <button class="uk-button uk-button-default" type="button" tabindex="-1">Select</button>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="login">Login</label>
        <div class="uk-form-controls">
          <input class="uk-input" id="login" type="text" placeholder="login">
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="password">Password</label>
        <div class="uk-form-controls">
          <input class="uk-input" id="password" type="password" placeholder="password">
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="confirmpassword">Password</label>
        <div class="uk-form-controls">
          <input class="uk-input" id="confirmpassword" type="password" placeholder="confirm password">
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="firstname">First name</label>
        <div class="uk-form-controls">
          <input class="uk-input" id="firstname" type="text" placeholder="first name">
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="lastname">Last name</label>
        <div class="uk-form-controls">
          <input class="uk-input" id="lastname" type="text" placeholder="last name">
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="dateofbirth">Date of birth</label>
        <div class="uk-form-controls">
          <input type="date" id="dateofbirth">
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="sex">Sex</label>
        <div class="uk-form-controls">
          <select class="uk-select" id="sex">
            <option>Male</option>
            <option>Female</option>
            <option>Other</option>
          </select>
        </div>
      </div>


    </form>

  </div>
</div>

</body>

</html>