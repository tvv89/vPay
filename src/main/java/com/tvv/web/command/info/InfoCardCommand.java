package com.tvv.web.command.info;

import com.google.gson.JsonObject;
import com.tvv.db.dao.AccountDAOImpl;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.AccountService;
import com.tvv.web.command.Command;
import com.tvv.web.command.UtilCommand;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * This command will be used in the future for show card information via send JSON response
 */
public class InfoCardCommand extends Command {

    private static final Logger log = Logger.getLogger(InfoCardCommand.class);

    /**
     * service for account command
     */
    private AccountService service;
    public InfoCardCommand(){
        service = new AccountService(new AccountDAOImpl());
    }
    /**
     * init service
     */
    public void setUp(AccountService service){
        this.service = service;
    }

    /**
     * Function for GET request. This command class don't use GET method
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UtilCommand.bedGETRequest(request, response);
    }

    /**
     * Execute POST function for Controller. This function use JSON data from request, parse it, and send response for
     * single page application. Function can show general information about Card info.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start POST method " + this.getClass().getSimpleName());
        JsonObject innerObject = new JsonObject();
        /**
         * Check user role
         */
        HttpSession session = request.getSession();
        Role userRole = (Role) session.getAttribute("userRole");
        User currentUser = (User) session.getAttribute("currentUser");
        if (userRole != Role.ADMIN && userRole != Role.USER) {
            response.sendRedirect(request.getContextPath() + Path.COMMAND__START_PAGE);
            return;
        }

        Integer accountId = null;
        try {
            Map<String, Object> jsonParameters =
                    UtilCommand.parseRequestJSON(request);
            accountId = (Integer) jsonParameters.get("accountId");
            String action = (String) jsonParameters.get("action");
            log.trace("Read account id for card info: " + accountId);
            /**
             * Send data for showing card info
             */
            if ("info".equals(action)) {
                innerObject = service.processCardInfo(accountId);
                log.trace("Show card info for " + accountId);
            }
            /**
             * Add card to account
             */
            if ("select".equals(action)) {
                Integer cardId = Integer.parseInt((String) jsonParameters.get("card"));
                innerObject = service.processCardSelect(accountId, cardId);
            }
        } catch (Exception e) {
            log.error("Can't read correct data from request, because " + e.getMessage());
            innerObject = UtilCommand.errorMessageJSON(e.getMessage());
        }
        /**
         * Send result response for single page
         */
        UtilCommand.sendJSONData(response, innerObject);
        log.trace("End POST method " + this.getClass().getSimpleName());
    }


}
