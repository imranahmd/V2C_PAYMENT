package com.rew.payment.router;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.rew.payment.utility.PGUtils;
import com.rew.pg.db.DataManager;
import com.rew.pg.dto.MerchantDTO;
import com.rew.pg.dto.RulesAccessDTO;
import com.rew.pg.dto.TransactionMaster;
import com.wibmo.tokenize.wrapper.impl.TokenizeWrapper;
import com.wibmo.tokenize.wrapper.model.DeTokenRequest;
import com.wibmo.tokenize.wrapper.model.DeTokenizePCIData;
import com.wibmo.tokenize.wrapper.model.TokenDataRequest;
import com.wibmo.tokenize.wrapper.model.TokenDataResponse;
import com.wibmo.tokenize.wrapper.util.Utility;

public class PGRedirector extends HttpServlet {
	private static Logger logger = LoggerFactory.getLogger(PGRedirector.class);
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
		String UPIIntentOrNot = "Other";

		String rmsResult = null;
		String rmsReason = null;

		String wibmoProperties = PGUtils.getPropertyValue("wibmoConfig");// wibmo property file path
		String clientId = PGUtils.getPropertyValue("clientId");
		String clientApiUser = PGUtils.getPropertyValue("clientApiUser");
		String clientApiKey = PGUtils.getPropertyValue("clientApiKey");
		String vaultId = PGUtils.getPropertyValue("vaultId");
		String tokenSecretKey = PGUtils.getPropertyValue("tokenSecretKey");
		String merchantId = PGUtils.getPropertyValue("merchantId");
		String deTokenSecretKey = PGUtils.getPropertyValue("deTokenSecretKey");

