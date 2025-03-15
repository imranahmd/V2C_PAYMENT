<%@page import="com.rew.payment.utility.PGUtils"%>
<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8" language="java" %>
<%@page import="java.util.* , java.text.*"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>

<!DOCTYPE html>
<html>

  <%		
    Logger log = LoggerFactory.getLogger("JSPS.PayFasTagPayment.jsp");

	String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	String uniqueOrderId  = new SimpleDateFormat("ddMMYYYY").format(new Date())+new Date().getTime();
	log.info("Merchant's Unique Order Number : "+uniqueOrderId);
%>


<head>
<title>Payment</title>
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

.spons-text {
    color: #646464;
    display: inline-block !important;
    padding: 15px !important;
    vertical-align: bottom;
    float: none !important;
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
    <span class="subtitle">PAYMENT PAGE </span><br/> </div>
   <div class="sum-pointer"></div>
    <div class="sum-total" width="20px"> </div>
    </header>
	<div class="main">
        
        
            <div class="white-border">
                             
            <form action="merchantRequestProcessor" method="post" accept-charset="ISO-8859-1">
    
    		
    		
    		<br>
    		
        	<TABLE width="65%" align="center" border="3" cellpadding='2' cellspacing='3'>	        	
                
               
             	
             	<tr>
               		<td colspan="2">&nbsp;&nbsp;</td>
               	</tr>

               <!--  <tr>
              	 	<td>
              	 		Merchant Id: <font color="red">*</font>
              	 	</td>
              	 	<td> -->
              	 		<input class="textbox" type="hidden" name="merchantId" id="merchantId" value="M00017" > 
				<!-- 	</td>
				</tr> -->
                
                <!-- <tr>
                	<td>
                		API Key : <font color="red">*</font>
                	</td>
                	<td> -->
                		<input class="textbox" type="hidden" name="apiKey" id="apiKey" value="BXVRCH2F92WMCVN1" > 
                	<!-- </td>
              	 </tr> -->
              	 
              	 <tr>
					<td>
						Enter Mobile No. : <font color="red">*</font>
					</td>
					<td>
						<input class="textbox"type="text" name="custMobile" id="custMobile" value="" minlength="10" maxlength="10" onkeypress="return isNumberKey(event);"> 
					</td>
				</tr>  
              	 
              	 <tr>
              		<td>
              			Enter Vehicle NO : <font color="red">*</font>
              		</td>
              		<td>
              			<input class="textbox"type="text" name="udf1" id="vec_no" value="" maxlength="22" onchange="onlyalphabate(document.getElementById('vec_no'), 'Vehicle Number should be alphanumeric ')">
					</td>
				</tr>
              	 
              	 </tr>
              	  <tr>
              		<td>
              			Select Type: <font color="red">*</font>
              		</td>
              		
              		<td>
              		
              			<select id="type" style="height:40px;width:425px;border-radius: 5px;">
              				<!-- <option value="Select Type">Select Type</option> -->
  							 <option value="New Issuance" selected>New Issuance</option>
 
						</select>
					</td>
				</tr>	
              	 
              	 <tr>
              		<td>
              			Enter Amount(Rs.) : <font color="red">*</font>
              		</td>
              		<td>
              			<input class="textbox"type="text" name="amount" id="amount" value="500"  onchange="checkamount(document.getElementById('amount'), 'Invalid Amount')" readonly>  
					</td>
				</tr>	
              	 
              	<!--  <tr>
              	 	 <td>
              	 		Merchant Order No. : <font color="red">*</font>
              	 	</td>  
              	 	<td>-->
              	 		<input class="textbox" type="hidden" name="txnId" id="txnId" value="<%=uniqueOrderId%>" >
					<!-- </td>
				</tr>
              	 -->
              							            	               	 
              	<!--
              	<tr>
              		 <td>
              			Transaction Date Time: <font color="red">*</font>
              		</td> 
              		<td>-->
              			<input class="textbox"type="hidden" name="dateTime" id="dateTime" value = "<%=dateTime%>" >
              		<!-- </td>
              	</tr> -->
             	
             	<!-- <tr>
             		<td>
             			Customer Mail ID : <font color="red">*</font>
             		</td>
             		<td> -->
             			<input class="textbox"type="hidden" name="custMail" id="custMail" value="test@test.com" > 
					<!-- </td>
				</tr> -->
				
				
              	
              <!-- 	<tr>
              		<td>
              			UDF2 : <font color="red">*</font>
              		</td>
              		<td> -->
              			<input class="textbox"type="hidden" name="udf2" id="udf2" value="NA" > 
				<!-- 	</td>
				</tr>        -->       	             	             
             	 
             <!-- 	<tr>
                	<td>
                		Merchant Return URL : <font color="red">*</font>
                	</td>
                	<td> -->
 						<input class="textbox1"type="hidden" name="returnURL" id="returnURL" value="https://domain/pay/PayFastagPaymentResponse.jsp"> 
 					
 						<input class="textbox1"type="hidden" name="isMultiSettlement" id="isMultiSettlement" value="0">
 					<!-- </td>
 				</tr> -->
 				
 				<!-- <tr>
              		<td>
              			Product ID : <font color="red">*</font>
              		</td>
    				<td> -->
    					<input class="textbox"type="hidden" name="productId" id="productId" value="DEFAULT" > 
    				<!-- </td>
    			</tr>   -->
    			
    			<!-- <tr>
              		<td>
              			Channel ID : <font color="red">*</font>
              		</td>
              		<td> -->
              			<input class="textbox" type="hidden" name="channelId" id="channelId" value="0" > 
					<!-- </td>
				</tr> -->
				<!-- 
    			<tr>
					<td>
						Transaction Type: <font color="red">*</font>
					</td>
					<td> -->
						<input class="textbox"type="hidden" name="txnType" id="txnType" value="DIRECT" > 
					<!-- </td>
              	</tr> -->
              	
              	<!-- <tr>
              		<td>
              			UDF3 :
              		</td>
              		<td> -->
              			<input class="textbox"type="hidden" name="udf3" id="udf3" value="NA" > 
					<!-- </td>
				</tr>  -->
				<!-- <tr>
              		<td>
              			UDF4 :
              		</td>
              		<td> -->
              			<input class="textbox"type="hidden" name="udf4" id="udf4" value="NA" > 
					<!-- </td>
				</tr>  -->
				<!-- <tr>
              		<td>
              			UDF5 :
              		</td>
              		<td> -->
              			<input class="textbox"type="hidden" name="udf5" id="udf5" value="NA" > 
              			<input class="textbox"type="hidden" name="type" id="type1" value="1.0" >
					<!-- </td>
				</tr>    	 -->			               
               	
              <!--  	<tr>
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
              			Instrument ID :
              		</td>
              		<td>-->
              			<input class="textbox"type="hidden" name="instrumentId" id="instrumentId" value="NA" > 
              			<!-- </td
				</tr>   -->
				
				
				
			<!-- 	<tr>
               		<td>
               			Card Details :
               		</td>
               		<td> -->
               			<input class="textbox"type="hidden" name="cardDetails" id="cardType" value="NA" > 
               		<!-- </td>
              	</tr>  
				 -->
               <!-- 	<tr>
               		<td>
               			Card Type :
               		</td>
               		<td> -->
               			<input class="textbox"type="hidden" name="cardType" id=""cardType"" value="NA" > 
               		<!-- </td>
              	</tr>            -->   	              	 
            	
            	<tr>
            		<td colspan="2">&nbsp;&nbsp;</td>
            	</tr>
            	
            	<tr>
            	
               		<td colspan="2" align="center">                	
                			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                			<input type = "submit" class="pay-now" id="subMe" value="Submit Request" onclick="return validateDetails();" />
                	</td>
                	
                </tr>
                
                

        	</TABLE>
        	
        	 </div>

    	</form>
  
                    
    </div>	

        </div>
    </div>
        <div class="footer">
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <div class="spons-logos"><img src="images/pci_logo.png" ></div></center>
        <div class="spons-logos"><img src="images/ssl.png"> </div>
		<p class="spons-text">Powered by Pay</p>
		</div>
	</div>
	
 <script>
 
 function onlyalphabate(element, AlertMessage){

     var regexp =/^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$/;// /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]$/;

     if(element.value.match(regexp)) {

            return true;

            }else{

                   alert(AlertMessage); 
                   element.focus(); 
                   element.value="";
                   element.focus();
                   return false;

                   } }
 
 function checkamount(element, AlertMessage){

     var regexp = /^\d+(\.\d{1,2})?$/;

     if(element.value.match(regexp)) {

            return true;

            }else{

                   alert(AlertMessage); 
                   element.focus(); 
                   element.value="";
                   element.focus();
                   return false;

                   } }
 function isNumberKey(e,spanId) 
	{
	    var charCode = (e.which) ? e.which : event.keyCode
	    if (charCode > 31 && (charCode < 48 || charCode > 57))
		{
	    	alert("Only Numbers Allowed upto 10 Digits");
	    	document.getElementById("custMobile").focus();
			
 		return false;		    
		}
	    valueChange(spanId);
		return true;
	}
 
function validateDetails(e) {
	
	try{
  var custMobile=document.getElementById("custMobile").value;
  var vec_no=document.getElementById("vec_no").value;
  var amount=document.getElementById("amount").value;
  var type=document.getElementById("type").value;

	
  document.getElementById("udf2").value=type;
  
  if(custMobile==null || custMobile=="")
	{
	  	alert("Enter  Mobile Number cannot be blank");
	  	document.getElementById("custMobile").value="";
	 	 return false;
	}
  else if(custMobile.length!=10)
	{
		  alert("Enter  Mobile Number Digits upto 10");
		  document.getElementById("custMobile").value="";
		  return false;
	}
	else  if(vec_no==null || vec_no=="")
	{
		alert("Enter  vehicle Number cannot be blank");
		document.getElementById("vec_no").value="";
		return false;
	}
	else if(vec_no.length>22)
	{
		  alert("Enter  vehicle Number Digits should not be gerater than 10 didgits");
		  document.getElementById("vec_no").value="";
		  return false;
	}
  
	else if(type==null || type=="" || type=="Select Type" )
	{
		  alert("Please Select Type");
		  document.getElementById("type").value="New Issueance";
		  return false
	}
	else if(amount==null || amount=="")
	{
		alert("Enter  Amount cannot be blank");
		document.getElementById("amount").value="";
		return false;
	}

	else
		{
		amount=amount+".00";
		  document.getElementById("amount").value=amount;
		}
 
	}catch(err)
	{
		alert(err);
	}
 
}
</script>
</body>
</html>