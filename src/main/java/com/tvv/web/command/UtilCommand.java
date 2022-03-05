package com.tvv.web.command;

import com.tvv.db.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UtilCommand {
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
}
