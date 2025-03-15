<%@ include file="/include.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.rew.payment.utility.PGUtils"%>
<%@page import="com.rew.pg.dto.TransactionMaster"%>
<%@page import="com.rew.pg.db.DataManager"%>
<%@ page language="java" session="true"%>
<%@ page import="java.util.StringTokenizer"%>
<%@ page import="java.util.Random"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.io.*"%>
<%@ page import="java.io.BufferedInputStream"%>
<%@ page import="java.io.BufferedReader"%>
<%@ page import="java.io.DataOutputStream"%>
<%@ page import="java.io.File"%>
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
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%
	TransactionMaster TM = null;
	String encRespXML = null;
	  Logger log = LoggerFactory.getLogger("JSPS.Redirect.jsp");
	if (request.getParameter("paymentid") == null) {
		log.info("Error in response>>>> without payment id");
	} else {

		String paymentid = request.getParameter("paymentid");
		String ErrorNo = request.getParameter("Error");
		String udf1 = request.getParameter("udf1");
		String udf2 = request.getParameter("udf2");
		String udf3 = request.getParameter("udf3");
		String udf4 = request.getParameter("udf4");
		String udf5 = request.getParameter("udf5");
		String amt = request.getParameter("amt");
		String trackid = request.getParameter("trackid");
		String result = request.getParameter("result");
		String auth = request.getParameter("auth");
		String ref = request.getParameter("ref");
		String tranid = request.getParameter("tranid");
		String postdate = request.getParameter("postdate");
		String ErrorText = request.getParameter("ErrorText");
		String authRespCode = request.getParameter("authRespCode");
		log.info("for pg_id=" + trackid + " paymentid=" + paymentid + " ErrorNo=" + ErrorNo + " ErrorText="
				+ ErrorText + " authRespCode=" + authRespCode + " result=" + result);
		TM = new DataManager().getTxnMaster(trackid);

		/* 
		First check, if error number is NOT present,then go for Hashing using required parameters 
		*/
		/* 
		NOTE - MERCHANT MUST LOG THE RESPONSE RECEIVED IN LOGS AS PER BEST PRACTICE. Since the
		logging mechanism is merchant driven, the sample code for same is not provided in this
		pages
		*/
		if (TM.getRespStatus().equalsIgnoreCase("5")) 
		{
			TM.setRespStatus("0");
			if (ErrorNo == null) {
				log.info("<<<<<<<<<<error is null>>>>>>>>>>>>>>");
				log.info("paymentid >>>" + paymentid + "\n ErrorNo >>>>> " + ErrorNo + " ErrorText="
						+ ErrorText);
				log.info("amt >>>" + amt + "\n trackid >>>>> " + trackid);
				log.info("trackid >>>" + trackid + "\n ErrorNo >>>>> " + ErrorNo);
				log.info("result >>>" + result + "\n auth >>>>> " + auth);
				log.info("ref >>>" + ref + "\n postdate >>>>> " + postdate);
				log.info("tranid >>>" + tranid + "\n authRespCode >>>>> " + authRespCode);
				log.info("for pg_id=" + trackid + " paymentid=" + paymentid + " ");
				//verification 

				String verificationurl = PGUtils.getPropertyValue("tranPortalUrlHDFC");
				log.info("verificationurl >>>>> " + verificationurl);
				log.info(" ============== Response Log PARes != Null Then ============== ");

				String PaymentIDMD = "<paymentid>" + request.getParameter("paymentid") + "</paymentid>";
				//Collect PaRes recieved from ACS
				log.info("PaymentIDMD  	==>   " + PaymentIDMD);

				ArrayList<String> spMidKey = null;

				log.info("for pg_id=" + TM.getId() + " paymentid=" + paymentid
						+ " getSPMidKey()  parameters::::::::::MerchantId:::::" + TM.getMerchantId()
						+ "bankId::::::::::" + TM.getBankId() + "InstrumentId::::::::" + TM.getInstrumentId()
						+ "spIdHDFCRupay:::::" + PGUtils.getPropertyValue("spIdHDFCRupay"));

				spMidKey = new DataManager().getSPMidKey(TM.getMerchantId(), TM.getBankId(),
						TM.getInstrumentId(), PGUtils.getPropertyValue("spIdHDFCRupay"));

				String sVerifyString = "<member>Anil</member>" + "<action>8</action>" + "<amt>" + amt + "</amt>"
						+ "<trackid>" + trackid + "</trackid>" + "<transid>" + tranid + "</transid>" + "<udf5>"
						+ trackid + "</udf5>" + "<ID>" + spMidKey.get(0).toString() + "</ID>" + "<password>"
						+ spMidKey.get(1).toString() + "</password>";

				log.info("for pg_id=" + TM.getId() + " paymentid=" + paymentid
						+ " Verification String ::::::sVerifyString" + sVerifyString);

				URL url = new URL(verificationurl);//PGUtils.getPropertyValue("mpiVerifyEnrollmentUrlHDFC"));

				Object obj;
				obj = (HttpsURLConnection) url.openConnection(); //create a SSL connection object server-to-server
				((URLConnection) obj).setDoInput(true);
				((URLConnection) obj).setDoOutput(true);
				((URLConnection) obj).setUseCaches(false);

				((URLConnection) obj).setConnectTimeout(45000);
				((URLConnection) obj).setReadTimeout(55000);

				((URLConnection) obj).setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); //defining content type
				DataOutputStream dataoutputstream = new DataOutputStream(
						((URLConnection) obj).getOutputStream());
				dataoutputstream.writeBytes(sVerifyString);
				dataoutputstream.flush();
				dataoutputstream.close();
				BufferedReader bufferedreader = new BufferedReader(
						new InputStreamReader(((URLConnection) obj).getInputStream()));
				String decodedString;
				String InitialResponse = "";
				while ((decodedString = bufferedreader.readLine()) != null) {
					InitialResponse = InitialResponse + decodedString; //response recieved from PG is stored in variable
				}
				log.info(InitialResponse);

				String NEResResult = GetTextBetweenTags(InitialResponse, "<result>", "</result>");//It will give  Result 
				String NEResAmount = GetTextBetweenTags(InitialResponse, "<amt>", "</amt>");//It will give Amount
				String NEResTrackId = GetTextBetweenTags(InitialResponse, "<trackid>", "</trackid>");//It will give TrackID 
				String NEResPayid = GetTextBetweenTags(InitialResponse, "<payid>", "</payid>");//It will give PaymentID
				String NEResRef = GetTextBetweenTags(InitialResponse, "<ref>", "</ref>");//It will give Reference NO
				String NEResTranid = GetTextBetweenTags(InitialResponse, "<tranid>", "</tranid>");//It will give Transaction ID

				log.info("for pg_id=" + TM.getId() + " paymentid=" + paymentid + " NEResResult   	 ==>    "
						+ NEResResult);
				log.info("for pg_id=" + TM.getId() + " paymentid=" + paymentid + " NEResAmount   	 ==>    "
						+ NEResAmount);
				log.info("for pg_id=" + TM.getId() + " paymentid=" + paymentid + " NEResTrackId   	 ==>    "
						+ NEResTrackId);
				log.info("for pg_id=" + TM.getId() + " paymentid=" + paymentid + " NEResPayid   	 ==>    "
						+ NEResPayid);
				log.info("for pg_id=" + TM.getId() + " paymentid=" + paymentid + " NEResRef  		 ==>    "
						+ NEResRef);
				log.info("for pg_id=" + TM.getId() + " paymentid=" + paymentid + " NEResTranid   	 ==>    "
						+ NEResTranid);
				String responseCode = NEResResult;
				String errorCode = "NA";
				String spErrorCode = "NA";
				String message = "NA";
				if (responseCode.equalsIgnoreCase("CAPTURED")) {
					log.info("for pg_id=" + TM.getId() + " paymentid=" + paymentid
							+ " respHandlerHDFC ::: ===================Success =====================");
					responseCode = "Ok";
					errorCode = "00000";
					message = "Transaction Successful.";
				} else {
					responseCode = "F";
					message = NEResResult;//"Failed";
					log.info("for pg_id=" + TM.getId() + " paymentid=" + paymentid
							+ " Redirect.jsp ::: NOT CAPTURED :: responseCode : " + responseCode
							+ " :: message : " + message);

					/*log.info("Redirect.jsp ::: NOT CAPTURED :: EnMPIResResult : "+responseCode+" :: EnMPIReserror : "+EnMPIReserror);
					
					 if(!EnMPIReserror.startsWith("!ERROR!-"))
						EnMPIReserror = "!ERROR!-"+EnMPIReserror;						
					
					spErrorCode = EnMPIReserror.substring(EnMPIReserror.indexOf("-")+1, EnMPIReserror.lastIndexOf("-"));																
					
					JSONObject errorCodeJSON =  DataManager.getErrorCode(spErrorCode, PGUtils.getPropertyValue("spIdHDFC"));
					
					errorCode	= errorCodeJSON.getString("aggErrorCode");
					message		= errorCodeJSON.getString("aggErrorDesc"); */

					//EnMPIReserror;
				}
				if (!TM.getRespStatus().equalsIgnoreCase("1")) {
					String resp_date_Time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

					TM.setTransStatus(responseCode);
					TM.setServiceRRN(NEResRef);
					TM.setServiceTxnId(NEResTranid);
					TM.setServiceAuthId(NEResPayid);
					TM.setRespStatus("1");
					TM.setErrorCode(errorCode);
					TM.setSpErrorCode(spErrorCode);
					TM.setRespMessage(message);
					TM.setRespDateTime(resp_date_Time);
					TM.setModified_By("HDFCRespHandlerRupay");
					TM.setModified_On(resp_date_Time);
					new DataManager().updateTxn(TM);

					//request.setAttribute("ResTrackId", trackid);			
					//request.getRequestDispatcher("/respHandlerHDFC").include(request, response);
					encRespXML = PGUtils.generateResponse(TM);
					//response.sendRedirect(TM.getReturn_url()+"?merchantId="+TM.getMerchantId()+"&respData="+encRespXML);

				} else {
					log.info(
							"TranVbyV.jsp ::: Transaction is already Processed.Response already updated for Txn Id '"
									+ TM.getId() + "'.");
					request.setAttribute("errorMsg",
							"Error 10054 : Transaction is already Processed.Response already updated for Txn Id '"
									+ TM.getId() + "'.");
					request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
				}

				//response.sendRedirect("http://localhost:8080/payment/rupay/StatusTRAN.jsp?ResResult="+result+"&ResTrackId="+trackid+"&ResPaymentId="+paymentid+"&ResRef="+ref+"&ResTranId="+tranid+"&ResAmount="+amt+"&ResauthRespCode="+authRespCode);
			} else {
				log.info("<<<<<<<<<<error is not null >>>>>>>>>>>>>>");
				log.info("paymentid >>>" + paymentid + "\n ErrorNo >>>>> " + ErrorNo + " ErrorText="
						+ ErrorText);
				log.info("amt >>>" + amt + "\n trackid >>>>> " + trackid);
				log.info("trackid >>>" + trackid + "\n ErrorNo >>>>> " + ErrorNo);
				log.info("result >>>" + result + "\n auth >>>>> " + auth);
				log.info("ref >>>" + ref + "\n postdate >>>>> " + postdate);
				log.info("tranid >>>" + tranid + "\n authRespCode >>>>> " + authRespCode);
				//verification
				try {
					String verificationurl = PGUtils.getPropertyValue("tranPortalUrlHDFC");//"https://securepgtest.fssnet.co.in/ipayb/servlet/TranPortalXMLServlet";

					log.info("for pg_id=" + TM.getId() + " paymentid=" + paymentid + " verificationurl="
							+ verificationurl);

				} catch (Exception e) {
					e.printStackTrace();
				}
				String PaymentIDMD = "<paymentid>" + paymentid + "</paymentid>";
				//Collect PaRes recieved from ACS
				log.info("PaymentIDMD  	==>   " + PaymentIDMD);
				TM = new DataManager().getTxnMaster(trackid);
				ArrayList<String> spMidKey = null;

				JSONObject errorCodeJSON = DataManager.getErrorCode(ErrorNo,
						PGUtils.getPropertyValue("spIdHDFC"));

				String errorCode = errorCodeJSON.getString("aggErrorCode");
				String message = errorCodeJSON.getString("aggErrorDesc");
				String responseCode = "F";

				if (!TM.getRespStatus().equalsIgnoreCase("1")) {
					String resp_date_Time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

					TM.setTransStatus(responseCode);
					TM.setServiceRRN(ref);
					TM.setServiceTxnId(tranid);
					TM.setServiceAuthId(PaymentIDMD);
					TM.setRespStatus("1");
					TM.setErrorCode(errorCode);
					TM.setSpErrorCode(errorCode);
					TM.setRespMessage(message);
					TM.setRespDateTime(resp_date_Time);
					TM.setModified_By("HDFCRespHandlerRupay");
					TM.setModified_On(resp_date_Time);
					new DataManager().updateTxn(TM);

					//request.setAttribute("ResTrackId", trackid);			

					//request.getRequestDispatcher("/respHandlerHDFC").include(request, response);
					encRespXML = PGUtils.generateResponse(TM);
					//response.sendRedirect(TM.getReturn_url()+"?merchantId="+TM.getMerchantId()+"&respData="+encRespXML);  

				} else {
					log.info(
							"TranVbyV.jsp ::: Transaction is already Processed.Response already updated for Txn Id '"
									+ TM.getId() + "'.");
					request.setAttribute("errorMsg",
							"Error 10054 : Transaction is already Processed.Response already updated for Txn Id '"
									+ TM.getId() + "'.");
					request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
				}

				//response.sendRedirect("http://localhost:8080/payment/rupay/StatusTRAN.jsp?ResError="+ErrorText+"&ResTrackId="+trackid+"&ResPaymentId="+paymentid+"&ResRef="+ref+"&ResTranId="+tranid+"&ResAmount="+amt+"&ResauthRespCode="+authRespCode);
			}

		} 
		else 
		{
			log.info("TranVbyV.jsp ::: Transaction is already posted to TransVbyV with status 5 '"+ TM.getId() + "'.");
			request.setAttribute("errorMsg","Error 10054 : Transaction is already Processed.Response already updated for Txn Id '"+ TM.getId() + "'.");
			request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
		}
	}
