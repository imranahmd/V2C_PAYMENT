<%@page import="com.rew.pg.dto.MerchantDTO"%>
<%@ include file="/include.jsp"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Vector"%>
<%@page import="com.rew.pg.dto.BankMaster"%>
<%@page import="com.rew.payment.utility.IndexVASDTO"%>

<%@page import="java.util.List"%>
<%@page import="com.rew.pg.db.DataManager"%>
<%@page import="com.rew.pg.dto.TransactionMaster"%>
<%@page import="com.rew.payment.utility.PGUtils"%>
<%@page import="com.apps.trides.crypto.*"%>
<%@page import="com.rew.payment.utility.TripleKeyEnc"%>
<%@page import="java.io.* , java.net.*"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>

<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<!DOCTYPE html>
<html>
<%
Logger log = LoggerFactory.getLogger("JSPS.index.jsp");

HttpSession sessionsa = request.getSession(true);

response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setHeader("Expires", "0");
response.setDateHeader("Expires", -1);
response.setDateHeader("Last-Modified", 0);

DataManager dm = new DataManager();
IndexVASDTO dataManager = new IndexVASDTO();

dataManager = dataManager.getIndexVASDTOData(request.getAttribute("pgid").toString());

ArrayList<String> ecmList = dataManager.getEcmsBanks();

log.info("index.jsp ::: PG ID " + request.getAttribute("pgid").toString()
		+ " page started and datamager object created");
String keyEncKey = dm.getKeyEncKey();
String iv = "3ad5485e60a4fecde36fa49ff63817dc";
TransactionMaster TM = dm.getTxnMaster(request.getAttribute("pgid").toString());

log.info("index.jsp ::: PG ID " + TM.getId() + " first DBconnection established TransactionMaster object created");
// String checksum = request.getParameter("checksum");
String checksum = request.getAttribute("checksum").toString();
log.info("index.jsp :: PG ID : " + request.getAttribute("pgid").toString() + " , checksum : " + checksum);

if (TM != null) {

}
String decCardNo = null, maskedCardNo = null, secretKey = null;

String userAgent = request.getHeader("user-agent");
log.info("index.jsp :: PG ID :::::::::::Mobile::::::::    " + userAgent);
if (userAgent.matches(".*Android.*") || userAgent.matches(".*iPhone.*")) {
	log.info("This se Mobile request");
} else {
	log.info("This Not mobile request");
}
%>


<%
Vector<List<String>> storedCardDetailsCC = (Vector<List<String>>) request.getAttribute("cardDetailsCC");
Vector<List<String>> storedCardDetailsDC = (Vector<List<String>>) request.getAttribute("cardDetailsDC");
boolean creditCardVisibility = false;
boolean debitCardVisibility = false;
boolean netBankVisibility = false;
boolean walletVisibility = false;
boolean bqrVisibility = false;
boolean upiVisibility = false;
boolean atomUPIVisibility = false;
boolean opVisibility = false;
boolean emiVisibility = false;
try {
	log.info("index.jsp :: storedCardDetailsCC -==============>: " + storedCardDetailsCC.toString());

	List returnList = dm.getInstrumentBankList(TM.getMerchantId(), TM.getAmount());
	log.info("index.jsp ::: PG ID " + request.getAttribute("pgid").toString()
	+ " second DBconnection established list returnList created");
	Hashtable instrumentHash = (Hashtable) returnList.get(0);
	//Hashtable bankHash = (Hashtable)returnList.get(1);

	LinkedHashMap<String, String> sortedbankHash = (LinkedHashMap<String, String>) returnList.get(1);
	List<String> Walletlist = dm.getWalletList(TM.getMerchantId());
	log.info("Walletlist:::::::>>>>> " + Walletlist);

	log.info("instrumentHash:::::::>>>>> " + instrumentHash);

	String amount = TM.getAmount();
	String surcharge = TM.getSurcharge();
	log.info("amount" + amount);
	log.info("amount in double " + Double.valueOf(amount));
	log.info("surcharge" + surcharge);
	log.info("surcharge in double" + Double.valueOf(surcharge));
	double total = Double.valueOf(amount) + Double.valueOf(surcharge);
	log.info("total" + total);
	String subTotal = String.format("%.2f", total);
	log.info("total" + subTotal);

	List<String> MerchantPageConfige = dm.getPageConfige(TM.getMerchantId());
	boolean isIntent = true;
	String queryResult = dm.IsEnableUpiorIntent(TM.getMerchantId(), isIntent);// "1";// or "0"; //call dbv

	log.info("MerchantPageConfige:::::::>>>>> " + MerchantPageConfige);
	List<String> banklist = dm.getEMIBankList(TM.getMerchantId());
	log.info("banklist:::::::>>>>> " + banklist);

	if (TM.getInstrumentId().equalsIgnoreCase("NA")) {
		if (instrumentHash.containsKey("CC")) {
	creditCardVisibility = true;
		}
		if (instrumentHash.containsKey("DC")) {
	debitCardVisibility = true;
		}
		if (instrumentHash.containsKey("NB")) {
	netBankVisibility = true;
		}
		if (instrumentHash.containsKey("WALLET")) {
	walletVisibility = true;
		}
		if (instrumentHash.containsKey("BQR")) {
	bqrVisibility = true;
		}
		if (instrumentHash.containsKey("UPI")) {
	upiVisibility = true;
		}
		if (instrumentHash.containsKey("ATOMUPI")) {
	atomUPIVisibility = true;
		}
		if (instrumentHash.containsKey("OP")) {
	opVisibility = true;
		}
		if (MerchantPageConfige != null) {
	emiVisibility = true;
		}
	} else {
		creditCardVisibility = true;
	}
%>
<head>
<style>
::placeholder {
	color: #D3D3D3 !important;
	opacity: 1; /* Firefox */
}

:-ms-input-placeholder { /* Internet Explorer 10-11 */
	color: #D3D3D3 !important;
}

::-ms-input-placeholder { /* Microsoft Edge */
	color: #D3D3D3 !important;
}

.loader {
	position: fixed;
	top: 50%;
	bottom: 50%;
	left: 50%;
	right: 50%;
	margin: auto;
	z-index: 1;
	color: red;
	background-color: red;
}

.overlay {
	width: 100%;
	height: 100%;
	background: #000;
	position: fixed;
	top: 0 !important; /* slight offeset to allow toggle button to work */
	left: 0;
	/*display:none;*/
	/* 0.4 = 40% */
	opacity: 0.7;
	filter: alpha(opacity = 40); /* For IE8 and earlier */
}

.over-top {
	z-index: 999999;
}

.blink_me1 {
	animation: blinker 1s linear infinite;
}

.blink_me2 {
	animation: blinker 2s linear infinite;
}

.blink_me3 {
	animation: blinker 3s linear infinite;
}

@
keyframes blinker { 50% {
	opacity: 0;
}
}
</style>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title></title>


<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"
	integrity="sha512-KfkfwYDsLkIlwQp6LFnl8zNdLGxu9YAA1QvwINks4PhcElQSvqcyVLLD9aMhXd13uQjoXtEKNosOWaZqXgel0g=="
	crossorigin="anonymous" referrerpolicy="no-referrer" />
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.0.0/crypto-js.min.js"></script>
<link href="assets/css/bootstrap.css" rel="stylesheet" />
<link href="assets/css/fonts/font.css" rel="stylesheet" />
<link href="assets/css/style.css" rel="stylesheet" />
<script src="assets/js/jquery-3.5.1.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script type="text/javascript"
	src="https://code.jquery.com/jquery-1.6.2.js"></script>
<script src="assets/js/bootstrap.min.js"></script>
<script src="assets/js/sweetalert.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2"
	crossorigin="anonymous"></script>
<%--    <script src="/docs/5.2/dist/js/bootstrap.bundle.min.js"--%>
<%--            integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2"--%>
<%--            crossorigin="anonymous"></script>--%>
<script src="newjs/index.js"></script>
<script src="assets/js/creditcardutils.js"></script>
<script src="assets/js/creditcardutilsOne.js"></script>
<script src="newjs/creditcard.js"></script>

<!--  <script src="newjs/index1.js"></script> -->
<script src="newjs/otherjs.js"></script>
<!--  <script src="newjs/otherjs.js"></script> -->
<script src="newjs/stripe.js"></script>


<style>
.select_15 {
	border: 0.1px solid #D3D3D3;
	border-radius: 4px;
	padding: 0.6px;
}

/*the container must be positioned relative:*/
.custom-select {
	position: relative;
	font-family: Arial;
}

.custom-select select {
	display: none !important; /*hide original SELECT element:*/
}

.select-selected {
	background-color: #e0e9f7 !important;
}

/*style the arrow inside the select element:*/
.select-selected:after {
	position: absolute !important;
	content: "";
	top: 14px;
	right: 10px;
	width: 0;
	height: 0;
	border: 6px solid transparent;
	border-color: DodgerBlue transparent transparent transparent;
}

/*point the arrow upwards when the select box is open (active):*/
.select-selected.select-arrow-active:after {
	border-color: transparent DodgerBlue DodgerBlue DodgerBlue !important;
	top: 7px;
}

/*style the items (options), including the selected item:*/
.select-items div, .select-selected {
	color: DodgerBlue;
	padding: 8px 16px;
	border: 1px solid transparent;
	border-color: transparent transparent rgba(0, 0, 0, 0.1) transparent;
	cursor: pointer;
	user-select: none;
}

/*style items (options):*/
.select-items {
	position: absolute;
	height: 200px !important;
	overflow: scroll;
	background-color: #ffffff !important;
	top: 100%;
	left: 0;
	right: 0;
	z-index: 99;
}

/*hide the items when the select box is closed:*/
.select-hide {
	display: none;
}

.select-items div:hover, .same-as-selected {
	background-color: rgba(0, 0, 0, 0.1);
}

.google-pay-vpa-logo, .bhim-vpa-logo, .phonepe-vpa-logo, .paytm-vpa-logo,
	.paytm-vpa-logo, .payzapp-vpa-logo {
	background-image: url('assets/images/g-pay.png');
	width: 56px;
	height: 48px;
	display: inline-block;
	filter: grayscale(100%);
	background-repeat: no-repeat;
	background-size: 100% 100%;
}

.bhim-vpa-logo {
	background-image: url('assets/images/bhim.png');
	background-size: 100% 100%;
}

.phonepe-vpa-logo {
	background-image: url('assets/images/phone-pay.png');
	height: 56px;
}

.paytm-vpa-logo {
	background-image: url('assets/images/paytm.png');
	width: 50px;
	height: 56px;
}

.payzapp-vpa-logo {
	background-image: url('assets/images/payzapp.png');
	width: 56px;
}

.select {
	filter: grayscale(0);
	box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.08);
	border-color: #004a97 !important;
}

.row1 {
	display: -ms-flexbox; /* IE10 */
	display: flex;
	-ms-flex-wrap: wrap; /* IE10 */
	flex-wrap: wrap;
	margin: 0 -16px;
}

.vpa-enter-warpper, .vpa-enter-warpper::before, .vpa-enter-warpper::after
	{
	display: block;
	position: relative;
	box-sizing: border-box;
}

.vpa-other-app {
	display: inline-block !important;
	line-height: 68px !important;
	font-size: 23px !important;
	color: #A8A8A8 !important;
}

.right-section-inner .btn:focus .google-pay-vpa-logo,
	.right-section-inner .btn:focus .bhim-vpa-logo, .right-section-inner .btn:focus .phonepe-vpa-logo,
	.right-section-inner .btn:focus .paytm-vpa-logo, .right-section-inner .btn:focus .payzapp-vpa-logo
	{
	filter: grayscale(0);
}

.upi-input-wrapper {
	border: 1px dashed #100f0f;;
	border-radius: 11px;
	padding: 10px 15px;
}

.upi-lable-text, .upi-app-description {
	width: 100%;
	color: #A8A8A8;
	font-size: 16px;
}
 .icon {
            vertical-align: middle;
            margin-right: 5px;
        }

.upi-input-wrapper .upi-lable-text {
	display: flex;
	justify-content: space-between;
}

.upi-app-description {
	padding-top: 24px;
	text-align: left;
}

.upi-lable-text a {
	color: #004a97;
}

.form-inline {
	display: flex;
	width: 100%;
}

.form-inline1 {
	display: flex;
	width: 100%;
}

.form-inline-otherApp {
	display: block;
	width: 100%;
}

.form-inline2 {
	display: none;
}

.upi-app-description span {
	font-weight: 700;
	display: inline-block;
	color: #a8a8a8;
}

.verify-cta-btn-wrapper {
	text-align: center;
	margin: 36px 0 0;
}

.verify-cta-btn-wrapper a {
	background: #ED1C24;
	box-shadow: 0px 7px 0px #AA050B;
	border-radius: 11px;
	width: 277px;
	height: 67px;
	line-height: 67px;
	display: inline-block;
	text-align: center;
	color: #fff;
}

.vpa-enter-wrapper .cancel-btn {
	margin-top: 20px;
}

.upi-input-wrapper .input-wrapper {
	margin: 8px 0 0;
}

.upi-input-wrapper .input-text-area {
	width: 80%;
	color: #E5E5E5;
	font-size: 20px;
}

.upi-input-wrapper .input-text-area input {
	width: 110%;
	height: 54px !important;
	line-height: 54px;
	border: 2px solid #E5E5E5 !important;
	border-right: none !important;
	border-radius: 11px 0 0 11px !important;
}

.upi-input-wrapper .input-text-area-otherApp input {
	width: 100%;
	height: 54px !important;
	line-height: 54px;
	border: 2px solid #E5E5E5;
	border-radius: 11px 11px 11px 11px;
}

.other-app {
	width: 100%;
	height: 40px;
	line-height: 54px;
	border: 2px solid #E5E5E5;
	border-radius: 11px 11px 11px 11px;
}

.upi-input-wrapper .fix-upi {
	width: 30%;
}

.multipleOption {
	background: #F2F3F4;
	border: 2px solid #E5E5E5;
	border-left: none;
	box-sizing: border-box;
	border-radius: 0px 11px 11px 0px;
	color: #212121;
	font-size: 18px;
	text-align: center;
	height: 54px;
	box-shadow: 0px 8px 16px 0px rgb(0 0 0/ 3%);
	background: url('assets/images/down_arrow_gray.png') no-repeat;
	background-repeat: no-repeat;
	background-size: 11px 11px;
	background-position: 125px 20px;
	background-color: #F2F3F4;
	-webkit-appearance: none;
	overflow: hidden;
	text-overflow: ellipsis;
}

.singleOption {
	background: #F2F3F4;
	border: 2px solid #E5E5E5;
	border-left: none;
	box-sizing: border-box;
	border-radius: 0px 11px 11px 0px;
	color: #212121;
	font-size: 18px;
	text-align: center;
	height: 54px;
	box-shadow: 0px 8px 16px 0px rgb(0 0 0/ 3%);
	appearance: none;
	-webkit-appearance: none;
}

.upi-input-wrapper select option {
	text-decoration: underline;
	border: blue;
}

.inputUPI23 select {
	-moz-appearance: none; /* Firefox */
	-webkit-appearance: none; /* Safari and Chrome */
	appearance: none;
}

select option {
	margin: 40px;
	background: #fff;
	color: #212121;
	text-shadow: 0 1px 0 rgba(0, 0, 0, 0.4);
	text-align: initial;
	border-bottom-left-radius: 0px;
	border-bottom-right-radius: 0px;
	border-bottom-right-radius: 6px;
	border-bottom-left-radius: 6px;
}

.upi-input-wrapper .form-control:focus {
	box-shadow: none;
}

input:focus-visible {
	outline: none;
}

select#inputUPI:focus-visible {
	outline: none;
}

@media screen and (max-width: 768px) {
	.home-banner {
		margin: 10px 0;
	}
	.upi-btn-card {
		display: block;
		margin: 0 auto 15px;
		font-size: 16px;
	}
	.m-top-sm-0 {
		margin: 0 !important;
	}
	.billing-information {
		padding: 15px 20px;
		border-radius: 10px 10px 0 0;
	}
	.right-section {
		border-radius: 0 0 10px 10px;
	}
	.billing-information .heading {
		font-size: 20px;
	}
	.merchant-demo {
		padding: 10px 20px;
	}
	.amount-info {
		font-size: 14px;
	}
	.upi-app-text {
		font-size: 14px;
		padding: 14px 0;
		line-height: 17px;
	}
	.vpa-enter-list-wrapper button {
		margin: 22px;
	}
	.right-section-inner {
		padding: 20px;
	}
	.cancel-btn {
		margin-top: 45px;
	}
	footer .logo {
		width: 100%;
		margin-bottom: 28px;
		margin-top: -20px;
	}
	footer .paymentgetway {
		height: 33px;
		max-width: 50%;
	}
	.upi-lable-text {
		font-size: 14px;
	}
	.upi-input-wrapper .fix-upi select {
		font-size: 14px;
		line-height: 40px;
	}
	.verify-cta-btn-wrapper {
		margin: 20px 0 0;
	}
	.verify-cta-btn-wrapper a {
		width: 180px;
		height: 44px;
		line-height: 44px;
		font-size: 14px;
	}
	.vpa-other-app {
		font-size: 16px;
		width: 100px;
		line-height: 1 !important;
	}
	.upi-lable-text, .upi-app-description {
		font-size: 14px;
		line-height: 17px;
		padding-top: 14px;
	}
	.model-header-text {
		font-size: 14px;
		margin: 0;
	}
	.body-text-modal ol li {
		font-size: 14px;
		margin-bottom: 8px;
		line-height: 28px;
	}
	.modal-body {
		padding-bottom: 0;
	}
	.cancel-modal-heading {
		font-size: 14px;
		line-height: 17px;
		margin-bottom: 18px;
	}
	.payment-cancel-modal-text {
		font-size: 12px;
	}
	.cancel-btn-wrapper {
		margin-top: 40px;
	}
	.loading-note-wrapper .icon {
		width: 24px;
		height: 24px;
		margin-right: 15px;
		background-size: 100% 100%;
	}
	.loading-note-wrapper .text, .loading-note-wrapper .text span {
		font-size: 12px;
	}
	.loading-description-text {
		font-size: 12px;
	}
	.upi-mobile-icon {
		width: 30px;
		height: 44px;
		background-size: 100% 100%;
		margin-right: 14px;
	}
	/*  .loader {
                border: 8px solid #F2F3F4;
                border-top: 8px solid #19B600;
                width: 86px;
                height: 86px;
            } */
	.lading-graph-wrapper .time {
		font-size: 18px;
	}
	.lading-graph-wrapper .time span {
		line-height: 17px;
	}
	.lading-graph-wrapper {
		margin: 20px 0 10px;
	}
	.payment-request-text {
		font-size: 14px;
		line-height: 19px;
		margin-left: 14px;
	}
	.cancel-btn-wrapper a {
		font-size: 14px;
	}
	.upi-input-wrapper select {
		font-size: 14px !important;
	}
	.centered-div {
		text-align: center;
	}

	/* Style for the image (adjust as needed) */
	.centered-div img {
		display: inline-block;
	}
}

