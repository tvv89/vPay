package com.tvv.web.command.load;

import com.tvv.db.entity.Role;
import com.tvv.web.command.Command;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * This command show page with list of Payment
 */
public class LoadListPaymentsCommand extends Command {

    private static final Logger log = Logger.getLogger(LoadListPaymentsCommand.class);

    /**
     * Function for GET request. This function redirect user to list payment
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

           process(request,response);

    }

    /**
     * Function for POST request. This function redirect user to list payment
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

            process(request,response);

    }

    /**
     * Main function is the same for POST and GET method. Forward user to list of payment. If user didn't
     * authorize, command redirect him to start page
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start load command with method" + request.getMethod());
        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("userRole");
        if (userRole!= Role.ADMIN && userRole!=Role.USER) response.sendRedirect(request.getContextPath() + Path.COMMAND__START_PAGE);
        else {
            request.getSession().setAttribute("currentPage", "payments");
            RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__LIST_PAYMENT);
            disp.forward(request, response);
            log.trace("Forward to: " + Path.PAGE__LIST_PAYMENT);
        }
        log.trace("Finish load command with method" + request.getMethod());

    }
}
