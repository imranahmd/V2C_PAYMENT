package com.rew.upi.hdfc;

public class HexUtil
{
  public HexUtil() {}
  


  public static byte[] HexfromString(String s)
  {
    int i = s.length();
    byte[] abyte0 = new byte[(i + 1) / 2];
    int j = 0;
    int k = 0;
    if (i % 2 == 1)
      abyte0[(k++)] = ((byte)HexfromDigit(s.charAt(j++)));
    while (j < i)
      abyte0[(k++)] = ((byte)(HexfromDigit(s.charAt(j++)) << 4 | HexfromDigit(s.charAt(j++))));
    return abyte0;
  }
  


  public static int HexfromDigit(char c)
  {
    if ((c >= '0') && (c <= '9'))
      return c - '0';
    if ((c >= 'A') && (c <= 'F'))
      return c - 'A' + 10;
    if ((c >= 'a') && (c <= 'f')) {
      return c - 'a' + 10;
    }
    throw new IllegalArgumentException("invalid hex digit: " + c);
  }
  





  public static String asHex(byte[] buf)
  {
    StringBuffer strbuf = new StringBuffer(buf.length * 2);
    for (int i = 0; i < buf.length; i++) {
      if ((buf[i] & 0xFF) < 16)
        strbuf.append("0");
      strbuf.append(Long.toString(buf[i] & 0xFF, 16));
    }
    
    return strbuf.toString();
  }
  







  public static String HextoString(byte[] abyte0, int i, int j)
  {
    char[] ac = new char[j * 2];
    int k = 0;
    for (int l = i; l < i + j; l++) {
      byte byte0 = abyte0[l];
      ac[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
      ac[(k++)] = hexDigits[(byte0 & 0xF)];
    }
    return new String(ac);
  }
  





  public static String HextoString(byte[] abyte0)
  {
    return HextoString(abyte0, 0, abyte0.length);
  }
  
  private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
}
