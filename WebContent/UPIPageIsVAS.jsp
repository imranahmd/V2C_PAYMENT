<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.rew.payment.utility.IndexVASDTO"%>
<%@page import="com.rew.pg.dto.MerchantDTO"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Vector"%>
<%@page import="com.rew.pg.dto.BankMaster"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.rew.pg.db.DataManager"%>
<%@page import="com.rew.pg.dto.TransactionMaster"%>
<%@page import="com.rew.payment.utility.PGUtils"%>
<%@page import="com.apps.trides.crypto.*"%>
<%@page import="com.rew.payment.utility.TripleKeyEnc"%>
<%@page import="java.io.* , java.net.*"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>

<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="java.text.DecimalFormat"%>

<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>HDFC Bank</title>
<link href="payfiVAS/css/jquery-ui.css" rel="stylesheet" type="text/css">
<link href="payfiVAS/css/style.css" rel="stylesheet" type="text/css">
<link href="payfiVAS/css/responsive.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="payfiVAS/css/font-awesome.css">
<link href="newcss/hdfcupicss.css" rel="stylesheet" type="text/css">
<!--<link href="newcss/hdfc_bootstrap.min.css" rel="stylesheet" type="text/css">  -->
<!-- <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"> -->
 <script src="payfiVAS/js/creditcard.js"></script>
 <script src="payfiVAS/js/sweetalert.min.js"></script>


<!--   <script src="payfiVAS/js/aes.js"></script> -->
  
  <!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">   -->
  
<style type="text/css">
.row {
  display: -ms-flexbox; /* IE10 */
  display: flex;
  -ms-flex-wrap: wrap; /* IE10 */
  flex-wrap: wrap;
  margin: 0 -16px;
}



 .input-hidden {
  position: absolute;
  left: -9999px;
}

input[type=radio]:checked + label>img 
{
  border: 1px solid #fff;
  box-shadow: 0 0 3px 3px #090;
}

/* Stuff after this is only to make things more pretty */
input[type=radio] + label>img 
{
  border: 1px dashed #444;
  width:50px;
  height:50px;
  transition: 500ms all;
    border-radius: 44px;
}

input[type=radio]:checked + label>img {
  trans: 
    rotateZ(-10deg) 
    rotateX(10deg);
}



#image-container {
   /*  width: 100%;
    position: relative;
    height: 42px;
    margin-bottom: -42px;
    line-height: 55px;
   
    margin-left: -22px;  */
    
  width: 100%;
  position: relative;
  height:42px;
  margin-bottom: 5px;
  line-height: 55px;  
}

#image-container img {
  position: absolute;
  right: 0;
  top: 59px;
}



/*card-content*/
.card-content select {

    border: 2px solid #c1c1c1;
    border-radius: 5px;
    font-size: 14px;
    font-family: 'Helvetica-Narrow-Regular', Arial;
    /* background: url(images/hdfc/icons/exp-date.png) 8% center no-repeat;  */
    width: 100%;
    padding: 18px 5% 18px 30%;
}
input, textarea, select {
    outline: none;
}

select {
    color: #75757e;
}
input, select {
    padding: 5px;
    border: 1px solid #e3ddd7;
}

.proceed_payment {
    text-align: center;
}

.custom-date { width: 28%; float: left; margin-right:1%; margin-top: 5px;}
.custom-date1 {
margin-right: 4%;
width: 18%;
float: left;
}

.custom-date2 {
width: 16%;
float: left;
margin-right: 4%;
margin-top: 5px;
}
.custom-year { width: 32%; float: left; margin-right:2%; margin-top: 5px;}
.cust-col3 {position:relative;}
.cvv_info{position: relative;width: 22px;float: left;margin-left: 3%;margin-right: 3%;}


</style>

<style type="text/css">
	.onoffswitch-label {
    display: block;
    overflow: hidden;
    cursor: pointer;
    border: 2px solid #999999;
    border-radius: 20px;
}

label {
    display: inline-block;
    max-width: 100%;
    margin-bottom: 5px;
    font-weight: 700;
}

