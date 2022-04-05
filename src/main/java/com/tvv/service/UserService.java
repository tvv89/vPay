package com.tvv.service;

import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.utils.FieldsChecker;
import com.tvv.utils.StringHash;
import com.tvv.web.webutil.ErrorMessageEN;
import com.tvv.web.webutil.ErrorString;

import javax.servlet.http.Part;
import java.io.File;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

public class UserService {

    public static void createUser (Map<String,String> userData) throws AppException {
        StringBuilder errorMessage = new StringBuilder();
        ErrorString error = new ErrorMessageEN();
        LocalDate date;
        try {
            date = LocalDate.parse(userData.get("dateofbirth"));
        }
        catch (Exception ex)  {
            date = LocalDate.now();
        }

        if (!FieldsChecker.checkAge18YearsOld(date)) errorMessage.append(error.no18YearsOld()).append("<br/>");
        if (!FieldsChecker.checkNameField(userData.get("firstname"))) errorMessage.append(error.badFirstName()).append("<br/>");
        if (!FieldsChecker.checkNameField(userData.get("lastname"))) errorMessage.append(error.badLastName()).append("<br/>");
        if (!FieldsChecker.checkEMailAddress(userData.get("email"))) errorMessage.append(error.badEmail()).append("<br/>");
        if (!FieldsChecker.checkPasswordField(userData.get("password"))) errorMessage.append(error.badPassword()).append("<br/>");
        if (!userData.get("password").equals(userData.get("confirmpassword"))) errorMessage.append(error.bedConfirmPassword());
        if (errorMessage.length()==0) {

            User user = new User();
            user.setId(1L);
            user.setLogin(userData.get("login"));
            user.setPassword(StringHash.getHashString(userData.get("password")));
            user.setEmail(userData.get("email"));
            user.setFirstName(userData.get("firstname"));
            user.setLastName(userData.get("lastname"));
            user.setDayOfBirth(userData.get("dateofbirth"));
            user.setSex(userData.get("sex"));
            user.setGender("");
            user.setRole(1);
            user.setPhoto(userData.get("photofile"));
            user.setStatus(true);

            UserDAO.insertUser(user);
        }
        else throw new AppException(errorMessage.toString(), new IllegalArgumentException());
    }

    public static String hideUserName(String firstName, String lastName) {
        StringBuilder result = new StringBuilder();
        String fn = firstName.toUpperCase(Locale.ROOT);
        String ln = lastName.toUpperCase(Locale.ROOT);
        for (int i = 0; i < fn.toCharArray().length; i++) {
            if (i< fn.toCharArray().length/2) result.append(fn.toCharArray()[i]);
            else result.append("*");
        }
        result.append(" ");
        for (int i = 0; i < ln.toCharArray().length; i++) {
            if (i >= ln.toCharArray().length/2) result.append(ln.toCharArray()[i]);
            else result.append("*");
        }
        result.append(" ");
        return result.toString();
    }

}
