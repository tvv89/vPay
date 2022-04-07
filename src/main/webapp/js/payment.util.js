var itemsPerPage=5;
var currentPGPage=1;
var sortBy=4;
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
        var statusP;
            if (tx[i].status=='Ready') statusP = `<span class="uk-label uk-label-warning" 
                                            onclick="changeStatusButton(${tx[i].id})">
                                            ${javascript_payment_status_ready}</span>`;
            else statusP = `<span class="uk-label uk-label-success" 
                                            onclick="">
                                            ${javascript_payment_status_submitted}</span>`;
        var row = `<tr id="tr_${tx[i].id}">
                <td>${tx[i].timeOfLog}</td>
                <td>${tx[i].guid}</td>
                <td>${tx[i].user.firstName} ${tx[i].user.lastName}</td>
                <td>${tx[i].sender.name}</td>
                <td>${tx[i].recipientId}</td>
                <td>${tx[i].currency}</td>
                <td>${tx[i].commission}</td>
                <td>${tx[i].total}</td>
                <td id="td_status_${tx[i].id}">${statusP}</td>
                <td><a uk-icon="icon: file-pdf; ratio: 1.5" onclick="pdfPayment(${tx[i].id})"></a>
                <a uk-icon="icon: trash; ratio: 1.5" onclick="deletePayment(${tx[i].id})"></a></td>
                </tr>`
        table.innerHTML += row;
    }

}

function changePaymentStatus(id) {
    fetch('controller?command=statusPayment', {
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        method: 'POST',
        body: JSON.stringify({
            action: 'status',
            paymentId: id})
    }) .then(response => response.json())
        .then(data =>  {
            if (data.status =='OK') {
                if ($('#td_status_' + data.id).length) {
                    if (data.statusPayment == 'Submitted') {
                        statusP = `<span class="uk-label uk-label-success" 
                                         onclick="">
                                         ${javascript_payment_status_submitted}</span>`;
                        $('#td_status_' + data.id).html(statusP);
                    }
                }
            } else callErrorAlert(data.message);
        })
        .catch(err => {
            callErrorAlert(err);
        });

}

function deletePayment(id) {
    UIkit.modal.confirm('Payment status will be changed to submitted. Are you sure?').then(function () {
        fetch('controller?command=statusPayment', {
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            },
            method: 'POST',
            body: JSON.stringify({action: 'delete',
                paymentId: id})
        }) .then(response => response.json())
            .then(data =>  {
                if (data.status =='OK') {
                    callPOSTRequest(1,0);
                    UIkit.notification({
                        message: javascript_payment_delete_message,
                        status: 'success',
                        timeout: 2000
                    });
                } else callErrorAlert(data.message);
            })
            .catch(err => {
                callErrorAlert(err);
            });

        console.log('Payment is deleted')
    }, function () {
        console.log('Canceling enable')
    });

}

function pdfPayment(id) {
    fetch('controller?command=statusPayment', {
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        method: 'POST',
        responseType: "arraybuffer",
        body: JSON.stringify({
            action: 'pdf',
            paymentId: id
        })
    }).then(response => {return response.arrayBuffer()})
        .then(data => {
        const blob = new Blob([data], { type: 'application/pdf' })
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob)
        link.download = 'payment.pdf';
        link.click()
    });
    console.log('Payment in PDF')
}

function changeStatusButton(e) {
    UIkit.modal.confirm(javascript_payment_status_modal_message).then(function () {
        changePaymentStatus(e);
        console.log('Payment is submitted')
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
