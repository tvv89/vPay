var itemsPerPage=5;
var currentPGPage=1;
var sortBy=1;
window.addEventListener('DOMContentLoaded', (event) => {
        callPOSTRequest(1,0);
});
function callPOSTRequest(option, parameter) {
    var items = parseInt($('#itemsPerPage').val());
    switch (option){
        case 1:
            break;
        case 2:
            currentPGPage = currentPGPage + parameter;
            break;
        case 3:
            currentPGPage = 1;
            itemsPerPage = items;
            break;
    }
    fetch('controller?command=updateListUser', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        body: JSON.stringify({currentPage: currentPGPage,
            items: itemsPerPage,
            sorting: sortBy})
    }) .then(response => response.json())
        .then(data =>  {
            if (data.status =='OK') {
                createPagination(data.page, data.pages);
                createTable(data.list);
            } else callErrorAlert(data.message);
        })
        .catch(err => {
            callErrorAlert(err);
        });
}

function createTable(tx) {
    var table = document.getElementById('table')
    table.innerHTML = "";
    for (var i = 0; i < tx.length; i++) {
        var userStatusButton = tx[i].status == true ? "Disable User" : "Enable User";
        var userRole = tx[i].role == 0 ? "ADMIN" : "USER";
        var row = `<tr id="tr_${tx[i].id}">
          
                <td><img class="uk-preserve-width uk-border-circle" src="images/${tx[i].photo}" width="40"
                                 alt="" onclick="userInfo(${tx[i].id})" uk-toggle="target: #userinfo"></td>
                <td>${tx[i].login}</td>
                <td>${tx[i].firstName}</td>
                <td>${tx[i].lastName}</td>
                <td><button id="js-modal-status" class="uk-button uk-button-default" type="button" name="userR"
                                    value="${tx[i].id}" onclick="changeRoleButton(${tx[i].id})">${userRole}</button>
                </td>
                <td>${tx[i].dayOfBirth}</td>
                <td>${tx[i].sex}</td>
                <td><button id="js-modal-status" class="uk-button uk-button-default" type="button" name="user"
                                    value="${tx[i].id}" onclick="changeStatusButton(${tx[i].id})">${userStatusButton}</button>
                </td>
                </tr>`
        table.innerHTML += row;
    }

}

function createPagination(page,pages) {
    var paginat = document.getElementById('pagination')
    paginat.innerHTML = "";
    var row = `<li class="uk-margin-small-right ${page == 1 ? 'uk-disabled' : ''}" id="previous-page" onclick="callPOSTRequest(2,-1)">
                            <a><span class="uk-margin-small-right" uk-pagination-previous></span> Previous</a>
                        </li>
                        <li class="uk-margin-small uk-align-center">Page ${page} of ${pages} </li>
                        <li class="uk-margin-small-left ${page == pages ? 'uk-disabled' : ''}" id="next-page" onclick="callPOSTRequest(2,1)">
                            <a>Next <span class="uk-margin-small-left" uk-pagination-next></span></a>
                        </li>`
    paginat.innerHTML += row;
}

function changeUserStatus(id) {
    fetch('controller?command=statusUser', {
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        method: 'POST',
        body: JSON.stringify({userId: id,
            currentPage: currentPGPage,
            items: itemsPerPage})
    }) .then(response => response.json())
        .then(data =>  {
            if (data.status =='OK') {
                if ($('#tr_' + data.user.id).length) {
                    var userStatusButton = data.user.status == true ? "Disable User" : "Enable User";
                    var userRole = data.user.role == 0 ? "ADMIN" : "USER";
                    var newHtml = `
                <td><img class="uk-preserve-width uk-border-circle" src="images/${data.user.photo}" width="40"
                                 alt="" onclick="userInfo(${data.user.id})" uk-toggle="target: #userinfo"></td>
                <td>${data.user.login}</td>
                <td>${data.user.firstName}</td>
                <td>${data.user.lastName}</td>
                <td><button id="js-modal-status" class="uk-button uk-button-default" type="button" name="userR"
                                    value="${data.user.id}" onclick="changeRoleButton(${data.user.id})">${userRole}</button>
                </td>
                <td>${data.user.dayOfBirth}</td>
                <td>${data.user.sex}</td>
                <td><button id="js-modal-status" class="uk-button uk-button-default" type="button" name="user"
                                    value="${data.user.id}" onclick="changeStatusButton(${data.user.id})">${userStatusButton}</button>
                `
                    $('#tr_' + data.user.id).html(newHtml);
                }

            } else callErrorAlert(data.message);
        })
        .catch(err => {
            callErrorAlert(err);
        });

}

