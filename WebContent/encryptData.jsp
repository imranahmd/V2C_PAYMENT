<%@page import="com.rew.payment.utility.PGUtils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%
Logger log = LoggerFactory.getLogger("JSPS.encryptData.jsp");


//System.out.println("inside  >>>>>>>> encryptdata" );
String cardno=request.getParameter("cardno");
String data=PGUtils.encryptcryptoJs(cardno);
//log.info("encryptData.jsp  >>>>>>>> encrypt card no " +data);
out.write(data);
%>
