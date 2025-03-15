package com.rew.payment.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/*import org.w3c.dom.NodeList;
*/import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.rew.pg.db.DataManager;
import com.rew.pg.dto.MerchantDTO;
import com.rew.pg.dto.RefundRequestAPI;
import com.rew.pg.dto.TransactionMaster;

public class PGUtils {
	private static Logger logger = LoggerFactory.getLogger(PGUtils.class);

	//private static final String PROP_FILE_NAME ="D:\\home\\ConfigFile\\vpay\\pay.properties";
	//private static final String PROP_FILE_NAME ="C:\\Users\\acer\\Desktop\\dupv2c\\payment\\payment-master\\WebContent\\WEB-INF\\classes\\config\\pay.properties";
	// File path relative to the classpath
    private static final String PROP_FILE_NAME = "config/pay.properties";

 //   private static final String PROP_FILE_NAME = "/home/ConfigFile/pay.properties";
	private static final Pattern DATE_FORMAT_PATTERN = Pattern.compile(PGUtils.getPropertyValue("DATE_FORMAT_PATTERN"));
	private static final Pattern EMAIL_PATTERN = Pattern.compile(PGUtils.getPropertyValue("EMAIL_PATTERN"));
	private static final String NUMERIC_STRING = PGUtils.getPropertyValue("NUMERIC_STRING");
	public static final String secureKey = PGUtils.getPropertyValue("secureKey");
	private static final String IV = PGUtils.getPropertyValue("IV");
	private static final String KEY = PGUtils.getPropertyValue("KEY");

	public PGUtils() {

	}

	public static String cryptoJs(String hexEncrypted) {
		try {
			logger.info("GUtils.java ::: hexEncrypted length=" + hexEncrypted.length());
			if (hexEncrypted.length() > 0) {
				// SecretKeySpec sks = new
				// SecretKeySpec(Hex.decodeHex(PGUtils.getPropertyValue("KEY").toCharArray()),
				// "AES");
				SecretKeySpec sks = new SecretKeySpec(Hex.decodeHex(KEY.toCharArray()), "AES");
				// IvParameterSpec iv = new
				// IvParameterSpec(Hex.decodeHex(PGUtils.getPropertyValue("IV").toCharArray()));
				IvParameterSpec iv = new IvParameterSpec(Hex.decodeHex(IV.toCharArray()));

				Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
				c.init(2, sks, iv);
				byte[] plain = c.doFinal(Hex.decodeHex(hexEncrypted.toCharArray()));
				String plainText = new String(plain);
				// logger.info("PGUtils.java ::: cryptoJs() :: Data Decrypted Successfully : " +
				// hexEncrypted);
				logger.info("PGUtils.java ::: cryptoJs() :: Data Decrypted Successfully  ");
				return plainText;
			} else {
				logger.info("GUtils.java ::: hexEncrypted is null or blank " + hexEncrypted);
				return null;
			}
		} catch (Exception e) {
			logger.error("PGUtils.java ::: cryptoJs() :: Error while Decrypting Data : ", e);
		}
		return null;
	}

	public static String encryptcryptoJs(String plaintext) {
		try {
			logger.info("GUtils.java ::: hexEncrypted length=" + plaintext.length());
			if (plaintext.length() > 0) {
				SecretKeySpec sks = new SecretKeySpec(Hex.decodeHex(KEY.toCharArray()), "AES");
				IvParameterSpec iv = new IvParameterSpec(Hex.decodeHex(IV.toCharArray()));
				Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
				c.init(1, sks, iv);
				byte[] encrypted = c.doFinal(plaintext.getBytes());
				char[] enctext = Hex.encodeHex(encrypted);
				String encString = new String(enctext);

				logger.info("PGUtils.java ::: cryptoJs() :: Data Decrypted Successfully : " + encString);
				return encString;
			} else {
				logger.info("GUtils.java ::: plaintext is null or blank " + plaintext);
				return null;
			}
		} catch (Exception e) {
			logger.error("PGUtils.java ::: encryptcryptoJs() :: Error while Decrypting Data : ", e);
		}
		return null;
	}

