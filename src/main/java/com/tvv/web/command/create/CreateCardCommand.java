package com.tvv.web.command.create;

import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.CardService;
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


public class CreateCardCommand extends Command {

	private static final Logger log = Logger.getLogger(CreateCardCommand.class);

	@Override
	public void executePost(HttpServletRequest request,
							HttpServletResponse response) throws IOException, ServletException {
		log.debug("Start create account POST command");

		request.setCharacterEncoding("UTF-8");

		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Role userRole = (Role) request.getSession().getAttribute("userRole");
		Map<String, String> accountData = readParemeters(request);
		accountData.put("ownerUser",currentUser.getId().toString());

		ErrorString error = new ErrorMessageEN();

		try {
			if (userRole==Role.USER) {
				CardService.createCard(accountData);
				response.sendRedirect(Path.COMMAND__LIST_CARDS);
			}
			else UtilCommand.bedGETRequest(request,response);
				//throw new AppException("You can not create account, you dont have permissions", new IllegalAccessException());
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
		result.put("cardnumber",request.getParameter("cardnumber"));

		result.put("expMM",request.getParameter("expMM"));
		result.put("expYY",request.getParameter("expYY"));

		return result;
	}

}