<%@page import="java.util.Set"%>
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

<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>

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
  
<style>

/* Create four equal columns that floats next to each other */
.columnDiv {
  float: left;
  width: 15%;
  padding: 10px;
 /* Should be removed. Only for demonstration */
}

/* Clear floats after the columns */
.rowDiv:after {
  content: "";
  display: table;
  clear: both;
}
</style>  
  
<style type="text/css">

.chb {
  width: 1.3em;
    height: 1.3em;
    background-color: white;
    border-radius: 50%;
    vertical-align: middle;
    border: 1px solid #ddd;
    appearance: none;
    -webkit-appearance: none;
    outline: none;
    cursor: pointer;

/*   width:25px;
  height: 25px;
  border-radius: 50%;
  vertical-align: middle;
  border: 1px solid black;
  appearance: none;
  -webkit-appearance: none;
  outline: none;
  cursor: pointer;
   */
}
/* .chb {
  appearance: auto;
  clip-path: circle(45% at 50% 50%);
  background-color: green;
} */

.chb:checked {
 appearance: auto;
    background-color: gray;
    clip-path: circle(45%);
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
	margin-top: -40px;
    text-align: center;
}

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
	@keyframes countdown {
   from {
     stroke-dashoffset: 178px;
   }
   to {
    stroke-dashoffset: 357px;
   }
   }
	
	#countdown-main{
	  position: relative;
	  margin: auto;
	  height: 100px;
	  width: 100px;
	  text-align: center;
	}
	.normal {
  position: absolute;
  top: 0;
  right: 0;
  width: 140px;
  height: 140px;
  transform: rotateY(-180deg) rotateZ(-90deg);
}
svg .inner-main {
  stroke: lightgray;
  fill: none;
  stroke-width: 7px;
  
}

svg .outer-main {
  stroke-dasharray: 360px;
  stroke-dashoffset: 0px;
  stroke-linecap: round;
  stroke-width: 7px;
  stroke: #78c778;
  fill: none;
  animation: countdown 840s linear infinite forwards;
}
	</style>
	
	<style>
	.round {
  position: relative;
}

.round label {
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 50%;
  cursor: pointer;
  height: 24px;
  left: 0;
  position: absolute;
  top: 0px;
  width: 24px;
}

.round label:after {
  border: 2px solid #fff;
  border-top: none;
  border-right: none;
  content: "";
  height: 6px;
  left: 6px;
  opacity: 0;
  position: absolute;
  top: 6px;
  transform: rotate(-45deg);
  width: 12px;
}

.round input[type="checkbox"] {
  visibility: hidden;
}

.round input[type="checkbox"]:checked + label {
  background-color: #66bb6a;
  border-color: #66bb6a;
}

.round input[type="checkbox"]:checked + label:after {
  opacity: 1;
}

</style>

