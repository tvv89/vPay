package com.tvv.web.webutil;

public class ErrorMessageUK implements ErrorString{

    @Override
    public String no18YearsOld() {
        return "Вам повинно бути 18 років";
    }

    @Override
    public String badFirstName() {
        return "Не коректне імя";
    }

    @Override
    public String badLastName() {
        return "Не коректне прізвище";
    }

    @Override
    public String badEmail() {
        return "Не коректна email адреса";
    }

    @Override
    public String badPassword() {
        return "Пасворд повинен: містити 4 і більше символів; 1 малу літеру; 1 велику літеру; 1 цифру; 1 символ";
    }

    @Override
    public String bedConfirmPassword() {
        return "Не вірне повторне введення паролю, перевірте правильність підтвердження вводу";
    }

    @Override
    public String notEnoughMoney() {
        return "Не достатньо коштів на рахунку";
    }


}
