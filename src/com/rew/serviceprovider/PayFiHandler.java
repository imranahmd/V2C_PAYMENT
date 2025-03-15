package com.rew.serviceprovider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.payment.rms.ReconMangamentSystem;
import com.rew.payment.utility.PGUtils;
import com.rew.payment.utility.S2SCall;
import com.rew.pg.db.DataManager;
import com.rew.pg.dto.TransactionMaster;

@WebServlet("/payfiPayin_callback")
public class PayFiHandler extends HttpServlet {
	private static final String SALT = "P91Jsw3vRMvMvxmdHpIfU6G7MfsoYuAJ";
	private static Logger logger = LoggerFactory.getLogger(PayFiHandler.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doOption(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Option Method not Allowed.");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("CallBack Response PayFI Handler : " + request.toString());

		String resp_date_Time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());

		String tranStatus = null;
		String message = null;
		String errorCode = null;
		String apiKey = null;
		String orderId = null;
		String transactionId = null;
		String amount = null;
		String utr = null; // Unique reference number
		String status = null;
		String errorMsg = null;
		String payerVpa = null;
		String receivedHash = null;
		String AutoRecon = PGUtils.getPropertyValue("AutoRecon");
		try {
			Map<String, String[]> parameterMap = request.getParameterMap();

			Map<String, String> params = new TreeMap<>();
			for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
				params.put(entry.getKey(), entry.getValue()[0]);
			}

			receivedHash = params.get("hash");
			Map<String, String> responseParams = new TreeMap<>();
			responseParams.put("amount", params.get("amount"));
			responseParams.put("apiKey", params.get("apiKey"));
			responseParams.put("errorMsg", params.get("errorMsg"));
			responseParams.put("orderId", params.get("orderId"));
			responseParams.put("payerVpa", params.get("payerVpa"));
			responseParams.put("status", params.get("status"));
			responseParams.put("transactionId", params.get("transactionId"));
			responseParams.put("utr", params.get("utr"));

			logger.info("responseHashPayFi: " + receivedHash);

			String calculatedHash = calculateHash(responseParams, SALT);

			logger.info("Calculated hash: {}", calculatedHash);
			logger.info("Received hash: {}", receivedHash);

			// Validate the hash
			if (calculatedHash.equals(receivedHash)) {
				apiKey = params.get("apiKey");
				orderId = params.get("orderId");
				transactionId = params.get("transactionId");
				amount = params.get("amount");
				utr = params.get("utr"); // Unique reference number
				status = params.get("status");
				errorMsg = params.get("errorMsg");
				payerVpa = params.get("payerVpa");
				
				
				logger.info("apiKey: " + apiKey);
				logger.info("orderId: " + orderId);
				logger.info("transactionId: " + transactionId);
				logger.info("amount: " + amount);
				logger.info("utr: " + utr);
				logger.info("status: " + status);
				logger.info("errorMsg: " + errorMsg);
				logger.info("payerVpa: " + payerVpa);
			

				// Log successful hash validation
				logger.info("Hash validation successful for orderId: {}", orderId);

				// Send a success response
				response.setStatus(HttpServletResponse.SC_OK);
				PrintWriter out = response.getWriter();
				out.print("success");
				out.flush();
			} else {
				// Hash validation failed
				logger.warn("Hash validation failed for orderId: {}", orderId);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				PrintWriter out = response.getWriter();
				out.print("Hash validation failed");
				out.flush();
			}

			DataManager dm = new DataManager();
			if(orderId!= null) {
			TransactionMaster txnMaster = dm.getTxnMaster(orderId);
			logger.info("Retrieved TransactionMaster for orderId: {}", orderId);
		
			if (!txnMaster.getRespStatus().equalsIgnoreCase("1")) {
				logger.info("Processing transaction for orderId: {}", orderId);

				if (!PGUtils.isEmptyNull(payerVpa)) {
					txnMaster.setUdf6(getUpdatedUdf6(txnMaster.getUdf6(), payerVpa));
					logger.info("Updated Udf6 Value: {}", txnMaster.getUdf6());
				}

				logger.info("Transaction Status: {} for txnId: {}", status, txnMaster.getId());
				if (status.equalsIgnoreCase("2")) {
					tranStatus = "Ok";
					message = "Transaction Successful.";
					errorCode = "00000";
				} else if (status.equalsIgnoreCase("3")) {
					tranStatus = "F";
					message = "Transaction Failed.";
					errorCode = "FFFFF";
					logger.info("Transaction failed for orderId: {}", orderId);
				}
				
				if(status.equalsIgnoreCase("2") ||status.equalsIgnoreCase("3") ) {

				// Set transaction details
				txnMaster.setTransStatus(tranStatus);
				txnMaster.setServiceRRN(utr); // EnMPIResRef
				txnMaster.setServiceTxnId(transactionId); // EnMPIResTranid
				txnMaster.setRespStatus("1");
				txnMaster.setInstrumentId("UPI");
				txnMaster.setBankId("UPI");
				txnMaster.setRespMessage(message);
				txnMaster.setErrorCode(errorCode);
				txnMaster.setSpErrorCode(status);
				txnMaster.setRespDateTime(resp_date_Time);
				txnMaster.setModified_By("PayFiHandler");
				txnMaster.setModified_On(resp_date_Time);

				logger.info("Updating transaction with status: {}", tranStatus);
				dm.updateTxnIntent(txnMaster);
				
				}
				else {
					logger.info("Status Recived from PayFi is Not Success Or Not Failed ");
				}

				// Log recon system update
				if (txnMaster.getTransStatus().equalsIgnoreCase("Ok")
						|| txnMaster.getTransStatus().equalsIgnoreCase("F")) {
					ReconMangamentSystem recon = new ReconMangamentSystem();
					logger.info("Reconciling transaction for txnId: {}", txnMaster.getId());
//					recon.ReconService(txnMaster.getId(), txnMaster.getTransStatus(), txnMaster.getBankId(),
//							txnMaster.getMerchantId(), txnMaster.getAmount(), txnMaster.getTxnId(),
//							txnMaster.getProcessId(), txnMaster.getInstrumentId());
				
					if (AutoRecon.equalsIgnoreCase("On")) 
					{
						logger.info("if AutoRecon::::::::::::::is::"+AutoRecon);
					//	Recon.ReconService(TM.getId(), TM.getTransStatus(),TM.getBankId(), TM.getMerchantId(), TM.getAmount(), TM.getTxnId(), TM.getProcessId(),TM.getInstrumentId());
	
						recon.ReconService(txnMaster.getId(), txnMaster.getTransStatus(), txnMaster.getBankId(),
								txnMaster.getMerchantId(), txnMaster.getAmount(), txnMaster.getTxnId(),
								txnMaster.getProcessId(), txnMaster.getInstrumentId());
						
					}
					else
					{
						logger.info("else:::AutoRecon::::::::::::::is::"+AutoRecon);
					}
					
				
				
				
				
				}
			} else {
				logger.info("Response already updated for orderId: {}", orderId);
			}
			}
			else {
				logger.info("Transaction id is null Found : {}", orderId);
				
			}

			// Prepare JSON response
			JSONObject resp = new JSONObject();
			resp.put("Status", "SUCCESS");
			resp.put("Flag", "True");
			response.setContentType("application/json");
			response.getWriter().write(resp.toString());
		} catch (Exception e) {
			logger.error("Error processing payment callback", e);
		}
	}

	private String getUpdatedUdf6(String udf6, String customerVpa) throws ParseException {
		if (PGUtils.isEmptyNull(udf6) || udf6.trim().equalsIgnoreCase("NA")) {
			JSONObject js = new JSONObject();
			js.put("CustomerVPA", customerVpa);
			return js.toString();
		} else {
			JSONObject js = new JSONObject(udf6);
			js.put("CustomerVPA", customerVpa);
			return js.toString();
		}
	}

	public static String merchantRefund(String Id, String Amount)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		JSONObject js = new JSONObject();
		String resp_date_Time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		Instant instant = Instant.parse(resp_date_Time.replace(' ', 'T') + "Z");

		// Convert Instant to a Unix timestamp in milliseconds
		double timestampMillis = instant.toEpochMilli();
		long timestampMillis1 = (long) timestampMillis;

		long unixTimestampMillis = System.currentTimeMillis();
		long plainNumber = (long) timestampMillis;
		logger.info("Inside Status::::::::::plainNumber::::::::::" + plainNumber);

		js.put("originalMerchantRequestId", Id);
		js.put("refundRequestId", "377373hdgg");
		js.put("refundAmount", Amount);
		js.put("refundType", "Online");
		js.put("merchantRefundVpa", "rewardoouat@rba");
		js.put("remarks", "Cancel");
		js.put("iat", String.valueOf(plainNumber));

		String sign = SHA256.getSignedContent(js.toString());

		return Id;
	}

	private String calculateHash(Map<String, String> params, String salt) {
		StringBuilder hashData = new StringBuilder(salt);
		params.keySet().stream().sorted().forEach(key -> {
			String value = params.get(key);
			if (value != null && !value.isEmpty()) {
				hashData.append("|").append(value);
			}
		});

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			byte[] hashBytes = digest.digest(hashData.toString().getBytes(StandardCharsets.UTF_8));
			StringBuilder hexString = new StringBuilder();
			for (byte b : hashBytes) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}
