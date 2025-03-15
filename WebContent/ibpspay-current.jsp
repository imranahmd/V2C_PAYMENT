<%@ include file="/include.jsp"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.concurrent.TimeUnit"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>

<%-- <%@ page import="testpay.DBConnection"%> --%>
<%@page import="com.rew.pg.db.DBConnectionHandler"%>
<%-- <%@ page import="com.recon.ibps.DBConnection" %> --%>
<%@page import="java.sql.*"%>

<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%
Logger log = LoggerFactory.getLogger("JSPS.ibpspay-current.jsp");

log.info("HI i am in ibps :::");
	String pmerchantId = request.getParameter("merch");
	String pinvoiceId = request.getParameter("txn");
	String apiKey="";
	String amount="0";
	String merchantName="NA";
	String emailid="NA";
	String contactno="NA";
	String message="Payment for the invoice is already made";
%>

<%
	Connection conn,conn1 = null;
	PreparedStatement stmt,stmt1 = null;
	ResultSet rs1 =null;
	ResultSet rs2 =null;
	int bPayment=1;
	log.info("HI i am in ibps pay");
		try{
		conn = DBConnectionHandler.getConnection();
		String sql1 = "select * from tbl_mstibps where invoice_no=? and merchant_id=?"; 
		log.info("sql1 :::: "+sql1);
		stmt = conn.prepareStatement(sql1);
		stmt.setString(1, pinvoiceId);
		stmt.setString(2, pmerchantId);
		rs1 = stmt.executeQuery();
		log.info("Size :: ");
		if (rs1.next()) 
		{
			log.info("pinvoiceid " + pinvoiceId + " pmerchantId " + pmerchantId + " amount " + rs1.getString("amount") + " Status "+ rs1.getString("status"));
			// log.info("pinvoiceid " + rs1.getString("email_id") );
			// log.info("pinvoiceid " + rs1.getString("contact_number"));
			// log.info("pinvoiceid " + rs1.getString("email_id") + " pmerchantId " + rs1.getString("contact_number") + " amount " + rs1.getString("amount") + " Status "+ rs1.getString("valid_upto"));
			// log.info( " Status "+ rs1.getString("amount"));
			
			 emailid = rs1.getString("email_id");
			contactno = rs1.getString("contact_number");
			amount = rs1.getString("amount");
			String s=rs1.getString("valid_upto");
			Date dtOld = new SimpleDateFormat("dd-MM-yyyy").parse(s);
			String todaysDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			log.info(todaysDate);
			Date todaysDt = new SimpleDateFormat("dd-MM-yyyy").parse(todaysDate);
			Long l = DBConnectionHandler.getDateDiff(todaysDt,dtOld, TimeUnit.DAYS);
			log.info("l",l);
			long days = TimeUnit.MILLISECONDS.toDays(l); 

			
			if((!rs1.getString("status").equalsIgnoreCase("1")) && (days>=0)){
				conn1 = DBConnectionHandler.getConnection();
				String sql2 = "select * from tbl_mstmerchant where MerchantId=?";
				stmt1 = conn.prepareStatement(sql2);
				stmt1.setString(1, pmerchantId);
				rs2 = stmt1.executeQuery();
				if (rs2.next()) 
				{
					apiKey = rs2.getString("transaction_key");
					merchantName = rs2.getString("merchant_name");
				}
				else
				{
					bPayment=0;
				}
				
			}else
			{
				bPayment=2;
				if(days>=0)
					message="Payment for the invoce is already made.";
				else
					message="Validity of the invoce is expired.";	
			}
				
		}else
		{
			bPayment=3;
			message="Invalid Invoice Number.";
		}
			
		
		if(bPayment==1){	
%>
<html>
<body onload="document.myform.submit();">
	<form action="ibpsRedirector.jsp" method="post" name="myform">
		<input type="hidden" name="merchantId" value="<%=pmerchantId%>">
		<input type="hidden" name="apiKey" value="<%=apiKey%>">
		<%log.info("....api == TKAY " + rs2.getString("transaction_key"));%>
		<input type="hidden" name="merchant_name" value="<%=merchantName%>%>">
		<input type="hidden" name="amount" value="<%=amount%>">

		<%log.info(".....Amount " + amount); %>
		<input type="hidden" name="txnId" value="<%=pinvoiceId%>">

		<!-- //added two fields -->
		<input type="hidden" name="custMobile" value="<%=contactno%>">
		<input type="hidden" name="custMail" value="<%=emailid%>"> <input
			type="hidden" name="txnType" value="DIRECT">
	</form>
</body>
</html>
<% 	
	}
		
	else
	{
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
				<h2 class='co'>&nbsp;INVOICEPAY RESPONSE PAGE</h2>
			</td>
		</tr>
	</table>

	<br>
	<CENTER>
		<H1><%=message%></H1>
	</CENTER>

</BODY>

</HTML>
<%
		
	}
		}
		catch(Exception e){
		 	e.printStackTrace();
		}
	
%>
