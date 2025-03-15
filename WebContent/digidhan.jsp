<%@page import="com.rew.payment.utility.PGUtils"%>
<%@page contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8" language="java" %>
<%@page import="java.util.* , java.text.*"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>

<!DOCTYPE html>
<html>

<%
	Logger log = LoggerFactory.getLogger("JSPS.earn-more.jsp");
	String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	//String uniqueOrderId  = new SimpleDateFormat("ddMMYYYY").format(new Date())+System.currentTimeMillis(); 
	String uniqueOrderId = UUID.randomUUID().toString().replaceAll("-","");
	log.info("Merchant's Unique Order Number : "+uniqueOrderId);
%>

<head>
<title>DIGIDHAN : Payment</title>
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
#subMe{
	position: relative;
	color: #FFFFFF;
	background-color: #2075b8;
	text-align: center;
	line-height: 48px;
    padding: 16px;
	font-weight: bold;
}

#subMe:hover{
background-color:#2075b8ba;
}
#subMe:hover::after{
    border-left: 38px solid #2075b8ba;
}
#subMe:after {
    content:"";
    position: absolute;
    left: 100%;
    top: 4px;
    width:0px;
    height:0px;
    border-top: 38px solid transparent;
    border-left: 36px solid #2075b8;
    border-bottom: 34px solid transparent;
}
.wd-50{
	width:50%;
}
.wd-100{
	width:100%;
}
.two-div{
	display:flex;
	margin-bottom:20px;
}
.no-margin{
	margin:0 !important;
}
.label-padding{
	padding:0 0 8px 0;
}
.ml-20{
	margin-left:20px;
}
.mr-20{
	margin-right:20px;
}
.mb-20{
	margin-bottom:20px;
}
.mt-20{
	margin-top:20px;
}
.footer-div{
	display:flex;
	justify-content:center;
}
.ssl-logo-div{
	margin: 0 25% 0 25%;
}
@media ( max-width : 480px) {
	.two-div {
		flex-direction: column;
	}
	.mr-20{
		margin-right:0px;
		margin-bottom:20px;
	}
	.wd-50{
		width:100% !important;
	}
	#subMe{
		width:85%;
	}
	.ssl-logo-div{
		margin: 0 5% 0 5% !important;
	}
	.pay-logo{
	}
	.pci-logo{
		width:75%;
	}
	.ssl-logo{
		width:90%;
	}
}
@media ( max-width : 768px) {
.ssl-logo-div{
		margin: 0 10% 0 10%;
	}
}
</style>

<!-- //for-mobile-apps -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<link href="css/style.css" rel="stylesheet" type="text/css" media="all" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css" />
<script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
<script type="text/javascript" src="script/script.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>

 <script src="newjs/creditcard.js"></script>
 <script src="newjs/aes.js"></script>
 <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script> 
 <script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
