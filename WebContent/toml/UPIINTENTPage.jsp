<%@ include file="/include.jsp" %>
<%@page import="java.io.*,java.net.*"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.rew.pg.db.DataManager"%>
<%@page import="com.rew.pg.dto.TransactionMaster"%>
<%@page import="com.rew.payment.utility.PGUtils"%>



<html>

    <head>
<% 
    Logger log = LoggerFactory.getLogger("JSPS.indextest.jsp"); 

     HttpSession sessionsa = request.getSession(true); 

     response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
     response.setHeader("Pragma", "no-cache"); 
    response.setHeader("Expires", "0"); 
    response.setDateHeader("Expires", -1);
    response.setDateHeader("Last-Modified", 0); 


   DataManager dm = new DataManager();
   log.info("Valu::::::::: "+request.getParameter("MTrackid").toString());
     log.info("indextest.jsp ::: PG ID " + request.getParameter("MTrackid").toString() + " page started and datamager object created");
    String keyEncKey = dm.getKeyEncKey();
    String iv = "3ad5485e60a4fecde36fa49ff63817dc"; 
   TransactionMaster TM = dm.getTxnMaster(request.getParameter("MTrackid").toString()); 

     log.info("indextest.jsp ::: PG ID " + TM.getId() + " first DBconnection established TransactionMaster object created"); 
     // String checksum = request.getParameter("checksum"); 
     //String checksum = request.getAttribute("checksum").toString();
     log.info("indextest.jsp :: PG ID : " + request.getParameter("MTrackid").toString() + " , checksum : "); 

     if (TM != null) {

     } 
    String Amount = TM.getAmount(); 
    String surcharge = TM.getSurcharge();
    log.info("amount" + Amount); 
     log.info("amount in double " + Double.valueOf(Amount)); 
    log.info("surcharge" + surcharge); 
     log.info("surcharge in double" + Double.valueOf(surcharge)); 
    double total = Double.valueOf(Amount) + Double.valueOf(surcharge);
    log.info("total" + total);
     String subTotal = String.format("%.2f", total);
     log.info("total" + subTotal); 

    
    
	
    	log.info("Kyu Nahi aa raha " + TM.getAmount()); 
    
    
    
    
 %> 


    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>HDFC Bank</title>
    <link href="payfiVAS/css/jquery-ui.css" rel="stylesheet" type="text/css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!--  <link href="payfiVAS/css/responsive.css" rel="stylesheet" type="text/css"> -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="assets/js/jquery-3.5.1.min.js"></script>
    <%--    <script type="text/javascript" src="https://code.jquery.com/jquery-1.6.2.js"></script>--%>
    <script src="assets/js/bootstrap.min.js"></script>
    <script src="newjs/circle.js"></script>
    <script src="newjs/circular-countdown.js"></script>
    <%--    <script type="text/javascript" src="http://ajax.microsoft.com/ajax/jQuery/jquery-1.4.2.min.js"></script>--%>
    <%--    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>--%>
    <script type="text/javascript"
            src="https://cdn.rawgit.com/sygmaa/CircularCountDownJs/master/circular-countdown.min.js"></script>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">


    <!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">   -->

    <%--    <style type="text/css">--%>
    <%--        /* general styling */--%>
    <%--        :root {--%>
    <%--            --smaller: .75;--%>
    <%--        }--%>

    <%--        * {--%>
    <%--            box-sizing: border-box;--%>
    <%--            margin: 0;--%>
    <%--            padding: 0;--%>
    <%--        }--%>

    <%--        html, body {--%>
    <%--            height: 100%;--%>
    <%--            margin: 0;--%>
    <%--        }--%>

    <%--        body {--%>
    <%--            align-items: center;--%>
    <%--            background-color: #ffd54f;--%>
    <%--            display: flex;--%>
    <%--            font-family: -apple-system,--%>
    <%--            BlinkMacSystemFont,--%>
    <%--            "Segoe UI",--%>
    <%--            Roboto,--%>
    <%--            Oxygen-Sans,--%>
    <%--            Ubuntu,--%>
    <%--            Cantarell,--%>
    <%--            "Helvetica Neue",--%>
    <%--            sans-serif;--%>
    <%--        }--%>

    <%--        .container {--%>
    <%--            color: #333;--%>
    <%--            margin: 0 auto;--%>
    <%--            text-align: center;--%>
    <%--            border: 5px #dde0ec;--%>
    <%--            border-radius: 10px;--%>
    <%--        }--%>

    <%--        h1 {--%>
    <%--            font-weight: normal;--%>
    <%--            letter-spacing: .125rem;--%>
    <%--            text-transform: uppercase;--%>
    <%--        }--%>

    <%--        li {--%>
    <%--            display: inline-block;--%>
    <%--            font-size: 1.5em;--%>
    <%--            list-style-type: none;--%>
    <%--            padding: 1em;--%>
    <%--            text-transform: uppercase;--%>
    <%--        }--%>

    <%--        li span {--%>
    <%--            display: block;--%>
    <%--            font-size: 4.5rem;--%>
    <%--        }--%>

    <%--        .emoji {--%>
    <%--            display: none;--%>
    <%--            padding: 1rem;--%>
    <%--        }--%>

    <%--        .emoji span {--%>
    <%--            font-size: 4rem;--%>
    <%--            padding: 0 .5rem;--%>
    <%--        }--%>

    <%--        @media all and (max-width: 768px) {--%>
    <%--            h1 {--%>
    <%--                font-size: calc(1.5rem * var(--smaller));--%>
    <%--            }--%>

    <%--            li {--%>
    <%--                font-size: calc(1.125rem * var(--smaller));--%>
    <%--            }--%>

    <%--            li span {--%>
    <%--                font-size: calc(3.375rem * var(--smaller));--%>
    <%--            }--%>
    <%--        }--%>
    <%--    </style>--%>
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

        input[type=radio]:checked + label > img {
            border: 1px solid #fff;
            box-shadow: 0 0 3px 3px #090;
        }

        /* Stuff after this is only to make things more pretty */
        input[type=radio] + label > img {
            border: 1px dashed #444;
            width: 50px;
            height: 50px;
            transition: 500ms all;
            border-radius: 44px;
        }

        input[type=radio]:checked + label > img {
            trans: rotateZ(-10deg) rotateX(10deg);
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
            height: 42px;
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

        .custom-date {
            width: 28%;
            float: left;
            margin-right: 1%;
            margin-top: 5px;
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

        .custom-year {
            width: 32%;
            float: left;
            margin-right: 2%;
            margin-top: 5px;
        }

        .cust-col3 {
            position: relative;
        }

        .cvv_info {
            position: relative;
            width: 22px;
            float: left;
            margin-left: 3%;
            margin-right: 3%;
        }

        .upiImage {
            width: 14%;
            height: auto;
            padding: 5px;
        }

        .bqrImage {
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

        #deleteicon {
            margin-left: 310px;
            padding-left: 16px;
            color: yellow;
        }

        .upi-content {
            display: none;
        }

        .bqr-content {
            display: none;
        }

        .cancelButton {
            width: 40%;
            margin: 0 auto;
            background: #80808082;
            color: #fff;
            font-size: 15px;
            text-transform: uppercase;
            padding: 18px 0px;
            border-radius: 5px;
            border: 0;
            border-bottom: 8px solid #80808082;
            box-shadow: inset 0 -1px 1px rgba(255, 255, 255, .15);
            transition: transform .2s ease-in-out;
        }

        .timerContent {
            margin-left: 100px;
        }

        body {
            background: #dde0ec;
            color: #333;
            font-family: 'Helvetica_Light', Arial;
            font-size: 13px;
            margin-top: 10px;
        }

        @media screen and (max-width: 480px) {
            .wrapper {
                margin: 0 auto;
                /*width: 100%;*/
                border-radius: 50px;
            }

            .timerContent {
                margin-left: 10px;
            }
        }
    </style>

    <script type="text/javascript">

        var timer2 = "5:00";
        var interval = setInterval(function () {


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
    <%----%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css"
          integrity="sha512-KfkfwYDsLkIlwQp6LFnl8zNdLGxu9YAA1QvwINks4PhcElQSvqcyVLLD9aMhXd13uQjoXtEKNosOWaZqXgel0g=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>

    <link href="assets/css/bootstrap.css" rel="stylesheet"/>
    <link href="assets/css/fonts/font.css" rel="stylesheet"/>
    <link href="assets/css/style.css" rel="stylesheet"/>
    <%--    --%>

    <style>
        .base-timer {
            position: relative;
            width: 300px;
            height: 300px;
        }

        .base-timer__svg {
            transform: scaleX(-1);
        }

        .base-timer__circle {
            fill: none;
            stroke: none;
        }

        .base-timer__path-elapsed {
            stroke-width: 7px;
            stroke: grey;
        }

        .base-timer__path-remaining {
            stroke-width: 7px;
            stroke-linecap: round;
            transform: rotate(90deg);
            transform-origin: center;
            transition: 1s linear all;
            fill-rule: nonzero;
            stroke: currentColor;
        }

        .base-timer__path-remaining.green {
            color: rgb(65, 184, 131);
        }

        .base-timer__path-remaining.blue {
            color: #538ff1;
        }

        .base-timer__path-remaining.orange {
            color: orange;
        }

        .base-timer__path-remaining.red {
            color: red;
        }
        .lading-graph-wrapper.green {
            color: rgb(65, 184, 131);
        }

        .lading-graph-wrapper.blue {
            color: #538ff1;
        }

        .lading-graph-wrapper.orange {
            color: orange;
        }

        .lading-graph-wrapper.red {
            color: red;
        }

        .base-timer__label {
            position: absolute;
            width: 300px;
            height: 300px;
            top: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 48px;
        }
        .overlay{
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            z-index: 10;
            background-color: rgba(0,0,0,0.5); /*dim the background*/
        }
    </style>

    <style>
        .loading-note-wrapper {
            background: #F2F3F4;
            border: 1px solid #E5E5E5;
            box-sizing: border-box;
            border-radius: 11px;
            display: flex;
            align-items: center;
            padding: 6px 18px;
            margin-bottom: 38px;
        }

        .loading-note-wrapper .text {
            color: #5A5A5A;
            font-size: 16px;
            width: 91%;
            line-height: 24px;
        }

        .loading-note-wrapper .text span {
            color: #5A5A5A;
            font-size: 16px;
            font-weight: 700;
        }

        .loading-note-wrapper .icon {
            background-image: url('assets/images/alert-icon.png');
            width: 30px;
            height: 30px;
            margin-right: 20px;
        }

        .upi-input-wrapper {
            border: 1px dashed #CCCCCC;
            border-radius: 11px;
            padding: 20px 30px;
        }

        .upi-input-wrapper .upi-lable-text {
            display: flex;
            justify-content: space-between;
        }

        .upi-input-wrapper .input-wrapper {
            margin: 8px 0 0;
        }

        .upi-input-wrapper .input-text-area {
            width: 70%;
            color: #E5E5E5;
            font-size: 20px;
        }

        .upi-input-wrapper .input-text-area input {
            width: 100%;
            height: 40px;
            line-height: 54px;
            border: 2px solid #E5E5E5;
            border-right: none;
            border-radius: 11px 0 0 11px;
        }

        .upi-input-wrapper .input-text-area-otherApp input {
            width: 100%;
            height: 40px;
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
        .upi-input-wrapper select option {
            text-decoration: underline;
            border: blue;
        }

        .upi-input-wrapper .form-control:focus {
            box-shadow: none;
        }

        .loading-description-text {
            font-size: 16px;
            line-height: 23px;
            color: #000;
            width: 88%;
        }

        .loading-wrapper {
            display: flex;
            margin-top: 20px;
        }

        .loading-wrapper-mobile{
            display:flex;
        }

        .upi-mobile-icon {
            background: url('assets/images/upi-mobile.png')no-repeat;
            width: 12%;
            height: 63px;
        }
        #countdown {
            position: relative;
            margin: auto;
            height: 100px;
            width: 100px;
            text-align: center;
        }

        #countdown-number {
            color: black;
            display: inline-block;
            font-size: x-large;
            margin-top: 50px;
            margin-right: 30px;
        }

        #countdown-number-mobile {
            color: black;
            display: inline-block;
            font-size: large;
            margin-top: 0px;
            margin-right: 28px;
        }



        svg {
            position: absolute;
            top: 0;
            right: 0;
            width: 140px;
            height: 140px;
            transform: rotateY(-180deg) rotateZ(-90deg);
        }

        svg .inner {
            stroke: lightgray;
            fill: none;
            stroke-width: 8px;

        }

        svg .outer {
            stroke-dasharray: 360px;
            stroke-dashoffset: 0px;
            stroke-linecap: round;
            stroke-width: 8px;
            stroke: #538ff1;
            fill: none;
            animation: countdown 300s linear infinite forwards;
        }

        svg .orange {
            stroke-dasharray: 360px;
            stroke-dashoffset: 0px;
            stroke-linecap: round;
            stroke-width: 8px;
            stroke: orange;
            fill: none;
            animation: countdown 300s linear infinite forwards;
        }

        svg .red {
            stroke-dasharray: 360px;
            stroke-dashoffset: 0px;
            stroke-linecap: round;
            stroke-width: 8px;
            stroke: orange;
            fill: none;
            animation: countdown 300s linear infinite forwards;
        }

        @keyframes countdown {
            from {
                stroke-dashoffset: 0px;
            }
            to {
                stroke-dashoffset: 357px;
            }
        }


        .countdownMobile{
            margin-left:20px;
        }

        .normal {
            position: absolute;
            top: 0;
            right: 0;
            width: 140px;
            height: 140px;
            transform: rotateY(-180deg) rotateZ(-90deg);
        }

        .mobile {
            position: absolute;
            top: 0;
            right: 0px;
            width: 92px;
            height: 113px;
            transform: rotateY(-180deg) rotateZ(-90deg);
        }

        svg .inner-mobile {
            stroke: lightgray;
            fill: none;
            stroke-width: 7px;

        }

        svg .outer-mobile {
            stroke-dasharray: 233px;
            stroke-dashoffset: 0px;
            stroke-linecap: round;
            stroke-width: 7px;
            stroke: #538ff1;
            fill: none;
            animation: countdownMobile 300s linear infinite forwards;
        }

        .lading-graph-wrapper {
            position: relative;
            width: fit-content;
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 0px 1px 30px 50px;
        }

        .lading-graph-wrapper .time {
            position: absolute;
            color: #5A5A5A;
            font-size: 24px;
            line-height: 27px;
            text-align: center;
        }

        .lading-graph-wrapper .time span {
            display: block;
            color: #A8A8A8;
            font-size: 18px;
        }

        .lading-graph-wrapper-mobile {
            position: relative;
            width: fit-content;
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 20px 7px 10px;
        }

        .lading-graph-wrapper-mobile .time {
            position: absolute;
            color: #5A5A5A;
            font-size: 24px;
            line-height: 27px;
            text-align: center;
        }

        .lading-graph-wrapper-mobile .time span {
            color: #A8A8A8;
            font-size: 18px;
        }


        .payment-request-text {
            color: #5A5A5A;
            font-size: 18px;
            font-weight: 700;
            width: 62%;
            margin-left: 0px;
            margin-top: 40px;
        }

        @keyframes countdownMobile {
            from {
                stroke-dashoffset: 0px;
            }
            to {
                stroke-dashoffset: 233px;
            }
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
            }
            .upi-lable-text,
            .upi-app-description {
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
            .loading-note-wrapper .text,
            .loading-note-wrapper .text span {
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
            .loader {
                border: 8px solid #F2F3F4;
                border-top: 8px solid #19B600;
                width: 86px;
                height: 86px;
            }
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
            .countdown-number-mobile {
                margin-top: 0px!important;
            }
            .lading-graph-wrapper-mobile {
                margin: 25px 25px 0px !important
            }
        }
        @media screen and (max-width:500px){
            #payzapp{
                margin-left:10px;
            }
            #paytm{
                margin-left:10px;
            }

            .upi-input-wrapper .input-text-area input{
                font-size:20px
            }
        }

        @media screen and (max-width: 500px) {
            .upi-input-wrapper {
                padding: 10px 15px;
                margin: 12px 0 0;
            }

            .container-mobile {
                padding-right: 0!important;
                padding-left: 0!important;
            }
            .countdown-number-mobile {
                margin-top: 0px!important;
            }
            .lading-graph-wrapper-mobile {
                margin: 15px 15px 0px !important
            }
        }
        @media (min-width: 500px) and (max-width: 768px){
            .upi-input-wrapper {
                padding: 23px 15px;
                margin: 12px 0 0;
            }


                #countdown-number-mobile {
                    color: black;
                    display: inline-block;
                    font-size: large;
                    margin-top: 30px;
                    margin-right: 28px;
                }
            }

