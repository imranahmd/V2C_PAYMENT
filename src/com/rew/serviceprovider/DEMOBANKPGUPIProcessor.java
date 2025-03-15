package com.rew.serviceprovider;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.payment.router.IDynamicPGSelector;
import com.rew.payment.utility.PGUtils;
import com.rew.pg.db.DataManager;
import com.rew.pg.dto.TransactionMaster;

public class DEMOBANKPGUPIProcessor implements IDynamicPGSelector{

	private static Logger logger = LoggerFactory.getLogger(IDynamicPGSelector.class);
	
	@Override
	public String redirectToPG(HttpServletRequest request) {
		
		String responseData = null;
		
		  StringBuilder resp = null;
		    HttpSession session = request.getSession();
		    if ((session.getAttribute("cToken") != null) && (request.getAttribute("checksum") != null) && (session.getAttribute("cToken").toString().equals(request.getAttribute("checksum").toString())))
		    {
		      String instrumentId = request.getAttribute("instrumentId").toString().trim();
		      String VPA =request.getAttribute("VPA").toString();
			    String Type = (String) request.getAttribute("txnType");
			    DataManager datamanager = new DataManager();
			    String id = (String)request.getAttribute("id");
			    String amount = (String)request.getAttribute("amount");
			    String custMail = (String)request.getAttribute("custMail");
			    String custMob = (String)request.getAttribute("custMob");
			    String merchantId = (String)request.getAttribute("Mid");
			    String spId = (String) request.getAttribute("spId");
			    String cardType= (String) request.getAttribute("cardType");
					String resp_date_Time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				    String Merchant_txn_id = (String)request.getAttribute("Merchant_txn_id");


				ArrayList<String> spMidKey = datamanager.getSPMidKey(merchantId,"UPI","UPI",spId);
				TransactionMaster txnMaster = datamanager.getTxnMaster(id);

		      logger.info("VPA::::::::"+VPA);
		      
		      logger.info("DEMOBANKPGUPIProcessor ::: Instrument Id is : " + instrumentId);
		      
		      try
		      {
		        if ((instrumentId != null) && (instrumentId.equals("UPI")))
		        {
		        	
		        	
		        if(VPA.equalsIgnoreCase("I")) {
		        	
		        	logger.info("inside the intend");
		        	
		      
		        	
//		        String id=request.getAttribute("id").toString();
		        String am =request.getAttribute("amount").toString();
		        	
//		     
		        String url = "upi://pay?ver=01&mode=05&tr="+id+"&am="+am+"&pa=demo@Vpay&pn=vPay-test-UPI";
		        logger.info("url:::::"+url);
		        
		        JSONObject js = new JSONObject();
			      
			       
			       String urldate = URLEncoder.encode(url, StandardCharsets.UTF_8);
			       logger.info("urldate:::::"+urldate);

			       
			       js.put("qrString",urldate);
			       logger.info("js:::::"+js.toString());
			       if (urldate !=null && cardType.equalsIgnoreCase("INTENT"))
					{
					    StringBuilder resp1 = new StringBuilder();
					    
					    resp1.append("<HTML>");
					    resp1.append("<BODY>");
					    
					
							resp1.append("<FORM ACTION='UPIINTENTPage.jsp' METHOD='POST' enctype='application/json'> ");
					



					    resp1.append("<input type=\"hidden\" name=\"MTrackid\" value=\"" + id + "\">");
					    resp1.append("<input type=\"hidden\" name=\"VPA\" value=\"" + VPA + "\">");
					    resp1.append("<input type=\"hidden\" name=\"merchantRefNo\" value=\"" + Merchant_txn_id + "\">");
					    resp1.append("<input type=\"hidden\" name=\"amt\" value=\"" + amount + "\">");
					    resp1.append("<input type=\"hidden\" name=\"Mid\" value=\"" + spMidKey.get(0) + "\">");
					    resp1.append("<input type=\"hidden\" name=\"QRUrl\" value=\"" + url + "\">");


					    resp1.append("</FORM>");
					    resp1.append("</BODY>");
					    resp1.append("<SCRIPT>");
					    resp1.append("document.forms[0].submit();");
					    resp1.append("</SCRIPT>");
					    resp1.append("</HTML>");
					    
					    responseData = resp1.toString();
					    
				        HttpSession session1=request.getSession();  
				        session1.setAttribute("merchantRefNo",merchantId);  
				        session1.setAttribute("MTrackid",id);  
				        session1.setAttribute("amt",amount);  	       
					}else {
			       String EncryptedResponseForIntent =   NPSTUPIProcessor.CreateResponse(js,id);

			       logger.info("EncryptedResponseForIntent::::::::::"+EncryptedResponseForIntent);
			       
			       responseData =EncryptedResponseForIntent;
					}
		    	}
		        else 
		        {
		        	if(Type.equalsIgnoreCase("REDIRECT")|| Type.equalsIgnoreCase("seamless"))
					{
						logger.info("Semless payment Value:::::::::"+Type);
						txnMaster.setTransStatus("Pending");  
						txnMaster.setServiceRRN("NA");    // EnMPIResRef
						txnMaster.setServiceTxnId("NA");  // EnMPIResTranid
						txnMaster.setServiceAuthId("NA");  // EnMPIResPayid
						txnMaster.setRespStatus("0");  
						txnMaster.setInstrumentId("UPI");
						txnMaster.setBankId("UPI");
						txnMaster.setCardDetails(VPA);
						txnMaster.setRespMessage("SUCCESS"); 
						txnMaster.setErrorCode("PPPP");
						txnMaster.setSpErrorCode("");
						txnMaster.setRespDateTime(resp_date_Time);  
						txnMaster.setModified_By("DEMOBANKPGUPIProcessor");
						txnMaster.setModified_On(resp_date_Time);
						 //String result = (new java.net.URI(JSONObject.getString("qrString")).getPath());
						txnMaster.setQrString("Collect");// 
						datamanager.updateTxn(txnMaster);
						String encRespXML = PGUtils.generateResponseForIntent(txnMaster);
						logger.info("Encrypt Value"+encRespXML);
						  JSONObject js1 = new JSONObject();
							 js1.put("merchantId", txnMaster.getMerchantId());
							 js1.put("respData", encRespXML);
							 js1.put("pgid", txnMaster.getId());
					    responseData = js1.toString();

					}else {
						resp = new StringBuilder();
				          
				          String postingUrl = PGUtils.getPropertyValue("postingUrlDemoBankUPI");
				          
				          resp.append("<HTML>");
				          resp.append("<BODY>");
				          resp.append("<FORM ACTION = \"" + postingUrl + "\" METHOD='POST'>");
				          resp.append("<input type=\"hidden\" name=\"PRN\" value=\"" + request.getAttribute("id").toString() + "\">");
				          resp.append("<input type=\"hidden\" name=\"AMT\" value=\"" + request.getAttribute("amount").toString() + "\">");
				          resp.append("<input type=\"hidden\" name=\"CRN\" value=\"INR\">");
				          resp.append("</FORM>");
				          resp.append("</BODY>");
				          
				          resp.append("<SCRIPT>");
				          resp.append("document.forms[0].submit();");
				          resp.append("</SCRIPT>");
				          resp.append("</HTML>");
					}
		        	
		        	
			       
		        }
		         
		        }
		      }
		      catch (Exception e)
		      {
		        logger.error("DEMOBANKPGUPIProcessor.java ::: Error Occurred :: ", e);
		        
		        resp = null;
		        return null;
		      }

		      if (resp != null) {
		        return resp.toString();
		      }
		    }
		    
		    else {
		    logger.info("DEMOBANKPGUPIProcessor.java ::: Token Mismatch :: Token in Session --> " + session.getAttribute("cToken") + " And Token in Request --> " + request.getAttribute("checksum"));
		    return PGUtils.redirectToErrorPage("10050", "Invalid Request Parameters.Direct Parameter Posting is not allowed.", "txnValidationErrors.jsp", "error");
		    }
		    
		    return responseData;
		    }
	
}

