
<%@page import="com.rew.pg.db.DataManager"%>
<%@page import="com.rew.payment.utility.PGUtils"%>
<%@page import="com.apps.lbps.mware.processor.CRUDService"%>
<%@page import="com.rew.pg.db.DBConnectionHandler"%>
<%@page import="com.rew.pg.dto.MerchantDTO"%>
<%@page contentType="text/html; charset=ISO-8859-1" language="java" %>
<%@page import="java.util.* , java.io.* , java.net.* , java.text.*"%>
<%@page import="java.sql.*"%>

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
    Logger log = LoggerFactory.getLogger("JSPS.IBPS RESPONSE.jsp");

	//This is GetTextBetweenTags function which return the value between two XML tags or two string
	JSONObject respJson = null;		
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
		
		String encResp = fields.get("respData").toString();
		String merchantId = fields.get("merchantId").toString();
		int istatus=0;
		try 
		{
			
			MerchantDTO merchantDTO =new DataManager().getMerchant(merchantId);

			String decryptedValue = PGUtils.getDecData(encResp, merchantDTO.getTransactionKey());
			respJson = new JSONObject(decryptedValue);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		String invoiceno[]=null;
		if(!respJson.getString("txn_id").equalsIgnoreCase(null) || !respJson.getString("txn_id").equalsIgnoreCase(""))
		{
			invoiceno=respJson.getString("txn_id").split("-");
		}
		log.info("invoiceno is ==================>"+invoiceno);
		
		if (respJson.getString("trans_status").equalsIgnoreCase("Ok"))
		{
			message = "Transaction Successful";
			istatus=1;
		}
		else if (respJson.getString("trans_status").equalsIgnoreCase("To"))
		{
			message = "Sorry!!Your Transaction is Timed Out";
			istatus=2;
		} 
		else
		{
			message = "Transaction Failed";
			istatus=2;
		}
%>




<%

    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Expires", "0");
    response.setDateHeader("Expires", -1);
    response.setDateHeader("Last-Modified", 0);
    Connection conn = null;
	    
    log.info("istatus =============>>>>"+istatus);
    
    CRUDService crud=new CRUDService();
    
    if(istatus==1)
    	crud.updatePaymentStatus(invoiceno[0].trim(), "1");
    else
    	crud.updatePaymentStatus(invoiceno[0].trim(), "2");
    	

%>

<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
    <HTML>
    
    <HEAD>
    	<TITLE>Merchant Response Page</TITLE>
    	<STYLE type='text/css'>
            H1       { font-family:Arial,sans-serif; font-size:24pt; color:#08185A; font-weight:100}
            H2.co    { font-family:Arial,sans-serif; font-size:24pt; color:#08185A; margin-top:0.1em; margin-bottom:0.1em; font-weight:100}
            H3.co    { font-family:Arial,sans-serif; font-size:16pt; color:#000000; margin-top:0.1em; margin-bottom:0.1em; font-weight:100}
            body     { font-family:Verdana,Arial,sans-serif; font-size:10pt; color:#08185A ;background-color:#FFFFFF }
            P        { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#FFFFFF }
            A:link   { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#08185A }
            A:visited{ font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#08185A }
            A:hover  { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#FF0000 }
            A:active { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#FF0000 }
            TD       { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#08185A }
            TD.red   { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#FF0066 }
            TD.green { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#00AA00 }
            TH       { font-family:Verdana,Arial,sans-serif; font-size:10pt; color:#08185A; font-weight:bold; background-color:#E1E1E1; padding-top:0.5em; padding-bottom:0.5em}
            input    { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#08185A; background-color:#E1E1E1; font-weight:bold }
            select   { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#08185A; background-color:#E1E1E1; font-weight:bold; width:463 }
            textarea { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#08185A; background-color:#E1E1E1; font-weight:normal; scrollbar-arrow-color:#08185A; scrollbar-base-color:#E1E1E1 }
        </STYLE>
    </HEAD>
    
    <BODY>
    <table width='100%' border='2' cellpadding='2' bgcolor='#C1C1C1'>
    	<tr>
    		<td bgcolor='#33EEFF' width='90%' align='center'>
    			<h2 class='co'>&nbsp;INVOICEPAY RESPONSE PAGE</h2>
    		</td>
    	</tr>
    </table>
	<BODY>
	 <table width='100%' border='2' cellpadding='2' bgcolor='#C1C1C1'>
		<!-- <tr>
			<td bgcolor='#33EEFF' width='90%' align='center'>
				<h2 class='co'>&nbsp;MERCHANT RESPONSE PAGE</h2>
			</td>
		</tr> -->
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
    <%} 
    %>
</HTML>


