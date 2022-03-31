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

public class LoadListAccountsCommand extends Command {

    private static final Logger log = Logger.getLogger(LoadListAccountsCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        process(request,response);

    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        process(request,response);

    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start load command with method" + request.getMethod());
        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("userRole");
        if (userRole!= Role.ADMIN && userRole!=Role.USER) response.sendRedirect(request.getContextPath() + Path.COMMAND__START_PAGE);
        else {
            request.getSession().setAttribute("currentPage", "accounts");
            RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__LIST_ACCOUNTS);
            disp.forward(request, response);
        }
        log.trace("Forward to: " + Path.PAGE__LIST_ACCOUNTS);
    }
}