	public static String randomNumeric(int count) throws NoSuchAlgorithmException, NoSuchProviderException {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
			;
			int no = secureRandom.nextInt(9);
			builder.append(no);
		}
		return builder.toString();
	}

	public static String formatAmount(String amount) {
		amount = amount.replace(".", "");
		int rem = 12 - amount.length();
		for (int i = 0; i < rem; i++) {
			amount = "0" + amount;
		}

		return amount;
	}

	public static String generateChecksum(String checksumParams, String checksumKey) {
		logger.info("PGUtils.java ::: generateChecksum() :: Checksum Params : " + checksumParams
				+ " And Checksum Key : " + checksumKey);

		String l_checkSumValue = null;

		try {
			String p_strValues = checksumParams + checksumKey;
			CRC32 l_crc = new CRC32();
			l_crc.update(p_strValues.getBytes());
			l_checkSumValue = String.valueOf(l_crc.getValue());
		} catch (Exception e) {
			logger.error("PGUtils.java ::: generateChecksum() :: Error Occurred in Checksum Generation : ", e);
		}

		return l_checkSumValue;
	}

	public static StringBuffer POSTURI(String URL, String data, String merchantid) {
		String line = null;
		StringBuffer response = new StringBuffer();
		try {
			URL url = new URL(URL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.addRequestProperty("Merchant-ID", merchantid);
			con.addRequestProperty("Content-Type", "application/json;");
			con.addRequestProperty("Content-Length", data.getBytes().length + "");
			con.setDoOutput(true);
			con.connect();
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			logger.info("Data length :: " + data.getBytes().length);
			wr.write(data);
			wr.flush();

			if (con.getResponseCode() == 200) {
				logger.info("HTTP OK....");
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				while ((line = br.readLine()) != null) {
					logger.info(line);
					response.append(line);
				}
				br.close();
				con.setDoOutput(true);
			}

			con.disconnect();
			response.toString();
		} catch (Exception e) {
			logger.error("PGUtils.java ::: ", e);
		}
		return response;
	}

	public static String maskCardNumber(String cardNo) {
		int total = cardNo.length();
		int startlen = 6;
		int endlen = 4;
		int masklen = total - (startlen + endlen);
		StringBuffer maskedbuf = new StringBuffer(cardNo.substring(0, startlen));
		for (int i = 0; i < masklen; i++) {
			maskedbuf.append('X');
		}
		maskedbuf.append(cardNo.substring(startlen + masklen, total));
		String masked = maskedbuf.toString();
		// logger.info("PGUtils.java ::: MaskedCardNumber : " + masked + " of :" +
		// masked.length() + " size");
		return masked;
	}
/*
	public static String getPropertyValue(String variableName) {
		FileInputStream inputStream = null;
		String variableValue = null;

		try {
			File file = new File(PROP_FILE_NAME);

			if ((file != null) && (file.exists())) {
				inputStream = new FileInputStream(file);

				if (inputStream != null) {
					Properties prop = new Properties();
					prop.load(inputStream);
					variableValue = prop.getProperty(variableName);
					inputStream.close();
				}
			} else {
				logger.info(
						"PGUtils.java ::: getPropertyValue() :: Property file '/home/ConfigFile/pay.properties' not found in the Classpath");
			}
		} catch (Exception e) {
			logger.error("PGUtils.java ::: getPropertyValue() :: Exception : ", e);
		}

		return variableValue;
	}
	*/
	public static String getPropertyValue(String variableName) {
	    String variableValue = null;

	    try (InputStream inputStream = PGUtils.class.getClassLoader().getResourceAsStream(PROP_FILE_NAME)) {
	        if (inputStream != null) {
	            Properties prop = new Properties();
	            prop.load(inputStream);
	            variableValue = prop.getProperty(variableName);
	        } else {
	            logger.info("Property file '{}' not found in the classpath", PROP_FILE_NAME);
	        }
	    } catch (Exception e) {
	        logger.error("Exception while loading property: ", e);
	    }

	    return variableValue;
	}
	

	public static String redirectToErrorPage(String errorCode, String errorMsg, String errorPage,
			String errorVariable) {
		logger.info("PGUtils.java ::: ErrorCode --> " + errorCode + " , ErrorMsg --> " + errorMsg + " , ErrorPage --> "
				+ errorPage + " , ErrorVariable --> " + errorVariable);

		StringBuilder sbXML = new StringBuilder();
		sbXML.append("<error>");
		sbXML.append("<error_code>" + errorCode + "</error_code>");
		sbXML.append("<error_msg>" + errorMsg + "</error_msg>");
		sbXML.append("</error>");

		StringBuilder errorResp = new StringBuilder();

		errorResp.append("<HTML>");
		errorResp.append("<BODY>");
		errorResp.append("<FORM ACTION=\"" + errorPage + "\"  METHOD='POST'>");
		errorResp.append("<input type=\"hidden\" name=\"" + errorVariable + "\" value=\"" + sbXML.toString() + "\">");
		errorResp.append("</FORM>");
		errorResp.append("</BODY>");

		errorResp.append("<SCRIPT>");
		errorResp.append("document.forms[0].submit();");
		errorResp.append("</SCRIPT>");
		errorResp.append("</HTML>");

		return errorResp.toString();
	}

	public Properties getProperties(String retValue) throws IOException {
		InputStream inputStream = null;

		Properties prop = new Properties();
		try {
			String propFileName = "config.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new java.io.FileNotFoundException(
						"property file '" + propFileName + "' not found in the classpath");
			}
		} catch (Exception e) {
			logger.error("Exception: ", e);
			inputStream.close();
			return prop;
		} finally {
			inputStream.close();
		}
		inputStream.close();

		return prop;
	}

	public static String generateResponseForIntent(TransactionMaster TM) {
		String encData = null;

		MerchantDTO merchantDTO = new DataManager().getMerchant(TM.getMerchantId());

		try {
			if (merchantDTO != null) {
				String verRespDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
				String txnDateTime = TM.getDateTime();
				if (txnDateTime.length() > 19)
					txnDateTime = txnDateTime.substring(0, 19);
				JSONObject respJSON = new JSONObject();

				respJSON.put("CustRefNum", TM.getTxnId());
				respJSON.put("AuthID", TM.getMerchantId());
				respJSON.put("AggRefNo", TM.getId());
				respJSON.put("Paymentdate", txnDateTime);
				respJSON.put("payStatus", TM.getTransStatus());
				respJSON.put("PayAmount", TM.getAmount());
				respJSON.put("resp_code", TM.getErrorCode());
				respJSON.put("resp_message", TM.getRespMessage());
				respJSON.put("serviceRRN", TM.getServiceRRN());
				respJSON.put("userEmailID", TM.getCustMail());
				respJSON.put("ContactNo", TM.getCustMobile());
				respJSON.put("MOP", TM.getInstrumentId());
				respJSON.put("payrespDate", verRespDateTime);
				respJSON.put("adf1", TM.getUdf1());
				respJSON.put("adf2", TM.getUdf2());
				respJSON.put("adf3", TM.getUdf3());
				respJSON.put("qrString", TM.getQrString());
				/*
				 * if (TM.getReseller_id().equalsIgnoreCase("") ||
				 * TM.getReseller_id().equalsIgnoreCase("NA")) {
				 * 
				 * } else { respJSON.put("reseller_id", TM.getReseller_id());
				 * respJSON.put("reseller_txn_id", TM.getReseller_txn_id()); }
				 */
				logger.info("UDF 6::::::::Response:::::::::::::::::::::::::::::;Value " + TM.getUdf6());
				logger.info(
						"PGUtils.java ::: generateResponse() :: JSON Response Generated :- \n" + respJSON.toString());
				//String confMerchantId =null;
				String confMerchantId = PGUtils.getPropertyValue("pspclId");
				logger.info("TM.getUploadedBy()=====>" + TM.getUploadedBy());
				if (confMerchantId.equals(TM.getMerchantId())) {
					logger.info("PGUtils.java ::: Merchant is pspclId ");
					encData = PSPCLEncryptor.encrypt(merchantDTO.getTransactionKey(), merchantDTO.getTransactionKey(),
							respJSON.toString());
					// encData = PSPCLEncryptor.encrypt("M0000283", "GO07X8MYK4H91Z2F",
					// respJSON.toString());
				} else if (TM.getUploadedBy().equalsIgnoreCase("V2")) {
					logger.info("PGUtils.java ::: Merchant is having 32 bit key " + TM.getMerchantId());
					encData = PSPCLEncryptor.encrypt(merchantDTO.getTransactionKey(),
							merchantDTO.getTransactionKey().substring(0, 16), respJSON.toString());
				}

				else {
					logger.info("PGUtils.java ::: Merchant is not pspclId ");
					encData = getEncData(respJSON.toString(), merchantDTO.getTransactionKey());
				}
			}
		} catch (Exception ex) {
			logger.info("faced issue while making the response which was supposed to shared {}", ex);
		}

		logger.info("PGUtils.java ::: generateResponse() :: Enc Response :- \n" + encData);

		// Push URL
		try {
			if (merchantDTO.getIs_push_url().equals("1")) {
				int result = 0;
				result = new DataManager().insertPushDataTemp(TM.getId(), TM.getTxnId(), TM.getMerchantId(),
						merchantDTO.getPushUrl(), encData, "", "insert");
				logger.info("result >>>>>>>>>>>>>>>>>>>>>> " + result);
				if (result == 1) {
					logger.info("data insertion successsfully");
				} else {
					logger.info("data insertion Failed in push schedular table");
				}

				/*
				 * if(result==1) { String
				 * respData=pushResponseToMerchant(merchantDTO.getPushUrl(),encData,merchantDTO.
				 * getMerchantId()); String stts="FAILED",pgRefId="TM.getId()";
				 * if((!stts.equalsIgnoreCase(null)|| !stts.equalsIgnoreCase("")) &&
				 * (!pgRefId.equalsIgnoreCase(null)|| !pgRefId.equalsIgnoreCase(""))) {
				 * 
				 * if(stts.equalsIgnoreCase("SUCCESS")) { logger.info("SUCCESS"); result=new
				 * DataManager().insertPushDataTemp(TM.getId(),TM.getTxnId(),TM.getMerchantId(),
				 * merchantDTO.getPushUrl(),stts,"delete"); if(result==2){
				 * logger.info("respdata deleted successfully  "+result); } else{
				 * logger.info("respdata deleted failed  "+result); } } else {
				 * logger.info("update"); result=new
				 * DataManager().insertPushDataTemp(TM.getId(),TM.getTxnId(),TM.getMerchantId(),
				 * merchantDTO.getPushUrl(),stts,"update"); if(result==3){
				 * logger.info("respdata updated successfully"+result); } else{
				 * logger.info("respdata updated failed"+result);
				 * 
				 * } } } } else{ logger.info("data insertion Failed in push schedular table"); }
				 */
			}
		} catch (Exception ex) {
			logger.info("catch block :::::::::: {}", ex);
		}

		logger.info("PGUtils.java ::: before return response :- ");

		return encData;
	}

	public static String generateResponse(TransactionMaster TM) {
		String encData = null;

		MerchantDTO merchantDTO = new DataManager().getMerchant(TM.getMerchantId());

		try {
			if (merchantDTO != null) {
				String verRespDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
				String txnDateTime = TM.getDateTime();
				if (txnDateTime.length() > 19)
					txnDateTime = txnDateTime.substring(0, 19);
				JSONObject respJSON = new JSONObject();

				/*
				 * respJSON.put("txn_id", TM.getTxnId()); respJSON.put("merchant_id",
				 * TM.getMerchantId()); respJSON.put("pg_ref_id", TM.getId());
				 * respJSON.put("txn_date_time", txnDateTime); respJSON.put("trans_status",
				 * TM.getTransStatus()); respJSON.put("txn_amount", TM.getAmount());
				 * respJSON.put("resp_code", TM.getErrorCode()); respJSON.put("resp_message",
				 * TM.getRespMessage()); respJSON.put("bank_ref_id", TM.getServiceRRN());
				 * respJSON.put("cust_email_id", TM.getCustMail());
				 * respJSON.put("cust_mobile_no", TM.getCustMobile());
				 * respJSON.put("payment_mode", TM.getInstrumentId());
				 * respJSON.put("resp_date_time", verRespDateTime); respJSON.put("udf1",
				 * TM.getUdf1()); respJSON.put("udf2", TM.getUdf2()); respJSON.put("udf3",
				 * TM.getUdf3()); respJSON.put("udf4", TM.getUdf4()); respJSON.put("udf5",
				 * TM.getUdf5()); respJSON.put("udf6", TM.getUdf6());
				 */
				respJSON.put("CustRefNum", TM.getTxnId());
				respJSON.put("AuthID", TM.getMerchantId());
				respJSON.put("AggRefNo", TM.getId());
				respJSON.put("PaymentDate", txnDateTime);
				respJSON.put("payStatus", TM.getTransStatus());
				respJSON.put("PayAmount", TM.getAmount());
				respJSON.put("resp_code", TM.getErrorCode());
				respJSON.put("resp_message", TM.getRespMessage());
				respJSON.put("serviceRRN", TM.getServiceRRN());
				respJSON.put("EmailId", TM.getCustMail());
				respJSON.put("ContactNo", TM.getCustMobile());
				respJSON.put("MOP", TM.getInstrumentId());
				respJSON.put("payrespDate", verRespDateTime);
				respJSON.put("adf1", TM.getUdf1());
				respJSON.put("adf2", TM.getUdf2());
				respJSON.put("adf3", TM.getUdf3());
				/*
				 * if (TM.getReseller_id().equalsIgnoreCase("") ||
				 * TM.getReseller_id().equalsIgnoreCase("NA")) {
				 * 
				 * } else { respJSON.put("reseller_id", TM.getReseller_id());
				 * respJSON.put("reseller_txn_id", TM.getReseller_txn_id()); }
				 */
				logger.info("UDF 6::::::::Response:::::::::::::::::::::::::::::;Value " + TM.getUdf6());
				logger.info(
						"PGUtils.java ::: generateResponse() :: JSON Response Generated :- \n" + respJSON.toString());
				String confMerchantId="null";
				//String confMerchantId = PGUtils.getPropertyValue("pspclId");
				logger.info("TM.getUploadedBy()=====>" + TM.getUploadedBy());
				if (confMerchantId.equals(TM.getMerchantId())) {
					logger.info("PGUtils.java ::: Merchant is pspclId ");
					encData = PSPCLEncryptor.encrypt(merchantDTO.getTransactionKey(), merchantDTO.getTransactionKey(),
							respJSON.toString());
					// encData = PSPCLEncryptor.encrypt("M0000283", "GO07X8MYK4H91Z2F",
					// respJSON.toString());
				} else if (TM.getUploadedBy().equalsIgnoreCase("V2")) {
					logger.info("PGUtils.java ::: Merchant is having 32 bit key " + TM.getMerchantId());
					encData = PSPCLEncryptor.encrypt(merchantDTO.getTransactionKey(),
							merchantDTO.getTransactionKey().substring(0, 16), respJSON.toString());
				}

				else {
					logger.info("PGUtils.java ::: Merchant is not pspclId ");
					encData = getEncData(respJSON.toString(), merchantDTO.getTransactionKey());
				}
			}
		} catch (Exception ex) {
			logger.info("faced issue while making the response which was supposed to shared {}", ex);
		}

		logger.info("PGUtils.java ::: generateResponse() :: Enc Response :- \n" + encData);

		// Push URL
		try {
			if (merchantDTO.getIs_push_url().equals("1")) {
				int result = 0;
				result = new DataManager().insertPushDataTemp(TM.getId(), TM.getTxnId(), TM.getMerchantId(),
						merchantDTO.getPushUrl(), encData, "", "insert");
				logger.info("result >>>>>>>>>>>>>>>>>>>>>> " + result);
				if (result == 1) {
					logger.info("data insertion successsfully");
				} else {
					logger.info("data insertion Failed in push schedular table");
				}

				/*
				 * if(result==1) { String
				 * respData=pushResponseToMerchant(merchantDTO.getPushUrl(),encData,merchantDTO.
				 * getMerchantId()); String stts="FAILED",pgRefId="TM.getId()";
				 * if((!stts.equalsIgnoreCase(null)|| !stts.equalsIgnoreCase("")) &&
				 * (!pgRefId.equalsIgnoreCase(null)|| !pgRefId.equalsIgnoreCase(""))) {
				 * 
				 * if(stts.equalsIgnoreCase("SUCCESS")) { logger.info("SUCCESS"); result=new
				 * DataManager().insertPushDataTemp(TM.getId(),TM.getTxnId(),TM.getMerchantId(),
				 * merchantDTO.getPushUrl(),stts,"delete"); if(result==2){
				 * logger.info("respdata deleted successfully  "+result); } else{
				 * logger.info("respdata deleted failed  "+result); } } else {
				 * logger.info("update"); result=new
				 * DataManager().insertPushDataTemp(TM.getId(),TM.getTxnId(),TM.getMerchantId(),
				 * merchantDTO.getPushUrl(),stts,"update"); if(result==3){
				 * logger.info("respdata updated successfully"+result); } else{
				 * logger.info("respdata updated failed"+result);
				 * 
				 * } } } } else{ logger.info("data insertion Failed in push schedular table"); }
				 */
			}
		} catch (Exception ex) {
			logger.info("catch block :::::::::: {}", ex);
		}

		logger.info("PGUtils.java ::: before return response :- ");

		return encData;
	}

	public static String GetTextBetweenTags(String InputText, String Tag1, String Tag2) {
		String Result = "NA";

		int index1 = InputText.indexOf(Tag1);

		int index2 = InputText.indexOf(Tag2);

		logger.info("PGUtils.java ::: GetTextBetweenTags() :: Index of '" + Tag1 + "' : " + index1 + " And Index of '"
				+ Tag2 + "' : " + index2);

		if ((index1 != -1) && (index2 != -1)) {
			index1 += Tag1.length();
			Result = InputText.substring(index1, index2);
		}

		return Result;
	}

	public static boolean isValidForamt(String patternName, String value) {
		Matcher matcher = null;

		if ((patternName != null) && (!patternName.equals("")) && (value != null) && (!value.equals(""))) {
			if (patternName.equalsIgnoreCase("EMAIL")) {
				matcher = EMAIL_PATTERN.matcher(value);
			} else if (patternName.equalsIgnoreCase("DATE")) {
				matcher = DATE_FORMAT_PATTERN.matcher(value);
			}
			if ((matcher != null) && (matcher.matches())) {

				return true;
			}

			logger.info("PGUtils.java ::: isValidForamt() :: " + patternName + " Pattern matching Failed for Value '"
					+ value + "'.");
			return true;
		}

		logger.info("PGUtils.java ::: isValidForamt() :: Pattern Name or Value is Missing/Null/Blank.");
		return true;
	}

	public static String generateVerificationResponse(TransactionMaster TM) {
		String verRespDateTime = TM.getRespDateTime();
		if (verRespDateTime.length() > 19)
			verRespDateTime = verRespDateTime.substring(0, 19);
		String txnDateTime = TM.getDateTime();
		if (txnDateTime.length() > 19) {
			txnDateTime = txnDateTime.substring(0, 19);
		}
		JSONObject respJSON = new JSONObject();


		respJSON.put("CustRefNum", TM.getTxnId());
		respJSON.put("AuthID", TM.getMerchantId());
		respJSON.put("AggRefNo", TM.getId());
		respJSON.put("PaymentDate", txnDateTime);
		respJSON.put("payStatus", TM.getTransStatus());
		respJSON.put("PayAmount", TM.getAmount());
		respJSON.put("resp_code", TM.getErrorCode());
		respJSON.put("resp_message", TM.getRespMessage());
		respJSON.put("serviceRRN", TM.getServiceRRN());
		respJSON.put("EmailId", TM.getCustMail());
		respJSON.put("ContactNo", TM.getCustMobile());
		respJSON.put("MOP", TM.getInstrumentId());
		respJSON.put("payrespDate", verRespDateTime);
		respJSON.put("adf1", TM.getUdf1());
		respJSON.put("adf2", TM.getUdf2());
		respJSON.put("adf3", TM.getUdf3());
		/*
		 * if (TM.getReseller_id().equalsIgnoreCase("") ||
		 * TM.getReseller_id().equalsIgnoreCase("NA")) {
		 * 
		 * } else { respJSON.put("reseller_id", TM.getReseller_id());
		 * respJSON.put("reseller_txn_id", TM.getReseller_txn_id()); }
		 */
		return respJSON.toString();
	}

	public static String generateRefundRequestResponse(RefundRequestAPI RRA) {
		JSONObject respJSON = new JSONObject();

		respJSON.put("AuthID", RRA.getMerchantId());
		respJSON.put("CustRefNum", RRA.getTxnId());
		respJSON.put("AggRefNo", RRA.getId());
		respJSON.put("refund_amount", RRA.getRefund_amount());
		respJSON.put("txn_amount", RRA.getAmount());
		respJSON.put("refund_type_status", RRA.getRefund_type());
		respJSON.put("resp_code", RRA.getErrorCode());
		respJSON.put("resp_message", RRA.getRespMessage());
		respJSON.put("rrn", RRA.getServiceRRN());

		logger.info("PGUtils.java ::: generateRefundRequestResponse() :: JSON Response Generated : " + respJSON);

		return respJSON.toString();
	}

	public static String getEncData(String sData, String encKey) {
		logger.info("inside getEncData method " + encKey.length());
		try {

			if (encKey.length() == 8) {
				encKey = encKey + encKey;
			}
			String ALGO = "AES";
			byte[] keyByte = encKey.getBytes();
			Key key = new SecretKeySpec(keyByte, ALGO);

			Cipher c = Cipher.getInstance(ALGO);
			c.init(1, key);
			byte[] encVal = c.doFinal(sData.getBytes());
			byte[] encryptedByteValue = Base64.getEncoder().encode(encVal);
			return new String(encryptedByteValue);

		} catch (Exception e) {
			logger.error("PGUtils.java ::: getDecData() :: Error occurred while Encryption : ", e);
		}

		return null;
	}

	public static String getDecData(String sData, String decKey) {
		try {
			if (decKey.length() == 8) {
				decKey = decKey + decKey;
			}
			String ALGO = "AES";
			byte[] keyByte = decKey.getBytes();
			Key key = new SecretKeySpec(keyByte, ALGO);
			Cipher c = Cipher.getInstance(ALGO);
			c.init(2, key);
			logger.debug("PGUtils.java ::: getDecData() ::  " + sData);
			byte[] decryptedByteValue = Base64.getDecoder().decode(sData.getBytes());
			logger.debug("PGUtils.java ::: getDecData() ::  " + decryptedByteValue);
			byte[] decValue = c.doFinal(decryptedByteValue);
			return new String(decValue);

		} catch (Exception e) {

			logger.error("PGUtils.java ::: getDecData() :: Error occurred while Decryption : ", e);
		}
		return null;
	}

	public static String null2Known(String[] reqFields, JSONObject jsonObject) {
		String errorResponse = "NOTNULL";
		try {
			for (int i = 0; i < reqFields.length; i++) {
				if ((!jsonObject.has(reqFields[i])) || (jsonObject.getString(reqFields[i]) == null)
						|| (jsonObject.getString(reqFields[i]).equals(""))
						|| (jsonObject.getString(reqFields[i]).trim().length() == 0)
						|| (jsonObject.getString(reqFields[i]).trim().equalsIgnoreCase("NULL"))) {
					errorResponse = "Error 10001 : One or More Request Field is Missing/Null/Blank [" + reqFields[i]
							+ "].";
					break;
				}
			}
		} catch (Exception e) {
			errorResponse = e.getMessage();
			logger.error("PGUtils.java ::: null2Known() :: Error Occurred during Validation : ", e);
		}
		return errorResponse;
	}

	public static String generateLog(String merchantId, String MerchanttxnId, String PGTxnId, String Amount) {
		String strTrackingLog = "  Merchant Id :: " + merchantId;
		strTrackingLog = strTrackingLog + "  MerchanttxnId :: " + MerchanttxnId;
		strTrackingLog = strTrackingLog + "  PGTxnId :: " + PGTxnId;
		strTrackingLog = strTrackingLog + "  Amount :: " + Amount;
		return strTrackingLog;
	}

	public static String getMaskedCardNo(int i) {
		String s = "";
		for (int j = 0; j < i; j++) {
			s = s + "X";
		}
		return s;
	}

	public static String RandomCaptcha(int length) {
		String CHAR_LOWER = PGUtils.getPropertyValue("CHAR_LOWER");
		String DATA_FOR_RANDOM_STRING = CHAR_LOWER;// + CHAR_UPPER + NUMBER;
		SecureRandom random = new SecureRandom();
		if (length < 1)
			throw new IllegalArgumentException();

		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
			char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
			sb.append(rndChar);
		}
		return sb.toString();
	}

	public String postDataRetry(TransactionMaster TM, String encRespXML, HttpServletRequest request, DataManager dm,
			final String type) {
		logger.info("before Posting data >>>> ");
		MerchantDTO merchantDTO = dm.getMerchant(TM.getMerchantId());
		String respRetry = "NA";
		try {
			if (merchantDTO.getIsRetry().equalsIgnoreCase("1") && TM.getTransStatus().equalsIgnoreCase("F")) {

				if (type.equalsIgnoreCase("RESPHANDLER")) {
					logger.info("RESPHANDLER.......................");
					StringBuilder resp = new StringBuilder();
					resp.append("<HTML>");
					resp.append("<BODY>");
					resp.append("<FORM ACTION=\"" + request.getContextPath() + "/Retry.jsp" + "\"  METHOD='POST'>");
					resp.append("<input type=\"hidden\" name=\"txnRefNo\" value=\"" + TM.getId() + "\">");
					resp.append("<input type=\"hidden\" name=\"respData\" value=\"" + encRespXML + "\">");
					resp.append("</FORM>");
					resp.append("</BODY>");

					resp.append("<SCRIPT>");
					resp.append("document.forms[0].submit();"); // internal submit
					resp.append("</SCRIPT>");
					resp.append("</HTML>");
					respRetry = resp.toString();
					return respRetry;
				} else if (type.equalsIgnoreCase("AJAXACTION")) {
					logger.info("AJAXACTION.......................");
					JSONObject job = new JSONObject();
					job.put("url", request.getContextPath() + "/Retry.jsp");
					job.put("encRespXML", encRespXML);
					job.put("merchantId", TM.getId());

					respRetry = job.toString();
					// logger.info(respRetry);
					return respRetry;
				}

			}

		} catch (Exception e) {
			logger.info("getting error in retry posting " + e.getMessage());
			return respRetry = "NA";
		}
		return respRetry;
	}

	public String getAttributes(String data, String tagName, String attributeName) {
		String result = null;
		DocumentBuilder documentBuilder;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
			dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			dbf.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
			dbf.setFeature("http://xml.org/sax/features/validation", true);
			dbf.setValidating(true);
			dbf.setXIncludeAware(false);
			dbf.setExpandEntityReferences(false);
			documentBuilder = dbf.newDocumentBuilder();
			InputSource inputSource = new InputSource();
			inputSource.setCharacterStream(new StringReader(data));
			Document doc = documentBuilder.parse(inputSource);
			NodeList dataTag = doc.getElementsByTagName(tagName);
			for (int temp = 0; temp < dataTag.getLength(); temp++) {
				Node nNode = dataTag.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					result = eElement.getAttribute(attributeName);
					System.out.println(result);
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			logger.info("error message" + e.getMessage());
		}
		return result;
	}

	public static Pattern[] patterns = { Pattern.compile("[^-a-zA-Z0-9 /?&{}+=_:,|.'@~]*[`!%;<>#$%^*>()\">]*"),
			Pattern.compile("<script>(.*?)</script>", 2), Pattern.compile("src[\r\n]*=[\r\n]*\\'(.*?)\\'", 42),
			Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 42), Pattern.compile("</script>", 2),
			Pattern.compile("<script(.*?)>", 42), Pattern.compile("eval\\((.*?)\\)", 42),
			Pattern.compile("expression\\((.*?)\\)", 42), Pattern.compile("javascript:", 2),
			Pattern.compile("vbscript:", 2), Pattern.compile("onload(.*?)=", 42), Pattern.compile("onerror(.*?)=", 42),
			Pattern.compile("window.", 2), Pattern.compile("document.", 2), Pattern.compile("location.", 2),
			Pattern.compile("alert\\((.*?)\\)", 42) };

	public String stripXSS(String value) {
		String checkedValue = null;
		if (value != null) {
			for (Pattern scriptPattern : patterns) {
				checkedValue = scriptPattern.matcher(value).replaceAll("");
				if (!value.equals(checkedValue)) {
					logger.info("XSSFilter.java ::: Break Script Pattern :: " + scriptPattern);
					break;
				}
			}
		}
		return checkedValue;
	}

	public static DateTimeFormatter getOrdinalDateFormatter() {
		HashMap<Long, String> ordinalNumbers = new HashMap<>(42);
		ordinalNumbers.put(1L, "1st");
		ordinalNumbers.put(2L, "2nd");
		ordinalNumbers.put(3L, "3rd");
		ordinalNumbers.put(21L, "21st");
		ordinalNumbers.put(22L, "22nd");
		ordinalNumbers.put(23L, "23rd");
		ordinalNumbers.put(31L, "31st");
		for (long d = 1; d <= 31; d++) {
			ordinalNumbers.putIfAbsent(d, "" + d + "th");
		}
		DateTimeFormatter dayOfMonthFormatter = new DateTimeFormatterBuilder()
				.appendText(ChronoField.DAY_OF_MONTH, ordinalNumbers).appendPattern(" MMMM, yyyy").toFormatter();
		return dayOfMonthFormatter;
	}

	public static boolean isEmptyNull(String value) {
		if (null == value)
			return true;
		else if (value.trim().length() == 0)
			return true;
		else if (value.trim().equalsIgnoreCase("NULL"))
			return true;

		return false;
	}

	public static void postUrlToPG(String PostingUrl, Map<String, String> Fields, PrintWriter out) {

		StringBuilder resp = new StringBuilder();
		resp.append("<HTML>");
		resp.append("<BODY>");
		resp.append("<FORM ACTION=\"" + PostingUrl + "\"  METHOD='POST'>");

		for (Map.Entry<?, ?> entry : Fields.entrySet()) {
			resp.append("<input type=\"hidden\" name=\"" + entry.getKey().toString() + "\" value=\""
					+ entry.getValue().toString() + "\">");
		}
		resp.append("</FORM>");
		resp.append("</BODY>");
		resp.append("<SCRIPT>");
		resp.append("document.forms[0].submit();");
		resp.append("</SCRIPT>");
		resp.append("</HTML>");
		out.println(resp.toString());
		out.flush();
		out.close();
		return;
	}

	/*
	 * public static void PostDataToPg(String Url, String Data, PrintWriter out) {
	 * StringBuilder resp = new StringBuilder(); String Ur = String.valueOf(Url) +
	 * Data; resp.append("<HTML>"); resp.append("<BODY>");
	 * resp.append("<FORM ACTION=\"" + Ur + "\"  METHOD='POST'>");
	 * resp.append("</FORM>"); resp.append("</BODY>"); resp.append("<SCRIPT>");
	 * resp.append("document.forms[0].submit();"); resp.append("</SCRIPT>");
	 * resp.append("</HTML>"); out.println(resp.toString()); out.flush();
	 * out.close(); }
	 */

	static String urlEncodeUTF8(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	public static String urlEncodeUTF8(Map<?, ?> map) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (sb.length() > 0)
				sb.append("&");
			sb.append(String.format("%s=%s", new Object[] { urlEncodeUTF8(entry.getKey().toString()),
					urlEncodeUTF8(entry.getValue().toString()) }));
		}
		return sb.toString();
	}

	/*
	 * public static void main(String[] args) { int a = merchantPushUrlService();
	 * System.out.println("result " +a); }
	 */
