package com.rew.payment.utility;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityHeadersFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if required
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Set X-Frame-Options to DENY
        httpResponse.setHeader("X-Frame-Options", "DENY");

        // Set X-XSS-Protection to block mode
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");

        // Set Content-Security-Policy
        httpResponse.setHeader("Content-Security-Policy", "default-src 'self'; script-src 'self' https://pg.payfi.co.in/; style-src 'self' https://pg.payfi.co.in;");

        // Set Referrer-Policy to NO-REFERRER
        httpResponse.setHeader("Referrer-Policy", "no-referrer");

        // Set HTTP Strict-Transport-Security (HSTS)
        httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000 ; includeSubDomains");
        
        
        // Handle CORS headers
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "*");
        httpResponse.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Max-Age", "3600"); // Cache preflight for 1 hour
        
//        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
//            httpResponse.setStatus(HttpServletResponse.SC_OK);
//            return;
//        }

        // Continue the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup if required
    }
}
