<%@page import="com.rew.payment.utility.PGUtils"%>
<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8" language="java" %>
<%@page import="java.util.* , java.text.*"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%		
    Logger log = LoggerFactory.getLogger("JSPS.merchantRequestParams.jsp");

	String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	String uniqueOrderId  = new SimpleDateFormat("ddMMYYYY").format(new Date())+new Date().getTime();
	log.info("Merchant's Unique Order Number : "+uniqueOrderId);
%>


<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>

<!-- <table width='65%' align="center" border='2' cellpadding='2' bgcolor='#C1C1C1' >
	<tr>
		<td bgcolor='#33EEFF' width='100%' align="center" >
		<h2 class='co mb-3'>&nbsp;SIMULATOR PAGE FOR TRANSACTION</h2>
		</td>
	</tr>
</table> -->
<nav  style="background-color:#669cda ;height: 40px ;text-align: center;color: white;margin-top: 20px;font-size: larger;"><h2 class='co mb-3'>&nbsp;SIMULATOR PAGE FOR TRANSACTION</h2></nav>
<head>
	<style>html *
		{
		   font-size: 13 !important;
		   font-family: Arial !important;
		}</style>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.min.js" integrity="sha384-cuYeSxntonz0PPNlHhBs68uyIAVpIIOZZ5JqeqvYYIcEL727kskC66kF92t6Xl2V" crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
</head>
<form class="forms-sample" style="text-align: center;"action="merchantRequestProcessor" method="post" accept-charset="ISO-8859-1">

	
<div class="row mt-4">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label">Auth Id : <font color="red">*</font></label>
	</div>
	<div class="col-3">
		<input class="textbox form-control" type="text" name="AuthID" id="AuthID" value="M00005353" > 
	</div>
	<div class="col-3 ">
		Enter your <font color="red">Test/Live</font> Auth Id
	</div>

</div>

<div class="row mt-3">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label">Auth Key : <font color="red">*</font></label>
	</div>
	<div class="col-3">
		<input class="textbox form-control" type="text" name="AuthKey" id="AuthKey" value="fj0RH5hm2El7FV8yr6ns7UL7qr3Np3km" >
	</div>
	<div class="col-3">
		Enter your <font color="red">Test/Live</font> Auth Key 
	</div>

</div>

<div class="row mt-3">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label">Customer Order No : <font color="red">*</font></label>
	</div>
	<div class="col-3">
		<input class="textbox form-control" type="text" name="CustRefNum" id="CustRefNum" value="<%=uniqueOrderId%>" >
	</div>
	<div class="col-3">
		Enter your <font >Enter Customer Order No </font> 
	</div>

</div>

<div class="row mt-3">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label">Transaction Amount(Rs.) : <font color="red">*</font></label>
	</div>
	<div class="col-3">
		<input class="textbox form-control" type="text" name="txn_Amount" id="txn_Amount" value="10.00" >
	</div>
	<div class="col-3">
	 <font >ENTER Transaction Amount(Rs.) </font> 
	</div>

</div>

<div class="row mt-3">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label">Transaction Date Time :  <font color="red">*</font></label>
	</div>
	<div class="col-3">
		<input class="textbox form-control" type="text" name="PaymentDate" id="PaymentDate" value="<%=dateTime%>" >
	</div>
	<div class="col-3">
	 <font >ENTER Transaction Amount(Rs.)</font> 
	</div>

</div>

<div class="row mt-3">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label">Email ID  :  <font color="red">*</font></label>
	</div>
	<div class="col-3">
		<input class="textbox form-control" type="text" name="EmailId" id="EmailId" value="test@test.com" >
	</div>
	<div class="col-3">
		<font >ENTER Your EMAIL ID</font> 
	</div>

</div>

<div class="row mt-3">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label">Mobile No. : <font color="red">*</font></label>
	</div>
	<div class="col-3">
		<input class="textbox form-control" type="text" name="ContactNo" id="ContactNo" value="9876543210" >
	</div>
	<div class="col-3">
		<font >ENTER Your Mobile No</font> 
	</div>

</div>

