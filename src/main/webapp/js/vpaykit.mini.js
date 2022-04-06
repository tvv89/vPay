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



function callErrorAlert(message){
    UIkit.modal.alert(message);
}
