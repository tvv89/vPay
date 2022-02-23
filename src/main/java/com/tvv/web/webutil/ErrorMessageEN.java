package com.tvv.web.webutil;

public class ErrorMessageEN implements ErrorString{

    @Override
    public String no18YearsOld() {
        return "You have to be 18 years old";
    }

    @Override
    public String badFirstName() {
        return "First name incorrect";
    }

    @Override
    public String badLastName() {
        return "Last name incorrect";
    }

    @Override
    public String badEmail() {
        return "Incorrect email address";
    }

    @Override
    public String badPassword() {
        return "Password must have: more then 4 symbols; 1 lowercase letter; 1 uppercase letter; 1 symbol";
    }

    @Override
    public String bedConfirmPassword() {
        return "Password mismatch, please check password and confirm password";
    }
}