</head>
<body>
<div class="container">
 <header>
    <div class="logo" style="width:27%;"><img class="mylogo" src="images/logo-digidhan.png"></div>
    <div class="title" align="center"> DIGIDHAN</br>
    <span class="subtitle">PAYMENT PAGE </span><br/> </div>
    <div class="sum-pointer"></div>
    <div class="sum-total" width="20px"> </div>
 </header>
	<div class="main">
            <div class="white-border">        
            <form action="merchantRequestProcessor" method="post" accept-charset="ISO-8859-1" autocomplete="off" id="paymentForm">
	            <div>
	            	<div class="wd-100 two-div">
	            	        <!-- <input class="textbox" type="hidden" name="merchantId" id="merchantId" value="M0000804" >
                            <input class="textbox" type="hidden" name="apiKey" id="apiKey" value="Ig2BU9RD9UT4uy0Sb5jt4xx6Np8tQ4CD" > -->
	            			<input class="textbox" type="hidden" name="merchantId" id="merchantId" value="M00094" >
	                		<input class="textbox" type="hidden" name="apiKey" id="apiKey" value="FE5rZ3nj4su4ZY0xB6hc8iw6zZ4Gz9tB" >
	                		<input class="textbox" type="hidden" name="txnId" id="txnId" value="<%=uniqueOrderId%>" >
              			<input class="textbox"type="hidden" name="dateTime" id="dateTime" value = "<%=dateTime%>" >
	            		<div class="wd-50 mr-20">
	            			<div class="label-padding">
	            				<label>Phone Number :<font color="red">*</font></label>
	            			</div>
	            			<div>
	            				<input class="textbox no-margin" type="text" name="custMobile" id="custMobile" 
									style="border-radius: 5px;">
							</div>
	            		</div>
	            		<div class="wd-50">
	            			<div class="label-padding">
	            				<label>Customer Name :<font color="red">*</font></label>
	            			</div>
	            			<div>
	            				<input class="textbox no-margin"type="text" name="udf1" id="udf1" value="" maxlength="50" required>
	            			</div>
	            			
	            		</div>
	            	</div>
	            	<div class="wd-100 two-div">
	            		<div class="wd-50 mr-20">
		            		<div class="label-padding">
			            		<label>Amount :<font color="red">*</font></label>
		            		</div>
		            		<div>
		            			<input class="textbox no-margin" type="text" name="amount" id="amount" style="border-radius: 5px;">
		            		</div>
		            	</div>
		            	<div class="wd-50">
		            		<div class="label-padding">
		            			<label>Email ID :<font color="red">*</font></label>
		            		</div>
		            		<div>
		            			<input class="textbox no-margin" type="email" name="custMail" id="custMail" placeholder="test@test.com" required>
		            		</div>
	            		</div>
	            	</div>
		            <input class="textbox" type="hidden" name="udf2" id="udf2" value="NA">
		            <input class="textbox"type="hidden" name="type" id="type1" value="1.1" >
		            <input class="textbox"type="hidden" name="udf3" id="udf3" value="NA" > 
					<input class="textbox"type="hidden" name="udf4" id="udf4" value="NA" >
					<input class="textbox"type="hidden" name="udf5" id="udf5" value="NA" >
				
					<input class="textbox1"type="hidden" name="returnURL" id="returnURL" value="https://domain/payment/merchantResponse.jsp">
					<input class="textbox1"type="hidden" name="isMultiSettlement" id="isMultiSettlement" value="0">
					<input class="textbox"type="hidden" name="productId" id="productId" value="DEFAULT" >
	       			<input class="textbox" type="hidden" name="channelId" id="channelId" value="0" >
					<input class="textbox"type="hidden" name="txnType" id="txnType" value="DIRECT" >
	      			<input class="textbox"type="hidden" name="instrumentId" id="instrumentId" value="NA" >
	       			<input class="textbox"type="hidden" name="cardDetails" id="cardDetails" value="NA" > 
	       			<input class="textbox"type="hidden" name="cardType" id="cardType" value="NA" >
		            <div>
		            	<a type = "submit" class="pay-now" id="subMe" onclick="return validateDetails();" >Pay now</a>
		            </div>
	            </div>
	            <div class="mt-20">
	            	<!-- <a href="downloadFile?merchant=M0000804" target="_blank">Terms and Conditions</a> -->
	            	<a href="downloadFile?merchant=M00094" target="_blank">Terms and Conditions</a>
	            </div>
            </form> 
    </div>	
    </div>
        <div class="footer-div">
        	<div>
        		<a href="http://seal.panaceainfosec.com/index.php?certid=CERTFE0E982CCB">
      				<img class="pci-logo" src="images/pci-logo.png">
      			</a>
        	</div>
        	<div class="ssl-logo-div">
        		<img class="ssl-logo" src="images/SSL-logo1.png">
        	</div>
        	<div>
        		<a href="https://pay.in/">
					<img class="pay-logo" src="images/Powered-by-pay.png">
				</a>
        	</div>
		</div>
 <script>
 
 function onlyalphabate(custName){
     var regexp =/^[A-Za-z ]+$/;
     if(regexp.test(custName)) {
            return true;
     }else{
         return false;
	 } 
 }

