package com.rew.payment.router;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.payment.utility.PGUtils;



/**
 * Servlet implementation class linkBasedPayment
 */
@WebServlet("/linkBasedPayment")
public class LinkBasedPayment extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(LinkBasedPayment.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LinkBasedPayment() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		logger.info("\n\n:::::::::::::::::Enterd in Linked Based Post Method::::::::::::::::::::::::::");
		PrintWriter out = response.getWriter();
		 StringBuilder resp = new StringBuilder();
		 // Read the request body into a StringBuilder
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        JSONObject jsonObject=null;
        
        LocalDateTime now = LocalDateTime.now();

        // Define the date-time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the current date and time
        String currentDateTime = now.format(formatter);
        
        
        logger.info("::::::::::currentDateTime:::::::"+currentDateTime);
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String jsonString = sb.toString();

        // Convert the request body to a JSONObject
        try {
			 jsonObject = new JSONObject(jsonString);
			 logger.info("\n::::::::::::::jsonObject:::"+jsonObject);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        {
//            "merchantId": "M00005353",
//            "transactionKey": "fj0RH5hm2El7FV8yr6ns7UL7qr3Np3km",
//            "amount": "10.00",
//            "mobile": "1234567890",
//            "email": "test@gmail.com"
//        }
//        
//        {
//        	  "AuthID": "M00005353",
//        	  "AuthKey": "fj0RH5hm2El7FV8yr6ns7UL7qr3Np3km",
//        	  "CustRefNum": "1807202Test279101",
//        	  "txn_Amount": "100.00",
//        	  "PaymentDate": "2024-07-18 22:36:27",
//        	  "ContactNo": "9876543210",
//        	  "EmailId": "test@test.com",
//        	  "IntegrationType": "DIRECT",
//        	  "CallbackURL": "https:\/\/pg.payfi.co.in\/pay\/merchantResponse.jsp",
//        	  "adf1": "NA",
//        	  "adf2": "NA",
//        	  "adf3": "NA",
//        	  "MOP": "NA",
//        	  "MOPType": "NA",
//        	  "MOPDetails": "NA",
//        	  
//        	}
        
        
//        {
//            "merchantId": "M00006513",
//            "transactionKey": "AB4jZ7FX1pj9lI8tV7mQ9TU8JG9Ha8UT",
//            "amount": "10.00",
//            "mobile": "9899584441",
//            "email": "vishal@roadcast.in",
//            "refNumber": "20240719014326"
//        }
//        
        
        logger.info("::::::::parmeter receved from UI:::::::::");
        String merchantId=jsonObject.getString("merchantId");
        String transactionKey=jsonObject.getString("transactionKey");
        String IV = transactionKey.substring(0, 16);
        String amount=jsonObject.getString("amount");
        String mobile=jsonObject.getString("mobile");
        String email=jsonObject.getString("email");
        String custRefNum=jsonObject.getString("refNumber");
        logger.info("::::::::::::::merchantId:::"+merchantId);
        logger.info("::::::::::::::transactionKey:::"+transactionKey);
        logger.info("::::::::::::::IV:::"+IV);
        logger.info("::::::::::::::amount:::"+amount);
        logger.info("::::::::::::::mobile:::"+mobile);
        logger.info("::::::::::::::email:::"+email);
        logger.info("::::::::::::::custRefNum:::"+custRefNum);
        logger.info(":::::::::::::::::");logger.info(":::::::::::::::::");
        
        
        JSONObject reqData = new JSONObject();
        reqData.put("AuthID", merchantId);
        reqData.put("AuthKey", transactionKey);
        reqData.put("CustRefNum", custRefNum);
        reqData.put("txn_Amount", amount);
        reqData.put("PaymentDate", currentDateTime);//date
        reqData.put("ContactNo", mobile);
        reqData.put("EmailId", email);
        reqData.put("IntegrationType", "DIRECT");
        //reqData.put("CallbackURL", "https://pg.payfi.asia/pay/merchantResponse.jsp");//callback url
        //reqData.put("CallbackURL", "http://localhost:8081/pay_PayFi_Master/merchantResponse.jsp");//callback url
        reqData.put("CallbackURL", "https://pg.payfi.co.in/pay/merchantResponse.jsp");//callback url
        reqData.put("adf1", "NA");
        reqData.put("adf2", "NA");
        reqData.put("adf3", "NA");
        reqData.put("MOP", "NA");
        reqData.put("MOPType", "NA");
        reqData.put("MOPDetails", "NA");
        
        logger.info(":::::::::::::::reqData::"+reqData);
        
        
        String dataR = reqData.toString();
        
        logger.info(":::::::::::::::dataR::"+dataR);
       //(String key, String initVector, String value)  
       String encryptedData= PGUtils.encrypt(transactionKey,IV,dataR);
       
       logger.info(":::::::::::encryptedData::::::"+encryptedData);
        
        
        
        
        
        // Set the response content type
        response.setContentType("text/html");
        String postingUrl = "https://pg.payfi.co.in/pay/paymentinit";
        
        logger.info(":::::::::::::postingUrl::::"+postingUrl);
       // String AuthID="M00005353";
        String encData=encryptedData;//="30v5goDTP+xRHwFRCsZgpdGhRvFyJSy5SbmgY0lMagXi9fwoiOJa7uUqc5d0VOLgMHjoDdN2L3QuiJE4FYAWPdkxx4Jnv0NSP7dK6tUkJDOMaRtlCZdPlhMrkPfDJPXkwZZpTaqX4RaUB24vtA5VbqMOqG4KYXLN+9fCQTJS+l9hmzSuVGY7P/dEiAyAo+7e/vVzX82XbGZha9w8tDLJHOlGJdZ5u4053sCGS+RCgpEbxu71wBmznl0VbDwWD97l1XekdHyEuRk/cjwyB1CrYHoSGSUF7rmTJlVz0SXImx1ncHMPSjvEHR/cQcDR/HtSy2Qiz3K3HbU3n9TL1NuYBUjvMC3haAG0f+RsYpK4LmumegIS+ZhonJEihKvDpRVeaM6CGxN5hSzbxqE+slTaYU9JSf/eYRDTor0PDG6S4ghQmHePzDbMztH8nv7pkMBrzl550bGdwrVlT+VMTllUNHyq0nu866LBxMjufFzXg4GrBIf5PLcVOD11ep3hbkmVhxilX4bILTlknO0/3SXtNZnsmfKbMV+X2c5HWHyGf3I42IILqhfZZSAXYh7No8DQnhROiNM/3WMM1994IbOwV0jioafhFcG5Isax2Xbf6sBaan05T7uwAVBP3qPuM7qj";
        logger.info("logger::::::::::::::;enc data::::::::::"+encData);
        
     // Set response type and write response
//        response.setContentType("text/html");
//        response.getWriter().println("<html><body>");
//        response.getWriter().println("<h2>Form Submitted Successfully</h2>");
//        response.getWriter().println("<p>AuthID: " + authID + "</p>");
//        response.getWriter().println("<p>encData: " + encData + "</p>");
//        response.getWriter().println("</body></html>");
//        
//        

        resp.append("<HTML>");
        resp.append("<BODY>");
        resp.append("<FORM ACTION = \"" + postingUrl + "\" METHOD='POST'>");
        resp.append("<input type=\"hidden\" name=\"AuthID\" value=\"" + merchantId + "\">");
        resp.append("<input type=\"hidden\" name=\"encData\" value=\"" + encData + "\">");
        resp.append("</FORM>");
        resp.append("</BODY>");
        resp.append("<SCRIPT>");
        resp.append("document.forms[0].submit();");
        resp.append("</SCRIPT>");
        resp.append("</HTML>");
	
        
       
    //    PrintWriter out = response.getWriter();

        // Generate the HTML form and JavaScript for auto-submission
//        out.println("<HTML>");
//        out.println("<BODY>");
//        out.println("<FORM ACTION=\"" + postingUrl + "\" METHOD='POST'>");
//        out.println("<input type=\"hidden\" name=\"AuthID\" value=\"" + AuthID + "\">");
//        out.println("<input type=\"hidden\" name=\"encData\" value=\"" + encData + "\">");
//        out.println("</FORM>");
//        out.println("</BODY>");
//        out.println("<SCRIPT>");
//        out.println("document.forms[0].submit();");
//        out.println("</SCRIPT>");
//        out.println("</HTML>");
        
        out.println(resp.toString());
		out.flush();
		out.close();
        
	}
	
	
	

}
