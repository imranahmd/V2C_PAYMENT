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



public class ResponseVerification
  extends HttpServlet
{
    private static Logger logger = LoggerFactory.getLogger(ResponseVerification.class);
  

  private static final long serialVersionUID = 1L;
  


  public ResponseVerification() {}
  

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    response.getWriter().append("Get Method not supported");
  }
  
  protected void doOption(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.getWriter().append("Option Method not Allowed.");
  }
  

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
  {
    response.setHeader("Cache-control", "no-cache, no-store");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Expires", "-1");
    String decRespXML = null;
    PrintWriter out = response.getWriter();;
    
    JSONObject respJson = new JSONObject();
    
    try
    {
      String encResp = request.getParameter("respData");
      
     logger.info("ResponseVerification.java ::: Enc Resp Data : " + encResp);
      
      if ((encResp != null) && (!encResp.isEmpty()))
      {
        decRespXML = new AesBase64Wrapper().decodeAndDecrypt(encResp);
        
        if (decRespXML != null)
        {
          respJson.put("txn_id", PGUtils.GetTextBetweenTags(decRespXML, "<txn_id>", "</txn_id>"));
          respJson.put("merchant_id", PGUtils.GetTextBetweenTags(decRespXML, "<merchant_id>", "</merchant_id>"));
          respJson.put("pg_ref_id", PGUtils.GetTextBetweenTags(decRespXML, "<pg_ref_id>", "</pg_ref_id>"));
          respJson.put("txn_date_time", PGUtils.GetTextBetweenTags(decRespXML, "<txn_date_time>", "</txn_date_time>"));
          respJson.put("trans_status", PGUtils.GetTextBetweenTags(decRespXML, "<trans_status>", "</trans_status>"));
          respJson.put("txn_amount", PGUtils.GetTextBetweenTags(decRespXML, "<txn_amount>", "</txn_amount>"));
          respJson.put("resp_code", PGUtils.GetTextBetweenTags(decRespXML, "<resp_code>", "</resp_code>"));
          respJson.put("resp_message", PGUtils.GetTextBetweenTags(decRespXML, "<resp_message>", "</resp_message>"));
          respJson.put("bank_ref_id", PGUtils.GetTextBetweenTags(decRespXML, "<bank_ref_id>", "</bank_ref_id>"));
          respJson.put("cust_email_id", PGUtils.GetTextBetweenTags(decRespXML, "<cust_email_id>", "</cust_email_id>"));
          respJson.put("cust_mobile_no", PGUtils.GetTextBetweenTags(decRespXML, "<cust_mobile_no>", "</cust_mobile_no>"));
          respJson.put("payment_mode", PGUtils.GetTextBetweenTags(decRespXML, "<payment_mode>", "</payment_mode>"));
          respJson.put("resp_date_time", PGUtils.GetTextBetweenTags(decRespXML, "<resp_date_time>", "</resp_date_time>"));
        }
      }
      else
      {
        respJson.put("txn_id", "NA");
        respJson.put("merchant_id", "NA");
        respJson.put("pg_ref_id", "NA");
        respJson.put("txn_date_time", "NA");
        respJson.put("trans_status", "NA");
        respJson.put("txn_amount", "NA");
        respJson.put("resp_code", "NA");
        respJson.put("resp_message", "Request Parameter 'respData' is MISSING or NULL or BLANK.");
        respJson.put("bank_ref_id", "NA");
        respJson.put("email", "NA");
        respJson.put("mobile_no", "NA");
        respJson.put("paymode", "NA");
        respJson.put("resp_date_time", "NA");
      }
    }
    catch (Exception e)
    {
      decRespXML = null;
      
     logger.error("ResponseVerification.java ::: Error occurred while Decryption : ", e);
      
      respJson.put("txn_id", "NA");
      respJson.put("merchant_id", "NA");
      respJson.put("pg_ref_id", "NA");
      respJson.put("txn_date_time", "NA");
      respJson.put("trans_status", "NA");
      respJson.put("txn_amount", "NA");
      respJson.put("resp_code", "NA");
      respJson.put("resp_message", "Error Occurred in Response Decryption");
      respJson.put("bank_ref_id", "NA");
      respJson.put("email", "NA");
      respJson.put("mobile_no", "NA");
      respJson.put("paymode", "NA");
      respJson.put("resp_date_time", "NA");
      
     logger.error(e.getMessage(),e);
    }
    
   logger.info("ResponseVerification.java ::: Resp JSON : " + respJson);

    out.write(respJson.toString());
    out.flush();
    out.close();
  }
}
