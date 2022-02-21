package com.tvv.web.command;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tvv.db.dao.UserDAO;
import com.tvv.db.entity.User;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

public class ListUsersCommand extends Command {

	private static final long serialVersionUID = 1863978254689586513L;
	
	private static final Logger log = Logger.getLogger(ListUsersCommand.class);

	private static class CompareByLogin implements Comparator<User>, Serializable {
		private static final long serialVersionUID = -1573481565177573283L;

		@Override
		public int compare(User u1, User u2) {
			return u1.getLogin().compareTo(u2.getLogin());
		}
	}
	
	private static Comparator<User> compareByLogin = new CompareByLogin();
			
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		log.debug("Commands starts");
				
		List<User> userList = new UserDAO().findAllUsers();
		log.trace("Found in DB: userList --> " + userList);
		
		Collections.sort(userList, compareByLogin);
		
		// put user order beans list to request
		request.setAttribute("usersList", userList);
		request.setCharacterEncoding("UTF-8");
		log.trace("Set the request attribute: userList --> " + userList);
		
		log.debug("Commands finished");
		return Path.PAGE__LIST_USERS;
	}

}