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
    fetch('controller?command=updateListPayment', {
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
        //var paymentStatusButton = tx[i].status == true ? "Disable Payment" : "Enable Payment";
        var row = `<tr id="tr_${tx[i].id}">
                <td>${tx[i].timeOfLog}</td>
                <td>${tx[i].guid}</td>
                <td>${tx[i].user.firstName} ${tx[i].user.lastName}</td>
                <td>${tx[i].senderId}</td>
                <td>${tx[i].recipientId}</td>
                <td>${tx[i].currency}</td>
                <td>${tx[i].commission}</td>
                <td>${tx[i].total}</td>
                <td>${tx[i].status}</td>
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

function changePaymentStatus(id) {
    fetch('controller?command=statusPayment', {
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        method: 'POST',
        body: JSON.stringify({paymentId: id})
    }) .then(response => response.json())
        .then(data =>  {
            if (data.status =='OK') {
                if ($('#tr_' + data.payment.id).length) {
                    var paymentStatusButton = data.payment.status == true ? "Disable payment" : "Enable payment";
                    var newHtml = `<td>${data.payment.name}</td>
                <td>${data.payment.iban}</td>
                <td>${data.payment.ipn}</td>
                <td>${data.payment.bankCode}</td>
                <td>${data.payment.currency}</td>
                <td>${data.payment.balance}</td>
                <td>${data.payment.ownerUser.firstName} ${data.payment.ownerUser.lastName}</td>
                <td><button id="js-modal-status" class="uk-button uk-button-default" type="button" name="payment"
                                    value="${data.payment.id}" onclick="changeStatusButton(${data.payment.id})">${paymentStatusButton}</button>
                </td>`
                    $('#tr_' + data.payment.id).html(newHtml);
                }

            } else callErrorAlert(data.message);
        })
        .catch(err => {
            callErrorAlert(err);
        });

}

function changeStatusButton(e) {
    UIkit.modal.confirm('Payment status will be changed. Are you sure?').then(function () {
        //changePaymentStatus(e);
        console.log('Payment is enabled')
    }, function () {
        console.log('Canceling enable')
    });
};

function changeSort(){
    sortBy = parseInt($('#sortPaymentsOption').val());
    currentPGPage=1;
    callPOSTRequest(1,0);
}

function callErrorAlert(message){
    UIkit.modal.alert(message);
}