#deleteicon
	{
	margin-left: 310px; 
	padding-left: 16px; 
	color : yellow;
	}
	.upiImage
{
	width: 14%;
    height: auto;
    padding: 5px;
}
.bqrImage
{
	width: 7%;
    height: auto;
    padding: 5px;
}
.arrow {
    /* top: 2pt; Uncomment this to lower the icons as requested in comments*/
    width: 0.4em;
    height: 0.4em;
    border-right: 0.2em solid black;
    border-top: 0.2em solid black;
    transform: rotate(133deg);
    margin: auto;
    margin-top: 10px;
}
	.upi-content
	{
		display:none;
	}
	.atomupi-content
	{
	display:none;
	}
	.bqr-content
	{
		display:none;
	}
	
	
	</style>
 <%
 
 Logger log = LoggerFactory.getLogger("JSPS.index.jsp");
	 
 String iv = "3ad5485e60a4fecde36fa49ff63817dc";
 
 response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
 response.setHeader("Pragma", "no-cache");
 response.setHeader("Expires", "0");
 response.setDateHeader("Expires", -1);
 response.setDateHeader("Last-Modified", 0);
 
 IndexVASDTO dataManager=new IndexVASDTO();
 
 	 log.info("Request mai kya aa raha hai "+request);
 	 
 	 String pgid=request.getParameter("MTrackid");
 	log.info("MTrackid "+pgid);
	
	 
	  dataManager=dataManager.getIndexVASDTOData(pgid);
	 
	 log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>>> DBconnextion object created ");
	String keyEncKey = dataManager.getKeyEncKey();
	
   	TransactionMaster TM=dataManager.getTM();
   	log.info("Kyu Nahi aa raha "+TM.getAmount());
     log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>> EmiDetails TransactionMaster object created ");
   	String checksum = "qbx6oPi3NwuBVf%2FloBLKIl9t%2B%2BFbNAVp6ICH6lGhcKM%3D";
   	/* request.setAttribute("checksum",checksum);
   	
   	log.info("index.jsp ::: Checksum in Request Attribute : "+checksum); */
   	
   	log.info("IndexVAS.java ::: pgid = "+pgid+" session.getAttribute(cToken)  "+(String)session.getAttribute("cToken"));
   	
   if(TM!=null){
   }
   
%>

<%-- <%	List returnList = new DataManager().getInstrumentBankList(TM.getMerchantId(),TM.getAmount()); %> --%>

<%    
		List returnList = dataManager.getReturnList();
	
		log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>>>First DBconnextion established list returnList fetched "+returnList.toString());
       Hashtable instrumentHash = (Hashtable)returnList.get(0);
       log.info("<<<<<<<<<<<<<<<<<<<<<< instrumentHash >>>>>>>>>>>>>>>>>>>> "+instrumentHash.toString());
       LinkedHashMap<String, String> sortedbankHash = (LinkedHashMap<String, String>)returnList.get(1);
	
    
	log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>>>second DBconnextion established list  MerchantPageConfige fetched ");
    String amount = TM.getAmount();
    String surcharge = TM.getSurcharge();
    log.info("amount"+amount);
    log.info("amount in double "+Double.valueOf(amount));
    log.info("surcharge"+surcharge);
    log.info("surcharge in double"+Double.valueOf(surcharge));
    double total = Double.valueOf(amount) + Double.valueOf(surcharge);
    log.info("total"+total);
    String subTotal = String.format("%.2f", total);
	log.info("total"+subTotal);
	
	
	
	
	
	
	
%>

<script type="text/javascript">
	
	var timer2 = "5:00";
	var interval = setInterval(function() {

  
	  var timer = timer2.split(':');
	  //by parsing integer, I avoid all extra string processing
	  var minutes = parseInt(timer[0], 10);
	  var seconds = parseInt(timer[1], 10);
	  --seconds;
	  minutes = (seconds < 0) ? --minutes : minutes;
	  if (minutes < 0) clearInterval(interval);
	  seconds = (seconds < 0) ? 59 : seconds;
	  seconds = (seconds < 10) ? '0' + seconds : seconds;
	  //minutes = (minutes < 10) ?  minutes : minutes;
	  $('.countdown').html(minutes + ':' + seconds);
	  timer2 = minutes + ':' + seconds;
	}, 1000);

