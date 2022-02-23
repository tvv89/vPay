package com.tvv.utils;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldsChecker {
    public static boolean checkNameField(String name) {
        String regex = "^([A-Za-zА-Яа-яїЇёЁъЪ]+)";
        return checkRegEx(name,regex);
    }

    public static boolean checkEMailAddress(String email) {
        String regex = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";
        return checkRegEx(email,regex);
    }

    public static boolean checkPasswordField (String password) {
        String regex = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9@#$%]).{4,}";
        return checkRegEx(password,regex);
    }

    public static boolean checkIBAN (String iban) {
        String regex = "^[A-Z]{2}[0-9]{27}";
        return checkRegEx(iban,regex);
    }

    public static boolean checkIPN (String ipn) {
        String regex = "[0-9]{10}";
        return checkRegEx(ipn,regex);
    }

    public static boolean checkBankCode (String code) {
        String regex = "[0-9]{6}";
        return checkRegEx(code,regex);
    }

    public static boolean checkEqualsPassword (String password, String confirmPassword){
        return password.equals(confirmPassword);
    }


    public static boolean checkAge18YearsOld(LocalDate date) {
        if (date==null) return false;
        return (Period.between(date, LocalDate.now()).getYears()>=18);
    }

    private static boolean checkRegEx(String field, String regex){
        if (field==null) return false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(field);
        return matcher.matches();
    }
}
