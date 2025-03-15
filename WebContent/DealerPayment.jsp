<%@page import="com.rew.payment.utility.PGUtils"%>
<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8" language="java" %>
<%@page import="java.util.* , java.text.*"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<!DOCTYPE html>
<html>

<%
    Logger log = LoggerFactory.getLogger("JSPS.DealerPayout.jsp");

	String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	String uniqueOrderId  = new SimpleDateFormat("ddMMYYYY").format(new Date())+System.currentTimeMillis(); 
	log.info("Merchant's Unique Order Number : "+uniqueOrderId);
%>


<head>
<title>Epaisaa</title>
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
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css" />
<script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
<script type="text/javascript" src="script/script.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>

<!-- Script add by bharat -->
 <script src="newjs/creditcard.js"></script>
 <script src="newjs/aes.js"></script>
 <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script> 
 <script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>

 

</head>
<body>
<div class="container">
 <header>
    <div class="logo"><img class="mylogo" src="images/Epaisaatransparent.png"></div>
    <div class="title" align="center"> Epaisaa FASTAG </br>
    <span class="subtitle">PAYMENT PAGE </span><br/> </div>
   <div class="sum-pointer"></div>
    <div class="sum-total" width="20px"> </div>
    </header>
	<div class="main">
        
        
            <div class="white-border">
                             
            <form action="merchantRequestProcessor" method="post" accept-charset="ISO-8859-1" autocomplete="off">
    
    		
    		
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
              	 		<input class="textbox" type="hidden" name="merchantId" id="merchantId" value="M00075" >
				<!-- 	</td>
				</tr> -->
                
                <!-- <tr>
                	<td>
                		API Key : <font color="red">*</font>
                	</td>
                	<td> -->
                		<input class="textbox" type="hidden" name="apiKey" id="apiKey" value="2BG31Z23LBLGRU4P" >
                	<!-- </td>
              	 </tr> -->
              	 
              	 <tr>
					<td>
						Enter Mobile No. : <font color="red">*</font>
					</td>
					<td>
						<input class="textbox" type="number" name="custMobile" id="custMobile" pattern="[0-9]{10}" maxlength="10" 
						style="height:43px;width:430px;border-radius: 5px;">
					</td>
				</tr>  
              	 
              	 <tr>
					<td>
						Name : <font color="red">*</font>
					</td>
					<td>
						<input class="textbox"type="text" name="udf5" id="udf5" value="" maxlength="50">
						<input class="textbox"type="hidden" name="type" id="type1" value="1.0" >
					</td>
				</tr>
              	 
              	 
              	  <tr>
              		<td>
              			Order Type: <font color="red">*</font>
              		</td>
              		
              		<td>
              		
              			<select id="type" style="height:40px;width:425px;border-radius: 5px;">
  							 <option value="TagOrder" selected>New Tag Order</option>
  							 <option value="Reorder">Reorder</option>
						</select>
					</td>
				</tr>	<tr><td><br></td><td><br></td></tr>
              	 
              	 <tr>
              		<td>
              			Enter Quantity : <font color="red">*</font>
              		</td>
              		<td>
              			<input class="textbox" type="number" name="amount" id="amount" value="5" style="height:43px;width:430px;border-radius: 5px;">
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
             	
             	<tr>
             		<td>
             			Customer Mail ID : <font color="red">*</font>
             		</td>
             		<td> 
             			<input class="textbox" type="email" name="custMail" id="custMail" value="test@test.com" required>
					</td>
				</tr>
				
				
              	
              <tr>
              		<td>
              			Address : <font color="red">*</font>
              		</td>
              		<td>
              			<input class="textbox" type="text" name="udf2" id="udf2" pattern=".{40,200}" value="NA" required title="minimum 40 and maximum 200 characters required">
				</td>
				</tr>  
				
				 <tr>
              		<td>
              			PIN Code: <font color="red">*</font>
              		</td>
              		<td>
              			<input class="textbox" type="text" name="udf1" id="vec_no" value="" minlength="6" maxlength="6" onkeypress="isNumberKeyPIN(event, 'PIN should be numeric ')">
					</td>
				</tr>    
				
				<tr>
              		<td>
              			Date of Birth: <font color="red">*</font>
              		</td>
              		<td>
              			<input type="text" name="udf4" id="datepicker" autocomplete="off" required>
					 </td>
				</tr>     	             	             
             	 
             <!-- 	<tr>
                	<td>
                		Merchant Return URL : <font color="red">*</font>
                	</td>
                	<td> -->
 						<input class="textbox1"type="hidden" name="returnURL" id="returnURL" value="https://pay.Epaisaapg.in/payment/EpaisaaFastagPaymentResponse.jsp">
 					<!-- //https://pay.Epaisaapg.in/payment/DealerPayoutResponse.jsp 
 					
 					</td>
 				</tr> -->
 				
 				<!-- <tr>
                	<td>
                		Multipart Settlement : <font color="red">*</font> 
                	</td>
                	<td> -->
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
              	
              	<tr>
              		<td>
              			LG Code :
              		</td>
              		<td>
              			<input class="textbox"type="text" name="udf3" id="udf3" value="NA" > 
				 </td>
				</tr> 
				<tr>
              		<td>
              			LC Code :
              		</td>
              		<td>
              			<input class="textbox"type="text" name="udf3" id="udf4" value="NA" > 
					 </td>
				</tr> 
				<!-- <tr>
              		<td>
              			UDF5 :
              		</td>
              		<td> -->
              			 
              			<!--  input class="textbox"type="hidden" name="type" id="type1" value="1.0" -->
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
               			<input class="textbox"type="hidden" name="cardType" id=""cardType"" value="NA" > 
               		<!-- </td>
              	</tr>  
				 -->
               <!-- 	<tr>
               		<td>
               			Card Type :
               		</td>
               		<td> 
               			<input class="textbox"type="hidden" name="cardType" id=""cardType"" value="NA" > 
               		< </td>
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
		<p class="spons-text">Powered by Epaisaa</p>
		</div>
	</div>
	
 <script>
 
 var maxBirthdayDate = new Date();
 maxBirthdayDate.setFullYear( maxBirthdayDate.getFullYear() - 18 );
 maxBirthdayDate.setMonth(11,31)
 $( "#datepicker" ).datepicker({
 	dateFormat: 'dd.mm.yy',
 	changeMonth: true,
     changeYear: true,
 	maxDate: maxBirthdayDate,
   yearRange: '1950:'+maxBirthdayDate.getFullYear(),
 });
 
 function onlyalphabate(custName){
     var regexp =/^[A-Za-z ]+$/;
     if(regexp.test(custName)) {
            return true;
     }else{
         return false;
	 } 
 }
 
 function isNumberKeyPIN(e,spanId) 
	{
	    var charCode = (e.which) ? e.which : event.keyCode
	    if (charCode > 31 && (charCode < 48 || charCode > 57))
		{
	    	alert("Only Numbers Allowed upto 6 Digits");
	    	document.getElementById("vec_no").focus();
			
		return false;		    
		}
	    valueChange(spanId);
		return true;
	}
 var cumulativeAmount = "0.00";
