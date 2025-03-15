package com.rew.upi.hdfc;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.payment.utility.PGUtils;
import com.rew.pg.db.DataManager;
import com.rew.pg.dto.TransactionMaster;

@WebServlet("/UpiVerification")
public class UpiVerification extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(UpiVerification.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("mTrackId");
		logger.debug("id ::::::::::::::::" + id);

		String tranStatus = null, message = null, errorCode = null;

		String verificationResponse = null;
		try {
			DataManager dataManager = new DataManager();
			TransactionMaster txnMaster = dataManager.getTxnMaster(id);

			verificationResponse = checkTxnStatus(txnMaster);
			logger.debug("verificationResponse ::::::::::::::::" + verificationResponse);
			JSONObject jsonverificationResponse = new JSONObject(verificationResponse);
			String resp_date_Time = new Timestamp(new Date().getTime()) + "";
			if (jsonverificationResponse.getString("PARAM4").equalsIgnoreCase("SUCCESS")) {
				tranStatus = "Ok";
				message = "Transaction Successful.";
				errorCode = "00000";

				txnMaster.setTransStatus(tranStatus);
				txnMaster.setServiceRRN(jsonverificationResponse.getString("PARAM0")); // EnMPIResRef
				txnMaster.setServiceTxnId(jsonverificationResponse.getString("PARAM9")); // EnMPIResTranid
				txnMaster.setServiceAuthId(jsonverificationResponse.getString("PARAM0")); // EnMPIResPayid
				txnMaster.setRespStatus("1");
				txnMaster.setInstrumentId("UPI");
				txnMaster.setBankId("UPI");
				txnMaster.setRespMessage(message);
				txnMaster.setErrorCode(errorCode);
				txnMaster.setSpErrorCode(errorCode);
				txnMaster.setRespDateTime(resp_date_Time);
				txnMaster.setModified_By("RespHandelerHDFCUPI");
				txnMaster.setModified_On(resp_date_Time); //
				logger.debug("RespHandlerHDFCUPI.java :: now upadte the trnsaction ... ");

				dataManager.updateTxn(txnMaster);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String checkTxnStatus(TransactionMaster txnMaster) throws Exception {
		DataManager dataManager = new DataManager();
		String orderNo = txnMaster.getTxnId();
		String email = txnMaster.getCustMail();
		String number = txnMaster.getCustMobile();
		String spId = PGUtils.getPropertyValue("spIdHDFCUPI");
		String bankId = "UPI";
		String instrumentId = "UPI";
		String merchantId = txnMaster.getMerchantId();
		String transactionId = txnMaster.getId();
		ArrayList<String> spMidKey = dataManager.getSPMidKey(merchantId, bankId, instrumentId, spId);

		String mid = spMidKey.get(0);
		String upiKeyHDFC = spMidKey.get(1);

		String upiCheckTxnStatusUrlHDFC = PGUtils.getPropertyValue("upiCheckTxnStatusUrlHDFC"); // "https://upitest.hdfcbank.com/upi/transactionStatusQuery"

		// String sData = mid+"|"+transactionId+"|"+ orderNo +"|"+orderNo+"|"+ email
		// +"|"+ number +"|||||||NA|NA";
		String sData = mid + "|" + transactionId + "|||||||||||NA|NA";
		logger.debug("---------------Verification request-----------" + sData);

		sData = new UPISecurity().encrypt(sData, upiKeyHDFC);
		JSONObject json = new JSONObject();
		json.put("requestMsg", sData);
		json.put("pgMerchantId", mid);
		logger.debug("RespHandlerHDFCUPI.java ::: Verification() :: UPI Request :: " + json.toString());
		PostToSoR pso = new PostToSoR();
		sData = pso.postData(upiCheckTxnStatusUrlHDFC, json.toString());
		logger.debug("RespHandlerHDFCUPI.java ::: encrypted Response :: " + sData);
		sData = new UPISecurity().decrypt(sData, upiKeyHDFC);
		logger.debug("RespHandlerHDFCUPI.java ::: Verification() :: Decrypted Data : " + sData);
		// Merchant Ref No,Payer Virtual Address,Payer Name,Status1,Status2
		sData = createJsonResponse(sData);
		logger.debug("RespHandlerHDFCUPI.java ::: Verification() :: Json Response Data : " + sData);
		return sData;
	}

	private static String createJsonResponse(String pipeSeperatedValue) {
		JSONObject json = new JSONObject();

		String[] strResp = pipeSeperatedValue.split("\\|");
		for (int i = 0; i < strResp.length; i++) {
			String value = strResp[i];
			json.put("PARAM" + i, value);
		}

		return json.toString();
	}

}