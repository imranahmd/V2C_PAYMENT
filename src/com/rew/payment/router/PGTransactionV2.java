package com.rew.payment.router;

import com.rew.payment.rms.RiskConfiguration;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import com.rew.payment.utility.AesBase64Wrapper;
import com.rew.payment.utility.PGUtils;
import com.rew.pg.db.DataManager;
import com.rew.pg.dto.MerchantDTO;
import com.rew.pg.dto.PGTransactionDTOV2;
import com.rew.pg.dto.ResellerDTO;
import com.rew.pg.dto.TransactionMaster;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/paymentinit")
public class PGTransactionV2 extends HttpServlet {
	private static Logger logger = LoggerFactory.getLogger(PGTransactionV2.class);
	private static final long serialVersionUID = 1L;
	String[] reqParams;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Get Method not supported");
	}

	protected void doOption(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Option Method not Allowed.");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Cache-control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "-1");

		// Retrieve the current session, or create a new one if it doesn't exist
        HttpSession session = request.getSession();
       // HttpSession session = request.getSession();

        // Get the session ID
        String sessionId = session.getId();
		
		Cookie sessionCookie = new Cookie("SESSIONID", sessionId);
		sessionCookie.setSecure(true); // Ensures the cookie is sent over HTTPS only
		sessionCookie.setHttpOnly(true); // Prevents access from JavaScript
		response.addCookie(sessionCookie);
		
		
		//https://pg.payfi.co.in/pay/
		sessionCookie.setDomain("pg.payfi.co.in"); // Restricts the cookie to the specified domain
		response.addCookie(sessionCookie);
		
		// Cookie sessionCookie = new Cookie("SESSIONID", sessionId);
		sessionCookie.setHttpOnly(true); // Prevents access from JavaScript
		response.addCookie(sessionCookie);
		
		
		//String csrfToken = generateCSRFToken();
		//request.getSession().setAttribute("csrfToken", csrfToken);
		// Include csrfToken in hidden form fields for POST requests


//		Cookie sessionCookie = new Cookie("SESSIONID", sessionId);
//		sessionCookie.setPath("/");
//		sessionCookie.setSecure(true);
//		sessionCookie.setHttpOnly(true);
//		sessionCookie.setComment("SameSite=None"); // Custom handling might be needed
//		response.addCookie(sessionCookie);
		

		
		
		
		
		String ReqData = null;
		PrintWriter out = response.getWriter();
		//HttpSession session = request.getSession();
		Enumeration params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String paramName = (String) params.nextElement();
			String paramValue = request.getParameter(paramName);
			logger.info("PGTransaction.java ::: Param Name : " + paramName + " :: Param Value : " + paramValue);
		}

		logger.info("PGTransaction.java ::: Start capturing params....");
		try {
			// DataManager dm=new DataManager();
			PGTransactionDTOV2 dm = new PGTransactionDTOV2();
			ReqData = request.getParameter("encData");
			boolean IsWhiteSpace = containsWhiteSpace(request.getParameter("encData"));
			logger.info("::::::::::::::::: value " + IsWhiteSpace);
			if (IsWhiteSpace) {
				logger.info(":::::::Enter the whit space :::::::::: value " + IsWhiteSpace);
				char ch = '+';
				String Url = request.getParameter("encData").replace(' ', ch);
				logger.info(Url);
				request.removeAttribute("encData");
				request.setAttribute("encData", Url);
				logger.info(":::::::Url:::::::::: value " + Url);

				ReqData = Url;
			}
			logger.error("Start get data by merchant id= " + session.getId().toString());
			dm = dm.getData(request.getParameter("AuthID"), ReqData);
			logger.error("End get data by merchant id= " + session.getId().toString());

			// MerchantDTO merchantDTO =
			// dm.getMerchant(request.getParameter("merchantId"));//getting merchant request
			// deatils from merchantid
			MerchantDTO merchantDTO = dm.getMerchantDTO();
			logger.info(
					"PGTransaction.java ::: request.getParameter(merchantId) : " + request.getParameter("merchantId"));

			// decrypting request in putting in jsonfromat
			// JSONObject json = getJsonRequest(merchantDTO.getTransactionKey(),
			// request.getParameter("reqData"), request.getParameter("merchantId"));
			JSONObject json = dm.getJson();
			DataManager Dm = new DataManager();

			if (json.has("Paymentdate") && json.has("ConatctNo") && json.has("CallbackUrl")) {
				json.put("PaymentDate", json.getString("Paymentdate"));
				json.put("ContactNo", json.getString("ConatctNo"));
				json.put("CallbackURL", json.getString("CallbackUrl"));

			}
			String date = json.getString("PaymentDate");
			logger.info("Request Date ====>" + date);
			String udf6 = null;
			logger.info("Request Date ====>" + date);
			if (json.has("udf6")) {
				String logge = json.getString("udf6");
				logger.info("logge:::::::::::::::::::::::::::::::::::::::::::::::::" + logge);

				if (logge.equalsIgnoreCase("") || logge == null) {

					udf6 = "NA";
					json.remove("udf6");
					json.put("udf6", udf6);
					logger.info("Udf Value:::: ====>" + json.getString("udf6"));

				}
			} else {
				json.put("udf6", "NA");

			}
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			json.remove("dateTime");
			json.put("dateTime", date);
			logger.info("overridden Date ====>" + date);

			String txnid = json.getString("CustRefNum");
			logger.info(
					"===========================================================================================================  {}",
					txnid);
			if (json != null) {
				logger.info("merchantDTO.getReseller_Id()::::::: Date ====>" + merchantDTO.toString());

				logger.info("merchantDTO.getReseller_Id()::::::: Date ====>" + merchantDTO.getReseller_Id());

//				if (merchantDTO.getReseller_Id() != null && !merchantDTO.getReseller_Id().equalsIgnoreCase("")) {
//					ResellerDTO resellerDTO = Dm.getReseller(merchantDTO.getReseller_Id());
//
//					
//				//	TM.setReseller_id();
//					logger.info("inside json  ::::::::::::::::;;====>" + json);
//
//					if (resellerDTO.getIntegration_type().equalsIgnoreCase("1")
//							|| resellerDTO.getIntegration_type().equalsIgnoreCase("2")) {
//
//						if (json.has("Rid") && merchantDTO.getReseller_Id() != null) {
//
//							logger.info("inside merchantDTO.getReseller_Id()  :::" + json.has("Rid")
//									+ ":::::::::::::;;====>" + merchantDTO.getReseller_Id());
//							logger.info("::::::::::::::::::::::::reseller :::::::" + resellerDTO.getIntegration_type());
//
//							if (merchantDTO.getReseller_Id().equalsIgnoreCase(json.getString("Rid")))
//
//							{
//
//								logger.info("Reseller_Id Match From Db level  ---====>" + date);
//
//								request.getRequestDispatcher("/RTransaction").forward(request, response);
//
//								logger.info("Reseller Id In Json::::::::::::::::::: ====>" + json.getString("Rid"));
//
//								return;
//							} else {
//								request.setAttribute("errorMsg",
//										"Error 1055 : Reseller_Id Mismatch or wrong Reseller integration type");
//								request.getRequestDispatcher("txnValidationErrors.jsp").forward(request, response);
//								return;
//							}
//						} else {
//							request.setAttribute("errorMsg", "Error 1055 : Reseller_Id Not found On this Request "
//									+ merchantDTO.getMerchantId());
//							request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
//							return;
//						}
//					}
//				}
				logger.info("inside else:::::Id() 0");

				if (json.has("Rid") && !json.getString("Rid").equalsIgnoreCase("NA")) {
					request.setAttribute("errorMsg", "Error 1055 : Merchant passing wrong parameter");
					request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
					return;
				}

				logger.info("insid::::::::::::::1:::Id() 0");

				json.put("Rid", "NA");
				json.put("ResellerTxnId", "NA");

				String[] reqParams = { "AuthID", "CustRefNum", "PaymentDate", "txn_Amount", "ContactNo", "EmailId",
						"CallbackURL", "AuthKey", "adf1", "adf2", "adf3", "IntegrationType", "MOP", "PaymentDate",
						"MOPDetails", "MOPType", "PaymentDate" };

				String sResp = PGUtils.null2Known(reqParams, json);// checking any parameter is null or blank
				InetAddress localHost = InetAddress.getLocalHost();

				String ipAddress = localHost.getHostAddress();
				logger.info("Server IP Address:::localHost:::: " + ipAddress + "::::::::" + localHost.getHostAddress()
						+ "::::::::::::: " + localHost);

				if (CheckValueMatched(ipAddress, merchantDTO.getCustIP()) == 1) {
					logger.info("Server IP Address::::::::::::::::::::::::::::::: " + ipAddress);

					if ((sResp != null) && (sResp.equals("NOTNULL"))) {

						// TransactionMaster TM=getTransactionMaster(json);//setting request details in
						// transaction master
						logger.error("Start get cards data= " + session.getId().toString());
						dm = dm.getCardsData(json);
						logger.error("Start get cards data= " + session.getId().toString());
						TransactionMaster TM = dm.getTM();
						
						
						TM.setReseller_id(merchantDTO.getReseller_Id());//mithi
						logger.info("::::::::::::::reseller::::id  mst merchant:::::::::"+merchantDTO.getReseller_Id());
						
						
						
						logger.info("PGTransaction.java ::: Transaction Master Created for mertxnid=" + TM.getTxnId());

						logger.info("PGTransaction.java ::::Ip Address:::::" + request.getRemoteAddr() + "::::::::::::"
								+ request.getHeader("X-Forwarded-For"));
						if (request.getHeader("X-Forwarded-For") == null) {
							TM.setHostAddress(request.getRemoteHost());
							request.setAttribute("CustIp", request.getRemoteHost());
							request.setAttribute("Mid", TM.getMerchantId());

							logger.info("PGTransaction.java ::::::" + request.getRemoteHost());
						} else {
							String ip = request.getHeader("X-Forwarded-For");
							if (ip.indexOf(",") == -1) {
								TM.setHostAddress(ip.toString());
								logger.info("PGTransaction.java ::::::::::" + ip.toString() + ":::::::"
										+ request.getRemoteHost());
								request.setAttribute("CustIp", ip.toString());
								request.setAttribute("Mid", TM.getMerchantId());
							} else {
								StringTokenizer ipToken = new StringTokenizer(ip, ",");
								String sFirstIP = ipToken.nextToken();
								logger.info("PGTransaction.java ::: for mertxnid=" + TM.getTxnId() + " IP Address  :  "
										+ sFirstIP);
								TM.setHostAddress(sFirstIP.toString());
								request.setAttribute("CustIp", sFirstIP.toString());
								request.setAttribute("Mid", TM.getMerchantId());
							}
						}
						RiskConfiguration RiskConfiguration = new RiskConfiguration();
						int i = 1;// RiskConfiguration.ValidateGlobalRMS(request, response);
						logger.info("PGTransaction.java ::  Validation of global::::::::::::: " + i);
						if (i == 1) {
							logger.info("PGTransaction.java ::: "
									+ PGUtils.generateLog(TM.getMerchantId(), TM.getTxnId(), "NA", TM.getAmount()));
							logger.info("PGTransaction.java ::: for mertxnid=" + TM.getTxnId() + " Remote Address :: "
									+ request.getRemoteAddr());
							String ip = String.valueOf(TM.getMerchantId()) + "|" + TM.getTxnId() + "|"
									+ TM.getDateTime() + "|" + TM.getAmount() + "|" + TM.getApiKey() + "|"
									+ TM.getCustMobile() + "|" + TM.getCustMail() + "|" + TM.getChannelId() + "|"
									+ TM.getTxnType();
							String sCheckSum = new AesBase64Wrapper().encryptAndEncode(ip, TM.getApiKey());
							TM.setChecksum(sCheckSum);// calculating checksum

							RequestHandler handler = new RequestHandler(TM);
							handler = handler.validateRequest(handler);

							if (handler.isValid()) {
								if (json.getString("CustRefNum") != null
										&& json.getString("CustRefNum").equalsIgnoreCase("REDIRECT")) {
									if ((json.getString("MOP").equalsIgnoreCase("DC"))
											|| (json.getString("MOP").equalsIgnoreCase("CC"))) {
										if (json.getString("MOPType").equalsIgnoreCase("Rupay")) {
											TM.setBankId("Rupay");
										} else {
											TM.setBankId("FINPG");
										}
									} else if (json.getString("MOP").equalsIgnoreCase("UPI")) {
										TM.setBankId(json.getString("MOPType"));
									}
									TM.setCardDetails(json.getString("MOPDetails"));
								}
								if ((TM.getCustMail().equals(" ")) || (TM.getCustMail().equals(""))
										|| (TM.getCustMail().equalsIgnoreCase("NA"))) {
									TM.setCustMail("NA@abc.com");
								}


								logger.error("PGTransactionV2 Start saving transaction master= "
										+ session.getId().toString());
								TM = dm.saveTM(TM);//saving txn--mithi
								logger.error(
										"PGTransactionV2 End saving transaction master= " + session.getId().toString());

								if (merchantDTO.getIsRetry().equalsIgnoreCase("1")) {
									int status = dm.saveRetry(TM, ReqData);
									logger.info("retry data saved " + TM.getId() + " status=" + status);
								}

								logger.info("PGTransaction.java after saveData()::: " + PGUtils
										.generateLog(TM.getMerchantId(), TM.getTxnId(), TM.getId(), TM.getAmount()));

								if ((TM.getId() != null) && (TM.getId().length() == 19)) {

									request.setAttribute("pgid", TM.getId());
									session.setAttribute("cToken", sCheckSum);
									logger.info("PGTransaction.java :: for id=" + TM.getId()
											+ " session.setAttribute : cToken : " + session.getAttribute("cToken"));
									request.setAttribute("checksum", sCheckSum);

									if (TM.getTxnType().equalsIgnoreCase("REDIRECT")
											|| TM.getTxnType().equalsIgnoreCase("seamless")) {

										request.setAttribute("instrumentId", json.getString("MOP"));
										request.setAttribute("cardType", json.getString("MOPType"));

										logger.info("=========Redirecting to PGRedirector===============");

										request.getRequestDispatcher("/PGRedirector").include(request, response);

									} else if ((merchantDTO.getIsVAS() != null)
											&& (merchantDTO.getIsVAS().equalsIgnoreCase("1"))) {

										Vector storedCardDetailsCC = dm.getStoredCardDetailsCC();
										request.setAttribute("cardDetails", storedCardDetailsCC);
										request.setAttribute("instrumentId", json.getString("instrumentId"));

										logger.info("=========Redirecting to indexVAS===============");
										request.getRequestDispatcher("/indexVAS.jsp").include(request, response);

									} else if ((merchantDTO.getIsVAS() != null)
											&& (merchantDTO.getIsVAS().equalsIgnoreCase("0"))) {
										Vector storedCardDetailsCC = dm.getStoredCardDetailsCC();
										request.setAttribute("cardDetailsCC", storedCardDetailsCC);
										Vector storedCardDetailsDC = dm.getStoredCardDetailsDC();
										request.setAttribute("cardDetailsDC", storedCardDetailsDC);
										logger.info("=========Redirecting to index===============");
										request.getRequestDispatcher("/index.jsp").include(request, response);
									} else {
										logger.info(
												"PGTransaction.java ::: Is_VAS value is : " + merchantDTO.getIsVAS());
									}
								} else if ((TM.getId() != null) && (TM.getId().equalsIgnoreCase("0"))) {
									String errorString = null;
									if (TM.getTxnType().equalsIgnoreCase("REDIRECT")
											|| TM.getTxnType().equalsIgnoreCase("seamless")) {
										errorString = PGUtils.redirectToErrorPage("10051",
												"Duplicate Merchant Transaction Id.Merchant Transaction Id '"
														+ TM.getTxnId() + "' already Processed.",
												TM.getReturn_url(), "txnErrorMsg");
										out.println(errorString);
										// out.flush();
										// out.close();
									} else {
										request.setAttribute("errorMsg",
												"Error 10051 : Duplicate Merchant Transaction Id.Merchant Transaction Id '"
														+ TM.getTxnId() + "' already Processed.");
										request.getRequestDispatcher("txnValidationErrors.jsp").include(request,
												response);
									}
								} else {
									String errorString = null;
									if (TM.getTxnType().equalsIgnoreCase("REDIRECT")
											|| TM.getTxnType().equalsIgnoreCase("seamless")) {
										errorString = PGUtils.redirectToErrorPage("10052",
												"Error while Processing Transcation Request.", TM.getReturn_url(),
												"txnErrorMsg");
										out.println(errorString);
										// out.flush();
										// out.close();
									} else {
										request.setAttribute("errorMsg",
												"Error 10052 : Error while Processing Transcation Request.");
										request.getRequestDispatcher("txnValidationErrors.jsp").include(request,
												response);
									}
								}
							} else {
								String errorString = null;
								if (TM.getTxnType().equalsIgnoreCase("REDIRECT")
										|| TM.getTxnType().equalsIgnoreCase("seamless")) {
									errorString = PGUtils.redirectToErrorPage(handler.getErrorCode(),
											handler.getErrorMessage(), TM.getReturn_url(), "txnErrorMsg");
									out.println(errorString);
									// out.flush();
									// out.close();
								} else {
									request.setAttribute("errorMsg",
											"Error " + handler.getErrorCode() + " : " + handler.getErrorMessage());
									request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
								}
							}
						} else if (i == 0) {
							request.setAttribute("errorMsg", "Error To Validate Global Fields");
							request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
						} else if (i == 2) {
							request.setAttribute("errorMsg", "Other Country Transaction will Not Proceed");
							request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
						} else if (i == 3) {
							request.setAttribute("errorMsg", "IP Blocked Please Contact PG-Support");
							request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
						}
					} else {
						request.setAttribute("errorMsg", sResp);
						request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
					}

				} else {
					logger.info("Server IP Address:::::Not Registerd:::::::::::::::::::::::: " + ipAddress);

					response.getWriter().write("{\"RespMsg\":\"IP IS NOT WHITE-LISTED\"}");
					return;

				}
			} else {
				request.setAttribute("errorMsg", "Error while Decrypting Transaction Request.");
				request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.setAttribute("errorMsg", "Error 10052 : Error while Processing Transcation Request.");
			request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
		} finally {
			out.flush();
			out.close();
		}
	}

	// added on 02-03-23
	public static boolean containsWhiteSpace(final String testCode) {
		if (testCode != null) {
			for (int i = 0; i < testCode.length(); i++) {
				if (Character.isWhitespace(testCode.charAt(i))) {
					return true;
				}
			}

		}
		return false;
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