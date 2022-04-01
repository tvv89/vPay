package com.tvv.web.command.load;

import com.tvv.db.dao.AccountDAO;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoadListCardsCommand extends Command {

    private static final Logger log = Logger.getLogger(LoadListCardsCommand.class);

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
        User currentUser = (User) session.getAttribute("currentUser");
        if (userRole != Role.ADMIN && userRole != Role.USER)
            response.sendRedirect(request.getContextPath() + Path.COMMAND__START_PAGE);
        else {
            if (userRole == Role.USER) {
                try {
                    session.setAttribute("userAccounts", AccountDAO.findAccountByUserId(currentUser.getId()));
                } catch (AppException e) {
                    log.error(e.getMessage());
                }
            }
            request.getSession().setAttribute("currentPage", "cards");
            RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__LIST_CARDS);
            disp.forward(request, response);

        }
        log.trace("Forward to: " + Path.PAGE__LIST_CARDS);
    }
}
