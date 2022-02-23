package com.tvv.web.command;

import com.tvv.db.dao.CardDAO;
import com.tvv.db.entity.Card;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CreateUserCommand extends Command {

	private static final Logger log = Logger.getLogger(CreateUserCommand.class);

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		log.debug("Commands starts");

		request.setCharacterEncoding("UTF-8");
		String body = request.getReader().readLine();
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		log.trace("Set the request attribute: create user " + "");
		request.setAttribute("error", "error message");
		log.debug("Commands finished");
		return Path.PAGE__LIST_USERS;
	}

}