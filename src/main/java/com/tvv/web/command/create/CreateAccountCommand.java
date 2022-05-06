package com.tvv.web.command.create;

import com.tvv.db.dao.AccountDAOImpl;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.AccountService;
import com.tvv.service.exception.AppException;
import com.tvv.web.command.Command;
import com.tvv.web.command.UtilCommand;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Command for create account. Account can be created only by USER role
 */
public class CreateAccountCommand extends Command {

	private static final Logger log = Logger.getLogger(CreateAccountCommand.class);

	private AccountService service;

	public CreateAccountCommand() {
		service = new AccountService(new AccountDAOImpl());
	}
	public void setUp(AccountService service){
		this.service = service;
	}
	/**
	 * Execute POST function for Controller. This function use JSON data from request, parse it, and send response to
	 * page. Create account for current user and redirect to users list
	 * @param request servlet request
	 * @param response servlet response
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void executePost(HttpServletRequest request,
							HttpServletResponse response) throws IOException, ServletException {
		log.debug("Start create account POST command " + this.getClass().getSimpleName());
		/**
		 * Check user role
		 */
		HttpSession session = request.getSession();
		Role userRole = (Role) session.getAttribute("userRole");
		User currentUser = (User) session.getAttribute("currentUser");

		if (userRole!=Role.ADMIN && userRole!=Role.USER)
		{
			response.sendRedirect(request.getContextPath()+ Path.COMMAND__START_PAGE);
			return;
		}

		/**
		 * Read parameter from request
		 */
		Map<String, String> accountData = readParemeters(request);
		log.debug("Read parameter: " + accountData);

		/**
		 * Check parameters and create account
		 */
		try {
			if (userRole==Role.USER) {
				service.createAccount(accountData, currentUser);
				response.sendRedirect(Path.COMMAND__LIST_ACCOUNTS);
				log.debug("Account is created for: " + currentUser);
			}
			else UtilCommand.bedGETRequest(request,response);
		} catch (AppException e) {
			log.trace(e.getMessage());
			request.setAttribute("errorHeader", "Error for account creation");
			request.setAttribute("errorMessage", e.getMessage());
			RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__ERROR_PAGE);
			disp.forward(request, response);
		}

		log.debug("Finish create account POST command "+this.getClass().getSimpleName());
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
	 * Function for readin parameter from request
	 * @param request request from servlet with parameters
	 * @return Map<String, String> key - parameter name, value - value of parameter
	 */
	private Map<String, String> readParemeters(HttpServletRequest request) {
		Map<String,String> result = new HashMap<>();

		result.put("name",request.getParameter("name"));
		result.put("iban",request.getParameter("iban"));
		result.put("currency",request.getParameter("currency"));
		/**
		 * reserved items
		 */
		result.put("bankCode","");
		result.put("ipn","");

		return result;
	}

}