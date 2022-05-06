package com.tvv.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tvv.db.dao.UserDAO;
import com.tvv.db.dao.UserDAOImpl;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

/**
 * Logout user command for authorisation user
 */
public class LogoutCommand extends Command {

	private static final Logger log = Logger.getLogger(LogoutCommand.class);

	private UserDAO userDAO;
	private void init(){
		userDAO = new UserDAOImpl();
	}
	/**
	 * POST request function is same GET request function. Execute 'process'
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void executePost(HttpServletRequest request,
							HttpServletResponse response) throws IOException, ServletException {
		process(request,response);
	}

	/**
	 * GET request function is same POST request function. Execute 'process'
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		process(request,response);
	}

	/**
	 * Function invalidate session and redirect to start page
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	void process (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.trace("Command starts "+ request.getMethod());
		init();
		HttpSession session = request.getSession();
		Role userRole = (Role) session.getAttribute("userRole");
		User currentUser = (User) session.getAttribute("currentUser");
		String currentLanguage = (String) session.getAttribute("currentLanguage");
		if (userRole!=Role.ADMIN && userRole!=Role.USER)
		{
			response.sendRedirect(request.getContextPath()+ Path.COMMAND__START_PAGE);
			log.debug("User role is not correct");
			return;
		}
		try {
			userDAO.updateLocalUserById(currentUser.getId(), currentLanguage);
			log.debug("Save currentLanguage to user");
		}
		catch (AppException ex)
		{
			log.error("Can not save currentLanguage to user");
		}

		session = request.getSession(false);
		if (session != null)
			session.invalidate();

		response.sendRedirect(request.getContextPath()+Path.COMMAND__START_PAGE);
		log.trace("Forward to start page: " + request.getContextPath()+Path.COMMAND__START_PAGE);
		log.debug("Command finished "+ request.getMethod());
	}
}