</style>

</head>
<body>
<div id="preloader" style="display: none;"></div>
<div>
<%--    <header class="header" style="display:none; background: white;padding-top: 10px;padding-bottom: 10px;">--%>
<%--        <div class="wrapper">--%>
<%--            <a href="index.html" class="logo">--%>
<%--                <img src="payfiVAS/images/logo.png" style="display:none;">--%>
<%--            </a>--%>
<%--        </div>--%>

<%--    </header>--%>
    <header>
        <div class="container">
            <div class="row">
                <div class="col-md-6 logo">
                    <a href="#">Payment</a>
                </div>
<%--                <div class="col-md-6 lft_logo">--%>
<%--                    <a href="#">--%>
<%--                        <img height="90" src="/payment/images/transparent.png"></a>--%>
<%--                </div>--%>
            </div>
        </div>
    </header>
    <%--    <section>--%>
    <%--        <div class="wrapper" style="background: white;border-radius: 50px">--%>
    <%--            <div class="spacer15" style="background: #dde0ec;height: 15px;display: none;"></div>--%>
    <%--            <div class="whiteBg">--%>
    <%--                <div class="eqHeight"> &nbsp;</div>--%>
    <%--            </div>--%>
    <%--            <div class="spacer15"></div>--%>
    <%--            <div class="whiteBg">--%>


    <%--                <div class="right" style="padding-left: 10px;">--%>
    <%--                    <img src="images/upi.png" style="height:18px;">--%>

    <%--                    <div class="form-group float-label-control col-md-10 timerContent" hidden style="">--%>

    <%--                        <font face="verdana" size="4px">--%>
    <%--                            Your Request Expires In :--%>
    <%--                        </font> <br>--%>

    <%--                        <div class="timer timerContent" style=""></div>--%>
    <%--                    </div>--%>
    <%--                    <div class="container">--%>
    <%--                        <h3 id="headline">Your Request Expires In :</h3>--%>
    <%--                        <div id="countdown">--%>
    <%--                            <ul>--%>
    <%--                                <li hidden><span id="days" hidden></span>days</li>--%>
    <%--                                <li hidden><span id="hours" hidden></span>Hours</li>--%>
    <%--                                <li><span id="minutes"></span>Minutes</li>--%>
    <%--                                <li><span id="seconds"></span>Seconds</li>--%>
    <%--                            </ul>--%>
    <%--                        </div>--%>
    <%--                    </div>--%>
    <%--                </div>--%>
    <%--                <div class="clear"></div>--%>
    <%--            </div>--%>
    <%--            <div class="spacer20"></div>--%>
    <%--        </div>--%>
    <%--        <form method="post" id="MyForm" name="MyForm" action="">--%>
    <%--            <input type="hidden" id="respDataID" name="respData" value="">--%>
    <%--            <input type="hidden" id="merchantId" name="merchantId" value="">--%>
    <%--            <input type="hidden" id="pgid" name="pgid" value="">--%>
    <%--        </form>--%>

    <%--        <FORM ACTION="" id="MyForm1" name="MyForm1" METHOD='POST'>--%>
    <%--            <input type="hidden" id="txnRefNo" name="txnRefNo" value="">--%>
    <%--            <input type="hidden" id="respData" name="respData" value="">--%>
    <%--        </FORM>--%>

    <%--    </section>--%>

    <!-- SOF BTN -->
    <section class="payment">
                <form method="post" id="MyForm" name="MyForm" action="">
                    <input type="hidden" id="respDataID" name="respData" value="">
                    <input type="hidden" id="merchantId" name="merchantId" value="">
                    <input type="hidden" id="pgid" name="pgid" value="">
                </form>

                <FORM ACTION="" id="MyForm1" name="MyForm1" METHOD='POST'>
                    <input type="hidden" id="txnRefNo" name="txnRefNo" value="">
                    <input type="hidden" id="respData" name="respData" value="">
                </FORM>
        <div class="container">
            <div class="row pymt_main_data">
                <div class="col-md-7 col-lg-6 data_mid_blk">

                    <!-- SOF UPI -->
                    <div class="item credit" id="pupi">
                        <div class="container container-mobile">
