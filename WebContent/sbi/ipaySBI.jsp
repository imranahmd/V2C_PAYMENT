<%@page import="com.rew.pg.dto.SbiAquiringDTO"%>
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
<%@ page import="com.fss.plugin.iPayPipe"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%
	response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Expires", "0");
	response.setDateHeader("Expires", -1);
	response.setDateHeader("Last-Modified", 0);
    Logger log = LoggerFactory.getLogger("JSPS.ipaySBI.jsp");

	Enumeration<String> params = request.getParameterNames();
	String paramName = null, paramValue = null;
	while (params.hasMoreElements()) {
		paramName = (String) params.nextElement();
		paramValue = request.getParameter(paramName);
		log.info("ipaySBI Parameter Name - " + paramName);
	}

	String spMid = null, spKey = null;

	String Mid = request.getParameter("Mid");
	String bankId = request.getParameter("bankId");
	String instrumentId = request.getParameter("instrumentId");
	String cardno = request.getParameter("cardno");
	String cvv = request.getParameter("cvv");

	String expmm = request.getParameter("expmm");
	String expyy = request.getParameter("expyy");
	String amt = request.getParameter("amt");
	String MTrackid = request.getParameter("MTrackid"); // our txnId atrnId
	String membername = request.getParameter("membername");
	String spId = request.getParameter("spId");

	log.info("spId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + spId);
	DataManager dm = new DataManager();
	TransactionMaster TM = dm.getTxnMaster(MTrackid);
	if (request.getParameter("tType") != null && request.getParameter("tType").equalsIgnoreCase("DIRECT")) {
		if (cardno != null && cvv != null && !cardno.isEmpty() && !cvv.isEmpty()) {

			String sCardNumber = PGUtils.cryptoJs(request.getParameter("cardno"));
			String[] args = sCardNumber.split("\\|");

			if (args.length > 1 && MTrackid.equals(args[1])) {
				log.info("ipaySBI.jsp ::: There is no tempered ... txn id is match... for id=" + MTrackid);
				cardno = args[0];
				cardno = cardno.replaceAll(" ", "").trim();
			log.info("MTrackid <<<<<<<<>>>>>>>for id=" + MTrackid);

				String binstatus = dm.getBinvalidation(MTrackid, cardno.substring(0, 9), instrumentId);
				log.info("for txnid=" + MTrackid + " binstatus >>>>>>>>>>>>>>>>>> " + binstatus);
			}

			else {

				log.info("ipaySBI.jsp ::: --> Invalid card number  for id=" + MTrackid);
				request.setAttribute("errorMsg", "Error 10050 : Invalid card number.. ");
				request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
				out.flush();
				out.close();
			}

			cvv = PGUtils.cryptoJs(request.getParameter("cvv"));
		}
	}
	String merchantId=TM.getMerchantId();
	session.setAttribute("merchantId", merchantId);
	SbiAquiringDTO sbiaquiringdetails  = dm.getSBIAquiringDetails(merchantId);
	String aliasName=sbiaquiringdetails.getAliasName();
	String resourcePath=sbiaquiringdetails.getKeyPath();

	try {

		log.info("for id=" + MTrackid + " Mid =" + Mid);
		log.info("for id=" + MTrackid + "bankId =" + bankId);
		log.info("for id=" + MTrackid + "instrumentId =" + instrumentId);
		//log.info("for id=" + MTrackid + "cardno =" + cardno);
		//log.info("for id=" + MTrackid + "cvv =" + cvv);
		//log.info("for id=" + MTrackid + "expmm =" + expmm);
		//log.info("for id=" + MTrackid + "membername =" + membername);
		log.info("for id=" + MTrackid + " amt =" + amt);
		log.info("for id=" + MTrackid + "MTrackid =" + MTrackid);
		log.info("for id=" + MTrackid + "spId =" + spId);

		//START SBI API CALL TO CONNECT SBI PAYMENT GATEWAY

		log.info("for id=" + MTrackid + "==========================>>>>>>>>>>>");
		//String resourcePath = PGUtils.getPropertyValue("resourcePath");
		//String keystorePath = PGUtils.getPropertyValue("keystorePath");
		String recieptURL = PGUtils.getPropertyValue("merchantResponseUrlsbiRupay");
		String errorURL = PGUtils.getPropertyValue("merchantResponseUrlsbiRupay");
		String action = PGUtils.getPropertyValue("action");
		//String aliasName = PGUtils.getPropertyValue("aliasName");
		String currency = PGUtils.getPropertyValue("currency");
		String language = PGUtils.getPropertyValue("language");
		String amount = amt;
		String trackid = MTrackid; // unique transaction id			

		// Card type. Mandatory 
		// Value for credit card "C", for debit card "D", for Rupay debit card "RDC" 

		String type = null;

		log.info("TM.getInstrumentId()="+TM.getInstrumentId()+" TM.getBankId()="+TM.getBankId());
		if (TM.getInstrumentId().equalsIgnoreCase("CC")) {
			type = "C";
		} 
		else if(TM.getInstrumentId().equalsIgnoreCase("DC"))
		{
			if (TM.getBankId().equalsIgnoreCase("Rupay"))
				type = "RDC";
			else
				type = "D";
		}


		iPayPipe pipe = new iPayPipe();

		pipe.setResourcePath(resourcePath);
		pipe.setKeystorePath(resourcePath);
		pipe.setAlias(aliasName);
		pipe.setAction(action);
		pipe.setCurrency(currency);
		pipe.setLanguage(language);
		pipe.setAmt(amount);
		pipe.setTrackId(trackid);
		pipe.setCard(cardno);
		pipe.setCvv2(cvv);
		pipe.setExpMonth(expmm);
		pipe.setExpYear(expyy);
		pipe.setMember(membername);
		pipe.setType(type);
		/* pipe.setEnableLogger(true); */

		pipe.setUdf1(null);
		pipe.setUdf2(null);
		pipe.setUdf3(null);
		pipe.setUdf4(null);
		pipe.setUdf5(null);

		//pipe.setKeystorePath(resourcePath);
		pipe.setResponseURL(recieptURL+"?merchantId="+merchantId);
		pipe.setErrorURL(errorURL+"?merchantId="+merchantId);

		//pipe.setWebAddress(webAddress);			

		//START PRINT LOG
		log.info("for id=" + MTrackid + " type = " + type);
		log.info("for id=" + MTrackid + " getResourcePath = " + pipe.getResourcePath());
		log.info("for id=" + MTrackid + " getKeystorePath = " + pipe.getKeystorePath());
		log.info("for id=" + MTrackid + " getAlias = " + pipe.getAlias());
		log.info("for id=" + MTrackid + " getAction = " + pipe.getAction());
		log.info("for id=" + MTrackid + " getCurrency = " + pipe.getCurrency());
		log.info("for id=" + MTrackid + " getLanguage = " + pipe.getLanguage());
		log.info("for id=" + MTrackid + " getAmt = " + pipe.getAmt());
		log.info("for id=" + MTrackid + " getTrackId = " + pipe.getTrackId());
		//log.info("for id=" + MTrackid + " getCard = " + pipe.getCard());
		//log.info("for id=" + MTrackid + " getCvv2 = " + pipe.getCvv2());
		//log.info("for id=" + MTrackid + " getExpMonth = " + pipe.getExpMonth());
		//log.info("for id=" + MTrackid + " getExpYear = " + pipe.getExpYear());
		//log.info("for id=" + MTrackid + " getMember = " + pipe.getMember());
		log.info("for id=" + MTrackid + " getType = " + pipe.getType());
		log.info("getUdf1 = " + pipe.getUdf1());
		log.info("getUdf2 = " + pipe.getUdf2());
		log.info("getUdf3 = " + pipe.getUdf3());
		log.info("getUdf4 = " + pipe.getUdf4());
		log.info("getUdf5 = " + pipe.getUdf5());
		log.info("for id=" + MTrackid + " getKeystorePath = " + pipe.getKeystorePath());
		log.info("for id=" + MTrackid + " getResponseURL = " + pipe.getResponseURL());
		log.info("for id=" + MTrackid + " getErrorURL = " + pipe.getErrorURL());

		//END PRINT LOG

		log.info("Start performVbVTransaction to SBI PG connection for id=" + pipe.getTrackId());
		int result = pipe.performVbVTransaction();
		log.info("After performVbVTransaction PG connection | result = " + result);

		//To redirect the web address. 
		if (result == 0) {
			log.info("for id=" + MTrackid + " pipe.getWebAddress()=" + pipe.getWebAddress());
			StringBuilder sb = new StringBuilder();
			sb.append("<body>");
			sb.append("<form method='post' action='" + pipe.getWebAddress() + "'>");
			sb.append("</form>");
			sb.append("</body>");
			sb.append("<SCRIPT>");
			sb.append("document.forms[0].submit();");
			sb.append("</SCRIPT>");

			out.write(sb.toString());

		} else {

			log.info("problem in redirecting request " + pipe.getError() + " id=" + pipe.getTrackId());

			int status = getTransactionMaster(TM, dm, pipe.getError());

			log.info("status=====>" + status);
			if (status == 0) {
				request.setAttribute("ResTrackId", MTrackid);
				request.getRequestDispatcher("/respHandlersbi").include(request, response);
			} else {
				log.info(
						"ipaySBI.jsp ::: Transaction is already Processed.Response already updated for Txn Id '"
								+ TM.getId() + "'.");
				request.setAttribute("errorMsg",
						"Error 10054 : Transaction is already Processed.Response already updated for Txn Id '"
								+ TM.getId() + "'.");
				request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
			}
		}

	} catch (Exception e) {

		log.info("ipaySBI.jsp ::: Error Occurred :: for id=" + MTrackid + " error=" + e);
		String respMessage = "Transaction Failed.Undefined Error.";
		int status = getTransactionMaster(TM, dm, respMessage);

		log.info("status=====>" + status);
		if (status == 0) {
			request.setAttribute("ResTrackId", MTrackid);
			request.getRequestDispatcher("/respHandlersbi").include(request, response);
		} else {
			log.info("ipaySBI.jsp ::: Transaction is already Processed.Response already updated for Txn Id '"
					+ TM.getId() + "'.");
			request.setAttribute("errorMsg",
					"Error 10054 : Transaction is already Processed.Response already updated for Txn Id '"
							+ TM.getId() + "'.");
			request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
		}

	}
%>

<%!
Logger log = LoggerFactory.getLogger("JSPS.ipaySBI.jsp");

	public int getTransactionMaster(TransactionMaster TM, DataManager dm, String respMessage) {

		String resp_date_Time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		if (!TM.getRespStatus().equalsIgnoreCase("1")) {
			TM.setTransStatus("F");
			TM.setServiceRRN("NA");
			TM.setServiceTxnId("NA");
			TM.setServiceAuthId("NA");
			TM.setRespStatus("1");
			TM.setErrorCode("FFFFF");
			TM.setSpErrorCode("NA");
			TM.setRespMessage(respMessage);
			TM.setRespDateTime(resp_date_Time);
			TM.setModified_By("ipaySBI");
			TM.setModified_On(resp_date_Time);
			dm.updateTxn(TM);

			return 0;

		} else {
			return 1;

		}

	}
	
%>
	
	