<style>
.dot {
  height: 5px;
  width: 5px;
  background-color: black;
  border-radius: 50%;
   display: inline-block; 
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
 String sCaptcha = PGUtils.RandomCaptcha(6);

  
 	
   String pgid=request.getAttribute("pgid").toString();
   
   log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>>> started ");
	log.info("(pgid)==>" +request.getAttribute("pgid").toString());
	session.setAttribute("cancelpgid",request.getAttribute("pgid").toString());
	
	 
	  dataManager=dataManager.getIndexVASDTOData(pgid);
	 
	 log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>>> DBconnextion object created ");
	String keyEncKey = dataManager.getKeyEncKey();
	
   	TransactionMaster TM=dataManager.getTM();
   	log.info("Kyu Nahi aa raha "+TM.getAmount());
     log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>> EmiDetails TransactionMaster object created ");
   	String checksum = request.getAttribute("checksum").toString();

   	String mappedinstrumentid = request.getAttribute("instrumentId").toString(); 
   	
   	/* request.setAttribute("checksum",checksum);
   	
   	log.info("index.jsp ::: Checksum in Request Attribute : "+checksum);
   	
   	*/
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
  		
  		//TreeMap sortedbankHash=new TreeMap();
 		//sortedbankHash.putAll(bankHash);
 		
 		log.info("sorted list");
  		
       //**************[Start Code added by Sonu for EMI Calculation]
       List<String> MerchantPageConfige = dataManager.getMerchantPageConfige();
       List<String> banklist=dataManager.getBanklist();
       //**************[END Code added by Sonu for EMI Calculation]
    		   
     log.info("sortedbankHash"+sortedbankHash);
     ArrayList<String> walletlist2=dataManager.getWalletlist2();
    		   
     ArrayList<String> walletlist1=dataManager.getWalletlist1();
	 log.info("walletlist:::::::>>>>> "+walletlist1.size());	
	 
	 
	 ArrayList<String> ecmList=dataManager.getEcmsBanks();
	
    
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
        	String redirectionUrl = "http://dsads.io/a/KY30Ya6";
        	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	Date when = format.parse(date);
        	List<String> merchantList = Arrays.asList("M00066","M00072");
        	// local system as below
        	 //lambda code for NetMagic
       	boolean allowedMerchants =merchantList.stream().anyMatch(merchantId -> merchantId.equalsIgnoreCase(TM.getMerchantId()));
        
        // UAT as below Lamda will not work in Old UAT Server
        //lambda converted into java code
      // boolean allowedMerchants = TM.getMerchantId().equals("M00066") || TM.getMerchantId().equals("M00072");	
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
               <div id="countdown-main">
  											<div id="countdown-number" style="font-size:unset;margin-right:36px"></div>
  											<div style="margin-right:38px; color:darkgray">min</div>
  												<svg class="normal">
    												<circle r="30" cx="70" cy="70" class="inner-main"></circle>
    												<circle r="30" cx="70" cy="70" class="outer-main"></circle>
  												</svg>
											</div>
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
            <div class="payment_dropdown">
              <div class="stage">
                <ul class="payment_mode"  id="payment_mode_drop_down">
                  <li id="pay_with" class="pay_with">Pay with</li>
                </ul>
              </div>
              <div class="drop_options" id="payment_mode_drop_down2">
                <div class="caret"></div>
                <ul class="payment_mode"  id="payment_mode_drop_down2" >
              
                 
                 
                 <%
                      log.info("--------------------mapped--instrument--Id--to--check"+mappedinstrumentid);
                       if((instrumentHash.containsKey("CC") && mappedinstrumentid.equals("NA")) || (mappedinstrumentid.equals("CC") && instrumentHash.containsKey("CC")) ){  
                	 
                	%> 
                	 <li class="c-card"><img src="payfiVAS/images/dropdown/credit-card.png">HDFC Bank Credit Card</li>
                	 <li class="oc-card"><img src="payfiVAS/images/dropdown/other-credit-card.png">Other Bank Credit Card</li>
                	 <% }
                	  if((instrumentHash.containsKey("DC") && mappedinstrumentid.equals("NA")) || (mappedinstrumentid.equals("DC") && instrumentHash.containsKey("DC"))){%>
                     <li class="d-card"><img src="payfiVAS/images/dropdown/debit-card.png">HDFC Bank Debit Card</li>
                     <li class="od-card"><img src="payfiVAS/images/dropdown/other-credit-card.png">Other Bank Debit Card</li>
                    <%} 
                	  if ((instrumentHash.containsKey("NB") && mappedinstrumentid.equals("NA")) || (mappedinstrumentid.equals("NB") && instrumentHash.containsKey("NB")) ){%>
                     <li class="netbanking"><img src="payfiVAS/images/dropdown/netbanking.png" >Net Banking</li>
                     <%}
                	  if ((instrumentHash.containsKey("UPI") && mappedinstrumentid.equals("NA")) || (mappedinstrumentid.equals("UPI") && instrumentHash.containsKey("UPI")) ){%>
               	   <li class="upi"><img src="images/upi.png" style="max-width:10%">UNIFIED PAYMENT INTERFACE (UPI)</li>
               	   <% }
               	   if ((instrumentHash.containsKey("ATOMUPI") && mappedinstrumentid.equals("NA")) || (mappedinstrumentid.equals("ATOMUPI") && instrumentHash.containsKey("ATOMUPI")) ){%>
                 	   <li class="atomupi"><img src="images/upi.png" style="max-width:10%">UNIFIED PAYMENT INTERFACE (UPI)</li>
                 	   	<% }
                 if ((instrumentHash.containsKey("WALLET") &&mappedinstrumentid.equals("NA")) || (mappedinstrumentid.equals("WALLET") && instrumentHash.containsKey("WALLET")) ){%>
                     <li class="wallet1"><img src="images/PayZapp-logo2.png" style="max-width:8%">PAYZAPP WALLET</li>
					<li class="wallet2"><img src="payfiVAS/images/dropdown/wallet.png" style="max-width:8%">OTHER WALLET</li>
					
					<%}
                	  if ((instrumentHash.containsKey("BQR") && mappedinstrumentid.equals("NA")) || (mappedinstrumentid.equals("BQR")) ){%>
                     <li class="bqr"><img class="bqrImage" src="images/bharat_qr-logo.png">BHARAT QR</li>
<%-- 					<%} --%>
<%--                 	  if (instrumentHash.containsKey("UPI")){%> --%>
<!--                 	   <li class="upi"><img src="images/upi.png" style="max-width:10%">UNIFIED PAYMENT INTERFACE (UPI)</li> -->
<%--                 	   <% } --%>
<%--                 	   if (instrumentHash.containsKey("ATOMUPI")){%> --%>
<!--                   	   <li class="atomupi"><img src="images/upi.png" style="max-width:10%">UNIFIED PAYMENT INTERFACE (UPI)</li> -->
                  	   	
                  	   	<%}
             
         			  // if (instrumentHash.containsKey("EMI")){
         				if(null==MerchantPageConfige){}
         				else{
         			 if(MerchantPageConfige.get(8).equals("1") && mappedinstrumentid.equals("NA") || (mappedinstrumentid.equals("EMI") && MerchantPageConfige.get(8).equals("1")) ){%>
                      	<li class="emi"><img src="payfiVAS/images/dropdown/emi.png">EMI</li>
                  		 <%}
         	
     				 } //} 
         			 log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>>> Dropdown option loaded  ");
                
                  	 
                	  if ((instrumentHash.containsKey("OP") && mappedinstrumentid.equals("NA")) || (mappedinstrumentid.equals("OP") && instrumentHash.containsKey("OP")) ){%>
     					<li class="Offline"><img src="payfiVAS/images/dropdown/offline.png">Offline Payment</li>
             			<%}
                	  %>
             
<!--         			  // if (instrumentHash.containsKey("EMI")){ -->
<!--          				if(null==MerchantPageConfige){} -->
<!--           				else{ -->
<%-- <%--          			 if(MerchantPageConfige.get(8).equals("1")){%> --%> 
<!-- <!--                       	<li class="emi"><img src="payfiVAS/images/dropdown/emi.png">EMI</li> --> 
<%-- <%--                   		 <%} --%> 
         	
<!--       				 } //}  -->
<!--         			 log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>>> Dropdown option loaded  "); -->
<!--                  %> -->
                  
                </ul>
              </div>
            </div>
            <div class="proceed_payment">
              <!-- <form id="pay_info" action="PGRouter" method="post"> -->
              
            <!--  EMI Start -->

                <div class="cc-emi">

                  <h4>1. Select card issued bank:</h4>

                  <div class="drop_down">

                    <div class="stage1" id="bank">

                      <ul class="payment_modes">

                        <li>Select bank </li>

                      </ul>

                    </div>

                    <div class="dropdown_options" id="bank_opt">

                      <div class="caret"></div>

                      <ul id="bank_id" class="payment_modes">

                      <!--   <li>HDFC</li> -->
                      	<%
					
					for(int i=0;i<banklist.size();i++){%>
                        <li><%=banklist.get(i)%></li>
                        
						<%} %>

                      </ul>


                    </div>

                   </div>
 <%-- <%    

       //****************[Start Code added by Sonu]*********************

    Map<String, List<String>> EmiDetails = dataManager.getEmiDetails(TM.getAmount(), "HDFC");
    log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>>>fifth DBconnextion established EmiDetails fetched ");
%>      
    --%>
                  

                  <div class="emi-table">

					<h4>2. Select EMI Option</h4>
                    <table  border="0" width="100%">

                      <tr>

                        <th>&nbsp;</th>

                        <th>EMI Tenure</th>

                        <th>Bank Interest Rate</th>

                        <th>Monthly Installments</th>

                        <th>Interest paid to Bank</th>

                      </tr>

                      <tr>

                        <td><input type="radio" name="select-emi" id="3m-emi"  value=""></td><!-- onclick="checkradio(this.value) -->

                        <td>3 Months</td>

                       
                        <td id="3mrate"></td>

                        <td id="3memi"></td>

                        <td id="3mbankcharges"></td>
                        
                        <!-- <td>14%</td>

                        <td>Rs. 59.8</td>

                        <td>Rs. 51.6</td> -->

                      </tr>

                      <tr>

                        <td><input type="radio" name="select-emi" id="6m-emi" value=""></td>

                        <td>6 Months</td>

                       <td id="6mrate"></td>

                        <td id="6memi"></td>

                        <td id="6mbankcharges"></td>

                      </tr>

                      <tr>

                        <td><input type="radio" name="select-emi" id="9m-emi" value=""></td>

                        <td>9 Months</td>

                        <td id="9mrate"></td>

                        <td id="9memi"></td>

                        <td id="9mbankcharges"></td>

                        <!-- <td>14%</td>

                        <td>Rs. 115.25</td>

                        <td>Rs. 25.5</td> -->

                      </tr>

                      <tr>

                        <td><input type="radio" name="select-emi" id="12m-emi" value=""></td>

                        <td>12 Months</td>

                        <td id="12mrate"></td>

                        <td id="12memi"></td>

                        <td id="12mbankcharges"></td>

                        <!-- <td>14%</td>

                        <td>Rs. 226.83</td>

                        <td>Rs. 14.49</td> -->

                      </tr>

                    </table>

                     <%  
						
       //****************[End Code added by Sonu]*********************

                                %> 
                  
                  <p><strong>Note: </strong>In case of any kind of refund in EMI transaction, interest already billed in a particular transaction will not be refunded. </p>
                  <p><strong>Tax will be applicable on the interest paid to bank.</strong> </p>
                  
                   <p><strong><font color="red">Convenience Fee of Rs 199 + GST applicable for EMI transactions on HDFC Bank Cards.</font></strong></p>
                  <!-- <p>
                    <input type="checkbox" checked="true">
                    &nbsp; I agree to terms governing EMI transaction as provided <a href="#">here</a>.</p> -->
                  <h4>3. Enter Card Details</h4>
                 <% log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>> EmiDetails table ended ");%>
                 
                                  </div> 
                                  
                                  <!-- Atom Emi Integration Div Start -->
                 <div id='atomEMI' style="display: none;">
                 <form action="PGRouter" method="post"  id="EMICCForm" class="datpayment-form"> 
                
                 <input type="hidden" name="instrumentId" value="CC">
                         <input type="hidden" class='atombankName' name="bank" id="bank" value=""> 
						<input type="hidden" name="txnId" id="txnId" value="<%=request.getAttribute("pgid").toString()%>">
						<input type="hidden" name="keyEncKey" value="<%=keyEncKey%>">
						<input type="hidden" name="cardType" class="dc" id="emicardType" value=""/>
						<input type="hidden" name="checksum" value="<%=checksum%>">
						<input type="hidden" pattern="[0-9]{10}" class="phone-input name-field" maxlength="10" id="phone" required="number" placeholder="Phone" />
						<input type="hidden" class="mail-input email-field"  placeholder="Email ID"  id="email" required>						
						<input type="hidden" id="emiexpiry" name="expiry"  />   
						<input type="hidden" id="cNameemi" name="cName" >	
						<input type="hidden" id="emioption" name="emioption" value="emi">		
							  
				 <div class="other_bank_btn">
                    <div class="btn">
                         <button type="submit" class="red-btn">Submit Details</button> 
                     <!--  <button type="submit" class="red-btn">confirm Payment</button>    --> <!-- disabled="disabled" -->
                     <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler" >cancel</button>  
                    </div>
                  </div>
			          </form>
                 </div>
                  <!-- Atom Emi Integration Div Start -->
                                  
                </div>
              <!-- EMI END   -->
                
                
                <div class="clear"></div>
        
             <!--  Start CC   -->
                <input type="hidden" name="merchantName" id="merchantName"  value="<%=merchantDTO.getName()%>">
                <%   	 	Cookie cookie = null;
                Cookie[] cookies = null;
                String mobile= "";
           	 	String email= ""; 
                cookies = request.getCookies();
                
                if(cookies != null ) {
               	
                   for (Cookie c : cookies) {
                       String tname = c.getName();
                          
                       if (tname.equals("mobileNo")) {
                            mobile = c.getValue();
                       } if (tname.equals("emailId")) {    
                            email = c.getValue();
                       }
                   }
                } 
           //     out.print(mobile+" "+email);      
       %>
         <input type="hidden" name="mobileNoFromCookie" id="mobileNoFromCookie" value="<%=mobile%>">
          <input type="hidden" name="emailIdFromCookie" id="emailIdFromCookie" value="<%=email%>">
            
              <div class=" " id="cookieexist" style="display: none"> <br>        
              <button type="button" class="btn btn-primary" id="backBtn3" style="display: inline-block;
    /* width: 50%; */
    border: none;
    background: white;
    color: gray; float: left;" ><font size="2px;">🡨Back</font></button>  
                <%
             
                
                /*    
                String rawCookie = request.getHeader("Cookie");
               // out.print("rawCookie:::"+rawCookie);
                String[] rawCookieParams = rawCookie.split(";");
               // out.print("rawCookie:::"+rawCookieParams[0]+" ");
                for(String rawCookieNameAndValue :rawCookieParams)
                {
                  String[] rawCookieNameAndValuePair = rawCookieNameAndValue.split("=");
            //    out.print("value:::"+rawCookieNameAndValuePair[0]+" "+rawCookieNameAndValuePair[1]);
                  if (rawCookieNameAndValuePair[0].trim().equalsIgnoreCase("mobileNo")) {	
                	 mobile = rawCookieNameAndValuePair[1];
                	
                }  if (rawCookieNameAndValuePair[0].trim().equalsIgnoreCase("emailId")) {     
                	email =rawCookieNameAndValuePair[1];
                }
                }  */
       
            
            
            
          /*   URL url = new URL(request.getRequestURL().toString());
            URLConnection conn = url.openConnection();
     
            conn.getContent(); */
/* 
            CookieManager cm = new CookieManager();
            CookieHandler.setDefault(cm);
            cm.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            CookieStore cs = cm.getCookieStore();
            List <HttpCookie> cookies = cs.getCookies();
            for (HttpCookie cookie: cookies) {
                      log.info("CookieHandler retrieved cookie: " + cookie);
            }
             
        */
        

            if(mobile!= null && email!= null){
            	%>
            <br>
           
              <form id="payinfoformwithcookie" action="PGRouter" method="post"> 	
					
						<input type="hidden" name="tokenRefId" id="wctokenRefId" value="">
							 
<!-- 			 		 	 <input type="hidden" name="cardNo" id="decCardNoDC" value="">
 -->						 <input type="hidden" name="instrumentId" id="wcinstrumentId" value="">
						 <input type="hidden" name="txnId" id="txnId" value="<%=request.getAttribute("pgid").toString()%>">
						 <input type="hidden" name="checksum" value="<%=checksum%>">
						 <input type="hidden" name="cardType" id="wccardType" class="dc"  value=""/>
						 <input type="hidden" pattern="[0-9]{10}" class="phone-input name-field" maxlength="10" id="phone"  placeholder="Phone" value=<%= mobile %>/>
						 <input type="hidden" class="mail-input email-field"  placeholder="Email ID"  id="email"  value=<%= email %>>
						 <input type="hidden" class="mail-input email-field"  id="wcvalue_of_iCC" >
						
						 <input type="hidden" id="wcExpiry"  name="expiry" maxlength="" value="" >
<!-- 						 <input type="hidden" name="cardNo" id="card-numberSCDC">
 -->						 <input type="hidden" name="cardName" id="wccard-holder"/>
						 <input type="hidden" name="cvv" id="wccvv"/>
				  <input type="hidden" name="cryptogram" id="wccryptogram"/>
						  <input type="hidden" name="panlast4" id="wcpanlast4"/>
						        <input type="hidden" name="MId" id="MId"  value="<%=TM.getMerchantId()%>"> 
						           <input type="hidden" name="customerId" id="wccustomerId">        
						  
			
            	
			<%	
            	Vector<List<String>>  storedCardDetail= new DataManager().getStoredCardsVAS(email, mobile,  TM.getMerchantId());
				
//				log.info("storedCardDetail::::"+storedCardDetail.size());
			if(storedCardDetail.size()>0){
				
		
				 for( int i = 0 ; i < storedCardDetail.size(); i++) 
                 {  
					  
                 %>		  
						   
<!-- 			<br>	
 -->			<input type="hidden" class="" id="wcnoOfCard" name="noOfCard" value= "<%=storedCardDetail.size()%>"> 

	<div id="wccr_<%=i%>" style="border: 1px solid #54a8ec;
  padding: 8px;
  border-radius: 20px; margin-top: 20px;">
	<div class="rowDiv">
  <div class="columnDiv" style="box-sizing: border-box; width: 10%; ">
           <div class="round">
    <input type="checkbox" style="text-align: center; visibility: visible; transform: scale(1.2) ; accent-color: #3c8c4b;" class="chb" name="chb" id="chb_<%=i%>" 
    onclick="return storedcardFillTokenizeWC('<%=i%>','<%=storedCardDetail.get(i).get(9)%>',
                           '<%=storedCardDetail.get(i).get(11)%>','<%=storedCardDetail.get(i).get(2)%>',
                            '<%=storedCardDetail.get(i).get(3)%>','<%=storedCardDetail.get(i).get(4)%>','<%=storedCardDetail.get(i).get(8)%>',
                            '<%=keyEncKey%>','<%=storedCardDetail.get(i).get(5)%>','<%=storedCardDetail.get(i).get(6)%>',
                            '<%=storedCardDetail.get(i).get(7)%>','<%=storedCardDetail.size()%>', '<%=storedCardDetail.get(i).get(12)%>', 
                            '<%=storedCardDetail.get(i).get(10)%>', '<%=storedCardDetail.get(i).get(15)%>')" />
<!--     <label for="checkbox"></label>
 -->  </div>
  </div>
  
  <div class="columnDiv" style="box-sizing: border-box; ">
  <%
  String cardT= null;
  if(storedCardDetail.get(i).get(4).equals("MasterCard")){
	  cardT= "images/Master.png";
  }else if(storedCardDetail.get(i).get(4).equals("Visa")){
	  cardT= "images/visa-card.png";
  }else if(storedCardDetail.get(i).get(4).equals("Rupay")){
	  cardT= "images/RuPay-card.png";
  }else if(storedCardDetail.get(i).get(4).equals("DinersClub")){
	  cardT= "images/dinner.png";
  }
  %>
     <div class="" style=""><span><img src= "<%=cardT %>"></span> </div>
  </div>
  
    <div class="columnDiv" style="box-sizing: border-box; width: 27%">
     <div class="" style="">&#9679; &#9679; &#9679; &#9679;<span style="margin-left: 5px;"><%= storedCardDetail.get(i).get(10) %></span> </div>
  </div>
  
  
  <div class="columnDiv" style="box-sizing: border-box">
  <input type="password" class="" id="wccvv_<%=i%>" name="cvv" placeholder="CVV" maxlength="4" style="width: 50px; border: 1px solid #ccc;
    border-radius: 4px; visibility: hidden; " > 
  </div>
 
  <div class="columnDiv" style="box-sizing: border-box; ">
     <div class="" style=""><span><%= storedCardDetail.get(i).get(2) %></span> </div>
  </div>
  <div class="columnDiv" style="box-sizing: border-box;">
   <div class="" onclick="deleteCardWC('<%=i%>','<%=storedCardDetail.get(i).get(14) %>','<%=storedCardDetail.get(i).get(9) %>',
  '<%=storedCardDetail.get(i).get(4) %>','<%=storedCardDetail.get(i).get(13) %>', '<%=storedCardDetail.size()%>')" style="background-color: white;color: red; border: none; cursor: pointer;" >
  <!-- <i class="fa fa-trash-o" style="font-size:22px;color:red"> </i>--><img src="images/deleteimg.png"></div>
  </div>
</div></div>		 
             <%    }		%>
<br>
 <div class="capacha col2" id="captchaDiv1">
                    <p class="subHead">Enter the characters visible in the box below</p>
                    <div class="column2 marr8p">
                      <input type="text" placeholder="Enter the characters" id="capacha1" maxlength="7" style="background: none !important;
                      border: 2px solid #c1c1c1; border-radius: 5px; height: 45px;">
                    </div>
                   <!--  <div class="column2 capacha-img"> <img src="payfiVAS/images/capacha.jpg"> </div> -->
                   
                   <div class="column2 capacha-img"

                       style="background-image: url(payfiVAS/images/capacha.jpg); height: 65px; width: 250px; text-align: center;">

                        <font color="maroon" size="7" face="Courier New"><i id ="lblCaptcha1"><%=sCaptcha%></i></font>

                   </div>
                    <div onclick="refreshCaptcha1()" style="padding-top: 14%;padding-bottom: 4%;padding-left: 55%; color: blue;cursor: pointer;text-decoration: underline; margin-right: 0px;">Refresh Captcha</div>
                  </div>
   <div class="btn" id="button1">
                       <button id="payNow" class="red-btn"  onclick="return ValidateCVVSaveCardWC();" >Pay Now</button> 
                      <%-- <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler?txnId=<%=request.getAttribute("pgid").toString()%>" >cancel</button>   --%>
                        <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler" >cancel</button>  
                    
                    </div><%	}  }
            
          %>
  </form>
  
</div> 
 
   <div class=" " id="cookienotexist" style="display: none"><br>         
          <button type="button" class="" id="backBtn4" style="display: inline-block;
    /* width: 50%; */
    border: none;
    background: white;
    color: gray; float: left;" ><font size="2px;">🡨Back</font></button>     
             <br>            
              <form id="payinfoformwithoutcookie" action="PGRouter" method="post"> 	
  		
						<input type="hidden" name="tokenRefId" id="woctokenRefId" value="">
							 
<!-- 			 		 	 <input type="hidden" name="cardNo" id="decCardNoDC" value="">
 -->						 <input type="hidden" name="instrumentId" id="wocinstrumentId" value="">
						 <input type="hidden" name="txnId" id="txnId" value="<%=request.getAttribute("pgid").toString()%>">
						 <input type="hidden" name="checksum" value="<%=checksum%>">
						 <input type="hidden" name="cardType" id="woccardType" class="dc"  value=""/>
						 <input type="hidden" pattern="[0-9]{10}" class="phone-input name-field" maxlength="10" id="phone" required="number" placeholder="Phone" />
						 <input type="hidden" class="mail-input email-field"  placeholder="Email ID"  id="email" required>
						 <input type="hidden" class="mail-input email-field"  id="wocvalue_of_iCC" >
						
						 <input type="hidden" id="wocExpiry"  name="expiry" maxlength="" value="" >
