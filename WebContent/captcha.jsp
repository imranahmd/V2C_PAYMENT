<%@page import="com.rew.payment.utility.PGUtils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
String captcha = PGUtils.RandomCaptcha(6);
session.setAttribute("captcha", captcha);
System.out.println("captcha >>>>>>>> " + captcha);
out.write(captcha);

%>