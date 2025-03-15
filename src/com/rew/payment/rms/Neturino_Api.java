package com.rew.payment.rms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Neturino_Api {
	static Logger log = LoggerFactory.getLogger(Neturino_Api.class.getName());

	public  String httpServerCall(String sURL) {
	    String line = null;
	    BufferedReader br = null;
	    StringBuffer respString = null;

	    log.info("S2SCall.java ::: httpServerCall() :: Posting URL : " + sURL);

	    try {
	      URL obj = new URL(sURL);
	      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	      con.setRequestMethod("GET");
	      con.addRequestProperty("user-id", "Pay");
	      con.addRequestProperty("api-key", "kEb8ZgK17oK3SBSB9w4KS5i075AnNhLY59lRZvbvVSpPWLlk");
	      con.addRequestProperty("Accept", "application/json");

	      con.setDoOutput(true);
	      con.connect();

	      OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

	      wr.flush();

	      respString = new StringBuffer();
	      log.info("S2SCall.java ::: httpServerCall() "+con.getResponseCode());
	      if (con.getResponseCode() == 200) {
	    	  log.info("S2SCall.java ::: httpServerCall() :: HTTP OK");
	        br = new BufferedReader(new InputStreamReader(con.getInputStream()));

	        while ((line = br.readLine()) != null) {
	        	log.info("S2SCall.java ::: httpServerCall() :: Response : " + line);
	          respString.append(line);
	        }
	        br.close();
	        return respString.toString();
	      }
	    } catch (Exception e) {
	    	log.error("S2SCall.java ::: secureServerCall() :: Error Occurred while Processing Request : ",
	          e);
	    }

	    return null;
	  }
}
