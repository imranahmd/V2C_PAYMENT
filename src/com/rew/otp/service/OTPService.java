package com.rew.otp.service;

import java.text.ParseException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.payment.utility.AjaxAction;
import com.rew.pg.db.DataManager;

public class OTPService {
	    static Logger log = LoggerFactory.getLogger(OTPService.class);

	    public void saveOtp(String mobile_number, String emailId, String Otp, String txnId) {
	  
	    	DataManager dm=new DataManager();
	        dm.saveOtp(mobile_number, emailId, Otp, txnId);
	       
	    }

		public String validateOtp(String mobileNo, String emailId, String otp) throws ParseException {
			String response= null;
			DataManager dm=new DataManager();
	        String validateOtpResp= dm.validateOtp(mobileNo, emailId, otp);
			
	        JSONObject json = new JSONObject(validateOtpResp.toString());
			
			String otpExist = json.getString("otpExist");
			String otp_exipry_time = json.getString("otp_exipry_time");
		
			 long currentTimeInMillis = System.currentTimeMillis();
			
			 log.info("otp_exipry_time:::"+otp_exipry_time+"currentTimeInMillis:::"+currentTimeInMillis);
			if(otp.equalsIgnoreCase(otpExist)) {
				if(Long.parseLong(otp_exipry_time) < currentTimeInMillis) {
					response= "OTP expired";
				}else {
					response= "OTP verified successfully";
				}
			
				
			}else {
				response= "OTP not verified successfully";
			}
			return response;
		}

	}