</script>

</head>
<body>
<div id="preloader"></div>
<div class="container">
  <header class="header">
    <div class="wrapper"> <a href="index.html" class="logo"><img src="payfiVAS/images/logo.png"></a> </div>
  </header>
  <section>
  	<div class="spacer15"></div>
  	<div class="wrapperAd">
  		<% 
        	log.info("banner====merchant="+TM.getMerchantId());
        	String date = "2021-11-30 23:59:59";
        	String redirectionUrl = "http://hdfcbk.io/a/KY30Ya6";
        	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	Date when = format.parse(date);
        	List<String> merchantList = Arrays.asList("M00066","M00072");
        	boolean allowedMerchants =merchantList.stream().anyMatch(merchantId -> merchantId.equalsIgnoreCase(TM.getMerchantId()));
        	if(allowedMerchants && new Date().before(when)){
        	%>
        		<a href="<%=redirectionUrl%>" target="_blank" rel="noopener noreferrer">
        			<img src="images/campaning/1280x218.jpg" style="box-shadow: 0 0 5px 2px #cfd2dd;">
        		</a>
        	<% }%>
   	</div>
    <div class="wrapper">
      <div class="spacer15"></div>
      <div class="whiteBg">
        <div class="left">
          <div class="billingInfo">
            <h4 class="hideInMobile">Billing Information</h4>
            <%log.info("IndexVAS.java ::: pgid = "+pgid+" Billing information Started"); %>
            <div class="spacer20 hideInMobile"></div>
            <div class="heading rightside"> <img src="payfiVAS/images/icons/amount.png" class="icon">
              <%-- <p>Amount</p></span><%=TM.getAmount()%> --%>
              <table style="width: 100%;">
              <tr>
              <td>Base Amount</td>
              <td style="text-align: right;">
              	<div id="baseAmount"><%=TM.getAmount()%></div>
              </td>
              </tr>
              <tr><td>Transaction Fee</td>
              <td style="text-align: right;">
              	<div id="txnFee">0.00</div>
              </td>
              </tr>
              <tr><td>GST</td>
              <td style="text-align: right;">
              	<div id="gst">0.00</div>
              </td>
              </tr>
              <tr><td>Total Amount</td>
              <td style="text-align: right;">
              <div id="totalAmount"><%=subTotal
              %></div>
              </td></tr>
              </table>
              <!-- <h5 class="amount"><span class="rs">`</span>749</h5> -->
              <%-- <h5 class="amount"><span class="rs">`</span><%=TM.getAmount()%></h5> --%>
               <%--  <% if(TM.getMerchantId().equalsIgnoreCase(PGUtils.getPropertyValue("irctcid")))
                {%>
                	+ convenience fee
                <%} %> --%>
            </div>
            <div class="borb_fff hideInMobile"></div>
            <div class="heading"> <img src="payfiVAS/images/icons/order.png" class="icon">
              <p>Order No</p>
               <h4 class="orderNo"> <%=TM.getTxnId()%> </h4>
            
            </div>
          </div>
          <div class="otherInfo">
            <div class="heading"> <img src="payfiVAS/images/icons/merchant.png" class="icon">
              <p>Merchant</p>
              <%
              MerchantDTO merchantDTO=dataManager.getMT(); %>
              
              <span class="narrow bold"><%=merchantDTO.getName()%></span> </div>
              <%log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>>>third DBconnextion established getMerchant fetched "); %>
            <div class="borb_ccc hideInMobile"></div>
            <div class="heading"> <img src="payfiVAS/images/icons/website.png" class="icon">
              <p>Website</p>
              <a class="wordBreak1 narrow" rel="noopener noreferrer" href="<%=merchantDTO.getMerchWebsiteURL()%>" target="_blank"><%=merchantDTO.getMerchWebsiteURL()%></a>
              
               <%log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>>>fourth DBconnextion established getMerchWebsiteURL fetched "); %>
           
             </div>
             <div>
             <div class="borb_fff hideInMobile"></div>
            <%--  <% if(TM.getMerchantId().equalsIgnoreCase(PGUtils.getPropertyValue("irctcid")))
                {%>
                	<p><b>Convinience Fee</b></p>
                	<br>NIL , For all Domestic Debit Cards up to Rs. 1 Lakh
                	<br>1.0 % + Taxes for all domestic Credit Cards.
                <%} %> --%>
             </div>
          </div>
        </div>
        <div class="right">
        <div class="pay_options">
        <div class="upiLastScreen">
                          <div class="col-lg-8 col-md-8 col-xs-12 p-0 right-section">
                        	<div id="vpa-enter" class="right-section-inner vpa-enter-wrapper">

                            <div class="loading-note-wrapper">
                                <div class="icon"></div>
                                <div class="text">
                                    <span>Note:</span> Please do not press the back button or leave/refresh the screen until the payment is completet
                                </div>
                            </div>

                            <div class="upi-input-wrapper">

                                <div class="d-flex">
                                    <div class="upi-mobile-icon"></div>
                                    <div class="loading-description-text">
                                        Open your UPI ID linked app or click on the notification from your UPI ID linked app and approve the payment request
                                    </div>
                                </div>
                                <div class="loading-wrapper">
                                    <div class="lading-graph-wrapper">
                                        <div id="countdown">
  											<div id="countdown-number"></div>
  											<div style="margin-right:30px; color:darkgray">min</div>
  												<svg class="normal">
    												<circle r="58" cx="70" cy="70" class="inner"></circle>
    												<circle r="58" cx="70" cy="70" class="outer"></circle>
  												</svg>
											</div>	
                                        </div>
                                        <div class="payment-request-text">Please approve the payment request before the time runs out</div>
                                    </div>
                                    <div class="loading-wrapper-mobile" style="display:flex">
                                    	<div class="lading-graph-wrapper-mobile">
                                        <div id="countdownMobile" style="margin-right: -6px; margin-left:25px">
  											<div id="countdown-number-mobile"></div>
  											<div style="margin-right:30px; color:darkgray; text-align:center">min</div>
  												<svg class="mobile">
    												<circle r="38" cx="50" cy="50" class="inner-mobile"></circle>
    												<circle r="38" cx="50" cy="50" class="outer-mobile"></circle>
  												</svg>
											</div>	
                                        </div>
                                    	<div class="payment-request-text">Please approve the payment request before the time runs out</div>
                                    </div>
                                    <form method="post" id="MyForm" name="MyForm" action="">
										<input type="hidden" id="respDataID" name="respData" value="">
										<input type="hidden" id="merchantId" name="merchantId" value="">
										<input type="hidden" id="pgid" name="pgid" value="">
									</form>
         							<FORM ACTION=""  id="MyForm1" name="MyForm1"  METHOD='POST'>
										<input type="hidden" id="txnRefNo" name="txnRefNo" value="">
										<input type="hidden" id="respData" name="respData" value="">
									</FORM>
                                </div>

                            </div>
