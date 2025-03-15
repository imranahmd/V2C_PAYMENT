<%@page import="javax.crypto.Cipher"%>
<%@page import="javax.crypto.spec.SecretKeySpec"%>
<%@page import="java.security.Key"%>
<%@page import="org.json.JSONObject"%>
<%@page contentType="text/html; charset=ISO-8859-1" language="java"%>
<%@page import="java.util.* , java.io.* , java.net.* , java.text.*"%>

<%!
	//This is GetTextBetweenTags function which return the value between two XML tags or two string
			
	private String GetTextBetweenTags(String InputText, String Tag1, String Tag2)
	{
		String Result = "NA";
	
		int index1 = InputText.indexOf(Tag1);
		
		int index2 = InputText.indexOf(Tag2);
		
		System.out.println("merchantResponseProcessor.jsp ::: GetTextBetweenTags() :: Index of '"+Tag1+"' : "+index1+" And Index of '"+Tag2+"' : "+index2);
		
		if (index1 != -1 && index2 != -1)
		{
			index1 = index1 + Tag1.length();
			Result = InputText.substring(index1, index2);
		}
		
		return Result;
	}
%>

<%
	//retrieve all the incoming parameters into a hash map
	Map<String,String> fields = new HashMap<String,String>();

	Enumeration<String> params = request.getParameterNames();
	while (params.hasMoreElements())
	{
		String fieldName = (String) params.nextElement();
		String fieldValue = request.getParameter(fieldName);
		System.out.println("merchantResponseProcessor.jsp ::: Response Field Name : "+fieldName+" And Response Field Value : "+fieldValue);
		if ((fieldValue != null) && (fieldValue.length() > 0)) 
		{
			fields.put(fieldName, fieldValue); 
		}
	}	
    
    if(fields.containsKey("txnErrorMsg") && fields.get("txnErrorMsg") != null)
    {
		String errorCode = null , errorMsg = null;
		
		String errorString = fields.get("txnErrorMsg").toString();
		
		errorCode = GetTextBetweenTags(errorString,"<error_code>","</error_code>");
		errorMsg = GetTextBetweenTags(errorString,"<error_msg>","</error_msg>");	
		
		System.out.println("merchantResponseProcessor.jsp ::: Error Code is : "+errorCode+" And Error Message is : "+errorMsg);
		
		String htmlRespone = "<html><table width='100%' border='2' cellpadding='2' bgcolor='#C1C1C1'>"+
					 "<tr><td bgcolor='#33EEFF' width='90%' align='center'><h2>&nbsp;MERCHANT RESPONSE PAGE</h2></td></tr>"+
					 "</table><br><br><br><br><br><br><br><br><br><br><br>";
	  	htmlRespone += "<center><h2><font color='red' size='3' face='Verdana,Arial,sans-serif'> Error "+ errorCode + " : " + errorMsg + "</font></h2></center>";          
	  	htmlRespone += "</html>";
		
		out.println(htmlRespone);
		out.flush();
		out.close();
    }
    
    else
    {
		String message = null;
		JSONObject respJson = null;
		String encResp = fields.get("respData").toString();
		String merchantId = fields.get("merchantId").toString();

		/* 
			Merchant has to enter the API Key shared by Epaisaa in the below variable 'merchantEncryptionKey' to Decrypt the Transaction Response.
			Algorithm used for encryption is AES.
			Merchant Encryption Key will be different for TEST and PRODUCTION environment.
		*/
		try 
		{
			//encResp = encResp.replaceAll("%2B", "+");
			//encResp = encResp.replaceAll("%2F", "/");  
			
			String merchantEncryptionKey = "jpuT6032jpuT6032"; //16 Charachter String
			
			String ALGO = "AES";
		   	byte[] keyByte = merchantEncryptionKey.getBytes();
		    Key key = new SecretKeySpec (keyByte, ALGO);
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.DECRYPT_MODE, key);			
			byte[] decryptedByteValue = Base64.getDecoder().decode(encResp.getBytes());
			byte[] decVal = c.doFinal(decryptedByteValue);
			String decryptedResponse = new String(decVal);
			
			System.out.println("merchantResponseProcessor.jsp ::: Decrypted Response :: "+decryptedResponse);
			
			respJson = new JSONObject(decryptedResponse);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (respJson.getString("trans_status").equalsIgnoreCase("Ok"))
		{
			message = "Transaction Successful";
		}
		else if (respJson.getString("trans_status").equalsIgnoreCase("To"))
		{
			message = "Sorry!!Your Transaction is Timed Out";
		} 
		else
		{
			message = "Transaction Failed";
		}
%>
<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<HTML>

<HEAD>
<TITLE>Merchant Response Page</TITLE>
<STYLE type='text/css'>
H1 {
	font-family: Arial, sans-serif;
	font-size: 24pt;
	color: #08185A;
	font-weight: 100
}

H2.co {
	font-family: Arial, sans-serif;
	font-size: 24pt;
	color: #08185A;
	margin-top: 0.1em;
	margin-bottom: 0.1em;
	font-weight: 100
}

H3.co {
	font-family: Arial, sans-serif;
	font-size: 16pt;
	color: #000000;
	margin-top: 0.1em;
	margin-bottom: 0.1em;
	font-weight: 100
}

body {
	font-family: Verdana, Arial, sans-serif;
	font-size: 10pt;
	color: #08185A;
	background-color: #FFFFFF
}

P {
	font-family: Verdana, Arial, sans-serif;
	font-size: 8pt;
	color: #FFFFFF
}

A:link {
	font-family: Verdana, Arial, sans-serif;
	font-size: 8pt;
	color: #08185A
}

A:visited {
	font-family: Verdana, Arial, sans-serif;
	font-size: 8pt;
	color: #08185A
}

A:hover {
	font-family: Verdana, Arial, sans-serif;
	font-size: 8pt;
	color: #FF0000
}

A:active {
	font-family: Verdana, Arial, sans-serif;
	font-size: 8pt;
	color: #FF0000
}

TD {
	font-family: Verdana, Arial, sans-serif;
	font-size: 8pt;
	color: #08185A
}

TD.red {
	font-family: Verdana, Arial, sans-serif;
	font-size: 8pt;
	color: #FF0066
}

TD.green {
	font-family: Verdana, Arial, sans-serif;
	font-size: 8pt;
	color: #00AA00
}

TH {
	font-family: Verdana, Arial, sans-serif;
	font-size: 10pt;
	color: #08185A;
	font-weight: bold;
	background-color: #E1E1E1;
	padding-top: 0.5em;
	padding-bottom: 0.5em
}

input {
	font-family: Verdana, Arial, sans-serif;
	font-size: 8pt;
	color: #08185A;
	background-color: #E1E1E1;
	font-weight: bold
}

select {
	font-family: Verdana, Arial, sans-serif;
	font-size: 8pt;
	color: #08185A;
	background-color: #E1E1E1;
	font-weight: bold;
	width: 463
}

textarea {
	font-family: Verdana, Arial, sans-serif;
	font-size: 8pt;
	color: #08185A;
	background-color: #E1E1E1;
	font-weight: normal;
	scrollbar-arrow-color: #08185A;
	scrollbar-base-color: #E1E1E1
}
</STYLE>
</HEAD>

<BODY>
	<table width='100%' border='2' cellpadding='2' bgcolor='#C1C1C1'>
		<tr>
			<td bgcolor='#33EEFF' width='90%' align='center'>
				<h2 class='co'>&nbsp;MERCHANT RESPONSE PAGE</h2>
			</td>
		</tr>
	</table>

	<br>
	<CENTER>
		<H1><%=message%></H1>
	</CENTER>

	<TABLE width="85%" align='center' cellpadding='5' border='0'>

		<tr bgcolor="#84FF33">
			<td colspan="2" height="25"><p>
					<strong>&nbsp;<font color="black">Payment Response
							Parameters - </font></strong>
				</p></td>
		</tr>

		<tr>
			<td>&nbsp;
			<td>
		<tr>
			<td align='right' width='50%'><strong><i>Merchant
						Txn Reference / Merchant Order Number : </i></strong></td>
			<td width='50%'><%=respJson.getString("txn_id")%></td>
		</tr>

		<tr>
			<td align='right' width='50%'><strong><i>Merchant
						Id : </i></strong></td>
			<td width='50%'><%=respJson.getString("merchant_id")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>PG Transaction
						Reference Number : </i></strong></td>
			<td><%=respJson.getString("pg_ref_id")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Transaction Response
						Date Time : </i></strong></td>
			<td><%=respJson.getString("resp_date_time")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Transaction Status : </i></strong></td>
			<td><%=respJson.getString("trans_status")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Transaction Amount : </i></strong></td>
			<td><%=respJson.getString("txn_amount")%></td>
		</tr>
		
		<tr>
			<td align='right'><strong><i>Transaction Response Code : </i></strong></td>
			<td><%=respJson.getString("resp_code")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Transaction Response
						Message : </i></strong></td>
			<td><%=respJson.getString("resp_message")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Customer Email Id : </i></strong></td>
			<td><%=respJson.getString("cust_email_id")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Customer Phone No. : </i></strong></td>
			<td><%=respJson.getString("cust_mobile_no")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Bank Reference Number
						: </i></strong></td>
			<td><%=respJson.getString("bank_ref_id")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Transaction Date : </i></strong></td>
			<td><%=respJson.getString("txn_date_time")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Payment Mode : </i></strong></td>
			<td><%=respJson.getString("payment_mode")%></td>
		</tr>

	</TABLE>
</BODY>

</HTML>
<%
	}
%>
