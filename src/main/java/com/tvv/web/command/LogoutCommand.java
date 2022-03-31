package com.tvv.web.command;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

public class LogoutCommand extends Command {


	private static final Logger log = Logger.getLogger(LogoutCommand.class);

	@Override
	public void executePost(HttpServletRequest request,
							HttpServletResponse response) throws IOException, ServletException {
		process(request,response);
	}

	@Override
	public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		process(request,response);
	}

	void process (HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.debug("Command starts "+ request.getMethod());

		HttpSession session = request.getSession(false);
		if (session != null)
			session.invalidate();

		response.sendRedirect(request.getContextPath()+Path.COMMAND__START_PAGE);
		log.trace("Forward to start page: " + request.getContextPath()+Path.COMMAND__START_PAGE);
		log.debug("Command finished "+ request.getMethod());
	}
}