<%--                            <h3 id="headline">Your Request Expires In :</h3>--%>
<%--                            <div id="countdown">--%>
<%--                                <ul>--%>
<%--                                    <li hidden><span id="days" hidden></span>days</li>--%>
<%--                                    <li hidden><span id="hours" hidden></span>Hours</li>--%>
<%--                                    <li><span id="minutes"></span>Minutes</li>--%>
<%--                                    <li><span id="seconds"></span>Seconds</li>--%>
<%--                                </ul>--%>
<%--                            </div>--%>
                            <div style="display: none;font-family: sans-serif;height: 100vh;place-items: center;">
                                <div id="app"></div>
                            </div>
                            <div id="vpa-enter" class="right-section-inner vpa-enter-wrapper">

                                <div class="loading-note-wrapper">
                                    <div class="icon"></div>
                                    <div class="text">
                                        <span>Note:</span> Please do not press the back button or leave/refresh the screen until the payment is completed
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
                                                    <circle r="58" cx="70" cy="70" id="base-timer-path-remaining" class="outer"></circle>
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
                                                    <circle r="38" cx="50" cy="50" id="base-timer-path-remaining-mobile" class="outer-mobile"></circle>
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
                        </div>

                    </div>

                    <div class="ftr_btn">
                        <ul class="secure">
                            <li><img src="assets/images/safe.png"></li>
                            <li><i class="fa-regular fa-circle-check"></i> Safe and secure payments</li>
                        </ul>
                        <ul class="fix_btn">
                            <li><span id="time">05:00</span>&nbsp;&nbsp;&nbsp;min</li>
                            <li>
                                <button type="button" class="btn btn_submit">Pay Now</button>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="col-md-12 col-lg-3 data_rht_blk">
                   <h6>Amount payable is <span value="Show Div1" class="buttons" type="button" id="btn1"> + </span>
                    </h6>
                    <h5><%=subTotal%></h5>
                    <div class="pymt_summ" id="addClass">
                        <h4>Payment summary</h4>
                        <div class="pymt_summ_data">
                            <p>Order Number</p>
               <h4 class="orderNo"><%=TM.getTxnId()%> </h4>
                        </div>
                        <div class="pymt_summ_gst">
                            <p>surcharge <span><%=surcharge%></span></p>
                            <p>subTotal <span><%=subTotal%></span></p>
                        </div>
                     
                        <div class="pymt_summ_ttl">
                            <p>Total <span><%=Amount%></span></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- SOF BTN -->
    <footer>
        <p>2022-23 secure.payfi.com All rights reserved</p>
    </footer>