@media screen and (max-width: 1024px) {
	.upi-btn-card {
		margin-bottom: 15px;
	}
	.upi-btn-card {
		width: 170px;
		height: 84px;
	}
	.outer-wrapper {
		width: 94%;
	}
}
</style>
</head>
<body oncontextmenu="return false" id="body">

	<!-- SOF Header -->
	<header>
		<div class="container">
			<div class="row">
				<div class="col-md-6 logo">
					<a href="#"><%=dm.getMerchant(TM.getMerchantId()).getName()%> </a>
				</div>
				<div class="col-md-6 lft_logo">
					<a href="#"> <img
									src="assets/images/payfiLogo.png" 
									width="80px" /><!-- <img height="90">PAY --><!-- <img height="90">PAY -->
					</a>
				</div>
			</div>
		</div>
	</header>
	<!-- EOF Header -->

	<!-- SOF BTN -->
	<section class="payment">
		<div class="container-fluid">
			<div class="container">
				<div class="row pymt_main_data ">
					<div class="col-md-12 col-lg-3 card data_lft_blk" id="addclass">

						<div class="check" id="addSelectClass">
							<ul>
								<%
								if (TM.getInstrumentId().equalsIgnoreCase("NA")) {
									log.info("mobile menu nistrument id ++++++++++++++++++++++++++++++++++++++++++" + TM.getInstrumentId());
									if (instrumentHash.containsKey("CC")) {
								%>
								<li id="op-pcredit" class="option side_nav"><img
									src="assets/images/icredit.png" onclick="resetSurcharge();"
									width="25px" /> <a href="#" onclick="resetSurcharge();">Credit
										Card</a> <span onclick="resetSurcharge();">Visa,
										MasterCard, RuPay & More</span></li>
								<%
								}
								if (instrumentHash.containsKey("DC")) {
								%>
								<li id="op-pdebit" onchange="resetSurcharge()"
									class="option side_nav"><img
									src="assets/images/idebit.png" onclick="resetSurcharge();"
									width="25px" /> <a href="#" onclick="resetSurcharge();">Debit
										Card</a> <span onclick="resetSurcharge();">Visa,
										MasterCard, RuPay & More</span></li>
								<%
								}
								if (instrumentHash.containsKey("WALLET")) {
								%>
								<li id="op-pwallet" class="option side_nav"><img
									src="assets/images/iwallet.png" onclick="resetSurcharge();"
									width="25px" /> <a href="#" onclick="resetSurcharge();">Wallet</a>
									<span onclick="resetSurcharge();">Freecharge & More</span></li>
								<%
								}
								if (instrumentHash.containsKey("UPI")) {
								%>
								<li id="op-pupi" class="option side_nav"><img
									src="assets/images/iupi.png"  onclick="getSurchargeUPI();" width="20px" /> <a href="#"
									onclick="getSurchargeUPI();">UPI</a> <!-- /QR --> <span>GPay,
										PhonePe & More</span></li>
								<%
								}
								if (instrumentHash.containsKey("NB")) {
								%>
								<li id="op-pnetbanking" class="option side_nav"><img
									src="assets/images/inet.png" onclick="resetSurcharge();"
									width="25px" /> <a href="#" onclick="resetSurcharge();">Net
										Banking</a> <span>All Indian Bank</span></li>
								<%
								}
								if (instrumentHash.containsKey("OP")) {
								%>
								<li id="op-poffline" class="option side_nav"><img
									src="assets/images/inet.png" onclick="resetSurcharge();"
									width="25px" /> <a href="#" onclick="resetSurcharge();">Offline
										Payment</a> <span>RTGS/NEFT</span></li>
								<%
								}
								if (instrumentHash.containsKey("BQR")) {
								%>
								<li id="op-6" class="option side_nav"><img
									src="assets/images/iwallet.png" onclick="resetSurcharge();"
									width="25px" /> <a href="#" onclick="resetSurcharge();">BQR</a>
									<span onclick="resetSurcharge();">BQR & More</span></li>
								<%
								}
								if (instrumentHash.containsKey("CC") && storedCardDetailsCC.size() > 0) {
								%>
								<li id="op-psavecard" class="option side_nav"><img
									src="assets/images/isave.png" onclick="resetSurcharge();"
									width="25px"> <a href="#" onclick="resetSurcharge();">Save
										Cards</a> <span onclick="resetSurcharge();">Visa,
										MasterCard, RuPay & More</span></li>
								<%-- <%
								}

								if ((userAgent.matches(".*Android.*") || userAgent.matches(".*iPhone.*")) && instrumentHash.containsKey("IsIntent")) {
								%>
								<li id="op-mobile" class="option side_nav"><img
									src="assets/images/iupi.png" width="20px" /> <a href="#"
									onclick="payMobile();">Pay Via Mobile <!--  <a href="#MobileModal" id="modals" data-bs-toggle="modal" 
                                     data-controls-modal="#MobileModal" data-backdrop="static" data-keyboard="false" 
                                     data-bs-target="#MobileModal"> -->

								</a> <span onclick="payMobile();">Mobile, Smartphone & More</span></li> --%>
								<%
								}

								if (MerchantPageConfige != null) {
								%>
								<li id="op-pemi" class="option side_nav"><img
									src="assets/images/ipayment.png" onclick="resetSurcharge();"
									width="25px" /> <a href="#" onclick="resetSurcharge();">EMI</a>
									<span onclick="resetSurcharge();">EMI & More</span></li>

								<%
								}
								} else {
								}
								%>

								<%--                            <li id="op-psavecard" class="option side_nav">--%>
								<%--                                <img src="assets/images/isave.png" width="25px">--%>
								<%--                                <a href="#">Save Cards</a>--%>
								<%--                                <span>Visa, MasterCard, RuPay & More</span>--%>
								<%--                            </li>--%>
							</ul>
						</div>
					</div>
					<div class="col-md-7 col-lg-6 card data_mid_blk">

						<%
						if (instrumentHash.containsKey("CC")) {
						%>
						<!-- SOF Credit Card -->
						<div class="item credit" id="pcredit" style="display: block;">
							<form action="PGRouter" method="post" id="pcredit_form"
								class="datpayment-form">

								<input type="hidden" id="instrumentId" name="instrumentId"
									value="CC" /> <input type="hidden" name="txnId" id="txnId"
									value="<%=request.getAttribute("pgid").toString()%>" /> <input
									type="hidden" name="keyEncKey" value="<%=keyEncKey%>" /> <input
									type="hidden" name="cardType" class="dc" id="cccardType"
									value="" /> <input type="hidden" name="checksum"
									value="<%=checksum%>" /> <input type="hidden"
									pattern="[0-9]{10}" class="phone-input name-field"
									maxlength="10" id="phone" required="number" placeholder="Phone" />
								<input type="hidden" class="mail-input email-field"
									placeholder="Email ID" id="email" required /> <input
									type="hidden" id="CCexpiry" name="expiry" /> <input
									type="hidden" id="cNameCC" name="cName" />
								<h4>Pay Using Credit Cards</h4>
								<div class="form_blk" id="cr-card-no">
									<label for="" class="form-label">Credit Card Number</label>
									<!-- <input
                                type="text" oninput='validate(event)' id=""  class="form-control card-number"
                                oninput='validate(event)' name="cardNo" placeholder="Credit Card Number"
                                              onkeypress="return checkDigit(event)"
                                              onkeyup="return onkeyupCard(event)"> -->
									<input type="text"
										class="form-control text-muted creditcardutils"
										id="card-number" name="cardNo" oninput='validate(event)'
										onkeypress="return checkDigit(event)"
										onkeyup="return onkeyupCard(event)"
										onkeypress='validate(event)' type="tel"
										placeholder="Enter your Card Number" maxlength='20'>
									<!--                                        placeholder="0000 0000 0000 0000" -->

									<span id='cardImg'></span> <i class="info fa fa-info-circle"
										style="font-size: 16px;"></i> <span
										class="text-muted info-fade-out">Enter the 16-digit
										card number, on your card</span>
								</div>
								<div class="form_blk">
									<label for="" class="form-label">Card Holder Name</label> <input
										type="text" maxlength="20" class="form-control txtOnly"
										id="card-holder" name="cardName"
										placeholder="Card Holder Name"> <i
										class="info fa fa-info-circle" style="font-size: 16px;"></i> <span
										class="text-muted info-fade-out">Enter name on the card</span>
								</div>

								<div class="form_blk form_blk_lft">
									<label for="" class="form-label">CVV</label> <input
										type="password" maxlength="4" class="form-control"
										id="card-cvc" name="cvv" placeholder="Enter CVV"
										onclick="validate_cvv(cvv)" pattern="[0-9]+"
										oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
									<i class="info fa fa-info-circle" style="font-size: 16px;"></i>
									<span class="text-muted info-fade-out">Enter 3-4 digit
										number on the card</span>
								</div>
								<div class="form_blk form_blk_rht">
									<label for="" class="form-label">Expiry Date</label> <input
										type="tel" maxlength="5" class="form-control"
										name="cardExpiry" id="cardExpiry" placeholder="MM/YY"
										onkeyup="formatString(event);"> <i
										class="info fa fa-info-circle" style="font-size: 16px;"></i> <span
										class="text-muted info-fade-out">Enter expiry date on
										the card</span>
								</div>
								<div class="form_blk text-start">
									<input type="checkbox" name="isRemember" id="isCCRemember"
										value="N" /> <label for="isCCRemember">Save this card
										for future transaction</label> <i class="info fa fa-info-circle"
										style="font-size: 16px;"></i> <span
										class="text-muted text-start info-fade-out">( This card
										will be securely saved for a faster payment experience. CVV
										number will not be saved )</span>
								</div>

								<!-- 						<input type="submit" value="submit">
    -->
								<%--                        function testCreditCard() avaliable in newjs/customjs.js--%>
								<%--                        <input--%>
								<%--                                type="submit" id="payNowCC1" class="pay-now" style="border: 0px"--%>
								<%--                                value="PAY NOW"--%>
								<%--                                onclick="return testCreditCard('card-number','ccForm','card-cvc','card-holder','cc-cvv-mob',event,'cardExpiry')">--%>
								<input type="hidden" id="cc-cvv-mob" name="cvv1" value="" /> <input
									type="hidden" id="card-month" name="card-month" value="" /> <input
									type="hidden" id="card-year" name="card-year" value="" />

							</form>

						</div>
						<%
						}
						%>

						<%
						if (instrumentHash.containsKey("DC")) {
						%>
						<!-- SOF Debit Card -->
						<div class="item credit" id="pdebit" style="display: block;">
							<form action="PGRouter" method="POST" id="pdebit_form"
								class="datpayment-form">

								<input type="hidden" name="instrumentId" id="instrumentId"
									value="DC" /> <input type="hidden" name="txnId" id="txnId"
									value="<%=request.getAttribute("pgid").toString()%>" /> <input
									type="hidden" name="keyEncKey" value="<%=keyEncKey%>" /> <input
									type="hidden" name="cardType" class="dc" id="dccardType"
									value="" /> <input type="hidden" name="checksum"
									value="<%=checksum%>" /> <input type="hidden"
									placeholder="Phone" maxlength="10" pattern="[0-9]{10}"
									id="phone1" class="phone-input name-field" /> <input
									type="hidden" class="mail-input email-field"
									placeholder="Email ID" id="email1" /> <input type="hidden"
									id="cNameDC" name="cName" /> <input type="hidden"
									id="DCExpiry" name="expiry" />
								<h4>Pay Using Debit Cards</h4>

								<div class="form_blk" id="cr-card-no1">

									<label for="" class="form-label">Debit Card Number</label> <input
										type="text" class="form-control creditcardutilsOne"
										id="card-number1" name="cardNo" oninput='validate(event)'
										onkeypress="return checkDigitOne(event)"
										onkeyup='return onkeyupCardOne(event)'
										onkeypress='validate(event)' type="tel"
										placeholder="Enter your Card Number" maxlength='20'>
									<!--                                                placeholder="0000 0000 0000 0000" -->
									<!--                                         onblur="if(this.value == '') {this.value = '0000 0000 0000 0000 0000';}" -->
									<span id='cardImgOne'></span> <i class="info fa fa-info-circle"
										style="font-size: 16px;"></i> <span
										class="text-muted info-fade-out">Enter the 16 digit
										card number, on your card</span>

								</div>
								<div class="form_blk">
									<label for="" class="form-label">Card Holder Name</label> <input
										type="text" maxlength="20" class="form-control txtOnly"
										id="card-holder1" name="cardName"
										placeholder="Card Holder Name"> <i
										class="info fa fa-info-circle" style="font-size: 16px;"></i> <span
										class="text-muted info-fade-out">Enter name on the card</span>
								</div>
								<div class="form_blk form_blk_lft">
									<label for="" class="form-label">CVV</label> <input
										type="password" maxlength="4" id="card-cvc1" name="cvv"
										class="form-control cvv" placeholder="Enter CVV"
										pattern="[0-9]+" onclick="validate_cvv(cvv)"
										oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');">
									<!--                                         onblur="if (this.value == '') {this.value = 'xxx';}" -->
									<i class="info fa fa-info-circle" style="font-size: 16px;"></i>
									<span class="text-muted info-fade-out">Enter 3-4 digit
										number on the card</span>

								</div>
								<div class="form_blk form_blk_rht">
									<label for="" class="form-label">Expiry Date</label> <input
										type="tel" maxlength="5" class="form-control"
										name="cardExpiry1" id="cardExpiry1" placeholder="MM/YY"
										onkeyup="formatString(event);" /> <i
										class="info fa fa-info-circle" style="font-size: 16px;"></i> <span
										class="text-muted info-fade-out">Enter expiry date on
										the card</span>
								</div>
								<div class="form_blk">
									<input type="checkbox" name="isRemember" id="isDCRemember"
										value="N" /> <label for="isDCRemember">Save this card
										for future transaction</label> <i class="info fa fa-info-circle"
										style="font-size: 16px;"></i> <span
										class="text-muted text-start info-fade-out">( This card
										will be securely saved for a faster payment experience. CVV
										number will not be saved )</span>
								</div>
								<input type="hidden" id="card-month1" name="card-month" value="" />
								<input type="hidden" id="card-year1" name="card-year" value="" />
								<input type="hidden" name="cardType" class="dc" id="dccardType"
									value="" /> <input type="hidden" name="cardType" class="dc"
									id="emicardType" value="" />

								<%--                        <input id="payNowDC1" type="submit" class="pay-now" style="border: 0px" value="PAY NOW DC"--%>
								<%--                               onclick="return testCreditCard('card-number1','dcForm','card-cvc1','card-holder1','dc-cvv-mob',event,'cardExpiry1')">--%>
								<input type="hidden" id="dc-cvv-mob" name="cvv1" value="" />
							</form>

						</div>
						<%
						}
						%>

						<%
						if (instrumentHash.containsKey("WALLET")) {
						%>
						<!-- SOF Wallets -->
						<div class="item credit" id="pwallet">
							<form id="pwallet_form" name="walletform" method="post"
								action="PGRouter">
								<input type="hidden" id="instrumentId" name="instrumentId"
									value="WALLET"> <input type="hidden" name="txnId"
									id="txnId" value="<%=request.getAttribute("pgid").toString()%>">
								<input type="hidden" name="checksum" value="<%=checksum%>">
								<input type="hidden" id="emioption" name="emioption" value="NA">
								<input type="hidden" id="cardType" name="cardType"
									value="Wallet">

								<h4>Pay Using Wallets</h4>
								<ul class="upi">
									<%
									for (int i = 0; i < Walletlist.size(); i++) {
									%>
									<%
									switch (Walletlist.get(i)) {
										case "PayzApp" :
									%>
									<li><a href="#"
										onclick="DynamicValue('PayzApp','Wallet-bank-select')"><img
											src="images/payzapp-large.png" width="60px"></a></li>
									<%
									break;
									case "Paytm" :
									%>
									<li><a href="#"
										onclick="DynamicValue('Paytm','Wallet-bank-select')"><img
											src="assets/images/paytm.png" width="60px"></a></li>
									<%
									break;
									case "apay" :
									%>
									<li><a href="#"
										onclick="DynamicValue('apay','Wallet-bank-select')"><img
											src="assets/images/apay.png" width="60px"></a></li>
									<%
									break;
									case "gpay" :
									%>
									<li><a href="#"
										onclick="DynamicValue('gpay','Wallet-bank-select')"><img
											src="assets/images/gpay.png" width="60px"></a></li>
									<%
									break;

									case "ppay" :
									%>
									<li><a href="#"
										onclick="DynamicValue('ppay','Wallet-bank-select')"><img
											src="assets/images/ppay.png" width="60px"></a></li>
									<%
									break;
									case "Freecharge" :
									%>
									<li><a href="#"
										onclick="DynamicValue('Freecharge','Wallet-bank-select')"><img
											src="assets/images/FreeCharge.png" width="60px"></a></li>
									<%
									break;

									default :
										out.println("It's Saturday.");
									}
									%>

									<%
									}
									%>
								</ul>
								<div class="form_blk pt_15">
									<label for="" class="form-label">Select wallet</label>
									<div class="custom-select">
										<select id="bank-select" style="position: relative;"
											name="bank" aria-label=".form-select-sm example">
											<option value="">Select a Wallet</option>
											<%
											for (int i = 0; i < Walletlist.size(); i++) {
											%>
											<option value="<%=Walletlist.get(i)%>"><%=Walletlist.get(i)%>
											</option>
											<%
											}
											%>

										</select>
									</div>
									<%--                            function ValidateWallet() present in payfiVAS/js/ppvalidationVAS.js--%>
									<%--                            <input type="submit" onclick="ValidateWallet()" id="payNowWallet" style="border: 0px"--%>
									<%--                                   value="PAY NOW">--%>
								</div>
								<!--  <p>UPI is a unique payment address linked to a
                                person bank account for payments</p> -->

							</form>
						</div>
						<%
						}
						%>

						<%
						if (instrumentHash.containsKey("UPI")) {
						%>
						<!-- SOF UPI -->
						<div class="item credit text-center" id="pupi">
						<h4 id="backbtn" class="hideButton deskview" style="text-align: left; color: #004a97"><img height="40" onclick="showDiv()"style="color:blue" src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiA/PjxzdmcgaWQ9IkxheWVyXzEiIHN0eWxlPSJlbmFibGUtYmFja2dyb3VuZDpuZXcgMCAwIDUxMiA1MTI7IiB2ZXJzaW9uPSIxLjEiIHZpZXdCb3g9IjAgMCA1MTIgNTEyIiB4bWw6c3BhY2U9InByZXNlcnZlIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIj48cGF0aCBkPSJNNTAuMiwxNjkuNkM5Ny45LDU2LjMsMjI4LjUsMy4xLDM0MS44LDUwLjlzMTY2LjQsMTc4LjMsMTE4LjcsMjkxLjZTMjgyLjIsNTA4LjksMTY4LjksNDYxLjEgIEM4Ni40LDQyNi40LDMyLjcsMzQ1LjUsMzIuNywyNTZDMzIuNywyMjYuMywzOC42LDE5Ni45LDUwLjIsMTY5LjZ6IE0yNTQuNyw0NDhjMTA2LDAsMTkyLTg2LDE5Mi0xOTJzLTg2LTE5Mi0xOTItMTkyICBzLTE5Miw4Ni0xOTIsMTkyUzE0OC43LDQ0OCwyNTQuNyw0NDh6IE0yMjYuNywxNDAuN0wxMTEuNCwyNTZsMTE1LjMsMTE1LjNsMjEuMi0yMS4yTDE2OC43LDI3MWgyMDguMnYtMzBIMTY4LjdsNzkuMS03OS4xICBMMjI2LjcsMTQwLjd6Ii8+PC9zdmc+" alt="Red dot" /></h4>
							<h4 style="text-align: left; color: #004a97">Pay Using
								Mobile/QR</h4>
							
							<%--                        <a href="#" class="text-center"><img src="images/bhim-large.png"--%>
							<%--                        ></a>--%>


							<ul class="upi" id="upiFirstScreen">
								<%--                            <li><a href="#" onclick="upiBtnClick('APay'); getSelectedUPI('APay')">--%>
								<%--                                <img src="assets/images/apay.png" width="100px"></a>--%>
								<%--                            </li>--%>

								<li class="box"><a href="#"  id="BHIM" class="BHIM"
									onclick="selectRadioButtonByValue('BHIM'); getSelectedUPI('BHIM')"><img
										src="assets/images/bhim-large.png" width="100px"></a></li>
								<li class="box"><a href="#" id="gpay" class="gpay"
									onclick="selectRadioButtonByValue('gpay'); getSelectedUPI('GPay')"><img
										src="assets/images/gpay.png" width="80px"></a></li>
								<li class="box"><a href="#" id="phonepe" class="phonepe"
									onclick="selectRadioButtonByValue('phonepe'); getSelectedUPI('PhonePe')"><img
										src="assets/images/ppay.png" width="100px"></a></li>
								<li class="box"><a href="#" id="paytm" class="paytm"
									onclick="selectRadioButtonByValue('Paytm'); getSelectedUPI('Paytm')"><img
										src="assets/images/paytm.png" width="100px"></a></li>
								<li class="box"><a href="#" id="payzapp" class="payzapp"
									onclick="selectRadioButtonByValue('PayZapp'); getSelectedUPI('PayZapp')"><img
										src="assets/images/payzapp-large.png" width="100px"></a></li>
								<li class="box"><a href="#"  id="otherApp" class="otherApp"
									onclick="selectRadioButtonByValue('otherApp');"><span>Other
											Apps</span></a></li>

							</ul>
							<input type="radio" id="payment_method" hidden
								name="chk_upi_payment" value="BHIM"> <input type="radio"
								hidden id="payment_method" name="chk_upi_payment" value="gpay">
							<input type="radio" hidden id="payment_method"
								name="chk_upi_payment" value="phonepe"> <input
								type="radio" hidden id="payment_method" name="chk_upi_payment"
								value="Paytm"> <input type="radio" hidden
								id="payment_method" name="chk_upi_payment" value="PayZapp">
							<input type="radio" hidden id="payment_method"
								name="chk_upi_payment" value="otherApp">
							&nbsp;&nbsp;&nbsp;
							<div class="form_blk rdo_btn">
								 <%
    if (instrumentHash.containsKey("UPI") && instrumentHash.get("isintentUPI").equals("0")) {
    %>
    <div class="form-check form-check-inline">
        <input class="form-check-input" type="radio" name="inlineRadioOptions" id="checkone" value="UPIMode" checked>
        <label class="form-check-label">
            <strong>Pay With UPI ID</strong>
        </label>
    </div>
     <div class="form-check form-check-inline" style=display:none>
        <input class="form-check-input" type="radio" name="inlineRadioOptions" id="showPayement" value="showPayementDiv">
        <label class="form-check-label">
            <strong>Pay With UPI QR Code</strong>
        </label>
    </div>
    <%
    } else if (instrumentHash.containsKey("UPI") && instrumentHash.get("isintentUPI").equals("1")) {
    %>
    <div class="form-check form-check-inline">
        <input class="form-check-input" type="radio" name="inlineRadioOptions" id="showPayement" value="showPayementDiv">
        <label class="form-check-label">
            <strong>Pay With UPI QR Code</strong>
        </label>
    </div>
    <%
    } else if (instrumentHash.containsKey("UPI") && instrumentHash.get("isintentUPI").equals("2")) {
    %>
    <!-- Show both radio buttons when isIntent is 2 -->
    <div class="form-check form-check-inline">
        <input class="form-check-input" type="radio" name="inlineRadioOptions" id="checkone" value="UPIMode">
        <label class="form-check-label">
            <strong>Pay With UPI ID</strong>
        </label>
    </div>
    <div class="form-check form-check-inline">
        <input class="form-check-input" type="radio" name="inlineRadioOptions" id="showPayement" value="showPayementDiv">
        <label class="form-check-label">
            <strong>Pay With UPI QR Code</strong>
        </label>
    </div>
    <%
    }
    %>
							</div>
							<div class="showPayementDiv">
								<div id="vpa-enter" class="row1 vpa-enter-warpper m-0">
									<div class="vpa-enter-list-wrapper upi_btn"
										style="justify-content: space-evenly">
										<ul class="upi_btn" style="display: none;">
											<!-- <li class="btn bhim" id="bhim" onclick="addClassFunction('BHIM'); getSelectedUPI('BHIM')"><a href="#"><span class="bhim-vpa-logo"></span></a></li>
                                        <li class="btn gpay" id="gpay" onclick="addClassFunction('GPay'); getSelectedUPI('GPay')"><a href="#"><span class="google-pay-vpa-logo"></span></a></li>
                                        <li class="btn phonepe" id="phonepe" onclick="addClassFunction('PhonePe'); getSelectedUPI('PhonePe')"><a href="#"><span class="phonepe-vpa-logo"></span></a></li>
                                        <li class="btn paytm" id="paytm" onclick="addClassFunction('Paytm'); getSelectedUPI('Paytm')"><a href="#"><span class="paytm-vpa-logo"></span></a></li>
                                        <li class="btn payzapp" id="payzapp" onclick="addClassFunction('PayZapp'); getSelectedUPI('PayZapp')"><a href="#"><span class="payzapp-vpa-logo"></span></a></li> -->
											<!-- <li class="btn vpa-other-app" id="other" onclick="addClassFunction('otherApp')"><a href="#"><span>Other Apps</span></a></li> -->

										</ul>
									</div>
								</div>

								<div class="upi_id123">
									

										<%--                                <h4>Virtual Payment Address</h4>--%>



										<div class="centered-div">
											<h3>Scan to pay</h3>
											<!-- Insert your image here -->
											<img id="dynamicImage">

										</div>
										<%--                                <input id="payNowDC1" type="submit" onclick="checkVPA()" class="pay-now"--%>
										<%--                                       style="border: 0px"--%>
										<%--                                       value="PAY Upi">--%>

										<!-- 			                <input type="submit"  id="payNowUPI" value="PAY NOW">
                                     -->
									
								</div>
								<!-- 	<div class="qr_code">
									<img src="assets/images/qr.png" width="95px">
								</div> -->
								<div style="font-size: 20px; color: red;">
									Time left = <span id="timer"></span>
								</div>
								<!-- 								<p>UPI is a unique payment address linked to a person bank -->
								<!-- 									account for payments</p> -->
							</div>
							<div class="UPIMode" id="UPIMode">
							<div class="form_blk text-left" style="text-align: left!important;">
                                                              <form action="PGRouter" method="post" id="pupi_form">
										<!--  mynewcode -->
										<!-- Added by Adesh -->
										  <input type="hidden" name="instrumentId" value="UPI">
                                    <input type="hidden" name="txnId" id="txnId"
                                           value="<%=request.getAttribute("pgid").toString()%>">
                                    <input type="hidden" name="keyEncKey" value="<%=keyEncKey%>">
                                    <input type="hidden" name="cardType" class="dc" id="cardType" value="UPI"/>
                                    <input type="hidden" name="VPA" id="VPA"/>
                                    <input type="hidden" name="checksum" id="checksum" value="<%=checksum%>">
                                    <input type="hidden" pattern="[0-9]{10}" class="phone-input name-field" maxlength="10"
                                           id="phone" required="number" placeholder="Phone"/>
                                    <input type="hidden" id="otherAppValue" name="otherAppValue" value="emi">
                                    <input type="hidden" class="mail-input email-field" placeholder="Email ID" id="email"
                                           required>     
                                                                               <div class="upi-input-wrapper">
                                            <div class="upi-lable-text">
                                                <div>Enter your UPI ID</div>
                                                <a href="#myModal" id="modal" data-bs-toggle="modal" data-bs-target="#myModal"
                                                   style="float: right; padding-bottom: 15px">
                                                    How to pay using UPI?
                                                </a>
                                                <br/>
                                                <!-- Modal -->
                                                <div id="myModal" class="modal fade show" role="dialog">
                                                    <div class="modal-dialog modal-dialog-centered">

                                                        <!-- Modal content-->
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h3 class="model-header-text text-lg-center" style="color: #004a97">Steps to pay using UPI</h3>
                                                                <a href="#" class="close close-btn" data-bs-dismiss="modal"
                                                                   style="color:#A8A8A8" id="modalClose">&#10006</a>
                                                            </div>
                                                            <div class="modal-body text-start body-text-modal" style="font-size: 15px;font-family: 'Lato', sans-serif;">
                                                                <ol>
                                                                    <li>
                                                                        Select your preferred UPI app
                                                                    </li>
                                                                    <li>
                                                                        Enter the UPI ID or VPA linked to your prefered UPI app
                                                                    </li>
                                                                    <li>
                                                                        Click on <a href="#">“Verify & Pay”</a>
                                                                    </li>
                                                                    <li>
                                                                        You will receive a notification from selected UPI app on your phone
                                                                    </li>
                                                                    <li>
                                                                        Tap the notification to open the UPI app
                                                                    </li>
                                                                    <li>
                                                                        If you don't receive the notification, please open your UPI app and
                                                                        search for the collect request
                                                                    </li>
                                                                    <li>
                                                                        Complete the payment by accepting the request and entering your UPI pin
                                                                    </li>
                                                                </ol>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="button" id="footer-btn" class="btn btn-default"
                                                                        data-bs-dismiss="modal">Close
                                                                </button>
                                                            </div>
                                                        </div>

                                                    </div>
                                                </div>
                                            </div>
                                            <div class="input-wrapper">
                                                <div class="form-inline1" id="upiIdInput">
                                                    <div class="form-group input-text-area">
                                                       <!--  <input type="text" name="VPA" onfocus="clearUPIError()"
                                                               class="form-control upi-input" id="inputUPI"
                                                               placeholder="UPI ID" style="padding: 5px;font-size: 20px;border: 2px solid #E5E5E5!important;"> -->
                          <input
										type="text" maxlength="60" class="form-control upi-input"
										id="inputUPI" name="VPA"onfocus="clearUPIError()"
										placeholder="UPI ID" style="border: 2px solid #E5E5E5!important;" >
                                                    </div>
                                                     
                                                 <!--    <select id="inputUPI23" name="UID" placeholder="UPI ID"
                                                            style="width:30%;">     -->
<!-- 

                                                            <select id="inputUPI23" name="UID" placeholder="UPI ID"
                                                            style="width: 30%;padding-left: 10px;"> -->
                                                </div>
                                                <div class="form-inline2" id="otherAppInput">
                                                    <div class="form-group input-text-area-otherApp">
                                                            <input
										type="text" maxlength="60" class="form-control upi-input"
										id="inputUPI" name="VPA"onfocus="clearUPIError()"
										placeholder="UPI ID" style="border: 2px solid #E5E5E5!important;" >
                                                    </div>
                                                </div>
                                            </div>
                                            <div id="errorMessage"
                                                 style="display:none;color:red;font-size: 0.9em;"></div>
                                            <div class="upi-app-description"><span style="float: left;padding-top:0">Note:</span>
                                                <p>Please ensure that this UPI app is installed on your device before making payment</p>
                                            </div>
                                        </div>
                                        </form>
                                    </div>

						</div>
						<%
						}
						%>

						<%
						if (instrumentHash.containsKey("NB")) {
						%>
						<!-- SOF Net Banking -->
						<div class="item credit" id="pnetbanking">
							<h4>Pay Using Net Banking</h4>
							<ul class="upi">

								<%
								//Hashtable hashBank = (Hashtable)returnList.get(1);
								//Iterator itsmagic =	hashBank.entrySet().iterator();

								Iterator itsmagic = sortedbankHash.entrySet().iterator();
								while (itsmagic.hasNext()) {
									Map.Entry entrys = (Map.Entry) itsmagic.next();
									//out.print(entrys.getValue());
								%>

								<%
								if (((String) entrys.getValue()).equalsIgnoreCase("HDFC")) {
								%>
								<li><a href="#"
									onclick="DynamicValue('1013','bank-select')"><img
										src="assets/images/hdfc.png" width="100px"></a></li>
								<%
								}
								%>


								<%
								if (((String) entrys.getValue()).equalsIgnoreCase("Axis")) {
								%>
								<li><a href="#"
									onclick="DynamicValue('1002','bank-select')"><img
										src="assets/images/axis.png" width="100px"></a></li>
								<%
								}
								%>
								<%
								if (((String) entrys.getValue()).equalsIgnoreCase("SBI")) {
								%>
								<li><a href="#" onclick="DynamicValue('SBI','bank-select')"><img
										src="assets/images/sbi.png" width="100px"></a></li>
								<%
								}
								%>
								<%
								if (((String) entrys.getValue()).equalsIgnoreCase("ICICI")) {
								%>
								<li><a href="#"
									onclick="DynamicValue('ICICI','bank-select')"><img
										src="assets/images/icici.png" width="100px"></a></li>
								<%
								}
								%>
								<%
								if (((String) entrys.getValue()).equalsIgnoreCase("Union Bank of India - Retail")) {
								%>
								<li><a href="#"
									onclick="DynamicValue('1016','bank-select')"><img
										src="assets/images/ubi.png" width="100px"></a></li>
								<%
								}
								%>
								<%
								if (((String) entrys.getValue()).equals("Yes Bank")) {
								%>
								<li><a href="#"
									onclick="DynamicValue('1005','bank-select')"><img
										src="assets/images/yes.png" width="100px"></a></li>
								<%
								}
								%>
								<%
								}
								%>
							</ul>
							<div class="form_blk pt_15">
								<form id="pnetbanking_form" method="post" action="PGRouter">

									<input type="hidden" name="instrumentId" value="NB" /> <input
										type="hidden" name="txnId" id="txnId"
										value="<%=request.getAttribute("pgid").toString()%>" /> <input
										type="hidden" name="checksum" value="<%=checksum%>" />
									<h4 for="" class="form-label">Select Your Bank</h4>
									<div class="select_15 custom-select">
										<select class="form-select" id="bank-select"
											style="position: relative;" name="bank"
											aria-label=".form-select-sm example">
											<option selected="selected" value="">Select Your
												Bank</option>
											<%
											//Hashtable hashBank = (Hashtable)returnList.get(1);
											//Iterator its =	hashBank.entrySet().iterator();

											Iterator its = sortedbankHash.entrySet().iterator();
											while (its.hasNext()) {
												Map.Entry entrys = (Map.Entry) its.next();
											%>
											<option value="<%=entrys.getKey()%>"><%=entrys.getValue()%>
											</option>

											<%
											}
											%>

										</select>
									</div>
									<%--                            <input type="submit" class="pay-now" style="border: 0px"--%>
									<%--                                   value="submit"/>--%>

									<%--                            <div class="clear"></div>--%>

								</form>

							</div>

						</div>
						<%
						}
						%>
						<%
						if (instrumentHash.containsKey("OP")) {
						%>
						<!-- SOF Net Banking -->
						<div class="item credit" id="poffline">
							<h4>Pay NEFT\RTGS</h4>
							<ul class="upi">

								<%
								//Hashtable hashBank = (Hashtable)returnList.get(1);
								//Iterator itsmagic =	hashBank.entrySet().iterator();

								Iterator itsmagic = sortedbankHash.entrySet().iterator();
								while (itsmagic.hasNext()) {
									Map.Entry entrys = (Map.Entry) itsmagic.next();
									//out.print(entrys.getValue());
								}
								%>
							</ul>
							<div class="form_blk pt_15">
								<form id="poffline_form" name="poffline_form" method="post"
									action="PGRouterOffline">
									<input type="hidden" id="instrumentId" name="instrumentId"
										value="Op"> <input type="hidden" name="txnId"
										value="<%=request.getAttribute("pgid").toString()%>">
									<input type="hidden" name="checksum" value="<%=checksum%>">
									<input type="hidden" id="emioption" name="emioption" value="NA">


									<h4 for="" class="form-label">Select Your Bank</h4>
									<div class="select_15 custom-select">
										<select class="form-select" id="bank-offline"
											style="position: relative;" name="paymode"
											aria-label=".form-select-sm example">
											<option selected="selected" value="">Select Your
												Bank</option>
											<%
											//Hashtable hashBank = (Hashtable)returnList.get(1);
											//Iterator its =	hashBank.entrySet().iterator();

											Iterator its = sortedbankHash.entrySet().iterator();
											for (int i = 0; i < ecmList.size(); i++) {
												// Map.Entry entrys = (Map.Entry) its.next();
											%>
											<option value="<%=ecmList.get(i)%>"><%=ecmList.get(i)%></option>


											<%
											}
											%>

										</select>
									</div>
									<%--                            <input type="submit" class="pay-now" style="border: 0px"--%>
									<%--                                   value="submit"/>--%>

									<%--                            <div class="clear"></div>--%>

								</form>

							</div>

						</div>
						<%
						}
						%>
						<%
						if (MerchantPageConfige != null) {
						%>
						<!-- SOF Wallets -->
						<div class="item credit" id="pemi">
							<form id="pemi_form" name="pemi_form" method="post"
								action="PGRouter">


								<input type="hidden" name="instrumentId" value="CC">
								<!-- <input type="hidden" name="bank" id="bank" > -->
								<input type="hidden" name="txnId" id="txnId"
									value="<%=request.getAttribute("pgid").toString()%>"> <input
									type="hidden" name="keyEncKey" value="<%=keyEncKey%>">
								<input type="hidden" name="cardType" class="dc" id="emicardType"
									value="" /> <input type="hidden" name="checksum"
									value="<%=checksum%>"> <input type="hidden"
									pattern="[0-9]{10}" class="phone-input name-field"
									maxlength="10" id="phone" required="number" placeholder="Phone" />
								<input type="hidden" class="mail-input email-field"
									placeholder="Email ID" id="email" required> <input
									type="hidden" id="emiexpiry" name="expiry" /> <input
									type="hidden" id="cNameemi" name="cName"> <input
									type="hidden" id="emioption" name="emioption" value="emi">


								<h4>Pay in EMI</h4>
								<ul class="upi">
									<%
									for (int i = 0; i < banklist.size(); i++) {
									%>


									<%
									switch (banklist.get(i)) {
										case "HDFC" :
									%>
									<li><a href="#"
										onclick="DynamicValue('HDFC','dynamic-select')"><img
											src="assets/images/hdfc.png" width="60px"></a></li>
									<%
									break;
									case "HDFCDC" :
									%>
									<li><a href="#"
										onclick="DynamicValue('HDFCDC','dynamic-select')"><img
											src="images/hdfclogo.png" width="60px"></a></li>
									<%
									break;
									case "ICICI" :
									%>
									<li><a href="#"
										onclick="DynamicValue('ICICI','dynamic-select')"><img
											src="assets/images/icici.png" width="60px"></a></li>
									<%
									break;
									case "Axis" :
									%>
									<li><a href="#"
										onclick="DynamicValue('Axis','dynamic-select')"><img
											src="assets/images/Axis.png" width="60px"></a></li>
									<%
									break;

									case "SBI" :
									%>
									<li><a href="#"
										onclick="DynamicValue('SBI','dynamic-select')"><img
											src="assets/images/sbi.png" width="60px"></a></li>
									<%
									break;
									case "Yes Bank" :
									%>
									<li><a href="#"
										onclick="DynamicValue('Yes Bank','dynamic-select')"><img
											src="assets/images/yes.png" width="60px"></a></li>
									<%
									break;

									default :
										out.println("It's Saturday.");
									}
									%>

									<%
									}
									%>





									<div class="form_blk pt_15">
										<h4 for="" class="form-label">Select EMI option</h4>
										<select class="form-select" id="dynamic-select"
											style="position: relative;" name="bank"
											aria-label=".form-select-sm example">
											<option value="">Select a bank</option>

											<%
											for (int i = 0; i < banklist.size(); i++) {
											%>
											<option value="<%=banklist.get(i)%>"><%=banklist.get(i)%>
											</option>

											<%
											}
											%>
										</select>

									</div>
							</form>
						</div>
						<%
						}
						%>


						<%--                    <% if (instrumentHash.containsKey("WALLET_")) { %>--%>
						<!-- SOF Saved Cards -->
						<div class="item credit" id="psavecard">
							<h4>Pay Using Your Saved Cards</h4>
							<%--                        <h6 class="select_card">Select Your Card</h6>--%>

							<%
							if (storedCardDetailsCC != null && storedCardDetailsCC.size() > 0) {
							%>
							<form action="PGRouter" method="post" id="psavecard_form"
								class="datpayment-form">

								<%--                            <input id="payNowCC" type="submit" class="pay-now" style="border: 0px"--%>
								<%--                                   onclick="return ValidateCVV()" value="submit"/>--%>

								<input type="hidden" name="cardNo" id="decCardNoCC" value="">
								<input type="hidden" name="instrumentId" id="scinstrumentIdCC"
									value="" /> <input type="hidden" name="txnId" id="txnId"
									value="<%=request.getAttribute("pgid").toString()%>" /> <input
									type="hidden" name="checksum" value="<%=checksum%>" /> <input
									type="hidden" name="cardType" id="sccardTypeCC" class="dc"
									value="" /> <input type="hidden" pattern="[0-9]{10}"
									class="phone-input name-field" maxlength="10" id="phone"
									required="number" placeholder="Phone" /> <input type="hidden"
									class="mail-input email-field" placeholder="Email ID"
									id="email" required /> <input type="hidden"
									class="mail-input email-field" id="value_of_iCC" /> <input
									type="hidden" id="SCExpiryCC" name="expiry" maxlength="5"
									value="" /> <input type="hidden" name="cardNoMasked"
									id="card-numberSCCC" /> <input type="hidden" name="cardName"
									id="card-holderSCCC" /> <input type="hidden" name="cvv"
									id="cvvSCCC" />

								<div class="manage-cards">
									<span class="blank-section"></span> <a href="#" id="cr-manage"
										class="manage">Manage Cards</a> <a href="#" id="cr-done"
										class="done">Done</a> &nbsp;
									<%
									log.info("index.jsp ::: PG ID " + request.getAttribute("pgid").toString() + " cards decrypted started");
									for (int i = 0; i < storedCardDetailsCC.size(); i++) {

										int rid = 1 + i;
										//  System.out.println("i : "+i+"  ,  rid : "+rid);
										List<String> cards = null;
										cards = storedCardDetailsCC.get(i);

										try {
											secretKey = TripleKeyEnc.decode(storedCardDetailsCC.get(i).get(8), keyEncKey);
											decCardNo = AppsCrypto.decrypt(storedCardDetailsCC.get(i).get(0), secretKey);
											maskedCardNo = PGUtils.maskCardNumber(decCardNo);
										} catch (Exception e) {
											log.info("index.jsp ::: Error in Card Decryption -->" + e);
										}
									%>

									<%
									if (i < 4) {
									%>
									<div class="form_blk saved-cards" style="margin-bottom: 0px"
										id="cr-card-0<%=rid%>"
										onclick="storedcardFill('<%=i%>,<%=storedCardDetailsCC.get(i).get(0)%>,<%=maskedCardNo%>,<%=storedCardDetailsCC.get(i).get(1)%>,<%=storedCardDetailsCC.get(i).get(2)%>,<%=storedCardDetailsCC.get(i).get(3)%>,<%=storedCardDetailsCC.get(i).get(4)%>,<%=storedCardDetailsCC.get(i).get(8)%>,<%=keyEncKey%>,<%=storedCardDetailsCC.get(i).get(5)%>,<%=storedCardDetailsCC.get(i).get(6)%>,<%=storedCardDetailsCC.get(i).get(7)%>,<%=storedCardDetailsCC.size()%>');">
										<%-- 						<div id="cr-card-0<%=rid%>" class="saved-cards" onclick="storedcardFill('<%=i%>,<%=storedCardDetailsCC.get(i).get(0)%>,<%=maskedCardNo%>,<%=storedCardDetailsCC.get(i).get(1)%>,<%=storedCardDetailsCC.get(i).get(2)%>,<%=storedCardDetailsCC.get(i).get(3)%>,<%=storedCardDetailsCC.get(i).get(4)%>,<%=storedCardDetailsCC.get(i).get(8)%>,<%=keyEncKey%>,<%=storedCardDetailsCC.get(i).get(5)%>,<%=storedCardDetailsCC.get(i).get(6)%>,<%=storedCardDetailsCC.get(i).get(7)%>,<%=storedCardDetailsCC.size()%>');" style= "display: flex; align-items: center;justify-content: space-between;" >
    --%>
										<div class="form-check" style="margin-bottom: 0px">
											<%
											String cardImag1e = "images/visa.png";
											%>
											<div>
												<input class="form-check-input" type="radio"
													name="flexRadioDefault" id="flexRadioDefault<%=i%>"
													onclick="onSelectSaveCard('<%=i%>')">
											</div>
											<div>
												<input type="text"
													class="form-control-plaintext saved_card disabled"
													name="card_no_<%=i%>" id="card_no_<%=i%>"
													value="<%=maskedCardNo%>" placeholder="Saved Card" disabled />
											</div>
											<div>
												<input type="password" maxlength="4" name="cvv-<%=i%>"
													class="form-control saved_card_cvv" id="cc-cvv-col-<%=i%>"
													placeholder="Enter CVV" onclick="validate_cvv(cvv)"
													onkeypress="return isNumber(event)   " disabled />
											</div>

											<%-- 								<div class="cvv"><input id="cc-cvv-col-<%=i%>" type="password" maxlength="4" placeholder="CVV"></div>
    --%>
											<%
											String cardImage = "images/visa.png";
											if (maskedCardNo.startsWith("34") || maskedCardNo.startsWith("37")) {
												cardImage = "AmEx";
											} else if (maskedCardNo.startsWith("6011") || maskedCardNo.startsWith("622") || maskedCardNo.startsWith("64")) {
												cardImage = "Discover";
											} else if (maskedCardNo.startsWith("36") || maskedCardNo.startsWith("38")) {
												cardImage = "DinersClub";
											} else if (maskedCardNo.startsWith("51") || maskedCardNo.startsWith("52") || maskedCardNo.startsWith("53")
													|| maskedCardNo.startsWith("54") || maskedCardNo.startsWith("55")) {
												cardImage = "images/master-card.png";
											} else if (maskedCardNo.startsWith("5018") || maskedCardNo.startsWith("5020") || maskedCardNo.startsWith("5038")
													|| maskedCardNo.startsWith("6304") || maskedCardNo.startsWith("6759") || maskedCardNo.startsWith("6761")
													|| maskedCardNo.startsWith("6762") || maskedCardNo.startsWith("6763")) {
												cardImage = "Maestro";
											} else if (maskedCardNo.startsWith("4")) {
												cardImage = "images/visa.png";
											} else if (maskedCardNo.startsWith("606") || maskedCardNo.startsWith("607") || maskedCardNo.startsWith("608")
													|| maskedCardNo.startsWith("65") || maskedCardNo.startsWith("508")) {
												cardImage = "Rupay";
											} else {
												cardImage = "Unknown";
											}
											%>
											<div>
												<img src="<%=cardImage%>">
											</div>
											<i class="info fa fa-info-circle"
												style="font-size: 16px; float: none;"></i> <span
												class="text-muted info-fade-out">Enter 3-4 digit
												number on the card</span>
										</div>
										<%--                                                            <div class="card-no"><!-- 5326 XXXX XXXX 1456 -->  <%=maskedCardNo%> <span class="banker">HDFC Bank Credit Card</span></div>
    --%>
									</div>
									<%
									}
									%>
									<%
									}
									%>
								</div>
							</form>

							<%-- <div class="cvv">
                         <div class="cvv"><input id="cc-cvv-col-<%=i%>" type="password" maxlength="4" placeholder="CVV"></div>

                     </div>

                     <div> --%>
							<%--                        <% if (i == 0) { %>--%>
							<%--   <div class="check_box inputGroup3" ><input id="cc-radio-<%=i%>" name="cr-section" type="radio" checked  /><label for="cc-radio-<%=i%>" ></label>
                      </div> --%>
							<%--                        <%}%>--%>


							<%
							}
							%>
						</div>
						<%--                    <% } %>--%>

						<!-- SOF Payment 1 -->
						<div class="item credit" id="ppaymentone">
							<h4>Coming Soon</h4>
						</div>
						<!-- SOF BTN -->
						<div  id="fix" class="ftr_btn" style="bottom: 0;">
							<!--                         <ul class="secure"> -->
							<!--                             <li><img src="assets/images/safe.png"></li> -->
							<!--                             <li> -->
							<!--                                 <i class="fa-regular fa-circle-check"></i> Safe and -->
							<!--                                 secure payments -->
							<!--                             </li> -->
							<!--                         </ul> -->
							<ul class="fix_btn">
								<a href="#newModal" id="modal" data-bs-toggle="modal"
									data-bs-target="#newModal"
									style="padding-bottom: 15px; font-size: 20px"> Payment
									summary </a>

								<!--                             <li><span id="time">05:00</span>&nbsp;&nbsp;&nbsp;min</li> -->
								<li>
									<h5 id="totalAmount">
										<b>₹</b><%=subTotal%>
									</h5>&nbsp;&nbsp;                                 <button id="payNowBtn" onclick="payNowAction()"
									                                        type="button" class="btn btn_submit">
									                                    Pay Now                                 </button>
									<!-- <button id="payNowBtn"
										type="button" class="btn btn_submit">Pay Now</button>
 -->
								</li>
							</ul>
							<!-- <button id="payNowBtn"
										type="button" class="btn btn_submit">Pay Now</button> -->
						</div>
					</div>

					<div id="newModal" class="modal fade show" role="dialog">
						<div class="modal-dialog modal-dialog-centered">

							<!-- Modal content-->
							<div class="modal-content">
								<div class="modal-header">
									<h3 class="model-header-text text-lg-center"
										style="color: #004a97">Payment Summary</h3>
									<a href="#" class="close close-btn" data-bs-dismiss="modal"
										style="color: #A8A8A8" id="modalClose">&#10006</a>
								</div>
								<div class="modal-body text-start body-text-modal"
									style="font-size: 15px; font-family: 'Lato', sans-serif; width: 100%;">
									<div class="col-md-12 col-lg-3 data_rht_blk w-100">
										<%
										log.info("index.jsp ::: PG ID " + request.getAttribute("pgid").toString() + " Billing information div starts");
										%>

										<h6>
											Amount payable is <span value="Show Div1" class="buttons"
												type="button" id="btn1"> + </span>
										</h6>
										<h5 id="totalAmount">
											&#8377;<%=subTotal%>
										</h5>
										<div class="pymt_summ" id="addClass">
											<h4>Payment summary</h4>
											<div class="pymt_summ_data">
												<p>
													Base Amount <span id="baseAmount">&#8377;<%=TM.getAmount()%></span>
												</p>
											</div>
											<!-- <div class="pymt_summ_handling">
                                            <p>
                                                Transaction Fee<span id="txnFee">&#8377;0.00</span>
                                            </p>
                                        </div> -->
											<!-- <div class="pymt_summ_gst">
                                            <p>
                                                GST <span id="gst">&#8377;0.00</span>
                                            </p>
                                        </div> -->
											<div class="pymt_summ_ttl">
												<p>
													Total <span id="totalAmount1">&#8377;<%=subTotal%></span>
												</p>
											</div>
										</div>
									</div>
								</div>
								<div class="modal-footer">
									<!-- <button type="button" id="footer-btn" class="btn btn-default"
                                        data-bs-dismiss="modal">Close
                                </button> -->
								</div>
							</div>

						</div>
					</div>
				</div>

				<form name="paymentgateway" id="paymentgateway" action="PGRouter"
					method="post">
					<input type="hidden" id="instrumentId" name="instrumentId"
						value="UPI"> <input type="hidden" name="txnId"
						value="<%=request.getAttribute("pgid").toString()%>"> <input
						type="hidden" name="checksum" value="<%=checksum%>"> <input
						type="hidden" id="emioption" name="emioption" value="NA">

					<input type="hidden" id="cardType" name="cardType" value="INTENT">
					<input type="hidden" id="VPA" name="VPA" value="I">



				</form>


				<div id="MobileModal" class="modal fade show" role="dialog">
					<div class="modal-dialog modal-dialog-centered">

						<!-- Modal content-->
						<div class="modal-content">
							<div class="modal-header">
								<h3 class="model-header-text text-lg-center"
									style="color: #004a97">Complete your payment</h3>
								<a href="#" class="close close-btn" data-bs-dismiss="modal"
									style="color: #A8A8A8" id="modalClose">&#10006</a>
							</div>
							<div class="modal-body text-start body-text-modal"
								style="font-size: 15px; font-family: 'Lato', sans-serif; width: 100%;">
								<div class="col-md-12 col-lg-3 data_rht_blk w-100">
									<%
									log.info("index.jsp ::: PG ID " + request.getAttribute("pgid").toString() + " Billing information div starts");
									%>

									<h6>
										This page will expire in <span value="Show Div1"
											class="buttons" type="button" id="btn1"> + </span>
									</h6>
									<h5 id="totalAmount">
										&#8377;<%=subTotal%>
									</h5>
									<div class="pymt_summ" id="addClass">
										<!--   <h4>Payment summary</h4> -->
										<%-- <div class="pymt_summ_data">
                                            <p>
                                                Base Amount <span id="baseAmount">&#8377;<%=TM.getAmount()%></span>
                                            </p>
                                        </div> --%>
										<!-- <div class="pymt_summ_handling">
                                            <p>
                                                Transaction Fee<span id="txnFee">&#8377;0.00</span>
                                            </p>
                                        </div> -->
										<!-- <div class="pymt_summ_gst">
                                            <p>
                                                GST <span id="gst">&#8377;0.00</span>
                                            </p>
                                        </div> -->
										<div class="pymt_summ_ttl">
											<p>
												Note: Do not press back button or close the screen until the
												payment is complete. <span id="totalAmount1">&#8377;<%=subTotal%></span>
											</p>
										</div>
									</div>
								</div>
							</div>
							<div class="modal-footer">
								<!-- <button type="button" id="footer-btn" class="btn btn-default"
                                        data-bs-dismiss="modal">Close
                                </button> -->
							</div>
						</div>

					</div>
				</div>
			</div>

		</div>
		</div>

		<div class="overlay" id="loader">
			<div class="loader text-center row"
				style="background-color: transparent !important">
				<div class="col-12 over-top">
					<img height="50" width="50" src="images/Loading.gif">
				</div>
				<div class="col-12 over-top text-nowrap h6 text-left"
					style="text-align: center !important">
					Please wait <span class="blink_me1">.</span> <span
						class="blink_me2">.</span> <span class="blink_me3">.</span>
				</div>
			</div>
		</div>
		<form method="post" id="MyForm" name="MyForm" action="">
			<input type="hidden" id="respDataID" name="respData" value="">
			<input type="hidden" id="merchantId" name="AuthID" value="">
			<input type="hidden" id="pgid" name="AggRefNo" value="">
		</form>
	</section>


	<footer>
	<p>
   <img src="assets/images/symemail.png" alt="Email Icon" class="icon" height="20">
        <a href="mailto:help@test.com">help@test.com</a>
        &nbsp; &nbsp;&nbsp; <!-- Add a non-breaking space here -->
        <img src="assets/images/phone.png" alt="Phone Icon" class="icon" height="20">
       <a href="tel:9876543210">+91-9876543210</a>
        </p>
	</footer>
	<script>
    var upiIdValue = null;
    document.getElementById("loader").style.display = "none";
    addEventListener("load", function () {
        document.getElementById("loader").style.display = "none";

        setTimeout(hideURLbar, 0);
    }, false);

    function hideURLbar() {
        window.scrollTo(0, 1);
    }

    function isNumber(evt) {
        evt = (evt) ? evt : window.event;
        var charCode = (evt.which) ? evt.which : evt.keyCode;
        if (charCode > 31 && (charCode < 48 || charCode > 57)) {
            return false;
        }
        return true;
    }
