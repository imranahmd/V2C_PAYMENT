package com.rew.payment.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.rew.pg.db.DataManager;
import com.rew.pg.dto.TransactionMaster;

public class S2SCallback {
	
	private static Logger logger = LoggerFactory.getLogger(S2SCall.class);



	public static int PostCallbackResponse(String id) {
		String line = null;
		BufferedReader br = null;
		StringBuffer respString=null;
		int status=0;
		DataManager dm = new DataManager();
		TransactionMaster txnMaster = dm.getTxnMaster(id);
        logger.info(" :::Enter in PostCallBack Response--------------------------" + txnMaster.getReturn_url());

        String encRespXML = PGUtils.generateResponse(txnMaster);

		if (encRespXML != null
                && (txnMaster.getReturn_url() != null || !txnMaster.getReturn_url().isEmpty())) {
            logger.info(" ::: inside if--------------------------------" +encRespXML);
			/*
			 * Unirest.setTimeouts(0, 0); try { HttpResponse<String> response =
			 * Unirest.post(txnMaster.getReturn_url()) .header("Content-Type",
			 * "application/x-www-form-urlencoded") . field("merchantId",
			 * txnMaster.getMerchantId()) . field("respData", encRespXML)
			 * .field("pgid",txnMaster.getId()) .asString(); logger.
			 * info(" ::: inside if----------Response status:::::::::------------------"
			 * +response.getStatus()); if (response.getStatus() == 200) {
			 * logger.info("Response sent to the merchant {}",txnMaster.getMerchantId());
			 * dm.updateIsPostedBackRes(txnMaster.getId()); status = 1;
			 * logger.info(" ::: inside if----------Response status:::::"+status); return
			 * status; } } catch (Exception e) {
			 * logger.error("Error in sending response to the merchant", e); }
			 */
            
            String data = "AuthID=" + txnMaster.getMerchantId()
            +"&respData=" + encRespXML
            +"&AggRefNo=" + txnMaster.getId();
         status = new DataPost().postData(txnMaster.getReturn_url(), data, id); 
        }
		return status;
	}

	

}
