package com.rew.payment.utility;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSession;

public class SecureSessionListener implements HttpSessionListener, ServletContextListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        session.getServletContext().getSessionCookieConfig().setSecure(true);
        session.getServletContext().getSessionCookieConfig().setHttpOnly(true);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        // Session cleanup if needed
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // Context initialization logic if needed
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // Context destruction logic if needed
    }
}
