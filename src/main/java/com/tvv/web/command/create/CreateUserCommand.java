package com.tvv.web.command.create;

import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.webutil.ErrorMessageEN;
import com.tvv.web.webutil.ErrorString;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.tvv.service.UserService.createUser;


public class CreateUserCommand extends Command {

	private static final Logger log = Logger.getLogger(CreateUserCommand.class);

	@Override
	public void executePost(HttpServletRequest request,
							HttpServletResponse response) throws IOException, ServletException {
		log.debug("Start registration POST command");

		request.setCharacterEncoding("UTF-8");
		log.trace("Login for registration: " + request.getParameter("login"));


		Part filePart = request.getPart("photofile");
		String fileName = request.getParameter("login")+".jpeg";
		InputStream fileContent = filePart.getInputStream();
		ServletContext servletContext = request.getServletContext();
		String absolutePathToIndexJSP = servletContext.getRealPath("/images");

		if (fileContent.available()>0) {
			File file = new File(absolutePathToIndexJSP, fileName);
			Files.copy(fileContent, file.toPath());
		}
		else
			fileName = "_blank.png";
		Map<String, String> userData = readParemeters(request);
		userData.put("photofile", fileName);
		ErrorString error = new ErrorMessageEN();
		LocalDate date = LocalDate.now();
		try {
			date = LocalDate.parse(userData.get("dateofbirth"));
		} catch (Exception ex) {
			log.debug("Bad parse date from dateOfBirth");
		}

		try {
			createUser(userData);
			response.sendRedirect(request.getContextPath()+ Path.COMMAND__START_PAGE);
		} catch (AppException e) {
			log.trace(e.getMessage());
			String forward = request.getContextPath()+ Path.PAGE__ERROR_PAGE;

			request.setAttribute("errorHeader", error.errorHeaderUser());
			request.setAttribute("errorMessage", e.getMessage());
			//request.setAttribute("errorMessage", e.getMessage());
			//request.setAttribute("errorCode", 1);
			//response.sendRedirect(forward);

			RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__ERROR_PAGE);
			disp.forward(request, response);
		}

		log.debug("Finish registration POST command.");
	}

	@Override
	public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

	}

	private Map<String, String> readParemeters(HttpServletRequest request) {
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