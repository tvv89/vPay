package com.tvv.service;

import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.utils.StringHash;

import javax.servlet.http.Part;
import java.io.File;
import java.time.LocalDate;
import java.util.Map;

public class UserService {
    public static void createUser (Map<String,String> userData) throws AppException {
        StringBuilder errorMessage = new StringBuilder();
        LocalDate date = LocalDate.parse(userData.get("dateofbirth"));
        //if (!FieldsChecker.checkAge18YearsOld(date)) errorMessage.append(error.no18YearsOld()).append('\n');
        //if (!FieldsChecker.checkNameField(userData.get("firstname"))) errorMessage.append(error.badFirstName()).append('\n');
        //if (!FieldsChecker.checkNameField(userData.get("lastname"))) errorMessage.append(error.badLastName()).append('\n');
        //if (!FieldsChecker.checkEMailAddress(userData.get("email"))) errorMessage.append(error.badEmail()).append('\n');
        //if (!FieldsChecker.checkPasswordField(userData.get("password"))) errorMessage.append(error.badPassword()).append('\n');
        //if (!userData.get("password").equals(userData.get("confirmpassword"))) errorMessage.append(error.bedConfirmPassword());
        if (errorMessage.length()==0) {

            User user = new User();
            user.setId(1L);
            user.setLogin(userData.get("login"));
            user.setPassword(StringHash.getHashString(userData.get("password")));
            user.setEmail(userData.get("email"));
            user.setFirstName(userData.get("firstname"));
            user.setLastName(userData.get("lastname"));
            user.setDayOfBirth(date);
            user.setSex(userData.get("sex"));
            user.setGender("");
            user.setRole(1);
            user.setPhoto("");
            user.setStatus(true);

            UserDAO.insertUser(user);
        }
        else throw new AppException("Can not create user", new IllegalArgumentException());
    }
}