function changeRoleStatus(id) {
    fetch('controller?command=roleUser', {
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        method: 'POST',
        body: JSON.stringify({userId: id,
            currentPage: currentPGPage,
            items: itemsPerPage})
    }) .then(response => response.json())
        .then(data =>  {
            if (data.status =='OK') {
                if ($('#tr_' + data.user.id).length) {
                    var userStatusButton = data.user.status == true ? "Disable User" : "Enable User";
                    var userRole = data.user.role == 0 ? "ADMIN" : "USER";
                    var newHtml = `
                <td><img class="uk-preserve-width uk-border-circle" src="images/${data.user.photo}" width="40"
                                 alt="" onclick="userInfo(${data.user.id})" uk-toggle="target: #userinfo"></td>
                <td>${data.user.login}</td>
                <td>${data.user.firstName}</td>
                <td>${data.user.lastName}</td>
                <td><button id="js-modal-status" class="uk-button uk-button-default" type="button" name="userR"
                                    value="${data.user.id}" onclick="changeRoleButton(${data.user.id})">${userRole}</button>
                </td>
                <td>${data.user.dayOfBirth}</td>
                <td>${data.user.sex}</td>
                <td><button id="js-modal-status" class="uk-button uk-button-default" type="button" name="user"
                                    value="${data.user.id}" onclick="changeStatusButton(${data.user.id})">${userStatusButton}</button>
                `
                    $('#tr_' + data.user.id).html(newHtml);
                }

            } else callErrorAlert(data.message);
        })
        .catch(err => {
            callErrorAlert(err);
        });

}

function changeStatusButton(e) {
    UIkit.modal.confirm('User status will be changed. Are you sure?').then(function () {
        changeUserStatus(e);
        console.log('User is enabled')
    }, function () {
        console.log('Canceling enable')
    });
};

function changeRoleButton(e) {
    UIkit.modal.confirm('User role will be changed. Are you sure?').then(function () {
        changeRoleStatus(e);
        console.log('User is enabled')
    }, function () {
        console.log('Canceling enable')
    });
};

function userInfo(id) {
    var usermodal = document.getElementById('userinfo')
    usermodal.innerHTML = "";
    fetch('controller?command=infoUser', {
        method: 'POST',
        body: JSON.stringify({userId: id})
    }).then(response => response.json())
        .then(data => {
            var status = data.user.status==true ? "uk-label-success" : "uk-label-danger";
            var role = data.user.role==0 ? "ADMIN" : "USER";
            var row = `<div class="uk-modal-dialog uk-modal-body">
        <h2 class="uk-modal-title">Information</h2>
        <button class="uk-modal-close-default" type="button" uk-close></button>
        <img class="uk-preserve-width uk-border-circle" src="images/${data.user.photo}" width="100"
                                 alt=""><br>
        <span class="uk-label ${status}">Status</span><br>
        <label>Login: ${data.user.login}</label><br>
        <label>First name: ${data.user.firstName}</label><br>
        <label>Last name: ${data.user.lastName}</label><br>
        <label>Day of birth: ${data.user.dayOfBirth}</label><br>
        <label>Sex: ${data.user.sex}</label><br>
        <label>Email: ${data.user.email}</label><br>
        <label>Role: ${role}</label><br>

    </div>`
            usermodal.innerHTML += row;
        });

};

function changeSort(){
    sortBy = parseInt($('#sortUsersOption').val());
    currentPGPage=1;
    callPOSTRequest(1,0);
}

function callErrorAlert(message){
    UIkit.modal.alert(message);
}
