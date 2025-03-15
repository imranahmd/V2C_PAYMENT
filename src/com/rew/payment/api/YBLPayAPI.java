//package com.rew.payment.api;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.UUID;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.gson.Gson;
//import com.rew.payment.utility.PGUtils;
//import com.rew.payment.utility.YBLPost;
//import com.upi.merchanttoolkit.security.UPISecurity;
//
//@WebServlet("/yblPay")
//public class YBLPayAPI extends HttpServlet{
//
//	private static Logger logger = LoggerFactory.getLogger(YBLPayAPI.class);
//	private static final long serialVersionUID = 1L;
//	private final Gson gsonJson = new Gson();
//	
//	protected void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		response.getWriter().append("Get Method not allowed: ").append(request.getContextPath());
//	}
//
//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//	
//		String uuid = UUID.randomUUID().toString();
//		PrintWriter out = response.getWriter();
//		String respData = null;
//		
//		try {
//			
//			StringBuilder jb = new StringBuilder();
//			String line = null;
//			BufferedReader reader = request.getReader();
//		
//			while ((line = reader.readLine()) != null)
//				jb.append(line);
//
//			logger.info("Pay Request received with details :[{}]:{}",uuid,jb.toString());
//			//https://uatsky.yesbank.in:444/app/uat/upi/mePayServerReqImps
//		
//			YBLPayReqPojo reqPojo = gsonJson.fromJson(jb.toString(), YBLPayReqPojo.class);
//			logger.info("Clear request data for YBL Pay API [{}]:: ",uuid + reqPojo.getRequestData());
//			UPISecurity upisecurity= new UPISecurity();
//			String key = PGUtils.getPropertyValue("YESUPIKey");
//			String payUrl = PGUtils.getPropertyValue("PayUrlYesBank");
//		
//			
//			logger.info("Clear request data for YBL forpojo sData:::  "+reqPojo.getRequestData());
//			String sData = upisecurity.encrypt(reqPojo.getRequestData(), key);
//			logger.info("Clear request data for YBL for sData"+sData);
//
//			JSONObject json = new JSONObject();
//			json.put("requestMsg", sData);
//			json.put("pgMerchantId", reqPojo.getPgMerchantId());
//			
//			logger.info("Clear request data for YBL for Json"+json.toString());
//
//			respData = new YBLPost().postData(payUrl, json.toString(),uuid);
//			logger.info("Pay response for : {} received from yes bank as : {}",uuid,respData);
//			respData = upisecurity.decrypt(respData, key);
//			logger.info("Pay response for :{} received from yes bank decrypted :{}",uuid,respData);
//		}
//		catch(Exception e)
//		{
//			logger.error("Error while processing pay request for :{} as : {}",uuid,e.getMessage(),e);
//			respData = "Error while processing your request - " + e.getMessage();
//		}
//		
//		JSONObject json = new JSONObject();
//		json.put("yblPayResponse", respData);
//		
//		response.setCharacterEncoding("UTF-8");
//		response.setContentType("application/json; charset=UTF-8");
//		out.print(json);
//		out.flush();
//		out.close();
//		
//	}
//}
