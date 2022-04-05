package com.tvv.utils;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util: check string fields
 */
public class FieldsChecker {

    /**
     * Check user first name or last name
     * @param name
     * @return correct or not
     */
    public static boolean checkNameField(String name) {
        String regex = "^([A-Za-zА-Яа-яїЇёЁъЪ]+)";
        return checkRegEx(name,regex);
    }

    /**
     * Check email address
     * @param email String with email
     * @return correct or not
     */
    public static boolean checkEMailAddress(String email) {
        String regex = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";
        return checkRegEx(email,regex);
    }

    /**
     * Check password
     * @param password String with password
     * @return correct or not
     */
    public static boolean checkPasswordField (String password) {
        String regex = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9@#$%]).{4,}";
        return checkRegEx(password,regex);
    }

    /**
     * Check IPN
     * @param ipn String with IPN
     * @return correct or not
     */
    public static boolean checkIPN (String ipn) {
        String regex = "[0-9]{10}";
        return checkRegEx(ipn,regex);
    }

    /**
     * Check bank code
     * @param code String with bank code
     * @return correct or not
     */
    public static boolean checkBankCode (String code) {
        String regex = "[0-9]{6}";
        return checkRegEx(code,regex);
    }

    /**
     * Check credit card number
     * @param number String with card number
     * @return correct or not
     */
    public static boolean checkCardNumber (String number) {
        String regex = "^[0-9]{12}(?:[0-9]{4})?$";
        return checkRegEx(number,regex);
    }

    /**
     * Check balance double format "+#.##"
     * @param value String double value
     * @return correct or not
     */
    public static boolean checkBalanceDouble(String value) {
        String regex = "^(\\d+(\\.\\d{0,2})?|\\.?\\d{1,2})$";
        return checkRegEx(value,regex);
    }

    /**
     * Check confirm equals password
     * @param password String password line
     * @param confirmPassword String confirm password
     * @return correct or not
     */
    public static boolean checkEqualsPassword (String password, String confirmPassword){
        return password.equals(confirmPassword);
    }

    /**
     * Check 18 years old for user
     * @param date LocalDate date of birth
     * @return correct or not
     */
    public static boolean checkAge18YearsOld(LocalDate date) {
        if (date==null) return false;
        return (Period.between(date, LocalDate.now()).getYears()>=18);
    }

    /**
     * Function for checking regex
     * @param field string value
     * @param regex regex value
     * @return result of checking
     */
    private static boolean checkRegEx(String field, String regex){
        if (field==null) return false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(field);
        return matcher.matches();
    }
}
