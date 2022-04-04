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
 * This command show page with list of Users
 */
public class LoadListUsersCommand extends Command {

    private static final Logger log = Logger.getLogger(LoadListUsersCommand.class);

    /**
     * Function for GET request. This function redirect user to list Users
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
     * Function for POST request. This function redirect user to list Users
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
     * Main function is the same for POST and GET method. Forward user to list of Users. If user didn't
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
            String page = Path.PAGE__LIST_USERS;
            if (userRole != Role.ADMIN) page = Path.PAGE__ACCESS_DENIED;
            request.getSession().setAttribute("currentPage", "users");
            RequestDispatcher disp = request.getRequestDispatcher(page);
            disp.forward(request, response);
            log.trace("Forward to: " + page);

        }
        log.trace("Finish load command with method" + request.getMethod());

    }
}