<!-- 						 <input type="hidden" name="cardNo" id="card-numberSCDC">
 -->						 <input type="hidden" name="cardName" id="woccard-holder"/>
						 <input type="hidden" name="cvv" id="woccvv"/>
				  <input type="hidden" name="cryptogram" id="woccryptogram"/>
						  <input type="hidden" name="panlast4" id="wocpanlast4"/>
						        <input type="hidden" name="MId" id="MId"  value="<%=TM.getMerchantId()%>"> 
						           <input type="hidden" name="customerId" id="woccustomerId">         
						  
		 <div class=" " id="cookienotexist1" ></div> 
			
           </form>   	
		

   
</div> 
                   <div class=" " id="verifyDiv" style="display: none; ">
                   
                   <br><br><br>
                         <div id="detailVarify">
<!--                              <input type="checkbox" name="isRemember" id="isCCRemember" value="Y" checked/>
 -->                         <button type="button" class="btn btn-primary" id="backBtn1" style="display: inline-block;
    /* width: 50%; */
    border: none;
    background: white;
    color: gray; float: left;" ><font size="2px;">🡨Back</font></button>
                             <span style="max-inline-size: fit-content;float: left; margin-left: 35px;font-size: 15px; font-weight: bold;">Access saved cards</span>
                             <br>
                             <span style="float: left;  margin-left: 78px; font-size: 13px; margin-top: 5px; color: #999999;">Enter your registered details for verification</span>
                        
                      
                       
                        <br><br>
                        <div style="margin-top: 2%; margin-bottom: 2%; " id="verifyEmailMobile">
                           
                        <div class="style1" style="display: inline-block; float: left; margin-left: 60px; width: 40%;">
  <label style="float: left; margin-left: 8%; font-size: 13px;">Mobile*</label>
  <br>
  <input type="text" name="mobileNoVerify" id="mobileNoVerify" value="" maxlength="10" style=" padding:17px; /* width: 20%;  */height: 2px; margin-left: 8%; border-radius: 5px; border: 2px solid #ccc; " >
  </div>
  <div class="style1" style="display: inline-block;/* width: 45%; */ ">
  <label style="float: left; margin-left: 2px; font-size: 13px;">Email*</label>
  <br>
  <input type="text" name="emailIdVerify" id="emailIdVerify"  value="" style=" padding:17px; /* width: 75%; */  height: 2px; margin-left: 0%; border-radius: 5px; border: 2px solid #ccc;" >   </div>
   
                        
   <!--  <input type="text" name="mobileNoVerify" id="mobileNoVerify" value="" style="width: 20%; height: 5%; margin-left: 0%; " >
    <input type="text" name="mobileNoVerify" id="emailIdVerify"  value="" style="width: 25%; height: 2%; margin-left: 0%;" >  --> </div>
      <input type="hidden" name="MId" id="MId"  value="<%=TM.getMerchantId()%>">         
        
          </div>
         <div class=" " id="otpVerify" style="display: none;">
                         <div class="">
                       <button type="button" class="btn btn-primary" id="backBtn2" style="display: inline-block;
    /* width: 50%; */
    border: none;
    background: white;
    color: gray; float: left;" ><font size="2px;">🡨Back</font></button>
                           <p style="max-inline-size: fit-content;float: left; margin-left: 20%;
                             font-size: 14px; font-weight: 600; ">Access your saved cards by entering the <span style="display: block;
            margin-top: 0.3em;" id="mobileSpan1">OTP sent to  </span></p>
                         
<!--                              <span style="float: left; margin-left: 23px; font-size: 13px">Your card details will be stored securely as per the latest RBI guidelines </span>
 -->                          </div>
                      
                    
                        <br><br>
                        <div style="margin-top: 2%; margin-bottom: 2%" >
    <input type="text" name="otpVerification" id="otpVerification" value="" maxlength="6" style="
    width: 20%; height: 5%; margin-left: 0%; border-radius: 5px; border: 2px solid #ccc; margin-top: 2%; text-align: center; " placeholder="OTP">
   <br> <br>
   
   <label class="control-label" style="display: inline-block;
  vertical-align: middle; font-size: 14px;
  margin: 10px 0;">Didn't receive the OTP?</label>
     <button type="button" class="btn btn-primary" id="resendNow1" style="display: inline-block;
  vertical-align: middle;
  margin: 10px 0;
    /* width: 50%; */
    border: none;
    background: white;
    color: blue;" onclick="return resendOtp1();">Resend Now</button> 
</div> 

  
                 
                  
                   
</div>   
   <br>
                    <div class="btn" id="submitButton">
                       <button id="submit" class="red-btn"  onclick="return otpVerify();" >Submit Details</button> 
 <form  action="RespCancelRequestHandler" method="post" style="display:inline;">
	  <button type="submit"  class="red-btn" formaction="RespCancelRequestHandler?txnId=<%=request.getAttribute("pgid").toString()%>" >cancel</button>
										</form>	<!--                         <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler" >cancel</button>  
 -->                    
                    </div>
                    
                     <div class="btn" id="fetchButton" style="display: none;">
                       <button id="submit" class="red-btn"  onclick="return fetchDetails();" >Fetch Details</button> 
 <form  action="RespCancelRequestHandler" method="post" style="display:inline;">
	  <button type="submit"  class="red-btn" formaction="RespCancelRequestHandler?txnId=<%=request.getAttribute("pgid").toString()%>" >cancel</button>
										</form>	<!--                         <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler" >cancel</button>  
 -->                    
                    </div>
                    
   
