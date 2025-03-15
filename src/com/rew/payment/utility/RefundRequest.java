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
import com.rew.pg.dto.RefundRequestAPI;

public class RefundRequest extends HttpServlet
{
    private static Logger logger = LoggerFactory.getLogger(RefundRequest.class);
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		BufferedReader reader = null;
		StringBuffer jb = new StringBuffer();
		JSONObject jsonObject = null;
		JSONObject respJson = new JSONObject();

		String txnId = "";
		String refundAmount = "";
		String mid = "";
		String refundRequestId = "";
		String addedBy = "";
		try {
			String line = null;
			reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}

			jsonObject = new JSONObject(jb.toString());

			logger.info("RefundRequest.java :: request params : " + jsonObject.toString());

			String respJSON = null;
			String encData = null;

			mid = jsonObject.getString("merchantId");
			encData = jsonObject.getString("encData");
			DataManager dm= new DataManager();
			logger.info("RefundRequest.java :: Mid : " + mid + " , encData : " + encData);

			if ((mid != null && !mid.isEmpty()) && (encData != null && !encData.isEmpty()))
			{
				MerchantDTO merchantDTO = dm.getMerchant(mid);
				
				JSONObject json = getJsonRequest(merchantDTO.getTransactionKey(), encData, merchantDTO.getMerchantId());

				logger.info("RefundTestFile.java ::: Decrypted Refuend Request : " + json.toString());
				if (json != null)
				{
					String[] reqParams = { "txnId", "refundAmount", "refundRequestId","addedBy" };
					String sResp = PGUtils.null2Known(reqParams, json);
					logger.info("sResp==>"+sResp);
					if ((sResp != null) && (sResp.equals("NOTNULL")))
					{
						logger.info("call the db operation");
						txnId = json.getString("txnId");
						refundAmount = json.getString("refundAmount");
						refundRequestId = json.getString("refundRequestId");
						addedBy=json.getString("addedBy");

						String atrn = "";
						if ((json.has("pgRefId")) && (json.getString("pgRefId") != null))
						{
							atrn = json.getString("pgRefId");
						}
						logger.info("RefuendRequest.java ::: Merchant Id : " + mid + " and Merchant Order No. : " + txnId + " Refund Amount : " + refundAmount);

						RefundRequestAPI RRA = dm.getRefundTxnDetails(mid, txnId, refundAmount, atrn, refundRequestId.trim(),addedBy);

						if (RRA.getRespMessage()!="Error Occurred while fetching Data.")
						{
							respJson=refundResponse( RRA.getErrorCode() ,RRA.getRespMessage(),RRA.getTxnId(), RRA.getMerchantId(), RRA.getId(), RRA.getRefund_amount(), RRA.getAmount(), RRA.getRefund_type(), RRA.getServiceRRN(),RRA.getRefundRequestId(),RRA.getUploadedBy(),respJson);
							logger.info("RefuendRequest.java ::: generateRefundRequestResponse() :: JSON Response Generated : " + respJson);
							out.write(respJson.toString());
						}
						else
						{
							respJson=refundResponse( "RF009", "Error Occurred while fetching Data.", txnId, mid, "NA", refundAmount, "NA", "NA", "NA", refundRequestId,addedBy,respJson);
							logger.info("RefuendRequest.java ::: generateRefundRequestResponse() :: JSON Response Generated : " + respJson);
							out.write(respJson.toString());
						}

					} else {

						respJson = refundResponse("RF006", "Required parameter is missing or blank.", txnId, mid, "NA",
								refundAmount, "NA", "NA", "NA", refundRequestId, addedBy, respJson);
						out.write(respJson.toString());
					}
				}
			} else {
				respJson = refundResponse("RF006", "Required parameter is missing or blank.", txnId, mid, "NA",
						refundAmount, "NA", "NA", "NA", refundRequestId, addedBy, respJson);
				out.write(respJson.toString());
			}

		}
		catch (Exception e)
		{
			logger.info("RefundRequest.java :: " + e.getMessage());
			respJson=refundResponse( "RF006", "Required parameter is missing.", txnId, mid, "NA", refundAmount, "NA", "NA", "NA",refundRequestId,addedBy,respJson);
			out.write(respJson.toString());

		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				reader = null;
			}
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				out = null;
			}
		}

	}

	public static JSONObject refundResponse(String respCode, String respMessage, String txnId, String mid,
			String pgrefId, String refundAmount, String txnAmount, String refundTypeStatus, String rrn,
			String refundRequestId, String addedBy, JSONObject respJson) {
		respJson.put("resp_code", respCode);
		respJson.put("resp_message", respMessage);
		respJson.put("txn_id", txnId);
		respJson.put("merchant_id", mid);

		respJson.put("pg_ref_id", pgrefId);
		respJson.put("refund_amount", refundAmount);
		respJson.put("txn_amount", txnAmount);
		respJson.put("refund_type_status", refundTypeStatus);
		respJson.put("refundRequestId", refundRequestId);
		respJson.put("rrn", rrn);
		respJson.put("addedBy", addedBy);
		return respJson;

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
			logger.info("RefuendRequest.java ::: getJsonRequest() :: Error while Decrypting the Refuend Request : ", e);
		}
		return null;
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

}