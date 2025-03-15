package com.rew.payment.utility;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TripleKeyEnc {
    private static Logger logger = LoggerFactory.getLogger(TripleKeyEnc.class);

	private static byte[] keyiv = null;

	static {
		if (keyiv == null) {
			byte[] decodedBytes = Base64.getDecoder().decode(PGUtils.getPropertyValue("Des3KeyIV"));
			keyiv = new String(decodedBytes).substring(0, 8).getBytes();
		}
	}

	public static String encode(String data, String key24) {
		try {
			byte[] encoding = Base64.getEncoder().encode(data.getBytes("UTF-8"));
			byte[] str5 = des3EncodeCBC(key24.getBytes(), keyiv, encoding);
			byte[] encoding1 = Base64.getEncoder().encode(str5);

			return new String(encoding1);
		} catch (UnsupportedEncodingException e) {
			logger.error("TripleKeyEnc.java ::: ", e);
		}
		return null;
	}

	public static String decode(String encdata, String key24) {
		try {
			byte[] decode = Base64.getDecoder().decode(encdata.getBytes("UTF-8"));
			byte[] str6 = des3DecodeCBC(key24.getBytes(), keyiv, decode);
			String data = new String(str6);
			byte[] decode1 = Base64.getDecoder().decode(data.trim().getBytes("UTF-8"));

			return new String(decode1);
		} catch (UnsupportedEncodingException e) {
			logger.error("TripleKeyEnc.java ::: ", e);
		}
		return "error in try block";
	}

	private static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data) {
		try {
			Key deskey = null;
			DESedeKeySpec spec = new DESedeKeySpec(key);
			SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
			deskey = keyfactory.generateSecret(spec);

			Cipher cipher = Cipher.getInstance("desede/ CBC/PKCS5Padding");
			IvParameterSpec ips = new IvParameterSpec(keyiv);
			cipher.init(1, deskey, ips);

			return cipher.doFinal(data);
		} catch (Exception e) {
			logger.error("methods qualified name", e);
		}
		return null;
	}

	private static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data) {
		try {
			Key deskey = null;
			DESedeKeySpec spec = new DESedeKeySpec(key);
			SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
			deskey = keyfactory.generateSecret(spec);

			Cipher cipher = Cipher.getInstance("desede/ CBC/NoPadding");
			IvParameterSpec ips = new IvParameterSpec(keyiv);
			cipher.init(2, deskey, ips);

			return cipher.doFinal(data);
		} catch (Exception e) {
			logger.error("methods qualified name", e);
		}
		return null;
	}

}
