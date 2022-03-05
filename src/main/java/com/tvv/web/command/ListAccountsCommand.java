package com.tvv.web.command;

import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.CardDAO;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Card;
import com.tvv.db.entity.Role;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListAccountsCommand extends Command {

	private static final Logger log = Logger.getLogger(ListAccountsCommand.class);

	private static class CompareByName implements Comparator<Account>, Serializable {

		@Override
		public int compare(Account u1, Account u2) {
			return u1.getName().compareTo(u2.getName());
		}
	}
	
	private static Comparator<Account> compareByName = new CompareByName();
			
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		log.debug("Commands starts");
		if (UtilCommand.noUserRedirect(request,response)) return Path.PAGE__LOGIN;
		//Access to account by user role
		List<Account> accountList = new ArrayList<>();
		if (request.getSession().getAttribute("userRole")== Role.USER)
			accountList = AccountDAO.findAccountByUserId(UtilCommand.currentUser(request,response).getId());
		if (request.getSession().getAttribute("userRole")== Role.ADMIN)
			accountList = AccountDAO.findAllAccount();

		log.trace("Load from DB: accountList is " + accountList);
		
		Collections.sort(accountList, compareByName);

		request.setAttribute("accountList", accountList);
		request.setCharacterEncoding("UTF-8");
		log.trace("Set the request attribute: accountList " + accountList);
		HttpSession session = request.getSession();
		session.setAttribute("currentPage", "accounts");
		log.trace("Set the session attribute: currentPage " + "accounts");
		log.debug("Commands finished");
		return Path.PAGE__LIST_ACCOUNTS;
	}

}