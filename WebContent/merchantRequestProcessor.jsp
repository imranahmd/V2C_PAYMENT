<%@ include file="/include.jsp"%>
<%@page import="com.rew.payment.utility.PGUtils"%>
<%@page import="javax.crypto.Cipher"%>
<%@page import="javax.crypto.spec.SecretKeySpec"%>
<%@page import="java.security.Key"%>
<%@page import="java.security.MessageDigest"%>
<%@page contentType="text/html; charset=ISO-8859-1" language="java"%>
<%@page import="java.util.* , java.io.* , java.net.*"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.rew.upi.hdfc.*"%>

<%  
	//add the Transaction Posting URL	
	
	// String URL = "http://domain:8080/payment/payprocessor"; 

	String URL = PGUtils.getPropertyValue("paymentURL");
	
	JSONObject json = new JSONObject();	
	Enumeration<String> e = request.getParameterNames();
	
	//retrieve all the incoming parameters into a JSON Object
    while (e.hasMoreElements())
	{
		String fieldName = (String) e.nextElement();
		String fieldValue = request.getParameter(fieldName);
		System.out.println("merchantRequestProcessor.jsp ::: Request Field Name : "+fieldName+" And Request Field Value : "+fieldValue);
		if ((fieldValue != null) && (fieldValue.length() > 0)) 
		{
			json.put(fieldName, fieldValue); 
		}
	}
	
	/* 
		Merchant has to enter the Merchant Id shared by Epaisaa in the below variable 'merchantId' and pass in the Request along with encrypted Request Data.
		Merchant has to enter the API Key shared by Epaisaa in the below variable 'merchantEncryptionKey' to Encrypt the Transaction Request.
		Algorithm used for encryption is AES.
		Merchant Encryption Key will be different for TEST and PRODUCTION environment.
	*/
	String merchantId = "M0002";
	String merchantEncryptionKey = "jpuT6032jpuT6032"; //16 Charachter String
	
	String ALGO = "AES";
   	byte[] keyByte = merchantEncryptionKey.getBytes();
    Key key = new SecretKeySpec (keyByte, ALGO);
	Cipher c = Cipher.getInstance(ALGO);
	c.init(Cipher.ENCRYPT_MODE, key);
	byte[] encVal = c.doFinal(json.toString().getBytes());
	byte[] encryptedByteValue = Base64.getEncoder().encode(encVal);
	String encryptedRequest = new String(encryptedByteValue);
	
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