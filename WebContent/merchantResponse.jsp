<%@page import="com.rew.payment.utility.PSPCLEncryptor"%>
<%@page import="com.rew.pg.dto.TransactionMaster"%>
<%@page import="com.rew.pg.db.DataManager"%>
<%@page import="com.rew.pg.dto.MerchantDTO"%>
<%@page import="com.rew.payment.utility.PGUtils"%>
<%@page import="java.security.MessageDigest"%>
<%@page import="javax.crypto.Cipher"%>
<%@page import="javax.crypto.spec.SecretKeySpec"%>
<%@page import="java.security.Key"%>
<%@page import="org.json.JSONObject"%>
<%@page contentType="text/html; charset=ISO-8859-1" language="java"%>
<%@page import="java.util.* , java.io.* , java.net.* , java.text.*"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%!
    Logger log = LoggerFactory.getLogger("JSPS.merchantResPonse.jsp");

	//This is GetTextBetweenTags function which return the value between two XML tags or two string
			
	private String GetTextBetweenTags(String InputText, String Tag1, String Tag2)
	{
		String Result = "NA";
	
		int index1 = InputText.indexOf(Tag1);
		
		int index2 = InputText.indexOf(Tag2);
		
		log.info("merchantResponse.jsp ::: GetTextBetweenTags() :: Index of '"+Tag1+"' : "+index1+" And Index of '"+Tag2+"' : "+index2);
		
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
		log.info("merchantResponse.jsp ::: Response Field Name : "+fieldName+" And Response Field Value : "+fieldValue);
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
		
		log.info("merchantResponse.jsp ::: Error Code is : "+errorCode+" And Error Message is : "+errorMsg);
		
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
		String merchantId = fields.get("AuthID").toString();
		String pgid=fields.get("AggRefNo").toString();
		
		DataManager dm=new DataManager();
		TransactionMaster TM=dm.getTxnMaster(pgid);
		try 
		{
			//encResp = encResp.replaceAll("%2B", "+");
			//encResp = encResp.replaceAll("%2F", "/");  
			MerchantDTO merchantDTO = new DataManager().getMerchant(merchantId);
			
			//String decryptedValue = PGUtils.getDecData(encResp, merchantDTO.getTransactionKey());
			
			
			
			
			String confMerchantId = PGUtils.getPropertyValue("pspclId"); 
			String decryptedValue = null;
			log.info("TM.getUploadedBy()=====>"+TM.getUploadedBy());
			if(confMerchantId.equals(TM.getMerchantId()))     
			{
				log.info("PGUtils.java ::: Merchant is pspclId");
				decryptedValue = PSPCLEncryptor.decrypt(merchantDTO.getTransactionKey(), merchantDTO.getTransactionKey(), encResp);	
					
			} 
			else if(TM.getUploadedBy().equalsIgnoreCase("V2"))
			{
				log.info("PGUtils.java ::: Merchant is having 32 bit key "+TM.getMerchantId());
				decryptedValue = PSPCLEncryptor.decrypt(merchantDTO.getTransactionKey(), merchantDTO.getTransactionKey().substring(0, 16),
						encResp);
			}
			
			else
			{
				log.info("PGUtils.java ::: Merchant is not pspclId ");
				decryptedValue = PGUtils.getDecData(encResp, merchantDTO.getTransactionKey());
			}
			
			respJson = new JSONObject(decryptedValue);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (respJson.getString("payStatus").equalsIgnoreCase("Ok"))
		{
			message = "Transaction Successful";
		}
		else if (respJson.getString("payStatus").equalsIgnoreCase("To"))
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
			<td width='50%'><%=respJson.getString("CustRefNum")%></td>
		</tr>

		<tr>
			<td align='right' width='50%'><strong><i>AuthID : </i></strong></td>
			<td width='50%'><%=respJson.getString("AuthID")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>PG Transaction
						Reference Number : </i></strong></td>
			<td><%=respJson.getString("AggRefNo")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Transaction Response
						Date Time : </i></strong></td>
			<td><%=respJson.getString("payrespDate")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Transaction Status : </i></strong></td>
			<td><%=respJson.getString("payStatus")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Transaction Amount : </i></strong></td>
			<td><%=respJson.getString("PayAmount")%></td>
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
			<td><%=respJson.getString("EmailId")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Customer Phone No. : </i></strong></td>
			<td><%=respJson.getString("ContactNo")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Bank Reference Number
						: </i></strong></td>
			<td><%=respJson.getString("serviceRRN")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Transaction Date : </i></strong></td>
			<td><%=respJson.getString("PaymentDate")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Payment Mode : </i></strong></td>
			<td><%=respJson.getString("MOP")%></td>
		</tr>

	</TABLE>
</BODY>

</HTML>
<%
	}
%>
