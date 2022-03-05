package com.tvv.web.command;

import com.tvv.db.dao.AccountDAO;
import com.tvv.db.dao.PaymentDAO;
import com.tvv.db.entity.Account;
import com.tvv.db.entity.Payment;
import com.tvv.db.entity.Role;
import com.tvv.utils.PaginationList;
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

public class ListPaymentsCommand extends Command {

	private static final Logger log = Logger.getLogger(ListPaymentsCommand.class);

	private static class CompareById implements Comparator<Payment>, Serializable {

		@Override
		public int compare(Payment u1, Payment u2) {
			return u1.getId().compareTo(u2.getId());
		}
	}
	private static class CompareByDate implements Comparator<Payment>, Serializable {

		@Override
		public int compare(Payment u1, Payment u2) {
			return u1.getTimeOfLog().compareTo(u2.getTimeOfLog());
		}
	}

	private static Comparator<Payment> compareById = new CompareById();
	private static Comparator<Payment> compareByDate = new CompareByDate();

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		log.debug("Commands starts");
		if (UtilCommand.noUserRedirect(request,response)) return Path.PAGE__LOGIN;
		//Access to payment by user role
		List<Payment> paymentList = new ArrayList<>();
		if (request.getSession().getAttribute("userRole")== Role.USER)
			paymentList = PaymentDAO.findPaymentsByUser(UtilCommand.currentUser(request,response).getId());
		if (request.getSession().getAttribute("userRole")== Role.ADMIN)
			paymentList = PaymentDAO.findAllPayments();

		log.trace("Load from DB: paymentList " + paymentList);
		
		Collections.sort(paymentList, compareById);

		int countOnPage = 5;
		int pageView = 1;
		int pages = (int)Math.ceil((double)paymentList.size()/countOnPage);

		List<Payment> pgList = PaginationList.getListPage(paymentList,pageView,countOnPage);
		request.setAttribute("paymentList", pgList);
		request.setAttribute("pageView",pageView);
		request.setAttribute("pages",pages);
		request.setCharacterEncoding("UTF-8");
		log.trace("Set the request attribute: paymentList " + pgList);
		HttpSession session = request.getSession();
		session.setAttribute("currentPage", "payments");
		log.trace("Set the session attribute: currentPage " + "payments");
		log.debug("Commands finished");
		return Path.PAGE__LIST_PAYMANT;
	}

}