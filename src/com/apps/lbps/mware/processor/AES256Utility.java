package com.apps.lbps.mware.processor;

public class AES256Utility {/*
	
	static Logger log = Logger.getLogger(AES256Utility.class.getName());
	
	 	private String ips;
	    private Key keySpec;
	    //public static String sKey="jpuT6032jpuT6032";
	  
	    public AES256Utility() {
	        try {
	        	byte[] keyBytes = new byte[32];
	            byte[] b = PGUtils.getPropertyValue("irctcKey").getBytes("UTF-8");//Utils.getPropertyValue("irctcKey").getBytes("UTF-8");
	            System.arraycopy(b, 0, keyBytes, 0, keyBytes.length);
	            log.info("keyBytes >>> "+new String(keyBytes));
	            keySpec = new SecretKeySpec(keyBytes, "AES");
	        	this.ips = PGUtils.getPropertyValue("irctcKey").substring(0, 16);//Utils.getPropertyValue("irctcKey").substring(0, 16);
	            log.info("ips >>> "+new String(ips));
	            
	        } catch (Exception e) {
	        }
	    }

	   

	    public String encrypt(String str) {
	        Cipher cipher;
	        try {
	            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	            cipher.init(Cipher.ENCRYPT_MODE, keySpec,
	                    new IvParameterSpec(ips.getBytes("UTF-8")));

	            byte[] encrypted = cipher.doFinal(str.getBytes("UTF-8"));
	            String Str = PGUtils.byteToHex(encrypted);//new String(Base64.encodeBase64(encrypted));
	            return Str;
	        } catch (Exception e) {
	        	
	        	e.printStackTrace();
	        }
	        return null;
	    }

	    public String decrypt(String str) {
	    	log.info("str ::: "+str);
	        Cipher cipher;
	        try {
	            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	            cipher.init(Cipher.DECRYPT_MODE, keySpec,
	                    new IvParameterSpec(ips.getBytes("UTF-8")));
	            
	            byte[] byteStr = PGUtils.hex2ByteArray(str);//Base64.decodeBase64(str.getBytes());
	            String Str = new String(cipher.doFinal(byteStr), "UTF-8");
	            
	            log.info("str after decryption::: "+Str);
	            return Str;
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        return null;
	    }
	
	    public MerchantDTO getMerchant(final String merchantId) {

	    	log.info("DataManager.java ::: getMerchant() :: merchantId="+merchantId);
	    	MerchantDTO MM = null;
	    	Connection conn = null;
	    	PreparedStatement stmt = null;
	    	ResultSet rs = null;
	    	try {
	    		conn = DBConnectionHandler.getConnection();
	    		final String sql = "select transaction_key,merchant_status,merchant_name,mer_return_url,mer_website_url,integration_type,max_token_size,is_vas,is_push_url,push_url,isretryAllowed from tbl_mstmerchant where MerchantId = ?";
	    		if (conn != null) {
	    			stmt = conn.prepareStatement(sql);
	    			stmt.setString(1, merchantId);
	    			rs = stmt.executeQuery();
	    			if (rs.next()) {
	    				MM = new MerchantDTO();
	    				MM.setMerchantId(merchantId);
	    				MM.setTransactionKey(rs.getString("transaction_key"));
	    				MM.setStatus(rs.getString("merchant_status"));
	    				MM.setName(rs.getString("merchant_name"));
	    				MM.setMerchReturnURL(rs.getString("mer_return_url"));                   
	    				MM.setMerchWebsiteURL(rs.getString("mer_website_url"));
	    				MM.setIntegrationType(rs.getString("integration_type"));
	    				MM.setMaxTokenSize(Double.valueOf(rs.getString("max_token_size")));
	    				MM.setIsVAS(rs.getString("is_vas"));
	    				MM.setIsPushEnabled(rs.getString("is_push_url"));
	    				MM.setPushUrl(rs.getString("push_url"));
	    				MM.setIsRetry(rs.getString("isretryAllowed"));
	    			}
	    		}
	    	}
	    	catch (Exception e) {
	    		System.out.println("DataManager.java ::: getMerchant() :: Error Occurred : "+e);

	    		return MM;
	    	}
	    	finally {
	    		try {
	    			if (rs != null) {
	    				rs.close();
	    				rs = null;
	    			}
	    			if (stmt != null) {
	    				stmt.close();
	    				stmt = null;
	    			}
	    			if (conn != null) {
	    				conn.close();
	    				conn = null;
	    			}
	    		}
	    		catch (Exception e) {
	    			System.out.println("DataManager.java ::: getMerchant() :: Error Occurred while closing Connection : " +e.getMessage());
	    		}
	    	}

	    	return MM;
	    }
	    
	    
	    public static String getDecData(String sData, String decKey)
		{
			try
			{
				if (decKey.length() == 8) {
					decKey = decKey + decKey;
				}
				String ALGO = "AES";
				byte[] keyByte = decKey.getBytes();
				Key key = new SecretKeySpec(keyByte, ALGO);
				Cipher c = Cipher.getInstance(ALGO);
				c.init(2, key);
				log.error("PGUtils.java ::: getDecData() ::  " + sData);
				byte[] decryptedByteValue = Base64.getDecoder().decode(sData.getBytes());
				log.error("PGUtils.java ::: getDecData() ::  " + decryptedByteValue);
				byte[] decValue = c.doFinal(decryptedByteValue);
				return new String(decValue);
			}
			catch (Exception e)
			{

				log.error("PGUtils.java ::: getDecData() :: Error occurred while Decryption : ", e);
			}
			return null;
		}
	   
*/}