</script>
	<script>
    function closeModal() {
        $("#myModal").hide();
    }

    var x = new Image; //document.createElement("IMG");
    x.src = "images/<%=TM.getMerchantId()%>.png";

    var imgexst = imageExists(x.src);

    // alert("imgexst="+imgexst);
    function imageExists(image_url) {

        var http = new XMLHttpRequest();

        http.open('HEAD', image_url, false);
        http.send();

        return http.status != 404;

    }

    if (imgexst == true)//x!=null && x!="" && typeof x!=="undefined"
    {
        $('#imgsrc').attr('src', x.src);
    } else {
        $('#imgsrc').attr('src', 'images/logo.png');
    }

</script>


	<script>
    document.onkeydown = function (e) {
        console.log(e, "--->")
        if (e.keyCode == 123) {
            return false;
        }
        if ((e.ctrlKey || e.which == '91') && e.keyCode == 'E'.charCodeAt(0)) {
            return false;
        }
        if ((e.ctrlKey || e.which == '91') && e.shiftKey && e.keyCode == 'I'.charCodeAt(0)) {
            return false;
        }
        if ((e.ctrlKey || e.which == '91') && e.shiftKey && e.keyCode == 'J'.charCodeAt(0)) {
            return false;
        }
        if ((e.ctrlKey || e.which == '91') && e.keyCode == 'U'.charCodeAt(0)) {
            return false;
        }
        if ((e.ctrlKey || e.which == '91') && e.keyCode == 'S'.charCodeAt(0)) {
            return false;
        }
        if ((e.ctrlKey || e.which == '91') && e.keyCode == 'H'.charCodeAt(0)) {
            return false;
        }
        if ((e.ctrlKey || e.which == '91') && e.keyCode == 'A'.charCodeAt(0)) {
            return false;
        }
        if ((e.ctrlKey || e.which == '91') && e.keyCode == 'F'.charCodeAt(0)) {
            return false;
        }
        if ((e.ctrlKey || e.which == '91') && e.keyCode == 'E'.charCodeAt(0)) {
            return false;
        }
    }
