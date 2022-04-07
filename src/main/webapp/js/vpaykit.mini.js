function changeLanguage() {
    var language = $('#languageSelect').val();

    fetch('controller?command=language', {
        method: 'POST',
        body: JSON.stringify({language: language})
    })
        .then(data =>  {
            UIkit.notification({message: 'Language is changed',
                status: 'success',
                timeout: 2000});
            window.location.reload();
        })
        .catch(err => {
            callErrorAlert(err.toString());
        });
}

function createPagination(page,pages) {
    var paginat = document.getElementById('pagination')
    paginat.innerHTML = "";
    var row = `<li class="uk-margin-small-right ${page == 1 ? 'uk-disabled' : ''}" id="previous-page" onclick="callPOSTRequest(2,-1)">
                            <a><span class="uk-margin-small-right" uk-pagination-previous></span> ${javascript_pagination_previous}</a>
                        </li>
                        <li class="uk-margin-small uk-align-center">${javascript_pagination_page} ${page} ${javascript_pagination_of} ${pages} </li>
                        <li class="uk-margin-small-left ${page == pages ? 'uk-disabled' : ''}" id="next-page" onclick="callPOSTRequest(2,1)">
                            <a>${javascript_pagination_next} <span class="uk-margin-small-left" uk-pagination-next></span></a>
                        </li>`
    paginat.innerHTML += row;
}

function callErrorAlert(message){
    console.log(message);
    UIkit.modal.alert(message);
}
