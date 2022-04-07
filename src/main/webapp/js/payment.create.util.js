var accountCurrency='';
var sortBy=1;
window.addEventListener('DOMContentLoaded', (event) => {
    changeAccount();
});

function changeAccount(){
    var currentAccountId = parseInt($('#currentPaymentAccount').val());
    fetch('controller?command=createPayment', {
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        method: 'POST',
        body: JSON.stringify({
            action: 'selectAccount',
            accountId: currentAccountId})
    }) .then(response => response.json())
        .then(data =>  {
            if (data.status =='OK') {
                accountCurrency = data.account.currency;
                $('#accountName').html(data.account.name);
                $('#accountStatus').html(data.account.status);
                $('#accountUID').html(data.account.iban);
                $('#accountBalance').html(data.account.balance);
                $('#accountCurrency').html(data.account.currency);

            } else callErrorAlert(data.message);
        })
        .catch(err => {
            callErrorAlert(err);
        });
}

function checkAccount(){
    var accountType = $('#recipientType').val();
    var accountNumber = $('#recipientNumber').val();

    fetch('controller?command=createPayment', {
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        method: 'POST',
        body: JSON.stringify({
            action: 'checkAccount',
            accountType: accountType,
            accountNumber: accountNumber})
    }) .then(response => response.json())
        .then(data =>  {
            if (data.status =='OK') {
                $('#accountOwner').html(data.name);
                $('#currencyOfPayment').val(data.currency);
            } else callErrorAlert(data.message);
        })
        .catch(err => {
            callErrorAlert(err);
        });
}

function calculatePayment(){
    var accountType = $('#recipientType').val();
    var accountNumber = $('#recipientNumber').val();
    var currencyOfPayment = $('#currencyOfPayment').val();
    var statusPayment = $('input[name=statusPayment]:checked').val();
    var valueOfPayment = $('#valueOfPayment').val();

    fetch('controller?command=createPayment', {
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        method: 'POST',
        body: JSON.stringify({
            action: 'calculatePayment',
            accountType: accountType,
            accountNumber: accountNumber,
            currencyFrom: accountCurrency,
            currencyTo: currencyOfPayment,
            value: valueOfPayment,
            status: statusPayment})
    }) .then(response => response.json())
        .then(data =>  {
            if (data.status =='OK') {
                $('#commission').html(data.commissionValue + ' ' +data.currency);
                $('#total').html(data.totalPayment + ' ' +data.currency);
            } else callErrorAlert(data.message);
        })
        .catch(err => {
            callErrorAlert(err);
        });
}

function submitPayment(){
    var accountType = $('#recipientType').val();
    var accountNumber = $('#recipientNumber').val();
    var currencyOfPayment = $('#currencyOfPayment').val();
    var statusPayment = $('input[name=statusPayment]:checked').val();
    var valueOfPayment = $('#valueOfPayment').val();
    var currentAccountId = parseInt($('#currentPaymentAccount').val());

    fetch('controller?command=createPayment', {
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        method: 'POST',
        body: JSON.stringify({
            action: 'createPayment',
            accountFromId: currentAccountId,
            accountType: accountType,
            accountNumber: accountNumber,
            currencyFrom: accountCurrency,
            currencyTo: currencyOfPayment,
            value: valueOfPayment,
            status: statusPayment})
    }) .then(response => response.json())
        .then(data =>  {
            if (data.status =='OK') {
                window.location.href="controller?command=listPayments";
            } else callErrorAlert(data.message);
        })
        .catch(err => {
            callErrorAlert(err);
        });
}

function callErrorAlert(message){
    UIkit.modal.alert(message);
}
