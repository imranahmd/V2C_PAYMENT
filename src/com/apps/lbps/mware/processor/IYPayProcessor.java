package com.apps.lbps.mware.processor;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.payment.utility.PGUtils;

/**
 * Servlet implementation class PayProcessor
 */
public class IYPayProcessor extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static Logger logger = LoggerFactory.getLogger(IYPayProcessor.class);


	/**
	 * Default constructor. 
	 */
	public IYPayProcessor() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{


		logger.debug("-------Link Based Payment System [LBPS] ---------- IYOGA PayProcessor called ------------------");

		response.setHeader("Cache-control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "-1");
		PrintWriter out = response.getWriter();

		Enumeration params = request.getParameterNames(); 
		while(params.hasMoreElements())
		{
			String paramName = (String)params.nextElement();	
			logger.debug(paramName + "   <<<Value>>> " + request.getParameter(paramName) );
		}

		String txnId = request.getParameter("invoiceId");
		try {
			txnId=txnId+"-"+PGUtils.randomNumeric(4);
		} catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
			e1.printStackTrace();
		}
		logger.debug("invoiceId : [txnId = "+txnId+"]");

		String amount = request.getParameter("amount");
		String udf1 = request.getParameter("studentId");
		String udf2 = request.getParameter("studentName");
		String merchantId = request.getParameter("merchantId");
		String centerCode = request.getParameter("centerCode");
		String expDate = request.getParameter("expDate");  //expiry date format yyyyMMdd
		String email = "";
		String mobile = ""; 

		if(request.getParameter("emailid")!=null) 
			email=request.getParameter("emailid");
		else 
			email="NA";

		if(request.getParameter("Mobileno")!=null) 
			mobile=request.getParameter("Mobileno");
		else 
			mobile="9876543219";

		logger.debug("Request ====>>>> amount="+amount+" | " +"udf1 studentId="+udf1+" | " +"udf2 studentName="+udf2+" | " +" merchantId="+merchantId+" | " +" centerCode="+centerCode+" | " +" expDate="+expDate+" | "+" email="+email+" | "+" mobile="+mobile);

		CRUDService objCRUDService=new CRUDService();

		Date d1=null;			


		try {

			d1 = new SimpleDateFormat("yyyyMMdd").parse(expDate);

			String incomingInvoiceNo=request.getParameter("invoiceId");

			String merchant_id=merchantId;
			float amountf=Float.parseFloat(amount);
			Timestamp date_time=null;
			String valid_upto=expDate;
			String cust_name=udf2; //Student Name
			String remarks="Processed for LOYRT IY";
			String email_id=email;
			String contact_number=mobile;
			String link="/IYMware/PayProcessor";
			String status="0";
			Timestamp createdOn=null;
			String createdBy=merchantId;
			Timestamp modifiedOn=null;
			String modifiedBy= merchantId;
			boolean isProcessed=false;


			boolean isExist = objCRUDService.checkInvoiceNumber(incomingInvoiceNo, merchantId);
			logger.debug("Already exist | isExist = "+isExist);

			if(!isExist)
				objCRUDService.insertPaymentLinkDetails(incomingInvoiceNo, merchant_id, amountf, date_time, valid_upto, cust_name, remarks, email_id, contact_number, link, status, createdOn, createdBy, modifiedOn, modifiedBy);
			else
			{
				String stts=objCRUDService.getStatus(incomingInvoiceNo,merchant_id);
				if(stts.equals("1")) 
				{
					isProcessed=true;	
					logger.debug("Already processed");
				}else{
					isProcessed=false;	
					logger.debug("Again Processing as status = "+stts);
				}

			}

			logger.debug("LOYRT : isProcessed = "+isProcessed);

			if(isProcessed) {

				response.sendRedirect("xssErrorPageIYPay.jsp?errorMessage=Already Processed");
			}else {			

				if(daysBetweenUsingJoda(d1)>=0 ) 
				{
					
					logger.debug("LOYRT PayProcessor.java :::: incomingInvoiceNo = "+incomingInvoiceNo+" | txnId = "+txnId);

					String URL = PGUtils.getPropertyValue("paymentURL");
					logger.debug("URL="+URL);

					JSONObject json = new JSONObject();
					json.put("merchantId", merchantId);
					json.put("apiKey", PGUtils.getPropertyValue("IYPaymerchantKey")); //jpuT6032
					json.put("txnId", txnId);
					json.put("amount", amount);
					json.put("dateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					json.put("custMail", email);
					json.put("custMobile", mobile);
					json.put("udf1",udf1);
					json.put("udf2", udf2);
					json.put("udf3", expDate);
					json.put("udf4", "NA");
					json.put("udf5", centerCode);
					json.put("returnURL", PGUtils.getPropertyValue("IYPayreturnURL")); 
					json.put("isMultiSettlement","0");
					json.put("productId", centerCode);
					json.put("channelId", "0");
					json.put("txnType", "DIRECT");
					json.put("instrumentId", "NA");
					json.put("cardType", "NA");
					json.put("cardDetails", "NA");


					String encData=PGUtils.getEncData(json.toString(), PGUtils.getPropertyValue("IYPaymerchantKey"));
					StringBuilder sb = new StringBuilder();
					sb.append("<html>");
					sb.append("<body>");
					logger.debug("Yahan tak aaya....." +json.toString());
					logger.debug("Yahan tak aaya....." +URL);
					sb.append("<form action=\""+URL+"\" method='post'>");
					sb.append("<input type='hidden' name='reqData' value=\""+encData+"\">");
					sb.append("<input type='hidden' name='merchantId'	value=\""+merchantId+"\">");
					sb.append("</form>");
					sb.append("</body>");
					sb.append("<SCRIPT>");
					sb.append("document.forms[0].submit();"); 
					sb.append("</SCRIPT>");
					sb.append("</html>");
					out.write(sb.toString());

				}else {
					response.sendRedirect("xssErrorPageIYPay.jsp?errorMessage=Sorry! Invalid Expiry date!!");
				}
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.sendRedirect("xssErrorPageIYPay.jsp?errorMessage=System Busy. Please try Later!!");
		}

	}

	public static long daysBetweenUsingJoda(Date d1){ 
		long l = 0;
		try {

			//in milliseconds
			long diff = d1.getTime() - new Date().getTime() ;
			System.out.print(diff + " diff, "+new Date());
			/*long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;*/
			l = diff / (24 * 60 * 60 * 1000);

			System.out.print(l + " days, ");
			/*System.out.print(diffHours + " hours, ");
			System.out.print(diffMinutes + " minutes, ");
			System.out.print(diffSeconds + " seconds.");*/

		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;

	}

}
