package com.tvv.web.servlet;

import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.User;
import com.tvv.utils.FieldsChecker;
import com.tvv.utils.StringHash;
import com.tvv.web.webutil.ErrorMessageEN;
import com.tvv.web.webutil.ErrorString;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "Registration", value = "/registration")
public class Registration extends HttpServlet {

    private static final Logger log = Logger.getLogger(Registration.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__REGISTRATION);
        disp.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Start registration servlet");
        String forward = Path.PAGE__LOGIN;
        request.setCharacterEncoding("UTF-8");
        log.trace("Login for registration: " + request.getParameter("login"));

        Map<String,String> userData = readParemeters(request);

        ErrorString error = new ErrorMessageEN();
        LocalDate date = LocalDate.now();
        try {
            date = LocalDate.parse(userData.get("dateofbirth"));
        } catch (Exception ex)
        {
            log.debug("Bad parse date from dateOfBirth");
        }
        StringBuilder errorMessage = new StringBuilder();

        //if (!FieldsChecker.checkAge18YearsOld(date)) errorMessage.append(error.no18YearsOld()).append('\n');
        //if (!FieldsChecker.checkNameField(userData.get("firstname"))) errorMessage.append(error.badFirstName()).append('\n');
        //if (!FieldsChecker.checkNameField(userData.get("lastname"))) errorMessage.append(error.badLastName()).append('\n');
        //if (!FieldsChecker.checkEMailAddress(userData.get("email"))) errorMessage.append(error.badEmail()).append('\n');
        //if (!FieldsChecker.checkPasswordField(userData.get("password"))) errorMessage.append(error.badPassword()).append('\n');
//        if (!userData.get("password").equals(userData.get("confirmpassword"))) errorMessage.append(error.bedConfirmPassword());
        log.trace(errorMessage);
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
            if (request.getPart("photofile").getSize()>0) {
                String appPath = request.getServletContext().getRealPath("");
                // constructs path of the directory to save uploaded file
                String savePath = appPath + File.separator +
                        "images";
                Part part = request.getPart("photofile");
                File fileSaveDir = new File(savePath);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdir();
                }
                String fileName = "photo_" + userData.get("login") + ".jpg";
                fileName = new File(fileName).getName();
                part.write(savePath + File.separator + fileName);
                user.setPhoto(fileName);

                //Part filePart =  ;
                //String fileName = "photo"+request.getParameter("login")+".jpg";
                //filePart.write(request.getServletContext().getRealPath(".")+"images/"+fileName);

            } else user.setPhoto("");
            user.setStatus(true);

            UserDAO.insertUser(user);
            response.sendRedirect(Path.PAGE__LOGIN);
            //request.getRequestDispatcher(Path.PAGE__LOGIN).forward(request,response);
        }
        else {
            log.trace(errorMessage);
            forward = Path.PAGE__ERROR_PAGE;
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("errorCode", 1);
            response.sendRedirect(forward);
            //RequestDispatcher disp = request.getRequestDispatcher("error");
            //disp.forward(request, response);

        }




    }

    private Map<String, String> readParemeters(HttpServletRequest request) {
        Map<String,String> result = new HashMap<>();

        result.put("login",request.getParameter("login"));
        result.put("password",request.getParameter("password"));
        result.put("confirmpassword",request.getParameter("confirmpassword"));
        result.put("email",request.getParameter("email"));
        result.put("firstname",request.getParameter("firstname"));
        result.put("lastname",request.getParameter("lastname"));
        result.put("dateofbirth",request.getParameter("dateofbirth"));
        result.put("sex",request.getParameter("sex"));

        return result;
    }


}