// added on 02-03-23
	public int merchantPushUrlService(TransactionMaster TM) // TransactionMaster TM
	{
		logger.info("In merchantPushUrlService for the ");
		int result = 0;
		String respJSON = null;
		String finalRU = null;
		MerchantDTO merchantDTO = new DataManager().getMerchant(TM.getMerchantId());
		// isEmptyNull
		if (!isEmptyNull(merchantDTO.getPushUrl()) && !merchantDTO.getPushUrl().equalsIgnoreCase("NA")) {
			logger.info(
					"passed condition for push url service and will be and will be sharing data on push url {} :::::::::: for the mentioned txn_id {}",
					merchantDTO.getPushUrl(), TM.getId());

			DataManager dm = new DataManager();
			TransactionMaster tm = dm.getTxnVerification(TM.getTxnId(), TM.getMerchantId());
			String data = null;
			if (tm != null) {
				logger.info("log to check tm condition :::::: {}", tm.toString());
				logger.info(
						"Checking CardType of the transaction for making the resposnse to sharre for webhook service ::: {}",
						TM.getCardType());
				if (TM.getCardType().equalsIgnoreCase("I")) {
					logger.info("The specified transaction is intent for the txnID ::::::: {} ::: cardtype is :: {}",
							TM.getId(), TM.getCardType());
					respJSON = PGUtils.generateResponseForIntent(tm);
				} else {
					logger.info("because the transaction is not intent :::::: {}", TM.getId());
					respJSON = PGUtils.generateResponse(tm);
				}
				// respJSON = PGUtils.generateVerificationResponse(tm);
				data = "AuthID=" + TM.getMerchantId() + "&respData=" + respJSON + "&AggRefNo=" + TM.getId();
				logger.info("data to be shared ::::::: {}", data);

			} else {
				logger.info("faced problem while verifing txn" + TM.getId());
			}

			logger.info("response=====>" + respJSON);

			finalRU = merchantDTO.getPushUrl();

			try {
				data = URLEncoder.encode(respJSON, "UTF-8");
				logger.info("for the encrypted data to be shared :::: {}", data);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("In block of push response to merchant");
			pushResponseToMerchant(finalRU, data);
			// respJSON

			result = 1;
		} else {
			logger.info("The merchant's push url is not available or is not assigned for merchantId {}",
					merchantDTO.getPushUrl());
		}
		return result;
	}

	private static void pushResponseToMerchant(String pushURL, String respJSON) {
		logger.info("pushURL ::::::: {} , respJSON :::::: {} ", pushURL, respJSON);
		URL url = null;
		HttpURLConnection con = null;
		OutputStreamWriter wr = null;
		try {
			url = new URL(pushURL);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setReadTimeout(30000);
			con.setDoOutput(true);
			con.connect();

			wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(respJSON);

			if ((con.getResponseCode() == 200) || (con.getResponseCode() == 202)) {
				logger.info("PGUtils.java ::: pushResponseToMerchant() :: Response sent Successfully on Push URL : "
						+ pushURL);
			} else {
				logger.info("PGUtils.java ::: pushResponseToMerchant() :: Response Code : " + con.getResponseCode());
			}
		} catch (Exception e) {
			logger.info(e.getMessage() + " , pushURL=" + pushURL + " , reponse=" + respJSON);
			logger.error(
					"PGUtils.java ::: pushResponseToMerchant() :: Error Occurred while Posting Response to Push URL : ",
					e);
		} finally {
			if (wr != null) {
				try {
					wr.flush();
					wr.close();
				} catch (IOException e) {
					logger.error("Problem with OutputStreamWriter closing", e);
				}
			}
			if (con != null) {
				con.disconnect();
			}
		}
	}
	
	
	public static String encrypt(String key, String initVector, String value)  
	{
		String tokenn=null;
	    try
	    {
	    
	      IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
	      SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
	      
	      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	      cipher.init(1, skeySpec, iv);
	      
	      byte[] encrypted = cipher.doFinal(value.getBytes());
	      System.out.println("encrypted string: " + 
	        java.util.Base64.getEncoder().encodeToString(encrypted));
	      
	      tokenn=java.util.Base64.getEncoder().encodeToString(encrypted);
	      return java.util.Base64.getEncoder().encodeToString(encrypted);

	    }
	    catch (Exception ex)
	    {
	      ex.printStackTrace();
	    }
	    
	    return tokenn;
	  }
	
	
	
	public static String decrypt(String key, String initVector, String encrypted)
	  {
		byte[] original=null;
	    try
	    {
	   
	      IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
	      SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
	      
	      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	      cipher.init(2, skeySpec, iv);
	      
	      //byte[] original = cipher.doFinal(java.util.Base64.getDecoder().decode(encrypted));
	      
	       original = cipher.doFinal(java.util.Base64.getDecoder().decode(encrypted));
	       
	       logger.info("String(original)::::::::::::::::::::::"+new String(original));
	      return new String(original);

	    }
	    catch (Exception ex)
	    {
	      ex.printStackTrace();
	    }
	    logger.info(":::::::::::::::::::");
	    return new String(original);
	  }
	

}