<!--                             <div class="cancel-btn-wrapper"> -->
<!--                                 <a href="#!" id="cancelPayment" data-toggle="modal" data-target="#exampleModalLong" >Cancel -->
<!--                                 Payment</a> -->
<!--                             </div> by sanidhya as per requirement   -->
                        	</div>
                    	</div>
                    	<div class="modal fade" id="exampleModalLong" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
                			<div class="modal-dialog modal-lg modal-dialog-centered" role="document">
                    			<div class="modal-content modal-content-space">
                        			<div class="modal-header">
                        			</div>
                        			<div class="modal-body cancel-modal-heading">
                           				 Are you sure you want to cancel this payment?
                        			</div>
                        			<div class="payment-cancel-modal-text">
                            			On cancelling this payment, the payment request will get cancelled only if you have not approved the payment on the UPI ID linked app
                        			</div>
<!--                         			<form> -->
<!--                         			<div class="modal-footer cancel-btn-wrapper"> -->
<!--                             			<button type="button" class="btn yes-cancel-btn" onclick="cancelPayment()">Yes, Cancel</button> -->
<!--                             			<button type="submit" class="btn yes-cancel-btn" data-dismiss="modal" formaction="RespCancelRequestHandler" disabled>Yes, Cancel</button> -->
<!--                             			<button type="button" class="btn no-nont-btn" onclick="closeModal()" disabled>No, Dont</button> -->
<!--                         			</div> -->
<!--                         			</form> by sanidhya as per requirement  -->
                    			</div>
                			</div>
            			</div> 
            			</div>
                    	</div>
      <div class="clear"></div>
    </div>
    <div class="spacer20"></div>
  </div>
     <form method="post" id="MyForm" name="MyForm" action="">
      <input type="hidden" id="respDataID" name="respData" value="">
      <input type="hidden" id="merchantId" name="merchantId" value="">
      <input type="hidden" id="pgid" name="pgid" value="">
  </form>
       
      <FORM ACTION=""  id="MyForm1" name="MyForm1"  METHOD='POST'>
              <input type="hidden" id="txnRefNo" name="txnRefNo" value="">
              <input type="hidden" id="respData" name="respData" value="">
      </FORM>
        </div>
        <div class="clear"></div>
      </div>
    </div>
  </section>
  <footer class="wrapper page-footer font-small brand-bg sticky-bottom ">
            <div class=" container footer-wrapper ">
                <a href="https://Epaisaa.in/" target="_blank" rel="noopener noreferrer">
                	<img src="images/one-logo.png" alt=" Powered by Epaisaa " class="logo" height="60" title=" Powered by 1 Pay " style="margin-top:-25px" />
                </a>
                <img src="images/multiple-logo.png" width="531" height="40" class="paymentgetway" />
            </div>
   </footer>
