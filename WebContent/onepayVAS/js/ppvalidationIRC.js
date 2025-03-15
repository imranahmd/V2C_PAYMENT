try{
var hdfcBinType = ""; 
var enc;
$(document).ready(function() {
	   $('.payment_mode li').click(function() {
	    // alert($(this).attr("class"));
	    //alert($(this).text());
	    var value_li = $(this).text();
	    if(value_li == "HDFC Bank Credit Card")
	    {
	    	hdfcBinType = "ONUS_CREDIT";
	    	document.getElementById("UPIForm").reset();
	    	clearUPIError();
	    }
	    else if(value_li == "HDFC Bank Debit Card")
	    {
	    	hdfcBinType = "ONUS_DEBIT";
	    	document.getElementById("UPIForm").reset();
	    	clearUPIError();
	    }
	    else if (value_li == "Other Bank Credit Card")
	    {
	    	hdfcBinType = "Other_Card";
	    	document.getElementById("UPIForm").reset();
	    	clearUPIError();
	    }
	    else if (value_li == "Other Bank Debit Card")
	    {
	    	hdfcBinType = "Other_Card";
	    	document.getElementById("UPIForm").reset();
	    	clearUPIError();
	    }
	    else if (value_li =+ "UNIFIED PAYMENT INTERFACE (UPI)")
	    {
	    	//hdfcBinType = "UPI";
	    	clearUPIError();
	    }
	    // alert("hdfcBinType "+hdfcBinType);
	   });
	  });


jQuery(document).ready(function()
		{
			/*Main.init();
			Login.init();*/
			$( "#card_no1" ).tooltip();
			// $( "#confPassword" ).tooltip();
			
			//Disable cut copy paste
		    $('body').on('cut copy paste', function (e) {
		    	swal("Copy/Cut Paste option is not Allowed for Card Number.");
		        e.preventDefault();
		    });

		   
		    //Disable mouse right click
		    $("body").on("contextmenu",function(e){
		        return false;
		    });
		    
		    $(':input').on('focus',function(){
		        $(this).attr('autocomplete', 'off');
		    });
		});
		


	$(document).ready(
	    function(){
	        $(".stored-card label").click(function () {
				var id = $(this).attr('id');
				$('.' + id).toggle();
				$(this).parents('.stored-card li').toggleClass("active");
				
	        });
	    });
	    

		$(document).on('keyup', '.onlynumbers', function(event){
		   this.value = this.value.replace(/[^\d]/g, '');
		}); 
		
	
		
		$(document).on('keyup', '.onlychar', function(event){
		   this.value  = this.value.replace(/[^A-z ]/g, ''); 
		}); 
		
		$(document).on('keyup', '.cardNumber', function(event){
		    var getVal = event.target.value.replace(/[^\dA-Z]/g, '').replace(/(.{4})/g, '$1   ');
		    event.target.value = getVal;
		});
		
		function show(param){
		    $(param).fadeToggle();
		}
		
		$('.datepicker').datetimepicker({
		'showTimepicker':false,
		'changeMonth': true,
		'changeYear': true,
		'minDate': '0'
		});
		
		
		
		$('.c-card-content input').on('keypress', function(){
		   if($(this).val()!=""){
		       $(this).removeClass('error');
		   } 
		    else{
		        $(this).addClass('error');
		    }
		});


		/*
		 $('#card_no1').blur(function (event) 
		{
		  //  alert("blur card_no");
			 if($('#card_no1').val().length >= 15)    // space 3 digit
				{	
					var cardNo_val = $('#card_no1').val();
					var cardNumber_val = cardNo_val.replace(/\s+/g, '');
					
					// swal("length 6   val :: "+cardNo_val);
					// alert("hdfcBinType ==> "+hdfcBinType);
					
					
					var cardNumber = cardNumber_val.substring(0, 6);
					//alert("Six digit :: "+cardNumber);

					if ( hdfcBinType =="ONUS_CREDIT" || hdfcBinType == "ONUS_DEBIT" )
					{
						
						$.ajax
						({				
							type: 'POST',
							//url: '<%=request.getContextPath()%>'+'/ajaxAction?type=getONUSBINDetail',
							url:"/ppPayfi/ajaxAction?type=getONUSBINDetail",
							async: false,
							data: {cardNumber : cardNumber , hdfcBinType : hdfcBinType,merchantId:merId},
							success: function(data)
							{											
								if (data == "Y")
								{		
									// swal("proper");				
								}
								else if(data == "N")
								{
									$("#card_no1").val("");	
									$("#card_no1").attr("placeholder", "Card Number");
						
									$('#card-image').html('<img src="Payfi/newjs/other.svg" height="100%">');
									swal("This card doesn't belong to HDFC Bank, please select the correct payment mode.");
									// swal("HDFC BIN Validate Failed "+cardNumber);
								}
								else if(data == "B")
								{
									$("#card_no1").val("");	
									$("#card_no1").attr("placeholder", "Card Number");
						
									$('#card-image').html('<img src="Payfi/newjs/other.svg" height="100%">');
									swal("This card is Blocked");
									// swal("HDFC BIN Validate Failed "+cardNumber);
								}
								else if(data=="MB")
								{
									$("#card_no1").val("");	
									$("#card_no1").attr("placeholder", "Card Number");
									$('#card-image').html('<img src="Payfi/newjs/other.svg" height="100%">');
									swal("This CardBin is not allowed for this merchant");
								}
								
							},
							error: function (err)
							{
								swal("Error in fetching data using  ONUS BIN Validate For HDFC in if block");
						    }
						});
					}
					
					else if(hdfcBinType =="Other_Card")
					{
						
						$.ajax
						({				
							type: 'POST',
							//url: "<%=request.getContextPath()%>/ajaxAction?type=getONUSBINDetailOtherCard",
							url:"/ppPayfi/ajaxAction?type=getONUSBINDetailOtherCard",
							async: false,
							data: {cardNumber : cardNumber , hdfcBinType : hdfcBinType,merchantId:merId},
							success: function(data)
							{											
								if (data == "Y")
								{		
									$("#card_no1").val("");	
									$("#card_no1").attr("placeholder", "Card Number");
									$('#card-image').html('<img src="Payfi/newjs/other.svg" height="100%">');
									swal("This card belongs to HDFC Bank, please select the correct payment mode.");
									// swal("proper");				
								}
								else if(data == "B")
								{
									$("#card_no1").val("");	
									$("#card_no1").attr("placeholder", "Card Number");
									$('#card-image').html('<img src="Payfi/newjs/other.svg" height="100%">');
									swal("Invalid Card NO");
								}
								else if(data=="MB")
								{
									$("#card_no1").val("");	
									$("#card_no1").attr("placeholder", "Card Number");
									$('#card-image').html('<img src="Payfi/newjs/other.svg" height="100%">');
									swal("This CardBin is not allowed for this merchant");
								}
								else if(data == "N")
								{
									//   using other card.. 
								}
								
							},
							error: function (err)
							{
								swal("Error in fetching data using  ONUS BIN Validate For Other Card..in else block");
						    }
						});
						
					}
				}
			 
		        
			  checkCardType();  // Other Details
		    });*/
		function chkCardNumber()
		{
			
			 if($('#card_no1').val().length >= 15)    // space 3 digit
				{	
				 
					var cardNo_val = $('#card_no1').val();
					var cardNumber_val = cardNo_val.replace(/\s+/g, '');
					
					// swal("length 6   val :: "+cardNo_val+"   :: "+cardNumber);
					//alert("hdfcBinType  1==>"+ hdfcBinType);
					
					
					var cardNumber = cardNumber_val.substring(0, 6);
					// alert("Six digit :: "+cardNumber);

					if ( hdfcBinType =="ONUS_CREDIT" || hdfcBinType == "ONUS_DEBIT" )
					{
						//alert(hdfcBinType);
						$.ajax
						({				
							type: 'POST',
							url: "/ppPayfi/ajaxAction?type=getONUSBINDetail",
							data: {cardNumber : cardNumber , hdfcBinType : hdfcBinType,merchantId:merId},
							async: false,
							success: function(data)
							{		
								//alert("data=="+data);
								if (data == "Y")
								{		
									// swal("proper");				
								}
								else if(data == "N")
								{
									$("#card_no1").val("");	
									$("#card_no1").attr("placeholder", "Card Number");
						
									$('#card-image').html('<img src="Payfi/newjs/other.svg" height="100%">');
									swal("This card doesn't belong to HDFC Bank, please select the correct payment mode.");
									// swal("HDFC BIN Validate Failed "+cardNumber);
								}
								else if(data == "B")
								{
									$("#card_no1").val("");	
									$("#card_no1").attr("placeholder", "Card Number");
						
									$('#card-image').html('<img src="Payfi/newjs/other.svg" height="100%">');
									swal("This card is Blocked");
									// swal("HDFC BIN Validate Failed "+cardNumber);
								}
								else if(data=="MB")
								{
									$("#card_no1").val("");	
									$("#card_no1").attr("placeholder", "Card Number");
									$('#card-image').html('<img src="Payfi/newjs/other.svg" height="100%">');
									swal("This CardBin is not allowed for this merchant");
								}
								
							},
							error: function (err)
							{
								swal("Error in fetching data using  ONUS BIN Validate For HDFC"+err);
						    }
						});
					}
					
					else if(hdfcBinType =="Other_Card")
					{
						//alert("data=="+data);
						$.ajax
						({				
							type: 'POST',
							url: "/ppPayfi/ajaxAction?type=getONUSBINDetailOtherCard",
							data: {cardNumber : cardNumber , hdfcBinType : hdfcBinType,merchantId:merId},
							async: false,
							success: function(data)
							{	
								//alert("data=="+data);
								if (data == "Y")
								{		
									$("#card_no1").val("");	
									$("#card_no1").attr("placeholder", "Card Number");
									$('#card-image').html('<img src="Payfi/newjs/other.svg" height="100%">');
									swal("This card belongs to HDFC Bank, please select the correct payment mode.");
									// swal("proper");				
								}
								else if(data == "B")
								{
									$("#card_no1").val("");	
									$("#card_no1").attr("placeholder", "Card Number");
									$('#card-image').html('<img src="Payfi/newjs/other.svg" height="100%">');
									swal("This card is Blocked");
								}
								else if(data=="MB")
								{
									$("#card_no1").val("");	
									$("#card_no1").attr("placeholder", "Card Number");
									$('#card-image').html('<img src="Payfi/newjs/other.svg" height="100%">');
									swal("This CardBin is not allowed for this merchant");
								}
								else if(data == "N")
								{
									//   using other card.. 
								}
								
							},
							error: function (err)
							{
								swal("Error in fetching data using  ONUS BIN Validate For Other Card.."+err);
						    }
						});
						
					}
				}
			 
		        
			  checkCardType();  // Other Details
		}
		// check for card type and display corresponding icon
	    function checkCardType() 
		{    
	        cardNumber1 = $('#card_no1').val();
	        cardNumber = cardNumber1.replace(/\s+/g, '');
	        var cardType = Stripe.card.cardType(cardNumber);
	        
	     // alert("CardType For Logo : "+cardType);   /*  bharat */
	        
	        //console.log(cardType);
	         switch (cardType) {
	            case 'Visa':
	            	
	                $('#card-image').html('<img src="Payfi/newjs/visa.svg" height="100%">');
	                break;

	            case 'MasterCard':
	                $('#card-image').html('<img src="Payfi/newjs/master.svg" height="100%">');
	                break;
	            case 'Discover':
	                $('#card-image').html('<img src="Payfi/newjs/discover.svg" height="100%">');
	               
	                break;

	            case 'American Express':
	                $('#card-image').html('<img src="Payfi/newjs/american.svg" height="100%">');
	                
	                break;
	             case 'Diners Club':
	                $('#card-image').html('<img src="Payfi/newjs/dinner.svg" height="100%">');
	                break;
	              
	             case 'Rupay':
	                 $('#card-image').html('<img src="Payfi/newjs/rupay.svg" height="100%">');
	                 break; 
	                 
	             case 'Maestro':
	                 $('#card-image').html('<img src="Payfi/newjs/maestro.png" height="100%">');
	                 break; 
	              
	             case 'Unknown': 
	                 $('#card-image').html('<img src="Payfi/newjs/other.svg" height="100%">');
	                 break;   
	        } 
	    }
		
		
		
	   
		
	    function ValidateCard(insId) 
		{
	    	
	    	chkCardNumber();
	    	
	    	 $( ".red-btn" ).css( "pointer-events", "none" );
				 $('#payment_mode_drop_down').attr('disabled', true);
				 $('#payment_mode_drop_down2').attr('disabled', true);
				 
				 $( ".payment_dropdown" ).css( "pointer-events", "none" );
				 var cName=$('#name').val();
			var currentDate = new Date();
				 	
		    if($('#card_no1').val()==""){
		        $('#card_no1').focus();
		        $('.c-card-content input').removeClass('error');
		        $('#card_no1').addClass('error');
		        $( ".red-btn" ).css( "pointer-events", "auto" );
		        $('#payment_mode_drop_down').attr('disabled', false);
				 $('#payment_mode_drop_down2').attr('disabled', false);
				 
				 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
		        return false;   
		    }
		    else if($('#name').val()=="")
		    {
		        $('#name').focus();
		        $('.c-card-content input').removeClass('error');
		        $('#name').addClass('error');
		        $( ".red-btn" ).css( "pointer-events", "auto" );
		        $('#payment_mode_drop_down').attr('disabled', false);
				 $('#payment_mode_drop_down2').attr('disabled', false);
				 
				 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
		        return false;   
		    }
		    
			else if($('#capacha').val()=="")
			{
		        $('#capacha').focus();
		        $('.c-card-content input').removeClass('error');
		        $('#capacha').addClass('error');
		        $( ".red-btn" ).css( "pointer-events", "auto" );
		        $('#payment_mode_drop_down').attr('disabled', false);
				 $('#payment_mode_drop_down2').attr('disabled', false);
				 
				 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
		        return false;   
		    }
		    
			//else if($('#capacha').val() != "alchemy")
			else if($('#capacha').val() != document.getElementById("lblCaptcha").innerHTML.trim())
			{
		        $('#capacha').focus();
		        $('.c-card-content input').removeClass('error');
		        $('#capacha').addClass('error');
		        swal("Please entered the correct capacha");
		        $( ".red-btn" ).css( "pointer-events", "auto" );
		        $('#payment_mode_drop_down').attr('disabled', false);
				 $('#payment_mode_drop_down2').attr('disabled', false);
				 
				 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
		        return false;   
		    } 
			
			document.getElementById('instrumentId').value = insId;	
		    myCardNo = document.getElementById("card_no1").value;   
		    
		    
		// alert("myCardNo >>>>> in ValidateCard method >>>>>> "+myCardNo);
		 
		 
		    if(myCardNo.indexOf('34')==0 || myCardNo.indexOf('37')==0)
			  {
			  	myCardType='AmEx';
			  }

			  else if(myCardNo.indexOf('601')==0 || myCardNo.indexOf('622')==0 || myCardNo.indexOf('64')==0)
			  {
			 	myCardType='Discover';
			  }

			  else if(myCardNo.indexOf('36')==0 || myCardNo.indexOf('38')==0 || myCardNo.indexOf('305')==0)
			  {
			  	myCardType='DinersClub';
			  }

			  else if(myCardNo.indexOf('51')==0 || myCardNo.indexOf('52')==0 || myCardNo.indexOf('53')==0 || myCardNo.indexOf('54')==0 || myCardNo.indexOf('55')==0)
			  {
			  	myCardType='MasterCard';
			  }
																
			  else if(myCardNo.indexOf('5018')==0 || myCardNo.indexOf('5020')==0 || myCardNo.indexOf('5038')==0 || myCardNo.indexOf('6304')==0 || myCardNo.indexOf('6759')==0 || myCardNo.indexOf('6761')==0 || myCardNo.indexOf('6762')==0 || myCardNo.indexOf('6763')==0 )
			  {
			    myCardType='Maestro';
			  }
			  else if(myCardNo.indexOf('4')==0)
			  {
			  	myCardType='Visa'; 
			  }
			  else if(myCardNo.indexOf('353')==0 || myCardNo.indexOf('356')==0)
			  {
			  	myCardType='JCB'; 
			  }
			 
			  else if(myCardNo.indexOf('606')==0 || myCardNo.indexOf('607')==0 || myCardNo.indexOf('608')==0 || myCardNo.indexOf('65')==0 ||myCardNo.indexOf('508')==0)
			  {
				myCardType='Rupay';
			  }
			  else
			  {
				    myCardType='Unknow';
			  } 
			  
			  
			  
			  
			  
			  //alert("CardType :: "+myCardType);
			  
			  
			  
			//  alert("currentDate.getMonth() :: "+currentDate.getMonth());
			  if((myCardType != "Maestro"))
			  {
				//  alert("Not Mastro :: Check expiry date and cvv .. ");
				
				 var creditMonth = $('#expMonthSelect').val();				
				 var creditYear= $('#expYearSelect').val();	
				 
				 if($('#expMonthSelect').val()=="" || $('#expYearSelect').val()=="")
				 {
					 if($('#expMonthSelect').val()=="")
						 {
						 $('#expMonthSelect').focus();
						 $('.c-card-content select').removeClass('error'); 
						 $('#expMonthSelect').addClass('error'); 
						 $( ".red-btn" ).css( "pointer-events", "auto" );
						 $('#payment_mode_drop_down').attr('disabled', false);
						 $('#payment_mode_drop_down2').attr('disabled', false);
						 
						 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
						 swal("Please select expiry month");
						 }
					 
					 else
						 {
						 $('#expYearSelect').focus();
						 $('.c-card-content input').removeClass('error');
					     $('#expYearSelect').addClass('error'); 
					     $( ".red-btn" ).css( "pointer-events", "auto" );
					     $('#payment_mode_drop_down').attr('disabled', false);
						 $('#payment_mode_drop_down2').attr('disabled', false);
						 
						 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
					     swal("Please select expiry year ");
						 }  
				        return false;   
				  }
				
				
				 else if($('#expMonthSelect').val() <= currentDate.getMonth() && $('#expYearSelect').val() == currentDate.getFullYear())
					{
					 swal("Please select the correct expiry month. can't select the previous month of the current year ");
					 $( ".red-btn" ).css( "pointer-events", "auto" );
					 $('#payment_mode_drop_down').attr('disabled', false);
					 $('#payment_mode_drop_down2').attr('disabled', false);
					 
					 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
					 return false; 
					}
				 
				  /* if($('#exp_date').val()==""){
				        $('#exp_date').focus();
				        $('.c-card-content input').removeClass('error');
				        $('#exp_date').addClass('error');
				        
				        return false;   
				    } */
				    
				    else if($('#cvv_no1').val()=="")
				    {
				        $('#cvv_no1').focus();
				        $('.c-card-content input').removeClass('error');
				        $('#cvv_no1').addClass('error');
				        $( ".red-btn" ).css( "pointer-events", "auto" );
				        $('#payment_mode_drop_down').attr('disabled', false);
						 $('#payment_mode_drop_down2').attr('disabled', false);
						 
						 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
				       
				        return false;   
				    }
				    
				    else if($('#cvv_no1').val().length < 3)
				    {
				        $('#cvv_no1').focus();
				        $('.c-card-content input').removeClass('error');
				        $('#cvv_no1').addClass('error');
				        swal("CVV Must be 3 Digit");
				        $( ".red-btn" ).css( "pointer-events", "auto" );
				        $('#payment_mode_drop_down').attr('disabled', false);
						 $('#payment_mode_drop_down2').attr('disabled', false);
						 
						 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
				        
				        return false;   
				    }
				  
			 }
			  
			  
			  if (checkCreditCard(myCardNo,myCardType))
			  {
				   //alert(myCardType);
				  document.getElementById('cardType').value = myCardType;
				  
				  var len_cvv = $('#cvv_no1').val().length;
				  
				 /*  if((myCardType == "Maestro") && ( $('#exp_date').val()==""  || len_cvv < 3 )) */
				 if((myCardType == "Maestro") && ( $('#expMonthSelect').val()=="" || $('#expYearSelect').val()=="" || len_cvv < 3 ))
				  {
					 // alert("exp val :: "+$('#exp_date').val());
					 // alert(" myCardType ::: Maestro and exp date and cvv check :: ");
					 
					/*  if($('#exp_date').val()=="")
						 {
						    $('#exp_date').focus();
					        $('.c-card-content input').removeClass('error');
					        $('#exp_date').addClass('error');   
						 } */
						 
						 if($('#expMonthSelect').val()=="")
						 {
						 $('#expMonthSelect').focus();
						 $('.c-card-content select').removeClass('error');
						 $( ".red-btn" ).css( "pointer-events", "auto" );
						 $('#payment_mode_drop_down').attr('disabled', false);
						 $('#payment_mode_drop_down2').attr('disabled', false);
						 
						 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
					    // $('#expMonthSelect').addClass('error'); 
						 }
					 
					 	else if($('#expYearSelect').val()=="")
						 {
						 $('#expYearSelect').focus();
						 $('.c-card-content input').removeClass('error');
						 $( ".red-btn" ).css( "pointer-events", "auto" );
						 $('#payment_mode_drop_down').attr('disabled', false);
						 $('#payment_mode_drop_down2').attr('disabled', false);
						 
						 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
					     // $('#expYearSelect').addClass('error'); 
					     
						 }  
						 
					 else if(len_cvv < 3)
						 {
						    $('#cvv_no1').focus();
					        $('.c-card-content input').removeClass('error');
					        $('#cvv_no1').addClass('error');		
					        $( ".red-btn" ).css( "pointer-events", "auto" );
					        $('#payment_mode_drop_down').attr('disabled', false);
							 $('#payment_mode_drop_down2').attr('disabled', false);
							 
							 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
						 }
				  		swal("If expiry date and CCV are not available then use following values-\nExpiry : 01/2023 \nCVV : 123");
				  		
				  		 return false;
				  }
				 
				
				  
				 /*  var exp = document.getElementById("exp_date").value;
					var strArray = exp.split("/");
					var mm = strArray[0];  
					// var dd = strArray[1];
					var yy = strArray[2].toString().substr(-2);    // 2018  --> 18
					document.getElementById("expiry").value = mm+"/"+yy; */
					
					var expMM = $('#expMonthSelect').val();
					var expYYYY = $('#expYearSelect').val();
					var txnId = $('#txnId').val();
					// alert(" expMM : "+expMM+"  ,  expYYYY : "+expYYYY)
					//alert("txnId ============>" +txnId);
					var expYY = expYYYY.toString().substr(-2);    // 2018  --> 18
					// alert("Expiry Date : expMM : "+expMM+" , expYY : "+expYY);
					
					/* document.getElementById("expiry").value = expMM+"/"+expYY;  */
					var expEnc = expMM+"/"+expYY; 
					// 1
					 //alert("myCardNo  >>>>> validate card=="+myCardNo);
					
			 
					  var encryptedCardInHex=encrypt(myCardNo+"|"+txnId);
		
					 // 2
					 //alert("3 encryptedCardInHex >>> "+encryptedCardInHex);

					 var encryptedExpInHex=encrypt(expEnc);
					
					var encryptedCvvInHex=encrypt($('#cvv_no1').val());
				
					$('#card_no').val(encryptedCardInHex);
					$('#cvv_no').val(encryptedCvvInHex);
					$('#expiry').val(encryptedExpInHex);
					$('#cName').val(cName);
					$('#expMonthSelect').val('00');
					$('#expYearSelect').val('0000');
					
					 return true;	
					
			  } 
			  
			  else {
				   swal(ccErrors[ccErrorNo]);
				 
				  // $('#card_no').focus();    // ajax calling on focus 
				  $('#card_no1').addClass('error');
				  $( ".red-btn" ).css( "pointer-events", "auto" );
				  $('#payment_mode_drop_down').attr('disabled', false);
					 $('#payment_mode_drop_down2').attr('disabled', false);
					 
					 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
				  
			    return false;
			  }
		}

function checkVPA()
{
	var txnId = $('#txnId').val();
 	var instrumentId = "UPI";
  	var cardType = "UPI";
  	var VPAValue = document.getElementById("VPA").value;
  	var checksumValue = document.getElementById("checksum").value;
  	var MTrackid = $('#txnId').val();

	document.getElementById("submitUpi").disabled = true;
	 //document.getElementById("payment_mode_drop_down").disabled = true;
	 $('#payment_mode_drop_down').attr('disabled', true);
	 $('#payment_mode_drop_down2').attr('disabled', true);
	 
	 $( ".payment_dropdown" ).css( "pointer-events", "none" );
	 $( ".red-btn" ).css( "pointer-events", "none" );
	 //$("#payment_mode_drop_down").prop("disabled", true);  
	
	 // $('#payment_mode_drop_down').attr('disabled', true);
	
//	document.getElementById("payment_mode_drop_down").setAttribute('disabled');
	
	$.ajax
 		({				
 			type: 'POST',
 			data: { txnId : txnId , instrumentId : instrumentId , cardType : cardType, VPA : VPAValue,checksum : checksumValue,MTrackid : MTrackid},
 			url: "/ppPayfi/UpiCheckServlet",
 			success: function(result)
 			{
 				//console.log(result);
 				if (result.match("failure"))
				{
 					document.getElementById("VPA").value="";
 					//alert("Enter the Valid VPA");
					document.getElementById("errorMessage").innerHTML = "&#9888; Enter the Valid VPA";
					document.getElementById("errorMessage").style.display = "block";
					document.getElementById("submitUpi").disabled = false;
					$( ".red-btn" ).css( "pointer-events", "auto" );
					  $('#payment_mode_drop_down').attr('disabled', false);
						 $('#payment_mode_drop_down2').attr('disabled', false);
						 
						 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
				}
 				else if(result.match("success"))
 				{
 					document.getElementById("UPIForm").submit();
 				}
				else if(result.match("null"))
 				{
 					document.getElementById("VPA").value="";
 					//alert("Enter the Valid VPA");
					document.getElementById("errorMessage").innerHTML = "&#9888; Enter the Valid VPA";
					document.getElementById("errorMessage").style.display = "block";
					document.getElementById("submitUpi").disabled = false;
					$( ".red-btn" ).css( "pointer-events", "auto" );
					  $('#payment_mode_drop_down').attr('disabled', false);
						 $('#payment_mode_drop_down2').attr('disabled', false);
						 
						 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
 				}
 			},
 			error: function (err)
 			{
 				document.getElementById("VPA").value="";
 				$(".red-btn").attr('disabled', false);
				document.getElementById("errorMessage").innerHTML = "&#9888; Error While Processing your request, Please try again later";
				document.getElementById("errorMessage").style.display = "block";
				$( ".red-btn" ).css( "pointer-events", "auto" );
				  $('#payment_mode_drop_down').attr('disabled', false);
					 $('#payment_mode_drop_down2').attr('disabled', false);
					 
					 $( ".payment_dropdown" ).css( "pointer-events", "auto" );

 		    }
 		});

}

function clearUPIError()
{
	document.getElementById("errorMessage").innerHTML = "";
	document.getElementById("errorMessage").style.display = "none";
	  $('#payment_mode_drop_down').attr('disabled', false);
		 $('#payment_mode_drop_down2').attr('disabled', false);
		 
		 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
}
	

function callTimeOutForbackBotton() {
	window.location.hash = window.location.hash[window.location.hash.length-1];
	setTimeout('callTimeOutForbackBotton()', 10);
}


$('.datepicker').datetimepicker({
	'showTimepicker':false,
	'changeMonth': true,
	'changeYear': true,
	'minDate': '0'
	});
	
$('#card_no1').on('keyup', function() {
	  var foo = $(this).val().split(" ").join(""); 
	  if (foo.length > 0) {
	    foo = foo.match(new RegExp('.{1,4}', 'g')).join("   ");
	  }
	  $(this).val(foo);
	});

setTimeout('callTimeOutForbackBotton()', 10);

/* $('#capacha').on('input', function () {     
  if (this.value.length > 5)         
      this.value = this.value.slice(0,5); 
}); */


$('#payerVpa').on('input', function () {     
  if (this.value.length > 255)         
      this.value = this.value.slice(0,255); 
});

$('#emailId').on('input', function () {     
  if (this.value.length > 35)         
      this.value = this.value.slice(0,35); 
});

$('#card_no1').on('keyup', function () {     
	this.value = this.value.replace(/[^\d ]/g, '');
  if($(this).val()!=""){
     $(this).removeClass('error');
 } 
  else{
      $(this).addClass('error');
  }
});

function changeMe(sel)
{
	if(sel.value==0)
		sel.style.color = "#75757e";
	else
  		sel.style.color = "#000000";              
}
window.history.forward(1);
 

/*   Net Banking Top 6 Bank for select Start */  

$('.radio-group .selectBank').click(function(){
	 
    $(this).parent().find('.selectBank').removeClass('selected');
    $(this).addClass('selected');
    var val = $(this).attr('data-value');
    
   // $('#bank-select').val(val);
   
    if($("#bank-select option[value="+ val +"]").length > 0)
   	 {
   		$('#bank-select').val(val);
   	 }
    else
   	 {
   	   $('#bank-select').val('');
   	   swal("Transaction amount is not as per the slab defined. Please select different bank to continue. ");
   	 }
    
});

function refreshCaptcha()
{
	//alert('test');
	 $.ajax({
	     type:"GET",    
	     url: "captcha.jsp",
	           
	       success: function(data) {
			$("#lblCaptcha").html(data);
			//alert("lblCaptcha"+document.getElementById("lblCaptcha").innerHTML);
			
	       }
	     }); 
}

function encrypt(cardno)
{
	//alert(' 1 test='+cardno);
	
	 $.ajax({
		 async: false,
	     type:"POST",    
	     url: "encryptData.jsp",
	     data: {cardno : cardno},     
	       success: function(data) {
			//alert(" 2 data"+data.trim());
			enc=data.trim();
			//alert("1 enc="+enc);	
	       }
	     }); 
	 //alert("2 enc="+enc);
	 return enc;
};
}catch(err)
{
	alert(err);
}
