<!DOCTYPE html>
<!-- Template Name: Clip-Two - Responsive Admin Template build with Twitter Bootstrap 3.x | Author: ClipTheme -->
<!--[if IE 8]><html class="ie8" lang="en"><![endif]-->
<!--[if IE 9]><html class="ie9" lang="en"><![endif]-->
<!--[if !IE]><!-->

<%
	response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Expires", "0");
	response.setDateHeader("Expires", -1);
	String errorMessage = request.getParameter("errorMessage");
%>

<html lang="en">
	<!--<![endif]-->
	<!-- start: HEAD -->
	<head>
		<title>Payment : Error Page</title>
		<!-- start: META -->
		<!--[if IE]><meta http-equiv='X-UA-Compatible' content="IE=edge,IE=9,IE=8,chrome=1" /><![endif]-->
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta content="" name="description" />
		<meta content="" name="author" />
		<!-- end: META -->
		<!-- start: GOOGLE FONTS -->
		<link href="css/fonts.googleapis.css" rel="stylesheet" type="text/css" />
		<!-- end: GOOGLE FONTS -->
		<!-- start: MAIN CSS -->
		<link rel="stylesheet" href="vendor/bootstrap/css/bootstrap.min.css">
		<link rel="stylesheet" href="vendor/fontawesome/css/font-awesome.min.css">
		<link rel="stylesheet" href="vendor/themify-icons/themify-icons.min.css">
		<link href="vendor/animate.css/animate.min.css" rel="stylesheet" media="screen">
		<link href="vendor/perfect-scrollbar/perfect-scrollbar.min.css" rel="stylesheet" media="screen">
		<link href="vendor/switchery/switchery.min.css" rel="stylesheet" media="screen">
		<!-- end: MAIN CSS -->
		<!-- start: CLIP-TWO CSS -->
		<link rel="stylesheet" href="assets/css/styles.css">
		<link rel="stylesheet" href="assets/css/plugins.css">
		<link rel="stylesheet" href="assets/css/themes/theme-1.css" id="skin_color" />
		<link rel="stylesheet" href="css/style.min.css"/>
		<!-- end: CLIP-TWO CSS -->
		<!-- start: CSS REQUIRED FOR THIS PAGE ONLY -->
         <style type="text/css">
            header .navbar-collapse
                {
                background-color:rgb(221,221,232) !important;
                }
                .navbar .navbar-header
                {
                    height:50px; 
                    line-height:50px;
                }
                 #sidebar
                {
                    top :50px !important;
                }
             	#navbar-heading
            	{
            		height :50px !important;
					background-color:#4981af !important;
            	}
				#navbar_header_1
            	{
              	 	background-color:#367fa9 !important;
			   		border-right: 1px solid transparent !important;
            	}
          		#user-dropdown
          		{
              		padding-top:3px;
              		padding-bottom:3px;  
          		}
	             #dashboard
	             {
	                 margin-top:5px;
	                 margin-bottom:5px;
					 font-weight:500;
					 font-size:24px;
					 color:black;
	             }
				 #fieldset1
				 {
				 padding: 0px; 
				 }
				 fieldset legend
				 {
					 color: #4981af !important;
				 }
	             #navbar-top
	             {
	                 height:50px;
	             }
             
             	footer 
             	{
				  position: absolute;
				  bottom: 0;
				}

				* {
				  width: 100%;
				  text-align: center;
				}
             
				 hr { 
				    display: block;
				    margin-top: 0.5em;
				    margin-bottom: 0.5em;
				    margin-left: auto;
				    margin-right: auto;
				    border-style: inset;
				    border-width: 1px;
				}             
             
             
        </style>
		<!-- end: CSS REQUIRED FOR THIS PAGE ONLY -->
	</head>
	<!-- end: HEAD -->
	
	<body>
		<div id="app">
			<div class="app-content">

					 <div class="navbar-collapse collapse" id="navbar-heading">
   							
   							<!-- <a class="navbar-brand" href="#"> -->
								<h2 class="mainTitle" style="margin:5px 0px; color:white; text-align: center">Payment</h2>
							<!-- </a> -->
	                </div>
					<!-- end: NAVBAR COLLAPSE -->
				
						<section id="page-title" style="padding:50px 5px;">
							<div class="row">
								<div class="col-sm-3">
									
								</div>
                                <div class="col-sm-2">
                              
                                </div>
								<div class="col-sm-2">
									<!-- start: MINI STATS WITH SPARKLINE -->
									<h3 style="margin:15px 10px; color:black; text-align: center">Error Page</h3>
									<!-- end: MINI STATS WITH SPARKLINE -->
								</div>
							</div>
						
						</section>
						
						<div>
							Received some Error in the Request.<br>
							<!-- Request Parameter Values must not contain special characters or words like SCRIPT,ONLOAD etc. --> 
							<%=errorMessage%>
						</div>
					
				
							
			
			<!-- start: FOOTER -->
			<footer>
				<div class="footer-inner">
					<div class="pull-left">
						<hr>
							&copy; <span class="current-year">Payment. All rights reserved</span>
					</div>
					<div class="pull-right">
						<!-- <span class="go-top"><i class="ti-angle-up"></i></span> -->
					</div>
				</div>
			</footer>
			
			
	</body>
</html>
