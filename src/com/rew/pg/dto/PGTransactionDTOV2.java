package com.rew.pg.dto;

import java.text.ParseException;
import java.util.Vector;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.payment.utility.PSPCLEncryptor;
import com.rew.pg.db.DataManager;

public class PGTransactionDTOV2 {
	private static Logger logger = LoggerFactory.getLogger(PGTransactionDTOV2.class);

	private MerchantDTO merchantDTO = null;
	private Vector storedCardDetailsCC = null;
	private Vector storedCardDetailsDC = null;
	private JSONObject json = null;
	private TransactionMaster TM = null;
	private DataManager dm = null;

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

	public PGTransactionDTOV2 getData(String merchantId, String reqData) throws Exception {

		this.dm = new DataManager();
		this.merchantDTO = dm.getMerchant(merchantId);
		this.setMerchantDTO(this.merchantDTO);
		logger.info("request Data  :::::::::::" + reqData);
		if (merchantDTO.getTransactionKey().length() == 8) {
			PGTransactionDTO dm = new PGTransactionDTO();

			this.json = dm.getJson();
			logger.info("json Data  :::::::DTO::::" + json);
		} else {
			this.json = this.getJsonRequest(this.merchantDTO.getTransactionKey(), reqData, merchantId);
			logger.info("json Data  :::::DTOV2::::::" + json);
		}

		this.setJson(json);

		return this;
	}

	public PGTransactionDTOV2 getCardsData(JSONObject json) {
		this.dm = new DataManager();
		this.TM = this.getTransactionMaster(json);
		this.setTM(this.TM);

		this.storedCardDetailsCC = dm.getStoredCards(TM.getCustMail(), TM.getCustMobile(), TM.getMerchantId());
		this.setStoredCardDetailsCC(this.storedCardDetailsCC);

		this.storedCardDetailsDC = dm.getStoredCards(TM.getCustMail(), TM.getCustMobile(), TM.getMerchantId());
		this.setStoredCardDetailsDC(this.storedCardDetailsDC);
		return this;
	}

	public TransactionMaster saveTM(TransactionMaster TM) {
		this.dm = new DataManager();
		this.TM = this.dm.saveData(TM);
		return TM;
	}

	public int saveRetry(TransactionMaster TM, String reqdata) {
		int status = this.dm.insertTxnRetry(TM.getMerchantId(), TM.getTxnId(), reqdata, TM.getUploadedBy(),
				TM.getDateTime());
		return status;
	}

	private JSONObject getJsonRequest(String transactionKey, String sData, String merchantId) throws Exception {
		try {
			logger.info("PGTransactionDTO.java calling getDecData() for merchantid=" + merchantId
					+ " transactionKey.length=" + transactionKey.length());

			String decryptedValue = null;

			if (transactionKey.length() != 32) {
				logger.info("Transaction key length is not 32 bit");
				return null;
			}
			decryptedValue = PSPCLEncryptor.decrypt(transactionKey, transactionKey.substring(0, 16), sData);
			logger.info("decryptedValue:::SS:::::" + transactionKey + ":::::::::::::::: " + decryptedValue);

			return new JSONObject(decryptedValue);
		} catch (ParseException var6) {
			logger.error(
					"PGTransactionDTO.java ::: getJsonRequest() :: Error while Decrypting the Transaction Request : ",
					var6);
		}
		return null;
	}

