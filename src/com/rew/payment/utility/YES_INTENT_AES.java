package com.rew.payment.utility;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.pg.db.DataManager;

public class YES_INTENT_AES {
	private static Logger logger = LoggerFactory.getLogger(YES_INTENT_AES.class);

	public  String YES_RequestIntent(HttpServletRequest req, String Mid, String Sid, String Mcc, String merchant_Vpa)
	{
		/**/
		String upikey=PGUtils.getPropertyValue("YESUPIKey");// "25018eb124398e7e5a0f4380c3286e5e"; // need to configure in Application proprty
	   
	    String amount =(String)req.getAttribute("amount");

		String id =(String)req.getAttribute("id");
		
		logger.info("Ready To Generate Url Yes bank::"+merchant_Vpa+":::"+Mcc+"::::: "+amount+" "+amount+" "+id);

		
		

		 String and="&";
		 String equal="=";
		 StringBuilder request = new StringBuilder();
		
			request.append("pa");
			request.append(equal);
			request.append(Mid);
			request.append(and);
			request.append("pn");
			request.append(equal);
			request.append(Sid);
			request.append(and);
			request.append("mc");
			request.append(equal);
			request.append("5816");//5999//changed 09-02
			request.append(and);
			request.append("tid");
			request.append(equal);
			request.append(id);
			request.append(and);
			request.append("tr");
			request.append(equal);
			request.append(id);
			request.append(and);
			request.append("tn");
			request.append(equal);
			request.append(id);//"1520"
			request.append(and);
			request.append("am");
			request.append(equal);
			request.append(amount);
			request.append(and);
			request.append("cu");
			request.append(equal);
			request.append("INR");
			//
			request.append(and);//added 09-02
			request.append("Mode");
			request.append(equal);
			request.append("04");
			
			/*
			 * request.append(and); request.append("refUrl"); request.append(equal);
			 * request.append("");
			 */
			logger.info("Ready To Genrating signature::::::::::"+request.toString());
			String start="upi://pay?"+request;
			String FinalUrl=start;//YESSignature(start);
			logger.info("Ready To Genrating signature::::::::::"+request.toString());
			
			
			logger.info("Qr Url for yes bank:::::::"+start+"::::::"+FinalUrl);
	    	return FinalUrl;
			/*
			 * upi://pay? ver=01 &mode=05 &orgId=700004 &tid=TESTGST123456qazqwe12345hs29
			 * hnd1qa2 &tr=MerRef123 &tn=GST%20QR &category=02 &url=https://www.test.com&
			 * pa=merchant@npci &pn=Test%20Merchant& mc=5411 &am=100.00 &cu=INR &mid=TS
			 * T5432 &msid=TSTABC123 &mtid=TSTABC1234 &gstBrkUp=CGST:08.45|SGST:08.45
			 * &qrMedium=02 &invoiceNo=BillRef123 &invoiceDate=2019-06- 11T13:21:50+05:30
			 * &invoiceName=Dummy%20Customer &QRexpire=2019-06- 11T13:21:50+05:30
			 * &QRts=2019-06- 12T13:21:50+05:30 &pinCode=400063 &tier=TIER1
			 * &gstIn=GSTNUM1234567890
			 * &sign=MEYCIQDaaEAL06Vsn9aNIarP7dai8/h9cMrVvuYe+uly0rYIMwIhAPRjz6Cj1ZodDLf/N
			 * ZIGYnW4gypE84DNDzRETQTY1IpM
			 */
	}

		


	public static  String YESSignature(String intentURL) {
		String finalBHIMURL = null;
		try {

			KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");

			logger.info("kpg Algorithm " + kpg.getAlgorithm());
			ECGenParameterSpec ecsp = new ECGenParameterSpec("secp256r1");

			kpg.initialize(ecsp); // ecsp
			KeyPair kyp = kpg.genKeyPair();

			PublicKey pubKey = kyp.getPublic();
			PrivateKey privKey = kyp.getPrivate();

			// Signature with Sha-256
			Signature dsa = Signature.getInstance("SHA256withECDSA");
			dsa.initSign(privKey);
			
			logger.info("\nintentURL::::::::::::::::::" + intentURL);
			byte[] strByte = intentURL.getBytes("UTF-8");
			dsa.update(strByte);
			// Sign with private key
			byte[] realSig = dsa.sign();
			// Encode signed URL with base 64

			String encodedSign = java.util.Base64.getEncoder().encodeToString(realSig);
			// Append with Signed URL
			finalBHIMURL = intentURL + "&sign=" + encodedSign;
			logger.info("\nSignature: " + new BigInteger(1, realSig).toString(16));
			logger.info("\nEncoded Signature:" + encodedSign);
			logger.info("\nFinal URL:" + finalBHIMURL);
			// Decode the signed URL with base 64
			String[] bhimReceivedURL = finalBHIMURL.split("&sign=");
			byte[] decodedSign = java.util.Base64.getDecoder().decode(bhimReceivedURL[1]);

			// Initialise verification with public key
			dsa.initVerify(pubKey);
			// Initialise using plain text bytes
			// dsa.update(bhimReceivedURL[0].getBytes("UTF8"));
			// Verify against sign
			boolean verified = dsa.verify(decodedSign);
			logger.info("\nSignature Verify Result:" + verified);
			return finalBHIMURL;

		} catch (Exception e) {
			logger.info("Exception during generate signature for the url ", e);
		}
		return finalBHIMURL;
	}

	
	public static void main(String args[])
	{
		
		
		 String and="&";
		 String equal="=";
		 StringBuilder request = new StringBuilder();
		
			
			request.append("pa");
			request.append(equal);
			request.append("RPVTLTD@rbl");
			request.append(and);
			request.append("pn");
			request.append(equal);
			request.append("rewardoo");
			request.append(and);
			request.append("mc");
			request.append(equal);
			request.append("1506");
			request.append(and);
			request.append("tr");
			request.append(equal);
			request.append("10103301001");
			request.append(and);
			request.append("tn");
			request.append(equal);
			request.append("Pay");//"1520"
			request.append(and);
			request.append("am");
			request.append(equal);
			request.append("10.00");
			request.append(and);
			request.append("cu");
			request.append(equal);
			request.append("INR");		
			request.append(and);
			request.append("mam");
			request.append(equal);
			request.append("10");
			request.append(and);
			request.append("refUrl");
			request.append(equal);
			request.append("");
			logger.info("Ready To Genrating signature::::::::::"+request.toString());
			String start="upi://pay?"+request;
			String FinalUrl=YESSignature(start);
			
			System.out.print(FinalUrl);
			
		  
		  
	}
}
