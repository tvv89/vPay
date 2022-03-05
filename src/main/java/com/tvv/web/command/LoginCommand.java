package com.tvv.web.command;

import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.utils.StringHash;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginCommand extends Command {

	private static final Logger log = Logger.getLogger(LoginCommand.class);
	
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		log.debug("Command starts");
		
		HttpSession session = request.getSession();

		String login = request.getParameter("login");
		log.trace("Request parameter: loging " + login);
		
		String password = request.getParameter("password");

		String errorMessage = null;		
		String forward = Path.PAGE__ERROR_PAGE;
		
		if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
			errorMessage = "Login/password cannot be empty";
			request.setAttribute("errorMessage", errorMessage);
			log.error("errorMessage " + errorMessage);
			return forward;
		}
		
		User currentUser = new UserDAO().findUserByLogin(login);
		log.trace("Load from DB: user " + currentUser);
			
		if (currentUser == null || !StringHash.getHashString(password).equals(currentUser.getPassword())) {
			errorMessage = "Can't find user with login and password";
			request.setAttribute("errorMessage", errorMessage);
			log.error("errorMessage: " + errorMessage);
			return forward;
		} else {
			Role userRole = Role.getRole(currentUser);
			log.trace("userRole: " + userRole);
				
			if (userRole == Role.ADMIN)
				forward = Path.COMMAND__LIST_USERS;
		
			if (userRole == Role.USER)
				forward = Path.COMMAND__LIST_ACCOUNTS;
			
			session.setAttribute("currentUser", currentUser);
			log.trace("Set the session attribute: user " + currentUser);
				
			session.setAttribute("userRole", userRole);				
			log.trace("Set the session attribute: userRole " + userRole);
			session.setAttribute("currentPage", "users");
			log.trace("Set the session attribute: currentPage " + "users");

		}
		
		log.debug("Command finished");
		return forward;
	}

}