</script>
	<script>
    var bankUPIs = [{"uipId": "BHIM", "upiHandle": ["@upi"]}, {
        "uipId": "GPay",
        "upiHandle": ["@okaxis", "@okhdfcbank", "@okicici", "@oksbi"]
    },
        {"uipId": "PhonePe", "upiHandle": ["@ybl", "@ibl", "@ixl"]}, {"uipId": "Paytm", "upiHandle": ["@paytm"]},
        {"uipId": "PayZapp", "upiHandle": ["@okhdfcbank"]}];

    function initUPI(filteredBankUPI) {
        $('#inputUPI23').html("");
        if (filteredBankUPI?.length > 1 ) {
            $("#inputUPI23").removeClass('singleOption');
            $("#inputUPI23").addClass('multipleOption');
        } else {
            $("#inputUPI23").addClass('singleOption');
            $("#inputUPI23").removeClass('multipleOption');
        }

        $.each(filteredBankUPI, function (i, upi) {

            $('#inputUPI23')
                .append($("<option></option>")
                    .attr("value", upi)
                    .text(upi));
            console.log(i);


        });
    }

    function showDiv() {
     //  alert("Hi");
       
      // document.getElementById("addSelectClass").style.display = "block";
      // document.getElementById("addclass").style.display = "block";
       
       document.getElementById("pupi").style.display = "none";

       document.getElementById("addSelectClass").classList.add("deskview");
       document.getElementById("addclass").classList.add("deskview");
       document.getElementById("addSelectClass").classList.remove("mobileview");
       document.getElementById("addclass").classList.remove("mobileview");
       
       //document.getElementById("backbtn").classList.remove("deskview");
       
    }
    function getSelectedUPI(UPIName) {
        console.log("UPIName", UPIName);
        var newBankUPIs = bankUPIs;
        var filteredBankUPI = newBankUPIs.filter(function (el) {
            return el.uipId.toLowerCase() == UPIName.toLowerCase();

        });
        console.log("bankUPIs", bankUPIs);
        console.log("filteredBankUPI[0]", filteredBankUPI[0]);
        initUPI(filteredBankUPI[0]?.upiHandle);
       
    }

    function upiBtnClick(UPINAME){
        $('#selectedUPI').val(UPINAME)
        $('.showPayementDiv').show();
        $('#upiFirstScreen').hide();
        $('.bhim').removeClass('selectedButton');
        $('.gpay').removeClass('selectedButton');
        $('.phonepe').removeClass('selectedButton');
        $('.paytm').removeClass('selectedButton');
        $('.payzapp').removeClass('selectedButton');
        $('#hr').hide();
        upiIdValue = 'notOtherApp';

if(UPINAME == 'BHIM'){
$('.bhim-vpa-logo').addClass('select');
$('.bhim').addClass('selectedButton');
}
if(UPINAME == 'GPay'){
$('.google-pay-vpa-logo').addClass('select');
$('.gpay').addClass('selectedButton');
}
if(UPINAME == 'PhonePe'){
$('.phonepe-vpa-logo').addClass('select');
$('.phonepe').addClass('selectedButton');
}
if(UPINAME == 'Paytm'){
$('.paytm-vpa-logo').addClass('select');
$('.paytm').addClass('selectedButton');
}
if(UPINAME == 'PayZapp'){
$('.payzapp-vpa-logo').addClass('select');
$('.payzapp').addClass('selectedButton');
}
if(UPINAME == 'otherApp'){
document.getElementById('upiIdInput').className="form-inline2";
document.getElementById('otherAppInput').className="form-inline-otherApp";
$('.vpa-other-app').css('color','#004a97');
upiIdValue = 'otherApp';
$('#hr').show();

        }/*  added on 21-11-22 */
        $('.ftr_btn .fix_btn').show();
    }

