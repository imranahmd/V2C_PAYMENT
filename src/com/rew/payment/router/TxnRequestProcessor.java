package com.rew.payment.router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxnRequestProcessor
  extends HttpServlet
{
    private static Logger logger = LoggerFactory.getLogger(TxnRequestProcessor.class);
  

  private static final long serialVersionUID = 1L;
  

  public TxnRequestProcessor() {}
  

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    response.getWriter().append("Get Method not Allowed.");
  }
  
  protected void doOption(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.getWriter().append("Option Method not Allowed.");
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    PrintWriter out = response.getWriter();
    

    String URL = "http://192.168.1.170:8080/payone/payprocessor";
    String checkSumURL = "http://139.59.1.254:8080/payone/checksumgenerator";
    

    String amount = request.getParameter("amount");
    String apiKey = request.getParameter("apiKey");
    String merchantId = request.getParameter("merchantId");
    String txnId = request.getParameter("txnId");
    String custMobile = request.getParameter("custMobile");
    String custMail = request.getParameter("custMail");
    

    String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    String channelId = "0";
    String txnType = request.getParameter("txnType");
    
    JSONObject checksumJSON = null;
    String checksum = null;
    String checksumParams = "merchantId=" + merchantId + "&txnId=" + txnId + "&dateTime=" + dateTime + "&" + 
      "amount=" + amount + "&apiKey=" + apiKey + "&custMobile=" + custMobile + "&custMail=" + custMail + "&channelId=" + channelId + "&txnType=" + txnType;
    String line = null;
    
    String isMultiSettlement = "N";
    String productId = "DEFAULT";
    String udf1 = "NA";
    String udf2 = "NA";
    String udf3 = "NA";
    String instrumentId = "NA";
    String cardType = "NA";
    

    try
    {
      logger.info("TxnRequestProcessor.java ::: Checksum Parameters :\n" + checksumParams);
      URL url = new URL(checkSumURL);
      HttpURLConnection con = (HttpURLConnection)url.openConnection();
      
      con.setRequestMethod("POST");
      con.setDoOutput(true);
      con.connect();
      
      OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
      logger.info("TxnRequestProcessor.java ::: Output Stream Open");
      wr.write(checksumParams);
      wr.flush();
      
      StringBuffer checksumResp = new StringBuffer();
      logger.info("TxnRequestProcessor.java ::: Response Code : " + con.getResponseCode());
      if (con.getResponseCode() == 200)
      {
        logger.info("TxnRequestProcessor.java ::: Response Code " + con.getResponseCode() + " is HTTP OK....");
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        
        while ((line = br.readLine()) != null)
        {
          logger.info("TxnRequestProcessor.java ::: Line : " + line);
          checksumResp.append(line + "\n");
        }
        br.close();
      }
      
      con.disconnect();
      checksumJSON = new JSONObject(checksumResp.toString());
      wr.write(checksumJSON.toString());
      wr.flush();
      wr.close();
      checksum = checksumJSON.getString("checksum");
      logger.info("TxnRequestProcessor.java ::: Checksum Generated : " + checksum);

    }
    catch (Exception e)
    {
      logger.error("TxnRequestProcessor.java ::: Error Occurred : ", e);
    }
    




    Map<String, String> fields = new HashMap();
    
    fields.put("isMultiSettlement", isMultiSettlement);
    fields.put("productId", productId);
    fields.put("udf1", udf1);
    fields.put("udf2", udf2);
    fields.put("udf3", udf3);
    fields.put("dateTime", dateTime);
    fields.put("channelId", channelId);
    fields.put("channelId", instrumentId);
    fields.put("channelId", cardType);
    fields.put("checksum", checksum);
    

    Enumeration<String> e = request.getParameterNames();
    while (e.hasMoreElements())
    {
      String fieldName = (String)e.nextElement();
      String fieldValue = request.getParameter(fieldName);
      logger.info("TxnRequestProcessor.java ::: Request Field Name : " + fieldName + " And Request Field Value : " + fieldValue);
      if ((fieldValue != null) && (fieldValue.length() > 0))
      {
        fields.put(fieldName, fieldValue);
      }
    }
    
    StringBuilder postReq = new StringBuilder();
    
    postReq.append("<HTML>");
    postReq.append("<BODY>");
    postReq.append("<FORM ACTION = '" + URL + "' METHOD='POST'>");
    for (Iterator<String> itr = fields.keySet().iterator(); itr.hasNext();)
    {
      String fieldName = (String)itr.next();
      String fieldvalue = (String)fields.get(fieldName);
      postReq.append("<input type=\"hidden\" name=\"" + fieldName + "\" value=\"" + fieldvalue + "\">");
    }
    
    postReq.append("</FORM>");
    postReq.append("</BODY>");
    
    postReq.append("<SCRIPT>");
    postReq.append("document.forms[0].submit();");
    postReq.append("</SCRIPT>");
    postReq.append("</HTML>");
    

    logger.info("TxnRequestProcessor.java ::: Final Request : " + postReq);
    
    out.println(postReq.toString());
    out.flush();
    out.close();
  }
}
