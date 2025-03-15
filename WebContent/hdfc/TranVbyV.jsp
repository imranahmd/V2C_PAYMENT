<%@ include file="/include.jsp"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.rew.pg.db.DBConnectionHandler"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.rew.payment.utility.PGUtils"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.rew.pg.db.DataManager"%>
<%@page import="com.rew.pg.dto.TransactionMaster"%>
<%@ page language="java" session="true"%>
<%@ page import="java.util.StringTokenizer"%>
<%@ page import="java.util.Random"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.io.*"%>
<%@ page import="java.io.BufferedInputStream"%>
<%@ page import="java.io.BufferedReader"%>
<%@ page import="java.io.DataOutputStream"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.FileOutputStream"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.io.InputStreamReader"%>
<%@ page import="java.io.PrintStream"%>
<%@ page import="java.net.URL"%>
<%@ page import="java.net.URLConnection"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.zip.ZipEntry"%>
<%@ page import="java.util.zip.ZipFile"%>
<%@ page import="javax.net.ssl.HttpsURLConnection"%>
<%@ page import="java.net.HttpURLConnection"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.CallableStatement"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%
response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setHeader("Expires", "0");
response.setDateHeader("Expires", -1);
response.setDateHeader("Last-Modified", 0);
/*
******************************************************************
* COMPANY    - FSS Pvt. Ltd.
******************************************************************

Name of the Program : Tranportal VbyV [3D Secure] UMI Pages
Page Description    : Allows Merchant to connect Payment Gateway and send request
Request parameters  : TranporatID,TranportalPassword,Action,Amount,CardNumber,CVV,
  ExpiryMonth,ExpiryYear,CardHolderName,Currency,TrackID,UDF1 to UDF5.
Response parameters : Result,Amount,Track ID,PaymentID Transaction ID, Reference ID, UDF1 to UDF5, Auth Code,postdate,AVR,Error
Values from Session : No
Values to Session   : No
Created by          : FSS Payment Gateway Team
Created On          : 01-01-2013
Version             : Version 2.0
******************************************************************************

The set of pages are developed and tested using below set of hardware and software only. 
In case of any issues noticed by merchant during integration, merchant can contact respective bank 
for technical assistance

NOTE - 
This pages are developed and tested on below platform
Java Version     - Sun JDK 1.6 and above
Application      - Tomcat 6.0
Operating System - Windows Server 2003
****************************************************************
*/
/*
*******IMPORTANT INFORMATION**************
This document is provided by Financial Software and System Pvt Ltd on the basis 
that you will treat it as private and confidential.
Data used in examples and sample data files are intended to be fictional and any 
resemblance to real persons or entities is entirely coincidental.
This example assumes that a form has been sent to this example with the required 
fields. The example then processes the command and displays the receipt or error 
to a HTML page in the users web browser.
*/
/*
Getting Required User Fields from Initial HTML page
Since this page for demonstration, values from HTML page are directly
taken from browser and used for transaction processing. 
Merchants SHOULD NOT follow this practice in production environment.
*/
/*Below are the parameters are taken from Initial Page for demonstration,
we suggest merchant SHOULD take this values from secure channel*/

Logger log = LoggerFactory.getLogger("JSPS.TranVbyV.jsp");
DataManager dm = new DataManager();
String spMid = null, spKey = null;
String Mid = request.getParameter("Mid");
String bankId = request.getParameter("bankId");
String instrumentId = request.getParameter("instrumentId");
String cardno = request.getParameter("cardno");
String cvv = request.getParameter("cvv");

String expmm = request.getParameter("expmm");
String expyy = request.getParameter("expyy");

String tokenCardNumber = request.getParameter("tokenCardNumber");
String tokenExpiryMM = request.getParameter("tokenExpiryMM");
String tokenExpiryYY = request.getParameter("tokenExpiryYY");
String amt = request.getParameter("amt");
String MTrackid = request.getParameter("MTrackid"); // our txnId atrnId
String membername = request.getParameter("membername");
String spId = request.getParameter("spId");
TransactionMaster TM = null;
if (request.getParameter("MD") != null) {
	log.info("11111111111");
	TM = dm.getTransMasterbyAuthID(request.getParameter("MD"));
} else {
	log.info("222222222");
	TM = dm.getTxnMaster(MTrackid);

}

