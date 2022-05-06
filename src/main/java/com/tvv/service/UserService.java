package com.tvv.service;

import com.tvv.db.dao.UserDAO;
import com.tvv.db.dao.UserDAOImpl;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.utils.FieldsChecker;
import com.tvv.utils.StringHash;
import com.tvv.utils.SystemParameters;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Business logic for User
 */
public class UserService {
    private static final Logger log = Logger.getLogger(UserService.class);
    private UserDAO userDAO;
    private String locale;
    private ResourceBundle message;

    public UserService() {
        userDAO = new UserDAOImpl();
        locale = "";
        try {
            message = SystemParameters.getLocale(locale);
        } catch (IOException e) {
            log.error("Locale error");
        }
    }

    public void setUp(UserDAO userDAO){
        this.userDAO = userDAO;
        locale = "";
        try {
            message = SystemParameters.getLocale(locale);
        } catch (IOException e) {
            log.error("Locale error");
        }
    }
    public void setUpLocale(String locale){
        try {
            message = SystemParameters.getLocale(locale);
        } catch (IOException e) {
            log.error("Locale error");
        }
    }

    /**
     * Create user function
     * @param userData Map with user fields values
     * @return successful operation
     * @throws AppException
     */
    public void createUser (Map<String,String> userData, String local) throws AppException, IOException {
        StringBuilder errorMessage = new StringBuilder();
        ResourceBundle error = SystemParameters.getLocale(local);
        LocalDate date;
        try {
            date = LocalDate.parse(userData.get("dateofbirth"));
        }
        catch (Exception ex)  {
            date = LocalDate.now();
        }

        /**
         * Check field for creating user
         */
        if (!FieldsChecker.checkAge18YearsOld(date))
            errorMessage.append(error.getString("error.user.create.no18_years_old")).append("<br/>");
        if (!FieldsChecker.checkNameField(userData.get("firstname")))
            errorMessage.append(error.getString("error.user.create.first_name")).append("<br/>");
        if (!FieldsChecker.checkNameField(userData.get("lastname")))
            errorMessage.append(error.getString("error.user.create.last_name")).append("<br/>");
        if (!FieldsChecker.checkEMailAddress(userData.get("email")))
            errorMessage.append(error.getString("error.user.create.email")).append("<br/>");
        if (!FieldsChecker.checkPasswordField(userData.get("password")))
            errorMessage.append(error.getString("error.user.create.password")).append("<br/>");
        if (!userData.get("password").equals(userData.get("confirmpassword")))
            errorMessage.append(error.getString("error.user.create.confirm_password"));
        /**
         * if all fields are OK - create user
         */
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

            userDAO.insertUser(user);
        }
        else throw new AppException(errorMessage.toString(), new IllegalArgumentException());
    }

    /**
     * Hide user first and last name "user name" -> "us** **me"
     * @param firstName user first name
     * @param lastName user last name
     * @return hide name string
     */
    public static String hideUserName(String firstName, String lastName) {
        StringBuilder result = new StringBuilder();
        String fn;
        String ln;
        if (firstName==null) fn = "";
        else fn = firstName.toUpperCase(Locale.ROOT);
        if (lastName==null) ln = "";
        else ln = lastName.toUpperCase(Locale.ROOT);
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
