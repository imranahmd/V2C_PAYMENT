
<%@page import="com.rew.pg.dto.TransactionMaster"%>
<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8" language="java" %>
<%@page import="com.rew.payment.utility.PSPCLEncryptor"%>
<%@page import="com.rew.pg.db.DataManager"%>
<%@page import="com.rew.pg.dto.MerchantDTO"%>
<%@page import="com.rew.payment.utility.PGUtils"%>
<%@page import="java.security.MessageDigest"%>
<%@page import="javax.crypto.Cipher"%>
<%@page import="javax.crypto.spec.SecretKeySpec"%>
<%@page import="java.security.Key"%>
<%@page import="org.json.JSONObject"%>
<%@page contentType="text/html; charset=ISO-8859-1" language="java"%>
<%@page import="java.util.*,java.io.*,java.net.*,java.text.*"%>
<%@ page import="com.rew.payment.utility.PSPCLEncryptor"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<!DOCTYPE html>
<html>

  <%!
    Logger log = LoggerFactory.getLogger("JSPS.PayfastagPaymentResponse.jsp");

	//This is GetTextBetweenTags function which return the value between two XML tags or two string
			
	private String GetTextBetweenTags(String InputText, String Tag1, String Tag2)
	{
		String Result = "NA";
	
		int index1 = InputText.indexOf(Tag1);
		
		int index2 = InputText.indexOf(Tag2);
		
		log.info("merchantResponse.jsp ::: GetTextBetweenTags() :: Index of '"+Tag1+"' : "+index1+" And Index of '"+Tag2+"' : "+index2);
		
		if (index1 != -1 && index2 != -1)
		{
			index1 = index1 + Tag1.length();
			Result = InputText.substring(index1, index2);
		}
		
		return Result;
	}
%>

<%
	//retrieve all the incoming parameters into a hash map
	Map<String,String> fields = new HashMap<String,String>();

	Enumeration<String> params = request.getParameterNames();
	while (params.hasMoreElements())
	{
		String fieldName = (String) params.nextElement();
		String fieldValue = request.getParameter(fieldName);
		log.info("merchantResponse.jsp ::: Response Field Name : "+fieldName+" And Response Field Value : "+fieldValue);
		if ((fieldValue != null) && (fieldValue.length() > 0)) 
		{
			fields.put(fieldName, fieldValue); 
		}
	}	
    
    if(fields.containsKey("txnErrorMsg") && fields.get("txnErrorMsg") != null)
    {
    	log.info("Field contains Error ............");
		String errorCode = null , errorMsg = null;
		
		String errorString = fields.get("txnErrorMsg").toString();
		
		errorCode = GetTextBetweenTags(errorString,"<error_code>","</error_code>");
		errorMsg = GetTextBetweenTags(errorString,"<error_msg>","</error_msg>");	
		
		log.info("merchantResponse.jsp ::: Error Code is : "+errorCode+" And Error Message is : "+errorMsg);
		
		String htmlRespone = "<html><table width='100%' border='2' cellpadding='2' bgcolor='#C1C1C1'>"+
					 "<tr><td bgcolor='#33EEFF' width='90%' align='center'><h2>&nbsp;MERCHANT RESPONSE PAGE</h2></td></tr>"+
					 "</table><br><br><br><br><br><br><br><br><br><br><br>";
	  	htmlRespone += "<center><h2><font color='red' size='3' face='Verdana,Arial,sans-serif'> Error "+ errorCode + " : " + errorMsg + "</font></h2></center>";          
	  	htmlRespone += "</html>";
		
		out.println(htmlRespone);
		out.flush();
		out.close();
    }
    
    else
    {
    	
    	
		String message = null;
		JSONObject respJson = null;
		String encResp = fields.get("respData").toString();
		String merchantId = fields.get("merchantId").toString();
		String pgid=fields.get("pgid").toString();
		
		DataManager dm=new DataManager();
		TransactionMaster TM=dm.getTxnMaster(pgid);

		try 
		{
			log.info("Field does not contains Error ............");
			//encResp = encResp.replaceAll("%2B", "+");
			//encResp = encResp.replaceAll("%2F", "/");  
			MerchantDTO merchantDTO = new DataManager().getMerchant(merchantId);
			log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+merchantDTO.getTransactionKey());
			
			String confMerchantId = PGUtils.getPropertyValue("pspclId"); 
			String decryptedValue=null;
			log.info("TM.getUploadedBy()=====>"+TM.getUploadedBy());
			if(confMerchantId.equals(merchantId))
			{
				log.info("PSPCL decrypt");
				 decryptedValue=PSPCLEncryptor.decrypt(merchantDTO.getTransactionKey(), merchantDTO.getTransactionKey(),encResp);
			}
			else if(TM.getUploadedBy().equalsIgnoreCase("V2"))
			{
				log.info("PGUtils.java ::: Merchant is having 32 bit key "+TM.getMerchantId());
				decryptedValue = PSPCLEncryptor.decrypt(merchantDTO.getTransactionKey(), merchantDTO.getTransactionKey().substring(0, 16),
						encResp);
			}
			
			else{
				log.info("general decrypt");
			 decryptedValue = PGUtils.getDecData(encResp, merchantDTO.getTransactionKey());
			}
			
			respJson = new JSONObject(decryptedValue);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (respJson.getString("trans_status").equalsIgnoreCase("Ok"))
		{
			message = "Transaction Successful";
		}
		else if (respJson.getString("trans_status").equalsIgnoreCase("To"))
		{
			message = "Sorry!!Your Transaction is Timed Out";
		} 
		else
		{
			message = "Transaction Failed";
		}
%>

<head>
<title>payment</title>
<!-- for-mobile-apps -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="Payment Form Widget Responsive web template, Bootstrap Web Templates, Flat Web Templates, Android Compatible web template, 
Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyEricsson, Motorola web design" />
<script type="application/x-javascript">
		addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false);
		function hideURLbar(){ window.scrollTo(0,1); } 
