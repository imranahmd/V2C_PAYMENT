package com.rew.upi.hdfc;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class UPISecurity
{
  private javax.crypto.spec.SecretKeySpec skeySpec;
  private Cipher cipher;
  
  public UPISecurity()
  {
    skeySpec = null;
    cipher = null;
  }
  
  public void initEncrypt(String key) throws Exception
  {
    try {
      skeySpec = new javax.crypto.spec.SecretKeySpec(HexUtil.HexfromString(key), "AES");
      cipher = Cipher.getInstance("AES");
      cipher.init(1, skeySpec);
    } catch (java.security.NoSuchAlgorithmException nsae) {
      throw new Exception("Invalid Java Version");
    } catch (NoSuchPaddingException nse) {
      throw new Exception("Invalid Key");
    }
  }
  
  public void initDecrypt(String key) throws Exception
  {
    try {
      skeySpec = new javax.crypto.spec.SecretKeySpec(HexUtil.HexfromString(key), "AES");
      cipher = Cipher.getInstance("AES");
      cipher.init(2, skeySpec);
    }
    catch (java.security.NoSuchAlgorithmException nsae) {
      throw new Exception("Invalid Java Version");
    } catch (NoSuchPaddingException nse) {
      throw new Exception("Invalid Key");
    }
  }
  
  public String encrypt(String message, String enc_key) throws Exception
  {
    try
    {
      initEncrypt(enc_key);
      
      byte[] encstr = cipher.doFinal(message.getBytes());
      return HexUtil.HextoString(encstr);

    }
    catch (BadPaddingException nse)
    {
      throw new Exception("Invalid input String");
    }
  }
  

  public String decrypt(String message, String dec_key)
    throws Exception
  {
    try
    {
      initDecrypt(dec_key);
      
      byte[] encstr = cipher.doFinal(HexUtil.HexfromString(message));
      return new String(encstr);

    }
    catch (BadPaddingException nse)
    {
      throw new Exception("Invalid input String");
    }
  }
}
