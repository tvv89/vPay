package com.tvv.web.command;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tvv.db.entity.Role;
import com.tvv.db.entity.User;
import com.tvv.service.exception.AppException;
import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Class extended functions for Commands
 */
public class UtilCommand {
    private static final Logger log = Logger.getLogger(UtilCommand.class);

    /**
     * Return current user from request
     * @param request
     * @param response
     * @return
     */
    public static User currentUser(HttpServletRequest request,
                                   HttpServletResponse response){
        User user = null;
        HttpSession session = request.getSession();
        user = (User) session.getAttribute("currentUser");
        return user;
    }

    /**
     * Function send JSOM data in response
     * @param response servlet response
     * @param innerObject JSON object (JSON data)
     * @throws IOException
     */
    public static void sendJSONData(HttpServletResponse response, com.google.gson.JsonObject innerObject) throws IOException {
        String sendData = new Gson().toJson(innerObject);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(sendData);
        out.flush();
    }

    /**
     * Send bytearray stream  (PDF file) to client
     * @param response servlet response
     * @param output PDF byte stream
     */
    public static void sendPDFData(HttpServletResponse response, ByteArrayOutputStream output) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "filename=payment.pdf");
        response.setContentLength(output.size());

        try (OutputStream os = response.getOutputStream()) {
            os.write(output.toByteArray() , 0, output.toByteArray().length);
        } catch (Exception excp) {
            log.error("Can not get response output stream");
        } finally {

        }
    }

    /**
     * Function for parsing JSON request
     * @param request servlet request
     * @return Map: key - string, value - object from JSON data
     * @throws IOException
     * @throws AppException
     */
    public static Map<String, Object> parseRequestJSON(HttpServletRequest request) throws IOException, AppException {
        Map<String, Object> result = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        String line = null;

        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) sb.append(line);
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(sb.toString());
            log.trace("Parse JSON object in POST method: " + jsonObject.toString());
        }
        catch (Exception ex){
            log.error("Bad parse JSON object in POST method because: " + ex.getMessage());
            throw new AppException("Bad parse JSON object in POST method",ex);
        }

        result = jsonObject.toMap();

        return result;
    }

    /**
     * Show Access denied page for user
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    public static void bedGETRequest (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start load command with method" + request.getMethod());
            RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__ACCESS_DENIED);
            disp.forward(request, response);
        log.trace("Forward to: " + Path.PAGE__ACCESS_DENIED);
    }

    /**
     * Redirect to Error Page (with parameters)
     * @param request servlet request
     * @param response servlet response
     * @throws IOException
     * @throws ServletException
     */
    public static void goToErrorPage (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start error page with method " + request.getMethod());
        response.sendRedirect(request.getContextPath() + Path.PAGE__ERROR_PAGE);
        log.trace("Forward to: " + Path.PAGE__ERROR_PAGE);
    }

    /**
     * Generate JSON object with Error message (for POST response)
     * @param message String error
     * @return JSON object (usually use for 'sendJSONData')
     */
    public static JsonObject errorMessageJSON (String message) {
        JsonObject innerObject = new JsonObject();
        innerObject.add("status", new Gson().toJsonTree("ERROR"));
        innerObject.add("message", new Gson().toJsonTree(message));
        return  innerObject;
    }

}
