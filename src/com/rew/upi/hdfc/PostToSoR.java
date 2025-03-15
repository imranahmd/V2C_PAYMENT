package com.rew.upi.hdfc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostToSoR
{
    private static Logger logger = LoggerFactory.getLogger(PostToSoR.class);
  public PostToSoR() {}
  
  public String postData(String finalURL, String data)
    throws Exception
  {
    String line = null;
    logger.debug("finalURL >>>>>> >>>> " + finalURL);
    URL url = new URL(finalURL);
    HttpURLConnection con = (HttpURLConnection)url.openConnection();
    
    con.setRequestMethod("POST");
    

    con.setDoOutput(true);
    con.connect();
    
    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
    logger.debug("Data length :: " + data.getBytes().length);
    wr.write(data);
    wr.flush();
    StringBuffer response = new StringBuffer();
    if (con.getResponseCode() == 200) {
      logger.debug("HTTP OK....");
      BufferedReader br = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
      
      while ((line = br.readLine()) != null) {
        logger.debug(line);
        response.append(line);
      }
      br.close();
    }
    
    con.disconnect();
    return response.toString();
  }
  
	/* public static void main(String[] args) {} */
}