<div class="row mt-3">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label">ADF1 : <font color="red">*</font></label>
	</div>
	<div class="col-3">
		<input class="textbox form-control" type="text" name="adf1" id="adf1" value="NA" ></div>
	<div class="col-3">
		Shipping address of the customer
	
	</div>

</div>

<div class="row mt-3">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label">ADF2 : <font color="red">*</font></label>
	</div>
	<div class="col-3">
		<input class="textbox form-control" type="text" name="adf2" id="adf2" value="NA" >
	</div>
	<div class="col-3">
		Shipping address of the customer
	</div>

</div>

<div class="row mt-3">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label">Callback URL : <font color="red">*</font></label>
	</div>
	<div class="col-3">
		<input class="textbox form-control" type="text" name="CallbackURL" id="CallbackURL" value="<%=PGUtils.getPropertyValue("merchantResponseUrl")%>" >  
	</div>
	<div class="col-3">
		Enter Callback URL 
	</td>
	</div>

</div>

<div class="row mt-3">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label">Integration Type :  <font color="red">*</font></label>
	</div>
	<div class="col-3">
		<input class="textbox form-control" type="text" name="IntegrationType" id="IntegrationType" value="DIRECT" > 
	</td>
	</div>
	<div class="col-3">
		Enter Integration Type
	</div>

</div>

<div class="row mt-3">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label">ADF3 :  <font color="red">*</font></label>
	</div>
	<div class="col-3">
		<input class="textbox form-control" type="text" name="adf3" id="adf3" value="NA" > 
	</td>
	</div>
	<div class="col-3">
		Shipping address of the customer
	</div>

</div>


<div class="row mt-3">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label">Reseller Id :  <font color="red">*</font></label>
	</div>
	<div class="col-3">
		<input class="textbox form-control" type="text" name="Rid" id="Rid" value="NA" > 
	</div>
	<div class="col-3">
	Enter Reseller Id
	</td>
	</div>

</div>

<div class="row mt-3">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label">Reseller Order No. : <font color="red">*</font></label>
	</div>
	<div class="col-3">
		<input class="textbox form-control" type="text" name="ResellerTxnId" id="ResellerTxnId" value="NA" >
	</td>
	</div>
	<div class="col-3">
	Enter Reseller Order No
	</div>

</div>

<div class="row mt-3">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label">Encryption Type :  <font color="red">*</font></label>
	</div>
	<div class="col-3 " >
		<select class="form-control" name="type" id="type">
			
			<option value="1.1">AES256</option>
			
	  </select>
	</td>
	</div>
	<div class="col-3 ">
	Enter Encryption Type
	</div>

</div>

<div class="row mt-3">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label"> MOP : <font color="red">*</font></label>
	</div>
	<div class="col-3">
		<input class="textbox form-control" type="text" name="MOP" id="MOP" value="NA" >
	</td>
	</div>
	<div class="col-3">
	Enter MOP 
	</div>

</div>

<div class="row mt-3">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label"> MOP Details  : <font color="red">*</font></label>
	</div>
	<div class="col-3">
		<input class="textbox form-control" type="text" name="MOPDetails" id="MOPDetails" value="NA" >
	</td>
	</div>
	<div class="col-3">
	Enter MOP Details
	</div>

</div>

<div class="row mt-3">
	<div class="col-4">
		<label for="exampleInputUsername2" class="text-end col-form-label"> MOP Type   : <font color="red">*</font></label>
	</div>
	<div class="col-3">
		<input class="textbox form-control" type="text" name="MOPType" id="MOPType" value="NA" >
	</td>
	</div>
	<div class="col-3">
		Enter MOP Type 
	</div>

</div>




	



	




	

	






	


	 

	




	<button type="submit" class="btn btn-primary me-2">Submit</button>
	<button class="btn btn-secondary">Cancel</button>
  </form>

