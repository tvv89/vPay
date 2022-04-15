package com.tvv.web.command.create;

import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.CardService;
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
 * Command for create card. Card can be created only by USER role
 */
public class CreateCardCommand extends Command {

	private static final Logger log = Logger.getLogger(CreateCardCommand.class);

	private CardService service;
	public CreateCardCommand() {
		service = new CardService();
	}
	public void setUp(CardService service) {
		this.service = service;
	}
	/**
	 * Execute POST function for Controller. This function use JSON data from request, parse it, and send response to
	 * page. Create card for current user and redirect to card list
	 * @param request servlet request
	 * @param response servlet response
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void executePost(HttpServletRequest request,
							HttpServletResponse response) throws IOException, ServletException {
		log.debug("Start create card POST command "+this.getClass().getSimpleName());
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
		Map<String, String> cardData = readParemeters(request);
		cardData.put("ownerUser",currentUser.getId().toString());
		log.debug("Read data: " + cardData);

		/**
		 * Check parameters and create card
		 */
		try {
			if (userRole==Role.USER) {
				service.createCard(cardData);
				response.sendRedirect(Path.COMMAND__LIST_CARDS);
				log.debug("Card was created");
			}
			else UtilCommand.bedGETRequest(request,response);
		} catch (AppException e) {
			log.trace(e.getMessage());

			request.setAttribute("errorHeader", "Error create card");
			request.setAttribute("errorMessage", e.getMessage());

			RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__ERROR_PAGE);
			disp.forward(request, response);
		}

		log.debug("Finish create account POST command.");
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
	 * Function for reading card's parameter from request
	 * @param request request from servlet with parameters
	 * @return Map<String, String> key - parameter name, value - value of parameter
	 */
	private Map<String, String> readParemeters(HttpServletRequest request) {
		Map<String,String> result = new HashMap<>();

		result.put("name",request.getParameter("name"));
		result.put("cardnumber",request.getParameter("cardnumber"));
		result.put("expMM",request.getParameter("expMM"));
		result.put("expYY",request.getParameter("expYY"));

		return result;
	}

}