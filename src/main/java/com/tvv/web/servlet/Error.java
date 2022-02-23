package com.tvv.web.servlet;

import com.tvv.web.webutil.Path;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "Error", value = "/error")
public class Error extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String forward = Path.PAGE__ERROR_PAGE;
        System.out.println("error: " + request.getParameter("errorMessage"));
        RequestDispatcher disp = request.getRequestDispatcher(forward);
        disp.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