</div>  
      
            	
    
             
              <% log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>> cards div started ");%>
                <div class="c-card-content" id="ccdiv">
                <form id="pay_info" action="PGRouter" method="post"> 
              		  <input type="hidden" id="instrumentId" name="instrumentId" value=""> 
						<input type="hidden" name="txnId"  id="txnId" value="<%=request.getAttribute("pgid").toString()%>">
						<input type="hidden" name="keyEncKey" value="<%=keyEncKey%>">
						<input type="hidden" id="cardType" name="cardType" class="dc"  value=""/>
						<input type="hidden" name="checksum" value="<%=checksum%>">
						<input id="expiry" type="hidden" name="expiry" maxlength="5" value="" > 
						<input type="hidden" id="emioption" name="emioption">
						<input type="hidden" id="cName" name="cName">
					
					<div id="image-container">
                              <span id="card-image" ><img src="payfiVAS/newjs/other.svg" height="100%"></span>  <!-- style="margin-top: 49px;margin-right: 12px;" -->
                     </div>				
                  <div class="card-no">
                 <!-- <input type="text" class="cardNumber" maxlength="32" id="card_no"  name="cardNo" placeholder="Card Number">  -->
                 
                   <!-- <input type="text" class="cardNumber" maxlength="32" id="card_no"  name="cardNo" placeholder="Card Number" onfocus="this.placeholder = &#39;&#39;" placeholder="" onkeyup="if(this.value.length == 0 || this.value.length == 4 || this.value.length == 16) performBrandCheck(this.value)" onblur="if(this.value.length &gt;= 12) performMaestroRupayCheck(this.value); if(this.value.length == 0) this.placeholder=&#39;Card Number&#39;;">  -->
                   
                  <input type="text" class="visa" maxlength="32" id="card_no" name="cardNo"
                   placeholder="Card Number" onfocus="this.placeholder = &#39;&#39;" 
                   placeholder="" 
                   onkeyup="if(this.value.length == 0 || this.value.length == 4 || this.value.length == 16) " 
                   onblur="checkCardType()"
                   >  
                  </div>
           <%if(merchantDTO.getIsSaveCard().equalsIgnoreCase("1")){ %>
        	   <button type="button" class="btn btn-primary" id="access_card" style="margin-left: 2%;
    /* width: 50%; */
    border: none;
    background: white;
    color: blue;" onclick="return validate()">Access my saved cards</button>  	         
        <%   } %>
	  
                  
                  <br>
                  <div class="person-name">
                    <input type="text" class="onlychar" id="name" name="cardName" placeholder="Your Name" maxlength="45">
                  </div>
                   
                  <div class="col3 card-content">
                 <!--  bharat MM -->
                  
           							 <div class="custom-date">	
                                          	<select name="expMonthSelect" id="expMonthSelect" class="datepicker" onchange="changeMe(this)" style="color: rgb(117, 117, 126);">
												<option value="">
													Expiry MM
												</option>
											
												<option value="01">01</option>
												<option value="02">02</option>
												<option value="03">03</option>
												<option value="04">04</option>
												<option value="05">05</option>
												<option value="06">06</option>
												<option value="07">07</option>
												<option value="08">08</option>
												<option value="09">09</option>
												<option value="10">10</option>
												<option value="11">11</option>
												<option value="12">12</option>
																		
										</select>
                                     </div>	 
                  
                  
                  				 <div class="custom-year">  <!-- style="margin-left: -60px; -->
                                            	<select name="expYearSelect" id="expYearSelect" class="datepicker" onchange="changeMe(this)" style="color: rgb(117, 117, 126);">
													<option value="">	Expiry YYYY	</option>
														
														<option value="2022">	2022	</option>
														<option value="2023">	2023	</option>
														<option value="2024">	2024	</option>
														<option value="2025">	2025	</option>
														<option value="2026">	2026	</option>
														<option value="2027">	2027	</option>
														<option value="2028">	2028	</option>
														<option value="2029">	2029	</option>
														<option value="2030">	2030	</option>
														<option value="2031">	2031	</option>
														<option value="2032">	2032	</option>
														<option value="2033">	2033	</option>
														<option value="2034">	2034	</option>
														<option value="2035">	2035	</option>
														<option value="2036">	2036	</option>
														<option value="2037">	2037	</option>
														<option value="2038">	2038	</option>
														<option value="2039">	2039	</option>
														<option value="2040">	2040	</option>
														<option value="2041">	2041	</option>
														<option value="2042">	2042	</option>
														<option value="2043">	2043	</option>
														<option value="2044">	2044	</option>
														<option value="2045">	2045	</option>
														<option value="2046">	2046	</option>
														<option value="2047">	2047	</option>
														<option value="2048">	2048	</option>
														<option value="2049">	2049	</option>
														<option value="2050">	2050	</option>
														<option value="2051">	2051	</option>
														<option value="2052">	2052	</option>
														<option value="2053">	2053	</option>
														<option value="2054">	2054	</option>
														<option value="2055">	2055	</option>
														<option value="2056">	2056	</option>
														<option value="2057">	2057	</option>
														<option value="2058">	2058	</option>
														<option value="2059">	2059	</option>
														<option value="2060">	2060	</option>
														<option value="2061">	2061	</option>
													</select>
												</div> 
                  
                    <!--  <div class="exp-date column">
                      <input type="text" class="datepicker" id="exp_date" name="exp_date" placeholder="Card Expiration Date" readonly="readonly">
                    </div>   -->
                    
                    <div class="cvv column">
                      <input type="password" minlength="3" maxlength="4" placeholder="cvv" class="onlynumbers" name="cvv" id="cvv_no">
                    </div>
                    
                    
                    <!--  <span title="The Card Verification Value (CVV) is a 3-digit number found on the signature panel on the back of your card following the printed Card number." class="masterTooltip"> <img src="images/icons/info.png" style="margin-top: 19px; margin-left: 10px; float: left; margin-bottom: 10px;">
						</span> -->
						
                   <!--  <div class="cvv_info column"> <a href="javascript:void(0);" class="Qmark" onclick="show('.tooltip')"> <img src="images/icons/info.png">
                      <div class="tooltip"> <img src="images/icons/cvv-demo.png"> </div>
                      </a>
                    </div> -->
                       
                    <div class="clear"></div>
                  </div>

                  <%
            	 if(TM.getMerchantId().contains(PGUtils.getPropertyValue("nosavecardids"))){
                	  
                  
                  if(!TM.getCustMobile().equalsIgnoreCase("NA")){%>
                  	<div>
                             <label class="ccet" style="margin-left: 250px;font-size: 14px;color: lightslategrey;">Save Card</label>
                             <div class="onoffswitch4 pull-right" style="margin-left: 450px; margin-top: -24px;">
                             <input type="checkbox" class="onoffswitch-checkbox" name="isRemember" id="isCCRemember" value="Y" style= "margin-left: 265px;" checked>
                             
                             <label class="onoffswitch-label" for="isCCRemember" >	
						        <span class="onoffswitch-inner"></span>
						        <span class="onoffswitch-switch" ></span>
						    </label>
                             </div>
                     </div>
                     <%}
                     }%> 
                     
                      <%
                      if(merchantDTO.getIsSaveCard().equalsIgnoreCase("1")){
                      if(!TM.getCustMobile().equalsIgnoreCase("NA")){%>
                         <div class=" " id="mobileEmailDiv">
                         <div class="">
<!--                              <input type="checkbox" name="isRemember" id="isCCRemember" value="Y" checked/>
 -->                         
                              <input type="checkbox" name="isRememberVAS" id="isRemember" value="N" style="max-inline-size: fit-content;float: left; margin-top: 3px; transform: scale(1.4);" />
                             <label for="isCCRemember" style="max-inline-size: fit-content;float: left; margin-left: 10px;font-size: 15px">Save my card for a quick future checkout experience </label>
                             <br>
                             <span style="float: left; margin-left: 23px; font-size: 13px">Your card details will be stored securely as per the latest RBI guidelines </span>
                          </div>
                      
                        <%}%> 
                        <br>
                        
                        
                        <div style=" margin-top: 2%; /*margin-bottom: 2%; */ display: none;" id="mobileEmailEnter" >
                        
                        <div class="style1" style="display: inline-block; float: left; margin-left: 5px; width: 40%;">
  <label style="float: left; margin-left: 8%; font-size: 13px;">Mobile*</label>
  <br>
  <input type="text" name="mobileNo" id="mobileNo" value="" maxlength="10" style=" padding:17px; /* width: 20%;  */height: 2px; margin-left: 8%;  " >
  </div>
  <div class="style1" style="display: inline-block;width: 45%; ">
  <label style="float: left; margin-left: 5px; font-size: 13px;">Email*</label>
  <br>
  <input type="text" name="emailId" id="emailId"  value="" style=" padding:17px; /* width: 75%; */  height: 2px; margin-left: 0%;" >   </div>
   
   </div>
   
</div> 
 <div class=" " id="otpDiv" style="display: none;">
     
                         <div class="" style=" line-height: 1.6;">
                      
                  <button type="button" class="btn btn-primary" id="backBtn" style="display: inline-block;
    /* width: 50%; */
    border: none;
    background: white;
    color: gray; float: left;" ><font size="2px;">🡨Back</font></button>           <label for="isRememberVAS" style="max-inline-size: fit-content;
                             font-size: 14px; ">Save your card by entering the OTP sent to <br><span id="mobileSpan"></span> </label>
                         
<!--                              <span style="float: left; margin-left: 23px; font-size: 13px">Your card details will be stored securely as per the latest RBI guidelines </span>
 -->                          </div>
                      
                    
                       
                        <div style="margin-top: 2%; margin-bottom: 2%" >
    <input type="text" name="otp" id="otp" placeholder="OTP" maxlength="6" style="width: 12%; height: 0px; margin-left: 0%; " >
   
</div>  <label class="control-label" style="display: inline-block;
  vertical-align: middle; font-size: 14px;
  margin: 10px 0;">Didn't receive the OTP?</label>
    
<button type="button" class="btn btn-primary" id="resendNow" style="display: inline-block;
  vertical-align: middle;
  margin: 10px 0;
    /* width: 50%; */
    border: none;
    background: white;
    color: blue;" onclick="return resendOtp();">Resend Now</button> 

                  <br><br>

                   
</div>  <%}%> 
                 <div class="capacha col2" id="captchaDiv">
                    <p class="subHead">Enter the characters visible in the box below</p>
                    <div class="column2 marr8p">
                      <input type="text" placeholder="Enter the characters" id="capacha" maxlength="7" style="background: none !important;">
                    </div>
                   <!--  <div class="column2 capacha-img"> <img src="payfiVAS/images/capacha.jpg"> </div> -->
                   
                   <div class="column2 capacha-img"

                       style="background-image: url(payfiVAS/images/capacha.jpg); height: 65px; width: 250px; text-align: center;">

                        <font color="maroon" size="7" face="Courier New"><i id ="lblCaptcha"><%=sCaptcha%></i></font>

                   </div>
                    <div onclick="refreshCaptcha()" style="padding-top: 10px;padding-bottom: 10px;color: blue;cursor: pointer;text-decoration: underline;">Refresh Captcha</div>
                  </div>
                  
                  
             			
                  
                <!--   CC Button -->
             <%if(merchantDTO.getIsSaveCard().equalsIgnoreCase("1")){ %>
                             <div class="cc_card_submit_btn">
                    <div class="btn">
                            <button id="submit" class="red-btn"  onclick="return ValidateCardSaveCard('CC')" >Pay Now</button> 
 
<!--                        <button id="submit" class="red-btn"  onclick="return ValidateCard('CC')" >confirm Payment</button> 
 -->                      <%-- <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler?txnId=<%=request.getAttribute("pgid").toString()%>" >cancel</button>   --%>
                        <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler" >cancel</button>  
                    
                    </div>
                  </div>
                  
                   <!--   DC Button --> 
                   
                  <div class="dc_card_submit_btn">
                    <div class="btn">
        <!--              <button type="submit" class="red-btn" onclick="return ValidateCard('DC')" >confirm Payment</button> --> 
         <button type="submit" class="red-btn" onclick="return ValidateCardSaveCard('DC')" >Pay Now</button>
                        <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler" >cancel</button>  
                         </div>
                  </div>
                  <!--  Other CC Button -->
                  <div class="occ_card_submit_btn">
                    <div class="btn">
                     <!--         <button type="submit" class="red-btn" onclick="return ValidateCard('CC')">confirm Payment</button>-->     <!-- disabled="disabled" -->
                   <button type="submit" class="red-btn" onclick="return ValidateCardSaveCard('CC')">Pay Now</button>
                     <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler" >cancel</button>  
                    </div>
                  </div>
                  <!--  Other DC Button -->
                  <div class="odc_card_submit_btn">
                    <div class="btn">
                    <!--    <button type="submit" class="red-btn" onclick="return ValidateCard('DC')">confirm Payment</button>--> 	<!-- disabled="disabled" -->
                  <button type="submit" class="red-btn" onclick="return ValidateCardSaveCard('DC')">Pay Now</button>
                    	<button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler" >cancel</button>  
                    </div>
                  </div>	   
                	  
               <% }else{ %>
                <div class="cc_card_submit_btn">
                    <div class="btn"> 
                       <button id="submit" class="red-btn"  onclick="return ValidateCard('CC')" >confirm Payment</button> 
                       <%-- <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler?txnId=<%=request.getAttribute("pgid").toString()%>" >cancel</button>   --%>
                        <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler" >cancel</button>  
                    
                    </div>
                  </div>
                  
                   <!--   DC Button --> 
                   
                  <div class="dc_card_submit_btn">
                    <div class="btn">
                     <button type="submit" class="red-btn" onclick="return ValidateCard('DC')" >confirm Payment</button>  
                        <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler" >cancel</button>  
                         </div>
                  </div>
                  <!--  Other CC Button -->
                  <div class="occ_card_submit_btn">
                    <div class="btn">
                             <button type="submit" class="red-btn" onclick="return ValidateCard('CC')">confirm Payment</button>     <!-- disabled="disabled" -->
                     <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler" >cancel</button>  
                    </div>
                  </div>
                  <!--  Other DC Button -->
                  <div class="odc_card_submit_btn">
                    <div class="btn">
                        <button type="submit" class="red-btn" onclick="return ValidateCard('DC')">confirm Payment</button> 	<!-- disabled="disabled" -->
                    	<button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler" >cancel</button>  
                    </div>
                  </div>	   
               <%} %>  
                
     
                 
                  <div class="cc_card_submit_btn1" style="display: none;">
                    <div class="btn">
 
                       <button id="submit" class="red-btn"  onclick="return ValidateCard('CC')" >confirm Payment</button> 
                      <%-- <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler?txnId=<%=request.getAttribute("pgid").toString()%>" >cancel</button>   --%>
                        <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler" >cancel</button>  
                    
                    </div>
                  </div>
                  
                  </form>
                </div>
                <% log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>> cards div ended ");%>
               <!--  CC END -->
               
                              <!-- UPI Start -->  
             	<div class="upi-content">
                     <form id="UPIForm" action="PGRouter" method="post" >    
                                   <!--  mynewcode cardTypeVal -->
                                    <input type="hidden" name="instrumentId" value="UPI">
									<input type="hidden" name="txnId" id="txnId"  value="<%=request.getAttribute("pgid").toString()%>">
									<input type="hidden" name="keyEncKey" value="<%=keyEncKey%>">
									<input type="hidden" name="cardType" class="dc" id="cardType" value="UPI"/>
									<input type="hidden" name="checksum" id="checksum"value="<%=checksum%>">
									<input type="hidden" pattern="[0-9]{10}" class="phone-input name-field" maxlength="10" id="phone" required="number" placeholder="Phone" />
									<input type="hidden" class="mail-input email-field"  placeholder="Email ID"  id="email" required>
									<input type="hidden" id="emioption" name="emioption" value="NA">	
									<input type="hidden" id="otherAppValue" name="otherAppValue" value="emi">				
                                <div class=" right-section-inner " id="upiFirstScreen">
                                    <div class="col-md-8">
                                        <div class="row">
                                           
                                        </div>
                                        <div class=" upi-app-text ">Select your UPI app</div>
                        				<div class="rowUPI" style="justify-content: space-evenly;">
                            				<div class=" col-lg-4 col-md-6 col-xs-12 ">
                                				<button type="button" class="btn upi-btn-card" onclick="upiBtnClick('BHIM'); getSelectedUPI('BHIM')">
                                    			<img src="images/bhim-large.png" alt=" BHIM " title=" BHIM " /></button>
                            				</div>
                            				<div class=" col-lg-4 col-md-6 col-xs-12 text-center ">
                                				<button type="button" class="btn upi-btn-card" onclick="upiBtnClick('GPay'); getSelectedUPI('GPay')">
                                    			<img src="images/g-pay-large.png" alt=" Google Pay " title=" Google Pay " /></button>
                            				</div>
                            				<div class=" col-lg-4 col-md-6 col-xs-12 text-center ">
                                				<button type="button" class="btn upi-btn-card" onclick="upiBtnClick('PhonePe'); getSelectedUPI('PhonePe')">
                                    			<img src="images/phonepe-large.png" alt=" PhonePe " title=" PhonePe " /></button>
                            				</div>
                        				</div>
                        				<div class="rowUPI" style="justify-content: space-evenly;">
                            				<div class=" col-lg-4 col-md-6 col-xs-12 ">
                                				<button type="button" class="btn upi-btn-card" onclick="upiBtnClick('Paytm'); getSelectedUPI('Paytm')">
                                    			<img src="images/paytm-large.png" alt=" paytm " title=" paytm " /></button>
                            				</div>
                            				<div class=" col-lg-4 col-md-6 col-xs-12 text-center ">
                                				<button type="button" class="btn upi-btn-card" onclick="upiBtnClick('PayZapp'); getSelectedUPI('PayZapp')">
                                    			<img src="images/PayZapp-logo2.png" alt=" PAYZAPP " title=" PAYZAPP " /></button>
                            				</div>
                            				<div class=" col-lg-4 col-md-6 col-xs-12 text-center ">
                                				<button type="button" class="btn upi-btn-card" onclick="upiBtnClick('otherApp'); getSelectedUPI('otherApp')">Other Apps</button>
                            				</div>
                        				</div>
                        				<div>
                            				<div class=" col-lg-12 col-md-12 col-xs-12 text-center ">
                                         <button  id="footer-btn" class="btn btn-default" type="submit"  formaction="RespCancelRequestHandler" >Cancel</button> 
                               				 </div>
                       					</div>
                                        <!-- <div class="row" id="UPIpayment" style="padding: 50px;">
                                        
                                            <div class="form-group float-label-control col-md-10" style="text-align: left;">
                                                <h3 style="color: #8080809c;font-size: 1em;">Virtual Payment Address</h3>
                                                <p style="color:#a4a4a4;font-size: 11px;">You Will Receive a collect Request</p>
                                                 <input name="VPA" id="VPA" onfocus="clearUPIError()" class="form-control" placeholder="" required="required" style="margin-top: 10px;width: 100%;padding: 10px;">
												 <div id="errorMessage" style="display:none;color:red;font-size: 0.9em;"></div>
                                                <div style="color: #8080809c;font-size: 0.9em;    padding-top: 10px;"><b>Note:</b> You will receive a payment request in your UPI payment App.Kindly authorize and confirm the request to complete a payment.</div>
                                                 <datalist>
                                                 </datalist>
                                            </div>
                                        </div> -->

                                    </div>
                                   
                                </div>
                                <div class="showPayementDiv" style="margin-top:60px;">
                                <div id="vpa-enter" class="row1 vpa-enter-warpper m-0">
                                <div class="vpa-enter-list-wrapper" style="justify-content: space-evenly">
                                    <button type="button" class="btn bhim" id="bhim" onclick="addClassFunction('BHIM'); getSelectedUPI('BHIM')">
                                    <span class="bhim-vpa-logo"></span>
                                </button>
                                    <button type="button" class="btn gpay" id="gpay" onclick="addClassFunction('GPay'); getSelectedUPI('GPay')">
                                    <span class="google-pay-vpa-logo"></span>
                                </button>
                                    <button type="button" class="btn phonepe" id="phonepe" onclick="addClassFunction('PhonePe'); getSelectedUPI('PhonePe')">
                                    <span class="phonepe-vpa-logo"> </span>
                                </button>
                                    <button type="button" class="btn paytm" id="paytm" onclick="addClassFunction('Paytm'); getSelectedUPI('Paytm')">
                                    <span class="paytm-vpa-logo"></span>
                                </button>
                                    <button type="button" class="btn payzapp" id="payzapp" onclick="addClassFunction('PayZapp'); getSelectedUPI('PayZapp')">
                                    <span class="payzapp-vpa-logo"></span>
                                </button>
                                    <a class="vpa-other-app" href="#!" onclick="addClassFunction('otherApp')">
                                    Other Apps <hr class="hrline" id="hr"/>
                                	</a>
                                	
                                	
                                </div>
                            </div>
                            <div class="upi-input-wrapper">
                                <div class="upi-lable-text">
                                    <div>Enter your UPI ID</div>

                                    <a href="#" id="modal" data-toggle="modal" data-target="#myModal">How to
                                    pay using
                                    UPI?</a>

                                    <!-- Modal -->
                                    <div id="myModal" class="modal fade show" role="dialog">
                						<div class="modal-dialog modal-dialog-centered">

                    					<!-- Modal content-->
                    					<div class="modal-content">
                        				<div class="modal-header">
                            				<a href="#!" class="close close-btn" data-dismiss="modal" style="color:#A8A8A8" id="modalClose">&#10006</a>
                        				</div>
                        				<div class="model-header-text">Steps to pay using UPI</div>
                        					<div class="modal-body body-text-modal">
                            					<ol>
                                					<li>
                                    					Select your prefered UPI app
                                					</li>
                                					<li>
                                    					Enter the UPI ID or VPA linked to your prefered UPI app
                                					</li>
                                					<li>
                                    					Click on <a href="#">âVerify & Payâ</a>
                                					</li>
                                					<li>
                                    					You will receive a notification from selected UPI app on your phone
                                					</li>
                                					<li>
                                    					Tap the notification to open the UPI app
                                					</li>
                                					<li>
                                    					If you donât receive the notification, please open your UPI app and search for the collect request
                                					</li>
                                					<li>
                                    					Complete the payment by accepting the request and entering your UPI pin
                                					</li>
                            					</ol>
                        					</div>
                        					<div class="modal-footer">
                            					<button type="button" id="footer-btn" class="btn btn-default" data-dismiss="modal" onclick="closeModal()">Close</button>
                        					</div>
                    					</div>

                					</div>
            					</div>

                                </div>
                                <div class="input-wrapper">
                                    <form class="form-inline">
                                    <div class="form-inline1" id="upiIdInput">
                                        <div class="form-group input-text-area">
                                            <input type="text" name="VPA" onfocus="clearUPIError()" class="form-control upi-input" id="inputUPI" placeholder="UPI ID">
                                        </div>
                                        
                                        <select id="inputUPI23" name="UID" placeholder="UPI ID">

                                    	</select>

                                    </div>
                                    <div class="form-inline2" id="otherAppInput">
                                    	<div class="form-group input-text-area-otherApp">
                                            <input type="text" name="otherVPA" onfocus="clearUPIError()" class="form-control upi-input" id="inputUPIOtherApp" placeholder="UPI ID">
                                        </div>
                                    </div>
                                    
                                    </form>
                                </div>
                                <div id="errorMessage" style="display:none;color:red;font-size: 0.9em;"></div>
                                <div class="upi-app-description"><span>Note:</span> Please ensure that this UPI app is installed on your device before making payment
                                </div>
                            </div>
                            <div class="btn" style="margin-top:10px">
                                <button type="submit" class="red-btn" id="pay" >Verify & Pay</button>
                            </div>
                            <div>
                                <div class=" col-lg-12 col-md-12 col-xs-12 text-center ">
                                       <button  id="footer-btn" class="btn btn-default" type="submit"  formaction="RespCancelRequestHandler" >Cancel</button> 
                                </div>	
                            </div>
                          </div>
                       </form>  
            		</div>	
				 
             <!-- UPI End -->
             
             <!--  ATOM UPI start-->
             
					<div id="atomupi-content" class="atomupi-content">
                     <form id="ATOMUPIForm" action="PGRouter" method="post" >    
                                   <!--  mynewcode cardTypeVal -->
                                    <input type="hidden" name="instrumentId" value="UPI">
									<input type="hidden" name="txnId" id="txnId"  value="<%=request.getAttribute("pgid").toString()%>">
									<input type="hidden" name="keyEncKey" value="<%=keyEncKey%>">
									<input type="hidden" name="cardType" class="dc" id="cardType" value="ATOMUPI"/>
									<input type="hidden" name="checksum" id="checksum"value="<%=checksum%>">
									<input type="hidden" pattern="[0-9]{10}" class="phone-input name-field" maxlength="10" id="phone" required="number" placeholder="Phone" />
									<input type="hidden" class="mail-input email-field"  placeholder="Email ID"  id="email" required>
									<input type="hidden" id="emioption" name="emioption" value="NA">
									<input type="hidden" id="otherAppValue" name="otherAppValue" value="emi">					
                                <div class=" right-section-inner " id="upiFirstScreen">
                                    <div class="col-md-8">
                                        <div class="row">
                                           
                                        </div>
                                        <div class=" upi-app-text ">Select your UPI app</div>
                        				<div class="rowUPI" style="justify-content: space-evenly;">
                            				<div class=" col-lg-4 col-md-6 col-xs-12 ">
                                				<button type="button" class="btn upi-btn-card" onclick="upiBtnClick('BHIM'); getSelectedUPI('BHIM')">
                                    			<img src="images/bhim-large.png" alt=" BHIM " title=" BHIM " /></button>
                            				</div>
                            				<div class=" col-lg-4 col-md-6 col-xs-12 text-center ">
                                				<button type="button" class="btn upi-btn-card" onclick="upiBtnClick('GPay'); getSelectedUPI('GPay')">
                                    			<img src="images/g-pay-large.png" alt=" Google Pay " title=" Google Pay " /></button>
                            				</div>
                            				<div class=" col-lg-4 col-md-6 col-xs-12 text-center ">
                                				<button type="button" class="btn upi-btn-card" onclick="upiBtnClick('PhonePe'); getSelectedUPI('PhonePe')">
                                    			<img src="images/phonepe-large.png" alt=" PhonePe " title=" PhonePe " /></button>
                            				</div>
                        				</div>
                        				<div class="rowUPI" style="justify-content: space-evenly;">
                            				<div class=" col-lg-4 col-md-6 col-xs-12 ">
                                				<button type="button" class="btn upi-btn-card" onclick="upiBtnClick('Paytm'); getSelectedUPI('Paytm')">
                                    			<img src="images/paytm-large.png" alt=" paytm " title=" paytm " /></button>
                            				</div>
                            				<div class=" col-lg-4 col-md-6 col-xs-12 text-center ">
                                				<button type="button" class="btn upi-btn-card" onclick="upiBtnClick('PayZapp'); getSelectedUPI('PayZapp')">
                                    			<img src="images/payzapp-large.png" alt=" PAYZAPP " title=" PAYZAPP " /></button>
                            				</div>
                            				<div class=" col-lg-4 col-md-6 col-xs-12 text-center ">
                                				<button type="button" class="btn upi-btn-card" onclick="upiBtnClick('otherApp'); getSelectedUPI('otherApp')">Other Apps</button>
                            				</div>
                        				</div>
                        				<div>
                            				<div class=" col-lg-12 col-md-12 col-xs-12 text-center ">
                                    		<button  id="footer-btn" class="btn btn-default" type="submit"  formaction="RespCancelRequestHandler" >Cancel</button>
                               				 </div>
                       					</div>
                                        <!-- <div class="row" id="UPIpayment" style="padding: 50px;">
                                        
                                            <div class="form-group float-label-control col-md-10" style="text-align: left;">
                                                <h3 style="color: #8080809c;font-size: 1em;">Virtual Payment Address</h3>
                                                <p style="color:#a4a4a4;font-size: 11px;">You Will Receive a collect Request</p>
                                                 <input name="VPA" id="VPA" onfocus="clearUPIError()" class="form-control" placeholder="" required="required" style="margin-top: 10px;width: 100%;padding: 10px;">
												 <div id="errorMessage" style="display:none;color:red;font-size: 0.9em;"></div>
                                                <div style="color: #8080809c;font-size: 0.9em;    padding-top: 10px;"><b>Note:</b> You will receive a payment request in your UPI payment App.Kindly authorize and confirm the request to complete a payment.</div>
                                                 <datalist>
                                                 </datalist>
                                            </div>
                                        </div> -->

                                    </div>
                                   
                                </div>
                                <div class="showPayementDiv" style="margin-top:60px;">
                                <div id="vpa-enter" class="row1 vpa-enter-warpper m-0">
                                <div class="vpa-enter-list-wrapper" style="justify-content: space-evenly">
                                    <button type="button" class="btn bhim" id="bhim" onclick="addClassFunction('BHIM'); getSelectedUPI('BHIM')">
                                    <span class="bhim-vpa-logo"></span>
                                </button>
                                    <button type="button" class="btn gpay" id="gpay" onclick="addClassFunction('GPay'); getSelectedUPI('GPay')">
                                    <span class="google-pay-vpa-logo"></span>
                                </button>
                                    <button type="button" class="btn phonepe" id="phonepe" onclick="addClassFunction('PhonePe'); getSelectedUPI('PhonePe')">
                                    <span class="phonepe-vpa-logo"> </span>
                                </button>
                                    <button type="button" class="btn paytm" id="paytm" onclick="addClassFunction('Paytm'); getSelectedUPI('Paytm')">
                                    <span class="paytm-vpa-logo"></span>
                                </button>
                                    <button type="button" class="btn payzapp" id="payzapp" onclick="addClassFunction('PayZapp'); getSelectedUPI('PayZapp')">
                                    <span class="payzapp-vpa-logo"></span>
                                </button>
                                    <a class="vpa-other-app" href="#!" onclick="addClassFunction('otherApp')">
                                    Other Apps
                                	</a>
                                	
                                </div>
                            </div>
                            <div class="upi-input-wrapper">
                                <div class="upi-lable-text">
                                    <div>Enter your UPI ID</div>

                                    <a href="#" id="modal" data-toggle="modal" data-target="#myModal">How to
                                    pay using
                                    UPI?</a>

                                    <!-- Modal -->
                                    <div id="myModal" class="modal fade show" role="dialog">
                						<div class="modal-dialog modal-dialog-centered">

                    					<!-- Modal content-->
                    					<div class="modal-content">
                        				<div class="modal-header">
                            				<a href="#!" class="close close-btn" data-dismiss="modal" style="color:#A8A8A8" id="modalClose">&#10006</a>
                        				</div>
                        				<div class="model-header-text">Steps to pay using UPI</div>
                        					<div class="modal-body body-text-modal">
                            					<ol>
                                					<li>
                                    					Select your prefered UPI app
                                					</li>
                                					<li>
                                    					Enter the UPI ID or VPA linked to your prefered UPI app
                                					</li>
                                					<li>
                                    					Click on <a href="#">âVerify & Payâ</a>
                                					</li>
                                					<li>
                                    					You will receive a notification from selected UPI app on your phone
                                					</li>
                                					<li>
                                    					Tap the notification to open the UPI app
                                					</li>
                                					<li>
                                    					If you donât receive the notification, please open your UPI app and search for the collect request
                                					</li>
                                					<li>
                                    					Complete the payment by accepting the request and entering your UPI pin
                                					</li>
                            					</ol>
                        					</div>
                        					<div class="modal-footer">
                            					<button type="button" id="footer-btn" class="btn btn-default" data-dismiss="modal" onclick="closeModal()">Close</button>
                        					</div>
                    					</div>

                					</div>
            					</div>
                                </div>
                                <div class="input-wrapper">
                                    <div class="form-inline1" id="upiIdInput">
                                        <div class="form-group input-text-area">
                                            <input type="text" name="VPA" onfocus="clearUPIError()" class="form-control upi-input" id="inputUPI" placeholder="UPI ID">
                                        </div>
                                        <select id="inputUPI23" name="UID" placeholder="UPI ID" style="width:30%">
                                    	</select>
                                    </div>
                                    <div class="form-inline2" id="otherAppInput">
                                    	<div class="form-group input-text-area-otherApp">
                                            <input type="text" name="otherVPA" onfocus="clearUPIError()" class="form-control upi-input" id="inputUPIOtherApp" placeholder="UPI ID">
                                        </div>
                                    </div>
                                </div>
                                <div id="errorMessage" style="display:none;color:red;font-size: 0.9em;"></div>
                                <div class="upi-app-description"><span>Note:</span> Please ensure that this UPI app is installed on your device before making payment
                                </div>
                            </div>
                            <div class="btn" style="margin-top:10px">
                                <button type="submit" class="red-btn" id="pay">Verify & Pay</button>
                            </div>
                            <div>
                                <div class=" col-lg-12 col-md-12 col-xs-12 text-center ">
                                <button  id="footer-btn" class="btn btn-default" type="submit"  formaction="RespCancelRequestHandler" >Cancel</button> 
                                </div>	
                            </div>
                          </div>
                    </form> 
			                  
				</div>
             <!--  ATOM UPI END -->
             
              <!-- offline payment start -->
                   
                  <div class="offline-content">
                  
                  <form id="offlineForm" name="offlineForm" method="post" action="PGRouterOffline">
					<input type="hidden" id="instrumentId" name="instrumentId" value="Op">
					<input type="hidden" name="txnId" value="<%=request.getAttribute("pgid").toString()%>">
					<input type="hidden" name="checksum" value="<%=checksum%>" > 
					<input type="hidden" id="emioption" name="emioption" value="NA">				
								
								
								
								
                  	<div class="pay_options_bank">
                		<div class="drop_down_bank">
                		<div class="pay_options_bank">
            			<div class="payment_dropdown_bank">
             
               		 	<div class="stage_bankpg" style="width: 540px; margin-left: -63px;">
                
                		<select  id="offline-select" name="paymode" required="required" class="payment_mode_bank dropdown_image" style="width: 515px; margin-left: 1px;">   <!-- onmousedown="if(this.options.length>8){this.size=8;}"  onchange='this.size=0;' onblur="this.size=0;" -->
					        <option selected="selected" value="">Offline Payment Option</option>				         
                         <!-- <option value="NEFTRTGS">NEFT/RTGS</option>
						<option value="CASH">Cash</option>
						<option value="CHEQUE">Cheque</option>
						<option value="DD">Demad Draft</option> -->
						<%
				
							for(int i=0;i<ecmList.size();i++){%>
							<option value="<%=ecmList.get(i)%>"><%=ecmList.get(i)%></option>
				
						<%} %>
                                                          
                             </select>
              		 </div> 
            		</div>
            		</div>
                    </div></div>
                    <div class="btn">
                    <button type="submit" class="red-btn" onclick="return ValidateOffline()">confirm Payment</button>

                     </div>
                    </form>
                  </div>
                <!-- end offline payment   -->

             
               
              <!--   Start stored-card -->
              
               <%	
               			log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>>Getting stored cards ");
                	Vector<List<String>> storedCardDetails = (Vector<List<String>>) request.getAttribute("cardDetails");	
					log.info("index.jsp ::: Total Stored Cards are :: "+storedCardDetails.size());					
					if (storedCardDetails != null && storedCardDetails.size() > 0)
					{					
					%>
              
                <div class="stored-card" id="stored-card">
                
                <form action="PGRouter" method="post"  id="scForm" class="datpayment-form">   
                                  
                                <%   storedCardDetails = dataManager.getStoredCardDetails();
                                		log.info("IndexVAS.java ::: pgid = "+pgid+"  TM.getCustMail() ::  "+TM.getCustMail()+"  TM.getCustMobile() "+TM.getCustMobile()+"  TM.getMerchantId()  "+TM.getMerchantId()+" stored cards ended");
      								request.setAttribute("cardDetails", storedCardDetails); %> 
      								
                                    <input type="hidden" name="cardNo" id="decCardNo" value="">
									<input type="hidden" name="instrumentId" id="scinstrumentId" value="">
									<input type="hidden" name="txnId"  id="txnId" value="<%=request.getAttribute("pgid").toString()%>">
									<input type="hidden" name="checksum" value="<%=checksum%>">
									<input type="hidden" name="cardType" id="sccardType" class="dc"  value=""/>
									<input type="hidden" pattern="[0-9]{10}" class="phone-input name-field" maxlength="10" id="phone" required="number" placeholder="Phone" />
									<input type="hidden" class="mail-input email-field"  placeholder="Email ID"  id="email" required>
									<input type="hidden" class="mail-input email-field"  id="value_of_i" value="">
									<input type="hidden" id="emioption" name="emioption" value="NA">
									<input type="hidden" id="SCExpiry"  name="expiry" maxlength="5" value="" >
									<input type="hidden" name="cardNo" id="card-number">
									<input type="hidden" name="cardName" id="card-holder"/>
                
                  			<ul>
	                		 <%	
	                		 
	                		 log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>> masking cards");
	                		 for(int i = 0 ; i < storedCardDetails.size() ;i++)
									{ 
								
									 List<String> cards = null;
										cards = storedCardDetails.get(i);
										
										String decCardNo = null, maskedCardNo  = null, secretKey = null;
										try
								      	{
											secretKey = TripleKeyEnc.decode(storedCardDetails.get(i).get(8),keyEncKey);
											decCardNo = AppsCrypto.decrypt(storedCardDetails.get(i).get(0), secretKey);
											maskedCardNo = PGUtils.maskCardNumber(decCardNo);
								      	}  
								      	catch (Exception e)
								      	{									
								      		log.info("PGRouter.java ::: Error in Card Decryption -->"+e);
								      		// e.printStackTrace();
								      	}
										 log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>> masking cards done");
									%>
												
					                    <li id="li<%=i%>">
					                      <div class="floatl">
					                        <p>
					                          <input type="checkbox" class="chkbx" id="<%=i%>"  name="cname" value="<%=i%>,<%=storedCardDetails.get(i).get(0)%>,<%=maskedCardNo%>,<%=storedCardDetails.get(i).get(1)%>,<%=storedCardDetails.get(i).get(2)%>,<%=storedCardDetails.get(i).get(3)%>,<%=storedCardDetails.get(i).get(4)%>,<%=storedCardDetails.get(i).get(8)%>,<%=keyEncKey%>,<%=storedCardDetails.get(i).get(5)%>,<%=storedCardDetails.get(i).get(6)%>,<%=storedCardDetails.get(i).get(7)%>,<%=storedCardDetails.size()%>" onchange="storedcardFill(this.value);">
					                          <label for="<%=i%>" id="<%=i%>"> <!-- CITI  <br>  -->
					                          
					
					                 <span> <%=maskedCardNo%> </span> </label>
					                        </p>
					                      </div>
					                      <div class="floatr">
					                        <p><a href="javascript:removeCard('<%=i%>,<%=storedCardDetails.get(i).get(0)%>,<%=maskedCardNo%>,<%=storedCardDetails.get(i).get(1)%>,<%=storedCardDetails.get(i).get(2)%>,<%=storedCardDetails.get(i).get(3)%>,<%=storedCardDetails.get(i).get(4)%>,<%=storedCardDetails.get(i).get(8)%>,<%=keyEncKey%>,<%=storedCardDetails.get(i).get(5)%>,<%=storedCardDetails.get(i).get(6)%>,<%=storedCardDetails.get(i).get(7)%>')" class="remove-card"> Remove Card </a> </p>  <!--  &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp; -->
					                        <p>
					                          <input type="password" maxlength="3" placeholder="CVV" class="cvv-num onlynumbers <%=i%>"  id="card_cvv<%=i%>" name="cvv" required="required">
					                        </p>
					                      </div>
					                    </li>
					                    
					                    <%} %>
                    
                    
                   
                  </ul>
                   <div class="btn">
                    <button type="submit" class="red-btn" onclick="return ValidateCVV()">confirm Payment</button>  <!-- onclick="return ValidateCVV()" -->
                     </div>
                  </form>
                </div>
                <%} %>
                <!--   End stored-card -->
                
                
				 <!-- Wallet2 form start -->
				             			
				<div class="wallet-content2">
				<div class="">
				<form id="walletform" name="walletform" method="post" action="PGRouter">
				<input type="hidden" id="instrumentId" name="instrumentId" value="WALLET"> 
				<input type="hidden" name="txnId" value="<%=request.getAttribute("pgid").toString()%>">
				
				<input type="hidden" name="checksum" value="<%=checksum%>">
				<input type="hidden" id="emioption" name="emioption" value="NA">
				<input type="hidden" id="cardType" name="cardType" value="WALLET">
				
				
				<div class="row radio-group" style="margin-left: 65px;"></div>
				
				<div class="pay_options_bank">
				<div class="drop_down_bank">
				<div class="pay_options_bank">
				<div class="payment_dropdown_bank">
				<div class="stage_bankpg2" style="background: #f2f3f4 url(../images/select-bg.png) no-repeat 95% center;
    border: 2px solid #004a8f;
    border-radius: 5px;
    cursor: pointer;
    margin-bottom: 20px;
    width: 540px;
    margin-left: -63px;">
				
				<select id="wallet-bank-select2" name="bank" required="required"
				class="payment_mode_bank dropdown_image" style="width: 515px;
    margin-left: 1px;">
				
				<option value="">Select a different Wallet</option>
				<%
				
				for(int i=0;i<walletlist2.size();i++){%>
				<option value="<%=walletlist2.get(i)%>"><%=walletlist2.get(i)%></option>
				
				<%}
				 log.info("walletlist2 ::: = "+walletlist2);%>
				
				</select>
				</div>
				</div>
				</div>
				</div>
				</div>
				<div class="btn">
				<button type="submit" class="red-btn"
				onclick="ValidateWallet2()">confirm Payment</button>
				
				
				 <form  action="RespCancelRequestHandler" method="post" style="display:inline;">
	  <button type="submit"  class="red-btn" formaction="RespCancelRequestHandler?txnId=<%=request.getAttribute("pgid").toString()%>" >cancel</button>
										</form>	
				</div>
				</form>
				</div>
				</div>
				
				<!-- wallet div ended -->
				
				
				
				
				 <!-- Wallet1 form start -->
				             			
				<div class="wallet-content1">
				<div class="">
				<form id="walletform" name="walletform" method="post" action="PGRouter">
				<input type="hidden" id="instrumentId" name="instrumentId" value="WALLET"> 
				<input type="hidden" name="txnId" value="<%=request.getAttribute("pgid").toString()%>">
				
				<input type="hidden" name="checksum" value="<%=checksum%>">
				<input type="hidden" id="emioption" name="emioption" value="NA">
				<input type="hidden" id="cardType" name="cardType" value="WALLET">
				
				
				<div class="row radio-group" style="margin-left: 65px;"></div>
				
				<div class="pay_options_bank">
				<div class="drop_down_bank">
				<div class="pay_options_bank">
				<div class="payment_dropdown_bank">
				
				<div class="stage_bankpg1" style="background: #f2f3f4 url(../images/select-bg.png) no-repeat 95% center;
    border: 2px solid #004a8f;
    border-radius: 5px;
    cursor: pointer;
    margin-bottom: 20px;
    width: 540px;
    margin-left: -63px;">
				