<!-- <html>
	 <HEAD>
    	<TITLE>Merchant Test Page</TITLE>
    	<STYLE type='text/css'>
            H1       { font-family:Arial,sans-serif; font-size:24pt; color:#08185A; font-weight:100}
            H2.co    { font-family:Arial,sans-serif; font-size:24pt; color:#08185A; margin-top:0.1em; margin-bottom:0.1em; font-weight:100}
            H3.co    { font-family:Arial,sans-serif; font-size:16pt; color:#000000; margin-top:0.1em; margin-bottom:0.1em; font-weight:100}
            body     { font-family:Verdana,Arial,sans-serif; font-size:10pt; color:#08185A ;background-color:#FFFFFF }
            P        { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#FFFFFF }
            A:link   { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#08185A }
            A:visited{ font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#08185A }
            A:hover  { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#FF0000 }
            A:active { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#FF0000 }
            TD       { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#08185A }
            TD.red   { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#FF0066 }
            TD.green { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#00AA00 }
            TH       { font-family:Verdana,Arial,sans-serif; font-size:10pt; color:#08185A; font-weight:bold; background-color:#E1E1E1; padding-top:0.5em; padding-bottom:0.5em}
            input    { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#08185A; background-color:#E1E1E1; font-weight:bold }
            select   { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#08185A; background-color:#E1E1E1; font-weight:bold; width:463 }
            textarea { font-family:Verdana,Arial,sans-serif; font-size:8pt; color:#08185A; background-color:#E1E1E1; font-weight:normal; scrollbar-arrow-color:#08185A; scrollbar-base-color:#E1E1E1 }
        </STYLE>
    </HEAD>

	<body>
    	<form action="merchantRequestProcessor" method="post" accept-charset="ISO-8859-1">
    
    		
    		<table width='65%' align="center" border='2' cellpadding='2' bgcolor='#C1C1C1'>
    			<tr>
    				<td bgcolor='#33EEFF' width='100%' align="center" >
    				<h2 class='co'>&nbsp;SIMULATOR PAGE FOR TRANSACTION</h2>
    				</td>
    			</tr>
    		</table>
    		
    		<br>
    		
        	<TABLE width="65%" align="center" border="2" cellpadding='2' cellspacing='3'>	        	
                
                <tr bgcolor="#84FF33">
            		<td colspan="2" height="25"><p><strong>&nbsp;<font color="black">Fields below are the Request Parameters required by payment - </font></strong></p></td>
        		</tr>
             	
             	<tr>
               		<td colspan="2">&nbsp;&nbsp;</td>
               	</tr>
               	
                	
                <tr>
              	 	<td>
              	 		Auth Id: <font color="red">*</font>
              	 	</td>
              	 	<td>
              	 		<input class="textbox form-control" type="text" name="AuthID" id="AuthID" value="M0002" > Enter you <font color="red">Test/Live</font> Auth Id
					</td>
				</tr>
                
                <tr>
                	<td>
                		Auth Key : <font color="red">*</font>
                	</td>
                	<td>
                		<input class="textbox form-control" type="text" name="AuthKey" id="AuthKey" value="jpuT6032" > Enter your <font color="red">Test/Live</font> Auth Key 
                	</td>
              	 </tr>
              	 
              	 <tr>
              	 	<td>
              	 		Customer Order No. : <font color="red">*</font>
              	 	</td>
              	 	<td>
              	 		<input class="textbox form-control" type="text" name="CustRefNum" id="CustRefNum" value="<%=uniqueOrderId%>" > Must be Unique for every Transaction
					</td>
				</tr>
              	
              	<tr>
              		<td>
              			Transaction Amount(Rs.) : <font color="red">*</font>
              		</td>
              		<td>
              			<input class="textbox form-control"type="text" name="txn_Amount" id="txn_Amount" value="10.00" >  Must be Decimal Value ( 2 decimal point )
					</td>
				</tr>								            	               	 
              	
              	<tr>
              		<td>
              			Transaction Date Time: <font color="red">*</font>
              		</td>
              		<td>
              			<input class="textbox form-control"type="text" name="PaymentDate" id="PaymentDate" value = "<%=dateTime%>" >
              		</td>
              	</tr>
             	
             	<tr>
             		<td>
             			Email ID : <font color="red">*</font>
             		</td>
             		<td>
             			<input class="textbox form-control"type="text" name="EmailId" id="EmailId" value="test@test.com" > 
					</td>
				</tr>
				
				<tr>
					<td>
					Mobile No. : <font color="red">*</font>
					</td>
					<td>
						<input class="textbox form-control"type="text" name="ContactNo" id="ContactNo" value="9876543210" > 
					</td>
				</tr>               	              
              	
              	<tr>
              		<td>
              			ADF1 : <font color="red">*</font>
              		</td>
              		<td>
              			<input class="textbox form-control"type="text" name="adf1" id="adf1" value="NA" > Shipping address of the customer
					</td>
				</tr>
              	
              	<tr>
              		<td>
              			ADF2 : <font color="red">*</font>
              		</td>
              		<td>
              			<input class="textbox form-control"type="text" name="adf2" id="adf2" value="NA" > Billing address of the customer 
					</td>
				</tr>              	             	             
             	 
             	<tr>
                	<td>
                		Callback URL : <font color="red">*</font>
                	</td>
                	<td>
 						<input class="textbox1"type="text" name="CallbackURL" id="CallbackURL" value="<%=PGUtils.getPropertyValue("merchantResponseUrl")%>"> Enter URL where Response will be redirected
 					</td>
 				</tr>
 				
 			
				
    			<tr>
					<td>
						Integration Type: <font color="red">*</font>
					</td>
					<td>
						<input class="textbox form-control"type="text" name="IntegrationType" id="IntegrationType" value="DIRECT" > DIRECT / REDIRECT
					</td>
              	</tr>
              	
              	<tr>
              		<td>
              			ADF3 :
              		</td>
              		<td>
              			<input class="textbox form-control"type="text" name="adf3" id="adf3" value="NA" > 
					</td>
				</tr> 
			
				<tr>
					<td>
              			Reseller Id :
              		</td>
              		<td>
              			<input class="textbox form-control"type="text" name="Rid" id="Rid" value="NA" > EX: "R0000"
              			
              			
					</td>
					</tr>
				<tr>
              		<td>
              			Reseller Order No. :
              		</td>
              		<td>
              			<input class="textbox form-control"type="text" name="ResellerTxnId" id="ResellerTxnId" value="NA" > EX: "293938595858585858"
              			
              			
					</td>
				</tr>
				 <tr>
              		<td>
              		    Encryption Type :
              		</td>
              		<td class="textbox form-control">
              		
              			
              			<select name="type" id="type">
							  <option value="1.0">AES128</option>
							  <option value="1.1">AES256</option>
						</select>
					</td>
				</tr>  	  				               
               	
               	<tr>
               		<td colspan="2">&nbsp;&nbsp;</td>
               	</tr>
               	
               	<tr bgcolor="#84FF33">
            		<td colspan="2" height="25"><p><strong>&nbsp;<font color="black">Proper Values of below field are required only if Txn Type is</font> <font color="red">"REDIRECT"</font> <font color="black">else default value is "NA".</font> </strong></p></td>
        		</tr>
        		
        		<tr>
               		<td colspan="2">&nbsp;&nbsp;</td>
               	</tr>
               	
               	<tr>
              		<td>
              			MOP :
              		</td>
              		<td>
              			<input class="textbox form-control"type="text" name="MOP" id="MOP" value="NA" > NB/CC/DC/UPI/WALLET for Txn Type "REDIRECT"
					</td> 
				</tr> 
				
				
				
				<tr>
               		<td>
               			MOP Details :
               		</td>
               		<td>
               			<input class="textbox form-control"type="text" name="MOPDetails" id="MOPDetails" value="NA" > For CC/DC : "CARDNAME|CARDNO|CVV|EXPIRY" or For NB : Bank specific code provided by payment or For UPI : VPA or For WALLET : Wallet Name(PayZapp/ATOMWALLET)
               		</td>
              	</tr>  
				
               	<tr>
               		<td>
               			MOP Type :
               		</td>
               		<td>
               			<input class="textbox form-control"type="text" name="MOPType" id="MOPType" value="NA" > For CC/DC : Visa/Mastercard/Rupay/Amex etc... or For UPI : UPI/ATOMUPI
               		</td>
              	</tr>              	              	 
            	
            	<tr>
            		<td colspan="2">&nbsp;&nbsp;</td>
            	</tr>
            	
            	<tr>
               		<td colspan="2" align="center" >                	
                		<input type = "submit" id="subMe" value="Submit Request"/>
                	</td>
                </tr>

        	</TABLE>

    	</form>

	</body>

</html> -->