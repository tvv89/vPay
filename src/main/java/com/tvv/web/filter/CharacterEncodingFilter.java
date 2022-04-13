package com.tvv.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import java.io.IOException;

/**
 * Filter for character encoding
 */
@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    /**
     * Function doFilter
     * @param request servlet request
     * @param response servlet response
     * @param chain filter chain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        /**
         * Use UTF-8 encoding
         */
        request.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }
}