<!-- 				<select id="wallet-bank-select1" name="bank" required="required" -->
<!-- 				class="payment_mode_bank dropdown_image" style="width: 515px; -->
<!--     margin-left: 1px;"> -->
				
<!-- <!-- 				<option value="">Select a different Wallet</option> --> 
<%-- 				<% --%>
				
<%-- 				for(int i=0;i<walletlist1.size();i++){%> --%>
<%-- <%-- 				<option value="<%=walletlist1.get(i)%>"><%=walletlist1.get(i)%></option> --%> 
<%-- 						<input type="text"  value="<%=walletlist1.get(i)%>"/> --%>

				
<%-- 				<%} --%>
<%-- 				 log.info("walletlist1 ::: = "+walletlist1);%> --%>
				
<!-- 				</select> -->



				
<!-- 				<option value="">Select a different Wallet</option> -->
				<%
				
				for(int i=0;i<walletlist1.size();i++){%>
<%-- 				<option value="<%=walletlist1.get(i)%>"><%=walletlist1.get(i)%></option> --%>
						<input type="text"  name="bank" required="required" style="width: 507px;
    margin-left: 1px;background-color: #f2f3f4; padding: 15px;" value="<%=walletlist1.get(i)%>" readonly/>

				
				<%}
				 log.info("walletlist1 ::: => "+walletlist1);%>
				
				
				
				</div>
				</div>
				</div>
				</div>
				</div>
				<div class="btn">
				<button type="submit" class="red-btn"
				onclick="ValidateWallet1()">confirm Payment</button>
				
				
				 <form  action="RespCancelRequestHandler" method="post" style="display:inline;">
	  <button type="submit"  class="red-btn" formaction="RespCancelRequestHandler?txnId=<%=request.getAttribute("pgid").toString()%>" >cancel</button>
										</form>	
				</div>
				</form>
				</div>
				</div>
				
				<!-- wallet1 div ended -->
				
                
                <!-- Bharat QR Start -->
             			
             			<div class="bqr-content">
             			<div >
             			<img style="width: 15%;height: auto;padding: 5px;" src="images/bharat_qr-logo.png">
             			</div>
             			<div>Scan here to pay</div>
             			<div class="arrow" title="arrow icon"></div>
             			<%-- <%
             			BQRProcessor obj = new BQRProcessor();
             			byte[] pngData = obj.getBQR(request.getAttribute("pgid").toString());
             			%> --%>
             			<%-- 	<img id="bqrImage"
								src="data:image/png;base64, <%=Base64.encodeBase64String(pngData)%>">
								 --%>
								 
						<img id="bqrImage">
             			 <div style="color: #8080809c;font-size: 0.9em;    padding-top: 10px;"><b>Note:</b> 
             			 To pay, Scan using BHARAT QR enabled wallet / Bank app.
             			 <br>
             			 You will be automatically redirected to the merchant website after 4:55 minutes.
						 
						  <div>Time left = <span id="timer"></span></div> 
             			 </div>
                                       <form  action="RespCancelRequestHandler" method="post" style="display:inline;">
					          <button type="submit"  style="width: 40%;margin: 0 auto;background: #ed1c24;color: #fff;font-size: 15px;text-transform: uppercase;padding: 18px 0px;border-radius: 5px;border: 0;border-bottom: 8px solid #aa050b;box-shadow: inset 0 -1px 1px rgba(255,255,255,.15);
   transition: transform .2s ease-in-out;" class="red-btn" formaction="RespCancelRequestHandler?txnId=<%=request.getAttribute("pgid").toString()%>" >cancel</button>
						</form>	        
             			</div>
				
             			<!-- Bharat QR End -->
                
                   <!-- NET BANKING -->
                    <% log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>> Netbanking div started");%>
                  <div class="nbpayzap-content">
                  
                  <form id="nbForm" name="nbForm" method="post" action="PGRouter">
					<input type="hidden" id="instrumentId" name="instrumentId" value="NB">
					<input type="hidden" name="txnId" value="<%=request.getAttribute("pgid").toString()%>">
					<input type="hidden" name="checksum" value="<%=checksum%>" > 
					<input type="hidden" id="emioption" name="emioption" value="NA">				
								
								<div class="row radio-group" style="margin-left: 65px;">
                                       
                                           <!-- <div class="col-md-2" style="padding-right: 20px;">
                                                <input type="radio" name="emotion" id="sbi" class="input-hidden"/>
                                                 <label for="sbi"><img src="payfiVAS/newimages/sbi.png" class="selectBank" data-value="1029"/></label>
                                            </div>
                                            
                                            <div class="col-md-2" style="padding-right: 20px;">
                                                <input type="radio" name="emotion" id="hdfc" class="input-hidden" />
                                                <label for="hdfc"> <img src="payfiVAS/newimages/hdfc.png" class="selectBank" data-value="1013" /></label>

                                            </div>
                                            
                                            <div class="col-md-2" style="padding-right: 20px;">
                                                <input type="radio" name="emotion" id="icici" class="input-hidden"/>
                                                <label for="icici"> <img src="payfiVAS/newimages/icici.png" class="selectBank" data-value="1014"/></label>
                                            </div>
                                            <div class="col-md-2" style="padding-right: 20px;">
                                                <input type="radio" name="emotion" id="axis" class="input-hidden"/>
                                                <label for="axis"> <img src="payfiVAS/newimages/axis.jpg" class="selectBank" data-value="1002"/></label>
                                            </div>
                                            <div class="col-md-2" style="padding-right: 20px;">
                                                <input type="radio" name="emotion" id="kotak" class="input-hidden"/>
                                                <label for="kotak"> <img src="payfiVAS/newimages/kotak.png" class="selectBank" data-value="1022"/></label>
                                            </div>
                                            <div class="col-md-2" style="padding-right: 20px;">
                                                <input type="radio" name="emotion" id="yes" class="input-hidden"/>
                                                <label for="yes"> <img src="payfiVAS/newimages/yes.jpg" class="selectBank" data-value="1035"/></label>
                                            </div>  -->
                                            
                                           <!--  </div> -->  <!-- bharat -->
                                        </div>
								
								
								
								
                  	<div class="pay_options_bank">
                		<div class="drop_down_bank">
                		<div class="pay_options_bank">
            			<div class="payment_dropdown_bank">
             
               		 	<div class="stage_bankpg">
                
                		<select  id="bank-select" name="bank" required="required" class="payment_mode_bank dropdown_image" >  
                		 <!-- onmousedown="if(this.options.length>8){this.size=8;}"  onchange='this.size=0;' onblur="this.size=0;" -->
					        <option selected="selected" value="">Select a different Bank</option>
					       
					         <% 
	                       		//Hashtable hashBank = (Hashtable)returnList.get(1);
	                      		Iterator its =	sortedbankHash.entrySet().iterator();
								while(its.hasNext()){
								Map.Entry entrys = (Map.Entry) its.next();
                              %>
                             <option value="<%=entrys.getKey()%>"><%=entrys.getValue()%></option> 
                       
                             <%} %>
                             </select>
              		 </div> 
            		</div>
            		</div>
                    </div></div>
                    <div class="btn">
                    <button type="submit" class="red-btn" onclick="return ValidateNB()">confirm Payment</button>
                   <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler" >cancel</button>
                     </div>
                    </form>
                  </div>
                  <% log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>> Netbanking div ended");%>
                <!-- end netbanking   -->
                
                
                 <div class="payzap-content">
                    <div class="btn"> <a href="javascript:void(0);" class="red-btn">confirm Payment</a> </div>
                  </div>
                
                
               
                

            	</div>
            </div>
          </div>
        </div>
        <div class="clear"></div>
      </div>
     <div class="spacer20"></div>
   
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

