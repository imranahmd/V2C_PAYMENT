<%@page import="com.rew.payment.utility.IndexVASDTO"%>
<%@page import="com.rew.payment.utility.PGUtils"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="com.rew.pg.db.DataManager"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
  <%

  
  
    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Expires", "0");
    response.setDateHeader("Expires", -1);
    response.setDateHeader("Last-Modified", 0);
    
 
%>
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
<!--  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"> 
 --> <script src="payfiVAS/js/creditcard.js"></script>
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

.chb1 {
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

.chb1:checked {
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

 <script>
                   $(".chb1").change(function()
                           {
                               $(".chb1").prop('checked',false);
                               $(this).prop('checked',true);
                           });
                                  </script> 
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
			<% 
			String sCaptcha = PGUtils.RandomCaptcha(6);
			IndexVASDTO dataManager=new IndexVASDTO();
			String keyEncKey = dataManager.getKeyEncKey();
			Logger log = LoggerFactory.getLogger("JSPS.cardSaveVas.jsp");
	 
		
        	if(session.getAttribute("cardDetails")!= null){
       		Vector<List<String>> storedCardDetail= (Vector)session.getAttribute("cardDetails");				
/* 			log.info("storedCardDetail::::"+storedCardDetail.size());
 */			if(storedCardDetail.size()>0){
				
				
				 for( int i = 0 ; i < storedCardDetail.size(); i++) 
                {  
					  
                %>		  
						   
			<!-- <br>	 -->
<%-- 			<input type="hidden" class="" id="noOfCard" name="noOfCard" value= "<%=storedCardDetail.size()%>"> 
 --%>
	<div id="woccr_<%=i%>" style="border: 1px solid #54a8ec;
 padding: 8px;
 border-radius: 20px; margin-top: 20px;">
	<div class="rowDiv">
 <div class="columnDiv" style="box-sizing: border-box; width: 10%; ">
          <div class="round">
   <input type="checkbox" style="text-align: center; visibility: visible; transform: scale(1.2) ; accent-color: #3c8c4b;" class="chb1" name="chb1" id="chb_<%=i%>" onclick="return storedcardFillTokenizeWOC('<%=i%>','<%=storedCardDetail.get(i).get(9)%>',
                          '<%=storedCardDetail.get(i).get(11)%>','<%=storedCardDetail.get(i).get(2)%>',
                           '<%=storedCardDetail.get(i).get(3)%>','<%=storedCardDetail.get(i).get(4)%>','<%=storedCardDetail.get(i).get(8)%>',
                           '<%=keyEncKey%>','<%=storedCardDetail.get(i).get(5)%>','<%=storedCardDetail.get(i).get(6)%>',
                           '<%=storedCardDetail.get(i).get(7)%>','<%=storedCardDetail.size()%>', '<%=storedCardDetail.get(i).get(12)%>', 
                           '<%=storedCardDetail.get(i).get(10)%>', '<%=storedCardDetail.get(i).get(15)%>', )" />
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
 <input type="password" class="" id="woccvv_<%=i%>" name="cvv" placeholder="CVV" maxlength="4" style="width: 50px; border: 1px solid #ccc;
   border-radius: 4px; visibility: hidden; " > 
 </div>

 <div class="columnDiv" style="box-sizing: border-box; ">
    <div class="" style=""><span><%= storedCardDetail.get(i).get(2) %></span> </div>
 </div>
 <div class="columnDiv" style="box-sizing: border-box;">
  <div class="" onclick="deleteCardWOC('<%=i%>','<%=storedCardDetail.get(i).get(14) %>','<%=storedCardDetail.get(i).get(9) %>',
 '<%=storedCardDetail.get(i).get(4) %>','<%=storedCardDetail.get(i).get(13) %>',  '<%=storedCardDetail.size()%>')" style="background-color: white;color: red; border: none; cursor: pointer;" >
 <!-- <i class="fa fa-trash-o" style="font-size:22px;color:red"></i> --><img src="images/deleteimg.png"></div>
 </div>
</div></div>		 
            <%    }		%>
<br>

<div class="capacha col2" id="captchaDiv2">
                   <p class="subHead">Enter the characters visible in the box below</p>
                   <div class="column2 marr8p">
                     <input type="text" placeholder="Enter the characters" id="capacha2" maxlength="7" style="background: none !important;
                     border: 2px solid #c1c1c1; border-radius: 5px; height: 45px;">
                   </div>
                  <!--  <div class="column2 capacha-img"> <img src="payfiVAS/images/capacha.jpg"> </div> -->
                  
                  <div class="column2 capacha-img"

                      style="background-image: url(payfiVAS/images/capacha.jpg); height: 65px; width: 250px; text-align: center;">

                       <font color="maroon" size="7" face="Courier New"><i id ="lblCaptcha2"><%=sCaptcha%></i></font>

                  </div>
                   <div onclick="refreshCaptcha1()" style="padding-top: 14%;padding-bottom: 4%;padding-left: 55%; color: blue;cursor: pointer;text-decoration: underline; margin-right: 0px;">Refresh Captcha</div>
                 </div>
  <div class="btn" id="button2">
                      <button id="payNow" class="red-btn"  onclick="return ValidateCVVSaveCardWOC();" >Pay Now</button> 
                     <%-- <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler?txnId=<%=request.getAttribute("pgid").toString()%>" >cancel</button>   --%>
                       <button type="submit"  class="red-btn"  formaction="RespCancelRequestHandler" >cancel</button>  
                   
                   </div><%	}} %>
                   
                   
                   
</body>


</html>