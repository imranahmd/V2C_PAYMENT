package com.rew.serviceprovider;

import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.LoggerFactory;



public class AESToken {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(AESToken.class);
	
	String client_secret = "85a8e61db715f0444267cc8dc2ee6bbebf7e3171c88d02594d52b86edab6895d";
	String requestid=generateRandomNumber();
	String timestamp=currentTimestamp();
	
	
	
	
	
	//public static String encrypt(String key, String initVector, String value)
	public static String encrypt(String key, String initVector, String value)  
	{
		String tokenn=null;
	    try
	    {
	    
	      IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
	      SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
	      
	      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	      cipher.init(1, skeySpec, iv);
	      
	      byte[] encrypted = cipher.doFinal(value.getBytes());
	      System.out.println("encrypted string: " + 
	        java.util.Base64.getEncoder().encodeToString(encrypted));
	      
	      tokenn=java.util.Base64.getEncoder().encodeToString(encrypted);
	      return java.util.Base64.getEncoder().encodeToString(encrypted);

	    }
	    catch (Exception ex)
	    {
	      ex.printStackTrace();
	    }
	    
	    return tokenn;
	  }
	
	
	
	public static String decrypt(String key, String initVector, String encrypted)
	  {
		byte[] original=null;
	    try
	    {
	   
	      IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
	      SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
	      
	      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	      cipher.init(2, skeySpec, iv);
	      
	      //byte[] original = cipher.doFinal(java.util.Base64.getDecoder().decode(encrypted));
	      
	       original = cipher.doFinal(java.util.Base64.getDecoder().decode(encrypted));
	       
	       logger.info("String(original)::::::::::::::::::::::"+new String(original));
	      return new String(original);

	    }
	    catch (Exception ex)
	    {
	      ex.printStackTrace();
	    }
	    logger.info(":::::::::::::::::::");
	    return new String(original);
	  }
	
	
	public static String currentTimestamp()
	{
		 long currentTimeMillis = System.currentTimeMillis();

	        // Convert to seconds (Unix timestamp)
	        long unixTimestamp = currentTimeMillis / 1000;
		
	        String timeStamp = String.valueOf(unixTimestamp);
	        
	return timeStamp;	
	}
	
	
	
	
	public static String generateRandomNumber() {
        Random random = new Random();

        // Generate a random number with a minimum length of 7
        StringBuilder randomNumber = new StringBuilder("1"); // Start with 1 to ensure minimum length

        while (randomNumber.length() < 7) {
            randomNumber.append(random.nextInt(10)); // Append a random digit (0-9)
        }

        return randomNumber.toString();
    }
	
	
//	public static String encodeString(String input) {
//        try {
//            // Use UTF-8 encoding for URL encoding
//            return URLEncoder.encode(input, StandardCharsets.UTF_8.toString());
//        } catch (UnsupportedEncodingException e) {
//            // Handle the exception according to your needs
//            e.printStackTrace();
//            return null;
//        }
//    }
	
	
	public static String encodeString(String input) {
		 byte[] encodedBytes = Base64.getEncoder().encode(input.getBytes());
	        return new String(encodedBytes);
   }
	

	
	public static void main(String[] args) {
		
		String Skey  = "84f8020f8006de247b808f7645478606";
		String iv = "0b88ad32f2ae6a9a";
		
		String enc = "d+ZqOqS9vfs3yfZ0sfN0jUc5+fcU+azn/4GzDeweEwtR2xHQ7vVSCGkbweyY0PK8/77Bj+dVPabZL7b0gm9st8U9bWWA/wPCIEojTvueb+eo6smLzMwZKsTyTlhHL4ZSJoZ5DiobQljY9FNsSC9+q+4XD5Z6p+5/PETtE8o5Hm1QFAZHSzK0XT5ChSI9qqmefCSXArVcy9QlXwAkDrEffkbZctziyh+bl6ZCRcV4jOX7TiKsBrJXzeiu7ztF1CRKOnpOQE9j7IuqGxWiUGKT5bcAGvPWvcg5B7nw7VXVJ3F+WpYcEFoMomPQlCTZNhqfmPQho7bf5FLYGwIQVwg7Gm0SOjDhOJuA8YAa4P313gJCk2l5ui/cCiWDCMajfSrIsboZe1gudfKaq+ro97SN97aTHRIRf6sGDRw6b1FxJRO+kA80lq1lSjebjIvX1VteNAOWCtgOKbM8giU9yq320JB7Amdb43p2RJKiUk7cZMn+ITFbraFG5a13icyGxeQlRvE9vJMPwBAPoBq8xJpHgIQW1ld/FnPD4ogX9UBu+toDcw5gmqqgv6/B87/mQsj09gRn4pm6df/EZaJiomUyfSyGNaed11ibHp6lN8PYIpE0KFvC8fGyUbGj/RjOQTDBeNeamOvpZ6aqmTE+w9eVd6Zh+Ej0OhJayLL4BuwgtkprkuRDvPObJSBXpwIfW1fSKjbczHtLoQf0SFpBSsrT/u0P8N9gMIwtTYSE/aMjnG3uRbOt46VbRufo7aassMfOWPO59f9h/b8Tt9kqVL0Awdv00kg5VtQtnk14fEcmsOFzDcr7EyiYgZ0cRImq5fCDYCISDZ9tVgeZ7Yj42ZXM+wxLr5r3fqWY8Tm1O/NiVwEiP3S+tKK/5DBB4ZzO2lU2mHxQTjg4vo7Zpo+AMOgqeLTC7KpkFDNdd8MUwIOGO+HEvgW0xVt8z9u89jMF8If6EwKXaA8x4HWsv5M9BMSoKCT19pKhS/UY815eEFS2kyOyNtqH6ubf6LEgLNArWt20wp2AWikDGpsUGpV+teP1yGkBW2PLY3l0dr8bhLkSzDest5Ea8n94YpIaWO1JYtltL5IXOgSpcYgHCdLRSYsgrhlYCOTTmtt9P3GH5PIpCJw=";
		
		String data = decrypt(Skey, iv, enc);
		
		System.out.println(":::::::::decrypted data:::::::"+data);
		
		
		
		
	}
	
	
}
