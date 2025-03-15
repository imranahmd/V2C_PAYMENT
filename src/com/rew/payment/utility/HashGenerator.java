package com.rew.payment.utility;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class HashGenerator
  extends HttpServlet
{
    private static Logger logger = LoggerFactory.getLogger(HashGenerator.class);
  
  private static final long serialVersionUID = 1L;
  

  public HashGenerator() {}
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    response.getWriter().append("Get Method not supported");
  }
  
  protected void doOption(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.getWriter().append("Option Method not Allowed.");
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    PrintWriter out = response.getWriter();
    String merchantId = request.getParameter("merchantId");
    String txnId = request.getParameter("txnId");
    String dateTime = request.getParameter("dateTime");
    String amount = request.getParameter("amount");
    String apiKey = request.getParameter("apiKey");
    String custMobile = request.getParameter("custMobile");
    String custMail = request.getParameter("custMail");
    String channelId = request.getParameter("channelId");
    String txnType = request.getParameter("txnType");
    
    String sRaw = merchantId + "|" + txnId + "|" + dateTime + "|" + amount + "|" + apiKey + "|" + custMobile + "|" + custMail + "|" + channelId + "|" + txnType;
    
    logger.info("HashGenerator.java ::: Request Received for CheckSum Generation : " + sRaw);
    
    String sCheckSum = new AesBase64Wrapper().encryptAndEncode(sRaw, apiKey);
    
    out.println(generateResponse(sCheckSum));
    out.flush();
    out.close();
  }
  
  private String generateResponse(String sCheckSum) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("checksum", sCheckSum);
    return jsonObject.toString();
  }
}
