//package com.rew.payment.api;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.PrintWriter;
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
//import com.rew.payment.utility.PGUtils;
//import com.rew.payment.utility.YBLPost;
//import com.rew.serviceprovider.RespHandlerYesBankNB;
//import com.upi.merchanttoolkit.security.UPISecurity;
//
//@WebServlet("/YesSubMerchant")
//public class SubMerchantOnbaording extends HttpServlet{
//	private static Logger logger = LoggerFactory.getLogger(RespHandlerYesBankNB.class);
//	private static final long serialVersionUID = 1L;
//
//	protected void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		logger.info("Getting response at doget");
//		this.doPost(request, response);
//	}
//
//	protected void doOption(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		response.getWriter().append("Option Method not Allowed.");
//	}
//
//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//           String upikey=PGUtils.getPropertyValue("YESUPIKey");// "25018eb124398e7e5a0f4380c3286e5e"; // need to configure in Application proprty
//String line=null;
//           StringBuilder jb = new StringBuilder();
//   		BufferedReader reader = request.getReader();
//   		while ((line = reader.readLine()) != null) {
//   					jb.append(line);
//   		}
//   		String sData=null;
//   		String DecryptData=null;
//        PrintWriter ps= response.getWriter();
//
//   		try {
//   		logger.info("Line:::::::::::: changes "+jb);
//   		
//   		JSONObject js = new JSONObject(jb.toString());
//   		
//   		logger.info("Line:::::::::::: changes"+js.toString());
//   		
//        UPISecurity upisecurity= new UPISecurity();
//        
//       String EncryReq= upisecurity.encrypt(jb.toString(), upikey);
//       
//       logger.info("Encrypt request::::::::::::::::::"+EncryReq);
//       JSONObject sj = new JSONObject();
//       sj.put("requestMsg", EncryReq);
//       sj.put("pgMerchantId", js.getString("pgMerchantId"));
//       YBLPost server= new YBLPost();
//
//       logger.info("sj:::::::::: "+sj.toString());
//		
//		  String OnboardingApi=PGUtils.getPropertyValue("SubMerchant");
//		  logger.info("OnboardingApi URL::::::::: "+OnboardingApi); 
//		  sData =server.postData(OnboardingApi, sj.toString(), "123");
//		 
//		  JSONObject encr= new JSONObject(sData);
//		  String encData=encr.getString("resp");
//
//   		
//       logger.info("Encrypt request::::Response Data::::::::::"+sData);
//       
//       DecryptData= upisecurity.decrypt(encData, upikey);
//      logger.info("Encrypt request::::Response:: Data::::::::::"+DecryptData);
//      response.setContentType("application/json");
//      
//   		}catch(Exception e)
//   		{
//   	      logger.info("Error while processing request",e);
//
//   			DecryptData="Error while processing request";
//   			
//   		}
//   		ps.write(DecryptData);
//        ps.flush();
//        ps.close();
//   		
//	}
//	
//
//	
//	
//	
//	
//}
//