$(".loading-wrapper-mobile").css('display','none');

var bankUPIs = [{"uipId":"BHIM","upiHandle":["@upi"]},{"uipId":"GPay","upiHandle":["@okaxis","@okhdfcbank","@okicici","@oksbi"]},
	{"uipId":"PhonePe","upiHandle":["@ybl","@ibl","@ixl"]}, {"uipId":"Paytm","upiHandle":["@paytm"]},
	{"uipId":"PayZapp","upiHandle":["@okhdfcbank"]}];

function initUPI(filteredBankUPI){
	$('#inputUPI23').html("");
	if(filteredBankUPI.length == 1){
		$("#inputUPI23").addClass('singleOption');
		$("#inputUPI23").removeClass('multipleOption');
	}
	else{
		$("#inputUPI23").removeClass('singleOption');
		$("#inputUPI23").addClass('multipleOption');
	}
	
	$.each(filteredBankUPI, function (i, upi) {
		
		$('#inputUPI23')
        .append($("<option></option>")
                   .attr("value", upi)
                   .text(upi)); 
		console.log(i);
	    
	    
	});
}

$("#isRemember").click(function ()
   		{
   			$(this).prop("checked") ? $(this).val("Y") : $(this).val("N");
   			
   			var v= $("#isRemember").val();
   			
   			if(v=='Y'){
   				jQuery('#mobileEmailEnter').show();
   				jQuery('#captchaDiv').hide();
   				
   			//	$("#payNow").text('Submit Details');

   			  	
   			}else{
   				jQuery('#captchaDiv').show();
   				$("#mobileNo").val("");
      			 $("#emailId").val("");
   				jQuery('#mobileEmailEnter').hide();	
   			 
   			}
   			
   		});
   					
    $("#isCCRemember").click(function ()
			{
				$(this).prop("checked") ? $(this).val("Y") : $(this).val("N");
			});	

