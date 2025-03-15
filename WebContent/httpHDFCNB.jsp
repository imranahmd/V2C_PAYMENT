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
	
	String dateTimeVery = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
%>


<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>

<html>
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
    	<form action="https://flexatsup.hdfcbank.com/netbanking/epi" method="post" accept-charset="ISO-8859-1">
    
    		<!-- get user input -->
    		<table width='65%' align="center" border='2' cellpadding='2' bgcolor='#C1C1C1'>
    			<tr>
    				<td bgcolor='#33EEFF' width='100%' align="center" >
    				<h2 class='co'>&nbsp;HDFC NB Verify PARAMETERS</h2>
    				</td>
    			</tr>
    		</table>
    		
    		<br>
    		
        	<TABLE width="65%" align="center" border="2" cellpadding='2' cellspacing='3'>	        	
            
               	
                <tr>
              	 	<td>
              	 		MerchantCode : <font color="red">*</font>
              	 	</td>
              	 	<td>
              	 		<input class="textbox" type="text" name="MerchantCode" id="MerchantCode" value="M0002" > Enter you <font color="red">Test/Live</font> Merchant ID
					</td>
				</tr>
                
                
              	 
              	 <tr>
              	 	<td>
              	 		MerchantRefNo . : <font color="red">*</font>
              	 	</td>
              	 	<td>
              	 		<input class="textbox" type="text" name="MerchantRefNo" id="MerchantRefNo" value="<%=uniqueOrderId%>" > Must be Unique for every Transaction
					</td>
				</tr>
              	
              								            	               	 
              	
              	<tr>
              		<td>
              			ClientCode : <font color="red">*</font>
              		</td>
              		<td>
              			<input class="textbox"type="text" name="ClientCode" id="ClientCode" value = "ClientCode01" >
              		</td>
              	</tr>
             	
             	<tr>
             		<td>
             			SuccessStaticFlag : <font color="red">*</font>
             		</td>
             		<td>
             			<input class="textbox"type="text" name="SuccessStaticFlag" id="SuccessStaticFlag" value="N" > 
					</td>
				</tr>
				
				<tr>
					<td>
						FailureStaticFlag. : <font color="red">*</font>
					</td>
					<td>
						<input class="textbox"type="text" name="FailureStaticFlag" id="FailureStaticFlag" value="N" > 
					</td>
				</tr>        
				
				<tr>
              		<td>
              			TxnAmount(Rs.) : <font color="red">*</font>
              		</td>
              		<td>
              			<input class="textbox"type="text" name="TxnAmount" id="TxnAmount" value="10.00" >  Must be Decimal Value ( 2 decimal point )
					</td>
				</tr>	
				       	              
              	
              	<tr>
              		<td>
              			TransactionId : <font color="red">*</font>
              		</td>
              		<td>
              			<input class="textbox"type="text" name="TransactionId" id="TransactionId" value="XTXTV01" > 
					</td>
				</tr>
              	
              	<tr>
              		<td>
              			FlgVerify : <font color="red">*</font>
              		</td>
              		<td>
              			<input class="textbox" type="text" name="FlgVerify" id="FlgVerify" value="Y" > Billing address of the customer 
					</td>
				</tr>              	             	             
             	 
             	 <tr>
              	 	<td>
              	 		dateTimeVery . : <font color="red">*</font>
              	 	</td>
              	 	<td>
              	 		<input class="textbox" type="text" name="Date" id="Date" value="<%=dateTimeVery%>" > Must be Unique for every Transaction
					</td>
				</tr>
				
				 <tr>
              	 	<td>
              	 		Ref1 . : <font color="red">*</font>
              	 	</td>
              	 	<td>
              	 		<input class="textbox" type="text" name="Ref1" id="Ref1" value="123456789000005" > Must be Unique for every Transaction
					</td>
				</tr>
				
             	<tr>
               		<td colspan="2" align="center" >                	
                		<input type = "submit" id="subMe" value="Submit Request"/>
                	</td>
                </tr>
				
				
				
        	</TABLE>

    	</form>

	</body>

</html>