package com.tvv.web.command.create;

import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.UserService;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.command.UtilCommand;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Command for create user. Use UserService for add and check user data
 */
public class CreateUserCommand extends Command {

	private static final Logger log = Logger.getLogger(CreateUserCommand.class);

	@Override
	public void executePost(HttpServletRequest request,
							HttpServletResponse response) throws IOException, ServletException {
		log.debug("Start registration POST command "+ this.getClass().getSimpleName());
		request.setCharacterEncoding("UTF-8");
		/**
		 * Check user role
		 */
		HttpSession session = request.getSession();
		Role userRole = (Role) session.getAttribute("userRole");
		User currentUser = (User) session.getAttribute("currentUser");
		if (userRole != Role.ADMIN && userRole != Role.USER) {
			response.sendRedirect(request.getContextPath() + Path.COMMAND__START_PAGE);
			return;
		}
		/**
		 * Create stream for read loaded photo
		 */
		Part filePart = request.getPart("photofile");
		String fileName = request.getParameter("login")+".jpeg";
		InputStream fileContent = filePart.getInputStream();
		ServletContext servletContext = request.getServletContext();
		String absolutePathToIndexJSP = servletContext.getRealPath("/images");
		/**
		 * Save photo file
		 */
		if (fileContent.available()>0) {
			File file = new File(absolutePathToIndexJSP, fileName);
			Files.copy(fileContent, file.toPath());
		}
		else
			fileName = "_blank.png";

		Map<String, String> userData = readParameters(request);
		userData.put("photofile", fileName);
		LocalDate date = LocalDate.now();
		try {
			date = LocalDate.parse(userData.get("dateofbirth"));
		} catch (Exception ex) {
			log.debug("Bad parse date from dateOfBirth");
		}
		/**
		 * Create user with parameter
		 */
		try {
			UserService.createUser(userData);
			response.sendRedirect(request.getContextPath()+ Path.COMMAND__START_PAGE);
		} catch (AppException e) {
			log.trace(e.getMessage());

			request.setAttribute("errorHeader", "Create user");
			request.setAttribute("errorMessage", e.getMessage());

			RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__ERROR_PAGE);
			disp.forward(request, response);
		}

		log.debug("Finish registration POST command "+this.getClass().getSimpleName());
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

	/**
	 * Function for reading user's parameter from request
	 * @param request request from servlet with parameters
	 * @return Map<String, String> key - parameter name, value - value of parameter
	 */
	private Map<String, String> readParameters(HttpServletRequest request) {
		Map<String,String> result = new HashMap<>();

		result.put("login",request.getParameter("login"));
		result.put("password",request.getParameter("password"));
		result.put("confirmpassword",request.getParameter("confirmpassword"));
		result.put("email",request.getParameter("email"));
		result.put("firstname",request.getParameter("firstname"));
		result.put("lastname",request.getParameter("lastname"));
		result.put("dateofbirth",request.getParameter("dateofbirth"));
		result.put("sex",request.getParameter("sex"));

		return result;
	}

}