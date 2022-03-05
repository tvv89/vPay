package com.tvv.web.command;

import com.tvv.db.dao.CardDAO;
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
import java.util.*;

public class ListCardsCommand extends Command {

	private static final Logger log = Logger.getLogger(ListCardsCommand.class);

	private static class CompareById implements Comparator<Card>, Serializable {

		@Override
		public int compare(Card c1, Card c2) {
			return c1.getId().compareTo(c2.getId());
		}
	}
	private static class CompareByName implements Comparator<Card>, Serializable {

		@Override
		public int compare(Card c1, Card c2) {
			return c1.getName().compareTo(c2.getName());
		}
	}

	private static Comparator<Card> compareById = new CompareById();
	private static Comparator<Card> compareByName = new CompareByName();

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		log.debug("Commands starts");
		//Return to login page, if current user is null
		if (UtilCommand.noUserRedirect(request, response)) {
			log.trace("No active user at this session");
			return Path.PAGE__LOGIN;
		}
		//Access to card list by role
		List<Card> cardList = new ArrayList<>();
		if (request.getSession().getAttribute("userRole")== Role.USER)
			cardList = CardDAO.findCardsByUser(UtilCommand.currentUser(request,response).getId());
		if (request.getSession().getAttribute("userRole")== Role.ADMIN)
			cardList = CardDAO.findAllCards();

		log.trace("Load from DB: cardList " + cardList);
		
		Collections.sort(cardList, compareById);
		//request.getParameter("p",)

		int countOnPage = 5;
		int pageView = 1;
		int pages = cardList.size()/countOnPage;
		//request.getParameter("p",)
		List<Card> pgList = cardList;// PaginationList.getListPage(cardList,pageView,countOnPage);

		request.setAttribute("cardList", pgList);
		request.setAttribute("pageView",pageView);
		request.setAttribute("pages",pages);
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		session.setAttribute("currentPage", "cards");
		log.trace("Set the session attribute: currentPage " + "cards");
		log.trace("Set the request attribute: cardList " + pgList);
		
		log.debug("Commands finished");
		return Path.PAGE__LIST_CARDS;
	}

}