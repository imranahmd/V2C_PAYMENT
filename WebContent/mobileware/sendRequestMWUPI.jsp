<%@ include file="/include.jsp"%>
<%@ page contentType="text/html;charset=ISO-8859-1" language="java" %>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@ page import="java.util.List,
         java.util.ArrayList,
         java.util.Collections,
         java.util.Comparator,
         java.util.Iterator,
         java.util.Enumeration,
         java.util.Date,
         java.security.MessageDigest,
         java.util.Map,
         java.net.URLEncoder,
         java.util.HashMap,
         java.security.*,
         java.security.spec.InvalidKeySpecException,
         javax.crypto.*,
         javax.crypto.spec.SecretKeySpec,
         org.apache.commons.codec.binary.Base64	"%>

<%!  
Logger log = LoggerFactory.getLogger("JSPS.sendRequestMWUPI.jsp");

//This is Secure Secret for encoding the SHA-256 hash
    	//String SECURE_SECRET="E59CD2BF6F4D86B5FB3897A680E0DD3E";//***Add Secure Secret here****

	//PG URL
	String URL = "";//***Add PG URL here****
  
	private static final String ALGO = "AES";

 	//This is the Key used for Encryption
 	//String encKey= "5EC4A697141C8CE45509EF485EE7D4B1";//***Add Encryption Key here****

    	String hashKeys = new String();
    	String hashValues = new String();
   
   	String hashAllFields(Map fields) {
        
        hashKeys = "";
        hashValues = "";
        
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
		// create a buffer for the SHA-256 input and add the secure secret first
        StringBuffer buf = new StringBuffer();
		
        //buf.append(SECURE_SECRET);

        // iterate through the list and add the remaining field values
        Iterator itr = fieldNames.iterator();
        
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            hashKeys += fieldName + ", ";
	    log.info(fieldName+"===="+fieldValue);
            if ((fieldValue != null) && (fieldValue.length() > 0)) { 
                buf.append(fieldValue); 
            }
        }
	log.info("String before Hashing===="+buf.toString());
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(buf.toString().getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
log.info("String after Hashing===="+hexString.toString());
            return hexString.toString();
        } catch(Exception ex){
           throw new RuntimeException(ex);
        }
    } // end hashAllFields()
    


/* public String encrypt(String Data,String keySet) throws Exception {
    	byte[] keyByte = keySet.getBytes();
		Key key = generateKey(keyByte);
	        Cipher c = Cipher.getInstance(ALGO);
	        c.init(Cipher.ENCRYPT_MODE, key); //2
	        byte[] encVal = c.doFinal(Data.getBytes()); //1
	        byte[] encryptedByteValue = new Base64().encode(encVal); //3
	        String encryptedValue = new String(encryptedByteValue); //4
	        return encryptedValue;
    }

    private static Key generateKey(byte[] keyByte) throws Exception {
        Key key = new SecretKeySpec (keyByte, ALGO);
        return key;
}
 */%><%    
    Map fields = new HashMap();
    Enumeration e = request.getParameterNames();
    while (e.hasMoreElements()) {
        String fieldName = (String) e.nextElement();
        String fieldValue = request.getParameter(fieldName);
        log.info("requesting befor sending it ahead"+request.getParameter("fieldValue"));
        if ((fieldValue != null) && (fieldValue.length() > 0)) {
            fields.put(fieldName, fieldValue); 
            log.info("Field name"+fieldName+"Field Value"+fieldValue);
        }
    }
    fields.remove("inprocess");
  String merchantId = request.getParameter("MerchantId");

   
    /* if (SECURE_SECRET != null && SECURE_SECRET.length() > 0) {
        String secureHash = hashAllFields(fields);
        fields.put("SecureHash", secureHash);
    } */
    response.setHeader("Content-Type", "text/html; charset=ISO-8859-1");
    response.setHeader("Expires", "Mon, 26 Jul 1997 05:00:00 GMT");
    response.setDateHeader("Last-Modified", new Date().getTime());
    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    response.setHeader("Pragma", "no-cache");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

    <head><title>eComm Merchant Adapter Example</title>
        <meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>
        <meta http-equiv="cache-control" content="no-cache" />
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="expires" content="0" />
        <script type="text/javascript">
            history.forward();
        </script>
        
        <script type="text/javascript">
        function submitMe()
        {
        	//alert('Submit Me....')
        	document.RedirectForm.submit();
        }
        </script>
        
    </head>

    <body onload="submitMe()">
 <%    


StringBuffer sb=new StringBuffer();
			for (Iterator itr = fields.keySet().iterator(); itr.hasNext();)
			{	
				log.info("itr:{}",itr);
				String fieldName = (String) itr.next();
				sb.append(fieldName);
				sb.append("=");
				sb.append((String)fields.get(fieldName));			
				sb.append("::");
			}

                   
	                String encName="EncData";
        	        String encValue=null;
                   
			log.info("String for Encrytion===="+sb.toString());
                        //encValue=encrypt(sb.toString(),encKey);
			encValue=sb.toString();
		        //log.info("String after Encrytion===="+encValue);
       %>
        <center><h1> Redirect Page </h1></center> 
     <form name="RedirectForm" action="https://uiwuei.trdshdsjhd.com:28085/merchantWeb/collect/service/meTransCollectSvc" method="post"> 
       
            <table width="80%" align="center" border="0" cellpadding='0' cellspacing='0'>
                  <%                    
                    for (Iterator itr = fields.keySet().iterator(); itr.hasNext();) {
                        String fieldName = (String) itr.next();
                        log.info("FieldName"+fields.get(fieldName));					
						log.info(fieldName+" "+fields.get(fieldName));
			%>
				<tr>
					<td><input type="hidden" name="<%=fieldName%>" value="<%=fields.get(fieldName)%>"></td>
				</tr>
			<%
                    }
			%>	
				<tr>
					<td><input type="hidden" name="<%=encName%>" value="<%=encValue%>"></td>
				</tr>
				
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2" align="center"> Please wait....
				<!--input type="submit" name="submit" value="Continue" /--></td></tr>
			</table>
		</form>
    </body>
         <head>
            <meta http-equiv="cache-control" content="no-cache" />
            <meta http-equiv="pragma" content="no-cache" />
            <meta http-equiv="expires" content="0" />
        </head>
</html>