</div>


<script type="text/javascript">

    function submitme(url, respData, mid, pgid) {
        if (mid.startsWith("M")) {
            document.getElementById("respDataID").value = respData;
            document.getElementById("merchantId").value = mid;
            document.getElementById("pgid").value = pgid;
            document.getElementById("MyForm").action = url;
            document.forms["MyForm"].submit();

        } else {
            document.getElementById("respData").value = respData;
            document.getElementById("txnRefNo").value = mid;
            document.getElementById("MyForm1").action = url;
            document.forms["MyForm1"].submit();
        }
    }

    // Verification call in 2 min space new requirement  edit shraddha 11 aug 2020
    function checkVerification() {
        <%
            String merchantRefNo2 =(String)session.getAttribute("merchantRefNo"); // merchant transaction id column name txn_id in transaction_master
            String mTrackId2 = (String)session.getAttribute("MTrackid"); // our transaction id column name id in transaction_master
            String amount2 = (String)session.getAttribute("amt");

        %>
        $.ajax
        ({
            type: 'POST',
            data: {mTrackId: '<%=mTrackId2%>', merchantRefNo: '<%=merchantRefNo2%>', amount: '<%=amount2%>'},
            url: "<%=request.getContextPath()%>/UpiVerification",

            success: function (result) {
                //alert("result "+result);
                if (result != null && result != "" && result != "N")  /* && i == 30 condition in ajaxAction*/
                {
                    var obj = $.parseJSON(result);
                    // alert("Data Find and redirect to merchant URL.."+obj.encRespXML);
                    submitme(obj.url, obj.encRespXML, obj.merchantId, obj.pgid);
                }

            },
            error: function (err) {
                // alert("Getting error : " + err);
            }

        });
    }

    // Verification call in 2 min space new requirement  edit shraddha 11 aug 2020
