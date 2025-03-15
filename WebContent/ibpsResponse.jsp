<%@page import="com.rew.payment.utility.PSPCLEncryptor"%>
<%@page import="com.rew.pg.dto.TransactionMaster"%>
<%@page contentType="text/html; charset=ISO-8859-1" language="java"%>
<%@page import="java.util.*,java.io.*,java.net.*,java.text.*"%>
<%@page import="java.sql.*"%>
<%@ page import="com.rew.pg.db.DBConnectionHandler"%>
<%@page import="com.rew.pg.db.DataManager"%>
<%@page import="com.rew.pg.dto.MerchantDTO"%>
<%@page import="com.rew.payment.utility.PGUtils"%>
<%@page import="java.security.MessageDigest"%>
<%@page import="javax.crypto.Cipher"%>
<%@page import="javax.crypto.spec.SecretKeySpec"%>
<%@page import="java.security.Key"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.format.DateTimeFormatterBuilder"%>
<%@page import="java.time.temporal.ChronoField"%>
<%@page import="java.util.Date"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="com.apps.lbps.mware.processor.CRUDService"%>

<%!
	Logger log = LoggerFactory.getLogger("JSPS.IBPS RESPONSE.jsp"); 
	//This is GetTextBetweenTags function which return the value between two XML tags or two string
	JSONObject respJson = null;		
	private String GetTextBetweenTags(String InputText, String Tag1, String Tag2)
	{
		String Result = "NA";
	
		int index1 = InputText.indexOf(Tag1);
		
		int index2 = InputText.indexOf(Tag2);
		
		log.info("ibpsResponse.jsp ::: GetTextBetweenTags() :: Index of '"+Tag1+"' : "+index1+" And Index of '"+Tag2+"' : "+index2);
		
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
	String txnId="";
	String paidOn="";
	String attemptedOn="";
	String paymentMethod="";
	String bankName="";
	String transStatus="";
	String txnAmount="";
	String merchName = "";
	String transNote = "";
	String invoiceNo="";
	Enumeration<String> params = request.getParameterNames();
	while (params.hasMoreElements())
	{
		String fieldName = (String) params.nextElement();
		String fieldValue = request.getParameter(fieldName);
		log.info("merchantResponse.jsp ::: Response Field Name : "+fieldName+" And Response Field Value ::::: "+fieldValue);
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
		String pgid=fields.get("pgid").toString();
		
		DataManager dm=new DataManager();
		TransactionMaster TM=dm.getTxnMaster(pgid);
		int istatus=0;
		try 
		{
			//encResp = encResp.replaceAll("%2B", "+");
			//encResp = encResp.replaceAll("%2F", "/");  
			MerchantDTO merchantDTO = new DataManager().getMerchant(merchantId);
			//log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>      "+merchantDTO.getTransactionKey());
			
			
			String confMerchantId = PGUtils.getPropertyValue("pspclId"); 
			String decryptedValue=null;
			log.info("TM.getUploadedBy()=====>"+TM.getUploadedBy());
			
			if(confMerchantId.equals(merchantId))
			{
				log.info("PSPCL decrypt");
				 decryptedValue=PSPCLEncryptor.decrypt(merchantDTO.getTransactionKey(), merchantDTO.getTransactionKey(),encResp);
			}
			else if(TM.getUploadedBy().equalsIgnoreCase("V2"))
			{
				log.info("PGUtils.java ::: Merchant is having 32 bit key "+TM.getMerchantId());
				decryptedValue = PSPCLEncryptor.decrypt(merchantDTO.getTransactionKey(), merchantDTO.getTransactionKey().substring(0, 16),
						encResp);
				log.info("decryptedValue:::::: decrypt"+decryptedValue);
			}
			
			else{
				log.info("general decrypt");
			 decryptedValue = PGUtils.getDecData(encResp, merchantDTO.getTransactionKey());
			 log.info("general decryptedValue:::::"+decryptedValue);
			}
		
			
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
		log.info("invoiceno is ==================>"+invoiceno[0]);
		
		if (respJson.getString("trans_status").equalsIgnoreCase("Ok"))
		{
			log.info("Paid Successfully!  ==================>");
			message = "Paid Successfully!";
			istatus=1;
		}
		else if (respJson.getString("trans_status").equalsIgnoreCase("To"))
		{
			//message = "Sorry!! Your Transaction is Timed Out";
			log.info("Transaction Pending ==================>");
			message = "Transaction Pending!";
			istatus=2;
		} 
		else
		{
			log.info("Transaction Failed! ==================>");
			message = "Transaction Failed!";
			istatus=3;
		}
%>




<%

    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Expires", "0");
    response.setDateHeader("Expires", -1);
    response.setDateHeader("Last-Modified", 0);
    Connection conn = null;
    conn = DBConnectionHandler.getConnection();
	PreparedStatement transStmt=null;
	ResultSet rs =null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	String transSql = "select *,Case when T1.bank_id = 'FINPG' then 'FINPG' when T1.bank_id = 'NA' or 'RES' or null then 'NA' else T2.BankName end as BankName "+
		       "from  tbl_transactionmaster T1 inner join tbl_mstpgbank T2 on T1.bank_id=T2.bankId inner join tbl_mstmerchant T3"+
		       " on T3.MerchantId = T1.merchant_id where T1.txn_Id = ? and T1.merchant_id=?"; 
	transStmt = conn.prepareStatement(transSql);
	transStmt.setString(1, respJson.getString("txn_id"));
	transStmt.setString(2, TM.getMerchantId());
	invoiceNo = respJson.getString("txn_id").split("-")[0];
	rs = transStmt.executeQuery();
	if (rs.next()) 
	{
		log.info("rs.next() ==================>");
		txnId = rs.getString("Id");
		String respDateString = rs.getString("resp_date_time");
		Date respDate =new SimpleDateFormat("yyyy-MM-dd").parse(respDateString);
		String dateTimeString = rs.getString("date_time");
		Date dateTime = new SimpleDateFormat("yyyy-MM-dd").parse(dateTimeString);
		DateTimeFormatter dayOfMonthFormatter = PGUtils.getOrdinalDateFormatter();
		paidOn = LocalDate.parse(new SimpleDateFormat("dd-MM-yyyy").format(respDate), formatter).format(dayOfMonthFormatter);
		attemptedOn = LocalDate.parse(new SimpleDateFormat("dd-MM-yyyy").format(dateTime), formatter).format(dayOfMonthFormatter);
		paymentMethod= rs.getString("instrument_id");
		bankName= rs.getString("BankName");
		transStatus= rs.getString("trans_status");
		txnAmount= rs.getString("txn_amount");
		merchName = rs.getString("business_name").toUpperCase();
		if(transStatus.equalsIgnoreCase("Ok")){
			message="Paid Successfully!";
		}else if(transStatus.equalsIgnoreCase("To")){
			message="Transaction Pending!";
			transNote ="In case your money has been debited, please check back after 2 hours.";
		}else{
			message="Transaction Failed!";
			transNote ="In case your money has been debited, it will be refunded in 5-7 working days.";
		}
	}
   CRUDService crud=new CRUDService();
    if(istatus!=0){
    	log.info("istatus!=0 ==================>");
    	crud.updatePaymentStatus(invoiceno[0].trim(), String.valueOf(istatus));
    }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "w3.org/TR/REC-html40/loose.dtd">
<HTML>

<HEAD>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<TITLE>Merchant Response Page</TITLE>
<STYLE type='text/css'>
body {
	padding: 0;
	margin: 0;
}

.header {
	background-color: #1b54dd;
	display: flex;
	justify-content: center;
	text-align: center;
	padding: 30px;
}

.header label {
	text-transform: uppercase;
	color: white;
	font-size: 30px;
	font-weight: 400 !important;
}

.content {
	display: flex;
	flex-direction: column;
}

.bordered-div {
	display: block;
	margin: 0 auto;
	/*outline: #1b54dd solid 3px;
				outline-offset: 25px;*/
	border: #1b54dd solid 3px;
	padding: 25px;
	width: calc(50% - 100px);
}

.value {
	color: black;
}

.pb10 {
	padding-bottom: 10px;
}

.w50 {
	width: 50%;
}

.w100 {
	width: 100%;
}

.d-flex {
	display: flex;
}

.payment-btn:hover {
	background-color: #4070e0;
}

.mobile-tr {
	display: none;
}

.label-td {
	width: 35%;
}

td label {
	word-break: break-all;
}

@media only screen and (max-width: 1200px) {
	.trans-note {
		padding: 0px 5px 20px 5px !important;
	}
	.table-div {
		width: 98% !important;
	}
}

@media only screen and (max-width: 1100px) {
	.desktop-tr td label {
		font-size: 16px !important;
	}
	.query-text {
		font-size: 14px;
	}
	.help-div a {
		font-size: 14px;
	}
}

@media only screen and (max-width: 900px) {
	.table-div {
		width: 98% !important;
	}
	.main-div {
		width: 100% !important;
	}
	.footer-div {
		width: 100% !important;
	}
}
/* jyoti */
@media only screen and (min-width: 900px) {
	.footer-div {
		position: unset !important;
	}
}

@media only screen and (max-width: 780px) {
	.footer-div {
		width: 100% !important;
	}
	.main-div {
		width: 100% !important;
	}
	.mer-name {
		font-size: 25px !important;
	}
	.amount-text {
		font-size: 22px !important;
	}
	.amount-digit {
		font-size: 14px !important;
	}
	.query-text {
		font-size: 12px;
	}
	.help-div a {
		font-size: 12px;
	}
}

@media only screen and (max-width: 550px) {
	.mer-name {
		font-size: 25px !important;
	}
	.amount-text {
		font-size: 22px !important;
	}
	.amount-digit {
		font-size: 14px !important;
	}
	.main-div {
		width: 100% !important;
		padding: 0 !important;
	}
	.desktop-tr td label {
		font-size: 12px !important;
	}
}

@media only screen and (max-width: 490px) {
	.footer-div {
		position: unset !important;
	}
	.table-div {
		width: 98% !important;
	}
	.payment-btn-div a {
		padding: calc(0.875rem - 1px) calc(1.5rem - 1px) !important;
	}
	.desktop-tr {
		display: none;
	}
	.mobile-tr {
		display: block;
	}
}
/* jyoti */
@media only screen and (min-width: 490px) {
	.footer-div {
		position: unset !important;
	}
}

@media only screen and (max-width: 330px) {
	.table-div {
		width: 100% !important;
	}
	.payment-btn-div a {
		padding: calc(0.875rem - 1px) calc(1.5rem - 1px) !important;
	}
}
</STYLE>
</HEAD>
<body
	style="font-family: system-ui, -apple-system, system-ui, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-weight: 500; background-color: #f1f1f1;">
	<div class="main-div" style="margin: 0 auto; width: 40%;">
		<div style="margin-bottom: 30px">
			<div
				style="background-color: #1b54dd; text-align: center; padding: 30px; text-align: center;">
				<label class="mer-name"
					style="text-transform: uppercase; color: white; font-size: 30px; font-weight: 400 !important;">
					<%=merchName%></label>
			</div>
			<div>
			<!-- 			jyoti padding: 25px 0 20px 0; -->
				<div class="trans-status-amount-div"
					style="text-align: center; padding: 25px 0 20px 0; background-color: white; display: flex; align-items: center; justify-content: center;">
					<div style="padding-right: 12px;">
						<% if(transStatus.equalsIgnoreCase("Ok")){%>
						<img src="images/svgs/success.svg" alt="success">
						<% } else if (transStatus.equalsIgnoreCase("To"))
							{%>
						<img src="images/svgs/pending.svg" alt="pending">
						<% } else{%>
						<img src="images/svgs/failed.svg" alt="failed">
						<% }%>
					</div>
					<div>
						<div class="amount-text"
							style="color: black; font-weight: 600; font-size: 32px;">
							&#8377;
							<%=respJson.getString("txn_amount")%></div>
						<div class="amount-digit" style="color: #808893; font-size: 18px;">
							<label><%=message%></label>
						</div>
					</div>
				</div>
				<%
					if(!transNote.equals("")){
				%>
<!-- 				jyoti padding: 0px 125px 10px 125px; -->
				<div class="trans-note"
					style="padding: 0px 125px 10px 125px; background-color: white; text-align: center;">
					<label style="color: #747d8a; font-size: 14px;"><%=transNote%></label>
				</div>
				<%} %>
				<div style="background-color: white; padding-bottom: 30px;">
					<div
						style="width: calc(95% - 100px); margin: 0 auto; background-color: white;"
						class="table-div">
						<table style="border: #1b54dd solid 3px; width: 100%;">
							<tbody>
								<tr class="desktop-tr">
									<td class="label-td" style="padding: 10px 0 10px 0;"><label
										style="margin-left: 20px; color: #747d8a; display: inline-block;">Invoice
											No.</label></td>
									<td style="padding: 10px 0 10px 0;"><label
										style="color: black; font-weight: 500;"><%=invoiceNo%></label>
									</td>
								</tr>
								<tr class="desktop-tr">
									<td class="label-td"
										style="padding: 20px 0 10px 0; vertical-align: top;"><label
										style="margin-left: 20px; color: #747d8a; display: inline-block;">Transaction
											Id</label></td>
									<td style="padding: 20px 0 10px 0;"><label
										style="color: black; font-weight: 500;"><%=txnId%></label></td>
								</tr>
								<% if(transStatus.equalsIgnoreCase("Ok")){%>
								<tr class="desktop-tr">
									<td class="label-td"
										style="padding: 10px 0 10px 0; vertical-align: top;"><label
										style="margin-left: 20px; color: #747d8a; display: inline-block;">Paid
											On</label></td>
									<td
										style="padding: 10px 0 10px 0; color: black; font-weight: 500; word-wrap: break-word; word-break: break-all; text-align-last: left;">
										<label style="color: black; font-weight: 500;"><%=paidOn%></label>
									</td>
								</tr>
								<% } else if (transStatus.equalsIgnoreCase("To"))
									{%>
								<tr class="desktop-tr">
									<td class="label-td"
										style="padding: 10px 0 10px 0; vertical-align: top;"><label
										style="margin-left: 20px; color: #747d8a; display: inline-block;">Attempted
											On</label></td>
									<td
										style="padding: 10px 0 10px 0; color: black; font-weight: 500; word-wrap: break-word; word-break: break-all; text-align-last: left;">
										<label style="color: black; font-weight: 500;"><%=attemptedOn%></label>
									</td>
								</tr>
								<% } else{%>
								<tr class="desktop-tr">
									<td class="label-td"
										style="padding: 10px 0 10px 0; vertical-align: top;"><label
										style="margin-left: 20px; color: #747d8a; display: inline-block;">Attempted
											On</label></td>
									<td
										style="padding: 10px 0 10px 0; color: black; font-weight: 500; word-wrap: break-word; word-break: break-all; text-align-last: left;">
										<label style="color: black; font-weight: 500;"><%=attemptedOn%></label>
									</td>
								</tr>
								<% }%>
								<tr class="desktop-tr">
									<td class="label-td" style="padding: 10px 0 20px 0;"><label
										style="margin-left: 20px; color: #747d8a; display: inline-block; word-break: break-word;">Payment
											Method</label></td>
									<td
										style="padding: 10px 0 20px 0; display: flex; flex-direction: column;">
										<label style="color: black; font-weight: 500;"><%=paymentMethod%></label>
										<label style="color: black; font-weight: 500;"><%=bankName%></label>
									</td>
								</tr>

								<!--mobile td-->
								<tr class="mobile-tr">
									<td style="padding: 20px 0 0 0;"><label
										style="margin-left: 20px; color: #747d8a; display: inline-block;">Invoice
											No.</label></td>
								</tr>
								<tr class="mobile-tr">
									<td style="padding: 0px 0 10px 0;"><label
										style="color: black; margin-left: 20px; font-weight: 500;"><%=invoiceNo%></label>
									</td>
								</tr>
								<tr class="mobile-tr">
									<td style="padding: 10px 0 0 0;"><label
										style="margin-left: 20px; color: #747d8a; display: inline-block;">Transaction
											Id</label></td>
								</tr>
								<tr class="mobile-tr">
									<td
										style="padding: 0px 0 10px 0; color: black; margin-left: 20px; font-weight: 500; word-wrap: break-word; word-break: keep-all; text-align-last: left; padding-left: 20px;">
										<%=txnId%>
									</td>
								</tr>
								<% if(transStatus.equalsIgnoreCase("Ok")){%>
								<tr class="mobile-tr">
									<td style="padding: 10px 0 0 0;"><label
										style="margin-left: 20px; color: #747d8a; display: inline-block;">Paid
											On</label></td>
								</tr>
								<tr class="mobile-tr">
									<td style="padding: 0px 0 10px 0;"><label
										style="color: black; margin-left: 20px; font-weight: 500;"><%=paidOn%></label>
									</td>
								</tr>
								<% } else if (transStatus.equalsIgnoreCase("To"))
									{%>
								<tr class="mobile-tr">
									<td style="padding: 10px 0 0 0;"><label
										style="margin-left: 20px; color: #747d8a; display: inline-block;">Attempted
											On</label></td>
								</tr>
								<tr class="mobile-tr">
									<td style="padding: 0px 0 10px 0;"><label
										style="color: black; margin-left: 20px; font-weight: 500;"><%=attemptedOn%></label>
									</td>
								</tr>
								<% } else{%>
								<tr class="mobile-tr">
									<td style="padding: 10px 0 0 0;"><label
										style="margin-left: 20px; color: #747d8a; display: inline-block;">Attempted
											On</label></td>
								</tr>
								<tr class="mobile-tr">
									<td style="padding: 0px 0 10px 0;"><label
										style="color: black; margin-left: 20px; font-weight: 500;"><%=attemptedOn%></label>
									</td>
								</tr>
								<% }%>
								<tr class="mobile-tr">
									<td style="padding: 10px 0 0 0;"><label
										style="margin-left: 20px; color: #747d8a; display: inline-block; word-break: break-word;">Payment
											Method</label></td>
								</tr>
								<tr class="mobile-tr">
									<td
										style="padding: 0px 0 20px 0; display: flex; flex-direction: column;">
										<label
										style="color: black; margin-left: 20px; font-weight: 500;"><%=paymentMethod%></label>
										<label
										style="color: black; margin-left: 20px; font-weight: 500;"><%=bankName%></label>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div
			style="background-color: #e3e1da; position: fixed; bottom: 0; width: 100%;"
			class="footer-div">
			<div class="social-links-div"
				style="text-align: center; padding-top: 15px;">
				<img src="images/svgs/twitter.svg" style="padding-right: 15px;"
					alt="twitter"> <img src="images/svgs/linkedin.svg"
					alt="linkedin">
			</div>
			<div class="help-div" style="text-align: center;">
				<div class="query-text">For any transaction related queries,
					please reach out to us at</div>
				<a style="text-decoration: none; color: #3290d3; font-weight: 500;"
					class="Epaisaa-support-mail" href="mailto:pg.support@Epaisaa.in">pg.supporh.in</a>
			</div>
			<div class="footer-logo-div"
				style="padding: 10px 0 10px 0; text-align: center;">
				<img src="images/svgs/logo.svg" alt="pay">
			</div>
		</div>
	</div>
</body>
<%} %>
</HTML>