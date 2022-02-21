<div class="uk-overflow-auto">
  <table class="uk-table uk-table-hover uk-table-middle uk-table-divider">
    <thead>
    <tr>
      <th class="uk-table-shrink"></th>
      <th class="uk-table-shrink">Photo</th>
      <th>ID</th>
      <th>Status</th>
      <th>Login</th>
      <th>First Name</th>
      <th>Last Name</th>
      <th>Date of birth</th>
      <th>Sex</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="user" items="${usersList}">
      <tr>
        <td><input class="uk-checkbox" type="checkbox"></td>
        <td><img class="uk-preserve-width uk-border-circle" src="images${user.photo}" width="40" alt=""></td>
        <td>${user.id}</td>
        <td>${user.status}</td>
        <td>${user.login}</td>
        <td>${user.firstName}</td>
        <td>${user.lastName}</td>
        <td>${user.dayOfBirth}</td>
        <td>${user.sex}</td>
        <td>
          <button class="uk-button uk-button-default" type="button">Disable User</button>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>