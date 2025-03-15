package com.rew.payment.utility;

import com.rew.pg.db.DataManager;
import com.rew.pg.dto.BankMaster;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BankList
  extends HttpServlet
{
    private static Logger logger = LoggerFactory.getLogger(BankList.class);
  
  private static final long serialVersionUID = 1L;
  

  public BankList() {}
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doPost(request, response);
  }
  
  protected void doOption(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.getWriter().append("Option Method not Allowed.");
  }
  


  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    PrintWriter out = response.getWriter();
    
    DataManager dm=new DataManager();

    List lstBank = dm.getBankList();
    if (lstBank.size() > 0) {
      out.println(sendSuccessResponse(lstBank));
    } else {
      out.println(sendErrorResponse(-1, "No bank found"));
    }
  }
  

  private String sendSuccessResponse(List bankList)
  {
    JSONObject json = new JSONObject();
    for (Iterator iterator = bankList.iterator(); iterator.hasNext();) {
      BankMaster object = (BankMaster)iterator.next();
      json.put("bankId", object.getBankId());
      json.put("bankName", object.getBankName());
    }
    JSONObject json1 = new JSONObject();
    json1.put("data", json.toString());
    logger.info(json1.toString());
    return json1.toString();
  }
  


  private String sendErrorResponse(int resp, String reason)
  {
    JSONObject json = new JSONObject();
    json.put("error_type", resp);
    json.put("error_description", reason);
    JSONObject json1 = new JSONObject();
    json1.put("errors", json.toString());
    logger.info(json1.toString());
    return json1.toString();
  }
}
