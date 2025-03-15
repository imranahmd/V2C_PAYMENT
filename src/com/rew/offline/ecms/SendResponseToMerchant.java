//package com.rew.offline.ecms;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.Unirest;
//import com.rew.payment.utility.PGUtils;
//import com.rew.pg.db.DataManager;
//import com.rew.pg.dto.MerchantDTO;
//import com.rew.pg.dto.TransactionMaster;
//
//public class SendResponseToMerchant extends HttpServlet {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 4026718361819138490L;
//	  private static final Logger logger = LoggerFactory.getLogger(SendResponseToMerchant.class);
//
//	private static String PASS = ";(frLgxZ<R+yr+ZX`>WP$)fL[mr]'E@$G),f;Jp~QkvN79P@@,aRTPky2;^j7`Hs/u<827CaVd;2S>uk~uBW?2qK=+J=q,4.=8Zh@aD?}33jQ/]+dcNG\\az**NL5'9:C";
//
//	public SendResponseToMerchant() {
//		super();
//	}
//
//	protected void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		response.sendError(HttpServletResponse.SC_FORBIDDEN);
//	}
//
//	@Override
//	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		if (req.getParameter("pass").equals(PASS)) {
//			int status = sendResponseToMerchant(req.getParameter("id"));
//			if(status==1) {
//				resp.setStatus(200);
//			}
//			else 
//				resp.setStatus(500);
//		}
//		else
//			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
//	}
//
//	private int sendResponseToMerchant(String txnId) {
//		int status = 0;
//		DataManager dm = new DataManager();
//		TransactionMaster txnMaster = dm.getTxnMasterByTxnId(txnId);
//		String mid=txnMaster.getMerchantId();
//		MerchantDTO mstMert=dm.getPushUrl(mid);
//		logger.info("mstMert::::"+mstMert);
//		String encRespXML = PGUtils.generateResponse(txnMaster);
//		logger.info("tm object encRespXML " + encRespXML);
//		System.out.println(":::::::::::::::::::"+txnMaster.getIsPostedBackRes()+"::::::::"+mstMert.getPushUrl()+"::::::::"+txnMaster.getReturn_url());
//
////		if (txnMaster.getIsPostedBackRes().equals("0") && encRespXML != null && (txnMaster.getReturn_url() != null || !txnMaster.getReturn_url().isEmpty())) {
//			
//		if (txnMaster.getIsPostedBackRes().equals("0") && encRespXML != null && (mstMert.getPushUrl() != null || !mstMert.getPushUrl().isEmpty())) {
//			logger.info(" ::: inside if--------------------------------" + mstMert.getPushUrl());
//			Unirest.setTimeouts(0, 0);
//			logger.info("Unirest.setTimeouts::::::::");
//			try {
//				logger.info("inside:::::::::"+logger);
//				HttpResponse<String> response = Unirest.post(mstMert.getPushUrl())
//						.header("Content-Type", "application/x-www-form-urlencoded")
//						.field("merchantId", txnMaster.getMerchantId())
//						.field("respData", encRespXML)
//						.field("pgid", txnMaster.getId())
//						.asString();
//				if (response.getStatus() == 200) {
//					logger.info("Response sent to the merchant {}",txnMaster.getMerchantId());
//					dm.updateIsPostedBackRes(txnMaster.getId());
//					status = 1;
//					return status;
//				}
//			} 
//			catch (Exception e) {
//				logger.error("Error in sending response to the merchant", e);
//			}
//		}
//		else {
//			logger.error("Response is already posted to the merchant");
//			status=1;
//		}
//		return status;
//	}
//}