</script>

<style>
.white-border {
    border: 1px solid #FFFFFF;
    border-radius: 8px;
    padding: 20px;
    margin: 5px;
    min-height: 200px;
    background-color: #ecf0f2;
}

.footer {
    width: 100%;
    height: 100px;
    text-align: center;
    display: inline-block;
    margin: auto;
    padding-top: 15px;
}

img {
    max-width: 100%;
    width: 120px;
    height: 60px;
}

.spons-text {
    color: #646464;
    display: inline-block;
    padding: 15px;
    vertical-align: bottom;
}

</style>

<!-- //for-mobile-apps -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<link href="css/style.css" rel="stylesheet" type="text/css" media="all" />
<script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
<script type="text/javascript" src="script/script.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>

<!-- Script add by bharat -->
 <script src="newjs/creditcard.js"></script>
 <script src="newjs/aes.js"></script>
 <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script> 

</head>
<body>
<div class="container">
 <header>
    <div class="logo"><img class="mylogo" src="images/logo.png"></div>
    <div class="title" align="center"> PAYMENT FASTAG </br>
    <span class="subtitle">	RESPONSE PAGE </span><br/> </div>
   <div class="sum-pointer"></div>
    <div class="sum-total" width="20px"> </div>
    </header>
	<div class="main">
        
        
            <div class="white-border">
   

	</br>
	<CENTER><H1><%=message%></H1></CENTER>
	<hr>

	<TABLE width="85%" align='center' cellpadding='10' border='0'>

		<tr bgcolor="#84FF33">
			<td colspan="2" height="25"><p>
					<strong>&nbsp;<font color="black">Payment Response Parameters - </font></strong>
				</p></td>
		</tr>

		<tr>
			<td>&nbsp;
			<td>
		<tr>
			<td align='right' width='50%'><strong><i>Merchant
						Txn Reference / Merchant Order Number : </i></strong></td>
			<td width='50%'><%=respJson.getString("txn_id")%></td>
		</tr>

		<tr>
			<td align='right' width='50%'><strong><i>Merchant
						Id : </i></strong></td>
			<td width='50%'><%=respJson.getString("merchant_id")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>PG Transaction
						Reference Number : </i></strong></td>
			<td><%=respJson.getString("pg_ref_id")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Transaction Response
						Date Time : </i></strong></td>
			<td><%=respJson.getString("resp_date_time")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Transaction Status : </i></strong></td>
			<td><%=respJson.getString("trans_status")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Transaction Amount : </i></strong></td>
			<td><%=respJson.getString("txn_amount")%></td>
		</tr>
		
		<tr>
			<td align='right'><strong><i>Transaction Response Code : </i></strong></td>
			<td><%=respJson.getString("resp_code")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Transaction Response
						Message : </i></strong></td>
			<td><%=respJson.getString("resp_message")%></td>
		</tr>

		

		<tr>
			<td align='right'><strong><i>Customer Phone No. : </i></strong></td>
			<td><%=respJson.getString("cust_mobile_no")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Bank Reference Number
						: </i></strong></td>
			<td><%=respJson.getString("bank_ref_id")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Transaction Date : </i></strong></td>
			<td><%=respJson.getString("txn_date_time")%></td>
		</tr>

		<tr>
			<td align='right'><strong><i>Payment Mode : </i></strong></td>
			<td><%=respJson.getString("payment_mode")%></td>
		</tr>

	</TABLE>
        	
        	 </div>
<%
	}
%>
    	
                    
    </div>	

        </div>
    </div>
        <div class="footer">
        <div class="spons-logos"><img src="images/pci_logo.png"></div>
        <div class="spons-logos"><img src="images/ssl.png"> </div>
		<p class="spons-text">Powered by payment</p>
		</div>
	</div>
	

</body>
</html>