%>
<%!//=======This is GetTextBetweenTags function which return the value between two XML tags or two string =====

	//Logger log = Logger.getLogger("JSPS.TranVbyV.jsp");

	public String GetTextBetweenTags(String InputText, String Tag1, String Tag2) {
		String Result;

		int index1 = InputText.indexOf(Tag1);
		//log.info(index1);
		int index2 = InputText.indexOf(Tag2);
		if (index1 != -1 && index2 != -1) {
			index1 = index1 + Tag1.length();
			Result = InputText.substring(index1, index2);
			return Result;
		} else {
			Result = "";
			return Result;
		}
	}%>

<html>
<head>
<title>Merchant Redirector</title>
<script type="text/javascript">
	//history.forward();
	function redirectRequest() {
		document.RedirectForm.submit();
	}
</script>
</head>
<body onload="redirectRequest();">

	<form name="RedirectForm" action="<%=TM.getReturn_url()%>"
		method="post">
		<table width="80%" align="center" border="0" cellpadding='0'
			cellspacing='0'>
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
						<strong>Please do not click Back/Refresh button while
							Redirecting.</strong>
					</h5>
				</td>
			</tr>
			<!-- 			response.sendRedirect(+"?merchantId="+TM.getMerchantId()+"&respData="+encRespXML); 
 -->
			<tr>
				<td><input type="hidden" name="respData"
					value="<%=encRespXML%>"></td>
			</tr>
			<tr>
				<td><input type="hidden" name="merchantId"
					value="<%=TM.getMerchantId()%>"></td>
			</tr>

			<tr>
				<td><input type="hidden" name="pgid" value="<%=TM.getId()%>"></td>
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