</script>

<script type="text/javascript">
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
    $(document).ready(function () {
        $("li.side_nav").removeClass("active");
        $('#headUpi').addClass("active");
        $(".item").hide();
        $(".data_mid_blk").show();
        $('#pupi').fadeIn();
        $('.ftr_btn').hide();
        $('.data_lft_blk').hide();


    });
    var i = 0;
    var auto = setInterval(function () {

        i++;


        // Verification call in 2 min space new requirement  edit shraddha 11 aug 2020
        if (i == 24 || i == 12) {
            //alert(i);
            checkVerification();
        }
        // Verification call in 2 min space new requirement  edit shraddha 11 aug 2020

        <%
         String merchantRefNo = (String)session.getAttribute("merchantRefNo"); // merchant transaction id column name txn_id in transaction_master
         String mTrackId = (String)session.getAttribute("MTrackid"); // our transaction id column name id in transaction_master
         String amount = (String)session.getAttribute("amt");

         System.out.println("merchantRefNo ====="+merchantRefNo +"mTrackId========"+mTrackId + "amount==========="+amount );
         %>
        $.ajax
        ({
            type: 'POST',
            data: {mTrackId: '<%=mTrackId%>', merchantRefNo: '<%=merchantRefNo%>', amount: '<%=amount%>', val_i: i},
            url: "<%=request.getContextPath()%>/ajaxAction?type=getStatusUPITransaction",

            success: function (result) {
                //alert("result "+result);
                if (result != null && result != "" && result != "N")  /* && i == 30 condition in ajaxAction*/
                {
                    var obj = $.parseJSON(result);
                    // alert("Data Find and redirect to merchant URL.."+obj.encRespXML);
                    submitme(obj.url, obj.encRespXML, obj.merchantId, obj.pgid);
                }

            },
            error: function (err) {
                alert("Getting error : " + err);
            }


        });

    }, 10000); // refresh every 5000 milliseconds