		TransactionMaster TM = null;
		HttpSession session = request.getSession();
		if ((session.getAttribute("cToken") != null) && (request.getAttribute("checksum") != null)
				&& (session.getAttribute("cToken").toString().equals(request.getAttribute("checksum").toString()))) {
			try {

				String txnId = request.getAttribute("pgid").toString();
				DataManager db = new DataManager();
				TM = db.getTxnMaster(txnId);
				TM.setCardName("NA");
				if (TM.getRespStatus().equalsIgnoreCase("99")
						&& TM.getModified_By().equalsIgnoreCase("PGTransaction")) {

					String sInstrumentId = request.getAttribute("instrumentId").toString();
					String[] cardDetails = null;
					String bankId = null;
					String maskedCardNo = null;
					bankId = TM.getBankId();
					String type;

					String emioption = "NA";
					String Status = db.CheckStatus(TM.getMerchantId());
					MerchantDTO merchantDTO = db.getMerchant(TM.getMerchantId());

					logger.info("Status::::::::::::::::::::: "+Status);
					InetAddress localHost = InetAddress.getLocalHost();
					String ipAddress = localHost.getHostAddress();
					if (CheckValueMatched(ipAddress,merchantDTO.getCustIP())==1) {

					if (Status.equalsIgnoreCase("Active") || Status.equalsIgnoreCase("Hold")) {

						/*
						 * if (sInstrumentId.equalsIgnoreCase("UPI")) {
						 * logger.info("Inside upi instrument::::::::" + TM.getCardDetails()); if
						 * (TM.getCardDetails().equalsIgnoreCase("I")) { type = "1"; } else { type =
						 * "0"; } String IsEnable = db.IsEnableUpiorIntent(TM.getMerchantId(), type);
						 * logger.info("Is access::::::::::::::::::: "+IsEnable); if
						 * (TM.getCardDetails().equalsIgnoreCase("I") && IsEnable.equalsIgnoreCase("1"))
						 * { logger.info("Intent is enable for this merchant::::::: " + IsEnable);
						 * UPIIntentOrNot = "Ok"; // for intent check } else if
						 * (!TM.getCardDetails().equalsIgnoreCase("I") &&
						 * IsEnable.equalsIgnoreCase("1")) {
						 * logger.info("collect is enable for this merchant::::::: " + IsEnable);
						 * UPIIntentOrNot = "Ok"; // for collect } else {
						 * logger.info("Intent or collect is not enable for this merchant:::::::" +
						 * IsEnable); UPIIntentOrNot = "Payment instrument not allowed"; } }
						 */
						logger.info("PGRedirector.java ::: for txnid=" + TM.getId() + " emioption=" + emioption
								+ "  instrumenid=" + sInstrumentId + " bankid=" + bankId);

						if ((sInstrumentId != null) && (sInstrumentId.equals("NB"))) {
							rmsResult = "0.00";
							rmsReason = "NA";

							bankId = TM.getBankId();

							logger.info("PGRedirector.java ::: for txnid=" + TM.getId() + " Bank Id entered is : "
									+ bankId);

							TM.setCardDetails("NA");
							TM.setRmsScore("NA");
							TM.setRmsReason("NA");
						} else if ((sInstrumentId != null) && ((sInstrumentId.equals("DC"))
								|| (sInstrumentId.equals("CC") || (sInstrumentId.equals("Amex"))))) {
							cardDetails = TM.getCardDetails().split("\\|");

							logger.info("PGRedirector.java ::: " + PGUtils.generateLog(TM.getMerchantId(),
									TM.getTxnId(), TM.getId(), TM.getAmount()));
							/*
							 * TransactionValidator tv = new TransactionValidator();
							 * 
							 * String sRe1 = PGUtils.getPropertyValue("rmsUrl");
							 * 
							 * String URL = sRe1 + "?type=rms&merchantId=" + TM.getMerchantId() +
							 * "&ipAddress=" + TM.getHostAddress() + "&cardNo=" + cardDetails[1].toString()
							 * + "&txtAmt=" + TM.getAmount() + "&cardType=" + TM.getInstrumentId(); String
							 * respStatus = tv.getValidatetrans(URL);
							 */
							String ctype = db.getBinvalidation(TM.getId(), cardDetails[1].substring(0, 6),
									sInstrumentId);

							// TM=db.getTxnMaster(txnId);
							TM.setInstrumentId(ctype);

							sInstrumentId = TM.getInstrumentId();

							logger.info("PGRedirector.java ::: for txnid=" + TM.getTxnId()
									+ "+after bin validation =======>" + sInstrumentId);

							/*
							 * if ((respStatus != null) && (!respStatus.isEmpty())) { JSONObject obj = new
							 * JSONObject(respStatus);
							 * 
							 * rmsResult = obj.getString("Result"); rmsReason = obj.getString("Reasons");
							 */
							String regex = "\\s";

							/*
							 * TM.setRmsScore(rmsResult);
							 * 
							 * if ((rmsReason != null) && (!rmsReason.isEmpty()))
							 * TM.setRmsReason(rmsReason); else { TM.setRmsReason("NA"); }
							 */
							TM.setRmsScore("0.00");
							TM.setRmsReason("NA");

							TM.setCardName(cardDetails[0].toString().replaceAll(regex, ""));

							if (String.valueOf(TM.getCardDetails().charAt(TM.getCardDetails().length() - 1))
									.equalsIgnoreCase("Y")) {
								TM.setCardDetails(cardDetails[4].toString());
								String wibmoPaymentDataResponseStr = null;
								JSONObject jsonResp = null;
								String TokenCardNumber = null;
								Map<String, String> headerMap = new HashMap<String, String>();

								headerMap.put("clientId", clientId);
								headerMap.put("clientApiUser", clientApiUser);
								headerMap.put("clientApiKey", clientApiKey);
								headerMap.put("vaultId", vaultId);
								headerMap.put("tokenSecretKey", tokenSecretKey);

								
								JSONObject getTokenDataRequest = new JSONObject();
								getTokenDataRequest.put("amount", TM.getAmount());
								getTokenDataRequest.put("currency", "356");
								getTokenDataRequest.put("clientReferenceId", txnId);
								getTokenDataRequest.put("merchantId", merchantId); // UAT
								
								TokenizeWrapper wrapper = new TokenizeWrapper(wibmoProperties);

								if (TM.getCardType().equalsIgnoreCase("Visa")
										|| TM.getCardType().equalsIgnoreCase("MasterCard")
										|| TM.getCardType().equalsIgnoreCase("Rupay")) {
									if (TM.getCardType().equalsIgnoreCase("Visa")) {

										ObjectMapper Mapper = new ObjectMapper();

										TokenDataRequest wibmoPaymentDataRequest = Mapper
												.readValue(getTokenDataRequest.toString(), TokenDataRequest.class);
										wibmoPaymentDataRequest.setTokenReferenceId(cardDetails[1].trim().toString());
										wibmoPaymentDataRequest.setCardType("V");

										TokenDataResponse tokenDataResponse = wrapper
												.getTokenData(wibmoPaymentDataRequest, headerMap);
										wibmoPaymentDataResponseStr = Mapper.writeValueAsString(tokenDataResponse);
										logger.info("Get Payment Data Response Visa : " + wibmoPaymentDataResponseStr);
										jsonResp = new JSONObject(wibmoPaymentDataResponseStr);

										TokenCardNumber = Utility.getAccountDetails(
												tokenDataResponse.getTokenInfo().getEncTokenInfo(),
												(String) headerMap.get("tokenSecretKey"), Utility.stringToByteArray(
														tokenDataResponse.getTokenInfo().getIv(), "HEX"));

									} else if (TM.getCardType().equalsIgnoreCase("MasterCard")) {

										// Thread.sleep(10000);
										ObjectMapper Mapper = new ObjectMapper();

										TokenDataRequest wibmoPaymentDataRequest = Mapper
												.readValue(getTokenDataRequest.toString(), TokenDataRequest.class);

										wibmoPaymentDataRequest.setTokenReferenceId(cardDetails[1].trim().toString());
										wibmoPaymentDataRequest.setCardType("M");
										TokenDataResponse tokenDataResponse = wrapper
												.getTokenData(wibmoPaymentDataRequest, headerMap);
										wibmoPaymentDataResponseStr = Mapper.writeValueAsString(tokenDataResponse);
										logger.info("Get Payment Data Response Master: " + wibmoPaymentDataResponseStr);
										jsonResp = new JSONObject(wibmoPaymentDataResponseStr);

										TokenCardNumber = Utility.getAccountDetails(
												tokenDataResponse.getTokenInfo().getEncTokenInfo(),
												(String) headerMap.get("tokenSecretKey"), Utility.stringToByteArray(
														tokenDataResponse.getTokenInfo().getIv(), "HEX"));

									}

									else if (TM.getCardType().equalsIgnoreCase("Rupay")) {

										ObjectMapper Mapper = new ObjectMapper();

										TokenDataRequest wibmoPaymentDataRequest = Mapper
												.readValue(getTokenDataRequest.toString(), TokenDataRequest.class);
										wibmoPaymentDataRequest.setTokenReferenceId(cardDetails[1].trim().toString());
										wibmoPaymentDataRequest.setCardType("R");
										TokenDataResponse tokenDataResponse = wrapper
												.getTokenData(wibmoPaymentDataRequest, headerMap);
										wibmoPaymentDataResponseStr = Mapper.writeValueAsString(tokenDataResponse);
										logger.info("Get Payment Data Response Rupay: " + wibmoPaymentDataResponseStr);
										jsonResp = new JSONObject(wibmoPaymentDataResponseStr);

										TokenCardNumber = Utility.getAccountDetails(
												tokenDataResponse.getTokenInfo().getEncTokenInfo(),
												(String) headerMap.get("tokenSecretKey"), Utility.stringToByteArray(
														tokenDataResponse.getTokenInfo().getIv(), "HEX"));
									}
									logger.info("TokenCardNumber:" + TokenCardNumber);
									request.setAttribute("cardName", cardDetails[0].toString().replaceAll(regex, ""));
									request.setAttribute("tokenReferenceId", cardDetails[1].toString());
									request.setAttribute("cardCVV", cardDetails[2].toString());
									request.setAttribute("tokenExpiry", cardDetails[3].toString());
									request.setAttribute("cardType", TM.getCardType());
									request.setAttribute("cryptogram",
											(new JSONObject(jsonResp.get("cryptogramInfo").toString()))
													.get("cryptogram"));
									request.setAttribute("tokenCardNumber", TokenCardNumber.trim().toString());
									request.setAttribute("lastDigit", cardDetails[4].toString());
								}

								else if (TM.getCardType().equalsIgnoreCase("DinersClub")) {

									JSONObject deTokenizeRequestJson = new JSONObject();
									deTokenizeRequestJson.put("cardType", "D");
									deTokenizeRequestJson.put("clientReferenceId", TM.getId());
									deTokenizeRequestJson.put("merchantId", merchantId);

									final ObjectMapper objectMapper = new ObjectMapper();

									final Map<String, String> headerMapDiners = new HashMap<String, String>();

								
									headerMapDiners.put("clientId", clientId);
									headerMapDiners.put("clientApiUser", clientApiUser);
									headerMapDiners.put("clientApiKey", clientApiKey);
									headerMapDiners.put("vaultId", vaultId);
									headerMapDiners.put("deTokenSecretKey", deTokenSecretKey);

									DeTokenRequest deTokenRequest = (DeTokenRequest) objectMapper
											.readValue(deTokenizeRequestJson.toString(), (Class) DeTokenRequest.class);
									deTokenRequest.setTokenReferenceId(cardDetails[1].trim().toString());
									DeTokenizePCIData deTokenizePCIDataResponse = wrapper.deTokenize(deTokenRequest,
											(Map) headerMapDiners);
									String deTokenizePCIDataResponseStr = objectMapper
											.writeValueAsString((Object) deTokenizePCIDataResponse);
									String iv = deTokenizePCIDataResponse.getIv();
									// logger.info( " DeTokenize : " + deTokenizePCIDataResponseStr);
									String DecryptedString = Utility.getAccountDetails(
											deTokenizePCIDataResponse.getEncryptedAccountData(),
											(String) headerMapDiners.get("deTokenSecretKey"),
											Utility.stringToByteArray(iv, "HEX"));

									JSONObject jsonObject = new JSONObject(DecryptedString);
									jsonObject.getString("pan");
									jsonObject.getString("expiryMonth");
									jsonObject.getString("expiryYear");

									request.setAttribute("cardName", cardDetails[0].toString().replaceAll(regex, ""));
									request.setAttribute("cardNo", jsonObject.getString("pan"));
									request.setAttribute("cardExpiry",
											jsonObject.getString("expiryMonth") + jsonObject.getString("expiryYear"));
									request.setAttribute("cardCVV", cardDetails[2].toString());

								}
							} else {
								maskedCardNo = PGUtils.maskCardNumber(cardDetails[1].toString().replaceAll(regex, ""));

								TM.setCardDetails(maskedCardNo);
								request.setAttribute("cardName", cardDetails[0].toString().replaceAll(regex, ""));
								request.setAttribute("cardNo", cardDetails[1].toString());
								request.setAttribute("cardCVV", cardDetails[2].toString());
								request.setAttribute("cardExpiry", cardDetails[3].toString());
							}

							/*
							 * } else { logger.debug("PGRedirector.java ::: " +
							 * PGUtils.generateLog(TM.getMerchantId(), TM.getTxnId(), TM.getId(),
							 * TM.getAmount())); logger.error("PGRedirector.java ::: for txnid="+TM.getId()
							 * +" RMS Response Received is Null/Empty."); }
							 */
						} else if ((sInstrumentId != null) && (sInstrumentId.equals("UPI"))) {
							logger.debug("PGRouter.java Instrument Id UPI for txnid=" + TM.getId());
							rmsResult = "0.00";
							rmsReason = "NA";
							TM.setCardType("NA");
							TM.setRmsScore("NA");
							TM.setRmsReason("NA");
						} else if (sInstrumentId != null && sInstrumentId.equals("WALLET")) {
							logger.debug("PGRouter.java Instrument Id WALLET ");
							rmsResult = "0.00";
							rmsReason = "NA";
							TM.setBankId(bankId);
							TM.setCardDetails("NA");
							TM.setCardType("NA");
							TM.setRmsScore("NA");
							TM.setRmsReason("NA");
						} else {
							logger.debug("PGRedirector.java ::: " + PGUtils.generateLog(TM.getMerchantId(),
									TM.getTxnId(), TM.getId(), TM.getAmount()));
							logger.debug("PGRedirector.java ::: for txnid=" + TM.getId()
									+ " Instrument Id inside Else block is : " + sInstrumentId);
						}

						String sClassFileLoader = null;
						String spId = null, retamount = null;
						int payout = 0;

						List<String> surchargeData = db.getCalculateSurcharge(bankId, sInstrumentId, TM.getAmount(),
								TM.getMerchantId(), emioption, TM.getId());
						retamount = surchargeData.get(0);
						sClassFileLoader = surchargeData.get(1);
						spId = surchargeData.get(2);
						payout = Integer.valueOf(surchargeData.get(3));

						TM.setRespStatus("0");
						TM.setServiceRRN("NA");
						TM.setServiceAuthId("NA");
						TM.setServiceTxnId("NA");
						TM.setRespMessage("NA");
						TM.setProcessId(spId);
						TM.setSurcharge(retamount);
						TM.setInstrumentId(sInstrumentId);
						TM.setModified_By("PGRedirector");
						TM.setModified_On(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

						int updatestatus = db.updateTxnWithCard(TM);

						if (updatestatus == 1) {
					
								logger.debug(
										"After Updating database ::::: for id-" + TM.getId() + " " + TM.getSurcharge());

								// if ((rmsResult != null) && (!rmsReason.equalsIgnoreCase("Black Listed IP
								// Address")))
								// {
								// Double amount = Double.valueOf(Double.parseDouble(TM.getAmount()) +
								// Double.parseDouble(TM.getSurcharge()));

								BigDecimal dec = new BigDecimal(TM.getAmount());
								BigDecimal dec1 = new BigDecimal(TM.getSurcharge());
								BigDecimal amount = dec;
								if (payout == 1) {
									amount = dec.add(dec1);
								}

								request.setAttribute("id", TM.getId());
								request.setAttribute("txnId", TM.getId());
								request.setAttribute("amount", amount + "");
								request.setAttribute("custMob", TM.getCustMobile());
								request.setAttribute("custMail", TM.getCustMail());
								request.setAttribute("txnType", "REDIRECT");
								request.setAttribute("Mid", TM.getMerchantId());
								request.setAttribute("bankId", bankId);
								request.setAttribute("spId", spId);
								request.setAttribute("instrumentId", sInstrumentId);
								request.setAttribute("Merchant_txn_id", TM.getTxnId());
								request.setAttribute("VPA", TM.getCardDetails());
								request.setAttribute("dateTime", TM.getDateTime());
								request.setAttribute("isVas", "0");
								logger.debug("PGRedirector.java ::: for txnid=" + TM.getId() + " amount=" + amount
										+ " custMob=" + TM.getCustMobile() + " custMail=" + TM.getCustMail()
										+ " bankId=" + bankId + " spId=" + spId + " instrumentId=" + sInstrumentId
										+ " classloader=" + sClassFileLoader);
								IDynamicPGSelector pgSelector = null;

								logger.info("Insert in reconmaster::::::::::::::: "+TM.getProductId(), TM.getMerchantId(), TM.getProcessId(),
										TM.getInstrumentId(), TM.getBankId(), TM.getAmount(), TM.getId(),
										TM.getTxnId());
								/*
								 * MongoDBDataManager mdm = new MongoDBDataManager();
								 * mdm.saveReconMaster(TM.getProductId(), TM.getMerchantId(), TM.getProcessId(),
								 * TM.getInstrumentId(), TM.getBankId(), TM.getAmount(), TM.getId(),
								 * TM.getTxnId());
								 * logger.info("after in reconmaster::::::::::::::: "+sClassFileLoader);
								 */
								logger.info("before  in risk configuration::::::::::::::: ");


								RulesAccessDTO rulesDTO = new RulesAccessDTO();
								rulesDTO.setMerchantId(TM.getMerchantId());
								rulesDTO.setAmount(TM.getAmount());
								rulesDTO.setId(TM.getId());
								rulesDTO.setInstrumentId(TM.getInstrumentId());
								
								logger.info("Middle  in risk configuration::::::::"+TM.getMerchantId()+":::"+TM.getAmount()+"::::"+TM.getId()+""+TM.getInstrumentId()+"");

									//String hostNameForReconMasterMicroService= "http://localhost:8081";//localhost:8088
					            Gson gson = new Gson();
					            
					            String hostNameForRulesMicroService = PGUtils.getPropertyValue("hostNameForRulesMicroService");
								
					            logger.info("hostNameForRulesMicroService::::::::::"+hostNameForRulesMicroService);
					            String jsonRequest = gson.toJson(rulesDTO);
								HttpRequest requesttm = HttpRequest.newBuilder()
								        .uri(URI.create(hostNameForRulesMicroService+"/rules/access"))
								        .header("Content-Type", "application/json")
								        .POST(BodyPublishers.ofString(jsonRequest))
								        .build();
								   
								HttpClient client  = HttpClient.newHttpClient();
								
								CompletableFuture<HttpResponse<String>> postResponse = client.sendAsync(requesttm,BodyHandlers.ofString());
								
								
					            
								/*
								 * String hostNameForRulesMicroService =
								 * PGUtils.getPropertyValue("hostNameForRulesMicroService"); String jsonRequest
								 * = gson.toJson(rulesDTO); HttpRequest requesttm = HttpRequest.newBuilder()
								 * .uri(URI.create(hostNameForRulesMicroService+"/rules/access"))
								 * .header("Content-Type", "application/json")
								 * .POST(BodyPublishers.ofString(jsonRequest)) .build();
								 * 
								 * HttpClient client = HttpClient.newHttpClient();
								 * 
								 * CompletableFuture<HttpResponse<String>> postResponse =
								 * client.sendAsync(requesttm,BodyHandlers.ofString());
								 * 
								 * try {
								 * System.out.println("post reponse body recon master---->"+postResponse.get().
								 * body()); } catch (InterruptedException e) { // TODO Auto-generated catch
								 * block e.printStackTrace(); } catch (ExecutionException e) { // TODO
								 * Auto-generated catch block e.printStackTrace(); }
								 */
					
								logger.info("after  in risk configuration::::::::::::::: ");

								//sClassFileLoader="com.payone.serviceprovider.NPSTUPIProcessor";
								String redirectPage = null;
								pgSelector = (IDynamicPGSelector) Class.forName(sClassFileLoader).newInstance();

								db.setSpTimestamp(TM.getId());

								redirectPage = pgSelector.redirectToPG(request);

								out.println(redirectPage);
								out.flush();
								out.close();
								return;
							
							/*
							 * }
							 * 
							 * logger.debug("PGRedirector.java ::: " +
							 * PGUtils.generateLog(TM.getMerchantId(), TM.getTxnId(), TM.getId(),
							 * TM.getAmount())); logger.debug("PGRedirector.java ::: RMS Result :: " +
							 * rmsResult + " and RMS Error :: " + rmsReason);
							 * request.setAttribute("errorMsg",
							 * "Error 10052 :Error while Processing Transaction Request." );
							 * request.getRequestDispatcher("txnValidationErrors.jsp").include(request,
							 * response);
							 */

						} else {
							logger.debug(
									"Locktime out or deadlock occurs Updating database ::::: for id-" + TM.getId());
							request.setAttribute("errorMsg",
									"Error 10052 :Error while Processing Transaction Request.");
							request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
						}
					} else {
						logger.info("Merchant is not active ::::: for id-" + TM.getId());
					    response.getWriter().write("{\"RespMsg\":\"Merchant Is Not Active Please contact with the Admin\"}");

						request.setAttribute("errorMsg", "Error 10052 :Merchant Is not active");
						request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
					}
					}else {
						 logger.info("Server IP Address:::::Not Registerd:::::::::::::::::::::::: " + ipAddress);

						    response.getWriter().write("{\"RespMsg\":\"IP IS NOT WHITE LISTED\"}");
						    return;
							
					}
				} else {
					logger.debug("PGRouter.java ::: brute force attack for txnid" + TM.getId());
					request.setAttribute("errorMsg", "Error 10052 :Error while Processing Transaction Request.");
					request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
				}
			} catch (Exception e) {
				logger.debug("PGRedirector.java ::: "
						+ PGUtils.generateLog(TM.getMerchantId(), TM.getTxnId(), TM.getId(), TM.getAmount()));
				logger.error("PGRedirector.java ::: Error Occurred in main Catch Block :: ", e);
				request.setAttribute("errorMsg", "Error 10052 :Error while Processing Transaction Request.");
				request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
			}

		}

		else {
			String txnId = request.getAttribute("pgid").toString();

			logger.debug("PGRedirector.java ::: for txnid=" + txnId + " Token Mismatch :: Token in Session --> "
					+ session.getAttribute("cToken") + " And Token in Request --> " + request.getParameter("checksum"));
			request.setAttribute("errorMsg",
					"Error 10050 : Invalid Request Parameters.Direct Parameter Posting is not allowed.");
			request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
		}
	}
		public static int CheckValueMatched(String custIP, String BlockIp) {
			if (custIP != null || BlockIp != null) {
				List<String> list = Arrays.asList(BlockIp.split(","));
				if (list.contains(custIP)) {
					logger.info("Ip found int hr black list:: ");
					return 1;

				} else {
					logger.info("Ip not found in this list");
					return 0;

				}

			} else {
				logger.info("Ip value::::::::::::: ");

			}
			return 0;
		}

}