function getSelectedUPI(UPIName){
	console.log("UPIName",UPIName);
	var newBankUPIs = bankUPIs;
	var filteredBankUPI = newBankUPIs.filter(function (el)
			{
		  return el.uipId.toLowerCase() == UPIName.toLowerCase();
		       
		});
	console.log("bankUPIs",bankUPIs);
	console.log("filteredBankUPI[0]", filteredBankUPI[0]);
	initUPI(filteredBankUPI[0].upiHandle);
}

function closeModal(){
	$("#myModal").hide();
}

(function () {
	  const second = 1000,
	        minute = second * 60,
	        hour = minute * 60,
	        day = hour * 24;
	  let today = new Date();
	  const countDown = today.getTime()+14*minute,
	      x = setInterval(function() {    

	        const now = new Date().getTime(),
	              distance = countDown - now;  
           $('#countdown-number').html(String(Math.floor((distance % (hour)) / (minute))).padStart(2, "0")+":"+String(Math.floor((distance % (minute)) / second)).padStart(2, "0"));
	        
	        if (distance < 0) {
	        	var form = document.createElement("form");
	        	form.action="SessionTimedoutTxnHandler";
	        	document.body.appendChild(form);
	        	form.submit();
	          clearInterval(x);
	        }
	      }, 500)
	  }())
</script>
<script type="text/javascript">
$(".chb").change(function()
                                  {
                                      $(".chb").prop('checked',false);
                                      $(this).prop('checked',true);
                                  });
                                  
                                  
$("#backBtn").click(function() {
	
	$("#otpDiv").hide();
	$("#otp").val("");
	$("#mobileEmailDiv").show();
	$("#captchaDiv").hide();
	
	 $('#payment_mode_drop_down').attr('disabled', false);
	 $('#payment_mode_drop_down2').attr('disabled', false);
	 
	 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
	});     

$("#backBtn1").click(function() {
	
	 $("#ccdiv").trigger("reset");
	$("#ccdiv").show();
	 $("#verifyDiv").hide();
	 $("#mobileNoVerify").val("");
	 $("#emailIdVerify").val("");
	 $('#payment_mode_drop_down').attr('disabled', false);
	 $('#payment_mode_drop_down2').attr('disabled', false);
	 
	 $(".payment_dropdown" ).css( "pointer-events", "auto" );
	
	}); 
	
$("#backBtn2").click(function() {

	$("#detailVarify").show();
	$("#submitButton").show();
	$("#otpVerify").hide();
	$("#fetchButton").hide();
	$("#otpVerification").val("");
	
	}); 
	
$("#backBtn3").click(function() {
	
	 $("#ccdiv").trigger("reset");
	$("#ccdiv").show();
	 document.getElementById("payinfoformwithcookie").reset();	
	//$("#backBtn3").hide();
	if ($("#cookieexist").is(':visible')){
		 $("#cookieexist").hide();
	}
	
	
	 $('#payment_mode_drop_down').attr('disabled', false);
	 $('#payment_mode_drop_down2').attr('disabled', false);
	 
	 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
	
	}); 
	
	
$("#backBtn4").click(function() {
	
	 $("#ccdiv").trigger("reset");
	$("#ccdiv").show();
	 document.getElementById("payinfoformwithoutcookie").reset();	
	//$("#backBtn3").hide();

	if($('#cookienotexist').css('display') == 'block'){
		$("#cookienotexist").css("display", "none");
	
		 $("#verifyDiv").hide();
			$("#otpVerify").hide();
			$("#fetchButton").hide();
			$("#detailVarify").hide();
			$("#submitButton").hide();
			$("#otpVerification").val("");
			 $("#mobileNoVerify").val("");
			 $("#emailIdVerify").val("");
	}

/* 	
	document.cookie = "mobileNo=; max-age=0";
	document.cookie = "emailId=; max-age=0";
 */

	 $('#payment_mode_drop_down').attr('disabled', false);
	 $('#payment_mode_drop_down2').attr('disabled', false);
	 
	 $(".payment_dropdown" ).css( "pointer-events", "auto" );
	 removeCookies();
	}); 
	
</script>

<script type="text/javascript">
function validate()
{
	$("#ccdiv").hide();
	
		var mobileNoFromCookie= $("#mobileNoFromCookie").val();
		var emailIdFromCookie= $("#emailIdFromCookie").val();
	 	
	console.log(mobileNoFromCookie+">>>>>>"+emailIdFromCookie);
	 if(mobileNoFromCookie=="" && emailIdFromCookie==""){
		 if ($('#otpVerify').css('display') == 'none') {
			   
			    $("#verifyDiv").show();
				$("#detailVarify").show();
				$("#submitButton").show();
			}
		

		 $('#payment_mode_drop_down').attr('disabled', true);
		 $('#payment_mode_drop_down2').attr('disabled', true);
		 
		 $(".payment_dropdown" ).css( "pointer-events", "none" );
		 
	 }else{
		 $("#cookieexist").show();
			let noOfCard = parseInt($('#wcnoOfCard').val());
		
		  if(noOfCard>0){
			 for (let i = 0; i < noOfCard; i++) {
						document.getElementById("wccvv_"+i).style.visibility = "hidden";	
						document.getElementById("wccvv_"+i).value = "";		 
		 }
	
	 } 
		  
		  $('#payment_mode_drop_down').attr('disabled', true);
			 $('#payment_mode_drop_down2').attr('disabled', true);
			 
			 $(".payment_dropdown" ).css( "pointer-events", "none" );
	 } 
			return false;
}
	
</script>

<script type="text/javascript">
function storedcardFillTokenizeWC(value_i, tokenRefId, tokenExpiry, cName, instrument, type, encKey, keKey, merchantId, cardMail, cardMobile, cardlength, cryptogram , panlast4, customerId){
	 
	for (let i = 0; i < cardlength; i++) {
		if(i==value_i){
			document.getElementById("wccvv_"+value_i).style.visibility = "visible";	
		}else{
			document.getElementById("wccvv_"+i).style.visibility = "hidden";	
			document.getElementById("wccvv_"+i).value = "";	
		}
		}
		
	var value_i = value_i;
	var cNumber = tokenRefId;
	var cExpiry = tokenExpiry;
	var cName = cName;
	var cInstrument = instrument;
	var cType = type;
	
	var encKey = encKey;
	var keKey = keKey;
	var len =  cardlength;
	
 	var cryptogram =  cryptogram;
	var panlast4 =  panlast4;
	var customerId =  customerId;
	
	
	// alert("length  :: "+len);
	
	
	//$('span[ID!="x"]').attr('style', "display: none;");
	// var cMid = strArray[9];
	// var cMail = strArray[10];
	// var cMob = strArray[11];
	// document.getElementById("email").value = cMail;
	//  document.getElementById("phone").value = cMob;
	
 			
	document.getElementById("wctokenRefId").value = cNumber;
  			//	document.getElementById("card-numberSCCC").value = maskedCNumber;
  			let expMMYY = cExpiry; 
			let encMMYY = expMMYY.substr(0, 2)+expMMYY.substr(2);
 			
 			var encryptedExpInHex=encrypt(encMMYY);
 		
 			$('#wcExpiry').val(encryptedExpInHex);
			
  			//	document.getElementById("SCExpiryC").value = cExpiry
  			
  				document.getElementById("wccard-holder").value = cName;
  				
  				document.getElementById("wccardType").value = cType;
  				document.getElementById("wcinstrumentId").value = cInstrument;
  				document.getElementById("wccryptogram").value = cryptogram;
  				document.getElementById("wcpanlast4").value = panlast4;
  				document.getElementById("wcvalue_of_iCC").value = value_i;
  				document.getElementById("wccustomerId").value = customerId;
  				  
  				
		}


</script>


