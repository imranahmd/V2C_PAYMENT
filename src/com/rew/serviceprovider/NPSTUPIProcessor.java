package com.rew.serviceprovider;

//import java.io.BufferedReader;

import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
//import java.net.URL;
import java.security.Key;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
//import java.sql.Timestamp;
//import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.NoSuchElementException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
//import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

//import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.payment.router.IDynamicPGSelector;
//import com.rew.payment.utility.NpstEncrypt_decrypt;
import com.rew.payment.utility.PGUtils;
//import com.rew.payment.utility.S2SCall;
import com.rew.pg.db.DataManager;
import com.rew.pg.dto.TransactionMaster;

public class NPSTUPIProcessor  implements IDynamicPGSelector
{
	private static Logger logger = LoggerFactory.getLogger(NPSTUPIProcessor.class);
  /*public static void main(String[] args) {}*/
	private static SecretKeySpec  secretKey;
  
  public static String generateChecksumMerchant(String concatenatedString, String checksumkey) throws IOException {
	  String inputString = concatenatedString + checksumkey;
	  StringBuffer sb = null;
	  MessageDigest md;
	  try {
	  md = MessageDigest.getInstance("SHA-256");
	  md.update(inputString.getBytes());
	  byte byteData[] = md.digest();
	  sb = new StringBuffer();
	  for (int i = 0; i < byteData.length; i++) {
	  sb.append(Integer.toString((byteData[i] & 0xff) + 0x100,16).substring(1));
	  }
	  } catch (NoSuchAlgorithmException e) {
	  }
	  return sb.toString();
	  }
  public static void setKey(String myKey) {
	  MessageDigest sha = null;
	  try {
	  byte[] key = myKey.getBytes("UTF-8");
	  sha = MessageDigest.getInstance("SHA-256");
	  key = sha.digest(key);
	  key = Arrays.copyOf(key, 16);
	secretKey = new SecretKeySpec(key, "AES");
	  } catch (NoSuchAlgorithmException e) {
	  } catch (UnsupportedEncodingException e) {
	  }
	  }
  
  public static String decryptResponse(String responseString, String encryptKey) {
	  try {
	  Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
	  cipher.init(Cipher.DECRYPT_MODE, setMerchantKey(encryptKey));
	  return new String(cipher.doFinal(Base64.getDecoder().decode(responseString)), "UTF-8");
	  } catch (Exception e) {
	  e.printStackTrace();
	  }
	  return null;
  }
  public static Key setMerchantKey(String myKey) {
	  MessageDigest sha = null;
	  try {
	  byte[] key = myKey.getBytes("UTF-8");
	  sha = MessageDigest.getInstance("SHA-256");
	  key = sha.digest(key);
	  key = Arrays.copyOf(key, 16);
	secretKey = new SecretKeySpec(key, "AES");
	 return secretKey;
	  } catch (NoSuchAlgorithmException e) {
	  } catch (UnsupportedEncodingException e) {
	  }
	return null;
	  }
  
  public static String encrypt(String strToEncrypt, String secret) {
	  try {
	  if(secretKey==null){
	  setKey(secret);
	  }
	 Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	 cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	 return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	 } catch (Exception e) {
	 }
	 return null;
	  }
//  public static String secureServerCall(String sURL, String data) {
//		String line = null;
//		BufferedReader br = null;
//		StringBuffer respString = null;
//
//		logger.info("S2SCall.java ::: secureServerCall() :: Posting URL : " + sURL);
//		try
//		{
//			String CID= "d9fa9e7f5c1a43a588556d1424d12a87";//PGUtils.getPropertyValue("CID");
//			URL obj = new URL(sURL);
//			HttpsURLConnection con = (HttpsURLConnection)obj.openConnection();
//			con.setSSLSocketFactory(bypassSSL());
//			con.setRequestMethod("POST");
//			con.addRequestProperty("Content-Type", "text/plain");
//			con.addRequestProperty("CID", CID);
//
//			con.addRequestProperty("Content-Length", data.getBytes().length+"");
//			con.setDoOutput(true);
//			con.connect();
//
//			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
//
//			wr.write(data);
//			wr.flush();
//
//			respString = new StringBuffer();
//logger.info("getting response code :::::"+con.getResponseCode());
//			if (con.getResponseCode() == 200)
//			{
//				logger.info("S2SCall.java ::: secureServerCall() :: HTTP OK");
//				br = new BufferedReader(
//						new InputStreamReader(con.getInputStream()));
//
//				while ((line = br.readLine()) != null)
//				{
//					logger.info("S2SCall.java ::: secureServerCall() :: Response : " + line);
//					respString.append(line);
//				}
//
//				br.close();
//
//				return respString.toString().trim();
//			}else{
//				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
//				while((line = br.readLine()) != null) { //io exception
//					respString.append(line);
//				}
//				logger.info("S2SCall.java ::: respString : " + respString);
//
//			}
//		}
//		catch (Exception e)
//		{
//			logger.error("S2SCall.java ::: secureServerCall() :: Error Occurred while Processing Request : ", e);
//		}
//
//
//		return null;
//	}
//  
  
  
  
