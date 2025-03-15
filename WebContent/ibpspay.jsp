<%@ include file="/include.jsp"%>
<%@page import="com.rew.payment.utility.PGUtils"%>
<%@page import="com.apps.lbps.mware.processor.CRUDService"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.concurrent.TimeUnit"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.format.DateTimeFormatterBuilder"%>
<%@page import="java.time.temporal.ChronoField"%>
<%@page import="com.rew.pg.db.DBConnectionHandler"%>

<%@page import="java.sql.*"%>

<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>

<%
	Logger log = LoggerFactory.getLogger("JSPS.ibpspay.jsp"); 
log.info("Inside ibpspay.jsp:::");
	String pmerchantId = request.getParameter("merch");
	log.info("merchantid" + pmerchantId);
	String pinvoiceId = request.getParameter("txn");
	log.info("pinvoiceId " + pinvoiceId);
	String payLinkId = request.getParameter("payLinkId");
	log.info("payLinkId " + payLinkId);
	String apiKey = "";
	String amount = "0";
	String merchantName = "NA";
	String emailid = "NA";
	String contactno = "NA";
	
	//reseller
	String pRid="NA";
	String pResellerTxnId="NA";
	String message = "Payment for the invoice is already made";
	Map<Long, String> ordinalNumbers = new HashMap<>(42);
	ordinalNumbers.put(1L, "1st");
	ordinalNumbers.put(2L, "2nd");
	ordinalNumbers.put(3L, "3rd");
	ordinalNumbers.put(21L, "21st");
	ordinalNumbers.put(22L, "22nd");
	ordinalNumbers.put(23L, "23rd");
	ordinalNumbers.put(31L, "31st");
	for (long d = 1; d <= 31; d++) {
		ordinalNumbers.putIfAbsent(d, "" + d + "th");
	}
	DateTimeFormatter dayOfMonthFormatter = new DateTimeFormatterBuilder()
			.appendText(ChronoField.DAY_OF_MONTH, ordinalNumbers).appendPattern(" MMMM, yyyy").toFormatter();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	Connection conn = null;
	PreparedStatement stmt = null;
	PreparedStatement stmt1 = null;
	PreparedStatement stmt2 = null;
	ResultSet rs1 = null;
	ResultSet rs2 = null;
	ResultSet rs3 = null;
	int bPayment = 1;
	String invoiceNo = pinvoiceId;
	String txnId = "";
	String paidOn = "";
	String attemptedOn = "";
	String paymentMethod = "";
	String bankName = "";
	String transStatus = "";
	String txnAmount = "";
	String merchName = "";
	String isDeleted = "";
	boolean validityExpired = false;
	boolean transactionPending = false;
	boolean isValidTxn = false;
	String transNote = "";
	try {
		log.info("Init connection >>>>>> ");
		conn = DBConnectionHandler.getConnection();
		if(payLinkId!=null){
			String sql1 = "select * from tbl_mstibps where payment_link_id=?"; 
			stmt = conn.prepareStatement(sql1);
			stmt.setString(1, payLinkId);
			rs1 = stmt.executeQuery();
		}else{
			String sql1 = "select * from tbl_mstibps where invoice_no=? and merchant_id=?";
			stmt = conn.prepareStatement(sql1);
			stmt.setString(1, pinvoiceId);
			stmt.setString(2, pmerchantId);
			rs1 = stmt.executeQuery();
		}
		log.info("Statement 1 :: " + stmt);
		if (rs1.next()) {
			if(payLinkId!=null){
				pinvoiceId = rs1.getString("invoice_no");
				pmerchantId = rs1.getString("merchant_id");
				//for reseller
				pRid = rs1.getString("Rid");
				pResellerTxnId = rs1.getString("ResellerTxnId");
				invoiceNo = pinvoiceId;
			}
			log.info("pinvoiceid " + pinvoiceId + " pmerchantId " + pmerchantId + " amount " + rs1.getString("amount")
			+ " Status " + rs1.getString("status"));
			isDeleted = rs1.getString("IsDeleted");
			if(isDeleted!=null && !isDeleted.equals("") && isDeleted.equalsIgnoreCase("Y")){
				bPayment = 5;
				message = "This payment has been cancelled.";
			}else{
				emailid = rs1.getString("email_id");
				contactno = rs1.getString("contact_number");
				amount = rs1.getString("amount");
				String s = rs1.getString("valid_upto");
				String invoice_no = rs1.getString("invoice_no");
				String merId = rs1.getString("merchant_id");
				Date dtOld = new SimpleDateFormat("dd-MM-yyyy").parse(s);
				String todaysDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
				log.info(todaysDate);
				Date todaysDt = new SimpleDateFormat("dd-MM-yyyy").parse(todaysDate);
				Long l = DBConnectionHandler.getDateDiff(todaysDt, dtOld, TimeUnit.DAYS);
				log.info("Days diff::", l);
				long days = TimeUnit.MILLISECONDS.toDays(l);
				log.info("days >>>> >> " + days);
				CRUDService cd = new CRUDService();
				isValidTxn = cd.isValidTxn(invoice_no, merId);
				log.info("isValidTxn >>>>  " + isValidTxn);
				if (isValidTxn) {
					bPayment = 3;
					log.info("inside isValidTxn error redirect");
					transNote = "In case your money has been debited, please check back after 2 hours.";
				} else if ((!rs1.getString("status").equalsIgnoreCase("1")) && (days >= 0)) {
					String sql2 = "select transaction_key,merchant_name from tbl_mstmerchant where MerchantId=?";
					stmt1 = conn.prepareStatement(sql2);
					stmt1.setString(1, pmerchantId);
					rs2 = stmt1.executeQuery();
					log.info("Statement 2::" + stmt1);
					if (rs2 != null && rs2.next()) {
						apiKey = rs2.getString("transaction_key");
						merchantName = rs2.getString("merchant_name");
					} else {
						bPayment = 0;
					}
				} else {
					bPayment = 2;
					if (days >= 0) {
						message = "Payment for the invoice is already made.";
						//this message currently will not print it has been replaced with other message
					} else {
						log.info("Invoice Validity expired");
						validityExpired = true;
						cd.updatePaymentStatus(pinvoiceId.trim(), "4");
						message = "Validity of the invoice is expired.";
					}
				}
				if(!validityExpired){
					String sql3 = "select *,Case when T1.bank_id = 'FINPG' then 'FINPG' when T1.bank_id = 'NA' or 'RES' or null then 'NA' else T2.BankName end as BankName "
					+ "from  tbl_transactionmaster T1 inner join tbl_mstpgbank T2 on T1.bank_id=T2.bankId inner join tbl_mstmerchant T3"
					+ " on T3.MerchantId = T1.merchant_id where T1.txn_Id LIKE ? and T1.merchant_id=?";
					stmt2 = conn.prepareStatement(sql3);
					stmt2.setString(1, pinvoiceId + "-%");
					stmt2.setString(2, pmerchantId);
					rs3 = stmt2.executeQuery();
					log.info("Statement 3:: " + stmt2);
					if (rs3.next()) {
						txnId = rs3.getString("Id");
						contactno = rs3.getString("contact_number");
						String respDateString = rs3.getString("resp_date_time");
						Date respDate = new SimpleDateFormat("yyyy-MM-dd").parse(respDateString);
						String dateTimeString = rs3.getString("date_time");
						Date dateTime = new SimpleDateFormat("yyyy-MM-dd").parse(dateTimeString);
						paidOn = LocalDate.parse(new SimpleDateFormat("dd-MM-yyyy").format(respDate), formatter)
								.format(dayOfMonthFormatter);
						attemptedOn = LocalDate.parse(new SimpleDateFormat("dd-MM-yyyy").format(dateTime), formatter)
								.format(dayOfMonthFormatter);
						paymentMethod = rs3.getString("instrument_id");
						bankName = rs3.getString("BankName");
						transStatus = rs3.getString("trans_status");
						txnAmount = rs3.getString("txn_amount");
						merchName = rs3.getString("business_name").toUpperCase();
						if (transStatus.equalsIgnoreCase("Ok")) {
							message = "Paid Successfully!";
						} else if (transStatus.equalsIgnoreCase("To")) {
							message = "Transaction Pending!";
						} else {
							message = "Transaction Failed!";
							transNote = "In case your money has been debited, it will be refunded in 5-7 working days.";
						}
						bPayment = 4;
					}
				}
			}
		} else {
			bPayment = 3;
			message = "Invalid Invoice Number.";
		}
		log.info("Message::" + message);
		log.info("bPayment::" + bPayment);
		if (bPayment == 1) {
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" "w3.org/TR/REC-html40/loose.dtd">
<html>
<TITLE>Merchant Response Page</TITLE>
<body onload="document.myform.submit();">
	<form action="ibpsRedirector.jsp" method="post" name="myform">
		<input type="hidden" name="merchantId" value="<%=pmerchantId%>">
		<input type="hidden" name="apiKey" value="<%=apiKey%>">
		<%
		log.info("....api == TKAY " + apiKey);
		%>
		<input type="hidden" name="merchant_name" value="<%=merchantName%>">
		<input type="hidden" name="amount" value="<%=amount%>">

		<%
		log.info(".....Amount " + amount);
		%>
		<input type="hidden" name="txnId"
			value="<%=pinvoiceId + "-" + PGUtils.randomNumeric(4)%>">

		<!-- //added two fields -->
		<input type="hidden" name="Rid" value="<%=pRid%>">
			<input type="hidden" name="ResellerTxnId"  value="<%=pResellerTxnId%>">
			
		<input type="hidden" name="custMobile" value="<%=contactno%>">
		<input type="hidden" name="custMail" value="<%=emailid%>"> <input
			type="hidden" name="txnType" value="DIRECT">
			
			
	</form>
</body>
<%
	} else if (bPayment == 3 || bPayment == 5 || validityExpired) {
%>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<TITLE>Merchant Response Page</TITLE>
<style type='text/css'>
.content {
	text-align: center;
	padding: 40px 25px 25px 25px;
}

body {
	background-color: #f1f1f1;
	margin: 0;
}

.footer-div {
	position: fixed;
	bottom: 0;
	width: 100%;
	background-color: white;
}
</style>
<div
	style="font-family: system-ui, -apple-system, system-ui, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-weight: 500;">
	<div>
		<div
			style="background-color: #1b54dd; text-align: center; padding: 30px; text-align: center;">
			<label class="mer-name"
				style="text-transform: uppercase; color: white; font-size: 30px; font-weight: 400 !important;">
				Merchant Response</label>
		</div>
		<div class="content">
			<%
			if (validityExpired) {
			%>
			<i style="font-size: 90px; color: red;" class="fa fa-clock-o"></i>
			<%
			} else {
			%>
			<i style="font-size: 65px; color: red;" class="fa fa-warning"></i>
			<%
			}
			%>
			<%
			if (isValidTxn) {
			%>
			<p style="font-size: 25px;"><%=transNote%></p>
			<%
			} else {
			%>
			<p style="font-size: 25px;"><%=message%></p>
			<%
			}
			%>
		</div>
	</div>
	<div class="footer-div">
		<div class="social-links-div"
			style="text-align: center; padding-top: 15px;">
			<img src="images/svgs/twitter.svg" style="padding-right: 15px;"
				alt="twitter"> <img src="images/svgs/linkedin.svg"
				alt="linkedin">
		</div>
		<div class="help-div"
			style="text-align: center; padding: 10px 0 10px 0;">
			<div class="query-text">For any transaction related queries,
				please reach out to us at</div>
			<a style="text-decoration: none; color: #3290d3; font-weight: 500;"
				class="pay-support-mail" href="mailto:pg.in">in</a>
		</div>
		<div class="footer-logo-div"
			style="padding: 10px 0 10px 0; text-align: center;">
			<img src="images/svgs/PAY.svg" alt="pay">
		</div>
	</div>
</div>
<%
	} else {
%>
<HEAD>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet"
	href="vendor/fontawesome/css/font-awesome.min.css">
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

.trans-status-amount-div {
	display: flex;
	align-items: center;
	justify-content: center;
	background-color: red;
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
</style>
</head>
<body
	style="font-family: system-ui, -apple-system, system-ui, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-weight: 500; background-color: #f1f1f1;">
	<div class="main-div" style="margin: 0 auto; width: 40%;">
		<div style="margin-bottom: 25px">
			<div
				style="background-color: #1b54dd; text-align: center; padding: 30px; text-align: center;">
				<label class="mer-name"
					style="text-transform: uppercase; color: white; font-size: 30px; font-weight: 400 !important;">
					<%=merchName%></label><i class="fas fa-times"></i>
			</div>
<!-- 			jyoti padding: 25px 0 20px 0; -->
			<div class="trans-status-amount-div"
				style="text-align: center; padding: 25px 0 20px 0; background-color: white; display: flex; align-items: center; justify-content: center;">
				<div style="padding-right: 12px;">
					<%
					if (transStatus.equalsIgnoreCase("Ok")) {
					%>
					<img src="images/svgs/success.svg" alt="success">
					<%
					} else if (transStatus.equalsIgnoreCase("To")) {
						
					%>
					<img src="images/svgs/pending.svg" alt="pending">
					<%
					} else {
					%>
					<img src="images/svgs/failed.svg" alt="failed">
					<%
					}
					%>
				</div>
				<div
					style="display: flex; flex-direction: column; align-items: center;">
					<div class="amount-text"
						style="color: black; font-weight: 600; font-size: 32px;">
						&#8377;
						<%=txnAmount%></div>
					<div class="amount-digit" style="color: #808893; font-size: 15px;">
						<label><%=message%></label>
					</div>
				</div>
			</div>
			<%
			if (!transNote.equals("")) {
			%>
<!-- 			jyoti padding: 0px 125px 10px 125px; -->
			<div class="trans-note"
				style="padding: 0px 125px 10px 125px; background-color: white; text-align: center;">
				<label style="color: #747d8a; font-size: 14px;"><%=transNote%></label>
 			</div>
			<%
			}
			%>
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
							<%
							if (transStatus.equalsIgnoreCase("Ok")) {
							%>
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
							<%
							} else if (transStatus.equalsIgnoreCase("To")) {
							%>
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
							<%
							} else {
							%>
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
							<%
							}
							%>
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
							<%
							if (transStatus.equalsIgnoreCase("Ok")) {
							%>
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
							<%
							} else if (transStatus.equalsIgnoreCase("To")) {
							%>
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
							<%
							} else {
							%>
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
							<%
							}
							%>
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
		<div class="footer-div"
			style="background-color: #e3e1da; position: fixed; bottom: 0; width: 100%!important;">
			<div class="social-links-div"
				style="text-align: center; padding-top: 15px;">
				<img src="images/svgs/twitter.svg" style="padding-right: 15px;"
					alt="twitter"> <img src="images/svgs/linkedin.svg"
					alt="linkedin">
			</div>
			<div class="help-div"
				style="text-align: center; /* padding: 10px 0 10px 0; */">
				<div class="query-text">For any transaction related queries,
					please reach out to us at</div>
				<a style="text-decoration: none; color: #3290d3; font-weight: 500;"
					class="pay-support-mail" href="mailto:#">#</a>
			</div>
			<div class="footer-logo-div"
				style="padding: 10px 0 10px 0; text-align: center;">
				<img src="images/svgs/PAY.svg" alt="pay">
			</div>
		</div>
	</div>
</body>
</html>
<%
		}
	} catch (Exception e) {
		logger.error("Error occured inside ibpspay.jsp", e);
	} finally {
		try {
		if (rs1 != null) {
			rs1.close();
			rs1 = null;
		}
		if (rs2 != null) {
			rs2.close();
			rs2 = null;
		}
		if (rs3 != null) {
			rs3.close();
			rs3 = null;
		}
		if (stmt != null) {
			stmt.close();
			stmt = null;
		}
		if (stmt1 != null) {
			stmt1.close();
			stmt1 = null;
		}
		if (stmt2 != null) {
			stmt2.close();
			stmt2 = null;
		}
		if (conn != null) {
			conn.close();
			conn = null;
		}
		} catch (SQLException localSQLException6) {
		logger.error("ibpspay.jsp ::: inside finally's catch:: Error Occurred while closing Connection : ",
				(Throwable) localSQLException6);
		}
}
%>
