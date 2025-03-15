//package com.rew.offline.ecms;
//
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.json.simple.parser.JSONParser;
//
//import com.metaparadigm.jsonrpc.JSONSerializer;
//import com.rew.payment.router.IDynamicPGSelector;
//import com.rew.payment.utility.PGUtils;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class ECMSChallanProcessor implements IDynamicPGSelector {
//
//  private static final int ACCOUNT_NO_LENGTH_WIHOUT_PREFIX = 14;
//
//	static Logger log = LoggerFactory.getLogger(ECMSChallanProcessor.class.getName());
//
//	@Override
//	public String redirectToPG(HttpServletRequest request) 
//	{
//             //PrintWriter out= response.getWriter();
//		log.info("inside ECMSChallanProcessor");
//		
//		HttpSession session = request.getSession();
//		
//		String id = (String) request.getAttribute("id");
//		String amount = (String) request.getAttribute("amount");
//		String merchantId = request.getAttribute("Mid").toString();
//		String MerchantTxnId = (String) request.getAttribute("Merchant_txn_id");
//		String spId = (String) request.getAttribute("spId");
//		String paymode = (String) request.getAttribute("bankId");
//		String instrumentId = (String)request.getAttribute("instrumentId");
//		
//		
//		log.info("spid >>>> " + spId);
//		StringBuilder response = new StringBuilder();
//		
//		try {
//			log.info("ECMSChallanGeneration.java ::: " );
//			String errorString;
//			if (session.getAttribute("cToken") != null && request.getAttribute("checksum") != null
//					&& session.getAttribute("cToken").toString().equals(request.getAttribute("checksum").toString())) {
//			
//			
//			String utrNo = request.getAttribute("id").toString();
//
//			ChallanDao dao = new ChallanDao();
//
//			String resp = dao.getPgParameters(utrNo);
//	        String custname = "";
//	        String ecmsva = "";
//			JSONObject res = new JSONObject(resp);
//			//JSONObject json = (JSONObject) JSONSerializer.toJSON(resp);        
//			JSONArray jsonarray = new JSONArray(res.getString("udf6"));
//			for (int i = 0; i < jsonarray.length(); i++) {
//			    JSONObject jsonobject = jsonarray.getJSONObject(i);
//			    if(jsonobject.has("custname")) {
//			    custname = jsonobject.getString("custname");
//			    }
//			    if(jsonobject.has("ecmsva")) {
//			    ecmsva = jsonobject.getString("ecmsva");
//			    }
//			}
//
//			
//
//log.info("res::::::::::::::"+jsonarray);
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String date = simpleDateFormat.format(new Date());
//        String reference_no = res.getString("refNo");// txnid
////        String cust_name = res.getString("udf1");
////        String walletID = res.getString("udf3");
//        String udf6value = res.getString("udf6");
//
//        
//        log.info("custname:::::::::"+custname+"::::::::"+"ecmsva::::"+ecmsva);
//        String beneficiaryName = PGUtils.getPropertyValue("beneficiaryName");
//        String ifsc = PGUtils.getPropertyValue("ifsc");
//        String bank = PGUtils.getPropertyValue("bank");
//        String branch = PGUtils.getPropertyValue("branch");
//
//        String txn_date = date;
//        String email = res.getString("email_id");
//        String phone = res.getString("mobile_no");
//        String ru = res.getString("ru");
//        String acc_no = null;
//
//        String sRandomCustId = null;
//        final String ACC_NO_PREFIX = dao.getAccNoPrefix(merchantId).get();
////        if (merchantId.equalsIgnoreCase("M00072")) { //Handling HDFC fasttag merchant
//        if (merchantId.equalsIgnoreCase("M00072")||merchantId.equalsIgnoreCase("M00066")||merchantId.equalsIgnoreCase("M0000515")) { //Handling HDFC fasttag merchant
//
////          if(walletID.length() != 14) {
//            if(ecmsva.length() != 20) {
//
//        	        errorString = PGUtils.redirectToErrorPage("Invalid WalletID",
//        	            "Wallet ID posted is not valid.",
//        	            "txnValidationErrors.jsp", "error");
//        	        return errorString; 
//          }
////          acc_no = ACC_NO_PREFIX.concat(walletID);
////          sRandomCustId = walletID;
//            acc_no = ACC_NO_PREFIX.concat(ecmsva);
//            log.info("acc_no:::::::::"+acc_no);
//            
//            sRandomCustId = ecmsva;
//            log.info("sRandomCustId:::::::::"+sRandomCustId);
//
//        } else {
//          sRandomCustId = generateRandomNumber(ACCOUNT_NO_LENGTH_WIHOUT_PREFIX);
//          acc_no = ACC_NO_PREFIX + sRandomCustId;
//        }
//
//			log.info("reference_no " + reference_no + "beneficiaryName:::" + beneficiaryName + "acc_no:::" + acc_no
//					+ "ifsc" + ifsc	+ "bank" + bank + "branch" + branch + "amount" + amount + "cust_name" + custname + "txn_date"
//					+ txn_date + "email" + email + "phone" + phone);
//
//			if ((!reference_no.equalsIgnoreCase(null) && !reference_no.equalsIgnoreCase("")) &&
//
//					(!beneficiaryName.equalsIgnoreCase(null) && !beneficiaryName.equalsIgnoreCase("")) &&
//
//					(!acc_no.equalsIgnoreCase(null) && !acc_no.equalsIgnoreCase("")) &&
//
//					(!ifsc.equalsIgnoreCase(null) && !ifsc.equalsIgnoreCase("")) &&
//
//					(!bank.equalsIgnoreCase(null) && !bank.equalsIgnoreCase("")) &&
//
//					(!branch.equalsIgnoreCase(null) && !branch.equalsIgnoreCase("")) &&
//
//					(!amount.equalsIgnoreCase(null) && !amount.equalsIgnoreCase("")) &&
//
//					(!custname.equalsIgnoreCase(null) && !custname.equalsIgnoreCase("")) &&
//
//					(!txn_date.equalsIgnoreCase(null) && !txn_date.equalsIgnoreCase("")) &&
//
//					(!email.equalsIgnoreCase(null) && !email.equalsIgnoreCase("")) &&
//
//					(!phone.equalsIgnoreCase(null) && !phone.equalsIgnoreCase("")))
//
//			{
//
//				
//
//				log.info("Challan Data: reference_no = "+reference_no);
//				log.info("Challan Data: beneficiaryName = "+beneficiaryName);
//				log.info("Challan Data: acc_no = "+acc_no);
//				log.info("Challan Data: ifsc = "+ifsc);
//				log.info("Challan Data: bank = "+bank);
//				log.info("Challan Data: branch = "+branch);
//				log.info("Challan Data: amount = "+amount);
//				log.info("Challan Data: cust_name = "+custname);
//				log.info("Challan Data: txn_date = "+txn_date);
//				log.info("Challan Data: email = "+email);
//				log.info("Challan Data: phone = "+phone);
//				log.info("Challan Data: utrNo = "+utrNo);
//				log.info("Challan Data: instrumentId = "+instrumentId);
//				log.info("Challan Data: sRandomCustId = "+sRandomCustId);
//
//
//
//          createRequest(amount, paymode, response, utrNo, acc_no, sRandomCustId, reference_no,
//        		  custname, beneficiaryName, ifsc, bank, branch, txn_date, email, phone, ru);
//
//          return response.toString();
//        }
//
//      } else {
//        log.info("ECMSChallanProcessor.java ::: "
//            + PGUtils.generateLog(merchantId, MerchantTxnId, id, amount));
//        log.info("ECMSChallanProcessor.java ::: Token Mismatch :: Token in Session --> "
//            + session.getAttribute("cToken") + " And Token in Request --> "
//            + request.getAttribute("checksum"));
//        errorString = PGUtils.redirectToErrorPage("Token Mismatch",
//            "Invalid Request Parameters.Direct Parameter Posting is not allowed.",
//            "txnValidationErrors.jsp", "error");
//        return errorString;
//      }
//    } catch (Exception var12) {
//      log.info("ECMSChallanProcessor.java ::: "
//          + PGUtils.generateLog(merchantId, MerchantTxnId, id, amount));
//      log.error("ECMSChallanProcessor.java ::: Exception Caught while processing request : ",
//          var12);
//      return null;
//    }
//    return null;
//  }
//
//
//  private String generateRandomNumber(int length) {
//    String AlphaNumericString = "0123456789";
//    StringBuilder sb = new StringBuilder(length);
//    for (int i = 0; i < length; i++) {
//      int index = (int) (AlphaNumericString.length() * Math.random());
//      sb.append(AlphaNumericString.charAt(index));
//    }
//
//    return sb.toString();
//  }
//
//  private boolean validateRequestAttribute(List<String> params) {
//
//    long validationerror =
//        params.stream().filter(item -> item == null || item.isEmpty()).map(item -> {
//          log.error(item + "is empty or null.");
//          return item;
//        }).count();
//    if (validationerror > 0) {
//      return false;
//    }
//    return true;
//  }
//
//  private void createRequest(String amount, String paymode, StringBuilder response, String utrNo,
//      String acc_no, String sRandomCustId, String reference_no, String cust_name,
//      String beneficiaryName, String ifsc, String bank, String branch, String txn_date,
//      String email, String phone, String ru) {
//
//    String urlFinal = PGUtils.getPropertyValue("ECMS_VIECHALLAN_URL");
//
//    
//    log.info("urlFinal   " + urlFinal);
//    response.append("<HTML>");
//    response.append("<BODY>");
//    response.append("<FORM ACTION = \"" + urlFinal + "\" METHOD='POST'>");
//    response.append("<input type=\"hidden\" name=\"reference_no\" value=\"" + reference_no + "\">");
//    response.append(
//        "<input type=\"hidden\" name=\"beneficiaryName\" value=\"" + beneficiaryName + "\">");
//    response.append("<input type=\"hidden\" name=\"acc_no\" value=\"" + acc_no + "\">");
//    response.append("<input type=\"hidden\" name=\"ifsc\" value=\"" + ifsc + "\">");
//    response.append("<input type=\"hidden\" name=\"bank\" value=\"" + bank + "\">");
//    response.append("<input type=\"hidden\" name=\"branch\" value=\"" + branch + "\">");
//    response.append("<input type=\"hidden\" name=\"amount\" value=\"" + amount + "\">");
//    response.append("<input type=\"hidden\" name=\"cust_name\" value=\"" + cust_name + "\">");
//    response.append("<input type=\"hidden\" name=\"txn_date\" value=\"" + txn_date + "\">");
//    response.append("<input type=\"hidden\" name=\"email\" value=\"" + email + "\">");
//    response.append("<input type=\"hidden\" name=\"phone\" value=\"" + phone + "\">");
//    response.append("<input type=\"hidden\" name=\"utrNo\" value=\"" + utrNo + "\">");
//    response.append("<input type=\"hidden\" name=\"ru\" value=\"" + ru + "\">");
//    response.append("<input type=\"hidden\" name=\"paymode\" value=\"" + paymode + "\">");
//    response.append("<input type=\"hidden\" name=\"customerId\" value=\"" + sRandomCustId + "\">");
//    response.append("</FORM>");
//    response.append("</BODY>");
//
//				response.append("<SCRIPT>");
//				response.append("document.forms[0].submit();");
//				response.append("</SCRIPT>");
//				response.append("</HTML>");
//				
//				
//			} 
//			
//}
