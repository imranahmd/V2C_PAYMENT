package com.rew.upi.hdfc;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.payment.utility.PGUtils;
import com.rew.payment.utility.S2SCall;


public class UPIOperations
{
    private static Logger logger = LoggerFactory.getLogger(UPIOperations.class);
  


  private static String upiCheckVPAUrlHDFC = PGUtils.getPropertyValue("upiCheckVPAUrlHDFC");
  private static String upiProcessPayUrlHDFC = PGUtils.getPropertyValue("upiProcessPayUrlHDFC");
  private static String upiCheckTxnStatusUrlHDFC = PGUtils.getPropertyValue("upiCheckTxnStatusUrlHDFC");
  
  public UPIOperations() {}
  
  public static String checkVPA(String merchantId, String vpa, String merchantRefNo, String status, String upiKeyHDFC) throws Exception {
    String sData = merchantId + "|" + merchantRefNo + "|" + vpa + "|" + status + "|||||||||NA|NA";
    logger.debug("UPIOperations.java ::: checkVPA() :: UPI Request Normal Text :: " + sData);
    
    sData = new UPISecurity().encrypt(sData, upiKeyHDFC);
    
    JSONObject json = new JSONObject();
    
    json.put("requestMsg", sData);
    json.put("pgMerchantId", merchantId);
    
    logger.debug("UPIOperations.java ::: checkVPA() :: UPI Request :: " + json.toString());
    
    sData = S2SCall.secureServerCall(upiCheckVPAUrlHDFC, json.toString());
    
    if (sData != null)
    {
      sData = new UPISecurity().decrypt(sData, upiKeyHDFC);
      
      logger.debug("UPIOperations.java ::: checkVPA() :: Decrypted Data : " + sData);
      
      sData = createJsonResponse(sData);
      logger.debug("UPIOperations.java ::: checkVPA() :: Json Response Data : " + sData);
    }
    
    return sData;
  }

  public static String processPay(String merchantId, String vpa, String merchantRefNo, String amount, String Remarks, String Expiry, String upiKeyHDFC)
    throws Exception
  {
    String sData = merchantId + "|" + merchantRefNo + "|" + vpa + "|" + amount + "|" + Remarks + "|" + Expiry + "|6012|||||||||NA|NA";
    
    logger.debug("UPIOperations.java ::: processPay() :: Plain text Request :: " + sData);
    
    sData = new UPISecurity().encrypt(sData, upiKeyHDFC);
    
    JSONObject json = new JSONObject();
    
    json.put("requestMsg", sData);
    
    json.put("pgMerchantId", merchantId);
    
    logger.debug("UPIOperations.java ::: processPay() :: UPI Request : " + json.toString());
    
    sData = S2SCall.secureServerCall(upiProcessPayUrlHDFC, json.toString());
    
    sData = new UPISecurity().decrypt(sData, upiKeyHDFC);
    
    logger.debug("UPIOperations.java ::: processPay() :: Decrypted Data : " + sData);
    


    sData = createJsonResponse(sData);
    
    logger.debug("UPIOperations.java ::: processPay() :: Json Response Data : " + sData);
    
    return sData;
  }
  
 /* public static String checkTxnStatus(String merchantId, String merchantRefNo) throws Exception
  {
    String upiKeyHDFC = PGUtils.getPropertyValue("upiKeyHDFC");
    
    String sData = merchantId + "|" + merchantRefNo + "|||||||||||NA|NA";
    sData = new UPISecurity().encrypt(sData, upiKeyHDFC);
    JSONObject json = new JSONObject();
    json.put("requestMsg", sData);
    json.put("pgMerchantId", merchantId);
    logger.debug("UPIOperations.java ::: checkTxnStatus() :: UPI Request :: " + json.toString());
    PostToSoR pso = new PostToSoR();
    sData = pso.postData(upiCheckTxnStatusUrlHDFC, json.toString());
    sData = new UPISecurity().decrypt(sData, upiKeyHDFC);
    logger.debug("UPIOperations.java ::: checkTxnStatus() :: Decrypted Data : " + sData);
    
    sData = createJsonResponse(sData);
    logger.debug("UPIOperations.java ::: checkTxnStatus() :: Json Response Data : " + sData);
    return sData;
  }*/
  

  public static String processCallback(String merchantId, String encRequest, String upiKeyHDFC)
    throws Exception
  {
    String sData = new UPISecurity().decrypt(encRequest, upiKeyHDFC);
    logger.debug("UPIOperations.java ::: processCallback() :: Decrypted Data : " + sData);
    
    sData = createJsonResponse(sData);
    logger.debug("UPIOperations.java ::: processCallback() :: Json Response Data : " + sData);
    return sData;
  }
  
  private static String createJsonResponse(String pipeSeperatedValue)
  {
    JSONObject json = new JSONObject();
    
    String[] strResp = pipeSeperatedValue.split("\\|");
    for (int i = 0; i < strResp.length; i++)
    {
      String value = strResp[i];
      json.put("PARAM" + i, value);
    }
    
    return json.toString();
  }
  
	/*
	 * public static void main(String[] args) { try { String upiKeyHDFC =
	 * PGUtils.getPropertyValue("upiKeyHDFC"); //checkVPA("HDFC000000648372",
	 * "9833700965@upi", "1234", "T", upiKeyHDFC);
	 * 
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace(); } }
	 */
 }
