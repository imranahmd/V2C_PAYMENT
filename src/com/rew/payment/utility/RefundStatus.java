package com.rew.payment.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.pg.db.DataManager;
import com.rew.pg.dto.MerchantDTO;


public class RefundStatus extends HttpServlet 
{
    private static Logger logger = LoggerFactory.getLogger(RefundStatus.class);

	private static final long serialVersionUID = 1L;


	public RefundStatus() 
	{
		super();
	}



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		PrintWriter out = response.getWriter();
		StringBuffer jb = new StringBuffer();
		JSONObject jsonObject = null;
		JSONObject respJson = new JSONObject();
		BufferedReader reader=null;
		String txnId = "";
		String refundRequestId = "" ;
		String merchantId="";
		try 
		{
			String line = null;
			 reader = request.getReader();
			while ((line = reader.readLine()) != null)
			{
				jb.append(line);
			}

			jsonObject = new JSONObject(jb.toString());

			logger.info("RefundStatus.java :: request params : "+jsonObject.toString());

			String encData = null;

			merchantId = jsonObject.getString("merchantId");
			encData = jsonObject.getString("encData");


			logger.info("RefundStatus.java :: merchantId : "+merchantId+" , encData : "+encData);

			if(merchantId != null && !merchantId.isEmpty() && encData != null && !encData.isEmpty()) {
				if (!merchantId.equals(stripXSS(merchantId))) {
					respJson = refundStatusResponse("RS003", "Malicious Content", "NA", txnId, "NA",
							"NA", "NA", "NA", "NA", "NA", "NA", refundRequestId, respJson);
					out.write(respJson.toString());
				}else{
					DataManager dm = new DataManager();
					MerchantDTO merchantDTO = dm.getMerchant(merchantId);

					JSONObject json = getJsonRequest(merchantDTO.getTransactionKey(), encData, merchantDTO.getMerchantId());

					//logger.info("RefundStatus.java ::: Decrypted RefuendStatus Request : "+json);
					if (json != null) {

						String[] reqParams = {"txnId", "refundRequestId"};
						String sResp = PGUtils.null2Known(reqParams, json);

						if (sResp != null && sResp.equals("NOTNULL")) {
							if (!validateXSS(reqParams, json)) {
								respJson = refundStatusResponse("RS003", "Malicious Content", "NA",
										txnId, merchantId, "NA", "NA", "NA", "NA", "NA", "NA", refundRequestId, respJson);
								out.write(respJson.toString());
							} else {
								txnId = json.getString("txnId");
								refundRequestId = json.getString("refundRequestId");

								logger.info("RefundStatus.java ::: txnId : " + txnId + " and refundRequestId. : " + refundRequestId);

								respJson = dm.getRefundStatus(merchantId, txnId, refundRequestId);
								logger.info("respJson::::::::::::::  "+respJson.toString());
								out.write(respJson.toString());
							}
						} else {
							logger.info("txnId or merchantId or refundRequestId is Missing");
							respJson = refundStatusResponse("RS003", "Required Parameters are Missing", "NA", txnId, merchantId, "NA", "NA", "NA", "NA", "NA", "NA", refundRequestId, respJson);
							out.write(respJson.toString());
						}

					} else {
						logger.info("Error in decrypting Request");
						respJson = refundStatusResponse("RS003", "Required Parameters are Missing", "NA", txnId, merchantId, "NA", "NA", "NA", "NA", "NA", "NA", refundRequestId, respJson);
						out.write(respJson.toString());
					}
				}
			}
			else
			{
				logger.info("merchantId or encData is Missing");
				respJson=refundStatusResponse("RS003", "Required Parameters are Missing", "NA", txnId, merchantId, "NA", "NA", "NA", "NA", "NA", "NA", refundRequestId, respJson);
				out.write(respJson.toString());
			}
			/*if (respJson != null) { added on 29-10-23
				out.write(respJson.toString());
			}else {
				out.write("");
			}*/

		} 

		catch (Exception e) 
		{
			logger.info("RefundStatus.java :: "+e.getMessage());
			respJson=refundStatusResponse("RS003", "Required Parameters are Missing", "NA", txnId, merchantId, "NA", "NA", "NA", "NA", "NA", "NA", refundRequestId, respJson);
			if (respJson != null) {
				out.write(respJson.toString());
			}else {
				out.write("");
			}
		}
		
