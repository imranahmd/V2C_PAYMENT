package com.rew.payment.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class CheckSumGenerator
{
    private static Logger logger = LoggerFactory.getLogger(CheckSumGenerator.class);
  String merchantId;
  String txnId;
  
  public CheckSumGenerator() {}
  
  public String getMerchantId() { return merchantId; }
  

  public void setMerchantId(String merchantId)
  {
    this.merchantId = merchantId;
  }
  
  public String getTxnId()
  {
    return txnId;
  }
  
  public void setTxnId(String txnId)
  {
    this.txnId = txnId;
  }
  
  public String getDateTime()
  {
    return dateTime;
  }
  
  public void setDateTime(String dateTime)
  {
    this.dateTime = dateTime;
  }
  
  public String getAmount()
  {
    return amount;
  }
  
  public void setAmount(String amount)
  {
    this.amount = amount;
  }
  
  public String getApiKey()
  {
    return apiKey;
  }
  
  public void setApiKey(String apiKey)
  {
    this.apiKey = apiKey;
  }
  
  public String getCustMobile()
  {
    return custMobile;
  }
  
  public void setCustMobile(String custMobile)
  {
    this.custMobile = custMobile;
  }
  
  public String getCustMail()
  {
    return custMail;
  }
  
  public void setCustMail(String custMail)
  {
    this.custMail = custMail;
  }
  
  public String getChannelId()
  {
    return channelId;
  }
  
  public void setChannelId(String channelId)
  {
    this.channelId = channelId;
  }
  
  public String getTxnType()
  {
    return txnType;
  }
  
  String dateTime;
  String amount;
  public void setTxnType(String txnType) { this.txnType = txnType; }
  
  String apiKey;
  String custMobile;
  String custMail;
  String channelId;
  String txnType;
  public String getChecksum(String URL) { String checksum = null;
    

    try
    {
      String data = "merchantId=" + getMerchantId() + "&txnId=" + getTxnId() + "&dateTime=" + getDateTime() + "&" + 
        "amount=" + getAmount() + "&apiKey=" + getApiKey() + "&custMobile=" + getCustMobile() + "&custMail=" + getCustMail() + "&channelId=" + getChannelId() + "&txnType=" + getTxnType();
      String line = null;
      logger.info("finalURL >>>>>> >>>> " + URL);
      URL url = new URL(URL);
      HttpURLConnection con = (HttpURLConnection)url.openConnection();
      
      con.setRequestMethod("POST");
      


      con.setDoOutput(true);
      con.connect();
      
      OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
      logger.info("Data length :: " + data.getBytes().length);
      wr.write(data);
      wr.flush();
      StringBuffer response = new StringBuffer();
      if (con.getResponseCode() == 200) {
        logger.info("HTTP OK....");
        BufferedReader br = new BufferedReader(
          new InputStreamReader(con.getInputStream()));
        
        while ((line = br.readLine()) != null) {
          logger.info(line);
          response.append(line);
        }
        br.close();
      }
      
      con.disconnect();
      response.toString();
      checksum = response.toString();
    } catch (Exception e) {
      logger.error(e.getMessage(),e);
    }
    
    return checksum;
  }
}