if (request.getParameter("MD") != null) {

	log.info("Inside second response from V/V Page >>>>>> for id=" + TM.getId() + " respstatus=" + TM.getRespStatus());
	if (TM.getRespStatus().equalsIgnoreCase("5")) {
		Mid = TM.getMerchantId();
		bankId = TM.getBankId();
		instrumentId = TM.getInstrumentId();
		spId = TM.getProcessId();
		TM.setRespStatus("0");

		log.info("TransVbyV.jsp ::: second response TransVbyV.jsp ::: for txnid=" + Mid + " bankId : " + bankId+ " :: Mid : " + Mid + "  instrumentId ::: :: " + instrumentId);
	}

}
log.info("respStatus >>>>>>>>>>>>>> " + TM.getRespStatus());
if (TM.getRespStatus().equalsIgnoreCase("0")) {
	TM.setRespStatus("5");
	dm.updateTxn(TM);

	log.info("for txnid=" + MTrackid + " spId >>>>>>>>>>>>>>>> " + spId);

	if (request.getParameter("tType") != null && request.getParameter("tType").equalsIgnoreCase("DIRECT")) {
		if (cardno != null && cvv != null && !cardno.isEmpty() && !cvv.isEmpty()) {

			String sCardNumber = PGUtils.cryptoJs(request.getParameter("cardno"));
			String[] args = sCardNumber.split("\\|");

			if (args.length > 1 && MTrackid.equals(args[1])) {
				log.info("TranVby.jsp ::: for id=" + MTrackid + " There is no tempered ... txn id is match...instrument"
						+ instrumentId);
				cardno = args[0];
				cardno = cardno.replaceAll(" ", "").trim();

				//validating Bin
				String binstatus = dm.getBinvalidation(MTrackid, cardno.substring(0, 9), instrumentId);
				log.info("for txnid=" + MTrackid + " binstatus >>>>>>>>>>>>>>>>>> " + binstatus);

			}

			else {

				log.info("TranVby.jsp ::: for txnid=" + MTrackid + " --> Invalid card number  ");
				request.setAttribute("errorMsg", "Error 10050 : Invalid card number.. ");
				request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
				out.flush();
				out.close();
			}

			cvv = PGUtils.cryptoJs(request.getParameter("cvv"));
		}
	else if (tokenCardNumber != null && tokenExpiryYY != null && tokenExpiryMM !=null && !tokenCardNumber.isEmpty() && !tokenExpiryMM.isEmpty() && !tokenExpiryYY.isEmpty()) {

			cardno = tokenCardNumber;
			expyy = tokenExpiryYY;
			expmm= tokenExpiryMM;
		}
	cvv = PGUtils.cryptoJs(request.getParameter("cvv"));
	}else if (request.getParameter("tType") != null && request.getParameter("tType").equalsIgnoreCase("REDIRECT")) {
		
	 if (tokenCardNumber != null && tokenExpiryYY != null && tokenExpiryMM !=null && !tokenCardNumber.isEmpty() && !tokenExpiryMM.isEmpty() && !tokenExpiryYY.isEmpty()) {

			cardno = tokenCardNumber;
			expyy = tokenExpiryYY;
			expmm= tokenExpiryMM;
		}
	}
	//log.info("TranVby.jsp ::: --> Invalid card number  "+Mid+bankId+instrumentId);
	try {
		if (Mid != null && bankId != null && instrumentId != null && !Mid.isEmpty() && !bankId.isEmpty()
				&& !instrumentId.isEmpty()) {
			/* ArrayList<String> spMidKey = dm.getSPMidKey(Mid,bankId,instrumentId,PGUtils.getPropertyValue("spIdHDFC")); */

			ArrayList<String> spMidKey = null;
			if (!bankId.equalsIgnoreCase("Rupay")) {
/* 				spId="12";
 */				spMidKey = dm.getSPMidKey(Mid, bankId, instrumentId, spId);
				//PGUtils.getPropertyValue("spIdHDFC")
			} else {
				spMidKey = dm.getSPMidKey(Mid, bankId, instrumentId, PGUtils.getPropertyValue("spIdHDFCRupay"));//PGUtils.getPropertyValue("spIdHDFCRupay")
			}

			if (spMidKey != null && spMidKey.size() > 0) {
				spMid = spMidKey.get(0).toString();
				spKey = spMidKey.get(1).toString();

				session.setAttribute("spMid", spMid);
				session.setAttribute("spKey", spKey);

				log.info("TransVbyV.jsp :::for txnid=" + MTrackid + "  Attributes set in Session :: SPMID : "
						+ session.getAttribute("spMid") + " And SPKEY : " + session.getAttribute("spKey"));
			}
		} else //show mw the propertyfi
		{
			spMid = session.getAttribute("spMid").toString();
			spKey = session.getAttribute("spKey").toString();

			log.info("TransVbyV.jsp ::: for txnid=" + MTrackid + " Fetch Attributes from Session :: SPMID : "
					+ session.getAttribute("spMid") + " And SPKEY : " + session.getAttribute("spKey"));
		}
	} catch (Exception e) {
		log.error("TransVbyV.jsp ::: for txnid=" + MTrackid + " Error in fetching SPMID and SPKEY :: ", e);
	}

	log.info("TransVbyV.jsp :::for txnid=" + MTrackid + "  SPMID : " + spMid + " :: SPKEY : " + spKey);

	/* String expmm=request.getParameter("expmm");
	String expyy=request.getParameter("expyy");
	String amt=request.getParameter("amt");
	String MTrackid=request.getParameter("MTrackid");
	String membername=request.getParameter("membername"); */

	//************START--PG Initial Request Parameters have to set here**************//

	/* to pass Tranportal ID provided by the bank to merchant. Tranportal ID is sensitive information of 
	merchant from the bank, merchant MUST ensure that Tranportal ID is never passed to customer browser 
	by any means. Merchant MUST ensure that Tranportal ID is stored in secure environment & securely at 
	merchant end. Tranportal Id is referred as id. Tranportal ID for test and production will be different.
	please contact bank for test and production Tranportal ID*/
	// Here XXXXXX refers to tranportal id of the respective merchant,Merchant should replace this with his original tranportal ID//	
	String TranportalID = "<id>" + spMid + "</id>"; //Mandatory

	/* to pass Tranportal password provided by the bank to merchant. Tranportal password is sensitive 
	information of merchant from the bank, merchant MUST ensure that Tranportal password is never passed 
	to customer browser by any means. Merchant MUST ensure that Tranportal password is stored in secure 
	environment & securely at merchant end. Tranportal password is referred as password. Tranportal 
	password for test and production will be different, please contact bank for test and production
	Tranportal password */
	// Here XXXXXX refers to tranportal password of the respective merchant ,Merchant should replace this with his original Password//	
	String TranportalPwd = "<password>" + spKey + "</password>"; //Mandatory

	/*Setting Customer Card NO*/
	String strcard = "<card>" + cardno + "</card>"; //Mandatory for Action code "1" & "4"

	/*Setting Customer Card CVV value*/
	String strcvv = "<cvv2>" + cvv + "</cvv2>"; //Mandatory for Action code "1" & "4"

	/*Setting Customer Card expiry year value in YYYY format */
	String strexpyear = "<expyear>" + expyy + "</expyear>"; //Mandatory for Action code "1" & "4"

	/*Setting Customer Card expiry Month value in MM format */
	String strexpmonth = "<expmonth>" + expmm + "</expmonth>"; //Mandatory for Action code "1" & "4"

	/* Action Code of the transaction, this refers to type of transaction. Action Code 1 stands of 
	Purchase transaction and Action code 4 stands for Authorization (pre-auth). Merchant should 
	confirm from Bank action code enabled for the merchant by the bank*/
	String straction = "<action>1</action>"; //Mandatory

	/* Transaction Amount that will be send to payment gateway by merchant for processing
	NOTE - Merchant MUST ensure amount is sent from merchant back-end system like database
	and not from customer browser. In below sample amount is taken from Initial HTML page, merchant need to pass 
	transaction amount ,amount including decimal point if required*/
	String stramt = "<amt>" + amt + "</amt>"; //Mandatory
	log.info("for txnid=" + MTrackid + " amt :::####  " + amt);
	/* Currency code of the transaction. By default INR i.e. 356 is configured. If merchant wishes 
	to do multiple currency code transaction, merchant needs to check with bank team on the available 
	currency code */
	String strcurrency = "<currencycode>356</currencycode>"; //Mandatory

	/*Setting CardHolder Name/Member name */
	String strmember = "<member>" + membername + "</member>"; //Mandatory

	/* To pass the merchant track id, in below sample merchant track id taken from Initial HTML page. 
	Merchant MUST pass his Merchant Track ID in this parameter. Track Id passed here should be 
	from merchant backend system like database and not from customer browser. */
	String strtrackid = "<trackid>" + MTrackid + "</trackid>"; //Highly Recommended for Merchant Reference.

	/* User Defined Fields as per Merchant or bank requirement. Merchant MUST ensure  
	he is not passing junk values OR CRLF in any of the UDF. In below sample UDF values 
	are not utilized */
	String strinitudf1 = "<udf1>" + request.getParameter("UDF1") + "</udf1>";
	String strinitudf2 = "<udf2>" + request.getParameter("UDF2") + "</udf2>";
	String strinitudf3 = "<udf3>" + request.getParameter("UDF3") + "</udf3>";
	String strinitudf4 = "<udf4>" + request.getParameter("UDF4") + "</udf4>";
	String strinitudf5 = "<udf5>" + request.getParameter("UDF5") + "</udf5>";
	String dateTxnTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	log.info("for txnid=" + MTrackid + " -------------LOG FOR HDFC TranVbyV.jsp --------------");

	log.info("for txnid=" + MTrackid + " TXNDate   ==>    " + dateTxnTime);
	log.info("for txnid=" + MTrackid + " TranportalID   ==>    " + TranportalID);
	//log.info("strcard        ==>    "+strcard);
	//log.info("strexpmonth    ==>    "+strexpmonth);
	//log.info("strexpyear     ==>    "+strexpyear);
	log.info("for txnid=" + MTrackid + " straction      ==>    " + straction);
	log.info("for txnid=" + MTrackid + " stramt         ==>    " + stramt);
	log.info("for txnid=" + MTrackid + " strcurrency    ==>    " + strcurrency);
	log.info("for txnid=" + MTrackid + " strmember      ==>    " + strmember);
	log.info("for txnid=" + MTrackid + " strtrackid     ==>    " + strtrackid);
	log.info("for txnid=" + MTrackid + " strinitudf1    ==>    " + strinitudf1);
	log.info("for txnid=" + MTrackid + " strinitudf2    ==>    " + strinitudf2);
	log.info("for txnid=" + MTrackid + " strinitudf3    ==>    " + strinitudf3);
	log.info("for txnid=" + MTrackid + " strinitudf4    ==>    " + strinitudf4);
	log.info("for txnid=" + MTrackid + " strinitudf5    ==>    " + strinitudf5);

	/*
	NOTE -
	ME should now do the validations on the amount value set like - 
	a) Transaction Amount should not be blank and should be only numeric
	b) Language should always be USA
	c) Action Code should not be blank
	d) UDF values should not have junk values and CRLF 
	(line terminating parameters)Like--> [ !#$%^&*()+[]\\\';,{}|\":<>?~` ]
	*/

	//************END--PG Initial Request Parameters have to set here**************//
	try {
		//Collecting PARes from ACS
		String PARes = request.getParameter("PaRes");
		//out.println("Read me PARes:"+PARes);
		//Check the PAREs value if it is null then initiate Initial VReq request to PG
		if (PARes == null) {
			String Envreq = null;
			log.info("for txnid=" + MTrackid + " bank id >>>>>>>>>>>" + bankId);
			/* Now merchant sets all the inputs in one string for passing request to Payment Gateway URL */
			bankId="Rupay";
			if (!bankId.equalsIgnoreCase("Rupay")) {
				Envreq = TranportalID + TranportalPwd + strcard + strcvv + strexpyear + strexpmonth + straction + stramt
						+ strcurrency + strmember + strtrackid + strinitudf1 + strinitudf2 + strinitudf3 + strinitudf4
						+ strinitudf5;
				log.info("Value pf return url:::::::::::::s::");
				log.info("Value pf return url:::::::::::s::::");
			} else //For Rupay
			{

				String responseURL = "<merchantResponseUrl>" + PGUtils.getPropertyValue("merchantResponseUrlRupay")
						+ "</merchantResponseUrl>";
				String errorURL = "<merchantErrorUrl>" + PGUtils.getPropertyValue("merchantResponseUrlRupay")
						+ "</merchantErrorUrl>";

						
						log.info("Value pf return url:::::::::::::::"+errorURL);
						log.info("Value pf return url:::::::::::::::"+responseURL);

				//String responseURL = PGUtils.getPropertyValue("merchantResponseUrlRupay");//"<merchantResponseUrl>http://localhost:8080/payment/rupay/Redirect.jsp</merchantResponseUrl>";
				//String errorURL = PGUtils.getPropertyValue("merchantResponseUrlRupay");//""<merchantErrorUrl>http://localhost:8080/payment/rupay/StatusTRAN.jsp</merchantErrorUrl>";
String urs="http://localhost:8080/PayVAS/rupay/Redirect.jsp";
				Envreq = TranportalID + TranportalPwd + strcard + strcvv + strexpyear + strexpmonth + straction + stramt
						+ strcurrency + strmember + strtrackid + strinitudf1 + strinitudf2 + strinitudf3 + strinitudf4
						+ strinitudf5 + responseURL + errorURL;
				// log.info("Envreq >>>>>>>>  "+Envreq);
			}
//			log.info(Envreq);

			/* Payment Gateway URL for sending initial request - Card Enrollment Verification Request */
			/* This is test environment URL,production URL will be different and will be shared by Bank during production movement */

			String sURL = PGUtils.getPropertyValue("mpiVerifyEnrollmentUrlHDFC");
			String InitialResponse = getTransVBYVS2SCall(sURL, Envreq);

			log.info("for txnid=" + MTrackid + "  InitialResponse>>>" + InitialResponse);

			//Collects Any error in initial Request using GetTextBetweenTags() function
			String strError = GetTextBetweenTags(InitialResponse, "<error_text>", "</error_text>");
			//Result response recieved from PG is stored in variable
			String strEnrollmentResult = GetTextBetweenTags(InitialResponse, "<result>", "</result>");

			/***********ECI Value Logic Start Here(Response does not has ECI value)***********/
			//check whether ECI is present in the response received from Payment Gateway
			//If response does not has ECI value than pass it as "7" 
			String strECIValue = GetTextBetweenTags(InitialResponse, "<eci>", "</eci>");
			if (strECIValue.equals("")) {
				strECIValue = "7";
			}
			String strECIValueTag = "<eci>" + strECIValue + "</eci>";

			log.info("for txnid=" + MTrackid + " ============== Response Log ============== ");
			log.info("for txnid=" + MTrackid + " strError  	      ==>    " + strError);
			log.info("for txnid=" + MTrackid + " strEnrollmentResult   ==>    " + strEnrollmentResult);
			log.info("for txnid=" + MTrackid + " strECIValue  	      ==>    " + strECIValue);
			log.info("for txnid=" + MTrackid + " strECIValueTag 	      ==>    " + strECIValueTag);

			/***********ECI Value Logic End Here(Response does not has ECI value)***********/

			//Below Condition Checks Any Error Present or not in the Initial Request
			if (strError.equals("")) {
				//Now checking PG Result parameter 
				if (strEnrollmentResult.equals("ENROLLED")) {
					/*************Enrolled card condition starts here************/
					String acsurl = GetTextBetweenTags(InitialResponse, "<url>", "</url>");//Collects ACS url
					String PAReq = GetTextBetweenTags(InitialResponse, "<PAReq>", "</PAReq>");//Collects PAReq
					String paymentid = GetTextBetweenTags(InitialResponse, "<paymentid>", "</paymentid>");//Collects paymentid

					log.info("for txnid=" + MTrackid + " acsurl    	==>   " + acsurl);
					log.info("for txnid=" + MTrackid + " PAReq  		==>   " + PAReq);
					log.info("for txnid=" + MTrackid + " paymentid 	==>   " + paymentid);
					TM = dm.getTxnMaster(MTrackid);
					TM.setServiceAuthId(paymentid);
					TM.setRespStatus("5");
					dm.updateTxn(TM);
%>
<HTML>
<BODY OnLoad="OnLoadEvent();">
	<form name="form1" action="<%=acsurl%>" method="post">
		<input type="hidden" name="PaReq" value="<%=PAReq%>"> <input
			type="hidden" name="MD" value="<%=paymentid%>">
		<%
		String termURL = PGUtils.getPropertyValue("termUrlHDFC");//"http://139.59.1.254:8080/payone/hdfc/TranVbyV.jsp";
		%>
		<input type="hidden" name="TermUrl" value="<%=termURL%>">
	</form>
	<script language="JavaScript">
		function OnLoadEvent() {
			// alert and script is testing in TestAlert.html 
			document.form1.submit();

		}
	</script>
</BODY>
</HTML>
<%
/*************Enrolled card condition Ends here************/
} else if (strEnrollmentResult.equals("NOT ENROLLED")) {

/*************NOT ENROLLED card condition Starts here************/

String strZIP = "<zip></zip>"; //Optinal,LEAVE field BLANK
String strADDR = "<addr></addr>"; //Optinal,LEAVE field BLANK
log.info(" for txnid=" + MTrackid + " strZIP   	==>    " + strZIP);
log.info(" for txnid=" + MTrackid + " strADDR   	==>    " + strADDR);

/* Now merchant sets all the inputs in one string for passing request to the Payment Gateway URL */
String NEAuthReq = TranportalID + TranportalPwd + strcard + strcvv + strexpyear + strexpmonth + straction + stramt
		+ strcurrency + strmember + strtrackid + strinitudf1 + strinitudf2 + strinitudf3 + strinitudf4 + strinitudf5
		+ strZIP + strADDR + strECIValueTag;

/* Below URL is used only when NOT ENROLLED response is received from Payment Gateway */
/* This is test environment URL,production URL will be different and will be shared by Bank during production movement */

String sURL2 = PGUtils.getPropertyValue("tranPortalUrlHDFC");
String NEAuthResponse = getTransVBYVS2SCall(sURL2, NEAuthReq);
log.info("for txnid=" + MTrackid + "  NEAuthResponse>>>" + NEAuthResponse);

//Collect All Authorization RESULT for NOT ENROLLED CASE																		
String NEResResult = GetTextBetweenTags(NEAuthResponse, "<result>", "</result>");//It will give  Result 
String NEResAmount = GetTextBetweenTags(NEAuthResponse, "<amt>", "</amt>");//It will give Amount
String NEResTrackId = GetTextBetweenTags(NEAuthResponse, "<trackid>", "</trackid>");//It will give TrackID 
String NEResPayid = GetTextBetweenTags(NEAuthResponse, "<payid>", "</payid>");//It will give PaymentID
String NEResRef = GetTextBetweenTags(NEAuthResponse, "<ref>", "</ref>");//It will give Reference NO
String NEResTranid = GetTextBetweenTags(NEAuthResponse, "<tranid>", "</tranid>");//It will give Transaction ID
// Bharat add one new flag
String NEResAuthRespCode = GetTextBetweenTags(NEAuthResponse, "<authRespCode>", "</authRespCode>");//It will give Transaction ID

log.info("for txnid=" + MTrackid + " NEResResult   	 ==>    " + NEResResult);
log.info("for txnid=" + MTrackid + " NEResAmount   	 ==>    " + NEResAmount);
log.info("for txnid=" + MTrackid + " NEResTrackId   	 ==>    " + NEResTrackId);
log.info("for txnid=" + MTrackid + " NEResPayid   	 ==>    " + NEResPayid);
log.info("for txnid=" + MTrackid + " NEResRef  		 ==>    " + NEResRef);
log.info("for txnid=" + MTrackid + " NEResTranid   	 ==>    " + NEResTranid);
log.info("for txnid=" + MTrackid + " NEResAuthRespCode   	 ==>    " + NEResAuthRespCode +" Parse ByBharat");
		
//MERCHANT CAN GET ALL RESULT PARAMETERS USING BELOW CODE ,
//Currently below code is commented,merchant can uncomment it and use the below parameters if required.
/*
String NEResAutht=GetTextBetweenTags(NEAuthResponse,"<auth>","</auth>");//It will give Authorization code
String NEResAvr=GetTextBetweenTags(NEAuthResponse,"<avr>","</avr>");//It will give AVR 
String NEResPostdate=GetTextBetweenTags(NEAuthResponse,"<postdate>","</postdate>");//It will give  postdate
String NEResUdf1=GetTextBetweenTags(NEAuthResponse,"<udf1>","</udf1>");//It will give udf1
String NEResUdf2=GetTextBetweenTags(NEAuthResponse,"<udf2>","</udf2>");//It will give udf2
String NEResUdf3=GetTextBetweenTags(NEAuthResponse,"<udf3>","</udf3>");//It will give udf3
String NEResUdf4=GetTextBetweenTags(NEAuthResponse,"<udf4>","</udf4>");//It will give udf4
String NEResUdf5=GetTextBetweenTags(NEAuthResponse,"<udf5>","</udf5>");//It will give udf5
*/
/*	
	IMPORTANT NOTE - MERCHANT DOES RESPONSE HANDLING AND VALIDATIONS OF 
	TRACK ID, AMOUNT AT THIS PLACE. THEN ONLY MERCHANT SHOULD UPDATE 
	TRANACTION PAYMENT STATUS IN MERCHANT DATABASE AT THIS POSITION 
	AND THEN REDIRECT CUSTOMER ON RESULT PAGE
*/
/* !!IMPORTANT INFORMATION!!
	During redirection, MERCHANT can pass the values as per MERCHANT requirement.
	NOTE: NO PROCESSING should be done on the RESULT PAGE basis of values passed in the RESULT PAGE from this page. 
	MERCHANT does all validations on this page and then redirects the customer to RESULT 
	PAGE ONLY FOR RECEIPT PRESENTATION/TRANSACTION STATUS CONFIRMATION
*/
/* If merchant wants, he can display All results in current Page itself or
he wants to redirect customer to Result/Display page ,then merchant need to make 
sure that correct values should be displayed on Result/display page form 
secure channel like DataBase after all response validations only.
*/

//	out.println(NEResResult);
/*	Redirecting to Final Status Page with required parameters	*/

log.info("TranVbyV.jsp ::: for txnid=" + MTrackid + " NOT ENROLLED Response :: NEResResult : " + NEResResult);

// if (!NEResResult.startsWith("!ERROR!-"))
//	NEResResult = "!ERROR!-" + NEResResult + "-";

String spErrorCode = NEResAuthRespCode; // "";

if(NEResResult.equalsIgnoreCase("NOT CAPTURED") && NEResAuthRespCode!="00" && NEResAuthRespCode!="")
	spErrorCode= NEResAuthRespCode;

else if (NEResResult.equalsIgnoreCase("NOT ENROLLED"))
	spErrorCode = "NE0001";
else
	spErrorCode = NEResResult.substring(NEResResult.indexOf("-") + 1, NEResResult.lastIndexOf("-"));

JSONObject errorCodeJSON = DataManager.getErrorCode(spErrorCode, PGUtils.getPropertyValue("spIdHDFC"));

String errorCode = errorCodeJSON.getString("aggErrorCode");
String message = errorCodeJSON.getString("aggErrorDesc");

String responseCode = "F";
//String message = strEnrollmentResult;
String resp_date_Time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

if (NEResTrackId != null && NEResTrackId != "")
	TM = dm.getTxnMaster(NEResTrackId);
else
	TM = dm.getTransMasterbyAuthID(NEResPayid);

if (!TM.getRespStatus().equalsIgnoreCase("1")) {
	TM.setTransStatus(responseCode);
	TM.setServiceRRN(NEResRef);
	TM.setServiceTxnId(NEResTranid);
	TM.setServiceAuthId(NEResPayid);
	TM.setRespStatus("1");
	TM.setErrorCode(errorCode);
	TM.setSpErrorCode(spErrorCode);
	TM.setRespMessage(message);
	TM.setRespDateTime(resp_date_Time);
	TM.setModified_By("HDFCRespHandler");
	TM.setModified_On(resp_date_Time);
	dm.updateTxn(TM);

	// response.sendRedirect(PGUtils.getPropertyValue("respUrlHDFC")+"?ResTrackId="+NEResTrackId); // comment by bharat for testing                  //+"&ResPaymentId="+NEResPayid+"&ResRef="+NEResRef+"&ResTranId="+NEResTranid+"&ResAmount="+NEResAmount);

	request.setAttribute("ResTrackId", NEResTrackId);
	request.getRequestDispatcher("/respHandlerHDFC").include(request, response);

} else {
	log.info("TranVbyV.jsp ::: for txnid=" + MTrackid
			+ " Transaction is already Processed.Response already updated for Txn Id '" + TM.getId() + "'.");
	request.setAttribute("errorMsg",
			"Error 10054 : Transaction is already Processed.Response already updated for Txn Id '" + TM.getId() + "'.");
	request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
}
/*************NOT ENROLLED card condition Ends here************/
}

// changes 26-04-2019 Rupay Card Change on else If block

else if (strEnrollmentResult.equals("INITIALIZED")) {
String paymentPage = GetTextBetweenTags(InitialResponse, "<url>", "</url>");
String paymentid = GetTextBetweenTags(InitialResponse, "<paymentid>", "</paymentid>");

log.info("TransVbyV.jsp : for txnid=" + MTrackid + "  paymentPage : " + paymentPage);
log.info("TransVbyV.jsp : for txnid=" + MTrackid + " paymentid : " + paymentid);
%>
<HTML>
<BODY OnLoad="OnLoadEvent();">
	<form name="form2" action="<%=paymentPage%>" method="post">
		<input type="hidden" name="PaymentID" value="<%=paymentid%>">
	</form>
	<script language="JavaScript">
		function OnLoadEvent() {
			document.form2.submit();
		}
	</script>
</BODY>
</HTML>
<%
}

else {
/*
	IMPORTANT NOTE - MERCHANT SHOULD UPDATE 
	TRANACTION PAYMENT STATUS IN MERCHANT DATABASE AT THIS POSITION 
	AND THEN REDIRECT CUSTOMER ON RESULT PAGE
*/
/* If merchant wants, he can display All results in current Page itself or
	he wants to redirect customer to Result/Display page ,then merchant need to make 
	sure that correct values should be displayed on Result/display page form 
	secure channel like DataBase after all response validations only.
*/
//Below code will display/Show result other than ENROLLED & NOT ENROLLED
log.info("TranVbyV.jsp ::: for txnid=" + MTrackid
    + " Response is neither ENROLLED nor NOT ENROLLED :: strEnrollmentResult : " + InitialResponse);
String Othrespaymentid = GetTextBetweenTags(InitialResponse, "<paymentid>", "</paymentid>");//It will give payid
String Othrestrackid = GetTextBetweenTags(InitialResponse, "<trackid>", "</trackid>");//It will give TrackID 

log.info("for txnid=" + MTrackid + " Othrespaymentid    ==>   " + Othrespaymentid);
log.info("for txnid=" + MTrackid + " Othrestrackid      ==>   " + Othrestrackid);

log.info("for txnid=" + MTrackid + " strEnrollmentResult : " + strEnrollmentResult);

if (!strEnrollmentResult.startsWith("!ERROR!-"))
  strEnrollmentResult = "!ERROR!-" + strEnrollmentResult;

//String spErrorCode = PGUtils.GetTextBetweenTags(strEnrollmentResult, "-", "-");
String spErrorCode = strEnrollmentResult.substring(strEnrollmentResult.indexOf("-") + 1,
    strEnrollmentResult.lastIndexOf("-"));
JSONObject errorCodeJSON = DataManager.getErrorCode(spErrorCode, PGUtils.getPropertyValue("spIdHDFC"));

String errorCode = errorCodeJSON.getString("aggErrorCode");
String message = errorCodeJSON.getString("aggErrorDesc");

String responseCode = "F";
//String message = strEnrollmentResult;
String resp_date_Time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

if (Othrestrackid != null && Othrestrackid != "")
  TM = dm.getTxnMaster(Othrestrackid);
else if (Othrespaymentid != null && Othrespaymentid != "")
  TM = dm.getTransMasterbyAuthID(Othrespaymentid);
else
  TM = dm.getTransMasterbyAuthID(MTrackid);

if (!TM.getRespStatus().equalsIgnoreCase("1")) {
  TM.setTransStatus(responseCode);
  TM.setServiceRRN(Othrespaymentid);
  TM.setServiceTxnId(Othrespaymentid);
  TM.setServiceAuthId(Othrespaymentid);
  TM.setRespStatus("1");
  TM.setErrorCode(errorCode);
  TM.setSpErrorCode(spErrorCode);
  TM.setRespMessage(message);
  TM.setRespDateTime(resp_date_Time);
  TM.setModified_By("HDFCRespHandler");
  TM.setModified_On(resp_date_Time);
  dm.updateTxn(TM);

  /*Redirecting to Final Status Page with required parameters*/

  // response.sendRedirect(PGUtils.getPropertyValue("respUrlHDFC")+"?ResTrackId="+Othrestrackid);  // comment by bharat for testing   //+"&ResPaymentId="+Othrespaymentid);

  request.setAttribute("ResTrackId", Othrestrackid);
  request.getRequestDispatcher("/respHandlerHDFC").include(request, response);

} else {
  log.info("TranVbyV.jsp ::: for txnid=" + MTrackid
  + " Transaction is already Processed.Response already updated for Txn Id '" + TM.getId()
  + "'.");
  request.setAttribute("errorMsg",
  "Error 10054 : Transaction is already Processed.Response already updated for Txn Id '"
      + TM.getId() + "'.");
  request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
}
}
log.info(" for txnid=" + MTrackid + " ============== Response Log  ============== ");
} else {
/*
ERROR IN TRANSACTION REQUEST PROCESSING
IMPORTANT NOTE - MERCHANT SHOULD UPDATE 
TRANACTION PAYMENT STATUS IN MERCHANT DATABASE AT THIS POSITION 
AND THEN REDIRECT CUSTOMER ON RESULT PAGE
*/
/*	If merchant wants, he can display All results in current Page itself or
	he wants to redirect customer to Result/Display page ,then merchant need to make 
	sure that correct values should be displayed on Result/display page form 
	secure channel like DataBase after all response validations only.
*/
//Below code will display/Show Any Error if comes from PG
//out.println(strError);
/*Redirecting to Final Status Page with required parameters along with Error*/
log.info("TranVbyV.jsp ::: for txnid=" + MTrackid
    + " ERROR IN INITIAL REQUEST PROCESSING :: strError : " + strError);

if (!strError.startsWith("!ERROR!-"))
strError = "!ERROR!-" + strError;

//String spErrorCode = PGUtils.GetTextBetweenTags(strError, "-", "-");

String spErrorCode = strError.substring(strError.indexOf("-") + 1, strError.lastIndexOf("-"));

log.info("TranVbyV.jsp :::for txnid=" + MTrackid + "  SP Error Code :: spErrorCode : " + spErrorCode);

JSONObject errorCodeJSON =
    DataManager.getErrorCode(spErrorCode, PGUtils.getPropertyValue("spIdHDFC"));

String errorCode = errorCodeJSON.getString("aggErrorCode");
String message = errorCodeJSON.getString("aggErrorDesc");

String responseCode = "F";

String resp_date_Time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
TM = dm.getTxnMaster(MTrackid);

if (!TM.getRespStatus().equalsIgnoreCase("1")) {
TM.setTransStatus(responseCode);
TM.setServiceRRN("NA");
TM.setServiceTxnId("NA");
TM.setServiceAuthId("NA");
TM.setRespStatus("1");
TM.setErrorCode(errorCode);
TM.setSpErrorCode(spErrorCode);
TM.setRespMessage(message);
TM.setRespDateTime(resp_date_Time);
TM.setModified_By("HDFCRespHandler");
TM.setModified_On(resp_date_Time);
dm.updateTxn(TM);
// response.sendRedirect(PGUtils.getPropertyValue("respUrlHDFC")+"?ResTrackId="+MTrackid);   // comment by bharat for testing

request.setAttribute("ResTrackId", MTrackid);
request.getRequestDispatcher("/respHandlerHDFC").include(request, response);

} else {
log.info("TranVbyV.jsp ::: for txnid=" + MTrackid
    + " Transaction is already Processed.Response already updated for Txn Id '" + TM.getId()
    + "'.");
request.setAttribute("errorMsg",
    "Error 10054 : Transaction is already Processed.Response already updated for Txn Id '"
    + TM.getId() + "'.");
request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
}
}
} else {
//*******If PARES is not NULL means once response from ACS is received then..condition starts here*****/

//Collect paymentid recieved from ACS
String PaymentIDMD = "<paymentid>" + request.getParameter("MD") + "</paymentid>";
log.info("for authid=" + PaymentIDMD
    + "  ============== Response Log PARes != Null Then ============== ");

//Collect PaRes recieved from ACS
PARes = "<PARes>" + request.getParameter("PaRes") + "</PARes>";

log.info(" PaymentIDMD  	==>   " + PaymentIDMD);
log.info("for authid=" + PaymentIDMD + " PARes          ==>   " + PARes);
//Creating Payers Authentication Request to PG
String EnrollAuth = TranportalID + TranportalPwd + PaymentIDMD + PARes;

/* Below URL is used to sent PARES and Payment ID to Payment Gateway once the response is received from Bank ACS */
/* This is test environment URL,production URL will be different and will be shared by Bank during production movement */

String sURL3 = PGUtils.getPropertyValue("mpiPayerAuthenticationUrlHDFC");
String ENMPIAuthResponse = getTransVBYVS2SCall(sURL3, EnrollAuth);
log.info("for authid=" + PaymentIDMD + " EnMPIReserror     ==>   " + ENMPIAuthResponse);

//Check Any Response Recived Or Not
if (ENMPIAuthResponse.length() > 0) {
log.info("for authid=" + PaymentIDMD + " ENMPIAuthResponse     ==>   " + ENMPIAuthResponse);

//Collect all The Authorization Response or Result Parameters using GetTextBetweenTags() Function
String EnMPIReserror = GetTextBetweenTags(ENMPIAuthResponse, "<error_text>", "</error_text>");//It will give  error_text 
String EnMPIResResult = GetTextBetweenTags(ENMPIAuthResponse, "<result>", "</result>");//It will give  Result 
String EnMPIResTrackId = GetTextBetweenTags(ENMPIAuthResponse, "<trackid>", "</trackid>");//It will give TrackID 
String EnMPIResPayid = GetTextBetweenTags(ENMPIAuthResponse, "<paymentid>", "</paymentid>");//It will give PaymentID
String EnMPIResRef = GetTextBetweenTags(ENMPIAuthResponse, "<ref>", "</ref>");//It will give Reference NO.
String EnMPIResTranid = GetTextBetweenTags(ENMPIAuthResponse, "<tranid>", "</tranid>");//It will give Transaction ID

log.info("for authid=" + PaymentIDMD + " EnMPIReserror     ==>   " + EnMPIReserror);
log.info("for authid=" + PaymentIDMD + " EnMPIResResult    ==>   " + EnMPIResResult);
log.info("for authid=" + PaymentIDMD + " EnMPIResTrackId   ==>   " + EnMPIResTrackId);
log.info("for authid=" + PaymentIDMD + " EnMPIResPayid     ==>   " + EnMPIResPayid);
log.info("for authid=" + PaymentIDMD + " EnMPIResRef   	  ==>   " + EnMPIResRef);
log.info("for authid=" + PaymentIDMD + " EnMPIResTranid    ==>   " + EnMPIResTranid);

//MERCHANT CAN GET ALL Authorization RESULT PARAMETERS USING BELOW CODE 
//Currently below code is commented,,merchant can uncomment it and use the below parameters if required.
/*
String EnMPIResAutht=GetTextBetweenTags(ENMPIAuthResponse,"<auth>","</auth>");//It will give Authorization Code 
String EnMPIResAvr=GetTextBetweenTags(ENMPIAuthResponse,"<avr>","</avr>");//It will give AVR 
String EnMPIResPostdate=GetTextBetweenTags(ENMPIAuthResponse,"<postdate>","</postdate>");//It will give  postdate
String EnMPIResUdf1=GetTextBetweenTags(ENMPIAuthResponse,"<udf1>","</udf1>");//It will give udf1
String EnMPIResUdf2=GetTextBetweenTags(ENMPIAuthResponse,"<udf2>","</udf2>");//It will give udf2
String EnMPIResUdf3=GetTextBetweenTags(ENMPIAuthResponse,"<udf3>","</udf3>");//It will give udf3
String EnMPIResUdf4=GetTextBetweenTags(ENMPIAuthResponse,"<udf4>","</udf4>");//It will give udf4
String EnMPIResUdf5=GetTextBetweenTags(ENMPIAuthResponse,"<udf5>","</udf5>");//It will give udf5
*/
/*	
	IMPORTANT NOTE - MERCHANT DOES RESPONSE HANDLING AND VALIDATIONS OF 
	TRACK ID, AMOUNT AT THIS PLACE. THEN ONLY MERCHANT SHOULD UPDATE 
	TRANACTION PAYMENT STATUS IN MERCHANT DATABASE AT THIS POSITION 
	AND THEN REDIRECT CUSTOMER ON RESULT PAGE
*/
/* !!IMPORTANT INFORMATION!!
	During redirection, MERCHANT can pass the values as per MERCHANT requirement.
	NOTE: NO PROCESSING should be done on the RESULT PAGE basis of values passed in the RESULT PAGE from this page. 
	MERCHANT does all validations on this page and then redirects the customer to RESULT 
	PAGE ONLY FOR RECEIPT PRESENTATION/TRANSACTION STATUS CONFIRMATION
*/
/* If merchant wants, he can display All results in current Page itself or
he wants to redirect customer to Result/Display page ,then merchant need to make 
sure that correct values should be displayed on Result/display page form 
secure channel like DataBase after all response validations only.
*/
//out.println(EnMPIResResult);
/*Redirecting to Final Status Page with required parameters*/
log.info("TranVbyV.jsp :: Test :: txn_id : EnMPIResTrackId  " + EnMPIResTrackId
    + " , featch the data form database");


if (EnMPIResTrackId != null && EnMPIResTrackId != "") {

TM = dm.getTxnMaster(EnMPIResTrackId);
} else {
TM = dm.getTransMasterbyAuthID(EnMPIResPayid);
}

log.info(new SimpleDateFormat().format(new Date()) + " for txnid = " + EnMPIResTrackId
    + " TM.getRespStatus >>>>>>>>>>>> +++++++++++ " + TM.getRespStatus());

if (null == EnMPIReserror || EnMPIReserror == "") {
EnMPIReserror = "NA";
log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "for txnid = "
    + EnMPIResTrackId + " EnMPIReserror ---------------------------------- " + EnMPIReserror);
}

if (EnMPIReserror.equalsIgnoreCase("!ERROR!-CM90004-Duplicate Record")) {

log.info("for txnid = " + EnMPIResTrackId + " response not send or updated for error="
    + EnMPIReserror + " for service_auth_id=" + EnMPIResPayid);
} else {

if (!TM.getRespStatus().equalsIgnoreCase("1")) {
  //Code to verify transaction 

  //Double addedAmt = Double.parseDouble(TM.getAmount())+ Double.parseDouble(TM.getSurcharge());


  //BigDecimal addedAmt=new BigDecimal(TM.getAmount()).add(new BigDecimal(TM.getSurcharge()));
  //DataManager dataManager = new DataManager();
  //List<String> surchargeData = dataManager.getCalculateSurcharge(TM.getBankId(),TM.getInstrumentId(), TM.getAmount(), TM.getMerchantId(), "NA", TM.getId());
  //int payout = Integer.valueOf(surchargeData.get(3));

  BigDecimal addedAmt = new BigDecimal(TM.getAmount());
  BigDecimal dec1 = new BigDecimal(0);//TM.getSurcharge());
  
  if(TM.getSurcharge() != null)
	  dec1 = new BigDecimal(TM.getSurcharge());
  
  //if (payout == 1) {
    addedAmt = addedAmt.add(dec1);
  //}

  log.info("change for bigdecimal=" + TM.getAmount() + " " + TM.getSurcharge() + " " + addedAmt);

  /* Now merchant sets all the inputs in one string for passing request to the Payment Gateway URL for Verification*/
  /* String enqReq = TranportalID + TranportalPwd + "<action>8</action>" + "<transid>"
  		+ EnMPIResTranid + "</transid>" + "<trackid>" + EnMPIResTrackId + "</trackid>"
  		+ "<amt>" + addedAmt + "</amt>" + strcurrency
  		+ "<member>Enquiry Name</member><udf5>TransID</udf5>"; */
  String enqReq = null;

  if ((EnMPIReserror != null || EnMPIReserror != "")
  && EnMPIReserror.equalsIgnoreCase("!ERROR!-CM90004-Duplicate Record")) {
    enqReq = TranportalID + TranportalPwd + "<action>8</action>" + "<transid>" + EnMPIResTranid
    + "</transid>" + "<trackid>" + EnMPIResTrackId + "</trackid>" + "<amt>" + addedAmt
    + "</amt>" + strcurrency + "<member>Enquiry Name</member><udf5>TrackID</udf5>";
  } else {
    //when user refresh page twice and we get !ERROR!-CM90004-Duplicate Record then in this case we requery and get ststis back
    enqReq = TranportalID + TranportalPwd + "<action>8</action>" + "<transid>" + EnMPIResTranid
    + "</transid>" + "<trackid>" + EnMPIResTrackId + "</trackid>" + "<amt>" + addedAmt
    + "</amt>" + strcurrency + "<member>Enquiry Name</member><udf5>TransID</udf5>";
  }

  log.info("for txnid = " + EnMPIResTrackId + " enqRequest   	 ==>    " + enqReq);

  /* Below URL is used only when we have to verify the response received from Payment Gateway*/
  /* This is test environment URL,production URL will be different and will be shared by Bank during production movement */

  String sURL4 = PGUtils.getPropertyValue("tranPortalUrlHDFC");
  String enqResponse = getTransVBYVS2SCall(sURL4, enqReq);
  log.info("for txnid = " + EnMPIResTrackId + " enqResponse   	 ==>    " + enqResponse);

  //Collect All Authorization RESULT for NOT ENROLLED CASE																		
  String enqResResult = GetTextBetweenTags(enqResponse, "<result>", "</result>");//It will give  Result 
  String enqAuthRespCode = GetTextBetweenTags(enqResponse, "<authRespCode>", "</authRespCode>");//It will give  Result
  /* String NEResAmount = GetTextBetweenTags(enqResponse, "<amt>", "</amt>");//It will give Amount
  String NEResTrackId=GetTextBetweenTags(enqResponse, "<trackid>", "</trackid>");//It will give TrackID 
  String NEResPayid=GetTextBetweenTags(enqResponse,"<payid>","</payid>");//It will give PaymentID
  String NEResRef=GetTextBetweenTags(enqResponse,"<ref>","</ref>");//It will give Reference NO
  String NEResTranid=GetTextBetweenTags(enqResponse,"<tranid>","</tranid>");//It will give Transaction ID */

  log.info("for txnid = " + EnMPIResTrackId
  + " TranVbyV.jsp ::: Verification/Enquiry Response Result :: enqResResult : " + enqResResult);
  /* log.info("NEResAmount   	 ==>    "+NEResAmount);
  log.info("NEResTrackId   	 ==>    "+NEResTrackId);
  log.info("NEResPayid   	 ==>    "+NEResPayid);
  log.info("NEResRef  		 ==>    "+NEResRef);
  log.info("NEResTranid   	 ==>    "+NEResTranid); */

  String responseCode = EnMPIResResult;
  String message = EnMPIResResult;
  String errorCode = "NA";
  String spErrorCode = "NA";

  if (responseCode.equalsIgnoreCase("CAPTURED") && enqResResult.equalsIgnoreCase("SUCCESS")) {
    log.info("for txnid = " + EnMPIResTrackId
    + " respHandlerHDFC ::: ===================Success =====================");
    responseCode = "Ok";
    errorCode = "00000";
    message = "Transaction Successful.";
  } 
  
  
  else if (!responseCode.equalsIgnoreCase("CAPTURED")) {
    log.info("for txnid = " + EnMPIResTrackId + " TranVbyV.jsp ::: NOT CAPTURED :: EnMPIResResult : "+ responseCode + " :: EnMPIReserror : " + EnMPIReserror);

    if (!EnMPIReserror.startsWith("!ERROR!-"))
  		EnMPIReserror = "!ERROR!-" + EnMPIReserror + "-";

    if (EnMPIReserror.equalsIgnoreCase("NOT CAPTURED") || EnMPIResResult.equalsIgnoreCase("NOT CAPTURED"))
        spErrorCode = enqAuthRespCode;    // "NE0002";
    //String spErrorCode = PGUtils.GetTextBetweenTags(NEResResult, "-", "-");
    else
  spErrorCode = EnMPIReserror.substring(EnMPIReserror.indexOf("-") + 1, EnMPIReserror.lastIndexOf("-"));

    JSONObject errorCodeJSON = DataManager.getErrorCode(spErrorCode, PGUtils.getPropertyValue("spIdHDFC"));

    errorCode = errorCodeJSON.getString("aggErrorCode");
    message = errorCodeJSON.getString("aggErrorDesc");

    responseCode = "F";
    //message = EnMPIReserror;
  }
  
  else if (!enqResResult.equalsIgnoreCase("SUCCESS")) {
    log.info("for txnid = " + EnMPIResTrackId + " TranVbyV.jsp ::: ERROR while fetching Enquiry Response :: enqResResult : " + enqResResult);

    /* if(!enqResResult.startsWith("!ERROR!-"))
    	enqResResult = "!ERROR!-"+enqResResult;						
    
    spErrorCode = enqResResult.substring(enqResResult.indexOf("-")+1, enqResResult.lastIndexOf("-"));																
    
    JSONObject errorCodeJSON =  DataManager.getErrorCode(spErrorCode, PGUtils.getPropertyValue("spIdHDFC")); */

    errorCode = "E0068";
    message = "Transaction Verification Failed." + enqResResult;

    responseCode = "F";
    //message = EnMPIReserror;
  }

  String resp_date_Time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

  TM.setTransStatus(responseCode);
  TM.setServiceRRN(EnMPIResRef);
  TM.setServiceTxnId(EnMPIResTranid);
  TM.setServiceAuthId(EnMPIResPayid);
  TM.setRespStatus("1");
  TM.setErrorCode(errorCode);
  TM.setSpErrorCode(spErrorCode);
  TM.setRespMessage(message);
  TM.setRespDateTime(resp_date_Time);
  TM.setModified_By("HDFCRespHandler");
  TM.setModified_On(resp_date_Time);
  dm.updateTxn(TM);

  // HttpSession session = request.getSession(false);
  // session.setAttribute("ResTrackId", EnMPIResTrackId);

  request.setAttribute("ResTrackId", EnMPIResTrackId);
  // http://localhost:8080/payment
  request.getRequestDispatcher("/respHandlerHDFC").include(request, response);

  // response.sendRedirect(PGUtils.getPropertyValue("respUrlHDFC")+"?ResTrackId="+EnMPIResTrackId);   // comment by bharat for testing  //+"&ResPaymentId="+EnMPIResPayid+"&ResRef="+EnMPIResRef+"&ResTranId="+EnMPIResTranid);

} else {
  log.info("TranVbyV.jsp ::: for txnid = " + EnMPIResTrackId
  + " Transaction is already Processed.Response already updated for Txn Id '" + TM.getId()
  + "'.");
  request.setAttribute("errorMsg",
  "Error 10054 : Transaction is already Processed.Response already updated for Txn Id '"
      + TM.getId() + "'.");
  request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
}

}
} else {
/*
	IMPORTANT NOTE - MERCHANT SHOULD UPDATE 
	TRANACTION PAYMENT STATUS IN MERCHANT DATABASE AT THIS POSITION 
	AND THEN REDIRECT CUSTOMER ON RESULT PAGE
*/
/* If merchant wants, he can display All results in current Page itself or
		he wants to redirect customer to Result/Display page ,then merchant need to make 
		sure that correct values should be displayed on Result/display page form 
		secure channel like DataBase after all response validations only.
		*/
//Below code will display/Show Message if No Response Received
//out.println("No Response");
/*Redirecting to Final Status Page with required parameters*/

String responseCode = "F";
String message = "No Response Received";
String resp_date_Time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


if (MTrackid != null && MTrackid != "")
TM = dm.getTxnMaster(MTrackid);
else
TM = dm.getTransMasterbyAuthID(PaymentIDMD);

if (!TM.getRespStatus().equalsIgnoreCase("1")) {
TM.setTransStatus(responseCode);
TM.setServiceRRN("NA");
TM.setServiceTxnId("NA");
TM.setServiceAuthId("NA");
TM.setRespStatus("1");
//TM.setErrorCode(errorCode);
//TM.setSpErrorCode(spErrorCode);
TM.setRespMessage(message);
TM.setRespDateTime(resp_date_Time);
TM.setModified_By("HDFCRespHandler");
TM.setModified_On(resp_date_Time);
dm.updateTxn(TM);

// response.sendRedirect(PGUtils.getPropertyValue("respUrlHDFC")+"?ResTrackId="+MTrackid);  // comment by bharat for testing

request.setAttribute("ResTrackId", MTrackid);
request.getRequestDispatcher("/respHandlerHDFC").include(request, response);

} else {
log.info("TranVbyV.jsp ::: for txnid = " + TM.getId()
    + " Transaction is already Processed.Response already updated for Txn Id '" + TM.getId()
    + "'.");
request.setAttribute("errorMsg",
    "Error 10054 : Transaction is already Processed.Response already updated for Txn Id '"
    + TM.getId() + "'.");
request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
}
}
/********If PARES is not NULL means once response from ACS is received then..condition ends here*******/
log.info("==============   Response Log End PARes != Null   ==============");
}

log.info("============== LOG END FOR HDFC TranVbyV.jsp ==============");
} catch (Exception e) {
//Merchant Can handle the exception in his/Her own logic/Way
//out.println(e.getMessage());
//Below code will display/Show Any Error if comes from PG
/*Redirecting to Final Status Page with required parameters along with Error*/
TM = dm.getTxnMaster(MTrackid);
log.error("for txnid=" + MTrackid + " TranVbyV.jsp ::: Error Occurred :: ", e);

String responseCode = "F";
String message = "Transaction Failed.Undefined Error.";
String resp_date_Time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

if (!TM.getRespStatus().equalsIgnoreCase("1")) {
TM.setTransStatus(responseCode);
TM.setServiceRRN("NA");
TM.setServiceTxnId("NA");
TM.setServiceAuthId("NA");
TM.setRespStatus("1");
TM.setErrorCode("FFFFF");
TM.setSpErrorCode("NA");
TM.setRespMessage(message);
TM.setRespDateTime(resp_date_Time);
TM.setModified_By("HDFCRespHandler");
TM.setModified_On(resp_date_Time);
dm.updateTxn(TM);
// response.sendRedirect(PGUtils.getPropertyValue("respUrlHDFC")+"?ResTrackId="+MTrackid);  // comment by bharat for testing

request.setAttribute("ResTrackId", MTrackid);
request.getRequestDispatcher("/respHandlerHDFC").include(request, response);

} else {
log.info("TranVbyV.jsp ::: for txnid=" + MTrackid
    + " Transaction is already Processed.Response already updated for Txn Id '" + TM.getId()
    + "'.");
request.setAttribute("errorMsg",
    "Error 10054 : Transaction is already Processed.Response already updated for Txn Id '"
    + TM.getId() + "'.");
request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
}
}
} else {
log.info("TranVbyV.jsp ::: for txnid=" + MTrackid
    + " Transaction is already posted to TransVbyV with status 5 " + TM.getId() + "'.");
request.setAttribute("errorMsg",
    "Error 10060 : Transaction is already Transaction is alreday posted to TransVbyV '" + TM.getId()
    + "'.");
request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
}
%>
<%!//=======This is GetTextBetweenTags function which return the value between two XML tags or two string =====

  static Logger log = LoggerFactory.getLogger("JSPS.TranVbyV.jsp");

  public String GetTextBetweenTags(String InputText, String Tag1, String Tag2) {
    String Result;

    int index1 = InputText.indexOf(Tag1);
    log.info("index1:{}", index1);
    int index2 = InputText.indexOf(Tag2);
    if (index1 != -1 && index2 != -1) {
      index1 = index1 + Tag1.length();
      Result = InputText.substring(index1, index2);
      return Result;
    } else {
      Result = "";
      return Result;
    }

  }

  public static String getTransVBYVS2SCall(String sURL, String Envreq) {

    String InitialResponse = "";
    try {
      URL url = new URL(sURL); 
      URLConnection urlcon = url.openConnection(); //create a SSL connection object server-to-server
      urlcon.setDoInput(true);
      urlcon.setDoOutput(true);
      urlcon.setUseCaches(false);
      

      urlcon.setConnectTimeout(45000);
      urlcon.setReadTimeout(55000);

      urlcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); //defining content type
      try (DataOutputStream dataoutputstream = new DataOutputStream(urlcon.getOutputStream())) {
        dataoutputstream.writeBytes(Envreq);
        dataoutputstream.flush();
        dataoutputstream.close();
      }

      try(BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(urlcon.getInputStream()))){
        String decodedString;
        while ((decodedString = bufferedreader.readLine()) != null) {
          InitialResponse = InitialResponse + decodedString; //response recieved from PG is stored in variable
        }
        bufferedreader.close();
      }
      

      return InitialResponse;
    } catch (Exception e) {
      log.error("Unable to post request: {" + sURL + "}", e);
    } finally {
    }
    return InitialResponse;
  }%>