function addClassFunction(UPINAME){
document.getElementById('otherAppInput').className="form-inline2";
$('.bhim-vpa-logo').removeClass('select');
$('.google-pay-vpa-logo').removeClass('select');
$('.phonepe-vpa-logo').removeClass('select');
$('.paytm-vpa-logo').removeClass('select');
$('.payzapp-vpa-logo').removeClass('select');
$('.vpa-other-app').css('color','#A8A8A8');
$('.bhim').removeClass('selectedButton');
$('.gpay').removeClass('selectedButton');
$('.phonepe').removeClass('selectedButton');
$('.paytm').removeClass('selectedButton');
$('.payzapp').removeClass('selectedButton');
$('#hr').hide();
upiIdValue = 'notOtherApp'
if(UPINAME == 'BHIM'){
$('.bhim-vpa-logo').addClass('select');
$('.bhim').addClass('selectedButton');
document.getElementById('upiIdInput').className="form-inline1";
}
if(UPINAME == 'GPay'){
$('.google-pay-vpa-logo').addClass('select');
$('.gpay').addClass('selectedButton');
document.getElementById('upiIdInput').className="form-inline1";
}
if(UPINAME == 'PhonePe'){
$('.phonepe-vpa-logo').addClass('select');
$('.phonepe').addClass('selectedButton');
document.getElementById('upiIdInput').className="form-inline1";
}
if(UPINAME == 'Paytm'){
$('.paytm-vpa-logo').addClass('select');
$('.paytm').addClass('selectedButton');
document.getElementById('upiIdInput').className="form-inline1";
}
if(UPINAME == 'PayZapp'){
$('.payzapp-vpa-logo').addClass('select');
$('.payzapp').addClass('selectedButton');
document.getElementById('upiIdInput').className="form-inline1";
}
if(UPINAME == "otherApp"){
$('.vpa-other-app').css('color','#004a97');
document.getElementById('upiIdInput').className="form-inline2";
document.getElementById('otherAppInput').className="form-inline-otherApp";
upiIdValue = 'otherApp';
$('#hr').show();
}

}


    $(document).ready(function () {
       // window.onload= getSurchargeUPI()
<%--         // var user=<%=userAgent%>;
 --%>        	// alert(user);
  
 //document.getElementById("op-pupi").click();
 debugger
 
 
 
// alert("YesNo");
 document.getElementById("UPIMode").style.display="none";
 document.getElementById("showPayement").checked = true;
 var butt = document.getElementById("fix");
 butt.style.display = "none";
debugger
var isintent="<%=instrumentHash.get("isintentUPI")%>";
 if(isintent=='0'){
	 
	 
	 document.getElementById("checkone").checked = true;
	 document.getElementById("UPIMode").style.display="block";
	// document.getElementById("showPayementDiv").style.display="none";
     $('.showPayementDiv').hide();
     butt.style.display = "block";
	 
 }else if(isintent=='1'){
	 
	 getQRData1();
	 document.getElementById("showPayement").checked = true;

 }else if(isintent=='2'){
	 getQRData1();
	 
	 }
 

	
	$('input[type="radio"]').click(function() {
		debugger
		   var payButton = document.getElementById("fix");
		var inputValuee = $(this).attr("value");
		var targetBox = $("." + inputValuee);
		$(".showPayementDiv,.UPIMode").not(targetBox).hide();
		if(inputValuee=='UPIMode')
			{
	     
            payButton.style.display = "block";
			}else{
		    payButton.style.display = "none";
			}
		$(targetBox).show();
	});
	

 //alert("Hi");
 var userTest="<%=userAgent%>";
	//alert(userTest);
	if(userTest.includes("Windows")){
 document.getElementById("op-pupi").click();
	}
window.onload= getSelectedUPI()
{
  document.getElementById("upiFirstScreen").click();
};

        $('.data_mid_blk').show()
        // getSurchargeUPI();

        $('.showPayementDiv').hide();
        $('input').keypress(function (event) {
            console.log("-------------------------------------------->");
            let regex = new RegExp("^[a-zA-Z0-9@_-.]+$");
            let key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
            if (!regex.test(key)) {
                event.preventDefault();
                return false;
            }
        });
        $('i.info').hover(function () {
            $(this).siblings('span.text-muted').toggleClass('info-fade-out')
        });
        $('body').bind('cut copy paste', function (e) {
            // swal('cut. copy. paste. not allowed!', '', 'warning');
            e.preventDefault();
        });
        $(document).bind("contextmenu", function (e) {
            return false;
        });
    });
    // $(document).keydown(function (event) {
    //     console.log(event, '----->')
    //     if ((event.ctrlKey == true || event.which == '91' ) && (event.which == '86'
    //         || event.which == '67' || event.which == '88' || event.keyCode == 'I'.charCodeAt(0)
    //         || event.keyCode == 'C'.charCodeAt(0)|| event.keyCode == 'J'.charCodeAt(0))) {
    //         swal('cut. copy. paste. not allowed!');
    //         event.preventDefault();
    //     }
    //     // if ((event.ctrlKey == true || event.which == 91 ) && (event.which == '86'
    //     //     || event.which == '67' || event.which == '88')) {
    //     //     swal('cut. copy. paste. not allowed!');
    //     //     event.preventDefault();
    //     // }
    // });
    $(document).mousedown(function (e) {
        if (e.button == 2) {
            //swal('right-click is disabled!', '', 'warning');
            e.preventDefault();
        }
    });

    function onSelectSaveCard(index) {
        debugger;
        saveCardSelectedIndex = index;
        console.log($(this));
        // alert("--->>", index);
        // $('#psavecard_form').find('input[type="radio"]').click(function () {
        //     var inputValue = $(this).attr("value");
        $("input[id^='cc-cvv-col-']").each(function (i, el) {
            //It'll be an array of elements
            $(this).val('');
            $(this).attr('disabled', true);
        });
        $('#cc-cvv-col-' + index).attr('disabled', false);
        // });
    }

  /*   document.getElementById("showPayement").addEventListener("click", function() {
    	alert("Hi");
	    var payButton = document.getElementById("payNowBtn");
	    if (this.checked) {
	        // If the "showPayement" radio button is checked, disable the "payButton"
	        payButton.style.display = "none";
	    } else {
	        // If the "showPayement" radio button is not checked, enable the "payButton"
	        payButton.style.display = "block";
	    }
	}); */
  /*   <div class="form_blk rdo_btn">
	<div class="form-check form-check-inline">
		<input class="form-check-input" type="radio" name="inlineRadioOptions" id="" value="UPIMode">
		<label class="form-check-label" >Pay With UPI ID</label>
		</div>
		<div class="form-check form-check-inline">
		<input class="form-check-input" type="radio" name="inlineRadioOptions" id="showPayement" value="showPayementDiv">
		<label class="form-check-label">Pay With UPI QR Code</label>
	</div>
</div>
     */
  /*   var showPayementElements = document.querySelectorAll(".rdo_btn");

 // Add a click event listener to each "showPayement" element
 showPayementElements.forEach(function(element) {
	 alert("Hi");
 
     element.addEventListener("click", function() {
         // Disable the button
         button.disabled = true;
     }); */
    function checkCardType(cardNo) {
        let cartType;
        if (cardNo.startsWith('34') || cardNo.startsWith('37')) {
            cartType = 'Amex';
        } else if (cardNo.startsWith('6011') || myCardNo.startsWith('622') || myCardNo.startsWith('64')) {
            cartType = 'Discover';
        } else if (cardNo.startsWith('36') || cardNo.startsWith('38')) {
            cartType = 'DinersClub';
        } else if (cardNo.startsWith('51') || cardNo.startsWith('52') || myCardNo.startsWith('53') || myCardNo.startsWith('54') || myCardNo.startsWith('55')) {
            cartType = 'MasterCard';
        } else if (cardNo.startsWith('5018') || myCardNo.startsWith('5020') || myCardNo.startsWith('5038') || myCardNo.startsWith('6304') || myCardNo.startsWith('6759') || myCardNo.startsWith('6761') || myCardNo.startsWith('6762') || myCardNo.startsWith('6763')) {
            cartType = 'Maestro';
        } else if (cardNo.startsWith('4')) {
            cartType = 'Visa';
        } else if (cardNo.startsWith('606') || cardNo.startsWith('607') || myCardNo.startsWith('608') || myCardNo.startsWith('65') || myCardNo.startsWith('508')) {
            cartType = 'Rupay';
        } else {
            cartType = 'Unknown';
        }

        return cartType;
    }

    var formToSubmit;
    let saveCardSelectedIndex;
    let formValArr;
    disableClick = false;
    $('#payNowBtn').prop('disabled', disableClick);

function DynamicValue(val,id) {
    debugger
     document.getElementById(id).value = val;
    }


    function payNowAction() {
    debugger
    document.getElementById("loader").style.display="block";
   // document.getElementById("body").style.opacity="0.5";
   // document.getElementById("body").style.pointer-events="none";
        console.log(formToSubmit);
        let formName = formToSubmit.id.split('-')[1];
       // alert(formName);
        if (disableClick) {
            return false;
        } else {
            disableClick = true;
            let myCardNoVal;
            let cardexpriy;
            let str;
            let currentDate = new Date();
            const txnId = $('#txnId').val();
            switch (formName) {
                case 'pcredit':
                    myCardNoVal = document.getElementById('card-number').value;
                    cardexpriy = document.getElementById('cardExpiry').value;
                    str = cardexpriy.split("/");
                    document.getElementById('card-month').value = str[0];
                    document.getElementById('card-year').value = "20" + str[1];
                    var month = document.getElementById("card-month").value;
                    var year = document.getElementById("card-year").value;
                    myCardNo = myCardNoVal.replace(/ /g, '', "");

                    myCvv = document.getElementById('card-cvc').value;
                    myCardName = document.getElementById('card-holder').value;

                    mobCvv = document.getElementById('cc-cvv-mob').value;

                    if (mobCvv.length === 3) {
                        myCvv = mobCvv;
                    }
                    myCardType = checkCardType(myCardNo);
                    document.getElementById('cccardType').value = myCardType;
                    document.getElementById('emicardType').value = myCardType;

                    var ccmmVal = document.getElementsByName("card-month")[0].value;
                    // alert("ccmmVal : "+ccmmVal);
                    var ccmm = ccmmVal;
                    if (ccmmVal.length === 1) {
                        ccmm = "0" + ccmmVal;
                    }

                    //  alert("ccmm : "+ccmm.length);
                    var ccyyyy = document.getElementsByName("card-year")[0].value;
                    if (myCardNo === '' || myCardNo === '0000 0000 0000 0000' || myCardNo < '15') {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("Please Enter Valid Card number.", '', 'error');
                        return false;
                    }

                    if (!validate_cvv(myCvv)) {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("Please enter valid CVV.", '', 'error');
                        return false;
                    }
                    var ccyy = ccyyyy.toString().substr(-2);  // 2018 --> 18

                    if (myCardType === 'Amex') {
                        ccyy = ccyyyy.toString();
                    }
                    var exCC = ccmm + "/" + ccyy;

                    if (myCardName === '') {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("Please Enter Card Holder's Name.", '', 'error');
                        document.getElementById('card-holder').focus();
                        return false;
                    } else if (formName === 'ccForm' && ccmm === '' && ccmm.length !== 2) {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("Please Enter Correct Expiry Month.", '', 'error');
                        document.getElementById("card-month").focus();
                        return false;
                    } else if (formName === 'ccForm' && ccyy === '' && ccyy.length !== 2) {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("Please Enter Correct Expiry Year.", '', 'error');
                        document.getElementById("card-year").focus();
                        return false;
                    }

                    if (myCardType !== "Maestro" && ccyyyy === '' || ccyyyy.length !== 4 || ccyyyy.length > 4 || ccyyyy < currentDate.getFullYear()) {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("Please Enter Correct Expiry Year.", '', 'error');
                        return false;
                    }

                    if (myCardType !== "Maestro" && ccmm <= currentDate.getMonth() && ccyyyy <= currentDate.getFullYear()) {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("Please Enter Correct Expiry month.", '', 'error');
                        return false;
                    }

                    if (myCardType !== "Maestro" && ccmm > 12 || ccmm.length === 0) {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("Please Enter Correct Expiry month.", '', 'error');
                        return false;
                    }

                    if ((myCardType === "Maestro") && ((ccmm == null || ccmm.length === 0) || (ccyy == null || ccyy.length === 0))) {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("If expiry date and CCV are not available then use following values-\nExpiry : 01/2023 \nCVV : 123", '', 'error');
                        return false;
                    }
                    var encryptedExp = CryptoJS.AES.encrypt(exCC, CryptoJS.enc.Hex.parse("<%=iv%>"),
                        {
                            iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                            mode: CryptoJS.mode.CBC,
                            padding: CryptoJS.pad.Pkcs7
                        });

                    var encryptedExpInHex = encryptedExp.ciphertext.toString(CryptoJS.enc.Hex);

                    document.getElementById("CCexpiry").value = encryptedExpInHex;

                    document.getElementById("card-month").value = '1';
                    document.getElementById("card-year").value = '2021';

                    // alert(myCardNo+"|"+txnId);
                    var encryptedCard = CryptoJS.AES.encrypt(myCardNo + "|" + txnId, CryptoJS.enc.Hex.parse("<%=iv%>"),
                        {
                            iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                            mode: CryptoJS.mode.CBC,
                            padding: CryptoJS.pad.Pkcs7
                        });

                    var encryptedCardInHex = encryptedCard.ciphertext.toString(CryptoJS.enc.Hex);

                    var encryptedCvv = CryptoJS.AES.encrypt(myCvv, CryptoJS.enc.Hex.parse("<%=iv%>"),
                        {
                            iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                            mode: CryptoJS.mode.CBC,
                            padding: CryptoJS.pad.Pkcs7
                        });
                    var encryptedCvvInHex = encryptedCvv.ciphertext.toString(CryptoJS.enc.Hex);

                    $('#card-number').val(encryptedCardInHex);
                    $('#card-cvc').val(encryptedCvvInHex);
                    $('#card-holder').val(myCardName);
                    break;
                    
                case 'pdebit':
                    myCardNoVal = document.getElementById('card-number1').value;
                    cardexpriy = document.getElementById('cardExpiry1').value;
                    str = cardexpriy.split("/");
                    document.getElementById('card-month1').value = str[0];
                    document.getElementById('card-year1').value = "20" + str[1];
                    myCardNo = myCardNoVal.replace(/ /g, '', "");

                    myCvv = document.getElementById('card-cvc1').value;
                    myCardName = document.getElementById('card-holder1').value;

                    mobCvv = document.getElementById('dc-cvv-mob').value;

                    if (mobCvv.length === 3) {
                        myCvv = mobCvv;
                    }
                    myCardType = checkCardType(myCardNo);
                    document.getElementById('dccardType').value = myCardType;
                    document.getElementById('emicardType').value = myCardType;
                    var dcmmVal = document.getElementById("card-month1").value;

                    var dcmm = dcmmVal;
                    if (dcmmVal.length === 1) {
                        dcmm = "0" + dcmmVal;
                    }

                    var dcyyyy = document.getElementById("card-year1").value;
                    var dcyy = dcyyyy.toString().substr(-2);  // 2018 --> 18

                    var exDC = dcmm + "/" + dcyy;

                    if (myCardNo === '' || myCardNo === '0000 0000 0000 0000' || myCardNo < '15') {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("Please Enter Valid Card number.", '', 'error');
                        return false;
                    }
                    if (!validate_cvv(myCvv)) {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("Please enter valid CVV.", '', 'error');
                        return false;
                    }



                    if (myCardName === '') {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("Please Enter Card Holder's Name.", '', 'error');
                        document.getElementById('card-holder1').focus();
                        return false;
                    } else if (dcmm === '' && dcmm.length !== 2) {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("Please Enter Expiry Month.", '', 'error');
                        document.getElementById("card-month1").focus();
                        return false;
                    } else if (dcyy === '' && dcyy.length !== 2) {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("Please Enter Expiry Year.", '', 'error');
                        document.getElementById("card-year1").focus();
                        return false;
                    } else if (myCvv === '' || myCvv.length < 3) {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("Please Enter Valid CVV.", '', 'error');
                        document.getElementById('card-cvc1').focus();
                        return false;
                    }

                    if (myCardType !== "Maestro" && dcyyyy === '' || dcyyyy.length !== 4 || dcyyyy.length > 4 || dcyyyy < currentDate.getFullYear()) {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("Please Enter Correct Expiry Year.", '', 'error');
                        return false;
                    }

                    if (myCardType !== "Maestro" && dcmm <= currentDate.getMonth() && dcyyyy <= currentDate.getFullYear()) {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("Please select the correct expiry month.", '', 'error');
                        return false;
                    }

                    if (myCardType !== "Maestro" && dcmm > 12 || dcmm.length === 0) {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("Please select the correct expiry month.", '', 'error');
                        return false;
                    }
                    if ((myCardType === "Maestro") && ((dcmm == null || dcmm.length === 0) || (dcyy == null || dcyy.length === 0))) {
                        disableClick = false;
                        document.getElementById("loader").style.display = "none";
                        swal("If expiry date and CCV are not available then use following values-\nExpiry : 01/2023 \nCVV : 123", '', 'error');
                        return false;
                    }
                    var encryptedExp = CryptoJS.AES.encrypt(exDC, CryptoJS.enc.Hex.parse("<%=iv%>"),
                        {
                            iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                            mode: CryptoJS.mode.CBC,
                            padding: CryptoJS.pad.Pkcs7
                        });

                    var encryptedExpInHex = encryptedExp.ciphertext.toString(CryptoJS.enc.Hex);

                    document.getElementById("DCExpiry").value = encryptedExpInHex;
                    document.getElementById("card-month1").value = '1';
                    document.getElementById("card-year1").value = '2021';

                    // alert(myCardNo+"|"+txnId);
                    var encryptedCard = CryptoJS.AES.encrypt(myCardNo + "|" + txnId, CryptoJS.enc.Hex.parse("<%=iv%>"),
                        {
                            iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                            mode: CryptoJS.mode.CBC,
                            padding: CryptoJS.pad.Pkcs7
                        });

                    var encryptedCardInHex = encryptedCard.ciphertext.toString(CryptoJS.enc.Hex);

                    var encryptedCvv = CryptoJS.AES.encrypt(myCvv, CryptoJS.enc.Hex.parse("<%=iv%>"),
                        {
                            iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                            mode: CryptoJS.mode.CBC,
                            padding: CryptoJS.pad.Pkcs7
                        });

                    var encryptedCvvInHex = encryptedCvv.ciphertext.toString(CryptoJS.enc.Hex);
                    $('#card-number1').val(encryptedCardInHex);
                    $('#card-cvc1').val(encryptedCvvInHex);
                    $('#card-holder1').val(myCardName);
                    break;
                case 'pwallet':
                    formValArr = $('#' + formName + '_form').serializeArray();
                    const selectedWallet = $('#' + formName + '_form').find('#Wallet-bank-select').val();
                    if (!selectedWallet) {
                        document.getElementById("loader").style.display = "none";
                        swal('Please select Wallet from the list', '', 'error')
                        disableClick = false;
                        return false
                    }
                    break;
                case 'poffline':
                  formValArr = $('#' + formName + '_form').serializeArray();
                //  formName='poffline';
                //  const selectedBank = $('#' + formName + '_form').find('#bank-offline').val();
                   // alert($('#bank-offline').val());
                   alert($('#bank-offline').val());
 var selectedop=$('#bank-offline').val();

                   if (selectedop=='') {
                        swal('Please select Bank from the list', '', 'error')
                       disableClick = false;
                        document.getElementById("loader").style.display="none";
                        return false
                  }
                    disableClick = false;
                    // return false

                    break;
                    
                case 'pupi':
                    let txnIdUPI = $('#' + formName + '_form').find('#txnId').val();
                    let instrumentId = "UPI";
                    let cardType = "UPI";
		    /*  added for upi */
                    if(upiIdValue != 'otherApp'){
                        var inputString = document.getElementById("inputUPI").value;
                      //  var upiId = document.getElementById("inputUPI23").value;
                        var VPAValue = inputString;
                        document.forms[formName + '_form'].otherAppValue.value = "false"
                    }else{
                        var VPAValue = document.getElementById("inputUPIOtherApp").value;
                        document.forms[formName + '_form'].otherAppValue.value = "true"
                    }
                    // let VPAValue = document.getElementById("VPA").value;
                    document.getElementById("VPA").value = VPAValue;
                    let checksumValue = document.getElementById("checksum").value;
                    let MTrackid = $('#txnId').val();
                    $("#payNowUPI").css("pointer-events", "none");
                    if (!VPAValue) {
                    document.getElementById("loader").style.display="none";
                        swal('Please enter you upi id / virtual address', '', 'error');
                        disableClick = false;
                        return false;
                    }
                    $.ajax
                    ({
                        type: 'POST',
                        data: {
                            txnId: txnIdUPI,
                            instrumentId: instrumentId,
                            cardType: cardType,
                            VPA: VPAValue,
                            checksum: checksumValue,
                            MTrackid: txnIdUPI
                        },
                        url: "/pay/UpiCheckServlet",
                        success: function (result) {
                            //console.log(result);
                            //if (result.match("failure")) {
                              //  $("#payNowUPI").css("pointer-events", "auto");
                            //} else if (result.match("success")) {
                                // document.getElementById("UPIForm").submit();
                             //   $('#' + formName + '_form').submit();
                           // }


console.log(result);
					if (result.match("failure"))
					{
					document.getElementById("loader").style.display="none";
						document.getElementById("VPA").value="";
						swal("Enter the Valid VPA", '', 'error');

						//alert("Enter the Valid VPA");
						$( "#payNowUPI" ).css( "pointer-events", "auto" );
					}
					else if(result.match("success")||result.match("null"))
					{
					//	document.getElementById("UPIForm").submit();
						 $('#' + formName + '_form').submit();
					}


                        },
                        error: function (err) {
                        document.getElementById("loader").style.display="none";
                                                    console.log(err);

                            document.getElementById("VPA").value = "";
                            swal("Error while processing your request. Please try again later", '', 'error');
                            $("#payNowUPI").css("pointer-events", "auto");
                        }
                    });
                    disableClick = false;

                    return false;
                    break;
                case 'pnetbanking':
                    formValArr = $('#' + formName + '_form').serializeArray();
                    const selectedBank = $('#' + formName + '_form').find('#bank-select').val();
                   // alert($('#bank-select').val())
                    if (!selectedBank) {
                        swal('Please select Bank from the list', '', 'error')
                        disableClick = false;
                        document.getElementById("loader").style.display="none";
                        return false
                    }
                    disableClick = false;
                    // return false

                    break;
                
                case 'psavecard':
                    formValArr = $('#' + formName + '_form').serializeArray();
                    console.log("formValArr", formValArr);
                    console.log(saveCardSelectedIndex);
                                      //  alert(saveCardSelectedIndex);

                    if (!(saveCardSelectedIndex >= 0)) {
                        swal('Please select from listed cards', '', 'error')
                        disableClick = false;
                        document.getElementById("loader").style.display="none";
                        return false
                    }
                    let selectedCardCvv = formValArr.find((fa) => fa.name == 'cvv-' + saveCardSelectedIndex && fa.value !== '')
                    let selectedCard = formValArr.find((fa) => fa.name == 'cardNo')
                    let selectedCardExp = formValArr.find((fa) => fa.name == 'expiry')
                    console.log("selectedCardCvv--->", selectedCardCvv);
                  // alert("selectedCardCvv--->", selectedCardCvv);
                    if (!selectedCardCvv) {
                    document.getElementById("loader").style.display="none";
                        swal('Please enter CVV for selected card', '', 'error')
                        disableClick = false;
                        return false
                    }
                 //   alert(validate_cvv(selectedCardCvv?.value))
                    if (!validate_cvv(selectedCardCvv?.value)) {

                        disableClick = false;
                        document.getElementById("loader").style.display="none";
                        swal("Please enter valid CVV.", '', 'error');
                        return false;
                    }
                    let encryptedSaveCard = CryptoJS.AES.encrypt(selectedCard?.value + "|" + txnId, CryptoJS.enc.Hex.parse("<%=iv%>"),
                        {
                            iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                            mode: CryptoJS.mode.CBC,
                            padding: CryptoJS.pad.Pkcs7
                        });

                    let encryptedSaveCardInHex = encryptedSaveCard.ciphertext.toString(CryptoJS.enc.Hex);

                    let encryptedSaveCvv = CryptoJS.AES.encrypt(selectedCardCvv?.value, CryptoJS.enc.Hex.parse("<%=iv%>"),
                        {
                            iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                            mode: CryptoJS.mode.CBC,
                            padding: CryptoJS.pad.Pkcs7
                        });

                    let encryptedSavedCvvInHex = encryptedSaveCvv.ciphertext.toString(CryptoJS.enc.Hex);
                    var encryptedSavedExp = CryptoJS.AES.encrypt(selectedCardExp.value, CryptoJS.enc.Hex.parse("<%=iv%>"),
                        {
                            iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                            mode: CryptoJS.mode.CBC,
                            padding: CryptoJS.pad.Pkcs7
                        });

                    var encryptedSaveExpInHex = encryptedSavedExp.ciphertext.toString(CryptoJS.enc.Hex);
                    $('#psavecard_form').find('#decCardNoCC').val(encryptedSaveCardInHex);
                    $('#psavecard_form').find('#cvvSCCC').val(encryptedSavedCvvInHex);
                    $('#psavecard_form').find('#cc-cvv-col-' + saveCardSelectedIndex).val(encryptedSavedCvvInHex);
                    $('#psavecard_form').find('#SCExpiryCC').val(encryptedSaveExpInHex);
                    disableClick = false;
                    let formValArr2 = $('#' + formName + '_form').serializeArray();
                  //  alert(formValArr2)
                    // console.log("formValArr", formValArr2);
                    // return false;
                    break;
                case 'pemi':
                                    formValArr = $('#' + formName + '_form').serializeArray();
                                    const selectedEMI = $('#' + formName + '_form').find('#dynamic-select').val();
                                    if (!selectedEMI) {
                                    document.getElementById("loader").style.display="none";
                                        swal('Please select EMI  from the list', '', 'error')
                                        disableClick = false;
                                        return false
                                    }
                                    break;
                case 'ppaymentone':
                    break;
                default:
            }

            $('#' + formName + '_form').submit(); //.trigger('submit');
            disableClick = false;
            return true;

        }

document.getElementById("loader").style.display="none";

    }

    function myCardChanged(e) {

        creditcardutils.parseCardType('4242-4242-4242-4242')
    }

    function checkDigit() {
        const code = (event.which) ? event.which : event.keyCode;
        return !((code < 48 || code > 57) && (code > 31));
    }

    function onkeyupCard() {
        const current_value = document.getElementById('card-number').value;
        document.getElementById('card-number').value = cc_format(current_value);
        let html = '';

        switch (creditcardutils.parseCardType(current_value)) {
            case 'mastercard':
                html = '<img src="assets/images/master.svg" style="height:100%; width: 30px;">';
                break;
            case 'visa':
                html = '<img src="assets/images/visa.svg" style="height:100%; width: 30px;">';
                break;
        }
        $('#cardImg').html(html);
    }

    function cc_format(value) {

        var v = value.replace(/\s+/g, '').replace(/[^0-9]/gi, '')
        var matches = v.match(/\d{4,16}/g);
        var match = matches && matches[0] || ''
        var parts = []

        for (i = 0, len = match.length; i < len; i += 4) {
            parts.push(match.substring(i, i + 4))
        }

        if (parts.length) {
            return parts.join('|')
        } else {
            return value
        }

    }
