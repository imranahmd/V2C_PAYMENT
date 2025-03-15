package com.rew.pg.dto;

import java.text.ParseException;
import java.util.Vector;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.payment.utility.PGUtils;
import com.rew.payment.utility.PSPCLEncryptor;
import com.rew.pg.db.DataManager;

public class PGTransactionDTO {
    private static Logger logger = LoggerFactory.getLogger(PGTransactionDTO.class);
	
	private MerchantDTO merchantDTO=null;
	private Vector storedCardDetailsCC=null;
	private Vector storedCardDetailsDC=null;
	private JSONObject json=null;
	private TransactionMaster TM=null;
	private DataManager dm=null;
	
	public MerchantDTO getMerchantDTO() {
		return merchantDTO;
	}
	public void setMerchantDTO(MerchantDTO merchantDTO) {
		this.merchantDTO = merchantDTO;
	}
	public Vector getStoredCardDetailsCC() {
		return storedCardDetailsCC;
	}
	public void setStoredCardDetailsCC(Vector storedCardDetailsCC) {
		this.storedCardDetailsCC = storedCardDetailsCC;
	}
	public Vector getStoredCardDetailsDC() {
		return storedCardDetailsDC;
	}
	public void setStoredCardDetailsDC(Vector storedCardDetailsDC) {
		this.storedCardDetailsDC = storedCardDetailsDC;
	}
	public TransactionMaster getTM() {
		return TM;
	}
	public void setTM(TransactionMaster tM) {
		TM = tM;
	}
	public JSONObject getJson() {
		return json;
	}
	public void setJson(JSONObject json) {
		this.json = json;
	}
	public PGTransactionDTO getData(String merchantId,String reqData)throws Exception{
		
		this.dm=new DataManager();
		this.merchantDTO=dm.getMerchant(merchantId);
		this.setMerchantDTO(this.merchantDTO);
		
		this.json=this.getJsonRequest(this.merchantDTO.getTransactionKey(), reqData, merchantId);
		this.setJson(json);
	

		return this;
	}
	
	public PGTransactionDTO getCardsData(JSONObject json)
	{
		
		this.TM=this.getTransactionMaster(json);
		this.setTM(this.TM);
		
		this.storedCardDetailsCC=dm.getStoredCards(TM.getCustMail(), TM.getCustMobile(), TM.getMerchantId());
		this.setStoredCardDetailsCC(this.storedCardDetailsCC);

		this.storedCardDetailsDC=dm.getStoredCards(TM.getCustMail(), TM.getCustMobile(), TM.getMerchantId());
		this.setStoredCardDetailsDC(this.storedCardDetailsDC);
		return this;
	}
	
	public TransactionMaster saveTM(TransactionMaster TM)
	{
		this.TM=this.dm.saveData(TM);
		return TM;
	}
	
	
	public int saveRetry(TransactionMaster TM,String reqdata)
	{
		int status=this.dm.insertTxnRetry( TM.getMerchantId(),TM.getTxnId(), reqdata, TM.getUploadedBy(), TM.getDateTime());
		return status;
	}
	
	private JSONObject getJsonRequest(String transactionKey, String sData, String merchantId) {
	    try {
	      logger.info("PGTransactionDTO.java calling getDecData() for merchantid="+merchantId);
	      String confMerchantId = PGUtils.getPropertyValue("pspclId");
	      String decryptedValue = null;
	      if (confMerchantId.equals(merchantId)) {
	        logger.info("PSPCL DEC for merchantid="+merchantId);
	        decryptedValue = PSPCLEncryptor.decrypt(transactionKey, transactionKey, sData);
	      }
	      else {
	        logger.info("GENERAL DEC for merchantid="+merchantId+" transactionKey.length="+transactionKey.length());
	        
	        
	        if(transactionKey.length()==32)
		      {
		    	  logger.info("Transaction key length is 32 bit allowed 8 and 16 bit");
		    	  return null;
		      }
	        
	        decryptedValue = PGUtils.getDecData(sData, transactionKey);
	      }
	      return new JSONObject(decryptedValue);
	    }
	    catch (ParseException var6) {
	      logger.error("PGTransactionDTO.java ::: getJsonRequest() :: Error while Decrypting the Transaction Request : ", var6);
	    }
	    return null;
	  }
	
	 private TransactionMaster getTransactionMaster(JSONObject json)
	  {
			String udf6= removeWhitespaces(json.getString("udf6"));

	      TransactionMaster TM = new TransactionMaster();
	      TM.setMerchantId(json.getString("merchantId"));
	      TM.setTxnId(json.getString("txnId"));
	      TM.setDateTime(json.getString("dateTime"));
	      TM.setAmount(json.getString("amount"));
	      TM.setCustMobile(json.getString("custMobile"));
	      TM.setCustMail(json.getString("custMail"));
	      TM.setReturn_url(json.getString("returnURL"));
	      TM.setApiKey(json.getString("apiKey"));
	      TM.setUdf1(json.getString("udf1"));
	      TM.setUdf2(json.getString("udf2"));
	      TM.setUdf3(json.getString("udf3"));
	      TM.setUdf4(json.getString("udf4"));
	      TM.setUdf5(json.getString("udf5"));
	      TM.setChannelId(json.getString("channelId"));
	      TM.setTxnType(json.getString("txnType"));
	      TM.setInstrumentId(json.getString("instrumentId"));
	      TM.setRespStatus("99");
	      TM.setServiceRRN("RES");
	      TM.setServiceTxnId("RES");
	      TM.setServiceAuthId("RES");
	      TM.setRespMessage("RES");
	      TM.setProcessId("NA");
	      TM.setIsMultiSettlement(json.getString("isMultiSettlement"));
	      TM.setProductId(json.getString("productId"));
	      TM.setSurcharge("0.00");
	      TM.setRespDateTime(json.getString("dateTime"));
	      TM.setCardDetails("RES");
	      TM.setCardType(json.getString("cardType"));
	      TM.setModified_By("PGTransaction");
	      TM.setModified_On(json.getString("dateTime"));
	      TM.setBankId(json.getString("cardDetails"));
	      TM.setUploadedBy("V1");  
	      TM.setUdf6(udf6);
		  TM.setReseller_id(json.getString("Rid"));
		  TM.setReseller_txn_id(json.getString("ResellerTxnId"));// changes Udf
		return TM;
		  
	  }
	 public static String removeWhitespaces(String json) {

		    boolean quoted = false;
		    boolean escaped = false;
		    String out = "";

		    for(Character c : json.toCharArray()) {

		        if(escaped) {
		            out += c;
		            escaped = false;
		            continue;
		        }

		        if(c == '"') {
		            quoted = !quoted;
		        } else if(c == '\\') {
		            escaped = true;
		        }

		        if(c == ' ' &! quoted) {
		            continue;
		        }

		        out += c;

		    }
logger.info("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::"+out);
		    return out;

		}
}