</script>

<script type="application/javascript">
    const add_minutes = (dt, minutes) => {
        return new Date(dt.getTime() + minutes * 60000);
    }
    /*(function () {
        const second = 1000,
            minute = second * 60,
            hour = minute * 60,
            day = hour * 24;

        //I'm adding this section so I don't have to keep updating this pen every year :-)
        //remove this if you don't need it
        let today = new Date(),
            dd = String(today.getDate()).padStart(2, "0"),
            mm = String(today.getMonth() + 1).padStart(2, "0"),
            yyyy = today.getFullYear(),
            nextYear = yyyy + 1,
            dayMonth = "09/30/",
            birthday = dayMonth + yyyy;

        today = mm + "/" + dd + "/" + yyyy;
        // if (today > birthday) {
        //     birthday = dayMonth + nextYear;
        // }
        //end

        const countDown = add_minutes(new Date(), 5),
            x = setInterval(function () {

                const now = new Date().getTime(),
                    distance = countDown - now;

                document.getElementById("days").innerText = Math.floor(distance / (day)),
                    document.getElementById("hours").innerText = Math.floor((distance % (day)) / (hour)),
                    document.getElementById("minutes").innerText = Math.floor((distance % (hour)) / (minute)),
                    document.getElementById("seconds").innerText = Math.floor((distance % (minute)) / second);

                //do something later when date is reached
                if (distance < 0) {
                    document.getElementById("headline").innerText = "Session Expired..!";
                    document.getElementById("countdown").style.display = "none";
                    document.getElementById("content").style.display = "block";
                    clearInterval(x);
                }
                //seconds
            }, 0)
    }());*/
    console.log("-======--------->");
    console.log("---------->");
    const FULL_DASH_ARRAY = 220;
    const WARNING_THRESHOLD = 60;
    const ALERT_THRESHOLD = 15;

    const COLOR_CODES = {
        info: {
            color: "blue"
        },
        warning: {
            color: "orange",
            threshold: WARNING_THRESHOLD
        },
        alert: {
            color: "red",
            threshold: ALERT_THRESHOLD
        }
    };

    const TIME_LIMIT = 300;
    let timePassed = 0;
    let timeLeft = TIME_LIMIT;
    let timerInterval = null;
    let remainingPathColor = COLOR_CODES.info.color;

    document.getElementById("app").innerHTML = ''+
