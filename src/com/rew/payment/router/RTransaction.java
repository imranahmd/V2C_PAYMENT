package com.rew.payment.router;

import com.rew.payment.utility.AesBase64Wrapper;
import com.rew.payment.utility.PGUtils;
import com.rew.pg.db.DataManager;
import com.rew.pg.dto.MerchantDTO;
import com.rew.pg.dto.PGTransactionDTO;
import com.rew.pg.dto.PGTransactionDTOV2;
import com.rew.pg.dto.TransactionMaster;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
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

@WebServlet("/RTransaction")
public class RTransaction extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(RTransaction.class);
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
String ReqData=null;
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
			ReqData=request.getParameter("encData");
			
			
			//boolean IsWhiteSpace=containsWhiteSpace(request.getParameter("reqData"));
			
			boolean IsWhiteSpace=containsWhiteSpace(ReqData);
			
			logger.info("::::::::::::::::: value "+IsWhiteSpace);
			if(IsWhiteSpace)
			{
				logger.info(":::::::Enter the whit space :::RTransaction::::::: value "+IsWhiteSpace);
				char ch='+';
				String Url= request.getParameter("encData").replace(' ', ch);  
	            logger.info(Url);
	           // request.removeAttribute("reqData");
	            //request.setAttribute("reqData", Url);
				
	            request.removeAttribute("encData");
	            request.setAttribute("encData", Url);
	            logger.info(":::::::Url:::::::::: value "+Url);

	            ReqData=Url;
			}
			
			logger.info("Final Request Data:::::::::::::"+ReqData);
			// DataManager dm=new DataManager();
			//String type=(String) request.getAttribute("Type");
			String type=(String) request.getAttribute("type");
			
			logger.info("type  valueeee..."+type);
				PGTransactionDTOV2 dm = new PGTransactionDTOV2();

			
			logger.debug("Start get data by merchant id= " + session.getId().toString());
			//dm = dm.getData(request.getParameter("merchantId"), ReqData);//////remove request getparameters
			dm = dm.getData(request.getParameter("AuthID"), ReqData);//////remove request getparameters
			
			logger.debug("End get data by merchant id= " + session.getId().toString());
			// MerchantDTO merchantDTO =
			// dm.getMerchant(request.getParameter("merchantId"));//getting merchant request
			// deatils from merchantid
			MerchantDTO merchantDTO = dm.getMerchantDTO();
			logger.info("Reseller Transaction.java ::: request.getParameter(merchantId) : " + request.getParameter("merchantId"));
		
			// decrypting request in putting in jsonformat
			// JSONObject json = getJsonRequest(merchantDTO.getTransactionKey(),
			// request.getParameter("reqData"), request.getParameter("merchantId"));
			JSONObject json = dm.getJson();

			//String date = json.getString("dateTime");
		
			String date = json.getString("PaymentDate");
			
			String udf6=null;
			logger.info("Request Date ====>" + date);
			if(json.has("udf6"))
			 {			String logge=json.getString("udf6");
				logger.info("logge:::::::::::::::::::::::::::::::::::::::::::::::::"+logge);

					if(logge.equalsIgnoreCase("") || logge==null)
					{
						udf6 = "NA";
						json.remove("udf6");
						json.put("udf6", udf6);
						logger.info("Udf6 Value::::{} ====>" + json.getString("udf6"));
					}
			 }else {
				 json.put("udf6", "NA");

			 }
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			json.remove("dateTime");
			json.put("dateTime", date);
			logger.info("overridden Date ====>" + date);
		
			
			String ResellerTxnId = json.getString("ResellerTxnId");
			String Rid = json.getString("Rid");
			
			logger.info(Rid+" -----------------p"+ResellerTxnId);

			if (json != null) {
//					String[] reqParams = { "merchantId", "txnId", "dateTime", "amount", "custMobile", "custMail",
//						"returnURL", "apiKey", "udf1", "udf2", "udf3", "udf4", "udf5","udf6","ResellerTxnId","Rid","channelId", "txnType",
//						"instrumentId", "isMultiSettlement", "productId", "dateTime", "cardDetails", "cardType",
//						"dateTime" };
				
				String[] reqParams =	{"dateTime","EmailId","adf2","CustRefNum","adf1","ResellerTxnId","PaymentDate",
						"txn_Amount","udf6","Rid","MOP","CallbackURL","MOPDetails","AuthID","ContactNo",
						"MOPType","AuthKey","adf3","IntegrationType"};
					
				
				
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
					logger.info(" midcheck----------------------------------down--------------check---------------------------------------------"+midcount);
					 if(midcount == 1 || midcount == 2 ) { 
                         logger.info("midcount------in--check---condition----------to--verify---"+midcount);
							String errorString = null;
							if (TM.getTxnType().equalsIgnoreCase("REDIRECT") || TM.getTxnType().equalsIgnoreCase("seamless")) {
								errorString = PGUtils.redirectToErrorPage(
										"10051", "Duplicate Transaction Id. '"
												+ TM.getTxnId() + "' already Processed.",
										TM.getReturn_url(), "txnErrorMsg");
//								out.println(errorString);
								out.flush();
								out.close();
							} else {
								request.setAttribute("errorMsg",
										"Error 10051 : Duplicate Transaction Id.  '"
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
					String ip = String.valueOf(TM.getMerchantId()) + "|" + TM.getTxnId() + "|" + TM.getDateTime() + "|"
							+ TM.getAmount() + "|" + TM.getApiKey() + "|" + TM.getCustMobile() + "|" + TM.getCustMail()
							+ "|" + TM.getChannelId() + "|" + TM.getTxnType();
					String sCheckSum = new AesBase64Wrapper().encryptAndEncode(ip, TM.getApiKey());
					TM.setChecksum(sCheckSum);// calculating checksum

					RequestHandler handler = new RequestHandler(TM);
					handler = handler.validateRequest(handler);

					if (handler.isValid()) {
						//if (json.getString("txnType") != null && json.getString("txnType").equalsIgnoreCase("REDIRECT")) {
							
						if (json.getString("IntegrationType") != null && json.getString("IntegrationType").equalsIgnoreCase("REDIRECT")) {
							
						if((json.getString("instrumentId").equalsIgnoreCase("DC"))
											|| (json.getString("instrumentId").equalsIgnoreCase("CC"))) {
								if (json.getString("cardType").equalsIgnoreCase("Rupay")) {
									TM.setBankId("Rupay");
								} else {
									TM.setBankId("FINPG");
								}
							}else if(json.getString("instrumentId").equalsIgnoreCase("UPI")) {
								TM.setBankId(json.getString("cardType"));
							}
							TM.setCardDetails(json.getString("cardDetails"));
						}
						if ((TM.getCustMail().equals(" ")) || (TM.getCustMail().equals(""))
								|| (TM.getCustMail().equalsIgnoreCase("NA"))) {
							TM.setCustMail("NA@abc.com");
						}
						logger.debug("PGTransaction Start saving transaction master= " + session.getId().toString());
						TM = dm.saveTM(TM);
						logger.debug("PGTransaction End saving transaction master= " + session.getId().toString());
						if (merchantDTO.getIsRetry().equalsIgnoreCase("1")) {
							int status = dm.saveRetry(TM, ReqData);
							logger.info("retry data saved " + TM.getId() + " status=" + status);
						}

						logger.info("PGTransaction.java after saveData()::: "
								+ PGUtils.generateLog(TM.getMerchantId(), TM.getTxnId(), TM.getId(), TM.getAmount()));

						if ((TM.getId() != null) && (TM.getId().length() == 19)) {

							request.setAttribute("pgid", TM.getId());
							session.setAttribute("cToken", sCheckSum);
							logger.info("PGTransaction.java :: for id=" + TM.getId() + " session.setAttribute : cToken : "
									+ session.getAttribute("cToken"));
							request.setAttribute("checksum", sCheckSum);

							if (TM.getTxnType().equalsIgnoreCase("REDIRECT") || TM.getTxnType().equalsIgnoreCase("seamless")) {

								//request.setAttribute("instrumentId", json.getString("instrumentId"));
								//request.setAttribute("cardType", json.getString("cardType"));

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
								logger.info("PGTransaction.java ::: Is_VAS value is : " + merchantDTO.getIsVAS());
							}
						} else if ((TM.getId() != null) && (TM.getId().equalsIgnoreCase("0"))  ) {
							String errorString = null;
							if (TM.getTxnType().equalsIgnoreCase("REDIRECT")  || TM.getTxnType().equalsIgnoreCase("seamless")) {
								errorString = PGUtils.redirectToErrorPage(
										"10051", "Duplicate Merchant Transaction Id.Merchant Transaction Id '"
												+ TM.getTxnId() + "' already Processed.",
										TM.getReturn_url(), "txnErrorMsg");
								out.println(errorString);
								//out.flush();
								//out.close();
							} else {
								request.setAttribute("errorMsg",
										"Error 10051 : Duplicate Merchant Transaction Id.Merchant Transaction Id '"
												+ TM.getTxnId() + "' already Processed.");
								request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
							}
							
						}  
						else {
							String errorString = null;
							if (TM.getTxnType().equalsIgnoreCase("REDIRECT")  || TM.getTxnType().equalsIgnoreCase("seamless")) {
								errorString = PGUtils.redirectToErrorPage("10052",
										"Error while Processing Transcation Request.", TM.getReturn_url(),
										"txnErrorMsg");
								out.println(errorString);
								//out.flush();
								//out.close();
							} else {
								request.setAttribute("errorMsg",
										"Error 10052 : Error while Processing Transcation Request.");
								request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
							}
						}
					} else {
						String errorString = null;
						if (TM.getTxnType().equalsIgnoreCase("REDIRECT")  || TM.getTxnType().equalsIgnoreCase("seamless")) {
							errorString = PGUtils.redirectToErrorPage(handler.getErrorCode(), handler.getErrorMessage(),
									TM.getReturn_url(), "txnErrorMsg");
							out.println(errorString);
							//out.flush();
							//out.close();
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
			
			} else {
				request.setAttribute("errorMsg", "Error while Decrypting Transaction Request.");
				request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
			}
		} catch (Exception ex) {
			logger.error("Error in Pay Fi VAS PGTransaction= " + ex);
			request.setAttribute("errorMsg",
					"Error 10052 : Error while Processing Transcation Request.");
			request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
		} finally {
			out.flush();
			out.close();
		}
	}
	public static boolean containsWhiteSpace(final String testCode){
	    if(testCode != null){
	        for(int i = 0; i < testCode.length(); i++){
	            if(Character.isWhitespace(testCode.charAt(i))){
	                return true;
	            }
	        }
	    }
	    return false;
	}

}