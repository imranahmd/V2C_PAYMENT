package com.rew.payment.utility;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class PSPCLEncryptor
{
    private static Logger logger = LoggerFactory.getLogger(PSPCLEncryptor.class);
  public PSPCLEncryptor() {}
  
  public static String encrypt(String key, String initVector, String value)
  {
    try
    {
    
      IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
      SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
      
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(1, skeySpec, iv);
      
      byte[] encrypted = cipher.doFinal(value.getBytes());
      System.out.println("encrypted string: " + 
        java.util.Base64.getEncoder().encodeToString(encrypted));
      
      return java.util.Base64.getEncoder().encodeToString(encrypted);

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    
    return null;
  }
  
  public static String decrypt(String key, String initVector, String encrypted)
  {
    try
    {
   
      IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
      SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
      
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(2, skeySpec, iv);
      
      byte[] original = cipher.doFinal(java.util.Base64.getDecoder().decode(encrypted));
      return new String(original);

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    
    return null;
  }
  
  public static void main(String[] args)
  {
    //String key = "ZEGs0367";
    //String initVector = "ZEGs0367";
    
    String key = "VB7gp9ol0dv2ob4dV7XX3aW5cy0vP9YS";
    //String initVector = "ZEGs0367ZEGs0367";
    String initVector = "VB7gp9ol0dv2ob4d";
    
    
    String enc = "ym3kTmXpZMS2HGf/mQS1Y4hUCBIM0nezoPh3m1SwZVjynGqJj6LjyxQlL7bwt+hC/wH3F1WMdBCGG5gb25wV3Uwi32O+UYnW5/1wckMS77jk9t1fNw6kHniO8344qAg8OCt6xaE9ONEjIsbku+7SHg4IF4rQwNc+Bgbqvit+P1lsBttjpjPUXQvByHKArFJ1NCC+m8EQ7SCu5UEjURsq2h3Rm0ccjtE4NvLpr8nKpt9nxuAZWQ/VJ8r6Zpk7bslIMk3cCIWVpruV5fDfbfRFeXo2b0hFwmePmajqFEXrLq1ps3Yy43aeALyIVyRHzWvqY5lYY6NE+qYp06gffhFePQxaYsZ1bNiHq+m+3f5ZSzS5jOH9Ho1emZiQYzMh7JFVcIMDNvC7eWtmRhs+smpPbmidlY3zMLmj+6aT5yvHb+yjtBNJgvsidkbGQGXDpLuwP/2YWoGrCi34udHQbBqRcasrTaN2iMA8jzciT67DpQWHWwpHRne8kGJsQ+YYnIulAx+hVYlz5gm1SxYtk54sVw0G8p9mr2uzTS+xwADaZrDfFO0ZSVpMuAoHvE264PBiZOGizQIC9Z2DNYYCBL4vfAPAv60kvQJPY/ZRAv4jHgxFDcx4CKzSzXTEgIMU7B1qGcr5Fj4TJ4n/xvlFoHC+2KtRI20MwqnJb3pR4fwb1ZPwpaz1fQc5hJ9ndeal4Ri/3kE289LN8+H9nemHBAJdhQ==";
    
    String decData="{\r\n"
    		+ "  \"AuthID\": \"M00006422\",\r\n"
    		+ "  \"AuthKey\": \"VB7gp9ol0dv2ob4dV7XX3aW5cy0vP9YS\",\r\n"
    		+ "  \"CustRefNum\": \"12121677766700066212\",\r\n"
    		+ "  \"txn_Amount\": \"1.00\",\r\n"
    		+ "  \"PaymentDate\": \"2023-11-27 18:07:02\",\r\n"
    		+ "  \"ContactNo\": \"8779799941\",\r\n"
    		+ "  \"EmailId\": \"arun.androapps@gmail.com\",\r\n"
    		+ "  \"IntegrationType\": \"SEAMLESS\",\r\n"
    		+ "  \"CallbackURL\": \"https://games.androappstech.in/api/callback\",\r\n"
    		+ "  \"adf1\": \"NA\",\r\n"
    		+ "  \"adf2\": \"NA\",\r\n"
    		+ "  \"adf3\": \"NA\",\r\n"
    		+ "  \"MOP\": \"UPI\",\r\n"
    		+ "  \"MOPType\": \"UPI\",\r\n"
    		+ "  \"MOPDetails\": \"I\"\r\n"
    		+ "}";
    //String de = encrypt(key, initVector, enc);
    //String Encdata = "1xRx6FH/N92Zjj9Wx7+l8dhjXo5YI7b8oGlHPcyyOQ3ulbYhSDMO5oNveGALAPP6JOb8dIhMdSTepCq2W8o7Mgk8W3b1S+5zP6pAM/0w9XcJMsBxJw79zahripup50tLlTjDSYw8kA7ML62hhKHDB8WNRaE1uSQvp/KL2DGU0JQ5S0+Dap/Z6h0UkmYoEBH1cHS0XY3eIXSec/I/gbH6PgjnBfZOQxHFu7G1Lk5YyRWBSkqSa/TuX4tDF0pbDK2J4qm9u+jGBhnYTXFVIScvx+XEgJJM0KBnI8MLhKzc7bzEBzmpmqL8U2CJo50imG/p4zW/sgdH+zU1G/UcHBFZPNFEeUgaHBO8s9/eQcdRXeW4q7uob4cxMiK9i8m9FYkNnY9CcVnvBQYl2ywRXgshbrRku2/y5LQIFQq+SUXFaUpeYON1/1zCNtvtjJ+wggMyxbxkocNRcDKNHWY8aocRy8mz96chwu3yxkM+BLOeZBnAaagxnq2ahTeOR9uC1Z3pHJZxNyA1rIaNcBeprEB9j+VrLuvsBop0d0xkkta7+qA7F8/hGD6bzq4SJrGNd2knLVbitRk1tj9m34USd18fEzivPEnUpntdXD2EH8J9yjoiKdTbP32LhcAYAGnj8E8tez2M9QCz8oZtuqq9C0kTD9QDMIOBkHeE99RY/s8PI1AGO5cMWiCthyt9X1B+GJYxJalwPdcrA4wmGCcKVc6duUQIj+UQsSXuEWus8WCKq3Mt4UBbnc54ihB8X7gUVw4CfiBKnwidlgToteIrs29pzEgfKCDzHlOF0snkzFFp9Ec/Ieq5rvq4qlMQmFq8uQls";
  //  System.out.println(encrypt(key, initVector, enc));
   // System.out.println(decrypt(key, initVector, de));
    
//    String de=PGUtils.getEncData(enc, "ZEGs0367ZEGs0367ZEGs0367ZEGs0367");
//    String dec=PGUtils.getDecData(de, "ZEGs0367ZEGs0367ZEGs0367ZEGs0367");
//    System.out.println(de);
//     System.out.println(dec);
//    
    
   // String data = decrypt(key, initVector, enc);
    
 //   System.out.println("\n\nData::::::::::::::::::::"+data);
    
   String encData =  encrypt(key, initVector, decData);
   
   System.out.println("encData:::::::::::::::::::"+encData);
    
  }
  
  
 
}