	private TransactionMaster getTransactionMaster(JSONObject json) {
		String udf6 = removeWhitespaces(json.getString("udf6"));

		if (json.has("Paymentdate")) {
			json.put("PaymentDate", json.getString("Paymentdate"));
		}
		logger.info("Check callback");
		TransactionMaster TM = new TransactionMaster();
		TM.setMerchantId(json.getString("AuthID"));
		TM.setTxnId(json.getString("CustRefNum"));
		TM.setDateTime(json.getString("PaymentDate"));
		TM.setAmount(json.getString("txn_Amount"));
		TM.setCustMobile(json.getString("ContactNo"));
		TM.setCustMail(json.getString("EmailId"));
		TM.setReturn_url(json.getString("CallbackURL"));
		TM.setApiKey(json.getString("AuthKey"));
		TM.setUdf1(json.getString("adf1"));
		TM.setUdf2(json.getString("adf2"));
		TM.setUdf3(json.getString("adf3"));
		TM.setUdf4("NA");
		TM.setUdf5("NA");
		TM.setChannelId("0");
		TM.setTxnType(json.getString("IntegrationType"));
		TM.setInstrumentId(json.getString("MOP"));
		TM.setRespStatus("99");
		TM.setServiceRRN("RES");
		TM.setServiceTxnId("RES");
		TM.setServiceAuthId("RES");
		TM.setRespMessage("RES");
		TM.setProcessId("NA");
		TM.setIsMultiSettlement("0");
		TM.setProductId("DEFAULT");
		TM.setSurcharge("0.00");
		TM.setRespDateTime(json.getString("PaymentDate"));
		TM.setCardDetails(json.has("MOPDetails") ? json.getString("MOPDetails") : "RES");
		TM.setCardType(json.getString("MOPType"));
		TM.setModified_By("PGTransaction");
		TM.setModified_On(json.getString("PaymentDate"));
		TM.setBankId(json.getString("MOPType"));
		TM.setUploadedBy("V2");
		TM.setUdf6(udf6);
		TM.setReseller_id(json.has("Rid") ? json.getString("Rid") : "NA");
		TM.setReseller_txn_id(json.has("ResellerTxnId") ? json.getString("ResellerTxnId") : "NA");

		logger.info("Check callback  " + TM.toString());

		return TM;

	}

	public static String removeWhitespaces(String json) {

		boolean quoted = false;
		boolean escaped = false;
		String out = "";

		for (Character c : json.toCharArray()) {

			if (escaped) {
				out += c;
				escaped = false;
				continue;
			}

			if (c == '"') {
				quoted = !quoted;
			} else if (c == '\\') {
				escaped = true;
			}

			if (c == ' ' & !quoted) {
				continue;
			}

			out += c;

		}
		logger.info("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::" + out);
		return out;

	}

	public static void main(String[] args) {
		String transactionKey = "dg8AU7cv1OB6nC5sO3Hu8Nw6pE2UZ0Li";
		String sData = "TliMsI4BqX9kB6me0GWTsGUucafgEEUfQGezGSGr6trj5PZksGztOjMd4MJtbE87rsmfxKtifcsrFSwvATRjZPYAORu/uJbuOvtQOm9b6wlSbTNXZrt5Hbz7ZZNi1YDEnLsx8KmTDE3ADZBODoHC/6sTT/A4KCuuo5k02oFNMWq9IJU7NZ/Y2wMuBfpkDoGgSKLMZoYyNYKPWxQhJCbcMm6yrRODBNTYta2IBDQnxsTENYr1vHQFr3WwyT1F+/tugspPvfif8Vk6/6F3Qc4DCs8IRE5TdIPwhGQqgiIXPwFa+x4/P5r/ZjnAxTcCo1InuSSdcpWmkfegx2YGeMJEyzKjmkLSNgyFnsiR+TRgTSgBaE1jtZaURoceatZLPJZ9zdoapk1EnJT1B9PONJ2rE+Y0QyQmpSQGAcdN/djBbROeMAzOhclTe5kX7ABEbttekEptSAgJXt0f1wQcVrvguhOwALdlpQe5ueIu9idE8xzYuzgcACSKfgY5R6XCbsTfCEUaR1GT8Nx6BYUjurtSGStOBS1PokJbTi6kx6KNjdIyxbPVEjQMM2KUdLMqlgEcADYrsfc92iKa7lxFnZ99HF3wwzRHOz09ecSuMSPncpmzEM62ZsLaVJk07ADKe46H+wnhB8K9YgfGfLyN2t8AXOS5BAqT2+MxgWp9BzJLqQ6g/8aonCAtDfTcPVWNgOde";

		String decryptedValue = PSPCLEncryptor.decrypt(transactionKey, transactionKey.substring(0, 16), sData);

		System.out.print("decrypt::: " + decryptedValue);

	}
}
