package com.rew.payment.utility;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClickjackingPreventionFilter implements Filter
{
  private String mode = "DENY";
	//  private String mode = "ALLOW";
  public ClickjackingPreventionFilter() {}
  
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException { HttpServletResponse res = (HttpServletResponse)response;
	HttpServletRequest httprequest = (HttpServletRequest) request;
//	if ("OPTIONS".equalsIgnoreCase(httprequest.getMethod())) {
//	    chain.doFilter(request, response); // Allow preflight requests
//	    return;
//	}
    res.addHeader("X-FRAME-OPTIONS", mode);
    res.addHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    res.addHeader("Pragma", "no-cache");
    res.addHeader("Expires", "0");
    
    chain.doFilter(request, response);
  }
  

  public void destroy() {}
  
  public void init(FilterConfig filterConfig)
    throws ServletException
  {
    String configMode = filterConfig.getInitParameter("mode");
    if (configMode != null) {
      mode = configMode;
    }
  }
}
