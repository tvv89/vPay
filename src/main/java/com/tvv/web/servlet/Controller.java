package com.tvv.web.servlet;

import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.command.CommandCollection;
import com.tvv.web.command.UtilCommand;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

/**
 * Main servlet controller. Application uses one controller and different command.
 *
 */
@WebServlet(name = "Controller", value = "/controller")
@MultipartConfig
public class Controller extends HttpServlet {

    private static final Logger log = Logger.getLogger(Controller.class);

    /**
     * Function for GET request. Use command GET function
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        String commandName = request.getParameter("command");
        log.trace("Request parameter command GET: " + commandName);

        Command command = CommandCollection.get(commandName);
        log.trace("Command is " + command);

        try {
            /**
             * GET executed function
             */
            command.executeGet(request, response);
        } catch (ServletException | NullPointerException e) {
            /**
             * Show error page
             */
            request.getSession().setAttribute("errorHeader", "404");
            request.getSession().setAttribute("errorMessage", "Page not found");
            UtilCommand.goToErrorPage(request,response);
        }

        log.debug("Controller finished GET with " + commandName);

    }

    /**
     * Function for POST request. Use command POST function
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        /**
         * Read command name
         */
        String commandName = request.getParameter("command");
        log.trace("Request parameter command POST: " + commandName);

        Command command = CommandCollection.get(commandName);
        log.trace("Command is " + command);

        try {
            /**
             * POST executed function
             */
            command.executePost(request, response);
        } catch (ServletException | NullPointerException |AppException e) {
            /**
             * Show error page
             */
            request.getSession().setAttribute("errorHeader", "404");
            request.getSession().setAttribute("errorMessage", "Page not found "+ e.getMessage());
            UtilCommand.goToErrorPage(request,response);
        }

        log.debug("Controller finished POST with " + commandName);

    }


}