function validateDetails(e) {
	try{
		  var custMobile=document.getElementById("custMobile").value;
		  var vec_no=document.getElementById("vec_no").value;
		  var quantity=document.getElementById("amount").value;
		  var type=document.getElementById("type").value;
		  var custName = document.getElementById("udf5").value;
		  var dob = document.getElementById("datepicker").value;
		  var lgCode=document.getElementById("udf3").value;
		  var lcCode=document.getElementById("udf4").value;

		  if(custMobile==null || custMobile=="")
			{
			  	alert("Mobile Number cannot be blank");
			  	document.getElementById("custMobile").value="";
			 	 return false;
			}
		  else if(custName==null || custName==""){
			    alert("Name cannot be blank");
			  	document.getElementById("udf5").value="";
			 	 return false;
			}
		  else if(!onlyalphabate(custName)){
			  alert("Name should contain alphabets only");
			  document.getElementById("udf5").value="";
			 	 return false;
		  }
		  else if(custMobile.length!=10)
			{
				  alert("Mobile Number should be of 10 digits");
				  document.getElementById("custMobile").value="";
				  return false;
			}
			else  if(vec_no==null || vec_no=="")
			{
				alert("PIN cannot be blank");
				document.getElementById("vec_no").value="";
				return false;
			}
			else  if(dob==null || dob=="")
			{
				alert("Date of birth cannot be blank");
				document.getElementById("vec_no").value="";
				return false;
			}
			else if(vec_no.length<6)
			{
				  alert("PIN Digits should not be gerater than 6 didgits");
				  document.getElementById("vec_no").value="";
				  return false;
			}
		  
			else if(type==null || type=="" || type=="Select Type" )
			{
				  alert("Please Select Type");
				  document.getElementById("type").value="TagOrder";
				  return false
			}
			else if(quantity==null || quantity=="")
			{
				alert("Quantity cannot be blank");
				document.getElementById("amount").value="5";
				return false;
			}else if(quantity.indexOf(".") != -1){
				alert("Invalid Quantity");
				document.getElementById("amount").value="5";
				return false;
			}else if(quantity < 5 || quantity > 30){
			    alert("Invalid Quantity");
                document.getElementById("amount").value="5";
                return false;
			}
			else{
				  if(cumulativeAmount < 1000){
					  amount = 1000 - cumulativeAmount;
					  amount = amount + ".00";
				  }else{
					  amount = "1.00";
				  }
				  alert("Final Amount : "+amount);
				  document.getElementById("amount").value=amount;
				  document.getElementById("cardType").value="NA";
				  document.getElementById("udf3").value=lgCode+"|"+lcCode+"|"+quantity;
			}
		 
		}catch(err) {
			alert(err);
		}
	}
	
	$('#custMobile').bind('change', function(e) { 
		var custMobile=document.getElementById("custMobile").value;
		var merchantId=document.getElementById("merchantId").value;
		var regexp =/[0-9]{10}/;
	    if(!regexp.test(custMobile)) {
	    	alert("Mobile Number should be of 10 digits");
	        return false;
		}
	    document.getElementById("type").disabled=false;
	    $.ajax ({				
			type: 'POST',
		    data: {mobile :custMobile, merchantId : merchantId},
			url: "${pageContext.request.contextPath}/ajaxAction?type=getDealerDetails",
			
			success: function(result) {
				//alert("result "+result);
				if(result != null && result !="") {
					var obj = $.parseJSON(result);
					document.getElementById("udf5").value=obj.name;
					document.getElementById("type").value="Reorder";
					document.getElementById("type").disabled=true;
					document.getElementById("custMail").value=obj.mail;
					document.getElementById("udf2").value=obj.address;
					document.getElementById("vec_no").value=obj.pincode;
					document.getElementById("datepicker").value=obj.dob;
					document.getElementById("udf3").value=obj.lg;
					document.getElementById("udf4").value=obj.lc;
					cumulativeAmount = obj.amount;
				}
			},
			error: function (err) {
				alert("Getting error : "+custMobile);
		    }
		});
	});
</script>
</body>
</html>
