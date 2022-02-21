package com.tvv.web.command;

import com.tvv.web.webutil.Path;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FormRegistration extends Command {

    private static final Logger log = Logger.getLogger(FormRegistration.class);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) throws IOException, ServletException {
        log.debug("Commands starts");

        request.setCharacterEncoding("UTF-8");
        log.trace("Forward to registration");

        log.debug("Commands finished");
        return Path.PAGE__REGISTRATION;
    }
}
