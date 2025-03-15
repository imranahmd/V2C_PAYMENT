<%@ page import="com.rew.payment.utility.XSSFilter"%>
<%@ page import="java.util.*"%>
<%@ page import="com.rew.payment.utility.PGUtils"%>
<%@ page import="java.util.regex.Pattern"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%
	
	response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Expires", "0");
	response.setDateHeader("Expires", -1);
	Logger logger = LoggerFactory.getLogger("JSPS.include.jsp");

	Enumeration<String> params = request.getParameterNames();
    String paramName = null;
    String paramValue = null;
    String retValue = null;
    while (params.hasMoreElements()) {
    	paramName = (String) params.nextElement();
    	paramValue = request.getParameter(paramName);
    	System.out.println("paramName: " + paramName + " paramValue: " + paramValue);
    	XSSFilter xss = new XSSFilter();
    	retValue = xss.stripXSSJSP(paramValue);
    	if ((retValue == null) || (!retValue.equals(paramValue))) {
    		request.getRequestDispatcher("xssErrorPage.jsp").forward(request, response);
    	}
    }
	
%>