</script>

	<script>
        $(document).ready(function () {
            document.getElementById("loader").style.display="none";
           

            
            var x, i, j, l, ll, selElmnt, a, b, c;
            /*look for any elements with the class "custom-select":*/
            x = document.getElementsByClassName("custom-select");
            x = $(".custom-select");
            console.log("---curom select---->", x)
            l = x.length;
            for (i = 0; i < l; i++) {
                console.log("---curom select ind---->", x[i].getElementsByTagName("select"))
                selElmnt = x[i].getElementsByTagName("select")[0];
             /*   ll = selElmnt.length; */
              ll = selElmnt?.length || 0;
	        /*for each element, create a new DIV that will act as the selected item:*/
                a = document.createElement("DIV");
                a.setAttribute("class", "select-selected");
            /*    a.innerHTML = selElmnt.options[selElmnt.selectedIndex].innerHTML; */
            a.innerHTML = selElmnt?.options[selElmnt?.selectedIndex].innerHTML;
	        x[i].appendChild(a);
                /*for each element, create a new DIV that will contain the option list:*/
                b = document.createElement("DIV");
                b.setAttribute("class", "select-items select-hide");
                for (j = 1; j < ll; j++) {
                    /*for each option in the original select element,
                    create a new DIV that will act as an option item:*/
                    c = document.createElement("DIV");
                 /*   c.innerHTML = selElmnt.options[j].innerHTML; */
		     c.innerHTML = selElmnt?.options[j]?.innerHTML;
                    c.addEventListener("click", function(e) {
                        /*when an item is clicked, update the original select box,
                        and the selected item:*/
                        var y, i, k, s, h, sl, yl;
                        s = this.parentNode.parentNode.getElementsByTagName("select")[0];
                        sl = s.length;
                        h = this.parentNode.previousSibling;
                        for (i = 0; i < sl; i++) {
                            if (s.options[i].innerHTML == this.innerHTML) {
                                s.selectedIndex = i;
                                h.innerHTML = this.innerHTML;
                                y = this.parentNode.getElementsByClassName("same-as-selected");
                                yl = y.length;
                                for (k = 0; k < yl; k++) {
                                    y[k].removeAttribute("class");
                                }
                                this.setAttribute("class", "same-as-selected");
                                break;
                            }
                        }
                        h.click();
                    });
                    b.appendChild(c);
                }
                x[i].appendChild(b);
                a.addEventListener("click", function(e) {
                    /*when the select box is clicked, close any other select boxes,
                    and open/close the current select box:*/
                    e.stopPropagation();
                    closeAllSelect(this);
                    this.nextSibling.classList.toggle("select-hide");
                    this.classList.toggle("select-arrow-active");
                });
            }
            function closeAllSelect(elmnt) {
                /*a function that will close all select boxes in the document,
                except the current select box:*/
                var x, y, i, xl, yl, arrNo = [];
                x = document.getElementsByClassName("select-items");
                y = document.getElementsByClassName("select-selected");
                xl = x.length;
                yl = y.length;
                for (i = 0; i < yl; i++) {
                    if (elmnt == y[i]) {
                        arrNo.push(i)
                    } else {
                        y[i].classList.remove("select-arrow-active");
                    }
                }
                for (i = 0; i < xl; i++) {
                    if (arrNo.indexOf(i)) {
                        x[i].classList.add("select-hide");
                    }
                }
            }
            /*if the user clicks anywhere outside the select box,
            then close all select boxes:*/
            document.addEventListener("click", closeAllSelect);
        });

</script>


	<!-- For Timer -->
	<script> 
     var timerInterval = null; 

     function startTimer(duration, display) { 
         var timer = duration, minutes, seconds; 
         timerInterval = setInterval(function () { 
            minutes = parseInt(timer / 60, 10); 
             seconds = parseInt(timer % 60, 10);

            minutes = minutes < 10 ? "0" + minutes : minutes; 
            seconds = seconds < 10 ? "0" + seconds : seconds; 

             display.textContent = minutes + ":" + seconds; 

             if (--timer < 0) { 
                 timer = duration; 
      }
        }, 1000); 
    } 

     window.onload = function () {
  document.getElementById("loader").style.display="none";

        initiateTimer(); 
     }; 

     function initiateTimer() { 
         var twoMinutes = 60 * 5, 
            display = document.querySelector('#time');
       startTimer(twoMinutes, display); 
    } 

 </script>

	<!-- For Tab Toggle -->
	<script type='text/javascript'>
    $("li.side_nav").click(function () {
        clearInterval(timerInterval);

        let selectedItem = $(this)
        console.log("------>", selectedItem[0].id);
        formToSubmit = selectedItem[0];
        let formName = formToSubmit.id.split('-')[1];
        initiateTimer();
        $(".item").hide();
        $(".data_mid_blk").show();
        // $('.item').eq($(this).index()).fadeIn();
        $('#' + formName).fadeIn();
    })
</script>

	<!-- For Tab Active -->
	<script type="text/javascript">
    $(document).ready(function () {
        $("li.side_nav").click(function () {
            $("li.side_nav").removeClass("active");
            $(this).addClass("active");
        });
    });
</script>

	<!-- Radio BTN Selection -->
	<script type="text/javascript">
    // $(document).ready(function () {
    console.log("-------+++++++>", $('#pupi').find('input[type="radio"]'))
    $('#pupi').find('input[type="radio"]').click(function () {
        var inputValue = $(this).attr("value");
        // alert(inputValue + "----->");
        var targetBox = $("." + inputValue);
        console.log("---->", targetBox)
        $(".upi_id, .qr_code").not(targetBox).hide();
        $(targetBox).show();
    });
    // });
</script>

	<!-- <script type="text/javascript">
        function checkWidth() {
            if ($(window).width() < 991) {
                $('#addClass').addClass('hdDivs');
            } else {
                $('#addClass').removeClass('hdDivs');
            }
        }

        $(window).resize(checkWidth);
    </script> -->

	<script type="text/javascript">
    function responsive(maxWidth) {
        if (maxWidth.matches) {
            $('#addClass').addClass('hdDivs');

        } else {
            $('#addClass').removeClass('hdDivs');
        }
    }

    var maxWidth = window.matchMedia("(max-width: 991px)");

    responsive(maxWidth);
    maxWidth.addListener(responsive);
</script>

	<script type="text/javascript">
    function responsive(maxWidth) {
        if (maxWidth.matches) {
        	//alert("Check");
        }
            $('#addSelectClass').addClass('select');

        } else {
            $('#addSelectClass').removeClass('select');
        }
    }

    var maxWidth = window.matchMedia("(max-width: 991px)");

    responsive(maxWidth);
    maxWidth.addListener(responsive);
</script>

	<script type="text/javascript">
    $('.select ul li.option').click(function () {
        $(this).siblings().children();
        var a = $(this).siblings().toggle();

    })
</script>

	<script>
    $(document).ready(function () {
        $(".hdDivs").hide();

        $('#btn1').click(function (e) {
            $(".hdDivs").slideToggle("fast");
            var val = $(this).text() == "-" ? "+" : "-";
            $(this).hide().text(val).fadeIn("fast");
            e.preventDefault();
        });
    });
</script>

	<!-- Expiry Date Format -->
	<script type="text/javascript">
    function formatString(e) {
        var inputChar = String.fromCharCode(event.keyCode);
        var code = event.keyCode;
        var allowedKeys = [8];
        if (allowedKeys.indexOf(code) !== -1) {
            return;
        }

        event.target.value = event.target.value.replace(
            /^([1-9]\/|[2-9])$/g, '0$1/' // 3 > 03/
        ).replace(
            /^(0[1-9]|1[0-2])$/g, '$1/' // 11 > 11/
        ).replace(
            /^([0-1])([3-9])$/g, '0$1/$2' // 13 > 01/3
        ).replace(
            /^(0?[1-9]|1[0-2])([0-9]{2})$/g, '$1/$2' // 141 > 01/41
        ).replace(
            /^([0]+)\/|[0]+$/g, '0' // 0/ > 0 and 00 > 0
        ).replace(
            /[^\d\/]|^[\/]*$/g, '' // To allow only digits and `/`
        ).replace(
            /\/\//g, '/' // Prevent entering more than 1 `/`
        );
    }
</script>


	<script type="text/javascript">
    const upiAddress = 'success@upi';
    upiAddress.replace(/(?<=.).(?=[^@]*?@)/g, "x");
    // output : sxxxxxx
    // @xyz
</script>

	<script type="text/javascript">
    function validate_cvv(cvv) {

        var myRe = /^[0-9]{3,4}$/;
        var myArray = myRe.exec(cvv);
        if (cvv != myArray) {
            //invalid cvv number
            return false;
        } else {
            return true;  //valid cvv number
        }

    }
</script>

	<script>
    function myCardChanged(e) {

        creditcardutilsOne.parseCardType('4242 4242 4242 4242')
    }

    function checkDigitOne() {
        var code = (event.which) ? event.which : event.keyCode;
        if ((code < 48 || code > 57) && (code > 31)) {
            return false;
        }

        return true;
    }

    function onkeyupCardOne() {


        var current_value = document.getElementById('card-number1').value;
        document.getElementById('card-number1').value = cc_format(current_value);
        var html = '';
        switch (creditcardutilsOne.parseCardType(current_value)) {
            case 'mastercard':
                html = '<img src="assets/images/master.svg" style="height:100%; width: 20px;">';
                break;
            case 'visa':
                html = '<img src="assets/images/visa.svg" style="height:100%; width: 20px;">';
                break;
        }
        $('#cardImgOne').html(html);
    }

    function cc_format(value) {
        var v = value.replace(/\s+/g, '').replace(/[^0-9]/gi, '')
        var matches = v.match(/\d{4,16}/g);
        var match = matches && matches[0] || ''
        var parts = []

        for (i = 0, len = match.length; i < len; i += 4) {
            parts.push(match.substring(i, i + 4))
        }

        if (parts.length) {
            return parts.join(' ')
        } else {
            return value
        }
    }
