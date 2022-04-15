package com.tvv.web.command;

import com.google.gson.JsonObject;
import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.utils.SystemParameters;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.Map;

/**
 * Command for change language
 */
public class LanguageCommand extends Command {

	private static final Logger log = Logger.getLogger(LanguageCommand.class);

	/**
	 * POST request function execute 'process'
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
	 * GET request function don't use
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

	}

	/**
	 * Function change language for user
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	void process (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		log.debug("Command starts "+ request.getMethod());

		HttpSession session = request.getSession();
		Map<String, Object> jsonParameters = null;
		try {
			jsonParameters = UtilCommand.parseRequestJSON(request);
		} catch (AppException e) {
			e.printStackTrace();
		}

		String lang = (String) jsonParameters.get("language");
		log.debug("language "+ lang);
		Config.set(session, "javax.servlet.jsp.jstl.fmt.locale", lang);
		session.setAttribute("currentLanguage", lang);
		session.setAttribute("langPack", SystemParameters.jsLanguagePack(lang));


		log.trace("Refresh page");
		log.debug("Command finished "+ request.getMethod());
	}
}