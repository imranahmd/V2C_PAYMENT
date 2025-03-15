<%@ include file="/include.jsp"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.rew.payment.utility.CheckSumGenerator"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>Shopping Cart</title>

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/meyer-reset/2.0/reset.min.css">


<link rel="stylesheet" href="css/style.css">

<script type="text/javascript">
 	function redirectRequest() {
		document.myform.submit();
	}
 </script>
</head>

<body onload="redirectRequest();">
	
	 <form name="myform" action="http://domain/pay/payprocessor" method="post">
	
	<%
	Logger log = LoggerFactory.getLogger("JSPS.ibpsRedirector.jsp");

  	String dateTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
  	CheckSumGenerator gen = new CheckSumGenerator();
  	gen.setAmount(request.getParameter("amount"));
  	gen.setApiKey(request.getParameter("apiKey"));
  	gen.setDateTime(dateTime);
  	gen.setMerchantId(request.getParameter("merchantId"));
  	gen.setTxnId(request.getParameter("txnId"));
  	gen.setCustMobile(request.getParameter("custMobile"));
  	gen.setCustMail(request.getParameter("custMail"));
  	gen.setTxnType("DIRECT");
  	gen.setChannelId("0");
  
  	String checksum=gen.getChecksum("http://domain/pay/checksumgenerator");
  	
  	checksum = checksum.substring(checksum.indexOf("<checksum>")+10,checksum.indexOf("</checksum>"));
  	log.info(checksum);
  %>
<input type="hidden" name="merchantId" value="<%=request.getParameter("merchantId")%>">;
<input type="hidden" name="txnId" value="<%=request.getParameter("txnId")%>">;
<input type="hidden" name="dateTime" value="<%=dateTime%>">;
<input type="hidden" name="amount" value="<%=request.getParameter("amount")%>">;
<input type="hidden" name="custMobile" value="<%=request.getParameter("custMobile")%>">;
<input type="hidden" name="custMail" value="<%=request.getParameter("custMail")%>">;

<input type="hidden" name="returnURL" value="http://domian/pay/ibpsResponse.jsp">; 
<input type="hidden" name="checksum" value="<%=checksum%>">;
<input type="hidden" name="apiKey" value="<%=request.getParameter("apiKey")%>">;
<input type="hidden" name="udf1" value="NA">;
<input type="hidden" name="udf2" value="NA">;
<input type="hidden" name="udf3" value="NA">;
<input type="hidden" name="cardType" value="NA">; 
<input type="hidden" name="channelId" value="0">
<input type="hidden" name="txnType" value="DIRECT">;
<input type="hidden" name="instrumentId" value="NA">
<input type="hidden" name="bankId" value="NA">;
<input type="hidden" name="productId" value="DEFAULT">;	

</form>

</body>
</html>