<script type="text/javascript">
function storedcardFillTokenizeWOC(value_i, tokenRefId, tokenExpiry, cName, instrument, type, encKey, keKey, merchantId, cardMail, cardMobile, cardlength, cryptogram , panlast4, customerId){
	 
	for (let i = 0; i < cardlength; i++) {
		if(i==value_i){
			document.getElementById("woccvv_"+value_i).style.visibility = "visible";	
		}else{
			document.getElementById("woccvv_"+i).style.visibility = "hidden";	
			document.getElementById("woccvv_"+i).value = "";	
		}
		}
		
	var value_i = value_i;
	var cNumber = tokenRefId;
	var cExpiry = tokenExpiry;
	var cName = cName;
	var cInstrument = instrument;
	var cType = type;
	
	var encKey = encKey;
	var keKey = keKey;
	var len =  cardlength;
	
 	var cryptogram =  cryptogram;
	var panlast4 =  panlast4;
	var customerId= customerId;
	
	// alert("length  :: "+len);
	
	
	//$('span[ID!="x"]').attr('style', "display: none;");
	// var cMid = strArray[9];
	// var cMail = strArray[10];
	// var cMob = strArray[11];
	// document.getElementById("email").value = cMail;
	//  document.getElementById("phone").value = cMob;
	
 			
	document.getElementById("woctokenRefId").value = cNumber;
  			//	document.getElementById("card-numberSCCC").value = maskedCNumber;
  			let expMMYY = cExpiry; 
			let encMMYY = expMMYY.substr(0, 2)+expMMYY.substr(2);
 			
 			var encryptedExpInHex=encrypt(encMMYY);
 		
 			$('#wocExpiry').val(encryptedExpInHex);
			
  			//	document.getElementById("SCExpiryC").value = cExpiry
  			
  				document.getElementById("woccard-holder").value = cName;
  				
  				document.getElementById("woccardType").value = cType;
  				document.getElementById("wocinstrumentId").value = cInstrument;
  				document.getElementById("woccryptogram").value = cryptogram;
  				document.getElementById("wocpanlast4").value = panlast4;
  				document.getElementById("wocvalue_of_iCC").value = value_i;
  				document.getElementById("woccustomerId").value = customerId;
  				
		}


</script>



<script type="text/javascript">
function otpVerify()
{
	
	if($('#mobileNoVerify').val()=="" ||$('#emailIdVerify').val()==""){
		
		  $('#emailIdVerify').focus();
			 $('#mobileNoVerify').focus();
			
		      
		        $('#mobileNoVerify').addClass('error');
		           $('#emailIdVerify').addClass('error');
		        $( ".red-btn" ).css( "pointer-events", "auto" );
		        $('#payment_mode_drop_down').attr('disabled', false);
				 $('#payment_mode_drop_down2').attr('disabled', false);
				 
				 $(".payment_dropdown" ).css( "pointer-events", "auto" );
				 
				 if($('#mobileNoVerify').val()==""){
						swal("Please enter mobile number");	 
				 }else if($('#emailIdVerify').val()==""){
						swal("Please enter email Id");	 
				 }
		       return false; 
		}

	if($('#mobileNoVerify').val()!="" && $('#emailIdVerify').val()!=""){
	 	var mobileNo = 	$('#mobileNoVerify').val();
	  	var emailId = 	$('#emailIdVerify').val();
		var txnId= 	<%=request.getAttribute("pgid").toString()%>; 
	
			 var regexemail=/^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			 var regxmobile = /^[6-9]\d{9}$/ ;
		
			 
			 if(!regexemail.test(emailId)) {
			 $('#emailIdVerify').focus();
			
			
		        $('.c-card-content input').removeClass('error');
		    
		           $('#emailIdVerify').addClass('error');
		        $( ".red-btn" ).css( "pointer-events", "auto" );
		        $('#payment_mode_drop_down').attr('disabled', false);
				 $('#payment_mode_drop_down2').attr('disabled', false);
				 
				 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
				 
					swal("Please enter valid email ID");	 
		       return false;
		        
			 }else if(!regxmobile.test(mobileNo)) {
			
			 $('#mobileNoVerify').focus();
			
		        $('.c-card-content input').removeClass('error');
		        $('#mobileNoVerify').addClass('error');
		         
		        $( ".red-btn" ).css( "pointer-events", "auto" );
		        $('#payment_mode_drop_down').attr('disabled', false);
				 $('#payment_mode_drop_down2').attr('disabled', false);
				 
				 $(".payment_dropdown").css( "pointer-events", "auto" );
				 
				 swal("Please enter valid mobile number ");	
		       return false;
		        
			 }
		 
		var merchantName=$('#merchantName').val();
		sendOtp(mobileNo, emailId, txnId, merchantName);
		resendNowTimer1();
		$("#otpVerify").show();
		$("#fetchButton").show();
		$("#submitButton").hide();
		$("#detailVarify").hide();
		$("#mobileSpan1").text("OTP sent to +91 "+mobileNo);
	
	}
		
	 	

	}
</script>

<script type="text/javascript">
function fetchDetails()
{
	


	if($('#mobileNoVerify').val()!="" && $('#emailIdVerify').val()!="" && $('#otpVerification').val()==""){
	 
		
			 
			 if($('#otpVerification').val()=="") {
				 
				swal("Please enter OTP");	 
		       return false;
		        
			 }
		 
	}else if($('#mobileNoVerify').val()!="" && $('#emailIdVerify').val()!="" && $('#otpVerification').val()!=""){
		
			  	var mobileNo = 	$('#mobileNoVerify').val();
			  	var emailId = 	$('#emailIdVerify').val();
				var otp = 	$('#otpVerification').val();
				var MId = 	document.getElementById("MId").value;
				
				var mobile=encrypt(mobileNo);
				var email=encrypt(emailId);
				var flag = 0;
			  		$.ajax
			  		({				
			  			type: 'POST',
			  			data: { mobileNo : mobile , emailId : email , MID: MId, otp: otp },
			  			url: "<%=request.getContextPath()%>/ajaxAction?type=getDetails",
			  			//async:false,
			  			success: function(data)
			  			{
			  		
			  				
			  				if(data=="Y"){
			  					 $("#verifyDiv").hide();
			  					$('#cookienotexist1').empty();
			  			        $("#cookienotexist1").load("cardSaveVas.jsp");
	
			  			    $("#cookienotexist").css("display", "block");
			  					
			  				}else if(data=='OTP expired')
			 				{
			  					$('#otpVerification').val("");
			  				 	flag= 1;
			  		 			swal("OTP has been expired. Please resend the OTP.");
			  					
			  					}else if(data=='OTP not verified successfully'){
			  						$('#otpVerification').val("");
			  					 	flag= 1;
			  						swal("OTP not verified successfully. Please enter correct OTP.");
			  					
			  					}
			  				
			  			},
			  			error: function (err)
			  			{
			  				
			  		    }
			  			
			  		});
			  	
			  		 if(flag==1){
			  			return false;	
			  			}  	
			  		
			  }
		
	 	

	}
</script>

<script type="text/javascript">
function resendNowTimer1() {
	 
	
    document.getElementById("resendNow1").disabled = true;
  
      setTimeout(function(){document.getElementById("resendNow1").disabled = false;}, 60000);  
     
}
    </script>
    
    
    
<script type="text/javascript">
function resendOtp()
    {
	$('#otp').val("");
	var cmobileNo= 	$('#mobileNo').val();
	var cemailId= 	$('#emailId').val();
	var txnId= 	$('#txnId').val();  
	var merchantName= 	$('#merchantName').val();  
	
	sendOtp(cmobileNo, cemailId, txnId, merchantName);
	resendNowTimer();
    		 }
    		 
</script>  	

<script type="text/javascript">
function resendOtp1()
    {
	$('#otpVerification').val("");
	var cmobileNo= 	$('#mobileNoVerify').val();
	var cemailId= 	$('#emailIdVerify').val();
	var txnId= 	$('#txnId').val();  
	var merchantName= 	$('#merchantName').val();
	sendOtp(cmobileNo, cemailId, txnId, merchantName);
	resendNowTimer1();
    		 }
    		 
</script>

<script type="text/javascript">
function sendOtp(cmobileNo, cemailId, txnId, merchantName)
    {
      	
    	var mobileNo= cmobileNo;
    	var emailId= cemailId;
    	var txnId= txnId;
    	
    	
    		$.ajax
    		({				
    			type: 'POST',
    			data: { mobileNo : mobileNo,  emailId : emailId, txnId : txnId, merchantName: merchantName},
    			//url: "/payment/ajaxAction?type=sendOtp",
    			url: "<%=request.getContextPath()%>/ajaxAction?type=sendOtp",	
    			success: function(result)
    			{
    				
    			
    			},
    			error: function (err)
    			{
    				alert("Error in sending otp "+err);
    		    }
    			
    		});
    		 
    		 }
    		 
</script>  		 
 <script>
    function deleteCardWC(divId, Id, tokenReferenceId, cardType, clientRefId, noOfCard) {
    	var MId = document.getElementById("MId").value;
        swal({
            title: "Are you sure?",
            text: "you want to delete!",
            icon: "warning",
            buttons: true,
            dangerMode: true,
        })
         .then((willDelete) => {
            if (willDelete) {
            	
            	$.ajax
    	    	({
    	    		type: 'POST',
    	    		data: {Id : Id, tokenReferenceId : tokenReferenceId, cardType : cardType, clientRefId : clientRefId, MId:MId},
    	    		url: "<%=request.getContextPath()%>/ajaxAction?type=deleteCard",
    	    		success: function(data)
    	    		{
    	    		  
   	    		       if(data=="success"){
   	    		   //	 $( ".red-btn" ).css( "pointer-events", "auto" );
   	    			 $('#payment_mode_drop_down').attr('disabled', true);
   	    			 $('#payment_mode_drop_down2').attr('disabled', true);
   	    			 
   	    			 $( ".payment_dropdown" ).css( "pointer-events", "none" );
   	    			$('#wccr_'+divId).hide();
   	    			
   	    			let flag= 0;
   	    			for (let i = 0; i < noOfCard; i++) {
   	    				
   	    				if($('#wccr_'+i).is(':visible')){
   	    					flag= flag+1;	
   	    				}
   	    				}
   	    			
   	    		
   	    			if(flag==0){	
   	    				 $("#captchaDiv1").hide();
   	    				 $("#button1").hide();
   	    				 removeCookies();
   	    			} 
   	    			swal("Your stored card has been deleted!", { icon: "success",}); 
    	    				
    	    			}
    	    			
   	    		      /*  else 
    	    			{
   	    		    	swal("Error in removing card!", { icon: "success",});
    	    			} */
    	    		 
   	    		
    	    		},
    	    		error: function (err)
    	    		{
    	    			alert("Error in removing card "+err);
    	    	    }
    	    		
    	    	
    	    	});
               
                }
            else {
                swal("Your stored card is safe!");
             }
        }); 
 

      return false;
    }
</script>

 <script>
    function deleteCardWOC(divId, Id, tokenReferenceId, cardType, clientRefId,noOfCard ) {
    	var MId = document.getElementById("MId").value;
        swal({
            title: "Are you sure?",
            text: "you want to delete!",
            icon: "warning",
            buttons: true,
            dangerMode: true,
        })
         .then((willDelete) => {
            if (willDelete) {
            	
            	$.ajax
    	    	({
    	    		type: 'POST',
    	    		data: {Id : Id, tokenReferenceId : tokenReferenceId, cardType : cardType, clientRefId : clientRefId, MId:MId},
    	    		url: "<%=request.getContextPath()%>/ajaxAction?type=deleteCard",
    	    		success: function(data)
    	    		{
    	    		  
   	    		       if(data=="success"){
   	    		 //  	 $( ".red-btn" ).css( "pointer-events", "none" );
   	    			 $('#payment_mode_drop_down').attr('disabled', true);
   	    			 $('#payment_mode_drop_down2').attr('disabled', true);
   	    			 
   	    			 $( ".payment_dropdown" ).css( "pointer-events", "none" );
   	    			$('#woccr_'+divId).hide();
   	    			let flag= 0;
   	    			for (let i = 0; i < noOfCard; i++) {
   	    				
   	    				if($('#woccr_'+i).is(':visible')){
   	    					flag= flag+1;	
   	    				}
   	    				}
   	    			
   	    		
   	    			if(flag==0){	
   	    			 $("#captchaDiv2").hide();
    				 $("#button2").hide();
    				 removeCookies();
   	    			} 
	    			
	    			
   	    			swal("Your stored card has been deleted!", { icon: "success",}); 
    	    				
    	    			}
    	    			
   	    		      /*  else 
    	    			{
   	    		    	swal("Error in removing card!", { icon: "success",});
    	    			} */
    	    		 
   	    		
    	    		},
    	    		error: function (err)
    	    		{
    	    			alert("Error in removing card "+err);
    	    	    }
    	    		
    	    	
    	    	});
               
                }
            else {
                swal("Your stored card is safe!");
             }
        }); 
 

      return false;
    }
</script>

<script type="text/javascript">
function removeCookies() {

/* 	  var res = document.cookie;

	  var multiple = res.split(";");

	 for(var i = 0; i < multiple.length; i++) {

	  var key = multiple[i].split("=");


	  if(key[0].trim()=='mobileNo'){
		
		  document.cookie = key[0]+" =; expires = Thu, 01 Jan 1970 00:00:00 UTC";  
	  }else if(key[0].trim()=='emailId'){
		
		  document.cookie = key[0]+" =; expires = Thu, 01 Jan 1970 00:00:00 UTC";  
	  }
	 

	  } */
	  
	
		$.ajax
		({				
			type: 'POST',
			data: {},
		//	url: "/payment/ajaxAction?type=removeCookie",
			url: "<%=request.getContextPath()%>/ajaxAction?type=removeCookie",	
			success: function(result)
			{
				
			
			},
			error: function (err)
			{
				alert("Error in sending otp "+err);
		    }
			
		});

	  }
</script>


<% log.info("IndexVAS.java ::: pgid = "+pgid+" >>>>>>>>>>>>> javascript validation starts  ");%>
<script src="payfiVAS/js/validationVAS.js"></script>
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