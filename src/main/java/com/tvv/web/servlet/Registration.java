package com.tvv.web.servlet;

import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
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

import static com.tvv.service.UserService.createUser;

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

        request.setCharacterEncoding("UTF-8");
        log.trace("Login for registration: " + request.getParameter("login"));

        Map<String, String> userData = readParemeters(request);

        ErrorString error = new ErrorMessageEN();
        LocalDate date = LocalDate.now();
        try {
            date = LocalDate.parse(userData.get("dateofbirth"));
        } catch (Exception ex) {
            log.debug("Bad parse date from dateOfBirth");
        }

        try {
            createUser(userData);
            response.sendRedirect(Path.PAGE__LOGIN);
        } catch (AppException e) {
            log.trace(e.getMessage());
            forward = Path.PAGE__ERROR_PAGE;
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("errorCode", 1);
            response.sendRedirect(forward);
        }

        //request.getRequestDispatcher(Path.PAGE__LOGIN).forward(request,response);
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
