package com.tvv.web.servlet;

import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.command.CommandCollection;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "Controller", value = "/controller")
@MultipartConfig
public class Controller extends HttpServlet {

    private static final Logger log = Logger.getLogger(Controller.class);

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        String commandName = request.getParameter("command");
        log.trace("Request parameter command GET: " + commandName);

        Command command = CommandCollection.get(commandName);
        log.trace("Command is " + command);

        try {
            command.executeGet(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }

        log.debug("Controller finished GET with " + commandName);

    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        String commandName = request.getParameter("command");
        log.trace("Request parameter command POST: " + commandName);

        Command command = CommandCollection.get(commandName);
        log.trace("Command is " + command);

        try {
            command.executePost(request, response);
        } catch (ServletException | AppException e) {
            e.printStackTrace();
        }

        log.debug("Controller finished POST with " + commandName);

    }


}