package com.tvv.web.command;

import com.tvv.db.dao.AccountDAO;
import com.tvv.db.entity.Account;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListAccountsCommand extends Command {

	private static final Logger log = Logger.getLogger(ListAccountsCommand.class);

	private static class CompareByName implements Comparator<Account>, Serializable {
		private static final long serialVersionUID = -1573481565177573283L;

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
				
		List<Account> accountList = AccountDAO.findAllAccount();
		log.trace("Load from DB: accountList is " + accountList);
		
		Collections.sort(accountList, compareByName);
		
		// put user order beans list to request
		request.setAttribute("accountList", accountList);
		request.setCharacterEncoding("UTF-8");
		log.trace("Set the request attribute: accountList " + accountList);
		
		log.debug("Commands finished");
		return Path.PAGE__LIST_ACCOUNTS;
	}

}