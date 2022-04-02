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
    fetch('controller?command=updateListAccount', {
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
                createTable(data.list, data.cards, data.userRole);
            } else callErrorAlert(data.message);
        })
        .catch(err => {
            callErrorAlert(err);
        });
}

function createTable(tx,cards,userRole) {
    var table = document.getElementById('table')
    table.innerHTML = "";
    for (var i = 0; i < tx.length; i++) {
        var accountStatusButton;
        var statusStyle;
        if (tx[i].status == "Enabled") {
            accountStatusButton = "Disable Account";
            statusStyle =  "uk-label-success";
        }
        else if (tx[i].status == "Disabled") {
            accountStatusButton = "Enable Account";
            statusStyle =  "uk-label-danger";

        }
        else {
            accountStatusButton = "Waiting for enable";
            statusStyle =  "uk-label-warning";
        }

        if (tx[i].status != "Enabled" && userRole == "USER") statusStyle += " uk-disabled";
        var statusButtonTopUp = "";
        if (userRole != 'USER') statusButtonTopUp = "display:none;";
        var selectcard = ` <label>Added card to account</label></br>
                            <select class="uk-select uk-width-1-1@m" 
                                  id="selectCardAccount_${tx[i].id}" 
                                  name="selectCardAccount_${tx[i].id}" 
                                  form-field>
                            <option value=-1>-none-</option>`
        for (var j = 0; j < cards.length; j++) {
            var dangerCard = "";
            if (cards[j].status == false) dangerCard="uk-text-danger";
            selectcard += `<option class="${dangerCard}" value=${cards[j].id}>${cards[j].name}</option>`
        }
            selectcard += `</select></br>
                           <button id="apply" 
                                    class="uk-button uk-button-primary uk-width-1-1@m" 
                                    type="button"
                                    value="${tx[i].id}" 
                                    onclick="selectCardFunction(${tx[i].id})">Applay
                            </button>`
        var row = `<tr id="tr_${tx[i].id}">
                <td>${tx[i].name}</td>
                <td>${tx[i].iban}</td>
                <td>${tx[i].currency}</td>
                <td id="td_balance_${tx[i].id}">${tx[i].balance}</td>
                <td>${tx[i].ownerUser.firstName} ${tx[i].ownerUser.lastName}</td>
                <td id="td_status_${tx[i].id}">
                <span class="uk-label ${statusStyle}" 
                                            onclick="changeStatusButton(${tx[i].id})">
                                            ${accountStatusButton}</span>        
                </td>
                <td id="td_addbalance_${tx[i].id}" class="" style="${statusButtonTopUp}">
                    <button id="js-modal-status-add" 
                                    class="uk-button ${tx[i].status != "Enabled" ? 'uk-disabled' : 'uk-button-secondary'}  uk-width-1-1@m" 
                                    type="button" 
                                    name="topup"
                                    value="${tx[i].id}" 
                                    onclick="addCoinButton(${tx[i].id})">Top up
                    </button></br>
                    <button id="js-modal-status-delete" 
                                    class="uk-button uk-button-danger uk-width-1-1@m" 
                                    type="button" 
                                    name="delete"
                                    value="${tx[i].id}" 
                                    onclick="removeAccountButton(${tx[i].id})">Remove
                    </button></br>
                    <button id="js-modal-info-card" 
                                    class="uk-button uk-button-primary uk-width-1-1@m" 
                                    type="button" 
                                    name="infocard"
                                    value="${tx[i].id}" 
                                    onclick="cardInfoFunction(${tx[i].id})" uk-toggle="target: #cardinfo">Card
                    </button>                
                </td>
                <td style="${statusButtonTopUp}">`
                 + selectcard +
                `</td>
                </tr>`
        table.innerHTML += row;
    }
    for (let i = 0; i < tx.length; i++) {
        if (typeof tx[i].card !== 'undefined') {
            document.getElementById('selectCardAccount_'+tx[i].id).value = tx[i].card.id;
        } else {
            document.getElementById('selectCardAccount_'+tx[i].id).value= -1;
         }
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

function changeAccountFunction(id) {
    fetch('controller?command=statusAccount', {
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        method: 'POST',
        body: JSON.stringify({accountId: id, action: 'change'})
    }) .then(response => response.json())
        .then(data =>  {
            if (data.status =='OK') {
                if ($('#tr_' + data.account.id).length) {
                    var accountStatusButton;
                    var statusStyle;
                    if (data.account.status == "Enabled") {
                        accountStatusButton = "Disable Account";
                        statusStyle =  "uk-label-success";
                    }
                    else if (data.account.status == "Disabled") {
                        accountStatusButton = "Enable Account";
                        statusStyle =  "uk-label-danger";

                    }
                    else {
                        accountStatusButton = "Waiting for enable";
                        statusStyle =  "uk-label-warning";
                    }
                    if (data.account.status != "Enabled" && data.userRole=="USER") statusStyle += " uk-disabled";
                    var newHtmlStatus =
                    `<span class="uk-label ${statusStyle}" 
                                            onclick="changeStatusButton(${data.account.id})">
                                            ${accountStatusButton}</span> `
                    $('#td_status_' + data.account.id).html(newHtmlStatus);
                    var newHtmlTopUp =
                        `<button id="js-modal-status-add" 
                                    class="uk-button ${data.account.status != "Enabled" ? 'uk-disabled' : 'uk-button-secondary'} uk-width-1-1@m" 
                                    type="button" 
                                    name="topup"
                                    value="${data.account.id}" onclick="addCoinButton(${data.account.id})">Top up
                        </button></br>
                        <button id="js-modal-status-delete" 
                                    class="uk-button uk-button-danger uk-width-1-1@m" 
                                    type="button" 
                                    name="delete"
                                    value="${data.account.id}" 
                                    onclick="removeAccountButton(${data.account.id})">Remove
                        </button></br>
                        <button id="js-modal-status-card" 
                                    class="uk-button uk-button-primary uk-width-1-1@m" 
                                    type="button" 
                                    name="delete"
                                    value="${data.account.id}" 
                                    onclick="cardActionButton(${data.account.id})">Card
                        </button> `
                    $('#td_addbalance_' + data.account.id).html(newHtmlTopUp);
                }

            } else callErrorAlert(data.message);
        })
        .catch(err => {
            console.log(err);
            callErrorAlert(err);

        });
}

function cardInfoFunction(id) {
    var cardmodal = document.getElementById('cardinfo')
    cardmodal.innerHTML = "";
    fetch('controller?command=infoCard', {
        method: 'POST',
        body: JSON.stringify({accountId: id, action: 'info'})
    }).then(response => response.json())
        .then(data => {
            if (data.status=='OK') {
            var row = `
                             <div class="uk-modal-dialog uk-modal-body uk-margin uk-position-top-center">

                               <div class="uk-inline uk-align-center">
                                   <img src="images/__card_blank.png" width="324" height="196" alt="">
                                   <label class="uk-position-absolute uk-text-large" style="left: 10%; top: 10%" >${data.cardname}</label>
                                   <label class="uk-position-absolute uk-text-large" style="left: 10%; top: 50%" >${data.cardnumber}</label>
                                   <label class="uk-position-absolute uk-text-large" style="left: 43%; top: 65%" >${data.expdate}</label>
                               </div>
                               </br>
                               </br>
                               <button class="uk-button uk-button-default uk-modal-close" type="button">OK</button>
                             </div>
                       `
                cardmodal.innerHTML += row;
            } else {
                var row = `
                             <div class="uk-modal-dialog uk-modal-body">
                               <div class="uk-inline uk-dark">
                                   <p>${data.message}</p>
                                   
                               </div>
                               </br>
                               <button class="uk-button uk-button-default uk-modal-close" type="button">OK</button>
                             </div>
                        `
                cardmodal.innerHTML += row;
            }
        });

};

function selectCardFunction(id) {
    var cardId = document.getElementById('selectCardAccount_'+id).value;
    fetch('controller?command=infoCard', {
        method: 'POST',
        body: JSON.stringify({action: 'select', accountId: id, card: cardId})
    }).then(response => response.json())
        .then(data => {
            if (data.status=='OK') {
                UIkit.notification({message: 'Success! Card is changed.',
                    status: 'success',
                    timeout: 2000});
            } else {
                callErrorAlert(data.message);
            }
        });

};

function addCoinFunction(id,value) {
    fetch('controller?command=addCoin', {
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        method: 'POST',
        body: JSON.stringify({accountId: id,
        coin: value})
    }) .then(response => response.json())
        .then(data =>  {
            if (data.status =='OK') {
                if ($('#tr_' + data.account.id).length) {
                    var newHtml = `${data.account.balance}`
                    $('#td_balance_' + data.account.id).html(newHtml);
                    UIkit.notification({message: 'Success! Balance is changed.',
                                        status: 'success',
                                        timeout: 2000});
                }
            } else callErrorAlert(data.message);
        })
        .catch(err => {
            callErrorAlert(err);
        });
}

function removeAccountFunction(id,value) {
    fetch('controller?command=statusAccount', {
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
        method: 'POST',
        body: JSON.stringify({
            accountId: id,
            action: 'delete'
        })
    }).then(response => response.json())
        .then(data => {
            if (data.status == 'OK') {
                callPOSTRequest(1, 0);
                UIkit.notification({
                    message: 'Success! Account was deleted.',
                    status: 'success',
                    timeout: 2000
                });
            } else callErrorAlert(data.message);
        })
        .catch(err => {
            callErrorAlert(err);
        });
}

function changeStatusButton(e) {
    UIkit.modal.confirm('Account status will be changed. Are you sure?').then(function () {
        changeAccountFunction(e);
        console.log('Account is enabled')
    }, function () {
        console.log('Canceling enable')
    });
};

function addCoinButton(e) {
    UIkit.modal.prompt('How much money do you want to add?').then(function (value) {
        var regex = new RegExp("^\\d+(?:\\.\\d{0,2})?$");
        if(regex.test(value)) {
            addCoinFunction(e,value);
            console.log('Coins is added')
        }else {
            UIkit.modal.dialog('<p class="uk-modal-body">Incorrect data</p>');
        }

    }, function () {
        console.log('Canceling')
    });
};

function removeAccountButton(e) {
    UIkit.modal.confirm('Do you want to delete this account?').then(function () {
            removeAccountFunction(e);
            console.log('Account was deleted')
    }, function () {
        console.log('Canceling')
    });
};

function changeSort(){
    sortBy = parseInt($('#sortAccountsOption').val());
    currentPGPage=1;
    callPOSTRequest(1, 0);
}

function callErrorAlert(message){
    console.log(message);
    UIkit.modal.alert(message);
}
