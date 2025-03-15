package com.rew.payment.utility;


import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.pg.db.DBConnectionHandler;




public class AesBase64Wrapper
{
    private static Logger logger = LoggerFactory.getLogger(AesBase64Wrapper.class);
  
  private String IV =null; //"IV_VALUE_16_BYTE";
  private String PASSWORD = null;//"01030204";
  private String SALT = "SALT_VALUE";
  
  public AesBase64Wrapper()
  {
	  try {
			this.PASSWORD=PGUtils.getPropertyValue("PASSWORD");
			this.IV=PGUtils.getPropertyValue("AESIV");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info(e.getMessage());
		} 
    getIVSalt();
  }
  
  public String encryptAndEncode(String raw, String pass)
  {
    try
    {
      PASSWORD = pass;
      Cipher c = getCipher(1);
      byte[] encryptedVal = c.doFinal(getBytes(raw));
      encryptedVal = generateHash(encryptedVal);
      String s = Base64.encode(encryptedVal);
      logger.info("AesBase64Wrapper.java ::: encryptAndEncode() :: CheckSum Generated : " + s);
      return s;
    }
    catch (Throwable t)
    {
      throw new RuntimeException(t);
    }
  }
  
  private byte[] generateHash(byte[] message) throws Exception {
    byte[] hash = null;
    
    try
    {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      hash = digest.digest(message);
    }
    catch (GeneralSecurityException e)
    {
      throw new Exception("SHA-256 Hashing algorithm not available");
    }
    return hash;
  }
  

  public String decodeAndDecrypt(String encrypted)
    throws Exception
  {
    byte[] decodedValue = Base64.decode(getBytes(encrypted));
    Cipher c = getCipher(2);
    byte[] decValue = c.doFinal(decodedValue);
    return new String(decValue);
  }
  


  private byte[] getBytes(String str)
    throws UnsupportedEncodingException
  {
    return str.getBytes("UTF-8");
  }
  
  private Cipher getCipher(int mode) throws Exception {
    Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    byte[] iv = getBytes(IV);
    c.init(mode, generateKey(), new IvParameterSpec(iv));
    return c;
  }
  
  private Key generateKey() throws Exception
  {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    char[] password = PASSWORD.toCharArray();
    byte[] salt = getBytes(SALT);
    
    KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
    SecretKey tmp = factory.generateSecret(spec);
    byte[] encoded = tmp.getEncoded();
    return new SecretKeySpec(encoded, "AES");
  }
  

  public String encryptAndEncodeRespData(String raw)
  {
    try
    {
      Cipher c = getCipher(1);
      byte[] encryptedVal = c.doFinal(getBytes(raw));
      return Base64.encode(encryptedVal);

    }
    catch (Throwable t)
    {
      throw new RuntimeException(t);
    }
  }
  
  private void getIVSalt()
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try
    {
      conn = DBConnectionHandler.getConnection();
      
      String sql = "select * from tbl_keymaster where Key_Name = ?";
      
      if (conn != null)
      {
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, "HashGenerator");
        rs = stmt.executeQuery();
        
        if (rs.next())
        {
          IV = rs.getString("Key_Value");
          SALT = rs.getString("Used_For");
        }
      }
    }
    catch (Exception e)
    {
      logger.error("AesBase64Wrapper.java ::: getIVSalt() :: Error Occurred : ", e);
      


      try
      {
        if (rs != null)
        {
          rs.close();
          rs = null;
        }
        if (stmt != null)
        {
          stmt.close();
          stmt = null;
        }
        if (conn != null)
        {
          conn.close();
          conn = null;
        }
      }
      catch (SQLException localSQLException4)
      {
        logger.error("AesBase64Wrapper.java ::: getIVSalt() :: Error Occurred while closing Connection : ", localSQLException4);
      }
    }
    finally
    {
      try
      {
        if (rs != null)
        {
          rs.close();
          rs = null;
        }
        if (stmt != null)
        {
          stmt.close();
          stmt = null;
        }
        if (conn != null)
        {
          conn.close();
          conn = null;
        }
      }
      catch (SQLException localSQLException4)
      {
        logger.error("AesBase64Wrapper.java ::: getIVSalt() :: Error Occurred while closing Connection : ", localSQLException4);
      }
    }
  }
  
  /*public static void main(String[] args)
  {
    AesBase64Wrapper wrapper = new AesBase64Wrapper();
    
    try
    {
      logger.info(wrapper.decodeAndDecrypt("eoxqaNaQ6Pmkmwgnq4TMg+KON8ob2HfzVPcyaRy2Szz6NA8pqR463gwjEeWNZzi5PU3OZrQV24Bnoif0igna52SoLOx+m4vMprMk24gEJSI5cvhJbFbjzVbLhkzSKwr1t5G5UEcPJ6+nhNT6eIvj1uy3ZKpuC/6t3+SOnnSYgvaEPrOe7plQBaeaxBKF7F+tS8eFMSMJirozRfagSatxJTttSF4HxqHLtR1/D1V8w0hABI2sfQNkod9b4ep+RMQ41GRbPdaPpNrpmXvzGC18I9aMIEx8oqJNhShtZ+ZDyHSf2ie66l7NJvylfq6t2hXhx+kwMxd7doL2ftpSJQsIKt8oB6/nM9uW3SsosjwsU1YCYmYsno9YIEhDbol6yYIosB4Ye2m5OeHd0QkM6GUtGUXru4/G5VsiFSP75nyipGcN/wQCflD82Iib2OZpYG9QZ5sq7lAacnkTkBrlTOj12hjCzA/Nj4P3WBPm4AotJ0OEFhh8DS6WSzugSSuyktSa+GxFH54yKDqwjyYLm8nvesH7yCqgq5TCPpkQeCn3IKCYWqnHBkv4fdI2Aj8Y/gu4ZWiUkAab13P6BCdMuTNycQDD03UUyj261Lx1SleQQNc="));
    }
    catch (Exception e)
    {
      logger.error(e);
    }
  }*/
}
