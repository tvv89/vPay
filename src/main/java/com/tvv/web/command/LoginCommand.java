package com.tvv.web.command;

import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.utils.StringHash;
import com.tvv.utils.SystemParameters;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;

/**
 * Login user command for authorisation user (check fields and user data)
 */
public class LoginCommand extends Command {

	private static final Logger log = Logger.getLogger(LoginCommand.class);

	/**
	 * Function for POST request. Check user  login and password, redirect to different page for USER and ADMIN
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void executePost(HttpServletRequest request,
							HttpServletResponse response) throws IOException, ServletException {
		
		log.debug("Command starts "+ this.getClass().getSimpleName());
		
		HttpSession session = request.getSession();

		String login = request.getParameter("login");
		log.trace("Request parameter: loging " + login);
		
		String password = request.getParameter("password");

		String errorMessage = null;		
		String forward = request.getContextPath() + Path.PAGE__ERROR_PAGE;

		/**
		 * Check blank login and password input
		 */
		if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
			errorMessage = "Login/password cannot be empty";
			session.setAttribute("errorHeader", "User login");
			session.setAttribute("errorMessage", errorMessage);
			UtilCommand.goToErrorPage(request, response);
			log.error("errorMessage " + errorMessage);
			return;
		}

		/**
		 * Read user by login
		 */
		User currentUser = null;
		try {
			currentUser = new UserDAO().findUserByLogin(login);
		} catch (AppException e) {
			errorMessage = "Login/password cannot be empty";
			session.setAttribute("errorHeader", "User login");
			session.setAttribute("errorMessage", errorMessage);
			UtilCommand.goToErrorPage(request, response);
			log.error("errorMessage " + errorMessage);
			return;
		}
		log.trace("Load from DB: user " + currentUser);

		/**
		 * Check user: exist or not
		 */
		if (currentUser == null) {
			errorMessage = "Can't find user with this login";
			session.setAttribute("errorHeader", "User login");
			session.setAttribute("errorMessage", errorMessage);
			UtilCommand.goToErrorPage(request, response);
			log.error("errorMessage: " + errorMessage);
			return;

		}
		/**
		 * Check user: status
		 */
		else if (!currentUser.isStatus()) {
			errorMessage = "User is locked, please, contact to administrator";
			session.setAttribute("errorHeader", "User login");
			session.setAttribute("errorMessage", errorMessage);
			UtilCommand.goToErrorPage(request, response);
			log.error("errorMessage: " + errorMessage);
			return;
		}
		/**
		 * Check user: password
		 */
		else if (!StringHash.getHashString(password).equals(currentUser.getPassword())) {
			errorMessage = "Bad password";
			session.setAttribute("errorHeader", "User login");
			session.setAttribute("errorMessage", errorMessage);
			UtilCommand.goToErrorPage(request, response);
			log.error("errorMessage: " + errorMessage);
			return;
		}
		/**
		 * Check user: all parameters are correct
		 */
		else {
			Role userRole = Role.getRole(currentUser);
			log.trace("userRole: " + userRole);

			if (userRole == Role.ADMIN)
				forward = Path.COMMAND__LIST_USERS;

			if (userRole == Role.USER)
				forward = Path.COMMAND__LIST_ACCOUNTS;

			currentUser.setPassword("");
			session.setAttribute("currentUser", currentUser);
			log.trace("Set the session attribute: user " + currentUser);

			session.setAttribute("userRole", userRole);
			log.trace("Set the session attribute: userRole " + userRole);
			session.setAttribute("currentPage", "users");
			log.trace("Set the session attribute: currentPage " + "users");

			String lang = "en";
			try {
				lang = currentUser.getLocal();
			}
			catch (Exception e)
			{
				lang = "en";
			}
			Config.set(session, "javax.servlet.jsp.jstl.fmt.locale", lang);
			session.setAttribute("currentLanguage", lang);
			session.setAttribute("langPack", SystemParameters.jsLanguagePack(lang));
		}
		
		log.debug("Command finished");
		response.sendRedirect(forward);
	}

	/**
	 * Execute GET function for Controller. This function doesn't have GET request, and redirect to error page
	 * @param request servlet request
	 * @param response servlet response
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		UtilCommand.bedGETRequest(request,response);
	}

}