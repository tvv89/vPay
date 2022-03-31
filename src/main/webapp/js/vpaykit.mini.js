var itemsPerPage=5;
var currentPage=1;
function callPOSTRequest(option, parameter) {
    var countOfUsers = $('#countOfUsers').val();
    var items = $('#itemsPerPage').val();
    switch (option){
        case 1:
            break;
        case 2:
            currentPage = currentPage + parameter;
            break;
        case 3:
            currentPage = 1;
            itemsPerPage = items;
            break;
    }
    fetch('hello-servlet', {
        method: 'POST',
        body: JSON.stringify({countOfUsers: countOfUsers,
            currentPage: currentPage,
            items: itemsPerPage})
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
        var row = `<tr>
                <td>${tx[i].id}</td>
                <td><img class="uk-preserve-width uk-border-circle" src="${tx[i].photo}" width="40"
                                 alt=""></td>
                <td>${tx[i].name}</td>
                <td>${tx[i].cost}</td>
                <td><button id="js-modal-disable" class="uk-button uk-button-default" type="button" name="user"
                                    value="${tx[i].id}">Disable User</button>
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

function callErrorAlert(message){
    UIkit.modal.alert(message);
}
