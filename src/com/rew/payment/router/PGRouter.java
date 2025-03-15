package com.rew.payment.router;

import com.apps.trides.crypto.AppsCrypto;
import com.google.gson.Gson;
import com.rew.payment.utility.PGUtils;
import com.rew.payment.utility.TripleKeyEnc;
import com.rew.pg.db.DataManager;
import com.rew.pg.db.MongoDBDataManager;
import com.rew.pg.dto.CheckOutMaster;
import com.rew.pg.dto.MerchantDTO;
import com.rew.pg.dto.RulesAccessDTO;
import com.rew.pg.dto.TransactionMaster;
import com.wibmo.tokenize.wrapper.model.*;
import com.wibmo.tokenize.wrapper.model.tokenize.*;
import com.wibmo.tokenize.wrapper.model.tokenize.TokenizeRequest;
import com.wibmo.tokenize.wrapper.model.tokenize.TokenizeResponse;
import com.wibmo.tokenize.wrapper.util.TokenizeHelper;
import com.wibmo.tokenize.wrapper.util.Utility;
import com.wibmo.tokenize.wrapper.impl.TokenizeWrapper;
import javax.crypto.spec.IvParameterSpec;

import org.bouncycastle.crypto.macs.CMac;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class PGRouter extends HttpServlet {
	private static Logger logger = LoggerFactory.getLogger(PGRouter.class);
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Get Method not allowed: ").append(request.getContextPath());
	}

	protected void doOption(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Option Method not Allowed.");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setHeader("Cache-control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "-1");
		String checksum = request.getParameter("checksum");
		logger.debug(validateChecksum(checksum) + "  " + checksum);
		if (validateChecksum(checksum)) {
			response.setHeader("checksum", checksum);
		} else {
			response.setHeader("checksum", "");
		}

		String rmsResult = null;
		String rmsReason = null;
		boolean iserror = false;
		String referer = PGUtils.getPropertyValue("referer");// "
		String referer1 = PGUtils.getPropertyValue("referer1"); //
		String referer2 = PGUtils.getPropertyValue("referer2");// 
		
		
		String wibmoProperties = PGUtils.getPropertyValue("wibmoConfig");// wibmo property file path
		
		/* wibmo header value */
		
		String clientId = PGUtils.getPropertyValue("clientId");
		String clientApiUser = PGUtils.getPropertyValue("clientApiUser");
		String clientApiKey = PGUtils.getPropertyValue("clientApiKey");
		String vaultId = PGUtils.getPropertyValue("vaultId");
		String tokenSecretKey = PGUtils.getPropertyValue("tokenSecretKey");
		String merchantId = PGUtils.getPropertyValue("merchantId");
		String deTokenSecretKey = PGUtils.getPropertyValue("deTokenSecretKey");
		
		HttpSession session = request.getSession();

		TransactionMaster TM = null;

		logger.debug(
				"Referer =" + request.getHeader("Referer") + "  ctoken=" + session.getAttribute("cToken").toString()
						+ "  checksum=" + request.getParameter("checksum").toString());

		if (request.getHeader("Referer") != null
				&& (request.getHeader("Referer").endsWith(referer) || request.getHeader("Referer").endsWith(referer1)
						|| request.getHeader("Referer").endsWith(referer2))
				&& session.getAttribute("cToken") != null && request.getParameter("checksum") != null
				&& session.getAttribute("cToken").toString().equals(request.getParameter("checksum").toString())) {

			String cardTypeVal = "NA";

			String txnId = request.getParameter("txnId");

			DataManager dataManager = new DataManager();
			DataManager db = dataManager;
			TM = db.getTxnMaster(txnId);
			String sCardNumber = null;
			String[] crnos = null;

			logger.debug("PGRouter.java :: fortxnid=" + TM.getId() + " response status=" + TM.getRespStatus()
					+ " Modified by " + TM.getModified_By());
			if (TM.getRespStatus().equalsIgnoreCase("99") && TM.getModified_By().equalsIgnoreCase("PGTransaction")) {

				String sInstrumentId = request.getParameter("instrumentId");
				String udf2 = TM.getUdf2();

				if (sInstrumentId != null && !sInstrumentId.equals("NB")) {
					cardTypeVal = request.getParameter("cardType").toString();
				}
				logger.debug("for id=" + TM.getTxnId() + " instrumentid=" + sInstrumentId + " udf2=" + udf2);
				if (request.getHeader("Referer").endsWith(referer1)) {
					if (udf2.equalsIgnoreCase("UPI")
							&& (!sInstrumentId.equalsIgnoreCase(udf2) || !cardTypeVal.equalsIgnoreCase(udf2))) {
						logger.debug("PGRouter.java ::: "
								+ PGUtils.generateLog(TM.getMerchantId(), TM.getTxnId(), TM.getId(), TM.getAmount()));
						logger.debug("PGRouter.java ::: for id=" + TM.getTxnId() + " paymode=" + udf2
								+ " doesnot equals upi  there is attempt of tempering");
						request.setAttribute("errorMsg", "Error 10052 : Error while Processing Transaction Request.");
						request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
						iserror = true;
						out.flush();
						out.close();
					} else if (udf2.equalsIgnoreCase("NOUPI")
							&& (sInstrumentId.equalsIgnoreCase("UPI") || cardTypeVal.equalsIgnoreCase("UPI"))) {
						logger.debug("PGRouter.java ::: "
								+ PGUtils.generateLog(TM.getMerchantId(), TM.getTxnId(), TM.getId(), TM.getAmount()));
						logger.debug("PGRouter.java ::: for id=" + TM.getTxnId() + " paymode=" + udf2
								+ "  equals upi  there is attempt of tempering");
						request.setAttribute("errorMsg", "Error 10052 : Error while Processing Transaction Request.");
						request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
						iserror = true;
						out.flush();
						out.close();
					}

				}
				logger.debug("iserror = " + iserror);

				if (!iserror) {
					String emioption = request.getParameter("emioption");

					logger.debug("emioption" + emioption);
					if (null == emioption || emioption == "") {
						emioption = "NA";
					}
					logger.debug("PGRouter.java ::: inside PGRouter for txnId=" + TM.getId() + " instrumentId="
							+ sInstrumentId + "  emioption=" + emioption);

					String bankId = null;

					if ((sInstrumentId != null) && (sInstrumentId.equals("NB"))) {
						bankId = request.getParameter("bank");
					} else {
						//cardTypeVal="Rupay";
						if (cardTypeVal.equalsIgnoreCase("Rupay")) {
							bankId = "Rupay";
						} else if (cardTypeVal.equalsIgnoreCase("Amex")) {
							bankId = "Amex";
						} else if (cardTypeVal.equalsIgnoreCase("UPI") || cardTypeVal.equalsIgnoreCase("INTENT")) {
							bankId = "UPI";
						} else if (cardTypeVal.equalsIgnoreCase("ATOMUPI")) {
							bankId = "ATOMUPI";
						} else if (cardTypeVal.equalsIgnoreCase("WALLET")) {
							bankId = request.getParameter("bank");
						} else if (emioption.contains("EMI")) {
							bankId = emioption;
						} else {
							bankId = "FINPG";
						}

						logger.debug("PGRouter.java ::: for txnId=" + TM.getId() + " cardtype=" + cardTypeVal
								+ " BankId=" + bankId);

					}

					if (bankId.equalsIgnoreCase("Rupay") || bankId.equalsIgnoreCase("FINPG")
							|| emioption.contains("EMI") || bankId.equalsIgnoreCase("Amex")) {

						logger.debug("PGRouter.java ::: for txnId="
								+ TM.getId() /*+" cardno="+request.getParameter("cardNo").toString()*/ );

						if(request.getParameter("cardNo")!= null) {
							sCardNumber = PGUtils.cryptoJs(request.getParameter("cardNo").toString());
							if (sCardNumber == null) {
								logger.debug("PGRouter.java ::: " + PGUtils.generateLog(TM.getMerchantId(), TM.getTxnId(),
										TM.getId(), TM.getAmount()));
								logger.debug("PGRouter.java :::  encrypted cardno is null  ");
								request.setAttribute("errorMsg",
										"Error 10052 : Error while Processing Transaction Request.");
								request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);

							out.flush();
							out.close();
						} else {
							crnos = sCardNumber.split("\\|");
						}

						if (crnos != null && crnos.length > 1) {
							sCardNumber = crnos[0].replace(" ", "").substring(0, 6);

							}
						}					

						logger.debug("===========before bin validation=============for txnid=" + TM.getId()
								+ " instrumentid=" + sInstrumentId + " crno=" + sCardNumber);
						// logger.debug("cardNo >>>>>>>>>>>>>>>>>>>>>>>> "+crNo);

						String ctype = db.getBinvalidation(TM.getId(), sCardNumber, sInstrumentId); // calling procedure
																									// bin validation to
																									// check correct CC
																									// or DC
						// TM=db.getTxnMaster(txnId);
						TM.setInstrumentId(ctype);

					} else {

						TM.setInstrumentId(sInstrumentId);
						// db.updateTxn(TM);
					}

					logger.debug("==========after bin validation===========for txnid=" + TM.getId() + " instrumentid = "
							+ TM.getInstrumentId());

					// logger.debug("txnId <<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>> " + txnId);
					// TM = db.getTxnMaster(txnId);

					sInstrumentId = TM.getInstrumentId();

					String sClassFileLoader = null;
					String spId = null, retamount = null;

					int payout = 0;
					logger.debug("bankId :" + bankId);
					logger.debug("sInstrumentId :" + sInstrumentId);
					logger.debug("Amount :" + TM.getAmount());
					logger.debug("Merchant Id :" + TM.getMerchantId());
					logger.debug("emioption :" + emioption);
					logger.debug("Id :" + TM.getId());
					List<String> surchargeData = db.getCalculateSurcharge(bankId, sInstrumentId, TM.getAmount(),
							TM.getMerchantId(), emioption, TM.getId());

					retamount = surchargeData.get(0);
					sClassFileLoader = surchargeData.get(1);
					spId = surchargeData.get(2);
					payout = Integer.valueOf(surchargeData.get(3));

					logger.debug("sCardNumber" + sCardNumber);

					boolean isHdfcCard = false;

					List<String> surchargeHDFCData = db.getSurchargeForHDFCCards(bankId, sInstrumentId, TM.getAmount(),
							TM.getMerchantId(), emioption, TM.getId());

					if (sCardNumber != null && !sCardNumber.isEmpty()) {
						isHdfcCard = db.isHDFCCard(sCardNumber.substring(0, 6));
					}
					logger.debug("ishdfcCard" + isHdfcCard);
					logger.debug("before retamount " + retamount + "payout :" + payout);
					if (isHdfcCard && surchargeHDFCData.size() > 0) {
						logger.debug("hdfc discounted mdr ");
						retamount = surchargeHDFCData.get(0);
						payout = Integer.valueOf(surchargeHDFCData.get(1));
					}
					logger.debug("before retamount " + retamount + "payout :" + payout);

					logger.debug("PGRouter.java ::: for txnId=" + TM.getId() + " spId=" + spId + " sClassFileLoader="
							+ sClassFileLoader + " retamount=" + retamount);

					TM.setInstrumentId(sInstrumentId);
					TM.setRespStatus("0");
					TM.setServiceRRN("NA");
					TM.setServiceTxnId("NA");
					TM.setServiceAuthId("NA");
					TM.setRespMessage("NA");
					TM.setProcessId(spId);
					TM.setSurcharge(retamount);
					TM.setModified_By("PGRouter");
					TM.setModified_On(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					TM.setCardName("NA");
					
					String customerId=null;
					if(request.getParameter("customerId")!= null) {
						customerId= request.getParameter("customerId");
					}
					String maskedCardNo = null;
					String sExpiry = null;

					if ((sInstrumentId != null) && (sInstrumentId.equalsIgnoreCase("DC"))
							|| (sInstrumentId.equalsIgnoreCase("CC") || (sInstrumentId.equalsIgnoreCase("Amex")))) {
						try {
							logger.debug("PGRouter.java ::: " + PGUtils.generateLog(TM.getMerchantId(), TM.getTxnId(),
									TM.getId(), TM.getAmount()));
							logger.debug("PGRouter.java ::: Merchant Id : " + TM.getMerchantId()
									+ " :: IP host Address : " + TM.getHostAddress() + " :: Amonut : " + TM.getAmount()
									+ " :: Instrument : " + TM.getInstrumentId());

							// TransactionValidator tv = new TransactionValidator();

							// String sRe1 = PGUtils.getPropertyValue("rmsUrl");//calculating rms

							// String URL = sRe1 + "?type=rms&merchantId=" + TM.getMerchantId() +
							// "&ipAddress=" + TM.getHostAddress() + "&cardNo=" + sCardNumber + "&txtAmt=" +
							// TM.getAmount() + "&cardType=" + TM.getInstrumentId();

							// String respStatus = tv.getValidatetrans(URL);

							JSONObject obj = new JSONObject();
							obj.put("Result", "0");
							obj.put("Reasons", "NA");
						
							if (obj != null) {
								logger.debug("PGRouter.java ::: " + PGUtils.generateLog(TM.getMerchantId(),
										TM.getTxnId(), TM.getId(), TM.getAmount()));
								// logger.debug("PGRouter.java ::: Encrypted Card Details : " +
								// request.getParameter("cardNo") + " , " + request.getParameter("expiry"));

								// sCardNumber = PGUtils.cryptoJs(request.getParameter("cardNo").toString());
								// String[] args = sCardNumber.split("\\|");

								if (crnos != null && (crnos.length > 1) && (txnId.equals(crnos[1]))) {
									sCardNumber = crnos[0];
									sExpiry = PGUtils.cryptoJs(request.getParameter("expiry").toString());
									// logger.debug("expiry:::::::"+sExpiry);
									// logger.debug("cardType"+request.getParameter("cardType"));
									// logger.debug("cardCVV:::::::::"+request.getParameter("cardCVV"));
									String regex = "\\s";
									sCardNumber = sCardNumber.replaceAll(regex, "").trim();
									maskedCardNo = PGUtils.maskCardNumber(sCardNumber);

								}

								logger.info("mobileNo::::"+request.getParameter("mobileNo")); 
								logger.info("emailId:::"+ request.getParameter("emailId"));
								
								String tokenrefId= null;
								String tokenexpiry= null;
								String cryptogram= null;
								String panlast4= null;
								JSONObject jsonResp= null;
								String wibmoPaymentDataResponseStr = null;
								String TokenCardNumber= null; 
							
								JSONObject getTokenDataRequest = new JSONObject();
								getTokenDataRequest.put("amount", TM.getAmount());
								getTokenDataRequest.put("currency", "356");
								getTokenDataRequest.put("clientReferenceId", TM.getId());
								getTokenDataRequest.put("merchantId", merchantId);   
						
								
								  Map<String, String> headerMap = new HashMap<String, String>();

								
								
								  		headerMap.put("clientId", clientId);
							            headerMap.put("clientApiUser", clientApiUser);
							            headerMap.put("clientApiKey", clientApiKey);
							            headerMap.put("vaultId", vaultId);
							            headerMap.put("tokenSecretKey", tokenSecretKey); 
							            
							        /*       headerMap.put("clientId", "66f8bded-365c-4ec5-a469-80e73844f339");
							            headerMap.put("clientApiUser", "100001-PAY-1fE4xL8tU9");
							            headerMap.put("clientApiKey", "PAY3pD1zV8uO7");
							            headerMap.put("vaultId", "100001");
							            headerMap.put("tokenSecretKey", "eb099c8b-2837-4e60-9f8c-ecb460aa1c33");  */
							
							         if(request.getParameter("tokenRefId")!=null && request.getParameter("expiry")!= null && request.getParameter("panlast4")!= null && !cardTypeVal.equalsIgnoreCase("DinersClub")) {
								  
							            
									 ObjectMapper Mapper = new ObjectMapper();
								     
							          TokenDataRequest wibmoPaymentDataRequest = Mapper.readValue(getTokenDataRequest.toString(), TokenDataRequest.class);
								        wibmoPaymentDataRequest.setTokenReferenceId(request.getParameter("tokenRefId").toString().trim());
								      if(cardTypeVal.equalsIgnoreCase("Visa")) {
								    	  wibmoPaymentDataRequest.setCardType("V");
								      }else  if(cardTypeVal.equalsIgnoreCase("MasterCard") || cardTypeVal.equalsIgnoreCase("Maestro")) {
								    	  wibmoPaymentDataRequest.setCardType("M");
								      }else  if(cardTypeVal.equalsIgnoreCase("Rupay")) {
								    	  wibmoPaymentDataRequest.setCardType("R");
								      }
								 
								  //    logger.info("wibmoProperties::::"+wibmoProperties);
								      TokenizeWrapper wrapper = new TokenizeWrapper(wibmoProperties);
							            TokenDataResponse tokenDataResponse = wrapper.getTokenData(wibmoPaymentDataRequest, headerMap);
							            wibmoPaymentDataResponseStr = Mapper.writeValueAsString(tokenDataResponse);
							            logger.info("Get Payment Data Response: " + wibmoPaymentDataResponseStr);
							            jsonResp= new JSONObject(wibmoPaymentDataResponseStr);
							            
							            TokenCardNumber = Utility.getAccountDetails(tokenDataResponse.getTokenInfo().getEncTokenInfo(),
								  				  (String)headerMap.get("tokenSecretKey"),
								  				  Utility.stringToByteArray(tokenDataResponse.getTokenInfo().getIv(), "HEX"));
									
									tokenrefId= request.getParameter("tokenRefId").toString();
									tokenexpiry= request.getParameter("expiry").toString();
									panlast4= request.getParameter("panlast4").toString();
									cryptogram= ((new JSONObject(jsonResp.get("cryptogramInfo").toString())).get("cryptogram")).toString();
									
									request.setAttribute("tokenReferenceId", tokenrefId);
									request.setAttribute("tokenExpiry", tokenexpiry);
									request.setAttribute("lastDigit", panlast4);									
									request.setAttribute("cryptogram", cryptogram);
									request.setAttribute("tokenCardNumber", TokenCardNumber.trim().toString());
								
									//logger.info("tokenrefId=" + tokenrefId + " expiry" + tokenexpiry.substring(0, 2)+" crypto"+ cryptogram+" panlast4"+ panlast4 );

								}else if(request.getParameter("tokenRefId")!=null && request.getParameter("expiry")!= null && request.getParameter("panlast4")!= null && cardTypeVal.equalsIgnoreCase("DinersClub")) {
									tokenrefId= request.getParameter("tokenRefId").toString();
									tokenexpiry= request.getParameter("expiry").toString();
									panlast4= request.getParameter("panlast4").toString();
									//logger.info("tokenrefId::::" + tokenrefId);
									
									request.setAttribute("tokenReferenceId", tokenrefId);
									request.setAttribute("tokenExpiryMM", tokenexpiry.substring(0, 2));
									request.setAttribute("tokenExpiryYY",tokenexpiry.substring(2));
									request.setAttribute("lastDigit", panlast4);
								
									JSONObject deTokenizeRequestJson = new JSONObject();
									deTokenizeRequestJson.put("cardType", "D");
									deTokenizeRequestJson.put("clientReferenceId", TM.getId());
									deTokenizeRequestJson.put("merchantId", merchantId);
								//	deTokenizeRequestJson.put("merchantId", "INMUM0111");

									 final ObjectMapper objectMapper = new ObjectMapper();
								       try {
								           final Map<String, String> headerMapDiners = new HashMap<String, String>();
								       
								        
								           logger.info("deTokenSecretKey::::"+deTokenSecretKey);   
								         headerMapDiners.put("clientId", clientId);
								           headerMapDiners.put("clientApiUser", clientApiUser);
								           headerMapDiners.put("clientApiKey", clientApiKey);
								           headerMapDiners.put("vaultId", vaultId); 
									       headerMapDiners.put("deTokenSecretKey", deTokenSecretKey);
									      

								        //      TokenizeWrapper wrapper = new TokenizeWrapper("D:\\PAYPG\\Wibmo\\application.properties");
										       TokenizeWrapper wrapper = new TokenizeWrapper(wibmoProperties);
   
								            DeTokenRequest deTokenRequest = (DeTokenRequest)objectMapper.readValue(deTokenizeRequestJson.toString(), (Class)DeTokenRequest.class);
								            deTokenRequest.setTokenReferenceId(tokenrefId.toString().trim());
								            DeTokenizePCIData deTokenizePCIDataResponse = wrapper.deTokenize(deTokenRequest, (Map)headerMapDiners);
								            String deTokenizePCIDataResponseStr = objectMapper.writeValueAsString((Object)deTokenizePCIDataResponse);
								            String iv = deTokenizePCIDataResponse.getIv();
								       //    logger.info(" DeTokenize : " + deTokenizePCIDataResponseStr);
								            String DecryptedString=  Utility.getAccountDetails(deTokenizePCIDataResponse.getEncryptedAccountData(), (String)headerMapDiners.get("deTokenSecretKey"), Utility.stringToByteArray(iv, "HEX"));

								            JSONObject jsonObject= new JSONObject(DecryptedString);
								            jsonObject.getString("pan");
								            jsonObject.getString("expiryMonth");
								            jsonObject.getString("expiryYear");
								        									            
								            
								    		String encCard = PGUtils.encryptcryptoJs(jsonObject.getString("pan")+"|"+txnId);
								    		String expiryMonth = PGUtils.encryptcryptoJs( jsonObject.getString("expiryMonth"));
								    		String expiryYear = PGUtils.encryptcryptoJs( jsonObject.getString("expiryYear"));
										
								    		request.setAttribute("cardNo", encCard);
								    		request.setAttribute("expiryMonth", expiryMonth);
								    		request.setAttribute("expiryYear", expiryYear);
								       }
								       catch (Exception e) {
								           e.printStackTrace();
								       }
								}
								
							         if ((request.getParameter("isRemember") != null)
												&& (request.getParameter("isRemember").equalsIgnoreCase("Y"))) {
											CheckOutMaster CM = new CheckOutMaster();
											String keyEncKey = request.getParameter("keyEncKey").toString();
											String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
											try {
												String secretKey = null;
												String encSecretKey = null;
												encSecretKey = db.checkSecretKey(TM.getCustMail(), TM.getCustMobile());

												if ((encSecretKey == null) || (encSecretKey.equals(""))) {
													secretKey = AppsCrypto.genetatyeKey();
													encSecretKey = TripleKeyEnc.encode(secretKey, keyEncKey);
												} else {
													secretKey = TripleKeyEnc.decode(encSecretKey, keyEncKey);
												}
												logger.debug("PGRouter.java ::: " + PGUtils.generateLog(TM.getMerchantId(),
														TM.getTxnId(), TM.getId(), TM.getAmount()));
												logger.debug("Secret Key is : " + secretKey + " And Encrypted Secret Key is : "
														+ encSecretKey);
												if ((secretKey != null) && (!secretKey.equals(""))) {
													String encCardNo = AppsCrypto.encrypt(sCardNumber, secretKey);

													CM.setMerchantId(TM.getMerchantId());
													CM.setCardNo(encCardNo);
													CM.setExpiryDate(sExpiry);
													CM.setCardName(request.getParameter("cardName").toString());
													CM.setCardType(request.getParameter("cardType").toString());
													CM.setInstrumentId(sInstrumentId);
													CM.setCustMail(TM.getCustMail());
													CM.setCustMobile(TM.getCustMobile());
													CM.setStatus("A");
													CM.setInsertedOn(dateTime);
													CM.setInsertedBy(TM.getMerchantId());
													CM.setModifiedOn(dateTime);
													CM.setModifiedBy(TM.getMerchantId());
													CM.setSecretKey(encSecretKey);

													db.saveCardDetails(CM);
													logger.debug("PGRouter.java ::: " + PGUtils.generateLog(TM.getMerchantId(),
															TM.getTxnId(), TM.getId(), TM.getAmount()));
													logger.debug("Save Card Details Procedure Executed Successfully");
												}
												
												
												
											} catch (Exception e1) {
												logger.error("PGRouter.java :::for id =" + TM.getTxnId()
														+ " Error in Card Saving : ", e1);
											}

										}



   
							         
								if ((request.getParameter("isRememberVAS") != null)
										&& (request.getParameter("isRememberVAS").equalsIgnoreCase("Y"))) {
									CheckOutMaster CM = new CheckOutMaster();
									String keyEncKey = request.getParameter("keyEncKey").toString();
									String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
									try {
										String secretKey = null;
										String encSecretKey = null;
										encSecretKey = db.checkSecretKey(TM.getCustMail(), TM.getCustMobile());

										if ((encSecretKey == null) || (encSecretKey.equals(""))) {
											secretKey = AppsCrypto.genetatyeKey();
											encSecretKey = TripleKeyEnc.encode(secretKey, keyEncKey);
										} else {
											secretKey = TripleKeyEnc.decode(encSecretKey, keyEncKey);
										}
										logger.debug("PGRouter.java ::: " + PGUtils.generateLog(TM.getMerchantId(),
												TM.getTxnId(), TM.getId(), TM.getAmount()));
										logger.debug("Secret Key is : " + secretKey + " And Encrypted Secret Key is : "
												+ encSecretKey);
										if ((secretKey != null) && (!secretKey.equals(""))) {
											
											String cardType= request.getParameter("cardType").toString();
											
											JSONObject tokenizeRequestJson = new JSONObject();
											tokenizeRequestJson.put("provider", "NETWORK");
											tokenizeRequestJson.put("userConsent", "Y");
											tokenizeRequestJson.put("userConsentTimestamp", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
											tokenizeRequestJson.put("clientReferenceId", TM.getId());
										
											 String tokenizeResponseStr= null;
										
										
											 JSONObject jsonReq= null;
											 ObjectMapper objectMapper = new ObjectMapper();
										      
											if(cardType.equalsIgnoreCase("Visa")) {
												JSONObject accountDetailsVisa = new JSONObject();
												
												accountDetailsVisa.put("pan", sCardNumber);
												accountDetailsVisa.put("expiryMonth", sExpiry.toString().substring(0,2));
												accountDetailsVisa.put("expiryYear", "20"+sExpiry.toString().substring(3));
												accountDetailsVisa.put("customerName", request.getParameter("cardName").toString());
											//	accountDetailsVisa.put("address1","");
											//	accountDetailsVisa.put("address2", "");
											//	accountDetailsVisa.put("postalCode", "");
											//	accountDetailsVisa.put("city", "");
											//	accountDetailsVisa.put("state", "");
												accountDetailsVisa.put("countryCode", "Ind");
												accountDetailsVisa.put("email", TM.getCustMail());
												accountDetailsVisa.put("mobile", TM.getCustMobile());
												accountDetailsVisa.put("source", "CARD");
												accountDetailsVisa.put("cardType", "V");
												accountDetailsVisa.put("merchantId", merchantId);

												
												logger.info(" Tokenize Request VISA PAY: " + tokenizeRequestJson);
												CustomerAccountDetails accountDetails = objectMapper.readValue(accountDetailsVisa.toString(), CustomerAccountDetails.class);
												String IV = Utility.generateIVString();
												TokenizeRequest tokenizeRequest = objectMapper.readValue(tokenizeRequestJson.toString(), TokenizeRequest.class);
												tokenizeRequest.setIv(IV);
												tokenizeRequest.setCardType("V");
												tokenizeRequest.setMerchantId(merchantId);
											

										        TokenizeWrapper wrapper = new TokenizeWrapper(wibmoProperties);

												TokenizeResponse tokenResponse = wrapper.tokenize(tokenizeRequest,accountDetails, headerMap);
										         TokenizeHelper helper = new TokenizeHelper();
										         tokenizeResponseStr = objectMapper.writeValueAsString(tokenResponse);
										         logger.info("Tokenize Response VISA PAY : " + tokenizeResponseStr);
										           
										         
											     jsonReq= new JSONObject(tokenizeResponseStr);
											 
											 		
											     if(jsonReq.get("statusCode").toString().equalsIgnoreCase("TK0000")) {
											    	   ObjectMapper Mapper = new ObjectMapper();
														
											            TokenDataRequest wibmoPaymentDataRequest = Mapper.readValue(getTokenDataRequest.toString(), TokenDataRequest.class);
												        wibmoPaymentDataRequest.setTokenReferenceId(jsonReq.get("tokenReferenceId").toString());
												        wibmoPaymentDataRequest.setCardType("V");
											            TokenDataResponse tokenDataResponse = wrapper.getTokenData(wibmoPaymentDataRequest, headerMap);
											            wibmoPaymentDataResponseStr = Mapper.writeValueAsString(tokenDataResponse);
											            logger.info("Get Payment Data Response Visa : " + wibmoPaymentDataResponseStr);
											            jsonResp= new JSONObject(wibmoPaymentDataResponseStr);
											            
											            TokenCardNumber = Utility.getAccountDetails(tokenDataResponse.getTokenInfo().getEncTokenInfo(),
												  				  (String)headerMap.get("tokenSecretKey"),
												  				  Utility.stringToByteArray(tokenDataResponse.getTokenInfo().getIv(), "HEX"));
													 
												 } 
											     
											    /* else {
													 request.setAttribute("errorMsg",
																"Error :"+jsonReq.get("statusCode").toString()+"- "+ jsonReq.get("msg").toString());

													 request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response); 
													 out.flush();
													 out.close();
												 } */
												         
											      				
											   }else if(cardType.equalsIgnoreCase("MasterCard") || cardType.equalsIgnoreCase("Maestro")) {
												   
												   JSONObject accountDetailsMaster = new JSONObject();
													
												   accountDetailsMaster.put("pan", sCardNumber);
													accountDetailsMaster.put("expiryMonth", sExpiry.toString().substring(0,2));
													accountDetailsMaster.put("expiryYear", "20"+sExpiry.toString().substring(3));
													accountDetailsMaster.put("customerName", request.getParameter("cardName").toString());
													accountDetailsMaster.put("address1","");
													accountDetailsMaster.put("address2", "");
													accountDetailsMaster.put("postalCode", "");
													accountDetailsMaster.put("city", "");
													accountDetailsMaster.put("state", "");
													accountDetailsMaster.put("countryCode", "Ind");
													accountDetailsMaster.put("email", TM.getCustMail());
													accountDetailsMaster.put("mobile", TM.getCustMobile());
													accountDetailsMaster.put("source", "CARD");
													accountDetailsMaster.put("cardType", "M");
													accountDetailsMaster.put("merchantId", merchantId);
													accountDetailsMaster.put("merchantName", "");
													
													   logger.info(" Tokenize Request Master PAY: " + tokenizeRequestJson);
											            CustomerAccountDetails accountDetails = objectMapper.readValue(accountDetailsMaster.toString(), CustomerAccountDetails.class);
											            String IV = Utility.generateIVString();
											            TokenizeRequest tokenizeRequest = objectMapper.readValue(tokenizeRequestJson.toString(), TokenizeRequest.class);
											            tokenizeRequest.setIv(IV);
											            tokenizeRequest.setCardType("M");
														tokenizeRequest.setMerchantId(merchantId);
												         TokenizeWrapper wrapper = new TokenizeWrapper(wibmoProperties);

											            TokenizeResponse tokenResponse = wrapper.tokenize(tokenizeRequest,accountDetails, headerMap);
											            TokenizeHelper helper = new TokenizeHelper();
											            tokenizeResponseStr = objectMapper.writeValueAsString(tokenResponse);
											            logger.info("Tokenize Response Master PAY : " + tokenizeResponseStr);
											                  
														 jsonReq= new JSONObject(tokenizeResponseStr);
												
													
													 
														
														if(jsonReq.get("statusCode").toString().equalsIgnoreCase("TK0000")) {
														
															
															  Thread.sleep(10000);
															   ObjectMapper Mapper = new ObjectMapper();
																
															
													            TokenDataRequest wibmoPaymentDataRequest = Mapper.readValue(getTokenDataRequest.toString(), TokenDataRequest.class);
														    
													           wibmoPaymentDataRequest.setTokenReferenceId(jsonReq.get("tokenReferenceId").toString());
														        wibmoPaymentDataRequest.setCardType("M");
													            TokenDataResponse tokenDataResponse = wrapper.getTokenData(wibmoPaymentDataRequest, headerMap);
													            wibmoPaymentDataResponseStr = Mapper.writeValueAsString(tokenDataResponse);
													            logger.info("Get Payment Data Response Master: " + wibmoPaymentDataResponseStr); 
													            jsonResp= new JSONObject(wibmoPaymentDataResponseStr);
															 
													             TokenCardNumber = Utility.getAccountDetails(tokenDataResponse.getTokenInfo().getEncTokenInfo(),
														  				  (String)headerMap.get("tokenSecretKey"),
														  				  Utility.stringToByteArray(tokenDataResponse.getTokenInfo().getIv(), "HEX"));
													            
													            
														 } /* else {
															 request.setAttribute("errorMsg",
																		"Error :"+jsonReq.get("statusCode").toString()+"- "+ jsonReq.get("msg").toString());

															 request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response); 
															 out.flush();
															 out.close();
														 } */
												     
												        
												   }else if(cardType.equalsIgnoreCase("Rupay")) {
													   
													   JSONObject accountDetailsRupay = new JSONObject();
														
													   	accountDetailsRupay.put("pan", sCardNumber);
														accountDetailsRupay.put("expiryMonth", sExpiry.toString().substring(0,2));
														accountDetailsRupay.put("expiryYear", "20"+sExpiry.toString().substring(3));
														accountDetailsRupay.put("customerName", request.getParameter("cardName").toString());
														accountDetailsRupay.put("address1","");
														accountDetailsRupay.put("address2", "");
														accountDetailsRupay.put("postalCode", "");
														accountDetailsRupay.put("city", "");
														accountDetailsRupay.put("state", "");
														accountDetailsRupay.put("countryCode", "Ind");
														accountDetailsRupay.put("email", TM.getCustMail());
														accountDetailsRupay.put("mobile", TM.getCustMobile());
														accountDetailsRupay.put("source", "CARD");
														accountDetailsRupay.put("cardType", "R");
														accountDetailsRupay.put("merchantId", merchantId);
														accountDetailsRupay.put("merchantName", "");
														
														logger.info(" Tokenize Request Rupay : " + tokenizeRequestJson);
													    CustomerAccountDetails accountDetails = objectMapper.readValue(accountDetailsRupay.toString(), CustomerAccountDetails.class);
													    String IV = Utility.generateIVString();
													    TokenizeRequest tokenizeRequest = objectMapper.readValue(tokenizeRequestJson.toString(), TokenizeRequest.class);
													    tokenizeRequest.setIv(IV);
													    tokenizeRequest.setCardType("R");
														tokenizeRequest.setMerchantId(merchantId);
												         TokenizeWrapper wrapper = new TokenizeWrapper(wibmoProperties);

													    TokenizeResponse tokenResponse = wrapper.tokenize(tokenizeRequest,accountDetails, headerMap);
											            tokenizeResponseStr = objectMapper.writeValueAsString(tokenResponse);
											            logger.info("Tokenize Response Rupay: " + tokenizeResponseStr);    
											       	 	jsonReq= new JSONObject(tokenizeResponseStr);      

													

													   	if(jsonReq.get("statusCode").toString().equalsIgnoreCase("TK0000")) {
													   	 ObjectMapper Mapper = new ObjectMapper();
													     
												          TokenDataRequest wibmoPaymentDataRequest = Mapper.readValue(getTokenDataRequest.toString(), TokenDataRequest.class);
													        wibmoPaymentDataRequest.setTokenReferenceId(jsonReq.get("tokenReferenceId").toString());
													        wibmoPaymentDataRequest.setCardType("R");
												            TokenDataResponse tokenDataResponse = wrapper.getTokenData(wibmoPaymentDataRequest, headerMap);
												            wibmoPaymentDataResponseStr = Mapper.writeValueAsString(tokenDataResponse);
												            logger.info("Get Payment Data Response Rupay: " + wibmoPaymentDataResponseStr);
												            jsonResp= new JSONObject(wibmoPaymentDataResponseStr);
												            
												            TokenCardNumber = Utility.getAccountDetails(tokenDataResponse.getTokenInfo().getEncTokenInfo(),
													  				  (String)headerMap.get("tokenSecretKey"),
													  				  Utility.stringToByteArray(tokenDataResponse.getTokenInfo().getIv(), "HEX"));
															 
														 } /*else {
															 request.setAttribute("errorMsg",
																		"Error :"+jsonReq.get("statusCode").toString()+"- "+ jsonReq.get("msg").toString());

															 request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response); 
															 out.flush();
															 out.close(); 
														 }*/
													  
												}else if(cardType.equalsIgnoreCase("DinersClub")) {
													   
													   JSONObject accountDetailsDiners = new JSONObject();
														
													   	accountDetailsDiners.put("pan", sCardNumber);
														accountDetailsDiners.put("expiryMonth", sExpiry.toString().substring(0,2));
														accountDetailsDiners.put("expiryYear", "20"+sExpiry.toString().substring(3));
														accountDetailsDiners.put("customerName", request.getParameter("cardName").toString());
														accountDetailsDiners.put("address1","");
														accountDetailsDiners.put("address2", "");
														accountDetailsDiners.put("postalCode", "");
														accountDetailsDiners.put("city", "");
														accountDetailsDiners.put("state", "");
														accountDetailsDiners.put("countryCode", "Ind");
														accountDetailsDiners.put("email", TM.getCustMail());
														accountDetailsDiners.put("mobile", TM.getCustMobile());
														accountDetailsDiners.put("source", "CARD");
														accountDetailsDiners.put("cardType", "D");
														accountDetailsDiners.put("merchantId", merchantId);

														accountDetailsDiners.put("merchantName", "");
														
														logger.info("Tokenize Request Diners : " + tokenizeRequestJson);
													    CustomerAccountDetails accountDetails = objectMapper.readValue(accountDetailsDiners.toString(), CustomerAccountDetails.class);
													    String IV = Utility.generateIVString();
													    TokenizeRequest tokenizeRequest = objectMapper.readValue(tokenizeRequestJson.toString(), TokenizeRequest.class);
													    tokenizeRequest.setIv(IV);
													    tokenizeRequest.setCardType("D");
														tokenizeRequest.setMerchantId(merchantId);
												         TokenizeWrapper wrapper = new TokenizeWrapper(wibmoProperties);

														TokenizeResponse tokenResponse = wrapper.tokenize(tokenizeRequest,accountDetails, headerMap);
											            tokenizeResponseStr = objectMapper.writeValueAsString(tokenResponse);
											            logger.info("Tokenize Response Diners: " + tokenizeResponseStr);    
											       	 	jsonReq= new JSONObject(tokenizeResponseStr);      


													 /*  	if(!jsonReq.get("statusCode").toString().equalsIgnoreCase("TK0000")) {
													   	 request.setAttribute("errorMsg",
																	"Error :"+jsonReq.get("statusCode").toString()+"- "+ jsonReq.get("msg").toString());

														 request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response); 
														 out.flush();
														 out.close();
															 
														 } */
													  
												}
										
									
										//	String encCardNo = AppsCrypto.encrypt(sCardNumber, secretKey);
											Vector<List<String>>  storedCardDetail= new DataManager().getStoredCardsVAS(request.getParameter("emailId"), request.getParameter("mobileNo"),  TM.getMerchantId());
											
											if(storedCardDetail.size()>0){
											
												customerId=	storedCardDetail.get(0).get(15);
											}else {
												customerId= new SimpleDateFormat("yyDDDHHmmsss").format(new Date());
											}
											logger.info("customerId1:::"+customerId);
											CM.setMerchantId(TM.getMerchantId());
											CM.setCardNo("");
											CM.setExpiryDate("");
											CM.setCardName(request.getParameter("cardName").toString());
											CM.setCardType(request.getParameter("cardType").toString());
											CM.setInstrumentId(sInstrumentId);
											CM.setCustMail(request.getParameter("emailId"));
											CM.setCustMobile(request.getParameter("mobileNo"));
											CM.setStatus("A");
											CM.setInsertedOn(dateTime);
											CM.setInsertedBy(TM.getMerchantId());
											CM.setModifiedOn(dateTime);
											CM.setModifiedBy(TM.getMerchantId());
											CM.setSecretKey(encSecretKey);
											CM.setStatusCode(jsonReq.get("statusCode").toString());
											CM.setErrorDesc(jsonReq.get("errorDesc").toString());
											CM.setTokenStatus(jsonReq.get("status").toString());
											CM.setMessage(jsonReq.get("msg").toString());
											CM.setTransactionId(jsonReq.get("transactionId").toString());
											CM.setVar1(jsonReq.get("var1").toString());
											CM.setVar2(jsonReq.get("var2").toString());
											CM.setVar3(jsonReq.get("var3").toString());
											CM.setClientReferenceId(jsonReq.get("clientReferenceId").toString());
											CM.setTokenReferenceId(jsonReq.get("tokenReferenceId").toString());
											CM.setTokenLast4(jsonReq.get("tokenLast4").toString());
											CM.setTokenExpiryDate(jsonReq.get("tokenExpiryDate").toString());
											CM.setPanLast4(jsonReq.get("panLast4").toString());
											CM.setToken_status(jsonReq.get("tokenStatus").toString());
											CM.setEncTokenInfo(jsonReq.get("encTokenInfo").toString());
											CM.setPaymentAccountReference(jsonReq.get("paymentAccountReference").toString());
											CM.setTokenAssetId(jsonReq.get("tokenAssetId").toString());		
											CM.setPanReferenceId(jsonReq.get("panReferenceId").toString());
											CM.setProvider(jsonReq.get("provider").toString());
											CM.setIv(jsonReq.get("iv").toString());
											CM.setMappedTokenBin(jsonReq.get("mappedTokenBin").toString());
											CM.setCustomerId(customerId);
											
										
											
											if(jsonReq.get("statusCode").toString().equalsIgnoreCase("TK0000") && cardType.equalsIgnoreCase("DinersClub") )
											{
												db.saveCardDetailsTokenize(CM);
												
												String mobileNo = request.getParameter("mobileNo");
												String emailId = request.getParameter("emailId");

										
											       Cookie ck1=new Cookie("mobileNo",mobileNo);  
											       Cookie ck2=new Cookie("emailId",emailId);  
											       
											       ck1.setMaxAge(31536000);
											       ck2.setMaxAge(31536000);
											       
												
										            response.addCookie(ck1);  
										            response.addCookie(ck2);  
										            
											}else if(jsonReq.get("statusCode").toString().equalsIgnoreCase("TK0000") && jsonResp.get("statusCode").toString().equalsIgnoreCase("TK0000") ) {
												CM.setCryptogram((new JSONObject(jsonResp.get("cryptogramInfo").toString())).get("cryptogram").toString());	
												CM.setOriginalToken((new JSONObject(jsonResp.get("tokenInfo").toString())).get("originalToken").toString());												
												request.setAttribute("cryptogram", (new JSONObject(jsonResp.get("cryptogramInfo").toString())).get("cryptogram"));
												request.setAttribute("tokenCardNumber", TokenCardNumber.trim().toString());
												db.saveCardDetailsTokenize(CM);	
												
										
												
												String mobileNo = request.getParameter("mobileNo");
												String emailId = request.getParameter("emailId");
												
											
												Cookie ck1=new Cookie("mobileNo",mobileNo);  
											       Cookie ck2=new Cookie("emailId",emailId);  
											       
											       ck1.setMaxAge(31536000);
											       ck2.setMaxAge(31536000);
											     
											
										            response.addCookie(ck1);  
										            response.addCookie(ck2);  
											}
				
											request.setAttribute("tokenReferenceId", jsonReq.get("tokenReferenceId").toString());
											request.setAttribute("tokenExpiry", PGUtils.encryptcryptoJs(jsonReq.get("tokenExpiryDate").toString()));
											//request.setAttribute("tokenExpiry",jsonReq.get("tokenExpiryDate").toString().substring(2));
										
											request.setAttribute("lastDigit", jsonReq.get("panLast4").toString());

											logger.debug("PGRouter.java ::: " + PGUtils.generateLog(TM.getMerchantId(),
													TM.getTxnId(), TM.getId(), TM.getAmount()));
											logger.info("Save Card Details Procedure Executed Successfully");
										}
										
										
										
									} catch (Exception e1) {
										logger.error("PGRouter.java :::for id =" + TM.getTxnId()
												+ " Error in Card Saving : ", e1);
									}

								}

							//	logger.info("TokenCardNumber::::::"+TokenCardNumber);
								
								TM.setBankId(bankId);
								TM.setCardDetails(maskedCardNo);
								TM.setCardType(request.getParameter("cardType").toString());

								logger.debug("PGRouter.java :: CardType : " + TM.getCardType());

								TM.setRmsScore("0");
								if ((rmsReason != null) && (!rmsReason.isEmpty())) {
									TM.setRmsReason(rmsReason);

								} else {
									TM.setRmsReason("NA");
								}
								TM.setCardName(request.getParameter("cardName"));
							}
							logger.info("PGRouter.java ::: " + PGUtils.generateLog(TM.getMerchantId(), TM.getTxnId(),
									TM.getId(), TM.getAmount()));
							// logger.error("PGRouter.java ::: RMS Response Received is Null/Empty.");
						} catch (Exception e) {
							logger.info("PGRouter.java ::: " + PGUtils.generateLog(TM.getMerchantId(), TM.getTxnId(),
									TM.getId(), TM.getAmount()));
							logger.error("PGRouter.java ::: Error in Card Validation/Checking : ", e);
						}
					} else if ((sInstrumentId != null) && (sInstrumentId.equals("NB"))) {
						rmsResult = "0.00";
						rmsReason = "NA";

						TM.setBankId(bankId);
						TM.setCardDetails("NA");
						TM.setCardType("NA");
						TM.setRmsScore("NA");
						TM.setRmsReason("NA");
					} else if ((sInstrumentId != null) && (sInstrumentId.equals("UPI"))) {
						logger.debug("PGRouter.java Instrument Id UPI for txnid=" + TM.getId());
						rmsResult = "0.00";
						rmsReason = "NA";

						TM.setBankId(bankId);

						MerchantDTO merchant = db.getMerchant(TM.getMerchantId());

						String isVAS = null;

						isVAS = merchant.getIsVAS();

						if (isVAS.equals("0")) {
							String vpa = request.getParameter("VPA");
							TM.setCardDetails(vpa);
						} else {
							String uidValueBoolean = request.getParameter("otherAppValue");
							if (uidValueBoolean.equals("false")) {
								String vpa = request.getParameter("VPA");
								String uid = request.getParameter("UID");
								TM.setCardDetails(vpa + uid);
							} else {
								String vpa = request.getParameter("otherVPA");
								TM.setCardDetails(vpa);
							}
						}
						TM.setCardType("NA");
						TM.setRmsScore("NA");
						TM.setRmsReason("NA");
					} else if (sInstrumentId != null && sInstrumentId.equals("WALLET")) {
						logger.info("PGRouter.java Instrument Id WALLET ");
						rmsResult = "0.00";
						rmsReason = "NA";
						TM.setBankId(bankId);
						TM.setCardDetails("NA");
						TM.setCardType("NA");
						TM.setRmsScore("NA");
						TM.setRmsReason("NA");
					} else {
						logger.info("Instrument Id inside Else block is for txnid=" + TM.getId() + "  instrumentid="
								+ TM.getInstrumentId());
					}
					TM.setCustomerId(customerId);
	              String Status = db.CheckStatus(TM.getMerchantId());
					
					if(Status.equalsIgnoreCase("Active") || Status.equalsIgnoreCase("Hold"))	
					{
					int updatestatus = db.updateTxnWithCard(TM);

					if (updatestatus == 1) {
						logger.info("After Updating database ::::: for id-" + TM.getId() + " " + TM.getSurcharge());

						try {
							// Double amount = Double.valueOf(Double.valueOf(TM.getAmount()).doubleValue() +
							// Double.parseDouble(TM.getSurcharge()));
							//TM.setSurcharge("0.75");
							BigDecimal dec = new BigDecimal(TM.getAmount());
							BigDecimal dec1 =new BigDecimal(TM.getSurcharge());
							BigDecimal amount = dec;

							

							//boolean bool= dec1.compareTo(BigDecimal.ZERO);
							if(dec1.compareTo(BigDecimal.ZERO) != 0) {
								amount = dec.add(dec1);
							}

							logger.info("Surcharge amount is "+dec1+"Added on amout:::"+amount);
							
							request.setAttribute("id", TM.getId());
							request.setAttribute("amount", amount + "");

							request.setAttribute("custMob", TM.getCustMobile());
							request.setAttribute("custMail", TM.getCustMail());

							logger.debug("PGRouter.java :: txnid=" + TM.getId() + " amount=" + amount + " custMob="
									+ TM.getCustMobile() + " custMail=" + TM.getCustMail());
							request.setAttribute("txnType", "DIRECT");
							request.setAttribute("Mid", TM.getMerchantId());
							request.setAttribute("bankId", bankId);

							request.setAttribute("checksum", request.getParameter("checksum"));
							request.setAttribute("instrumentId", sInstrumentId);
							request.setAttribute("spId", spId);
							request.setAttribute("Merchant_txn_id", TM.getTxnId());
							request.setAttribute("VPA", TM.getCardDetails());
							request.setAttribute("dateTime", TM.getDateTime());
							if(request.getParameter("cardType")!=null) {
								request.setAttribute("cardType", request.getParameter("cardType").toString());	
							}

							logger.info("MerchantId:::::::Enter in db:::"+sClassFileLoader+":::: "+TM.getMerchantId());

							MerchantDTO merchant = db.getMerchant(TM.getMerchantId());
							logger.info("MerchantId:::::::::::::: "+merchant);
							request.setAttribute("isVas", merchant.getIsVAS());
							/*
							 * if(TM.getInstrumentId().equalsIgnoreCase("CC") ||
							 * TM.getInstrumentId().equalsIgnoreCase("DC")) {
							 * sClassFileLoader="com.payone.serviceprovider.HDFCPGProcessor"; }
							 */
							IDynamicPGSelector pgSelector = null;
							// String redirectPage = null;

							
							logger.info("PGRouter.java :: txnid=" + TM.getId() + " Mid=" + TM.getMerchantId()
							+ " instrumentId=" + sInstrumentId + " Merchant_txn_id=" + TM.getTxnId()
							+ " sClassFileLoader=" + sClassFileLoader);
					logger.info(">>>>>>>>> " + sClassFileLoader);
							logger.debug("PGRouter.java :: txnid=" + TM.getId() + " Mid=" + TM.getMerchantId()
									+ " instrumentId=" + sInstrumentId + " Merchant_txn_id=" + TM.getTxnId()
									+ " sClassFileLoader=" + sClassFileLoader);
							logger.debug(">>>>>>>>> " + sClassFileLoader);
							
							
							pgSelector = (IDynamicPGSelector) Class.forName(sClassFileLoader).newInstance();
							
							logger.info("pgSelector >>> "+pgSelector);
							logger.info("pgSelector >>> "+pgSelector);
							db.setSpTimestamp(TM.getId());
							String redirectPage = pgSelector.redirectToPG(request);
							logger.info("redirectPage >>> "+redirectPage);
							logger.info("redirectPage >>> "+redirectPage);
							out.println(redirectPage);

							out.flush();
							out.close();
						} catch (Exception e) {
							// e.printStackTrace();
							logger.debug("Error At ================>" + e.getMessage());
							logger.debug("PGRouter.java ::: " + PGUtils.generateLog(TM.getMerchantId(), TM.getTxnId(),
									TM.getId(), TM.getAmount()));
							logger.error("PGRouter.java ::: Error Occurred :: " + e);

							request.setAttribute("errorMsg",
									"Error 10052 : Error while Processing Transaction Request.");

							request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
						}

					} else {
						logger.debug("Locktime out or deadlock occurs Updating database ::::: for id-" + TM.getId());

						logger.debug("PGRouter.java ::: "
								+ PGUtils.generateLog(TM.getMerchantId(), TM.getTxnId(), TM.getId(), TM.getAmount()));

						request.setAttribute("errorMsg", "Error 10052 : Error while Processing Transaction Request.");

						request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);

					}
					

				
				}else
				{
					logger.debug("Status False " +Status);

					logger.debug("PGRouter.java ::: "
							+ PGUtils.generateLog(TM.getMerchantId(), TM.getTxnId(), TM.getId(), TM.getAmount()));

					request.setAttribute("errorMsg", "Error 10054 : Merchant is " +Status+ " will not proceed this Transaction");

					request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);

				}
				}
			} else {
				logger.debug("PGRouter.java ::: brute force attack for txnid" + TM.getId());
				request.setAttribute("errorMsg", "Error 10052 : Error while Processing Transaction Request.");
				request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
			}
				
				
			//Mongodb code for recon master table
			
			logger.debug("::::::::::::::::::::::::::::::::::::;rcon:::::::::::::::::::::::::;;;rute force attack for txnid" + TM.getId());

			//MongoDBDataManager mdm = new MongoDBDataManager();
			//mdm.saveReconMaster(TM.getProductId(), TM.getMerchantId(), TM.getProcessId(), TM.getInstrumentId(), TM.getBankId(), TM.getAmount(), TM.getId(), TM.getTxnId());
		/** 30/05/2023 comment all mongo db related call
		 * 
		 
			new MongoDBReconMaster(TM.getProductId(), TM.getMerchantId(), TM.getProcessId(), TM.getInstrumentId(), TM.getBankId(), TM.getAmount(), TM.getId(), TM.getTxnId()).start();
			
			// Check rules and updated risk attributes in tbl_transactionmaster mongodb
			**/
			RulesAccessDTO rulesDTO = new RulesAccessDTO();
			rulesDTO.setMerchantId(TM.getMerchantId());
			rulesDTO.setAmount(TM.getAmount());
			rulesDTO.setId(TM.getId());
			rulesDTO.setInstrumentId(TM.getInstrumentId());
			
			logger.info("rulesDTO::::::::::::::::::::"+rulesDTO.getMerchantId(),rulesDTO.getAmount(),rulesDTO.getId(),rulesDTO.getInstrumentId());
			
				//String hostNameForReconMasterMicroService= "http://localhost:8081";//localhost:8088
            Gson gson = new Gson();
			
            String hostNameForRulesMicroService = PGUtils.getPropertyValue("hostNameForRulesMicroService");
			
            logger.info("hostNameForRulesMicroService:::::::::::::::::::::::::"+hostNameForRulesMicroService);
            String jsonRequest = gson.toJson(rulesDTO);
			HttpRequest requesttm = HttpRequest.newBuilder()
			      //  .uri(URI.create(hostNameForRulesMicroService+"/rules/access"))
					  .uri(URI.create(hostNameForRulesMicroService+"/rules/access"))
					.header("Content-Type", "application/json")
			        .POST(BodyPublishers.ofString(jsonRequest))
			        .build();
			
			logger.info("requesttm:::::::::::::::"+requesttm);
			logger.info("requesttm:::::::::::::::"+requesttm.toString());
			logger.info("requesttm::::::::::uri:::::"+requesttm.uri());
			   
			HttpClient client  = HttpClient.newHttpClient();
			
			CompletableFuture<HttpResponse<String>> postResponse = client.sendAsync(requesttm,BodyHandlers.ofString());
		
			
			/*
			 * try {
			 * System.out.println("post reponse body recon master---->"+postResponse.get().
			 * body()); } catch (InterruptedException e) { // TODO Auto-generated catch
			 * block e.printStackTrace(); } catch (ExecutionException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 */
		} else {
			String txnId = request.getParameter("txnId");

			logger.debug("PGRouter.java ::: for txnid=" + txnId + " Token Mismatch :: Token in Session --> "
					+ session.getAttribute("cToken") + " And Token in Request --> " + request.getParameter("checksum"));
			request.setAttribute("errorMsg",
					"Error 10050 : Invalid Request Parameters.Direct Parameter Posting is not allowed.");
			request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
		}
	}

	private boolean validateChecksum(String checksum) {
		Pattern pattern = Pattern
				.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$");
		if (checksum != null) {
			Matcher checkedValue = pattern.matcher(checksum);
			if (checkedValue.find()) {
				return true;
			}
		}
		return false;
	}

}