</div>
<script src="payfiVAS/js/jquery.min.js" type="text/javascript"></script> 
<script src="payfiVAS/js/jquery-ui.min.js" type="text/javascript"></script> 
<script src="payfiVAS/js/jquery-ui-timepicker-addon.min.js" type="text/javascript"></script> 
<script src="payfiVAS/js/common-se.js" type="text/javascript"></script> 
<script src="payfiVAS/js/selectivizr-ie7-css3-support.js" type="text/javascript"></script> 
<script src="payfiVAS/js/stripe.js"></script>

<script>
var amt=<%=TM.getAmount()%>;
console.log(amt);
var merId='<%=TM.getMerchantId()%>';
var transactionId = <%=TM.getId()%>;
var merchantTxnId = <%=TM.getTxnId()%>
var processId = <%=TM.getProcessId()%>;
var cardType="";
var instrumentId = "UPI";
var amount = amt;
var merchantId = merId;
var emioption= "NA";
var bank = "";

$(document).ready(function () {
	
	console.log("windows width:::"+screen.width)
   if (screen.width <= 768)
    {
	    $(".loading-wrapper").css('display','none');
        $(".loading-wrapper-mobile").css('display','flex');
    }
    else
    {
    	$(".loading-wrapper").css('display','flex');
        $(".loading-wrapper-mobile").css('display','none');
    }

});

$(".loading-wrapper-mobile").css('display','none');

function closeModal(){
	$('#exampleModalLong').css('display','none');
	$('#myModal').css('display','none');
};


if(screen.width <= 768){
	var countdownNumberElMobile = document.getElementById('countdown-number-mobile');
	var countdownMobile = 300;

	countdownNumberElMobile.textContent = countdownMobile;

	setInterval(function() {
		const minutes = Math.floor(countdownMobile / 60);
		  let seconds = countdownMobile % 60;

		  if (seconds < 10) {
		    seconds = "0"+seconds;
		  }	
		  countdownMobile = --countdownMobile <= 0 ? 300 : countdownMobile;

	  countdownNumberElMobile.textContent = minutes+":"+seconds;
	}, 1000);
}
else{
	var countdownNumberEl = document.getElementById('countdown-number');
	var countdown = 300;

	countdownNumberEl.textContent = countdown;

	setInterval(function() {
		const minutes = Math.floor(countdown / 60);
		  let seconds = countdown % 60;

		  if (seconds < 10) {
		    seconds = "0"+seconds;
		  }	
	  countdown = --countdown <= 0 ? 300 : countdown;

	  countdownNumberEl.textContent = minutes+":"+seconds;
	}, 1000);
}
</script>

