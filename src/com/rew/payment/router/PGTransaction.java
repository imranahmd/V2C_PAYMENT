package com.rew.payment.router;

import com.rew.payment.utility.AesBase64Wrapper;
import com.rew.payment.utility.PGUtils;
import com.rew.pg.db.DataManager;
import com.rew.pg.dto.MerchantDTO;
import com.rew.pg.dto.PGTransactionDTO;
import com.rew.pg.dto.ResellerDTO;
import com.rew.pg.dto.TransactionMaster;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PGTransaction extends HttpServlet {
	private static Logger logger = LoggerFactory.getLogger(PGTransaction.class);
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

		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		Enumeration params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String paramName = (String) params.nextElement();
			String paramValue = request.getParameter(paramName);
			logger.info("PGTransaction.java ::: Param Name : " + paramName + " :: Param Value : " + paramValue);
		}

		logger.info("PGTransaction.java ::: Start capturing params....");
		try {
			// DataManager dm=new DataManager();
			DataManager Dm = new DataManager();

			PGTransactionDTO dm = new PGTransactionDTO();
			logger.debug("Start get data by merchant id= " + session.getId().toString());
			dm = dm.getData(request.getParameter("merchantId"), request.getParameter("reqData"));
			logger.debug("End get data by merchant id= " + session.getId().toString());
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

			String date = json.getString("dateTime");
			String udf6 = null;
			logger.info("Request Date ====>" + date);
			if (json.has("udf6")) {
				String logge = json.getString("udf6");
				logger.info("logge:::::::::::::::::::::::::::::::::::::::::::::::::" + logge);

				if (logge.equalsIgnoreCase("") || logge == null) {
					udf6 = "NA";
					json.remove("udf6");
					json.put("udf6", udf6);
					logger.info("Udf6 Value::::{} ====>" + json.getString("udf6"));
				}
			} else {
				json.put("udf6", "NA");

			}
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			json.remove("dateTime");
			json.put("dateTime", date);
			logger.info("overridden Date ====>" + date);

			if (json != null) {

				ResellerDTO resellerDTO = Dm.getReseller(merchantDTO.getReseller_Id());

				logger.info("inside json  :::::" + resellerDTO.getReseller_name() + ":::::::::::;;====>" + json);
				if (resellerDTO.getIntegration_type().equalsIgnoreCase("1")
						|| resellerDTO.getIntegration_type().equalsIgnoreCase("2")) {
					if (json.has("Rid") && merchantDTO.getReseller_Id() != null) {
						logger.info("inside merchantDTO.getReseller_Id()  :::" + json.has("Rid")
								+ ":::::::::::::;;====>" + merchantDTO.getReseller_Id());
						logger.info("::::::::::::::::::::::::reseller :::::::" + resellerDTO.getIntegration_type());
						if (merchantDTO.getReseller_Id().equalsIgnoreCase(json.getString("Rid"))) {
							logger.info("Reseller_Id Match From Db level  ---====>" + date);
							request.getRequestDispatcher("/RTransaction").forward(request, response);
							logger.info("Reseller Id In Json::::::::::::::::::: ====>" + json.getString("Rid"));
						} else {
							request.setAttribute("errorMsg",
									"Error 1055 : Reseller_Id Mismatch or wrong Reseller integration type");
							request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
						}
					} else {
						request.setAttribute("errorMsg",
								"Error 1055 : Reseller_Id Not found for this merchant " + merchantDTO.getMerchantId());
						request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
					}
				} else {
					logger.info("inside else:::::Id() 0");
					if (json.has("Rid") && !json.getString("Rid").equalsIgnoreCase("NA")) {
						request.setAttribute("errorMsg", "Error 1055 : Merchant passing wrong parameter");
						request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
						return;
					}
					json.put("Rid", "NA");
					json.put("ResellerTxnId", "NA");

					String[] reqParams = { "merchantId", "txnId", "dateTime", "amount", "custMobile", "custMail",
							"returnURL", "apiKey", "udf1", "udf2", "udf3", "udf4", "udf5", "udf6", "channelId",
							"txnType", "instrumentId", "isMultiSettlement", "productId", "dateTime", "cardDetails",
							"cardType", "dateTime" };

					String sResp = PGUtils.null2Known(reqParams, json);// checking any parameter is null or blank

					if ((sResp != null) && (sResp.equals("NOTNULL"))) {

						// TransactionMaster TM=getTransactionMaster(json);//setting request details in
						// transaction master
						logger.debug("Start get cards data= " + session.getId().toString());
						dm = dm.getCardsData(json);
						logger.debug("End get cards data= " + session.getId().toString());
						TransactionMaster TM = dm.getTM();
						logger.info("PGTransaction.java ::: Transaction Master Created for mertxnid=" + TM.getTxnId());

						if (request.getHeader("X-Forwarded-For") == null) {
							TM.setHostAddress(request.getRemoteHost());
						} else {
							String ip = request.getHeader("X-Forwarded-For");
							if (ip.indexOf(",") == -1) {
								TM.setHostAddress(ip.toString());
							} else {
								StringTokenizer ipToken = new StringTokenizer(ip, ",");
								String sFirstIP = ipToken.nextToken();
								logger.info("PGTransaction.java ::: for mertxnid=" + TM.getTxnId() + " IP Address  :  "
										+ sFirstIP);
								TM.setHostAddress(sFirstIP.toString());
							}
						}

						DataManager DM = new DataManager();
						int midcount = DM.txnidverify(TM.getTxnId());
						logger.info(
								" midcheck----------------------------------down--------------check---------------------------------------------"
										+ midcount);
						if (midcount == 1 || midcount == 2) {
							logger.info("midcount------in--check---condition----------to--verify---" + midcount);
							String errorString = null;
							if (TM.getTxnType().equalsIgnoreCase("REDIRECT")) {
								errorString = PGUtils.redirectToErrorPage("10051",
										"Duplicate Transaction Id. '" + TM.getTxnId() + "' already Processed.",
										TM.getReturn_url(), "txnErrorMsg");
//								out.println(errorString);
								out.flush();
								out.close();
							} else {
								request.setAttribute("errorMsg", "Error 10051 : Duplicate Transaction Id.  '"
										+ TM.getTxnId() + "' already Processed.");
								request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
//								out.println(errorString);
								out.flush();
								out.close();

							}

						}

						logger.info("PGTransaction.java ::: "
								+ PGUtils.generateLog(TM.getMerchantId(), TM.getTxnId(), "NA", TM.getAmount()));
						logger.info("PGTransaction.java ::: for mertxnid=" + TM.getTxnId() + " Remote Address :: "
								+ request.getRemoteAddr());
						String ip = String.valueOf(TM.getMerchantId()) + "|" + TM.getTxnId() + "|" + TM.getDateTime()
								+ "|" + TM.getAmount() + "|" + TM.getApiKey() + "|" + TM.getCustMobile() + "|"
								+ TM.getCustMail() + "|" + TM.getChannelId() + "|" + TM.getTxnType();
						String sCheckSum = new AesBase64Wrapper().encryptAndEncode(ip, TM.getApiKey());
						TM.setChecksum(sCheckSum);// calculating checksum

						RequestHandler handler = new RequestHandler(TM);
						handler = handler.validateRequest(handler);

						if (handler.isValid()) {
							if (json.getString("txnType") != null
									&& json.getString("txnType").equalsIgnoreCase("REDIRECT")) {
								if ((json.getString("instrumentId").equalsIgnoreCase("DC"))
										|| (json.getString("instrumentId").equalsIgnoreCase("CC"))) {
									if (json.getString("cardType").equalsIgnoreCase("Rupay")) {
										TM.setBankId("Rupay");
									} else {
										TM.setBankId("FINPG");
									}
								} else if (json.getString("instrumentId").equalsIgnoreCase("UPI")) {
									TM.setBankId(json.getString("cardType"));
								}
								TM.setCardDetails(json.getString("cardDetails"));
							}
							if ((TM.getCustMail().equals(" ")) || (TM.getCustMail().equals(""))
									|| (TM.getCustMail().equalsIgnoreCase("NA"))) {
								TM.setCustMail("NA@abc.com");
							}
							logger.debug(
									"PGTransaction Start saving transaction master= " + session.getId().toString());
							TM = dm.saveTM(TM);
							logger.debug("PGTransaction End saving transaction master= " + session.getId().toString());
							if (merchantDTO.getIsRetry().equalsIgnoreCase("1")) {
								int status = dm.saveRetry(TM, request.getParameter("reqData"));
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

								if (TM.getTxnType().equalsIgnoreCase("REDIRECT")) {

									request.setAttribute("instrumentId", json.getString("instrumentId"));
									request.setAttribute("cardType", json.getString("cardType"));

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
									logger.info("PGTransaction.java ::: Is_VAS value is : " + merchantDTO.getIsVAS());
								}
							} else if ((TM.getId() != null) && (TM.getId().equalsIgnoreCase("0"))) {
								String errorString = null;
								if (TM.getTxnType().equalsIgnoreCase("REDIRECT")) {
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
									request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
								}

							}

							else {
								String errorString = null;
								if (TM.getTxnType().equalsIgnoreCase("REDIRECT")) {
									errorString = PGUtils.redirectToErrorPage("10052",
											"Error while Processing Transcation Request.", TM.getReturn_url(),
											"txnErrorMsg");
									out.println(errorString);
									// out.flush();
									// out.close();
								} else {
									request.setAttribute("errorMsg",
											"Error 10052 : Error while Processing Transcation Request.");
									request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
								}
							}
						} else {
							String errorString = null;
							if (TM.getTxnType().equalsIgnoreCase("REDIRECT")) {
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
					} else {
						request.setAttribute("errorMsg", sResp);
						request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
					}

				}
			} else {
				request.setAttribute("errorMsg", "Error while Decrypting Transaction Request.");
				request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
			}
		} catch (Exception ex) {
			logger.error("Error in PayFi  PGTransaction= " + ex);
			request.setAttribute("errorMsg", "Error 10052 : Error while Processing Transcation Request.");
			request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
		} finally {
			out.flush();
			out.close();
		}
	}

}