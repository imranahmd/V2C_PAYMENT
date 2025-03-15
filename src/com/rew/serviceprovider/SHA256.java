package com.rew.serviceprovider;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.util.Base64URL;

import com.nimbusds.jwt.SignedJWT;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.io.IOException;


public class SHA256
{
  public SHA256() {}
  
 /* public static void main(String[] args)
  {
    String abc = "payeeid=000000008788|itc=AXISBANK|prn=100505835821668111|date=2018-06-13|amt=1";
    
    System.out.println("CHECKSUM:   " + getSha256(abc));
  }*/
 

  public static String getSha256(String value)
  {
    try
    {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(value.getBytes());
      return bytesToHex(md.digest());
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
  
  private static String bytesToHex(byte[] bytes) { StringBuffer result = new StringBuffer();
    byte[] arrayOfByte = bytes;int j = bytes.length; for (int i = 0; i < j; i++) { byte b = arrayOfByte[i];result.append(Integer.toString((b & 0xFF) + 256, 16).substring(1)); }
    return result.toString();
  }
  
  
  public static String payzAppEnc(String msghash) throws UnsupportedEncodingException{
		String str = null;
		try {
			MessageDigest messageDigest256=MessageDigest.getInstance("SHA-256");
			str = new String(Base64.getEncoder().encode(messageDigest256.digest(msghash.getBytes("UTF-8"))));
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		}
		
		return str;
	}

  public static String getSignedContent(String content) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		Payload contentPayload = new Payload(content);
		
		String PRIVATE_KEY_PKCS8 = readPrivateKeyFromFile("C:\\Rew\\privatekey.pem"); // Private Key
		//String PRIVATE_KEY_PKCS8 = readPrivateKeyFromFile()
;		PrivateKey privateKey = getEncodedPrivateKey(PRIVATE_KEY_PKCS8);
		try { 
			RSASSASigner rsa = new RSASSASigner((RSAPrivateKey) privateKey);
			JWSAlgorithm alg = JWSAlgorithm.RS256;
			JWSHeader header = new JWSHeader.Builder(alg).keyID("e78c28ae-c06a-d265-fe82-107a44e73847") // KeyId
					.build();
			
			JWSObject jws = new JWSObject(header, contentPayload);
			jws.sign(rsa);
			System.out.println(jws);
			 String finalJson = String.format("{\"signature\": \"%s\", \"payload\": \"%s\", \"protected\": \"%s\"}",
					 jws.getSignature(), jws.getPayload().toBase64URL(), jws.getHeader().toBase64URL());

			return finalJson;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

public static String readPrivateKeyFromFile(String filePath) throws IOException {
    // Create a Path object from the file path
    Path privateKeyPath = Paths.get(filePath);

    // Read the contents of the file into a byte array
    byte[] privateKeyBytes = Files.readAllBytes(privateKeyPath);

    // Convert the byte array to a string using UTF-8 encoding
    String privateKey = new String(privateKeyBytes, StandardCharsets.UTF_8);

    return privateKey;
}
private static PrivateKey getEncodedPrivateKey(String pspPrivateKey)
		  throws InvalidKeySpecException, NoSuchAlgorithmException {
				java.security.Security.addProvider(
				         new org.bouncycastle.jce.provider.BouncyCastleProvider()
				);
		  pspPrivateKey = pspPrivateKey.replace("-----BEGIN RSA PRIVATE KEY-----", "");
		  pspPrivateKey = pspPrivateKey.replace("-----END RSA PRIVATE KEY-----", "");
		  pspPrivateKey = pspPrivateKey.replaceAll("\\s+", ""); 
		  System.out.println("Hi "+pspPrivateKey);
		  byte[] pkcs8EncodedBytes = Base64.getDecoder().decode(pspPrivateKey);
		  PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
		  KeyFactory kf = KeyFactory.getInstance("RSA");
		  return kf.generatePrivate(keySpec);
			}
}