<script type="text/javascript">
	var i = 0;
     var auto = setInterval(function ()
     {
    	 console.log("Inside the set Interval");
 			i++;
 			console.log(i);
 			
 			 if (i == 24 || i == 12)
 			{
 				checkVerification();
 			} 
 			
			<%
 			String merchantRefNo = (String)session.getAttribute("merchantRefNo"); // merchant transaction id column name txn_id in transaction_master
 			String mTrackId = (String)session.getAttribute("MTrackid"); // our transaction id column name id in transaction_master
 			amount = (String)session.getAttribute("amt");
 			
 			System.out.println("merchantRefNo ====="+merchantRefNo +"mTrackId========"+mTrackId + "amount==========="+amount );
 			%>
  		 $.ajax
	   	({				
	   		
	   		type: 'POST',
	   	    data: {mTrackId : '<%=mTrackId%>' , merchantRefNo : '<%=merchantRefNo%>' , amount : '<%=amount%>' , val_i : i},
	   		url: "<%=request.getContextPath()%>/ajaxAction?type=getStatusUPITransaction",
	   		
	   		success: function(result)
	   		{
	   			 
	   			if(result != null && result !="" && result!="N" )  /* && i == 30 condition in ajaxAction*/ 
	   			{
	   				var obj = $.parseJSON(result);
	   				clearInterval(auto);
	   				submitme(obj.url,obj.encRespXML,obj.merchantId,obj.pgid);
	   			}
	   		
	   		},
	   		error: function (err)
    		{
	   			console.log("Getting error : "+err);
	   		alert("Getting error : "+err);
    	    }
	   		
	   		
	   	});
		 
     }, 10000); 
</script>

<script type="text/javascript">
	function submitme(url,respData,mid,pgid)
	{
		if(mid.startsWith("M"))
		{
			document.getElementById("respDataID").value = respData;
			document.getElementById("merchantId").value = mid;
			document.getElementById("pgid").value = pgid;
			document.getElementById("MyForm").action = url;
			document.forms["MyForm"].submit();
			
		}
		else
			{
				document.getElementById("respData").value = respData;
				document.getElementById("txnRefNo").value = mid;
				document.getElementById("MyForm1").action = url;
				document.forms["MyForm1"].submit();
			}
	}
	
	// Verification call in 2 min space new requirement  edit shraddha 11 aug 2020
	function checkVerification()
	{
		<%
			String merchantRefNo2 = (String)session.getAttribute("merchantRefNo"); // merchant transaction id column name txn_id in transaction_master
			String mTrackId2 = (String)session.getAttribute("MTrackid"); // our transaction id column name id in transaction_master
			String amount2 = (String)session.getAttribute("amt");

		%>
 		 $.ajax
 	   	({				
 	   		
 	   		type: 'POST',
 	   	    data: {mTrackId : '<%=mTrackId2%>' , merchantRefNo : '<%=merchantRefNo2%>' , amount : '<%=amount2%>'},
 	   		url: "<%=request.getContextPath()%>/UpiVerification",
 	   		
 	   		success: function(result)
 	   		{
 	   			 //alert("result "+result);
 	   			if(result != null && result !="" && result!="N" )  /* && i == 30 condition in ajaxAction*/ 
 	   			{
 	   				var obj = $.parseJSON(result);
 	   				submitme(obj.url,obj.encRespXML,obj.merchantId,obj.pgid);
 	   			}
 	   		
 	   		},
 	   		error: function (err)
     		{
 	   		console.log("Getting error : "+err);
 	   			alert("Getting error : "+err);
     	    }
 	   		
 	   		
 	   	});
	}
	
