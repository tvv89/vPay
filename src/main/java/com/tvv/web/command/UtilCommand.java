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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class UtilCommand {
    private static final Logger log = Logger.getLogger(UtilCommand.class);

    public static boolean noUserRedirect(HttpServletRequest request,
                                         HttpServletResponse response){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");
        return user==null;
    }

    public static User currentUser(HttpServletRequest request,
                                   HttpServletResponse response){
        User user = null;
        HttpSession session = request.getSession();
        user = (User) session.getAttribute("currentUser");
        return user;
    }

    public static void sendJSONData(HttpServletResponse response, com.google.gson.JsonObject innerObject) throws IOException {
        String sendData = new Gson().toJson(innerObject);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(sendData);
        out.flush();
    }

    public static Map<String, Object> parseRequestJSON(HttpServletRequest request, String ... keys) throws IOException, AppException {
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

    public static void bedGETRequest (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.trace("Start load command with method" + request.getMethod());
            RequestDispatcher disp = request.getRequestDispatcher(Path.PAGE__ACCESS_DENIED);
            disp.forward(request, response);
        log.trace("Forward to: " + Path.PAGE__ACCESS_DENIED);
    }

    public static JsonObject errorMessageJSON (String message) {
        JsonObject innerObject = new JsonObject();
        innerObject.add("status", new Gson().toJsonTree("ERROR"));
        innerObject.add("message", new Gson().toJsonTree(message));
        return  innerObject;
    }
}
