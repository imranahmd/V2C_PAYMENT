package com.rew.payment.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apps.trides.crypto.AppsCrypto;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.rew.otp.service.OTPService;
import com.rew.otp.service.SMS;
import com.rew.pg.db.DataManager;
import com.rew.pg.dto.CheckOutMaster;
import com.rew.pg.dto.TransactionMaster;
import com.wibmo.tokenize.wrapper.impl.TokenizeWrapper;
import com.wibmo.tokenize.wrapper.model.tokenize.DeleteTokenRequest;
import com.wibmo.tokenize.wrapper.model.tokenize.DeleteTokenResponse;

public class AjaxAction
extends HttpServlet
{
    private static Logger logger = LoggerFactory.getLogger(AjaxAction.class);


	private static final long serialVersionUID = 1L;


	public AjaxAction() {}


	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		response.getWriter().append("Get Method not supported");
	}

	protected void doOption(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getWriter().append("Option Method not Allowed.");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setHeader("Cache-control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "-1");
		logger.info("AjaxAction.java  ::: started  successfully +++++++++++++++++++++++++++++++++++++++++++ ");
		PrintWriter out = response.getWriter();
		String dateTime = null;
		dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String type = request.getParameter("type");
		TransactionMaster TM;
		DataManager dm=new DataManager();
		String wibmoProperties = PGUtils.getPropertyValue("wibmoConfig");// wibmo property file path
		
		String clientId = PGUtils.getPropertyValue("clientId");
		String clientApiUser = PGUtils.getPropertyValue("clientApiUser");
		String clientApiKey = PGUtils.getPropertyValue("clientApiKey");
		String vaultId = PGUtils.getPropertyValue("vaultId");
		String tokenSecretKey = PGUtils.getPropertyValue("tokenSecretKey");
		String IdMerchant = PGUtils.getPropertyValue("merchantId");
		String deTokenSecretKey = PGUtils.getPropertyValue("deTokenSecretKey");

		
		if ((type != null) && (type.equals("BackButton")))
		{
			JSONObject job = new JSONObject();
			String encRespXML = null;
logger.info("Transaction Back througe the user");
			String amount = request.getParameter("amount");
			String mTrackId = request.getParameter("mTrackId");
			String merchantRefNo = request.getParameter("merchantRefNo");
			
			logger.info("Transaction Back througe the user "+mTrackId);

			TM = new TransactionMaster();

			TM = dm.getTxnMaster(mTrackId);
			TM.setTransStatus("F");
			TM.setErrorCode("FFFFF");
			TM.setRespMessage("Transaction Cancelled!");
			dm.updateTxn(TM);
			encRespXML = PGUtils.generateResponse(TM);

			final String txnType="AJAXACTION";

			
				job = new JSONObject();
				job.put("url", TM.getReturn_url());
				job.put("encRespXML", encRespXML);
				job.put("merchantId", TM.getMerchantId());
				job.put("pgid", TM.getId());
				out.print(job);

		}
		else if (type != null && type.equals("getQRDataV")) {

			logger.info("type::::::::" + type);
			YES_INTENT_AES YesIn = new YES_INTENT_AES();
			String id = request.getParameter("transactionId");
			//String id = request.getParameter("transactionId");
			logger.info("type::::::Id::::::::::" + id);
String bankId="UPI";
String sInstrumentId="UPI";
			
String retamount=null;
String sClassFileLoader ,spId;
int payout;
			
			String MerchantVpa = "shfkdfjhkj";
			DataManager datamanager = new DataManager();

			
			  TransactionMaster txnMaster = datamanager.getTxnMaster(id); 
			  String amount = txnMaster.getAmount(); 
			  logger.info("amount::::::::" + amount);
			 
			request.setAttribute("id", id);
			request.setAttribute("amount", amount);
			
			IntentRouter in = new IntentRouter();
						String finalData = in.RoutingSystem(id, amount, txnMaster.getMerchantId(), txnMaster,request);
			logger.info("finalData::::::::" + finalData);
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = null;
		    byte[] pngData = null;
			 try {
			      bitMatrix = qrCodeWriter.encode(finalData, BarcodeFormat.QR_CODE, 250, 250);
			      ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
			      MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
			      pngData = pngOutputStream.toByteArray();
			    } catch (WriterException e) {
			      e.printStackTrace();
			    } catch (IOException e) {
			      e.printStackTrace();
			    }
			 String data ="data:image/png;base64,"+Base64.encode(pngData);
				logger.info("data :::"+data);
				JSONObject job = new JSONObject();
job.put("data",data);
job.put("Url",finalData);

			out.print(job);
		
		}
		else if ((type != null) && (type.equals("getStatusUPITransaction")))
		{
			JSONObject job = new JSONObject();
			String encRespXML = null;

			String amount = request.getParameter("amount");
			String mTrackId = request.getParameter("mTrackId");
			String merchantRefNo = request.getParameter("merchantRefNo");
			String i = request.getParameter("val_i");

			logger.info("AjaxAction.java  ::: " + amount + ", " + mTrackId + " , " + merchantRefNo +" , " + i);

			TM = new TransactionMaster();

			TM = dm.getTxnMaster(mTrackId);

			logger.info("AjaxAction.java GetStatus for UPI ::: Service Txn_Id  :: " + TM.getServiceTxnId() + "  ,  Service rrn :: " + TM.getServiceRRN() + "  against the txn id " + mTrackId);

			final String txnType="AJAXACTION";

			if(TM.getIsPostedBackRes().equals("0")) {
				if ((!TM.getServiceRRN().equalsIgnoreCase("NA")) && (!TM.getServiceRRN().isEmpty()) && (TM.getServiceRRN() != null) &&
						(!TM.getServiceTxnId().equalsIgnoreCase("NA")) && (!TM.getServiceTxnId().isEmpty()) && (TM.getServiceTxnId() != null))
				{
					encRespXML = PGUtils.generateResponse(TM);

					String respRetry=new PGUtils().postDataRetry(TM, encRespXML, request, dm, txnType);
					dm.updateIsPostedBackRes(mTrackId);
					if(!respRetry.equalsIgnoreCase("NA"))
					{
						out.print(respRetry);
					}
					else
					{
						job = new JSONObject();
						job.put("url", TM.getReturn_url());
						job.put("encRespXML", encRespXML);
						job.put("merchantId", TM.getMerchantId());
						job.put("pgid", TM.getId());
						logger.info("AjaxAction.jsp ::: UPI  :: SUCCESS Return_URL  :: " + TM.getReturn_url());
						out.print(job.toString());
					}

				}
				else if (((TM.getServiceTxnId().equalsIgnoreCase("NA")) || (TM.getServiceTxnId().isEmpty())) && (i.equals("30")))
				{
					encRespXML = PGUtils.generateResponse(TM);

					String respRetry=new PGUtils().postDataRetry(TM, encRespXML, request, dm, txnType);
					dm.updateIsPostedBackRes(mTrackId);
					if(!respRetry.equalsIgnoreCase("NA"))
					{
						out.print(respRetry);
					}
					else
					{
						encRespXML = PGUtils.generateResponse(TM);
						job = new JSONObject();
						job.put("url", TM.getReturn_url());
						job.put("encRespXML", encRespXML);
						job.put("merchantId", TM.getMerchantId());
						job.put("pgid", TM.getId());
						logger.info("AjaxAction.java ::: UPI  ::: After TimeOut Return_URL  :: " + TM.getReturn_url());
						out.print(job.toString());
					}
				}
				else
				{
					String status = "N";
					out.print(status);
				}
			}else {
				String status = "N";
				out.print(status);
			}

		}
		
		else if(type != null && type.equals("Intnet"))
				{
			JSONObject job = new JSONObject();
			job.put("QrString", "upi://pay?pa=EPAISAA@rbl&pn=rewardoo&mc=1506&tr=101444401001&tn=Pay&am=10.00&cu=INR&mam=10&refUrl=&sign=MEUCIQCA1utL2VrrkjRD0OOCJU74WmFY//OzbRiKFIBVhTe9KQIgA8vtA1XH7007E08ZjJu7yxclzKCn+InJtCZ8G2QJBgE=");

			out.print(job);

				}
		else if (type != null && type.equals("cancelUPITransaction")) {
			
			JSONObject job = new JSONObject();
			String encRespXML = null;
			
			String amount = request.getParameter("amount");
			String mTrackId = request.getParameter("mTrackId");
			String merchantRefNo = request.getParameter("merchantRefNo");
			
			logger.info("AjaxAction.java  ::: " + amount + ", " + mTrackId + " , " + merchantRefNo );

			TM = new TransactionMaster();

			TM = dm.getTxnMaster(mTrackId);
			
			TM.setTransStatus("F");
			
			dm.updateTxn(TM);
			
			encRespXML = PGUtils.generateResponse(TM);
			
			final String txnType="AJAXACTION";

			String respRetry=new PGUtils().postDataRetry(TM, encRespXML, request, dm, txnType);
			dm.updateIsPostedBackRes(mTrackId);
			if(!respRetry.equalsIgnoreCase("NA"))
			{
				out.print(respRetry);
			}
			else
			{
				encRespXML = PGUtils.generateResponse(TM);
				job = new JSONObject();
				job.put("url", TM.getReturn_url());
				job.put("encRespXML", encRespXML);
				job.put("merchantId", TM.getMerchantId());
				job.put("pgid", TM.getId());
				logger.info("AjaxAction.java ::: UPI  ::: After Cancel Payment Return_URL  :: " + TM.getReturn_url());
				out.print(job.toString());
			}
			
		}
		else if (type != null && type.equals("getStatusBQRTransaction")) {
	        JSONObject job = new JSONObject();
	        String encRespXML = null;
	        String transId = request.getParameter("transactionId");
	        String i = request.getParameter("val_i");
	        logger.info("AjaxAction.java  ::: transId" + transId + ", i " + i);
	        TM = dm.getTxnMaster(transId);
	        logger.info("outside >>>>>>>>>>>");
	        logger.info("AjaxAction.java  ::: transId" + transId + ", TM.getRespStatus() " + TM.getRespStatus());
	        String txnType = "AJAXACTION";
	        if (TM.getRespStatus().equals("1")) {
	          encRespXML = PGUtils.generateResponse(TM);
	          String respRetry = new PGUtils().postDataRetry(TM, encRespXML, request, dm, "AJAXACTION");
	          if (!respRetry.equalsIgnoreCase("NA")) {
	            out.print(respRetry);
	          } else {
	            job = new JSONObject();
	            job.put("url", TM.getReturn_url());
	            job.put("encRespXML", encRespXML);
	            job.put("merchantId", TM.getMerchantId());
	            job.put("pgid", TM.getId());
	            logger.info("AjaxAction.java ::: BQR  ::: After TimeOut Return_URL  :: " + TM.getReturn_url());
	            out.print(job.toString());
	          } 
	        } else if (i.equalsIgnoreCase("29") || i == "29") {
	          logger.info("inside >>>>>>>>>>>");
	          encRespXML = PGUtils.generateResponse(TM);
	          String respRetry = new PGUtils().postDataRetry(TM, encRespXML, request, dm, "AJAXACTION");
	          if (!respRetry.equalsIgnoreCase("NA")) {
	            out.print(respRetry);
	          } else {
	            job = new JSONObject();
	            job.put("url", TM.getReturn_url());
	            job.put("encRespXML", encRespXML);
	            job.put("merchantId", TM.getMerchantId());
	            job.put("pgid", TM.getId());
	            logger.info("AjaxAction.java ::: BQR  ::: After TimeOut Return_URL  :: " + TM.getReturn_url());
	            out.print(job.toString());
	          } 
	        } 
	      } 
		else if ((type != null) && (type.equals("getONUSBINDetail")))
		{


			String cardNumber = request.getParameter("cardNumber").toString();
			String binType = request.getParameter("hdfcBinType").toString();
			String merchantId=request.getParameter("merchantId").toString();
			String status = dm.binValidate(cardNumber, binType,merchantId);
			logger.info("AjaxAction.java :::  getDeatail for ONUS BIN ::  cardNumber :: " + cardNumber + "   ,  binType  :: " + binType + "  ,  status   ::  " + status);

			out.print(status);

		}
		else if ((type != null) && (type.equals("getONUSBINDetailOtherCard")))
		{


			String cardNumber = request.getParameter("cardNumber").toString();
			String binType = request.getParameter("hdfcBinType").toString();
			String merchantId=request.getParameter("merchantId").toString();
			String status = dm.binValidateOtherCard(cardNumber, binType,merchantId);
			logger.info("AjaxAction.java :::  getDeatail for ONUS BIN ::  cardNumber :: " + cardNumber + "   ,  binType  :: " + binType + "  ,  status   ::  " + status);

			out.print(status);



		}
		else if ((type != null) && (type.equals("rms")))
		{
			String merchantId = request.getParameter("merchantId");
			String ipAddress = request.getParameter("ipAddress");
			String txtAmt = request.getParameter("txtAmt");
			String cardNo = request.getParameter("cardNo");
			String cardType = request.getParameter("cardType");

			logger.info("AjaxAction.java :: Pay  :: merchantId : " + merchantId + " , ipAddress : " + ipAddress + " , txtAmt : " + txtAmt + " , cardType : " + cardType);
			Map<String, String> rmsData = dm.getRMSDetails(merchantId, ipAddress, txtAmt, cardNo, cardType);
			JSONObject rms = new JSONObject();
			for (Map.Entry<String, String> entry : rmsData.entrySet())
			{
				rms.put((String)entry.getKey(), entry.getValue());
			}

			/*
			 * logger.info("AjaxAction.java Pay  :: RMS Result from DB : " + rms);
			 */
			out.print(rms.toString());
		}
		else if ((type != null) && (type.equals("removeCard")))
		{
			int status = 0;
			String cMid = request.getParameter("cMid").toString();
			String cNo = request.getParameter("cNo").toString();
			String cMail = request.getParameter("cMail").toString();
			String cMob = request.getParameter("cMob").toString();

			logger.info("AjaxAction.java ::: Card to Remove is : " + cNo);

			CheckOutMaster CM = new CheckOutMaster();

			CM.setCardNo(cNo);
			CM.setMerchantId(cMid);
			CM.setCustMail(cMail);
			CM.setCustMobile(cMob);
			CM.setModifiedOn(dateTime);

			status = dm.removeCard(CM);

			out.print(status);
		}
		else if ((type != null) && (type.equals("decryptCard")))
		{
			String secretKey = null;String decCardNo = null;

			String encCardNo = request.getParameter("encCardNo").toString();
			String encKey = request.getParameter("encKey").toString();
			String keKey = request.getParameter("keKey").toString();

			/*
			 * logger.info("AjaxAction.java ::: decryptCard :: Card to decrypt is : " +
			 * encCardNo);
			 */
			try
			{
				secretKey = TripleKeyEnc.decode(encKey, keKey);
				decCardNo = AppsCrypto.decrypt(encCardNo, secretKey);


			}
			catch (Exception e)
			{


				logger.error("AjaxAction.java ::: decryptCard :: Error in Card Decryption -->", e);
			}

			out.print(decCardNo);
		}
		else if ((type != null) && (type.equals("encryptCard")))
		{

			String secureCardNo = null;String secureCvv = null;
			JSONObject respJSON = null;



			logger.info("AjaxAction.java ::: Card to encrypt is : ","Hide details");

			try
			{
				String cardNo = PGUtils.cryptoJs(request.getParameter("cardNo")).toString().replaceAll(" ", "").trim();
				String cvv = request.getParameter("vNo").toString();
				secureCardNo = AppsCrypto.encrypt(cardNo, "jodkrxhnjoamvael");
				secureCvv = AppsCrypto.encrypt(cvv, "jodkrxhnjoamvael");
				/*
				 * logger.info("AjaxAction.java ::: encryptCard :: Secured Card No is : " +
				 * secureCardNo + " And Secured CVC is : " + secureCvv);
				 */				respJSON = new JSONObject();
				respJSON.put("secureCardNo", secureCardNo);
				respJSON.put("secureCvv", secureCvv);

			}
			catch (Exception e)
			{
				logger.error("AjaxAction.java ::: encryptCard :: Error in Card Decryption -->", e);
			}
			if (respJSON != null) {
				out.print(respJSON.toString());
			}else {
				out.print("");
			}
		}

		else if(type != null && type.equals("getemidetails"))
		{
			//logger.info("type ===================>"+type);
			String amt=request.getParameter("amt");
			String bankname=request.getParameter("bankname");

			String EmiDetails = dm.getEmiDetails1(amt,bankname);
			logger.info("EmiDetails ===================== > "+EmiDetails);
			out.print(EmiDetails.trim());

		}
		else if(type != null && type.equals("serviceProviderSearch"))
		{
			String spId=request.getParameter("processId");

			String spName = dm.getServiceProviderName(spId);
			logger.info("EmiDetails ===================== > "+spName);
			out.print(spName);

		}
		
		else if(type != null && type.equals("getDealerDetails")) {
			String mobile = request.getParameter("mobile");
			String merchantId = request.getParameter("merchantId");
			double amount = 0.0;
			List<TransactionMaster> transactionDetails = null;
			JSONObject dealerDetails = null;
			try {
				if(mobile != null) {
					transactionDetails = dm.getTxnMasterByMobileNo(mobile, merchantId);
					if(transactionDetails != null && !transactionDetails.isEmpty()) {
						for(TransactionMaster transaction : transactionDetails) {
							if(transaction != null && transaction.getAmount() != null) {
								amount = amount + Double.parseDouble(transaction.getAmount());
							}
						}
						dealerDetails = new JSONObject();
						String[] lclg = transactionDetails.get(0).getUdf3().split("\\|");
						dealerDetails.put("pincode", transactionDetails.get(0).getUdf1());
						dealerDetails.put("address", transactionDetails.get(0).getUdf2());
						if(lclg.length>1) {
							dealerDetails.put("lc", lclg[0]);
							dealerDetails.put("lg", lclg[1]);
						}
						dealerDetails.put("dob", transactionDetails.get(0).getUdf4());
						dealerDetails.put("name", transactionDetails.get(0).getUdf5());
						dealerDetails.put("mail", transactionDetails.get(0).getCustMail());
						dealerDetails.put("amount", amount);
					}else {
						logger.info("No transaction found for this mobile no.");
					}
				}else {
					logger.error("mobile number is null");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			out.print(dealerDetails);
		}
		else if(type != null && type.equals("formatAmount")) {
			String amount = request.getParameter("amount");
			DecimalFormat df = new DecimalFormat("0.00");
			amount = df.format(Double.parseDouble(amount));
			logger.info("formatted amount - "+amount);
			out.print(amount);
		}
		else if(type != null && type.equals("txnLimitCheck")) {
			logger.info("txnLimitCheck is executing..");
			String merchantId = request.getParameter("merchantId");
			String link = request.getParameter("link");
			String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String todayEnd = today;
			today = today+" 00:00:00";
			todayEnd = todayEnd+ " 23:59:59";
			Boolean isLimitBreached = dm.isTransLimitBreached(link, merchantId,today,todayEnd);
			out.print(isLimitBreached);
		}
		
		else if(type != null && type.equals("removeCookie")) {
			logger.info("inside remove Cookie.....");
	
			 Cookie ck1=new Cookie("mobileNo","");  
			    Cookie ck2=new Cookie("emailId",""); 
			    
			    ck1.setMaxAge(0);
			    ck2.setMaxAge(0);
			  
			     response.addCookie(ck1);  
			     response.addCookie(ck2);  
		}
		
		else if(type != null && type.equals("sendOtp")) {
			logger.info("inside otp implementation.....");
			String jSession=null;
			String mobileNo = request.getParameter("mobileNo");
			String emailId = request.getParameter("emailId");
			String merchantName = request.getParameter("merchantName");
		
			
			String txnId = request.getParameter("txnId");
			String otp;
			OTPService service = new OTPService();
			SMS sms = new SMS();
			try {
				otp = randomNumeric(6);
			
				
				service.saveOtp(mobileNo, emailId, otp, txnId);
				
			 sms.sendSMS(mobileNo, otp, merchantName);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else if(type != null && type.equals("validateOtp")) {
			logger.info("inside otp validation.....");
			
			String mobileNo = request.getParameter("mobileNo");
			String emailId = request.getParameter("emailId");
		
			String otp = request.getParameter("otp");
			
		
			OTPService service = new OTPService();
			
			try {
			
			
				String resp= service.validateOtp(mobileNo, emailId, otp);
				
				out.print(resp);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else if(type != null && type.equals("getDetails")) {
			logger.info("Cookie implementation..");
			String mobileNo = request.getParameter("mobileNo");
			String emailId = request.getParameter("emailId");
			String otp = request.getParameter("otp");
			String MId = request.getParameter("MID");

			OTPService service = new OTPService();
			try {
				String resp= service.validateOtp(PGUtils.cryptoJs(mobileNo), PGUtils.cryptoJs(emailId), otp);
		
				if(resp.equalsIgnoreCase("OTP verified successfully")) {
					
				//	response.setHeader("SET-COOKIE", "mobileNo=" + mobileNo + "; Max-Age=31536000; HttpOnly" );
				//	response.setHeader("SET-COOKIE", "emailId=" + emailId + ";  Max-Age=31536000; HttpOnly" );
					
				       Cookie ck1=new Cookie("mobileNo",mobileNo);  
				       Cookie ck2=new Cookie("emailId",emailId); 
				    //   Cookie ck3=new Cookie("session-flag","1"); 
				       
				       ck1.setMaxAge(31536000);
				       ck2.setMaxAge(31536000);
				     
			            response.addCookie(ck1);  
			            response.addCookie(ck2);  

			     
			            
						Vector<List<String>> storedCard= dm.getStoredCardsVAS(emailId, mobileNo, MId);
						
						
						HttpSession session= request.getSession(false);
				        
						session.setAttribute("cardDetails", storedCard);
				
					out.print("Y");	
				}else if(resp.equalsIgnoreCase("OTP expired")){
					out.print("OTP expired");	
				}else if(resp.equalsIgnoreCase("OTP not verified successfully")){
					out.print("OTP not verified successfully");	
				}


			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
		else if ((type != null) && (type.equals("deleteCard")))
		{
			int status = 0;
			String Id = request.getParameter("Id").toString();
			String tokenReferenceId = request.getParameter("tokenReferenceId").toString();     //bcz card no replace with token refId
			String cardType = request.getParameter("cardType").toString();
			String clientRefId = request.getParameter("clientRefId").toString();
			String MId = request.getParameter("MId").toString();

			logger.info("AjaxAction.java ::: Token Ref ID to Remove is : " + tokenReferenceId+":::CRefID::"+clientRefId);

			CheckOutMaster CM = new CheckOutMaster();

			CM.setId(Id);
			CM.setTokenReferenceId(tokenReferenceId);
		
			CM.setModifiedBy(MId);
			CM.setModifiedOn(dateTime);
			CM.setMerchantId(MId);

			  
			 
			   String tokenStatus= null;
			   try {
			   ObjectMapper objectMapper = new ObjectMapper();
		      
		            Map<String, String> headerMap = new HashMap<String, String>();
		           
		           
		             headerMap.put("clientId", clientId);
		            headerMap.put("clientApiUser", clientApiUser);
		            headerMap.put("clientApiKey", clientApiKey);
		            headerMap.put("vaultId", vaultId);
		            headerMap.put("tokenSecretKey", tokenSecretKey); 
		            
			         TokenizeWrapper wrapper = new TokenizeWrapper(wibmoProperties);
  
			   if(cardType.equalsIgnoreCase("MasterCard")) {
				   JSONObject deleteTokenMC = new JSONObject();
				 //  deleteTokenMC.put("tokenReferenceId", tokenRefId.trim());
				   deleteTokenMC.put("causedBy", "CARDHOLDER");
				   deleteTokenMC.put("reason", "Lost/Stolen Device");
				   deleteTokenMC.put("reasonCode", "SUSPECTED_FRAUD");
				   deleteTokenMC.put("cardType", "M");
				   deleteTokenMC.put("merchantId",IdMerchant);
				   deleteTokenMC.put("clientReferenceId", clientRefId.trim());
				   
			
			            DeleteTokenRequest deleteRequest = objectMapper.readValue(deleteTokenMC.toString(), DeleteTokenRequest.class);
			            deleteRequest.setTokenReferenceId(tokenReferenceId.trim());
			            DeleteTokenResponse responseData = wrapper.deleteToken(deleteRequest, headerMap);
			            String deleteDataResponseStr = objectMapper.writeValueAsString(responseData);
			            logger.info(" Delete Token Response Master: " + deleteDataResponseStr);

			        	JSONObject json= new JSONObject(deleteDataResponseStr);
			        	tokenStatus= json.getString("tokenStatus");
			       
				
			   } else if(cardType.equalsIgnoreCase("Rupay")) {
				   JSONObject deleteTokenRupay = new JSONObject();
				   
				 //  deleteTokenRupay.put("tokenReferenceId", tokenRefId);
				   deleteTokenRupay.put("causedBy", "CARDHOLDER");
				   deleteTokenRupay.put("reason", "Lost/Stolen Device");
				   deleteTokenRupay.put("reasonCode", "SUSPECTED_FRAUD");
				   deleteTokenRupay.put("cardType", "R");
				   deleteTokenRupay.put("merchantId", IdMerchant);
				   deleteTokenRupay.put("clientReferenceId", clientRefId.trim());
				   
			
			            DeleteTokenRequest deleteRequest = objectMapper.readValue(deleteTokenRupay.toString(), DeleteTokenRequest.class);
			            deleteRequest.setTokenReferenceId(tokenReferenceId.trim());
			            DeleteTokenResponse responseData = wrapper.deleteToken(deleteRequest, headerMap);
			            String deleteDataResponseStr = objectMapper.writeValueAsString(responseData);
			            logger.info(" Delete Token Response Rupay: " + deleteDataResponseStr);     
			        	JSONObject json= new JSONObject(deleteDataResponseStr);
			        	tokenStatus= json.getString("tokenStatus");	
			   }
			   else if(cardType.equalsIgnoreCase("Visa")) {
				   JSONObject deleteTokenVisa = new JSONObject();
				   
				 //  deleteTokenVisa.put("tokenReferenceId", tokenRefId.trim());
				   deleteTokenVisa.put("causedBy", "CARDHOLDER");
				   deleteTokenVisa.put("reason", "Lost/Stolen Device");
				   deleteTokenVisa.put("reasonCode", "SUSPECTED_FRAUD");
				   deleteTokenVisa.put("cardType", "V");
				   deleteTokenVisa.put("merchantId", IdMerchant);

				   deleteTokenVisa.put("clientReferenceId", clientRefId.trim());
				   
			
			            DeleteTokenRequest deleteRequest = objectMapper.readValue(deleteTokenVisa.toString(), DeleteTokenRequest.class);
			            deleteRequest.setTokenReferenceId(tokenReferenceId.trim());
			            DeleteTokenResponse responseData = wrapper.deleteToken(deleteRequest, headerMap);
			            String deleteDataResponseStr = objectMapper.writeValueAsString(responseData);
			            logger.info("Delete Token Response Visa : " + deleteDataResponseStr);     
			        	JSONObject json= new JSONObject(deleteDataResponseStr);
			        	tokenStatus= json.getString("tokenStatus");	
			   }else if(cardType.equalsIgnoreCase("DinersClub")) {
				   JSONObject deleteTokenDiners = new JSONObject();
				   
					 //  deleteTokenVisa.put("tokenReferenceId", tokenRefId.trim());
					   deleteTokenDiners.put("causedBy", "CARDHOLDER");
					   deleteTokenDiners.put("reason", "Lost/Stolen Device");
					   deleteTokenDiners.put("reasonCode", "SUSPECTED_FRAUD");
					   deleteTokenDiners.put("cardType", "D");
					  deleteTokenDiners.put("merchantId", IdMerchant);

					   deleteTokenDiners.put("clientReferenceId", clientRefId.trim());
					   
				
				            DeleteTokenRequest deleteRequest = objectMapper.readValue(deleteTokenDiners.toString(), DeleteTokenRequest.class);
				            deleteRequest.setTokenReferenceId(tokenReferenceId.trim());
				            DeleteTokenResponse responseData = wrapper.deleteToken(deleteRequest, headerMap);
				            String deleteDataResponseStr = objectMapper.writeValueAsString(responseData);
				            logger.info("Delete Token Response Diners : " + deleteDataResponseStr);     
				        	JSONObject json= new JSONObject(deleteDataResponseStr);
				        	tokenStatus= json.getString("tokenStatus");	
				  }
		
			
		        CM.setToken_status(tokenStatus);
			status = dm.removeTokenizeCard(CM);

			logger.info("status::::"+status);
			if(status==1) {
				out.print("success");
			}
		
		        } catch (Exception e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        }
		}		
		out.flush();
		out.close();
	}
	
	
	
	public static String randomNumeric(int count) throws NoSuchAlgorithmException {
		final StringBuilder otp = new StringBuilder(count);
		 SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		for (int i = 0; i < count; i++) {
			otp.append(secureRandom.nextInt(9));
		}
		return otp.toString();
	}
}
