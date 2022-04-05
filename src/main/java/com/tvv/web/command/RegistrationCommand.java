package com.tvv.web.command;

import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Registration command
 */
public class RegistrationCommand extends Command {

    private static final Logger log = Logger.getLogger(RegistrationCommand.class);

    /**
     * Load registration page for new user
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__REGISTRATION);
        disp.forward(request, response);
        log.trace("Forward to: " + Path.PAGE__REGISTRATION);
    }

    /**
     * Load registration page for new user
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__REGISTRATION);
        disp.forward(request, response);
        log.trace("Forward to: " + Path.PAGE__REGISTRATION);
    }



}
