
<!DOCTYPE html>
<html class="uk-height-1-1">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Register vPay</title>
  <link rel="stylesheet" href="css/uikit.min.css">
  <script src="js/uikit.min.js"></script>
  <!--<script src="js/main.js"></script> -->
</head>

<body class="uk-height-1-1">

<div class="uk-align-left@m uk-text-center uk-height-1-1">
    <h1>Registration form</h1>
    <form class="uk-form-horizontal" data-action="register-form">
      <!--<input type="hidden" name="command" value="registration"/> -->
      <div class="uk-margin">
        <label class="uk-form-label" for="photofile">Photo</label>
        <div uk-form-custom="target: true">
          <input type="file" id="photofile" accept="image/jpeg">
          <input class="uk-input uk-form-width-large" type="text" placeholder="Photo" disabled>
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="login">Login</label>
        <div class="uk-form-controls">
          <input class="uk-input uk-form-width-large" id="login" type="text" placeholder="Login">
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="password">Password</label>
        <div class="uk-form-controls">
          <input class="uk-input" id="password" type="password" placeholder="Password">
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="confirmpassword">Confirm password</label>
        <div class="uk-form-controls">
          <input class="uk-input" id="confirmpassword" type="password" placeholder="Confirm password">
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="email">Email</label>
        <div class="uk-form-controls">
          <input class="uk-input uk-form-width-large" id="email" type="text" placeholder="Email">
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="firstname">First name</label>
        <div class="uk-form-controls">
          <input class="uk-input" id="firstname" type="text" placeholder="First name">
        </div>
      </div>
      <div class="uk-margin">
        <label class="uk-form-label" for="lastname">Last name</label>
        <div class="uk-form-controls">
          <input class="uk-input" id="lastname" type="text" placeholder="Last name">
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
      <div>
        <input class="uk-width-1-1 uk-button uk-button-primary uk-button-default" type="submit" value="Register"/>
      </div>
    </form>
  </div>
</div>

</body>

</html>
<script>
  function getFormFieldValues(fields) {
    return Array.from(fields).reduce((formData, field) => {
      const formGroup = field.getAttribute('form-group');
      if (formGroup && !formData[formGroup]) {
        formData[formGroup] = {};
      }
      const property = formGroup ? formData[formGroup] : formData;
      const fieldType = field.type;
      const fieldName = field.getAttribute('form-field') || field.name;
      const saveRadioChecked = field.getAttribute('form-radio-checked') !== null;
      if (fieldType === 'text' || fieldType === 'password' || fieldType === 'select-one' || fieldType === 'textarea') {
        property[fieldName] = field.value;
      } else if (fieldType === 'checkbox') {
        property[fieldName] = field.checked;
      } else if (fieldType === 'radio' && !saveRadioChecked && field.checked) {
        property[fieldName] = field.value;
      } else if (fieldType === 'radio' && saveRadioChecked) {
        property[fieldName] = field.checked;
      }
      return formData;
    }, {});
  }

  function initRegisterForm(form) {
    form.addEventListener('submit', e => { e.preventDefault(); });
    const formFields = form.querySelectorAll('[form-field]');
    const submitButton = form.querySelector('input[type="submit"]');
    submitButton.addEventListener('click', () => {
      const loginData = getFormFieldValues(formFields);
      fetch('/controller?command=createUser', {
        method: 'POST',
        body: JSON.stringify(loginData)
      })
              .then(response => response.json())
              .then(data =>  {
                // here code if BE response was successful
              })
              .catch(err => {
                // here code if BE responses with error
              });
    });
  }

  window.addEventListener('DOMContentLoaded', () => {
    initRegisterForm(document.querySelector('[data-action="register-form"]'));
  });
</script>