  private static javax.net.ssl.SSLSocketFactory bypassSSL() throws KeyManagementException, NoSuchAlgorithmException,KeyStoreException,CertificateException,IOException
  {
  final javax.net.ssl.TrustManager[] trustAll = new javax.net.ssl.TrustManager[]{new javax.net.ssl.X509TrustManager(){

  public java.security.cert.X509Certificate[] getAcceptedIssuers() {
  return null;
  }

  public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
  throws java.security.cert.CertificateException {
  }


  public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
  throws java.security.cert.CertificateException {
  }

  }};

         
  final javax.net.ssl.SSLContext sslcontext = javax.net.ssl.SSLContext.getInstance("SSL");
  sslcontext.init(null, trustAll, new java.security.SecureRandom());

  final javax.net.ssl.SSLSocketFactory scoFactory = sslcontext.getSocketFactory();

  javax.net.ssl.HostnameVerifier allverify = new javax.net.ssl.HostnameVerifier() {
  public boolean verify(String arg0, javax.net.ssl.SSLSession arg1) {
  return true;
  }
  };

  javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(allverify);
  logger.info("[postdata[Post[bypassSSL]]]Returning ssl factory with bypass ssl validation");
  return scoFactory;
  }
  
  
  public  static void main(String[] args)
  {
	  
	}
    
  public static String  CreateResponse(JSONObject JSONObject,String id) throws NoSuchElementException, URISyntaxException
  {
	  logger.info("Create REsponse for the Intent Upi "+JSONObject.toString());
		DataManager datamanager = new DataManager();
		String resp_date_Time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		TransactionMaster txnMaster = datamanager.getTxnMaster(id);

	  txnMaster.setTransStatus("Pending");  
		txnMaster.setServiceRRN("NA");    // EnMPIResRef
		txnMaster.setServiceTxnId("NA");  // EnMPIResTranid
		txnMaster.setServiceAuthId("NA");  // EnMPIResPayid
		txnMaster.setRespStatus("0");  
		txnMaster.setInstrumentId("UPI");
		txnMaster.setBankId("UPI");
		txnMaster.setCardDetails("I");
		txnMaster.setRespMessage("SUCCESS"); 
		txnMaster.setErrorCode("PPPP");
		txnMaster.setSpErrorCode("");
		txnMaster.setRespDateTime(resp_date_Time);  
		txnMaster.setModified_By("AllProcessor");
		txnMaster.setModified_On(resp_date_Time);
		 String result = (new java.net.URI(JSONObject.getString("qrString")).getPath());
		txnMaster.setQrString(result);//
		
		logger.info("NPSTUPIProcessor.java :: now upadte the for the intent trnsaction ... ");
		datamanager.updateTxn(txnMaster);
		
		String encRespXML = PGUtils.generateResponseForIntent(txnMaster);
	  JSONObject js = new JSONObject();
	 js.put("AuthID", txnMaster.getMerchantId());
	 js.put("respData", encRespXML);
	 js.put("AggRefNo", txnMaster.getId());
	return js.toString(); //JSONObject.toString();
  }
@Override
public String redirectToPG(HttpServletRequest paramHttpServletRequest) {
	// TODO Auto-generated method stub
	return null;
}
}