function validateDetails(e) {
	try{
		  var custMobile=document.getElementById("custMobile").value;
		  var amount=document.getElementById("amount").value;
		  var custName = document.getElementById("udf1").value.trim();
	
		  if(custMobile==null || custMobile=="") {
			  alert("Mobile Number cannot be blank");
			  document.getElementById("custMobile").value="";
			  return false;
		  } else if(custName==null || custName==""){
			  alert("Name cannot be blank");
			  document.getElementById("udf1").value="";
			  return false;
		  } else if(!onlyalphabate(custName)){
			  alert("Name should contain alphabets only");
			  document.getElementById("udf5").value="";
			  return false;
		  } else if(custMobile.length!=10) {
			  alert("Mobile Number should be of 10 digits");
			  document.getElementById("custMobile").value="";
			  return false;
		  } else if(amount==null || amount=="") {
			  alert("Amount cannot be blank");
			  document.getElementById("amount").value="";
			  return false;
		  }else if(amount.indexOf(".") == -1){
		      amount = amount + ".00"
              document.getElementById("amount").value=amount;
              return false;
          }else{
			  var link = "digidhan";
              document.getElementById("udf2").value=link;
              document.getElementById('paymentForm').submit();
              /* isLimitBreached(link); */
		  }
		}catch(err) {
			alert(err);
		}
	}

	/* function isLimitBreached(link){
        $.ajax ({
            type: 'POST',
            data: {link :link, merchantId : document.getElementById("merchantId").value},
            url: "${pageContext.request.contextPath}/ajaxAction?type=txnLimitCheck",

            success: function(result) {
                if(result != null && result !="") {
                    var obj = JSON.parse(result);
                    if(obj){
                        alert("You have reached the daily transaction limit. Kindly try again after 12AM.");
                    }
                    else{
                        document.getElementById('paymentForm').submit();
                    }
                }else{
                    alert("Oops, something went wrong");
                }
            },
            error: function (err) {
                alert("Oops, something went wrong");
            }
        });
    } */
	
	$('#amount').bind('change', function(e) {
		var amount=document.getElementById("amount").value;
		$.ajax ({
			type: 'POST',
		    data: {amount :amount},
			url: "${pageContext.request.contextPath}/ajaxAction?type=formatAmount",
			success: function(result) {
				//alert("result "+result);
				if(result != null && result !="") {
					document.getElementById("amount").value=result;
				}
			},
			error: function (err) {
				alert("Getting error : "+amount);
		    }
		});
	});

	
	function setInputFilter(textbox, inputFilter) {
		  ["input", "keydown", "keyup", "mousedown", "mouseup", "select", "contextmenu", "drop"].forEach(function(event) {
		    textbox.addEventListener(event, function() {
		      if (inputFilter(this.value)) {
		        this.oldValue = this.value;
		        this.oldSelectionStart = this.selectionStart;
		        this.oldSelectionEnd = this.selectionEnd;
		      } else if (this.hasOwnProperty("oldValue")) {
		        this.value = this.oldValue;
		        this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
		      } else {
		        this.value = "";
		      }
		    });
		  });
		}
		
		// Install input filters.
		setInputFilter(document.getElementById("custMobile"), function(value) {
		  return /^-?\d*$/.test(value); 
		  });
		var validNumber = new RegExp(/^\d*\.?\d*$/);
		var lastValid = document.getElementById("amount").value;
		function validateNumber(elem) {
		  if (validNumber.test(elem.value)) {
		    lastValid = elem.value;
		  } else {
		    elem.value = lastValid;
		  }
		}
</script>
</body>
</html>
