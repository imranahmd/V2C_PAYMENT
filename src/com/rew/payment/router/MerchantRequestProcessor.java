package com.rew.payment.router;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.payment.utility.PGUtils;
import com.rew.payment.utility.PSPCLEncryptor;

public class MerchantRequestProcessor
extends HttpServlet
{
    private static Logger logger = LoggerFactory.getLogger(MerchantRequestProcessor.class);


	private static final long serialVersionUID = 1L;



	public MerchantRequestProcessor() {}



	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		response.getWriter().append("Get Method not Allowed.");
	}

	protected void doOption(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getWriter().append("Option Method not Allowed.");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();

		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		response.setDateHeader("Expires", -1L);
		response.setDateHeader("Last-Modified", 0L);



		JSONObject json = new JSONObject();

		//List<String> requiredParams = Arrays.asList(new String[] { "merchantId", "txnId", "dateTime", "amount", "apiKey", "custMobile", "custMail", "channelId", "txnType", "checksum", "udf1", "udf2", "udf3", "udf4", "udf5","udf6","Rid","ResellerTxnId","type", "instrumentId", "cardType", "isMultiSettlement", "productId", "returnURL", "cardDetails" });
		List<String> requiredParams = Arrays.asList(new String[] { "AuthID", "CustRefNum", "PaymentDate", "txn_Amount", "AuthKey", "ContactNo", "EmailId", "IntegrationType", "checksum", "adf1", "adf2", "adf3","Rid","ResellerTxnId","type", "MOP", "MOPType", "CallbackURL", "MOPDetails" });

		boolean falseParam = false;

		Enumeration<String> e = request.getParameterNames();
		while (e.hasMoreElements())
		{
			String fieldName = (String)e.nextElement();
			String fieldValue = request.getParameter(fieldName);


			if (!requiredParams.contains(fieldName))
			{
				falseParam = true;
				logger.debug("MerchantRequestProcessor.java ::: Invalid Input Paramter Received in Request :: " + fieldName);

			}
			else
			{
				json.put(fieldName, fieldValue);
			}
		}

		if (!falseParam)
		{


			String merchantId = request.getParameter("AuthID");
			String apiKey = request.getParameter("AuthKey");
			String encryptedRequest = null;
			String type=request.getParameter("type");
			String URL=null;
			
			if(merchantId.equalsIgnoreCase("M00075")) {
				if(!validateRequest(json)) {
					request.setAttribute("errorMsg", "Error 10051 : Error while processing your request");
					request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
					logger.error("Error while encrypting by given key "+apiKey);
				}
			}
			
			logger.debug("type ---- "+type);
			if(type.equalsIgnoreCase(null) || type.equalsIgnoreCase(""))
				type="1.0";

			if(type.equalsIgnoreCase("1.0"))
			{
				URL = PGUtils.getPropertyValue("paymentURL");
			}
			else 
			{
				URL = PGUtils.getPropertyValue("paymentURLV2");
			}
			logger.debug("=======URL="+URL+" =============================");
			String confMerchantId = PGUtils.getPropertyValue("pspclId");

			if (confMerchantId.equals(merchantId))
			{
				logger.debug("MerchantRequestParams.java PSPCL DEC : ");
				encryptedRequest = PSPCLEncryptor.encrypt(apiKey, apiKey, json.toString());
			}
			else if(type.equalsIgnoreCase("1.1"))
			{
				logger.debug("MerchantRequestParams.java type : "+type+" apiKey.length="+apiKey.length());
				/*if(apiKey.length()!=32)
				{
					request.setAttribute("errorMsg", "Error 10052 : Invalid Key");
					request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
				}
				else
				{*/
					encryptedRequest = PSPCLEncryptor.encrypt(apiKey, apiKey.substring(0, 16),json.toString());  
				/*}*/
			}
			else
			{
				/*logger.debug("MerchantRequestParams.java GENERAL DEC : apiKey.length="+apiKey.length());
				if(apiKey.length()==32)
				{
					request.setAttribute("errorMsg", "Error 10052 : Invalid Key");
					request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
				}
				else
				{*/
					encryptedRequest = PGUtils.getEncData(json.toString(), apiKey);
				/*}*/
			}
			logger.debug("MerchantRequestProcessor.java ::: Merchant Id :: " + merchantId + " And API Key :: " + apiKey);


			if (encryptedRequest == null) {
				request.setAttribute("errorMsg", "Error 10051 : Error while processing your request");
				request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
				logger.error("Error while encrypting by given key "+apiKey);
			} else {
			StringBuilder postReq = new StringBuilder();
			postReq.append("<HTML>");
			postReq.append("<BODY>");
			postReq.append("<FORM ACTION = '" + URL + "' METHOD='POST'>");			
			postReq.append("<input type=\"hidden\" name=\"encData\" value=\"" + encryptedRequest + "\">");
			postReq.append("<input type=\"hidden\" name=\"AuthID\" value=\"" + merchantId + "\">");
			postReq.append("</FORM>");
			postReq.append("</BODY>");
			postReq.append("<SCRIPT>");
			postReq.append("document.forms[0].submit();");
			postReq.append("</SCRIPT>");
			postReq.append("</HTML>");

			out.println(postReq.toString());
			
			}
			out.flush();
			out.close();
		}
		else
		{
			request.setAttribute("errorMsg", "Error 10053 : Invalid Input Parameter Received in Request.");
			request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
		}
	}


	private boolean validateRequest(JSONObject json) {
		logger.debug("validating dealer request=");
		boolean isValid = true;
		String mobileRegex = "[0-9]{10}";
		String nameRegex = "^[A-Za-z ]+$";
		String mobile = json.getString("custMobile");
		String name = json.getString("udf5");
		String amount = json.getString("amount");
		String address = json.getString("udf2");
		String pincode = json.getString("udf1");
		String dob = json.getString("udf4");
		//changes udf6
		String Param = json.getString("udf6");
		logger.info("Param::::::::::::::::::::::::::::::::::::::: "+Param);
		String mail = json.getString("custMail");
		if(mobile == null || mobile.equals("") || !mobile.matches(mobileRegex)) {
			isValid = false;
		}else if(name == null || name.equals("") || !name.matches(nameRegex)) {
			isValid = false;
		}else if(address == null || address.equals("")) {
			isValid = false;
		}else if(pincode == null || pincode.equals("") || pincode.length() < 6) {
			isValid = false;
		}else if(mail == null || mail.equals("")) {
			isValid = false;
		}else if(dob == null || dob.equals("")) {
			isValid = false;
		}else if(amount == null || amount.equals("")) {
			isValid = false;
		}else if(Param == null || Param.equals(""))
		{
			isValid = false;	
		}
		return isValid;
	}
	
	 public static void main(String[] args)
		{
			String enc="{\"dateTime\":\"2022-11-16 19:07:02\","
					+ "\"amount\":\"1.00\","
					+ "\"isMultiSettlement\":\"0\","
					+ "\"custMobile\":\"9876543210\","
					+ "\"apiKey\":\"Aa3ZI6aj1ag8We1Is1ny5ty4kx8MC6NO\","
					+ "\"productId\":\"DEFAULT\","
					+ "\"instrumentId\":\"NA\","
					+ "\"cardType\":\"NA\","
					+ "\"txnType\":\"DIRECT\","
					+ "\"udf3\":\"NA\",\"udf1\":\"NA\",\"udf2\":\"NA\",\"merchantId\":\"M00005357\",\"custMail\":\"test@test.com\",\"returnURL\":\"https://www.hughey.in/\",\"channelId\":\"0\",\"txnId\":\"230820181535031422094\",\"udf4\":\"NA\",\"udf5\":\"NA\",\"udf6\":\"NA\",\"cardDetails\":\"NA\"}";
		
			String apikey="Aa3ZI6aj1ag8We1Is1ny5ty4kx8MC6NO";
		    String encryptedRequest = PGUtils.getEncData(enc, apikey);
		    
		    System.out.print(encryptedRequest);

		
		}
}