		finally {
			try
			{
				if(reader!=null)
				{
					reader.close();
				}
			}catch(Exception e)
			{
				e.printStackTrace();
				reader=null;
			}
			try
			{
				if(out!=null)
				{
					out.flush();
					out.close();
				}
			}catch(Exception e)
			{
				e.printStackTrace();
				out=null;
			}
		}
	}

	private static JSONObject getJsonRequest(String transactionKey, String sData, String merchantid)
	{
		logger.info("merchantid="+merchantid+" transactionKey length="+transactionKey.length());
		try
		{
			String confMerchantId = PGUtils.getPropertyValue("pspclId");
			String encryptedRequest = null;

			if(transactionKey.length()!=32)
			{
				if (confMerchantId.equals(merchantid))
				{
					logger.info("MerchantRequestParams.java PSPCL 128 DEC : ");
					encryptedRequest = PSPCLEncryptor.decrypt(transactionKey, transactionKey, sData);
				}
				else
				{
					logger.info("MerchantRequestParams.java GENERAL 128 DEC : ");
					encryptedRequest = PGUtils.getDecData(sData, transactionKey);
				}
				return new JSONObject(encryptedRequest);
			}
			else
			{
				logger.info("MerchantRequestParams.java 256 DEC : ");
				encryptedRequest = PSPCLEncryptor.decrypt(transactionKey,transactionKey.substring(0, 16),sData);
				return new JSONObject(encryptedRequest);
			}


		}
		catch (ParseException e)
		{
			logger.info("RefundRequest.java ::: getJsonRequest() :: Error while Decrypting the Refuend Request : ", e);
		}
		return null;
	}

	public static JSONObject refundStatusResponse(String resp_code,String resp_message,String pg_ref_id,String txn_id,String merchant_id,String refund_amount,String txn_amount,String rrn,String refund_type_status,String addedBy,String ArnNo,String refundRequestId,JSONObject respJson)
	{
		respJson.put("resp_code",resp_code);
		respJson.put("resp_message", resp_message);
		respJson.put("pg_ref_id", pg_ref_id);
		respJson.put("txn_id", txn_id);
		respJson.put("merchant_id", merchant_id);
		respJson.put("refund_amount", refund_amount);
		respJson.put("txn_amount", txn_amount);
		respJson.put("rrn", rrn);
		respJson.put("refund_type_status", refund_type_status);
		respJson.put("addedBy", addedBy);
		respJson.put("ArnNo", ArnNo);
		respJson.put("refundRequestId",refundRequestId);
		return respJson;
	}

	public static Pattern[] patterns = { Pattern.compile("[^-a-zA-Z0-9 /?&{}+=_:,|.'@~]*[`!%;<>#$%^*>()\">]*"),
			Pattern.compile("<script>(.*?)</script>", 2), Pattern.compile("src[\r\n]*=[\r\n]*\\'(.*?)\\'", 42),
			Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 42), Pattern.compile("</script>", 2),
			Pattern.compile("<script(.*?)>", 42), Pattern.compile("eval\\((.*?)\\)", 42),
			Pattern.compile("expression\\((.*?)\\)", 42), Pattern.compile("javascript:", 2),
			Pattern.compile("vbscript:", 2), Pattern.compile("onload(.*?)=", 42), Pattern.compile("onerror(.*?)=", 42),
			Pattern.compile("window.", 2), Pattern.compile("document.", 2), Pattern.compile("location.", 2),
			Pattern.compile("alert\\((.*?)\\)", 42) };

	private String stripXSS(String value) {
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

	public Boolean validateXSS(String[] reqFields, JSONObject jsonObject) {
		Boolean errorResponse = true;
		try {
			for (int i = 0; i < reqFields.length; i++) {
				String paramValue = jsonObject.getString(reqFields[i]);
				if (!stripXSS(paramValue).equals(paramValue)) {
					errorResponse = false;
					break;
				}
			}
		} catch (Exception e) {
			logger.error("PGUtils.java ::: null2Known() :: Error Occurred during Validation : ", e);
		}
		return errorResponse;
	}
	
	
	public static void main (String args[])
	{
		String sData="O9LljcxBcYE6fFYYIO1xRUWrX20bict+ccqIgEmxN/TE50nc8Zv77gr+mV1etsaflc8JJFwy8aczJK5Xks+2a7J09gJEktW0bn3EX0WMVc0D5JUqe2z/LgJ72m4SGvibQ63HlQqKL97QMb+h+4ITyA==";
		String transactionKey="ML0FH1Bd8LG4Xm9yx0gG0eS5wW0DU0fj";
		String encryptedRequest = PSPCLEncryptor.decrypt(transactionKey,transactionKey.substring(0, 16),sData);
		
		System.out.print("Encryption value ::::::::"+encryptedRequest);

		
	}

}
