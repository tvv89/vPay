function getFormFieldValues(fields) {
    return Array.from(fields).reduce((formData, field) => {
        const formGroup = field.getAttribute('form-group');
        if (formGroup && !formData[formGroup]) {
            formData[formGroup] = {};
        }
        const property = formGroup ? formData[formGroup] : formData;
        const fieldType = field.type;
        const fieldName = field.getAttribute('form-field') || field.name;
        const saveRadioChecked = field.getAttribute('form-radio-checked') !== null;
        if (fieldType === 'text' || fieldType === 'password' || fieldType === 'select-one' || fieldType === 'textarea') {
            property[fieldName] = field.value;
        } else if (fieldType === 'checkbox') {
            property[fieldName] = field.checked;
        } else if (fieldType === 'radio' && !saveRadioChecked && field.checked) {
            property[fieldName] = field.value;
        } else if (fieldType === 'radio' && saveRadioChecked) {
            property[fieldName] = field.checked;
        }
        return formData;
    }, {});
}

function initLoginForm(form) {
    form.addEventListener('submit', e => { e.preventDefault(); });
    const formFields = form.querySelectorAll('[form-field]');
    const submitButton = form.querySelector('input[type="submit"]');
    submitButton.addEventListener('click', () => {
        const loginData = getFormFieldValues(formFields);
        fetch('/controller?command=createUser', {
            method: 'POST',
            body: JSON.stringify(loginData)
        })
            .then(response => response.json())
            .then(data =>  {
                // here code if BE response was successful
            })
            .catch(err => {
                // here code if BE responses with error
            });
    });
}

window.addEventListener('DOMContentLoaded', () => {
    initLoginForm(document.querySelector('[data-action="register-form"]'));
});
