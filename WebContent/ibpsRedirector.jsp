<%@ include file="/include.jsp"%>
<%@page import="java.util.Base64"%>
<%@page import="javax.crypto.Cipher"%>
<%@page import="javax.crypto.spec.SecretKeySpec"%>
<%@page import="java.security.Key"%>
<%@page import="java.util.Enumeration"%>
<%@page import="com.rew.payment.utility.PGUtils"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.rew.payment.utility.CheckSumGenerator"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>

<%@page import="javax.crypto.spec.IvParameterSpec"%>
<%@page import="javax.crypto.spec.SecretKeySpec"%>
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
	
	<%--  <form name="myform" action="https://hdfcprodsigning.in/payfiVAS/payprocessor" method="post">
	
	<%
	Logger log = Logger.getLogger("JSPS.ibpsRedirector.jsp"); 
  	String dateTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
  	//CheckSumGenerator gen = new CheckSumGenerator();
  	/* gen.setAmount(request.getParameter("amount"));
  	gen.setApiKey(request.getParameter("apiKey"));
  	gen.setDateTime(dateTime);
  	gen.setMerchantId(request.getParameter("merchantId"));
  	gen.setTxnId(request.getParameter("txnId"));
  	gen.setCustMobile(request.getParameter("custMobile"));
  	gen.setCustMail(request.getParameter("custMail"));
  	gen.setTxnType("DIRECT");
  	gen.setChannelId("0");
  
  	String checksum=gen.getChecksum("http://139.59.1.254:8080/payone/checksumgenerator");
  	
  	checksum = checksum.substring(checksum.indexOf("<checksum>")+10,checksum.indexOf("</checksum>"));
  	log.info(checksum); */
  %>
<input type="hidden" name="merchantId" value="<%=request.getParameter("merchantId")%>">;
<input type="hidden" name="txnId" value="<%=request.getParameter("txnId")%>">;
<input type="hidden" name="dateTime" value="<%=dateTime%>">;
<input type="hidden" name="amount" value="<%=request.getParameter("amount")%>">;
<input type="hidden" name="custMobile" value="<%=request.getParameter("custMobile")%>">;
<input type="hidden" name="custMail" value="<%=request.getParameter("custMail")%>">;
<!-- <input type="hidden" name="returnURL" value="https://pay.Epaisaapg.in/payone/ibpsResponse.jsp">; -->
<input type="hidden" name="returnURL" value="https://hdfcprodsigning.in/payfiVAS/ibpsResponse.jsp">; 

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

</body> --%>
<%
//String URL = PGUtils.getPropertyValue("paymentURL");

String URL = null;
String apiVal = request.getParameter("apiKey");

	JSONObject json = new JSONObject();	
	Enumeration<String> e = request.getParameterNames();
	
	//retrieve all the incoming parameters into a JSON Object
    while (e.hasMoreElements())
	{
		String fieldName = (String) e.nextElement();
		String fieldValue = request.getParameter(fieldName);
		System.out.println("ibpsRedirector.jsp ::: Request Field Name : "+fieldName+" And Request Field Value : "+fieldValue);
		if ((fieldValue != null) && (fieldValue.length() > 0)) 
		{
			json.put(fieldName, fieldValue); 
		}
	}		
		json.put("dateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		json.put("returnURL", PGUtils.getPropertyValue("ibpsReturnURL"));
		json.put("udf1", "NA");
		json.put("udf2", "NA");
		json.put("udf3", "NA");
		json.put("udf4", "NA");
		json.put("udf5", "NA");
		json.put("channelId", "0");
		json.put("instrumentId", "NA");
		json.put("bankId", "NA");
		json.put("productId", "DEFAULT");
		json.put("isMultiSettlement", "0");
		json.put("cardDetails", "NA");
		json.put("cardType", "NA");
		
	
	/* 
		Merchant has to enter the Merchant Id shared by Epaisaa in the below variable 'merchantId' and pass in the Request along with encrypted Request Data.
		Merchant has to enter the API Key shared by (Pay) in the below variable 'merchantEncryptionKey' to Encrypt the Transaction Request.
		Algorithm used for encryption is AES.
		Merchant Encryption Key will be different for TEST and PRODUCTION environment.
	*/
	String merchantId = request.getParameter("merchantId");
	String merchantEncryptionKey = request.getParameter("apiKey"); //16 Charachter String
	String encryptedRequest = null;
	if(merchantEncryptionKey.length()<16){
		URL = PGUtils.getPropertyValue("paymentURL");
		merchantEncryptionKey=merchantEncryptionKey+merchantEncryptionKey;
		System.out.println("merchantEncryptionKey:::::::::::"+merchantEncryptionKey);
		String ALGO = "AES";
	   	byte[] keyByte = merchantEncryptionKey.getBytes();
	    Key key = new SecretKeySpec(keyByte, ALGO);
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(json.toString().getBytes());
		byte[] encryptedByteValue = Base64.getEncoder().encode(encVal);
		 encryptedRequest = new String(encryptedByteValue);
	}
		
	else{
		
		URL = PGUtils.getPropertyValue("paymentURLV2");
		String merchKey =	merchantEncryptionKey.substring(0, 16);
		 IvParameterSpec iv = new IvParameterSpec(merchKey.getBytes("UTF-8"));
	      SecretKeySpec skeySpec = new SecretKeySpec(merchantEncryptionKey.getBytes("UTF-8"), "AES");
	      
	      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	      cipher.init(1, skeySpec, iv);
	      
	      byte[] encVal = cipher.doFinal(json.toString().getBytes());
	      System.out.println("encrypted string: " + 
	        java.util.Base64.getEncoder().encodeToString(encVal));
	      
	      byte[] encryptedByteValue = Base64.getEncoder().encode(encVal);
			 encryptedRequest = new String(encryptedByteValue);
	}
	
	
	System.out.println("merchantRequestProcessor.jsp ::: Encrypted Request :: "+encryptedRequest);
	
	
	response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Expires", "0");
	response.setDateHeader("Expires", -1);
	response.setDateHeader("Last-Modified", 0);
%>


<!-- 
	Submit the Request to the Epaisaa's Transaction Posting URL.
	Request Parameters to be sent are 'merchantId' and 'reqData'.
-->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<title>Test Merchant Redirector</title>
	<script type="text/javascript">
		//history.forward();
		function redirectRequest() 
		{
			document.RedirectForm.submit();
		}
	</script>
</head>

<body onload="redirectRequest();">
	<center>
		<h1>Redirect Page</h1>
	</center>
	<form name="RedirectForm" action="<%=URL%>" method="post">
		<table width="80%" align="center" border="0" cellpadding='0' cellspacing='0'>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2" align="center" bgcolor='#FCF3CF'>
					<h5>
						<strong>Please do not click Back/Refresh button while Redirecting.</strong>
					</h5>
				</td>
			</tr>
			<tr>
				<td><input type="hidden" name="reqData" value="<%=encryptedRequest%>"></td>
			</tr>
			<tr>
				<td><input type="hidden" name="merchantId" value="<%=merchantId%>"></td>
			</tr>
			
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<!-- <tr>
                	<td colspan="2" align="center"><input type="submit" name="submit" value="Submit Request" /></td>
                </tr> -->
		</table>
	</form>
</body>



</html>