'<div class="base-timer"> ' +
        '<svg class="base-timer__svg" viewBox="0 0 80 80" xmlns="http://www.w3.org/2000/svg"> ' +
        '<g class="base-timer__circle"> ' +
        '<circle class="base-timer__path-elapsed" cx="40" cy="40" r="35"></circle> ' +
        '<path id="base-timer-path-remaining" stroke-dasharray="220" class="base-timer__path-remaining '+remainingPathColor +'"' +
    'd="M 40, 40 m -35, 0 a 35,35 0 1,0 70,0 a 35,35 0 1,0 -70,0"></path>' +
       ' </g>'+
  '</svg>'+
  '<span id="base-timer-label" class="base-timer__label">'+formatTime(timeLeft)+'</span>'+
'</div>';

    console.log("Before timer start")
    startTimer();
    console.log("after timer start")

    function onTimesUp() {
        clearInterval(timerInterval);
    }

    function startTimer() {
        timerInterval = setInterval(() => {
            timePassed = timePassed += 1;
            timeLeft = TIME_LIMIT - timePassed;
            document.getElementById("base-timer-label").innerHTML = formatTime(
                timeLeft
            );
            setCircleDasharray();
            setRemainingPathColor(timeLeft);

            if (timeLeft === 0) {
                onTimesUp();
            }
        }, 1000);
    }

    function formatTime(time) {
        const minutes = Math.floor(time / 60);
        let seconds = time % 60;

        if (seconds < 10) {
            seconds = '0'+seconds;
        }

        return minutes+":"+seconds;
    }

    function setRemainingPathColor(timeLeft) {
        console.log(timeLeft, "====>");
        const {alert, warning, info} = COLOR_CODES;
        if (timeLeft <= alert.threshold) {
            document
                .getElementById("base-timer-path-remaining")
                .classList.remove('orange');
            document
                .getElementById("base-timer-path-remaining")
                .classList.add('red');
            document
                .getElementById("base-timer-path-remaining-mobile")
                .classList.remove('orange');
            document
                .getElementById("base-timer-path-remaining-mobile")
                .classList.add('red');
        } else if (timeLeft <= warning.threshold) {
            document
                .getElementById("base-timer-path-remaining")
                .classList.remove('outer');
            document
                .getElementById("base-timer-path-remaining")
                .classList.add('orange');
            document
                .getElementById("base-timer-path-remaining-mobile")
                .classList.remove('outer');
            document
                .getElementById("base-timer-path-remaining-mobile")
                .classList.add('orange');
        }
    }

    function calculateTimeFraction() {
        const rawTimeFraction = timeLeft / TIME_LIMIT;
        return rawTimeFraction - (1 / TIME_LIMIT) * (1 - rawTimeFraction);
    }

    function setCircleDasharray() {
        const circleDasharray = (calculateTimeFraction() * FULL_DASH_ARRAY ).toFixed(0)+' 220';
        document
            .getElementById("base-timer-path-remaining")
            .setAttribute("stroke-dasharray", circleDasharray);
    }
</script>

<script>
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

<script type="application/javascript">

</script>
<script type="text/javascript">

    $('.timer').circularCountDown({
        size: 80,
        borderSize: 8,
        /* colorCircle: 'blue',
        background: 'white', */
        colorCircle: 'blue',
        background: 'white',
        fontFamily: 'sans-serif',
        fontColor: '#333333',
        fontSize: 16,
        delayToFadeIn: 0,
        delayToFadeOut: 0,
        reverseLoading: false,
        reverseRotation: false,
        duration: {
            hours: 0,
            minutes: 4,
            seconds: 60
        },
        beforeStart: function () {
        },
        end: function () {
            // do something
        }
    });


</script>



</body>
</html>