</script>
	<script type="text/javascript">

    var cc = 0;
    var dc = 0;

    var creditCardVisibility = <%=creditCardVisibility%>;
    var debitCardVisibility = <%=debitCardVisibility%>;
    var netBankVisibility = <%=netBankVisibility%>;
    var walletVisibility = <%=walletVisibility%>;
    var bqrVisibility = <%=bqrVisibility%>;
    var upiVisibility = <%=upiVisibility%>;
    var atomUPIVisibility = <%=atomUPIVisibility%>;
    var opVisibility = <%=opVisibility%>;
    var emiVisibility = <%=emiVisibility%>


        $(function () {
            $('.txtOnly').keydown(function (e) {
                if (e.ctrlKey || e.altKey) {
                    e.preventDefault();
                } else {
                    var key = e.keyCode;
                    if (!((key == 8) || (key == 9) || (key == 32) || (key == 46) || (key >= 35 && key <= 40) || (key >= 65 && key <= 90))) {
                        e.preventDefault();
                    }
                }
            });
        });


    /* $(document).ready(function(){
    $('.cdnumberOnly').on('keypress', function () {
        $(this).val(function (index, value) {

            return value.replace(/[^a-z0-9]+/gi, '').replace(/(.{4})/g, '$1 ');

        });
    });
        */

    /* $('.cdnumberOnly').on('keypress change blur', function () {
           $(this).val(function (index, value) {

               return value.replace(/[^a-z0-9]+/gi, '').replace(/(.{4})/g, '$1 ');

           });
            */

           

     $('#card-number').blur(function (event)
    {


        cardNumber1 = $('#card-number').val();
        cardNumber = cardNumber1.replace(/-/, "");
        var cardType = Stripe.card.cardType(cardNumber);

       document.getElementById('cardType').value = cardType;
        
        getSurchargeCC();

        /*  switch (cardType)
         {

            case 'Visa':
                  document.getElementById('card-number').className="visa";
                break;

            case 'MasterCard':
                document.getElementById('card-number').className="master";
                break;
            case 'Discover':
                document.getElementById('card-number').className="discover";
                break;

            case 'American Express':
                document.getElementById('card-number').className="american";
                break;
             case 'Diners Club':
                 document.getElementById('card-number').className="dinner";
                break;

             case 'Rupay':
                 document.getElementById('card-number').className="rupay";
                 break;

             case 'Maestro':
                 document.getElementById('card-number').className="maestro";
                 break;

             case 'Unknown':
                 document.getElementById('card-number').className="other";
                 break;
         }
 */    });

     $('#card-number1').blur(function (event)
                {

                    cardNumber1 = $('#card-number1').val();

                    cardNumber = cardNumber1.replace(/-/, "");
                 // swal("cardNumber"+cardNumber, '', 'error');

                    // alert("cardNumber"+cardNumber);

                    var cardType = Stripe.card.cardType(cardNumber);

                    document.getElementById('dccardType').value = cardType;
                    getSurchargeDC();

                     /* switch (cardType)
                     {

                        case 'Visa':
                              document.getElementById('card-number1').className="visa";
                            break;

                        case 'MasterCard':
                            document.getElementById('card-number1').className="master";
                            break;
                        case 'Discover':
                            document.getElementById('card-number1').className="discover";
                            break;

                        case 'American Express':
                            document.getElementById('card-number1').className="american";
                            break;
                         case 'Diners Club':
                             document.getElementById('card-number1').className="dinner";
                            break;

                         case 'Rupay':
                             document.getElementById('card-number1').className="rupay";
                             break;

                         case 'Maestro':
                             document.getElementById('card-number1').className="maestro";
                             break;

                         case 'Unknown':
                             document.getElementById('card-number1').className="other";
                             break;
                     } */
                });


    function storedcardFillDC(cardno) {
        // alert("storedcardFillDC()  :: "+cardno);

        var strArray = cardno.split(",");
        var value_i = strArray[0];
        var cNumber = strArray[1];
        var maskedCNumber = strArray[2];
        var cExpiry = strArray[3];
        var cName = strArray[4];
        var cInstrument = strArray[5];
        var cType = strArray[6];

        var encKey = strArray[7];
        var keKey = strArray[8];
        var len = strArray[12];


        $.ajax
        ({
            type: 'POST',
            data: {encCardNo: cNumber, encKey: encKey, keKey: keKey},
            url: "<%=request.getContextPath()%>/ajaxAction?type=decryptCard",
            success: function (result) {
                // alert("Result ::: "+result);
                // array[x] = [result,maskedCNumber,cExpiry,cName,cType,cInstrument];
                // alert(result);
                document.getElementById("decCardNoDC").value = result;
                document.getElementById("card-numberSCDC").value = maskedCNumber;
                document.getElementById("SCExpiryDC").value = cExpiry

                document.getElementById("card-holderSCDC").value = cName;

                document.getElementById("sccardTypeDC").value = cType;
                document.getElementById("scinstrumentIdDC").value = cInstrument;

                // alert("sccardType :: "+cType+"  scinstrumentIdCC "+cInstrument);

                document.forms['psavecard_form']['scinstrumentIdDC'].value = cInstrument;
                document.forms['psavecard_form']['value_of_iDC'].value = value_i;

                // alert("cInstrument  ::  "+document.getElementById("scinstrumentId").value);
                // $("#scFormDC")[0].reset();
                getSurchargeSDC();
            },
            error: function (err) {
                swal("Error in getting Card Number " + err, '', 'error');
            }

        });

    }


    function ValidateCVVDC() {
        $('#payNowDC').attr('disabled', false);
        var val_i = document.forms['scFormDC']['value_of_iDC'].value;

        if (val_i == '' || val_i == "") {
            $('#payNowDC').attr('disabled', true);
            swal("Please enter the cvv must be 3 digit !", '', 'error');
            return false;
        } else {
            var len = document.getElementById("dc-cvv-col-" + val_i).value.length;
            var cvvVal = document.getElementById("dc-cvv-col-" + val_i).value;

            if (len < 3) {
                $('#payNowDC').attr('disabled', true);
                swal("Please enter the cvv must be 3 digit !", '', 'error');
                return false;
            } else {

                var myCardNo = document.getElementById("decCardNoDC").value;
                var expEnc = document.getElementById("SCExpiryDC").value;

                var txnId = $('#txnId').val();

                // alert("myCardNo : "+myCardNo+"  , expEnc : "+expEnc);
                var encryptedCard = CryptoJS.AES.encrypt(myCardNo + "|" + txnId, CryptoJS.enc.Hex.parse("<%=iv%>"),
                    {
                        iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                        mode: CryptoJS.mode.CBC,
                        padding: CryptoJS.pad.Pkcs7
                    });

                var encryptedCardInHex = encryptedCard.ciphertext.toString(CryptoJS.enc.Hex);

                // 2

                var encryptedExp = CryptoJS.AES.encrypt(expEnc, CryptoJS.enc.Hex.parse("<%=iv%>"),
                    {
                        iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                        mode: CryptoJS.mode.CBC,
                        padding: CryptoJS.pad.Pkcs7
                    });

                var encryptedExpInHex = encryptedExp.ciphertext.toString(CryptoJS.enc.Hex);

                // 3
                var encryptedCvv = CryptoJS.AES.encrypt(cvvVal, CryptoJS.enc.Hex.parse("<%=iv%>"),
                    {
                        iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                        mode: CryptoJS.mode.CBC,
                        padding: CryptoJS.pad.Pkcs7
                    });

                var encryptedCvvInHex = encryptedCvv.ciphertext.toString(CryptoJS.enc.Hex);
                //alert("Enc :: "+encryptedCardInHex);
                // bharat ST
                // alert("CVV encryptedCvvInHex :  "+encryptedCvvInHex);
                $('#decCardNoDC').val(encryptedCardInHex);
                // document.getElementById("cc-cvv-col-"+val_i).value = encryptedCvvInHex;

                // document.getElementById("cvvSCCC").value = encryptedCvvInHex;
                $('#cvvSCDC').val(encryptedCvvInHex);
                $('#SCExpiryDC').val(encryptedExpInHex);

                $('#payNowDC').attr('disabled', true);
                document.getElementById("scFormDC").submit();
                return true;
            }
        }
    }

    function CheckDCLength(DCsize) {
        if (DCsize != "0") {
            return;
        }
        $('#radio16').prop('checked', true);
        $('#debit-card-info').show();
        $('#dr-pay-02').css('display', 'table');
        $('#radio16').prop('checked', true);
        $('#dc-cvv-col-0, #dc-cvv-col-1,#dc-cvv-col-2, #dc-cvv-col-3, #dc-cvv-col-4,#dc-cvv-col-5, #dr-pay-01, #cvv-dr-col-13').hide();
        $('#dc-cvv-col-0, #dc-cvv-col-1,#dc-cvv-col-2, #dc-cvv-col-3, #dc-cvv-col-4,#dc-cvv-col-5, #dr-pay-01, #cvv-dr-col-13').val('');
        $('#dr-card-new').addClass('resp-tab-active');
        $('#dr-card-01, #dr-card-02, #dr-card-03, #dr-card-04 ,#dr-card-05, #dr-card-06').removeClass('resp-tab-active');
        $("#card-number, #card-holder, #card-cvc, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');
        e.preventDefault();
    }

    function storedcardFill(cardno) {
        /* 	  alert("Card Deatils----- "+cardno);
         */  //	alert("storedcardFill  :: "+cardno);

        var strArray = cardno.split(",");

        var value_i = strArray[0];


        var cNumber = strArray[1];

        var maskedCNumber = strArray[2];

        var cExpiry = strArray[3];
        var cName = strArray[4];
        var cInstrument = strArray[5];
        var cType = strArray[6];
        var encKey = strArray[7];
        var keKey = strArray[8];
        var len = strArray[12];


        $.ajax
        ({
            type: 'POST',
            data: {encCardNo: cNumber, encKey: encKey, keKey: keKey},
            url: "<%=request.getContextPath()%>/ajaxAction?type=decryptCard",
            success: function (result) {
                document.getElementById("decCardNoCC").value = result;
                document.getElementById("card-numberSCCC").value = maskedCNumber;
                document.getElementById("SCExpiryCC").value = cExpiry

                document.getElementById("card-holderSCCC").value = cName;

                document.getElementById("sccardTypeCC").value = cType;
                document.getElementById("scinstrumentIdCC").value = cInstrument;

                document.forms['psavecard_form']['scinstrumentIdCC'].value = cInstrument;
                document.forms['psavecard_form']['value_of_iCC'].value = value_i;

                // alert("cInstrument  ::  "+document.getElementById("scinstrumentId").value);
                // $("#scForm")[0].reset();
                getSurchargeSCC();
            },
            error: function (err) {
                swal("Error in getting Card Number " + err, '', 'error');
            }

        });

    }


    function ValidateCVV() {
        $('#payNowCC').attr('disabled', false);
        var val_i = document.forms['scFormCC']['value_of_iCC'].value;
        if (val_i == '' || val_i == "") {
            $('#payNowCC').attr('disabled', true);
            swal("CVV must be 3 digit !", '', 'error');
            return false;
        } else {
            var len = document.getElementById("cc-cvv-col-" + val_i).value.length;
            var cvvVal = document.getElementById("cc-cvv-col-" + val_i).value;

            if (len < 3) {
                $('#payNowCC').attr('disabled', true);
                swal("CVV must be 3 digit !", '', 'error');
                return false;
            } else {


                var myCardNo = document.getElementById("decCardNoCC").value;
                var expEnc = document.getElementById("SCExpiryCC").value;
                var txnId = $('#txnId').val();

                var encryptedCard = CryptoJS.AES.encrypt(myCardNo + "|" + txnId, CryptoJS.enc.Hex.parse("<%=iv%>"),
                    {
                        iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                        mode: CryptoJS.mode.CBC,
                        padding: CryptoJS.pad.Pkcs7
                    });

                var encryptedCardInHex = encryptedCard.ciphertext.toString(CryptoJS.enc.Hex);

                // 2

                var encryptedExp = CryptoJS.AES.encrypt(expEnc, CryptoJS.enc.Hex.parse("<%=iv%>"),
                    {
                        iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                        mode: CryptoJS.mode.CBC,
                        padding: CryptoJS.pad.Pkcs7
                    });

                var encryptedExpInHex = encryptedExp.ciphertext.toString(CryptoJS.enc.Hex);

                // 3
                var encryptedCvv = CryptoJS.AES.encrypt(cvvVal, CryptoJS.enc.Hex.parse("<%=iv%>"),
                    {
                        iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                        mode: CryptoJS.mode.CBC,
                        padding: CryptoJS.pad.Pkcs7
                    });

                var encryptedCvvInHex = encryptedCvv.ciphertext.toString(CryptoJS.enc.Hex);

                $('#decCardNoCC').val(encryptedCardInHex);
                $('#cvvSCCC').val(encryptedCvvInHex);
                $('#SCExpiryCC').val(encryptedExpInHex);

                $('#payNowCC').attr('disabled', true);
                document.getElementById("scFormCC").submit();
                return true;
            }
        }
    }

   
    $("#isDCRemember").click(function () {
        $(this).prop("checked") ? $(this).val("Y") : $(this).val("N");
    });

    $("#isCCRemember").click(function () {
        $(this).prop("checked") ? $(this).val("Y") : $(this).val("N");
    });

    $("#isCCCRemember").click(function () {
        $(this).prop("checked") ? $(this).val("Y") : $(this).val("N");
    });


    function removeCard(cardno, divId, ccdcSize) {
        // swal("REMOVE CARD");
        var strArray = cardno.split(",");
        var value_i = strArray[0];
        var cNo = strArray[1];
        var cMid = strArray[9];
        var cMail = strArray[10];
        var cMob = strArray[11];
        var remDiv = parseInt(value_i) + 1;
        // alert("remDiv : "+remDiv);
        swal({
            title: "Are you sure ?",
            text: "you want to delete ?",
            icon: "warning",
            buttons: true,
            dangerMode: true,
        })
            .then((willDelete) => {
                if (willDelete) {

                    $.ajax
                    ({
                        type: 'POST',
                        data: {cNo: cNo, cMid: cMid, cMail: cMail, cMob: cMob},
                        url: "<%=request.getContextPath()%>/ajaxAction?type=removeCard",
                        success: function (result) {

                            if (divId == 'cr-card-0') {
                                cc = cc + 1;
                                // alert("CC : "+cc)
                                if (cc == ccdcSize) {
                                    $('#cr-done,#cr-manage').hide();
                                    $('.hide-cards-cr,.two-more-cr').hide();
                                    $('.inputGroup3 label, .inputGroup3 input:checked ~ label').show();
                                    $('#radio13').prop('checked', true);
                                    $('#credit-card-info').show();
                                    $('#cr-pay-02').css('display', 'table');
                                    $('#cr-card-01').css('border', '');
                                    $('#cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3, #cc-cvv-col-4 , #cc-cvv-col-5, #cr-pay-01, #cvv-cr-col-11, #cvv-cr-col-12').hide();
                                    $('#cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3, #cc-cvv-col-4 , #cc-cvv-col-5, #cr-pay-01, #cvv-cr-col-11, #cvv-cr-col-12').val('');
                                    $('#cr-card-01, #cr-card-02, #cr-card-03, #cr-card-04, #cr-card-05, #cr-card-06').removeClass('resp-tab-active');

                                    $("#card-number1, #card-holder1, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');

                                }
                            }

                            if (divId == 'dr-card-0') {
                                dc = dc + 1;
                                // alert("DC : "+dc)

                                if (dc == ccdcSize) {
                                    $('#dr-done,#dr-manage').hide();
                                    $('.hide-cards-dr,.two-more-dr').hide();
                                    $('.inputGroup3 label, .inputGroup3 input:checked ~ label').show();
                                    $('#radio16').prop('checked', true);
                                    $('#debit-card-info').show();
                                    $('#dr-pay-02').css('display', 'table');
                                    $('#radio16').prop('checked', true);
                                    $('#dc-cvv-col-0, #dc-cvv-col-1,#dc-cvv-col-2, #dc-cvv-col-3, #dc-cvv-col-4,#dc-cvv-col-5, #dr-pay-01, #cvv-dr-col-13').hide();
                                    $('#dc-cvv-col-0, #dc-cvv-col-1,#dc-cvv-col-2, #dc-cvv-col-3, #dc-cvv-col-4,#dc-cvv-col-5, #dr-pay-01, #cvv-dr-col-13').val('');
                                    $('#dr-card-new').addClass('resp-tab-active');
                                    $('#dr-card-01, #dr-card-02, #dr-card-03, #dr-card-04 ,#dr-card-05, #dr-card-06').removeClass('resp-tab-active');

                                    $("#card-number, #card-holder, #card-cvc, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');

                                }
                            }

                            $('#' + divId + remDiv).hide();

                            swal("Your stored card has been deleted!", {icon: "success",});

                        },
                        error: function (err) {
                            swal("Error in removing card " + err, '', 'error');
                        }
                    });

                    /* swal("Poof! Your stored card has been deleted!", { icon: "success",}); */
                } else {
                    swal("Your stored card is safe!");
                }
            });

    }


    var disableClick = false;

    function testCreditCard(cardFieldName, formName, myCvvFieldName, cardNameFieldName, mobField, evt, cardExpiryfileds) {

        if (disableClick) {
            return false;
        } else {
            console.log("testCreditCard method-----");
            disableClick = true;
            var currentDate = new Date();
            var cardexpriy = document.getElementById(cardExpiryfileds).value;
            const str = cardexpriy.split("/");
            // alert(str[0]);
            // alert(str[1]);
            if (cardFieldName === "card-number" && formName === 'ccForm') {
                document.getElementById('card-month').value = str[0];
                document.getElementById('card-year').value = "20" + str[1];
                var month = document.getElementById("card-month").value;
                var year = document.getElementById("card-year").value;
            } else {
                document.getElementById('card-month1').value = str[0];
                document.getElementById('card-year1').value = "20" + str[1];
            }
            var myCardNoVal = document.getElementById(cardFieldName).value;
            /* 		 myCardNo = myCardNoVal.replace(/-/g, "");
             */
            myCardNo = myCardNoVal.replace(/ /g, '', "");

            myCvv = document.getElementById(myCvvFieldName).value;
            myCardName = document.getElementById(cardNameFieldName).value;

            mobCvv = document.getElementById(mobField).value;

            if (mobCvv.length === 3) {
                myCvv = mobCvv;
            }

            //alert("myCardNo="+myCardNo);
            //alert("myCardNo.startwith="+myCardNo);
            if (myCardNo.startsWith('34') || myCardNo.startsWith('37')) {
                myCardType = 'Amex';
            } else if (myCardNo.startsWith('6011') || myCardNo.startsWith('622') || myCardNo.startsWith('64')) {
                myCardType = 'Discover';
            } else if (myCardNo.startsWith('36') || myCardNo.startsWith('38')) {
                myCardType = 'DinersClub';
            } else if (myCardNo.startsWith('51') || myCardNo.startsWith('52') || myCardNo.startsWith('53') || myCardNo.startsWith('54') || myCardNo.startsWith('55')) {
                myCardType = 'MasterCard';
            } else if (myCardNo.startsWith('5018') || myCardNo.startsWith('5020') || myCardNo.startsWith('5038') || myCardNo.startsWith('6304') || myCardNo.startsWith('6759') || myCardNo.startsWith('6761') || myCardNo.startsWith('6762') || myCardNo.startsWith('6763')) {
                myCardType = 'Maestro';
            } else if (myCardNo.startsWith('4')) {
                myCardType = 'Visa';
            } else if (myCardNo.startsWith('606') || myCardNo.startsWith('607') || myCardNo.startsWith('608') || myCardNo.startsWith('65') || myCardNo.startsWith('508')) {
                myCardType = 'Rupay';
            } else {
                myCardType = 'Unknown';
            }
            //alert("myCardType="+myCardType+" "+myCardNo.startsWith('65'));
            if (checkCreditCard(myCardNo, myCardType)) {
                document.getElementById('cccardType').value = myCardType;
                document.getElementById('dccardType').value = myCardType;

                document.getElementById('cccardType').value = myCardType;
                document.getElementById('dccardType').value = myCardType;

                document.getElementById('emicardType').value = myCardType;

                var ccmmVal = document.getElementsByName("card-month")[0].value;
                // alert("ccmmVal : "+ccmmVal);
                var ccmm = ccmmVal;
                if (ccmmVal.length === 1 && formName === 'ccForm') {
                    ccmm = "0" + ccmmVal;
                }

                //  alert("ccmm : "+ccmm.length);
                var ccyyyy = document.getElementsByName("card-year")[0].value;

                if (cardFieldName === "card-number" && formName === 'ccForm') {
                    if (formName === 'ccForm' && myCardType !== "Maestro" && ccyyyy === '' || ccyyyy.length !== 4 || ccyyyy.length > 4 || ccyyyy < currentDate.getFullYear()) {
                        disableClick = false;
                        swal("Please Enter Correct Expiry Year.", '', 'error');
                        return false;
                    }

                    if (formName === 'ccForm' && myCardType !== "Maestro" && ccmm <= currentDate.getMonth() && ccyyyy <= currentDate.getFullYear()) {
                        disableClick = false;
                        swal("Please select the correct expiry month.", '', 'error');
                        return false;
                    }

                    if (formName === 'ccForm' && myCardType !== "Maestro" && ccmm > 12 || ccmm.length === 0) {
                        disableClick = false;
                        swal("Please select the correct expiry month.", '', 'error');
                        return false;
                    }
                }
                var ccyy = ccyyyy.toString().substr(-2);  // 2018 --> 18

                if (myCardType === 'Amex') {
                    ccyy = ccyyyy.toString();
                }
                var exCC = ccmm + "/" + ccyy;

                var dcmmVal = document.getElementById("card-month1").value;

                var dcmm = dcmmVal;
                if (dcmmVal.length === 1 && formName === 'dcForm') {
                    dcmm = "0" + dcmmVal;
                }

                var dcyyyy = document.getElementById("card-year1").value;
                var dcyy = dcyyyy.toString().substr(-2);  // 2018 --> 18

                var exDC = dcmm + "/" + dcyy;

                if (cardFieldName === "card-number1" && formName === 'dcForm') {
                    if (formName === 'dcForm' && myCardType !== "Maestro" && dcyyyy === '' || dcyyyy.length !== 4 || dcyyyy.length > 4 || dcyyyy < currentDate.getFullYear()) {
                        disableClick = false;
                        swal("Please Enter Correct Expiry Year.", '', 'error');
                        return false;
                    }

                    if (formName === 'dcForm' && myCardType !== "Maestro" && dcmm <= currentDate.getMonth() && dcyyyy <= currentDate.getFullYear()) {
                        disableClick = false;
                        swal("Please select the correct expiry month.", '', 'error');
                        return false;
                    }

                    if (formName === 'dcForm' && myCardType !== "Maestro" && dcmm > 12 || dcmm.length === 0) {
                        disableClick = false;
                        swal("Please select the correct expiry month.", '', 'error');
                        return false;
                    }

                }

                if (cardFieldName === "card-number") {
                    if ((myCardType === "Maestro") && ((ccmm == null || ccmm.length === 0) || (ccyy == null || ccyy.length === 0))) {
                        disableClick = false;
                        swal("If expiry date and CCV are not available then use following values-\nExpiry : 01/2023 \nCVV : 123", '', 'error');
                        return false;
                    }
                } else if (cardFieldName === "card-number1") {
                    if ((myCardType === "Maestro") && ((dcmm == null || dcmm.length === 0) || (dcyy == null || dcyy.length === 0))) {
                        disableClick = false;
                        swal("If expiry date and CCV are not available then use following values-\nExpiry : 01/2023 \nCVV : 123", '', 'error');
                        return false;
                    }
                }

                if (myCardName === '') {
                    disableClick = false;
                    swal("Please Enter Card Holder's Name.", '', 'error');
                    document.getElementById(cardNameFieldName).focus();
                    return false;
                } else if (formName === 'ccForm' && ccmm === '' && ccmm.length !== 2) {
                    disableClick = false;
                    swal("Please Enter Expiry Month.", '', 'error');
                    document.getElementById("card-month").focus();
                    return false;
                } else if (formName === 'ccForm' && ccyy === '' && ccyy.length !== 2) {
                    disableClick = false;
                    swal("Please Enter Expiry Year.", '', 'error');
                    document.getElementById("card-year").focus();
                    return false;
                } else if (formName === 'dcForm' && dcmm === '' && dcmm.length !== 2) {
                    disableClick = false;
                    swal("Please Enter Expiry Month.", '', 'error');
                    document.getElementById("card-month1").focus();
                    return false;
                } else if (formName === 'dcForm' && dcyy === '' && dcyy.length !== 2) {
                    disableClick = false;
                    swal("Please Enter Expiry Year.", '', 'error');
                    document.getElementById("card-year1").focus();
                    return false;
                } else if (myCvv === '' || myCvv.length < 3) {
                    disableClick = false;
                    swal("Please Enter Valid CVV.", '', 'error');
                    document.getElementById(myCvvFieldName).focus();
                    return false;
                } else {

                    if ((exCC.length === 5 || exCC.length === 7) && formName === 'ccForm') {

                        var encryptedExp = CryptoJS.AES.encrypt(exCC, CryptoJS.enc.Hex.parse("<%=iv%>"),
                            {
                                iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                                mode: CryptoJS.mode.CBC,
                                padding: CryptoJS.pad.Pkcs7
                            });

                        var encryptedExpInHex = encryptedExp.ciphertext.toString(CryptoJS.enc.Hex);

                        document.getElementById("CCexpiry").value = encryptedExpInHex;

                        document.getElementById("card-month").value = '1';
                        document.getElementById("card-year").value = '2021';

                    } else {
                        var encryptedExp = CryptoJS.AES.encrypt(exDC, CryptoJS.enc.Hex.parse("<%=iv%>"),
                            {
                                iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                                mode: CryptoJS.mode.CBC,
                                padding: CryptoJS.pad.Pkcs7
                            });

                        var encryptedExpInHex = encryptedExp.ciphertext.toString(CryptoJS.enc.Hex);

                        document.getElementById("DCExpiry").value = encryptedExpInHex;
                        document.getElementById("card-month1").value = '1';
                        document.getElementById("card-year1").value = '2021';

                    }
                    // bharat card

                    var txnId = $('#txnId').val();
                    // alert(myCardNo+"|"+txnId);
                    var encryptedCard = CryptoJS.AES.encrypt(myCardNo + "|" + txnId, CryptoJS.enc.Hex.parse("<%=iv%>"),
                        {
                            iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                            mode: CryptoJS.mode.CBC,
                            padding: CryptoJS.pad.Pkcs7
                        });

                    var encryptedCardInHex = encryptedCard.ciphertext.toString(CryptoJS.enc.Hex);

                    var encryptedCvv = CryptoJS.AES.encrypt(myCvv, CryptoJS.enc.Hex.parse("<%=iv%>"),
                        {
                            iv: CryptoJS.enc.Hex.parse("<%=iv%>"),
                            mode: CryptoJS.mode.CBC,
                            padding: CryptoJS.pad.Pkcs7
                        });

                    var encryptedCvvInHex = encryptedCvv.ciphertext.toString(CryptoJS.enc.Hex);

                    $('#' + cardFieldName).val(encryptedCardInHex);
                    $('#' + myCvvFieldName).val(encryptedCvvInHex);
                    $('.' + cName).val(myCardName);

                    if (formName === 'dcForm') {
                        $("#dcForm").submit();
                    }
                    return true;
                }
            } else {
                disableClick = false;
                swal(ccErrors[ccErrorNo], '', 'error');
                document.getElementById(cardFieldName).focus();
                return false;
            }
        }
    }


</script>

	<script type="text/javascript">


    $(".bank-list li").click(function () {
        var val = $(this).attr('data-value');

        if ($("#bank-select option[value=" + val + "]").length > 0) {
            $('#bank-select').val(val);
        	//alert($('#bank-select').val(val));

            getSurchargeNB();
        } else {
            $('#bank-select').val('');
            swal("Transaction amount is not as per the slab defined. Please select different bank to continue. ", '', 'error');
        }
    });

    $("#bank-select").change(function () {
    	//alert("Hi");
    
        var end = this.value;
        getSurchargeNB();
    });

    $("#Wallet-bank-select").change(function () {
        var end = this.value;
        $('#bank').val(end);
        getSurchargeWALLET();
    });

</script>

	<%
	log.info("index.jsp ::: PG ID " + request.getAttribute("pgid").toString() + " javascript valiadtion ends");
	%>

	<script>
    $(document).ready(function () {
debugger
    	 ;
        <%log.info("checked paymentmode as per request");

if (TM.getInstrumentId().equalsIgnoreCase("Wallet")) {%>
        document.getElementById("liradio1").style.display = "none";
        document.getElementById("liradio2").style.display = "none";
        document.getElementById("liradio3").style.display = "none";
        document.getElementById("liradio4").style.display = "none";

        document.getElementById("mliradio1").style.display = "none";
        document.getElementById("mliradio2").style.display = "none";
        document.getElementById("mliradio3").style.display = "none";
        document.getElementById("mliradio4").style.display = "none";

        $('#radio5').attr('checked', true);
        $("#radio5").click();

        <%} else if (TM.getInstrumentId().equalsIgnoreCase("UPI")) {%>
        document.getElementById("liradio1").style.display = "none";
        document.getElementById("liradio2").style.display = "none";
        document.getElementById("liradio3").style.display = "none";

        document.getElementById("mliradio1").style.display = "none";
        document.getElementById("mliradio2").style.display = "none";
        document.getElementById("mliradio3").style.display = "none";

        $('#radio4').attr('checked', true);
        $("#radio4").click();

        <%} else if (TM.getInstrumentId().equalsIgnoreCase("NB")) {%>
        //alert("NB");
        document.getElementById("liradio1").style.display = "none";
        document.getElementById("liradio2").style.display = "none";

        document.getElementById("mliradio1").style.display = "none";
        document.getElementById("mliradio2").style.display = "none";

        $('#radio3').attr('checked', true);
        $("#radio3").click();

        <%} else if (TM.getInstrumentId().equalsIgnoreCase("DC")) {%>
        //alert("DC");
        document.getElementById("liradio1").style.display = "none";

        document.getElementById("mliradio1").style.display = "none";

        $('#radio2').attr('checked', true);
        $("#radio2").click();

        <%} else if (TM.getInstrumentId().equalsIgnoreCase("CC")) {%>
        $('#radio1').attr('checked', true);
        $("#radio1").click();

        <%}%>


    });

    function checkVPA() {
        var txnId = $('#txnId').val();
        var instrumentId = "UPI";
        var cardType = "UPI";
        var VPAValue = document.getElementById("VPA").value;
        var checksumValue = document.getElementById("checksum").value;
        var MTrackid = $('#txnId').val();

        $("#payNowUPI").css("pointer-events", "none");

        $.ajax
        ({
            type: 'POST',
            data: {
                txnId: txnId,
                instrumentId: instrumentId,
                cardType: cardType,
                VPA: VPAValue,
                checksum: checksumValue,
                MTrackid: MTrackid
            },
            url: "/pay/UpiCheckServlet",
            success: function (result) {
                console.log(result);
                if (result.match("failure")) {
                    document.getElementById("VPA").value = "";
                    swal("Enter the Valid VPA", '', 'error');
                    $("#payNowUPI").css("pointer-events", "auto");
                } else if (result.match("success")) {
                    document.getElementById("UPIForm").submit();
                }
                /* else if(result.match("null")) //commented bcz merchants with non hdfc upi sp were getting below alert.
                {
                    document.getElementById("VPA").value="";
                   swal("Enter the Valid VPA", '', 'error');

                   // alert("Enter the Valid VPA");
                    $( "#payNowUPI" ).css( "pointer-events", "auto" );

                } */
            },
            error: function (err) {
                document.getElementById("VPA").value = "";
                swal("Error while processing your request. Please try again later", '', 'error');
                $("#payNowUPI").css("pointer-events", "auto");
            }
        });

    }


    function startTimerForBQR() {
        i = 0;
        document.getElementById('timer').innerHTML =
            04 + ":" + 55;


        startTimer();


        function startTimer() {
            var presentTime = document.getElementById('timer').innerHTML;
            var timeArray = presentTime.split(/[:]+/);
            var m = timeArray[0];
            var s = checkSecond((timeArray[1] - 1));
            if (s == 59) {
                m = m - 1
            }

            document.getElementById('timer').innerHTML =
                m + ":" + s;
            timer = setTimeout(startTimer, 1000);
        }

        function checkSecond(sec) {
            if (sec < 10 && sec >= 0) {
                sec = "0" + sec
            }
            ; // add zero in front of numbers < 10
            if (sec < 0) {
                sec = "59"
            }
            ;
            return sec;
        }

        //alert(i);


        checkStatus();


    }

    function clearTimer() {
        //alert("clearTimer");
        i = -1;
    }

    function checkStatus() {
        //check the status

        var auto = setInterval(function () {
            //alert("test"+i);
            //alert("valid :"+(i !== -1));
            if (i !== -1) {

                i++;


                var transactionId = $('#txnId').val();
                //var transactionId ="1001927010100480032";
                $.ajax
                ({

                    type: 'POST',
                    data: {transactionId: transactionId, val_i: i},
                    url: "/pay/ajaxAction?type=getStatusBQRTransaction",

                    success: function (result) {
                        // alert("result "+result);
                        if (result != null && result != "" && result != "N")  /* && i == 30 condition in ajaxAction*/
                        {
                            var obj = $.parseJSON(result);


                            submitme(obj.url, obj.encRespXML, obj.merchantId, obj.pgid);
                        }

                    },
                    error: function (err) {
                        swal("Getting error : " + merchantRefNo, '', 'error');
                    }


                });
            }
        }, 10000);

        // refresh every 5000 milliseconds

    }

    function submitme(url, respData, mid, pgid) {
        document.getElementById("respDataID").value = respData;
        document.getElementById("merchantId").value = mid;
        document.getElementById("pgid").value = pgid;
        document.getElementById("MyForm").action = url;
        document.forms["MyForm"].submit();
    }

    function loadQRData() {
        var transactionId = $('#txnId').val();
        $.ajax
        ({

            type: 'GET',
            data: {transactionId: transactionId},
            url: "/pay/BQRProcessor",

            success: function (result) {
                if (result != null && result != "") {
                    $("#bqrImage").attr("src", result);
                    startTimerForBQR();
                }

            },
            error: function (err) {
                swal("Getting error : " + err, '', 'error');
            }
        });
    }

    function getSurchargeCC() {
        var transactionId = $('#txnId').val();
        var cardType = $('#cardType').val();
        var instrumentId = $('#instrumentId').val();
        var amount = <%=TM.getAmount()%>;
        var merchantId = '<%=TM.getMerchantId()%>';
        var emioption = $('#emioption').val();
        var bank = $('#bank').val();

        $.ajax
        ({

            type: 'GET',
            data: {
                transactionId: transactionId,
                cardType: cardType,
                instrumentId: instrumentId,
                amount: amount,
                merchantId: merchantId,
                emioption: emioption,
                bank: bank
            },
            url: "/pay/SurchargeCalculation",

            success: function (result) {
                if (result != null && result != "") {
                    var obj = $.parseJSON(result);
                    //alert(result);
                    $('#txnFee').text(obj.txnFee);
                    $('#gst').text(obj.gst);
                    $('#totalAmount').text(obj.total);
                    $('#totalAmount1').text(obj.total);

                    
                }

            },
            error: function (err) {
                swal("Getting error : " + err, '', 'error');
            }
        });
    }
    $(document).ready(function () {
        $('#MobileModal').modal({
               backdrop: 'static',
               keyboard: false
        })
       
       });
    function getSurchargeDC() {
        var transactionId = $('#txnId').val();
        var cardType = $('#dccardType').val();
        var instrumentId = "DC";
        var amount = <%=TM.getAmount()%>;
        var merchantId = '<%=TM.getMerchantId()%>';
        var emioption = $('#emioption').val();
        var bank = $('#bank').val();

        $.ajax
        ({

            type: 'GET',
            data: {
                transactionId: transactionId,
                cardType: cardType,
                instrumentId: instrumentId,
                amount: amount,
                merchantId: merchantId,
                emioption: emioption,
                bank: bank
            },
            url: "/pay/SurchargeCalculation",

            success: function (result) {
                if (result != null && result != "") {
                    var obj = $.parseJSON(result);
                    //alert(result);
                    $('#txnFee').text(obj.txnFee);
                    $('#gst').text(obj.gst);
                    $('#totalAmount').text(obj.total);
                    $('#totalAmount1').text(obj.total);

                    $('#totalAmtdrpay02').text(obj.total);
                }

            },
            error: function (err) {
                swal("Getting error : " + err, '', 'error');
            }
        });
    }


    function getSurchargeSCC() {
        var transactionId = $('#txnId').val();
        var cardType = $('#sccardTypeCC').val();
        var instrumentId = "CC";
        var amount = <%=TM.getAmount()%>;
        var merchantId = '<%=TM.getMerchantId()%>';
        var emioption = $('#emioption').val();
        var bank = $('#bank').val();

        $.ajax
        ({

            type: 'GET',
            data: {
                transactionId: transactionId,
                cardType: cardType,
                instrumentId: instrumentId,
                amount: amount,
                merchantId: merchantId,
                emioption: emioption,
                bank: bank
            },
            url: "/pay/SurchargeCalculation",

            success: function (result) {
                if (result != null && result != "") {
                    var obj = $.parseJSON(result);
                    //alert(result);
                    $('#txnFee').text(obj.txnFee);
                    $('#gst').text(obj.gst);
                    $('#totalAmount').text(obj.total);
                    $('#totalAmount1').text(obj.total);

                    $('#totalAmtcrpay01').text(obj.total);
                }

            },
            error: function (err) {
                swal("Getting error : " + err, '', 'error');
            }
        });
    }

    function getSurchargeSDC() {
        var transactionId = $('#txnId').val();
        var cardType = $('#sccardTypeDC').val();
        var instrumentId = "DC";
        var amount = <%=TM.getAmount()%>;
        var merchantId = '<%=TM.getMerchantId()%>';
        var emioption = $('#emioption').val();
        var bank = $('#bank').val();

        $.ajax
        ({

            type: 'GET',
            data: {
                transactionId: transactionId,
                cardType: cardType,
                instrumentId: instrumentId,
                amount: amount,
                merchantId: merchantId,
                emioption: emioption,
                bank: bank
            },
            url: "/pay/SurchargeCalculation",

            success: function (result) {
                if (result != null && result != "") {
                    var obj = $.parseJSON(result);
                    //alert(result);
                    $('#txnFee').text(obj.txnFee);
                    $('#gst').text(obj.gst);
                    $('#totalAmount').text(obj.total);
                    $('#totalAmount1').text(obj.total);

                    $('#totalAmtdrpay01').text(obj.total);
                }

            },
            error: function (err) {
                swal("Getting error : " + err, '', 'error');
            }
        });
    }

    function getSurchargeNB() {
    	
    	
    	element.style.display = 'none';           // Hide

        var transactionId = $('#txnId').val();
        var cardType = $('#cardType').val();
        var instrumentId = "NB";
        var amount = <%=TM.getAmount()%>;
        var merchantId = '<%=TM.getMerchantId()%>';
        var emioption = $('#emioption').val();
        var bank = $('#bank-select').val();

        $.ajax
        ({

            type: 'GET',
            data: {
                transactionId: transactionId,
                cardType: cardType,
                instrumentId: instrumentId,
                amount: amount,
                merchantId: merchantId,
                emioption: emioption,
                bank: bank
            },
            url: "/pay/SurchargeCalculation",

            success: function (result) {
                if (result != null && result != "") {
                    var obj = $.parseJSON(result);
                    //alert(result);
                    $('#txnFee').text(obj.txnFee);
                    $('#gst').text(obj.gst);
                    $('#totalAmount').text(obj.total);
                    $('#totalAmount1').text(obj.total);

                    $('#totalAmtnetpay').text(obj.total);
                }

            },
            error: function (err) {
                swal("Getting error : " + err, '', 'error');
            }
        });

    }

    function getSurchargeUPI() {
    	
    	//alert("Hi")
    	debugger
       // document.getElementById("addSelectClass").style.display = "none";
        //document.getElementById("addclass").style.display = "none";
	   
       document.getElementById("addSelectClass").classList.remove("deskview");
       document.getElementById("addclass").classList.remove("deskview");
       document.getElementById("addSelectClass").classList.add("mobileview");
       document.getElementById("addclass").classList.add("mobileview");
       
       //document.getElementById("backbtn").classList.remove("hideButton");
       //document.getElementById("backbtn").classList.add("deskview");

        
        var transactionId = $('#txnId').val();
        var cardType = "UPI";
        var instrumentId = "UPI";
        var amount = <%=TM.getAmount()%>;
        var merchantId = '<%=TM.getMerchantId()%>';
        var emioption = $('#emioption').val();
        var bank = "UPI";

        $.ajax
        ({

            type: 'GET',
            data: {
                transactionId: transactionId,
                cardType: cardType,
                instrumentId: instrumentId,
                amount: amount,
                merchantId: merchantId,
                emioption: emioption,
                bank: bank
            },
            url: "/pay/SurchargeCalculation",

            success: function (result) {
                if (result != null && result != "") {
                    var obj = $.parseJSON(result);
                    //alert(result);
                    $('#txnFee').text(obj.txnFee);
                    $('#gst').text(obj.gst);
                    $('#totalAmount').text(obj.total);
                    $('#totalAmount1').text(obj.total);

                    $('#totalAmtupi').text(obj.total);
                }

            },
            error: function (err) {
                swal("Getting error : " + err, '', 'error');
            }
        });

    }

    function getSurchargeATOMUPI() {
        var transactionId = $('#txnId').val();
        var cardType = "ATOMUPI";
        var instrumentId = "UPI";
        var amount = <%=TM.getAmount()%>;
        var merchantId = '<%=TM.getMerchantId()%>';
        var emioption = $('#emioption').val();
        var bank = "ATOMUPI";

        $.ajax
        ({

            type: 'GET',
            data: {
                transactionId: transactionId,
                cardType: cardType,
                instrumentId: instrumentId,
                amount: amount,
                merchantId: merchantId,
                emioption: emioption,
                bank: bank
            },
            url: "/pay/SurchargeCalculation",

            success: function (result) {
                if (result != null && result != "") {
                    var obj = $.parseJSON(result);
                    //alert(result);
                    $('#txnFee').text(obj.txnFee);
                    $('#gst').text(obj.gst);
                    $('#totalAmount').text(obj.total);
                    $('#totalAmtupi').text(obj.total);
                }

            },
            error: function (err) {
                swal("Getting error : " + err, '', 'error');
            }
        });

    }

    function getSurchargeWALLET() {
        var transactionId = $('#txnId').val();
        var cardType = "Wallet";
        var instrumentId = "Wallet";
        var amount = <%=TM.getAmount()%>;
        var merchantId = '<%=TM.getMerchantId()%>';
        var emioption = $('#emioption').val();
        var bank = $('#Wallet-bank-select').val();

        $.ajax
        ({

            type: 'GET',
            data: {
                transactionId: transactionId,
                cardType: cardType,
                instrumentId: instrumentId,
                amount: amount,
                merchantId: merchantId,
                emioption: emioption,
                bank: bank
            },
            url: "/pay/SurchargeCalculation",

            success: function (result) {
                if (result != null && result != "") {
                    var obj = $.parseJSON(result);
                    //alert(result);
                    $('#txnFee').text(obj.txnFee);
                    $('#gst').text(obj.gst);
                    $('#totalAmount').text(obj.total);
                    $('#totalAmount1').text(obj.total);

                    $('#totalAmtupi').text(obj.total);
                }

            },
            error: function (err) {
                swal("Getting error : " + err, '', 'error');
            }
        });

    }
    function payMobile() {
    	debugger
    	document.getElementById("paymentgateway").submit();
        document.getElementById("loader").style.display = "block";

    }
    function resetSurcharge() {
    	
    	
        $('#txnFee').text("0.00");
        $('#gst').text("0.00");
        $('#totalAmount').text(document.getElementById('baseAmount').innerHTML);
        $('#totalAmount1').text(document.getElementById('baseAmount').innerHTML);

    }

    $(document).ready(function () {
        // $('input').on("cut copy paste", function (e) {
        //     e.preventDefault();
        // });
    });
    $(document).bind("contextmenu", function (e) {
        return false;
    });

    
    
    
    
    function getQRData1() {
       debugger
     //  var transactionId =txnId
    	var transactionId =$('#txnId').val();
      // alert("transactionId:::::::"+transactionId);
       //console.log("Transaction");
        $.ajax
        ({
            type: 'POST',
            url: "<%=request.getContextPath()%>/ajaxAction?type=getQRDataV",
            data: {transactionId: transactionId},
            success: function (result) {
                if (result != null && result != "") {
//                     $("#bqrImage").attr("src", result);
//                     startTimerForBQR();
                    var obj = $.parseJSON(result);

                	var imgElement = document.getElementById("dynamicImage");

                    // Set the src attribute of the img element to the base64 image data
                    imgElement.src = obj.data;
                    
                    var url=obj.Url;
                   // alert(url);
                    
                    var elements = document.querySelectorAll('.payzapp, .otherApp, .BHIM');
                    var modifiedUri = obj.Url.replace("upi://pay", "");
                    document.getElementById("gpay").setAttribute("href", 'gpay://upi:/pay' + modifiedUri);
                    document.getElementById("phonepe").setAttribute("href", 'phonepe://pay' + modifiedUri);
                    document.getElementById("paytm").setAttribute("href", 'paytmmp://pay' + modifiedUri);
                   // document.getElementById("gpay").setAttribute("href", "gpay://upi:/pay"+modifiedUri);

                 // Set the href attribute for all selected anchor elements
                 elements.forEach(function(element) {
                     element.setAttribute("href", obj.Url);
                 });
                    startTimerForBQR();
                }

            },
            error: function (err) {
                swal("Getting error : " + err, '', 'error');
            }
        });
    }
   /*  function selectRadioButtonByValue(name) {
    	alert("Hi")
    
        //var radio = $("input[name='payment_method'][value='" + name + "']");
    	 var radio =document.getElementById("payment_method");
        
        
            //radio.prop("checked", true);
        
    } */
/*     $("input[name='chk_upi_payment']").click(function () {
    	debugger
    	alert("Hi");
        // Get the data-pay attribute value from the selected radio button
        var dataPayUrl = $(this).attr("data-pay");
alert(dataPayUrl);
        // Set the value of the element with ID "payment_method" to the data-pay URL
        $("#payment_method").val(dataPayUrl);
    }); */
</script>
	<%
	log.info("Index page loaded successfully for index id=" + TM.getId());

	} catch (Exception e) {
	e.printStackTrace();
	}
	%>


</body>
</html>