package com.tvv.web.servlet;

import com.tvv.web.command.Command;
import com.tvv.web.command.CommandCollection;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "Controller", value = "/controller")
public class Controller extends HttpServlet {

    private static final Logger log = Logger.getLogger(Controller.class);

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        process(request, response);

    }

    private void process(HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {

        log.debug("Controller starts");

        String commandName = request.getParameter("command");
        log.trace("Request parameter command: " + commandName);
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        Command command = CommandCollection.get(commandName);
        log.trace("Command is" + command);

        String forward = command.execute(request, response);
        log.trace("Forward address " + forward);

        log.debug("Controller finished, forward address " + forward);

        if (forward != null) {
            if ("GET".equals(request.getMethod())){
                RequestDispatcher disp = request.getRequestDispatcher(forward);
                disp.forward(request, response);}
            if ("POST".equals(request.getMethod())){
                response.sendRedirect(forward);
            }

        }
    }
}