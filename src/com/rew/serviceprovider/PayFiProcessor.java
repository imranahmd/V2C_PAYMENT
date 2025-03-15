package com.rew.serviceprovider;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.payment.router.IDynamicPGSelector;
import com.rew.payment.utility.S2SCall;
import com.rew.pg.db.DBConnectionHandler;
import com.rew.pg.db.DataManager;
import com.rew.pg.dto.ServiceProviderDto;

public class PayFiProcessor implements IDynamicPGSelector {

	private static Logger logger = LoggerFactory.getLogger(PayFiProcessor.class);

	@Override
	public String redirectToPG(HttpServletRequest request) {
		
		
		
		logger.info("===============================PayFiProcessor====================================");

		String id = (String) request.getAttribute("id");
		String amount = (String) request.getAttribute("amount");
		String custMail = (String) request.getAttribute("custMail");
		String custMob = (String) request.getAttribute("custMob");
		String merchantId = (String) request.getAttribute("Mid");
		String merchantRefNo = (String) request.getAttribute("Merchant_txn_id");
		String spId = (String) request.getAttribute("spId");
		String Type = (String) request.getAttribute("txnType");
		String resp_date_Time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String Merchant_txn_id = (String) request.getAttribute("Merchant_txn_id");
		String vpa = (String) request.getAttribute("VPA");
		String cardType = (String) request.getAttribute("cardType");
		String instrumentId = request.getAttribute("instrumentId").toString().trim();
		String VPA = (String) request.getAttribute("VPA");

		DataManager dm = new DataManager();
		
		ServiceProviderDto  spdto = dm.getServiceProviderById(Long.parseLong(spId));
		
		String API_URL = spdto.getUdf_1();
		String SALT = spdto.getUdf_2();
		
		
		logger.info("API_URL"+spdto.getUdf_1());
		logger.info("SALT"+ spdto.getUdf_2());
		logger.info("APIKEY "+spdto.getApi_key());
		
		
		logger.info("id: " + id);
		logger.info("amount: " + amount);
		logger.info("custMail: " + custMail);
		logger.info("custMob: " + custMob);
		logger.info("merchantId: " + merchantId);
		logger.info("merchantRefNo: " + merchantRefNo);
		logger.info("spId: " + spId);
		logger.info("type: " + Type);
		logger.info("resp_date_Time: " + resp_date_Time);
		logger.info("merchantTxnId: " + Merchant_txn_id);
		logger.info("vpa: " + vpa);
		logger.info("cardType: " + cardType);
		logger.info("instrumentId: " + instrumentId);

		String responseData = null;
		String IntentUrl = null;

		Map<String, String> params = new TreeMap<>();
		params.put("apiKey", spdto.getApi_key());
		params.put("orderId", id);
		params.put("amount", amount);
		params.put("name", merchantRefNo);
		params.put("phone", custMob);
		params.put("email", custMail);

		logger.info("Param : " + params.toString());

		try {

			String hash = getHash(params, SALT);

			logger.info(
					"Request Param For Server to Server Call  : " + params.toString() + "\n\n\n" + hash + "\n\n\n\n");

			JSONObject responseDataPayFi = S2SCall.servertoServerPayFi(params, hash, API_URL);
			logger.info("Response from " + responseDataPayFi.toString());

			if (responseDataPayFi != null) {

				String msg = responseDataPayFi.getString("msg");
				int code = responseDataPayFi.getInt("code");
				JSONObject data = responseDataPayFi.getJSONObject("data");
				String amountPayFi = data.getString("amount");
				String apiKeyPayFi = data.getString("apiKey");
				String orderIdPayFi = data.getString("orderId");
				String upiIntentUrlPayFi = data.getString("upiIntentUrl");
				String transactionIdPayFi = data.getString("transactionId");
				String responseHashPayFi = data.getString("hash");

				logger.info("Message: {}", msg);
				logger.info("Code: {}", code);
				logger.info("Amount: {}", amountPayFi);
				logger.info("API Key: {}", apiKeyPayFi);
				logger.info("Order ID: {}", orderIdPayFi);
				logger.info("UPI Intent URL: {}", upiIntentUrlPayFi);
				logger.info("Transaction ID: {}", transactionIdPayFi);
				logger.info("Hash: {}", responseHashPayFi);

				// Validate the hash
				Map<String, String> responseParams = new TreeMap<>();
				responseParams.put("apiKey", apiKeyPayFi);
				responseParams.put("orderId", orderIdPayFi);
				responseParams.put("transactionId", transactionIdPayFi);
				responseParams.put("amount", amountPayFi);
				responseParams.put("upiIntentUrl", upiIntentUrlPayFi);

				logger.info("responseHashPayFi: " + responseHashPayFi);

				String calculatedHash = getHash(responseParams, SALT);
				logger.info("calculatedHash: " + calculatedHash);

				if (calculatedHash.equals(responseHashPayFi)) {
					logger.info("Response hash is valid.");

					IntentUrl = upiIntentUrlPayFi;
					updateTxn(id, transactionIdPayFi);
				} else {
					logger.info("Response hash is invalid. Possible tampering detected.");
				}

			}

			logger.info("IntentUrl:::::::::::::::::::::" + IntentUrl);

			try {

				String FinalUrl = IntentUrl;

				logger.info("FinalUrl::::intent:::::::::::::::::" + FinalUrl);

				logger.info("VPA:::::::::::::::::::::" + VPA);

				if (VPA.equalsIgnoreCase("I")) {
					responseData = FinalUrl;

				} else {
					logger.info("Called from UI Not S2SCall:::::::::::::::::::: ");
					logger.info("EncryptedResponseForIntent::::::::::" + FinalUrl);

					responseData = FinalUrl;
				}

			} catch (Exception e1) {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseData;

	}

	public static String getHash(Map<String, String> map, String salt) {
		Object[] keys = map.keySet().toArray();
		Arrays.sort(keys);
		StringBuilder hashData = new StringBuilder();
		hashData.append(salt);
		for (Object key : keys) {
			Object value = map.get(key.toString());
			if (value != null) {
				hashData.append("|").append(value);
			}
		}
		try {
			return sha512(hashData.toString()).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	private static String sha512(String text) throws NoSuchAlgorithmException {
		return encrypt(text, "SHA-512");
	}

	private static String encrypt(String text, String algorithm) throws NoSuchAlgorithmException {
		if (text == null || text.length() == 0) {
			return null;
		}
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
		messageDigest.update(text.getBytes(StandardCharsets.UTF_8));
		byte[] byteBuffer = messageDigest.digest();
		StringBuilder sb = new StringBuilder();
		for (byte b : byteBuffer) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	public static void updateTxn(String txnId, String sptxnid) {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DBConnectionHandler.getConnection();
			logger.info("updateTxn() ::: id=" + txnId);
			logger.info("Service Provide txn id  ::: id=" + sptxnid);

			if (conn != null) {
				final String sql = "UPDATE tbl_transactionmaster SET udf3 = ? WHERE id = ?";
				logger.info("Query ::: " + sql);
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, sptxnid);
				stmt.setString(2, txnId);

				int rowsAffected = stmt.executeUpdate();
				if (rowsAffected > 0) {

					logger.info("Record updated successfully. Rows affected: " + rowsAffected);
				} else {
					logger.info("No record found with the given ID.");
				}
			} else {
				logger.info("Connection object is null.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}