// 	$('#cancelPayment').click(function(){
// 		$("#exampleModalLong").show();
	});
	
	
	serviceProvider(processId);
	
	var sp_name = null;
	
	function serviceProvider(processId)
	{
			$.ajax({				
				type: 'POST',
				url: "${pageContext.request.contextPath}/ajaxAction?type=serviceProviderSearch",
						
				data: {processId:processId},		
				success: function(data) {
					console.log("Service Provider Name---->"+data);
					sp_name = data;
					getSurchargeUPI();
				},
				error: function (err) {
					console.log("Error in deleteing merchant mdr.");
			    }
			});
	
	}
	
	
	function getSurchargeUPI()
	{
		var transactionId = transactionId;
		var cardType = "";
		var instrumentId = "UPI";
		var amount = amt;
		var merchantId = merId;
		var emioption= "NA";
		var bank = "";
		
		if(sp_name=="Atom UPI"){
			bank = "ATOMUPI";
			cardType = "ATOMUPI";
		}else{
			bank = "UPI";
			cardType = "UPI";
		}
		
		console.log("Service Provider Name:::"+sp_name);
		
		  

		 $.ajax
		({				
			
			type: 'GET',
		    data: {transactionId :transactionId,cardType:cardType,instrumentId:instrumentId,amount:amount,merchantId:merchantId,emioption:emioption,bank:bank},
			url: "<%=request.getContextPath()%>/SurchargeCalculation",
			
			success: function(result)
			{
				if(result != null && result !="") 
				{
					var obj = $.parseJSON(result);
					//alert(result);
					$('#txnFee').text(obj.txnFee);
					$('#gst').text(obj.gst);
					$('#totalAmount').text(obj.total);
					$('#totalAmtupi').text(obj.total);
				}
			
			},
			error: function (err)
			{
				console.log("Getting error : "+err);
				alert("Getting error : "+err);
		    }
		});
	}

</script>

<script>

function cancelPayment(){
		<%
		String merchantRefNo3 = (String)session.getAttribute("merchantRefNo"); // merchant transaction id column name txn_id in transaction_master
		String mTrackId3 = (String)session.getAttribute("MTrackid"); // our transaction id column name id in transaction_master
		String amount3 = (String)session.getAttribute("amt");
		
		System.out.println("merchantRefNo ====="+merchantRefNo +"mTrackId========"+mTrackId + "amount==========="+amount );
		%>

	 $.ajax
   	({				
   		
   		type: 'POST',
   	    data: {mTrackId : '<%=mTrackId2%>' , merchantRefNo : '<%=merchantRefNo3%>'  , amount : '<%=amount3%>'},
   		url: "<%=request.getContextPath()%>/ajaxAction?type=cancelUPITransaction",
   		
   		success: function(result)
   		{
   			 //alert("result "+result);
   			if(result != null && result !="" && result!="N" )  /* && i == 30 condition in ajaxAction*/ 
   			{
   				var obj = $.parseJSON(result);
   				submitme(obj.url,obj.encRespXML,obj.merchantId,obj.pgid);
   			}
   		
   		},
   		error: function (err)
		{
			
   			console.log("Getting error : "+err); 
   			alert("Getting error : "+err);
	    }
   		
   		
   	});
	
}

function submitme(url,respData,mid,pgid)
{
	document.getElementById("respDataID").value = respData;
	document.getElementById("merchantId").value = mid;
	document.getElementById("pgid").value = pgid;
	document.getElementById("MyForm").action = url;
	document.forms["MyForm"].submit();
}

</script>




               

<% log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>> javascript validation starts  ");%>
<% log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>> javascript validation ends  ");%>

<div id="ui-datepicker-div" class="ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all"></div>
<form method="post" id="MyForm" name="MyForm" action="">
		<input type="hidden" id="respDataID" name="respData" value="">
		<input type="hidden" id="merchantId" name="merchantId" value="">
		<input type="hidden" id="pgid" name="pgid" value="">
	</form>
<%log.info("Index page loaded successfully for Index vas id="+pgid);%>
</body>
</html>
