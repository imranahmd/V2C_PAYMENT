package com.rew.otp.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.payment.utility.IndexVASDTO;
import com.rew.pg.dto.MerchantDTO;

public class SMS {
  private static Logger log = LoggerFactory.getLogger(SMS.class);

	public void sendSMS(String mobileNo, String otp, String merchantName) throws Exception {
		// TODO Auto-generated method stub
		String sURL = "http://apps.xyxx.com/sendsms/sendsms.php?";
	
		log.info("merchant name:::"+merchantName);
		URL obj = new URL(sURL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		String smsText = otp +" is the OTP for accessing saved cards on "+merchantName+". Valid for 2 mins. Do not share this with anyone. Team PayFI";
		
		String urlParameters = "username=ISVpay&password=123456&type=TEXT&sender=vpay"
				+ "&mobile="+mobileNo+"&message=" + smsText+"&peId=1001845912764659704&tempId=1007271087620489850";
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		int responseCode = con.getResponseCode();
		log.info("Sending 'GET' request to URL : " + sURL);
		//log.info("Post parameters : " + urlParameters);
		log.info("Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());

	}

	// where is ur invoce getting generated?

	

}
