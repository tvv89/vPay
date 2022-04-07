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
    fetch('controller?command=updateListCard', {
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
        var status
            if (tx[i].status==true) status= `<span class="uk-label uk-label-success" 
                                            onclick="changeStatusButton(${tx[i].id})">
                                            ${javascript_general_enabled}</span>`;
            else status= `<span class="uk-label uk-label-danger" 
                                            onclick="changeStatusButton(${tx[i].id})">
                                            ${javascript_general_disabled}</span>`;
        var row = `<tr id="tr_${tx[i].id}">
                <td>${tx[i].name}</td>
                <td>${tx[i].number}</td>
                <td>${tx[i].expDate}</td>
                <td>${tx[i].user.firstName} ${tx[i].user.lastName}</td>
                <td>
                 ${status}
                    </td>
                <td></td>
                </tr>`
        table.innerHTML += row;
    }

}

function changeCardStatus(id) {
    fetch('controller?command=statusCard', {
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        method: 'POST',
        body: JSON.stringify({cardId: id})
    }) .then(response => response.json())
        .then(data =>  {
            if (data.status =='OK') {
                if ($('#tr_' + data.card.id).length) {
                    var status = "";
                    if (data.card.status==true) status= `<span class="uk-label uk-label-success" 
                                            onclick="changeStatusButton(${data.card.id})">
                                            ${javascript_general_enabled}</span>`;
                    else status= `<span class="uk-label uk-label-danger" 
                                            onclick="changeStatusButton(${data.card.id})">
                                            ${javascript_general_disabled}</span>`;
                    var newHtml = `
                <td>${data.card.name}</td>
                <td>${data.card.number}</td>
                <td>${data.card.expDate}</td>
                <td>${data.card.user.firstName} ${data.card.user.lastName}</td>
                <td>
                 ${status}
                    </td>
                <td></td>
                `
                    $('#tr_' + data.card.id).html(newHtml);
                }

            } else callErrorAlert(data.message);
        })
        .catch(err => {
            callErrorAlert(err);
        });

}

function changeStatusButton(e) {
    UIkit.modal.confirm(javascript_card_status_modal).then(function () {
        changeCardStatus(e);
        console.log('Card is enabled')
    }, function () {
        console.log('Canceling enable')
    });
};

function changeSort(){
    sortBy = parseInt($('#sortCardOption').val());
    currentPGPage=1;
    callPOSTRequest(1,0);
}

function callErrorAlert(message){
    UIkit.modal.alert(message);
}
