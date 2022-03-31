package com.tvv.web.command.create;

import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.AccountService;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.command.UtilCommand;
import com.tvv.web.webutil.ErrorMessageEN;
import com.tvv.web.webutil.ErrorString;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class CreateAccountCommand extends Command {

	private static final Logger log = Logger.getLogger(CreateAccountCommand.class);

	@Override
	public void executePost(HttpServletRequest request,
							HttpServletResponse response) throws IOException, ServletException {
		log.debug("Start create account POST command");

		request.setCharacterEncoding("UTF-8");
		log.trace("Login for registration: " + request.getParameter("login"));

		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Role userRole = (Role) request.getSession().getAttribute("userRole");
		Map<String, String> accountData = readParemeters(request);

		ErrorString error = new ErrorMessageEN();

		try {
			if (userRole==Role.USER) {
				AccountService.createAccount(accountData, currentUser);
				response.sendRedirect(Path.COMMAND__LIST_ACCOUNTS);
			}
			else UtilCommand.bedGETRequest(request,response);
		} catch (AppException e) {
			log.trace(e.getMessage());
			String forward = request.getContextPath()+ Path.PAGE__ERROR_PAGE;

			request.setAttribute("errorHeader", error.errorHeaderUser());
			request.setAttribute("errorMessage", e.getMessage());

			RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__ERROR_PAGE);
			disp.forward(request, response);
		}

		log.debug("Finish create account POST command.");
	}

	@Override
	public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		UtilCommand.bedGETRequest(request,response);
	}

	private Map<String, String> readParemeters(HttpServletRequest request) {
		Map<String,String> result = new HashMap<>();

		result.put("name",request.getParameter("name"));
		result.put("iban",request.getParameter("iban"));
		result.put("bankCode",request.getParameter("bankCode"));
		result.put("ipn",request.getParameter("ipn"));
		result.put("currency",request.getParameter("currency"));

		return result;
	}

}