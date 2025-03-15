try{
var hdfcBinType = ""; 
var emiopt="NA";

var iv="3ad5485e60a4fecde36fa49ff63817dc";

var upiIdValue = null;



$(document).ready(function(){
	 $(".emi-table").hide();
	 $("#ccdiv").hide();
	 $("#ccdiv1").hide();
	 $('.cc_card_submit_btn').hide();
	  $('.cc_card_submit_btn1').hide();
	 $('.offline-content').hide();
	 $("#UPIpayment").hide();
	 $(".upi_submit_btn").hide();
	 $('#myModal').css('display','none'); 
	 $('.showPayementDiv').hide();
	 $('#hr').hide();
});

$(document).ready(function() {
	   $('#bank_id li').click(function() {
		 var value_li = $(this).text();
		if(value_li == 'Other Banks'){
			$(".other_bank_btn").show();
			$("#ccdiv").hide();
		}else{
			$(".other_bank_btn").hide();
			$("#ccdiv").show();
			$("#access_card").hide();
			$("#mobileEmailDiv").hide();
			
		}
		$('.atombankName').val(value_li);
		 $.ajax({
		     type:"POST",    
		     	url: "/payment/ajaxAction?type=getemidetails",
		        data:{bankname:value_li,amt:amt},  
		       success: function(data) {
		    	  // alert(data);
		    	   
		    	   var emidetails=JSON.parse(data.trim());
		    	   //alert(emidetails["3"]["emi"]);
		    	  
				  if (emidetails["3"] != null && emidetails["3"] != '')
				  {
		    	  document.getElementById("3mrate").innerHTML=emidetails["3"]["rate"]+"%";
		    	  document.getElementById("3memi").innerHTML="Rs."+emidetails["3"]["emi"];
		    	  document.getElementById("3mbankcharges").innerHTML="Rs."+emidetails["3"]["bank_charge"];
		    	  document.getElementById("3m-emi").value=emidetails["3"]["bank_id"];
		    	  
		    	  
		    	  
		    	  document.getElementById("6mrate").innerHTML=emidetails["6"]["rate"]+"%";
		    	  document.getElementById("6memi").innerHTML="Rs."+emidetails["6"]["emi"];
		    	  document.getElementById("6mbankcharges").innerHTML="Rs."+emidetails["6"]["bank_charge"];
		    	  document.getElementById("6m-emi").value=emidetails["6"]["bank_id"];
		    	  
		    	  document.getElementById("9mrate").innerHTML=emidetails["9"]["rate"]+"%";
		    	  document.getElementById("9memi").innerHTML="Rs."+emidetails["9"]["emi"];
		    	  document.getElementById("9mbankcharges").innerHTML="Rs."+emidetails["9"]["bank_charge"];
		    	  document.getElementById("9m-emi").value=emidetails["9"]["bank_id"];
		    	  
		    	  document.getElementById("12mrate").innerHTML=emidetails["12"]["rate"]+"%";
		    	  document.getElementById("12memi").innerHTML="Rs."+emidetails["12"]["emi"];
		    	  document.getElementById("12mbankcharges").innerHTML="Rs."+emidetails["12"]["bank_charge"];
		    	  document.getElementById("12m-emi").value=emidetails["12"]["bank_id"];
		    	  	    	  
		    	  $(".emi-table").show();
		    	  $("#ccdiv").hide();
		    	  $('.cc_card_submit_btn').hide();
		    	    $('.cc_card_submit_btn1').hide();
				  }
				  else
				  {
					$(".emi-table").hide();
					$("#atomEMI").show();
				  }
		       },
		       error: function(data) {
		    	   alert("getting error in getemidetails"+data.toString()); 
		    	 
		       }
		     }); 
		 
		 
	   });
	  
	 });


$(document).ready(function() {
	   $('.payment_mode li').click(function() {
	    // alert($(this).attr("class"));
	    //alert($(this).text());
	  
	   $('#isRemember').val('N');
	   
	    	$('#captchaDiv').show();
   				$("#mobileNo").val("");
      			 $("#emailId").val("");
   				$('#mobileEmailEnter').hide();	
   				
	    var value_li = $(this).text();
	    if(value_li == "HDFC Bank Credit Card")
	    {
	    	//alert("1");
	    	hdfcBinType = "ONUS_CREDIT";
	    	document.getElementById("UPIForm").reset();
	    	clearUPIError();
			resetSurcharge();
			document.getElementById('instrumentId').value = 'CC';
	    }
	    else if(value_li == "HDFC Bank Debit Card")
	    {
	    	//alert("2");
	    	hdfcBinType = "ONUS_DEBIT";
	    	document.getElementById("UPIForm").reset();
	    	clearUPIError();
			resetSurcharge();
			document.getElementById('instrumentId').value = 'DC';
	    }
	    else if (value_li == "Other Bank Credit Card")
	    {
	    	hdfcBinType = "Other_Card";
	    	document.getElementById("UPIForm").reset();
	    	clearUPIError();
			resetSurcharge();
			document.getElementById('instrumentId').value = 'CC';
	    }
	    else if (value_li == "Other Bank Debit Card")
	    {
	    	hdfcBinType = "Other_Card";
	    	document.getElementById("UPIForm").reset();
	    	clearUPIError();
			resetSurcharge();
			document.getElementById('instrumentId').value = 'DC';
	    }
		else if (value_li == "Net Banking")
	    {
			resetSurcharge();
			document.getElementById('instrumentId').value = 'NB';
	    }
	    else if (value_li == "WALLET")
	    {
			resetSurcharge();
			document.getElementById('instrumentId').value = 'WALLET';
	    }
		else if (value_li == "OTHER WALLET")
	    {
			resetSurcharge();
			document.getElementById('instrumentId').value = 'WALLET';
	    }
	    else if (value_li == "PAYZAPP WALLET")
	    {
			resetSurcharge();
			document.getElementById('instrumentId').value = 'WALLET';
	    }
	    else if (value_li == "UNIFIED PAYMENT INTERFACE (UPI)")
	    {
	    	//hdfcBinType = "UPI";
			document.getElementById('instrumentId').value = 'UPI';
	    	clearUPIError();
			$('.showPayementDiv').hide();
			$('#upiFirstScreen').show();
	    }
	    else if(value_li == "EMI")
	    {
	    	//alert("1");
	    	hdfcBinType = "ONUS_CREDIT_EMI";
	    	document.getElementById("UPIForm").reset();
	    	clearUPIError();
			resetSurcharge();
	    }
 		else if (value_li == "BHARAT QR")
	    {
			loadQRData();
			resetSurcharge();
	    }
	    else
	    {
	    	//alert("3");
	    	hdfcBinType = "Other_Card";
	    	document.getElementById("UPIForm").reset();
	    	clearUPIError();
	    }
	    // alert("hdfcBinType "+hdfcBinType);
	   });
	  // alert("hdfcBinType"+hdfcBinType);
	  });
function checkradio(emioptionvalue) {
	//alert("hi = "+emioptionvalue);
	//document.forms['pay_info']['emioption'].value = emioptionvalue;	
	 emiopt=emioptionvalue;
	 //alert("emiopt >>>> "+emiopt);
	 
	}
  
  jQuery(document).ready(function(){
	 // alert("emiopt >>>> "+emiopt);
	  document.forms['pay_info']['emioption'].value = 'NA';
	$('input[type="radio"]').click(function(){
	    if ($(this).is(':checked'))
	    {
	      //alert("checked method called  >>>>>>"+$(this).val());
	     document.forms['pay_info']['emioption'].value = $(this).val();	
	     emiopt=$(this).val();
	     //alert("emiopt >>>> "+emiopt);
	     //document.forms['pay_info']['emioption'].value = emiopt;
	     $("#ccdiv").show();
	     $("#access_card").hide();
	$("#mobileEmailDiv").hide();
	   //  $('.cc_card_submit_btn').show();
	        $('.cc_card_submit_btn1').show();
		// add instrument id value here 
		document.getElementById('instrumentId').value = 'CC';
		document.getElementById('bank').value = emiopt;
	    }
	  }); 
	  
	
	/* else{
		alert("NA");
		 document.forms['pay_info']['emioption'].value = 'NA';	
	} */

 });  

jQuery(document).ready(function()
		{
			/*Main.init();
			Login.init();*/
			$( "#card_no" ).tooltip();
			// $( "#confPassword" ).tooltip();
			
			//Disable cut copy paste
		    $('body').on('cut copy paste', function (e) {
		    	swal("Copy/Cut Paste option is not Allowed for Card Number.");
		        e.preventDefault();
		    });
            $('body').on('dragstart drop', function(e){
                e.preventDefault();
                return false;
            });
		    //Disable mouse right click
		    $("body").on("contextmenu",function(e){
		        return false;
		    });
		    
		    $(':input').on('focus',function(){
		        $(this).attr('autocomplete', 'off');
		    });
		});
		


	function ValidateCVV()
	{
		var val_i =document.forms['scForm']['value_of_i'].value;  
		 var len = document.getElementById("card_cvv"+val_i).value.length;
		 var cvvVal = document.getElementById("card_cvv"+val_i).value;
		 // alert("cvvVal : "+cvvVal);
		// var len = $("#card_cvv"+val_i).val().length;
		// alert("Length  :: "+len);
		
	
		if(len < 3) 
		{
			swal("Please enter the cvv must be 3 digit !");
			 return false;
		}
		else
		{
			// alert("document.getElementById(decCardNo).value : "+document.getElementById("decCardNo").value);
			// alert("document.getElementById(SCExpiry).value : "+document.getElementById("SCExpiry").value);
			var myCardNo = document.getElementById("decCardNo").value;
			var expEnc = document.getElementById("SCExpiry").value;
			
			// alert("myCardNo : "+myCardNo+"  , expEnc : "+expEnc);
			/*var encryptedCard = CryptoJS.AES.encrypt(myCardNo,CryptoJS.enc.Hex.parse(iv),
			 		{
		                     iv : CryptoJS.enc.Hex.parse(iv),
		                     mode : CryptoJS.mode.CBC,
		                     padding : CryptoJS.pad.Pkcs7
	               	}); 
			
			 var encryptedCardInHex = encryptedCard.ciphertext.toString(CryptoJS.enc.Hex);*/
			 
			 // 2
			 
			/* var encryptedExp = CryptoJS.AES.encrypt(expEnc,CryptoJS.enc.Hex.parse(iv),
					 		{
				                     iv : CryptoJS.enc.Hex.parse(iv),
				                     mode : CryptoJS.mode.CBC,
				                     padding : CryptoJS.pad.Pkcs7
			               	}); 
					
					 var encryptedExpInHex = encryptedExp.ciphertext.toString(CryptoJS.enc.Hex);*/
					 
			 // 3
			/* var encryptedCvv = CryptoJS.AES.encrypt(cvvVal,CryptoJS.enc.Hex.parse(iv),
				 		{
			                     iv : CryptoJS.enc.Hex.parse(iv),
			                     mode : CryptoJS.mode.CBC,
			                     padding : CryptoJS.pad.Pkcs7
		               	}); 
				
			var encryptedCvvInHex = encryptedCvv.ciphertext.toString(CryptoJS.enc.Hex);*/
			  
			//alert("Enc :: "+encryptedCardInHex);
			// bharat ST
				// alert("CVV encryptedCvvInHex :  "+encryptedCvvInHex);
			$('#decCardNo').val(encryptedCardInHex);
			document.getElementById("card_cvv"+val_i).value = encryptedCvvInHex;
			// document.getElementsByName("cvv_no").value = encryptedCvvInHex;
			
			$('#SCExpiry').val(encryptedExpInHex);
			
			
			document.getElementById("scForm").submit();
			return true;
		}
	
	} 


	$(document).ready(
	    function(){
	        $(".stored-card label").click(function () {
				var id = $(this).attr('id');
				$('.' + id).toggle();
				$(this).parents('.stored-card li').toggleClass("active");
				
	        });
	    });
	    

	// checkBox work as a radio button script
	
	$('input.chkbx').on('change', function() {
		
	$('input.chkbx').not(this).prop('checked', false);  
	
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
		 $('#card_no').blur(function (event) 
		{
		  //  alert("blur card_no");
			 if($('#card_no').val().length >= 15)    // space 3 digit
				{	
					var cardNo_val = $('#card_no').val();
					var cardNumber_val = cardNo_val.replace(/\s+/g, '');
					
					// swal("length 6   val :: "+cardNo_val+"   :: "+cardNumber);
					 //alert("hdfcBinType  1==>"+ hdfcBinType);
					
					
					var cardNumber = cardNumber_val.substring(0, 6);
					// alert("Six digit :: "+cardNumber);

					if ( hdfcBinType =="ONUS_CREDIT" || hdfcBinType == "ONUS_DEBIT" )
					{
						
						$.ajax
						({				
							type: 'POST',
							url: "/payment/ajaxAction?type=getONUSBINDetail",
							async: false,
							data: {cardNumber : cardNumber , hdfcBinType : hdfcBinType,merchantId:merId},
							success: function(data)
							{		
								//alert("data=="+data);
								if (data == "Y")
								{		
									// swal("proper");				
								}
								else if(data == "N")
								{
									$("#card_no").val("");	
									$("#card_no").attr("placeholder", "Card Number");
						
									$('#card-image').html('<img src="payfiVAS/newjs/other.svg" height="100%">');
									swal("This card doesn't belong to HDFC Bank, please select the correct payment mode.");
									// swal("HDFC BIN Validate Failed "+cardNumber);
								}
								else if(data == "B")
								{
									$("#card_no").val("");	
									$("#card_no").attr("placeholder", "Card Number");
						
									$('#card-image').html('<img src="payfiVAS/newjs/other.svg" height="100%">');
									swal("This card is Blocked");
									// swal("HDFC BIN Validate Failed "+cardNumber);
								}
								else if(data=="MB")
								{
									$("#card_no").val("");	
									$("#card_no").attr("placeholder", "Card Number");
									$('#card-image').html('<img src="payfiVAS/newjs/other.svg" height="100%">');
									swal("This CardBin is not allowed for this merchant");
								}
							
							},
							error: function (err)
							{
								swal("Error in fetching data using  ONUS BIN Validate For HDFC");
						    }
						});
					}
					
					else if(hdfcBinType =="Other_Card")
					{
						//alert("data=="+data);
						$.ajax
						({				
							type: 'POST',
							url: "/payment/ajaxAction?type=getONUSBINDetailOtherCard",
							async: false,
							data: {cardNumber : cardNumber , hdfcBinType : hdfcBinType,merchantId:merId},
							success: function(data)
							{	
								//alert("data=="+data);
								if (data == "Y")
								{		
									$("#card_no").val("");	
									$("#card_no").attr("placeholder", "Card Number");
									$('#card-image').html('<img src="payfiVAS/newjs/other.svg" height="100%">');
									swal("This card belongs to HDFC Bank, please select the correct payment mode.");
									// swal("proper");				
								}
								else if(data == "B")
								{
									$("#card_no").val("");	
									$("#card_no").attr("placeholder", "Card Number");
									$('#card-image').html('<img src="payfiVAS/newjs/other.svg" height="100%">');
									swal("This card is Blocked");
								}
								else if(data=="MB")
								{
									$("#card_no").val("");	
									$("#card_no").attr("placeholder", "Card Number");
									$('#card-image').html('<img src="payfiVAS/newjs/other.svg" height="100%">');
									swal("This CardBin is not allowed for this merchant");
								}
								else if(data == "N")
								{
									//   using other card.. 
								}
							
							},
							error: function (err)
							{
								swal("Error in fetching data using  ONUS BIN Validate For Other Card..");
						    }
						});
						
					}
				}
			 
		        
			  checkCardType();  // Other Details
		    });
		*/
		function chkCardNumber()
		{
			 if($('#card_no').val().length >= 15)    // space 3 digit
				{	
					var cardNo_val = $('#card_no').val();
					var cardNumber_val = cardNo_val.replace(/\s+/g, '');
					
					// swal("length 6   val :: "+cardNo_val+"   :: "+cardNumber);
					// alert("hdfcBinType  1==>"+ hdfcBinType);
					
					
					var cardNumber = cardNumber_val.substring(0, 6);
					// alert("Six digit :: "+cardNumber);

					if ( hdfcBinType =="ONUS_CREDIT" || hdfcBinType == "ONUS_DEBIT" || hdfcBinType == "ONUS_CREDIT_EMI")
					{
						
						$.ajax
						({				
							type: 'POST',
							url: "/payment/ajaxAction?type=getONUSBINDetail",
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
									$("#card_no").val("");	
									$("#card_no").attr("placeholder", "Card Number");
						
									$('#card-image').html('<img src="payfiVAS/newjs/other.svg" height="100%">');
									swal("This card doesn't belong to HDFC Bank, please select the correct payment mode.");
									// swal("HDFC BIN Validate Failed "+cardNumber);
								}
								else if(data == "B")
								{
									$("#card_no").val("");	
									$("#card_no").attr("placeholder", "Card Number");
						
									$('#card-image').html('<img src="payfiVAS/newjs/other.svg" height="100%">');
									swal("This card is Blocked");
									// swal("HDFC BIN Validate Failed "+cardNumber);
								}
								else if(data=="MB")
								{
									$("#card_no").val("");	
									$("#card_no").attr("placeholder", "Card Number");
									$('#card-image').html('<img src="payfiVAS/newjs/other.svg" height="100%">');
									swal("This CardBin is not allowed for this merchant");
								}
								else if(data=="IDB")
								{
									$("#card_no").val("");	
									$("#card_no").attr("placeholder", "Card Number");
									$('#card-image').html('<img src="payfiVAS/newjs/other.svg" height="100%">');
									swal("This CardBin is not allowed for this payment mode");
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
							url: "/payment/ajaxAction?type=getONUSBINDetailOtherCard",
							data: {cardNumber : cardNumber , hdfcBinType : hdfcBinType,merchantId:merId},
							async: false,
							success: function(data)
							{	
								//alert("data=="+data);
								if (data == "Y")
								{		
									$("#card_no").val("");	
									$("#card_no").attr("placeholder", "Card Number");
									$('#card-image').html('<img src="payfiVAS/newjs/other.svg" height="100%">');
									swal("This card belongs to HDFC Bank, please select the correct payment mode.");
									// swal("proper");				
								}
								else if(data == "B")
								{
									$("#card_no").val("");	
									$("#card_no").attr("placeholder", "Card Number");
									$('#card-image').html('<img src="payfiVAS/newjs/other.svg" height="100%">');
									swal("This card is Blocked");
								}
								else if(data=="MB")
								{
									$("#card_no").val("");	
									$("#card_no").attr("placeholder", "Card Number");
									$('#card-image').html('<img src="payfiVAS/newjs/other.svg" height="100%">');
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
	        cardNumber1 = $('#card_no').val();
	        cardNumber = cardNumber1.replace(/\s+/g, '');
	        var cardType = Stripe.card.cardType(cardNumber);
	        
			document.getElementById('cardType').value = cardType;
			if (cardNumber !== '')
			{
				getSurcharge();
			}
			
	     // alert("CardType For Logo : "+cardType);   /*  bharat */
	        
	        //console.log(cardType);
	         switch (cardType) {
	            case 'Visa':
	            	
	                $('#card-image').html('<img src="payfiVAS/newjs/visa.svg" height="100%">');
	                break;

	            case 'MasterCard':
	                $('#card-image').html('<img src="payfiVAS/newjs/master.svg" height="100%">');
	                break;
	            case 'Discover':
	                $('#card-image').html('<img src="payfiVAS/newjs/discover.svg" height="100%">');
	               
	                break;

	            case 'American Express':
	                $('#card-image').html('<img src="payfiVAS/newjs/american.svg" height="100%">');
	                
	                break;
	             case 'Diners Club':
	                $('#card-image').html('<img src="payfiVAS/newjs/dinner.svg" height="100%">');
	                break;
	              
	             case 'Rupay':
	                 $('#card-image').html('<img src="payfiVAS/newjs/rupay.svg" height="100%">');
	                 break; 
	                 
	             case 'Maestro':
	                 $('#card-image').html('<img src="payfiVAS/newjs/maestro.png" height="100%">');
	                 break; 
	              
	             case 'Unknown': 
	                 $('#card-image').html('<img src="payfiVAS/newjs/other.svg" height="100%">');
	                 break;   
	        } 

	
	    }
		
		
		
	    $("#isCCRemember").click(function ()
	   			{
	   				$(this).prop("checked") ? $(this).val("Y") : $(this).val("N");
	   			});	
		
	
	function ValidateCard(insId) 
	{
		chkCardNumber();
		 $( ".red-btn" ).css( "pointer-events", "none" );
		 $('#payment_mode_drop_down').attr('disabled', true);
		 $('#payment_mode_drop_down2').attr('disabled', true);
		 
		 $( ".payment_dropdown" ).css( "pointer-events", "none" );
		 var cName=$('#name').val();
	var currentDate = new Date();
		 	
    if($('#card_no').val()==""){
        $('#card_no').focus();
        $('.c-card-content input').removeClass('error');
        $('#card_no').addClass('error');
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
    myCardNo = document.getElementById("card_no").value;     
 // alert("myCardNo >>>>> in ValidateCard method >>>>>> "+myCardNo);
	  if(myCardNo.startsWith('34') || myCardNo.startsWith('37'))
	  {
	  	myCardType='Amex';
	  }

	  else if(myCardNo.startsWith('6011') || myCardNo.startsWith('622') || myCardNo.startsWith('64'))
	  {
	 	myCardType='Discover';
	  }

	  else if(myCardNo.startsWith('36') || myCardNo.startsWith('38') || myCardNo.startsWith('305'))
	  {
	  	myCardType='DinersClub';
	  }

	  else if(myCardNo.startsWith('51') || myCardNo.startsWith('52') || myCardNo.startsWith('53') || myCardNo.startsWith('54') || myCardNo.startsWith('55') || myCardNo.startsWith('222'))
	  {
	  	myCardType='MasterCard';
	  }
														
	  else if(myCardNo.startsWith('5018') || myCardNo.startsWith('5020') || myCardNo.startsWith('5038') || myCardNo.startsWith('6304') || myCardNo.startsWith('6759') || myCardNo.startsWith('6761') || myCardNo.startsWith('6762') || myCardNo.startsWith('6763') )
	  {
	    myCardType='Maestro';
	  }
	  else if(myCardNo.startsWith('4'))
	  {
	  	myCardType='Visa'; 
	  }
	  else if(myCardNo.startsWith('353') || myCardNo.startsWith('356'))
	  {
	  	myCardType='JCB'; 
	  }
	 
	  else if(myCardNo.startsWith('606') || myCardNo.startsWith('607') || myCardNo.startsWith('608') || myCardNo.startsWith('65') || myCardNo.startsWith('508') || myCardNo.startsWith('817'))
	  {
		myCardType='Rupay';
	  }
	  else
	  {
		    myCardType='Unknown';
	  }
	  
	  
	  
	  
	 // alert("CardType :: "+myCardType);
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
		    
		    else if($('#cvv_no').val()=="")
		    {
		        $('#cvv_no').focus();
		        $('.c-card-content input').removeClass('error');
		        $('#cvv_no').addClass('error');
		        $( ".red-btn" ).css( "pointer-events", "auto" );
		        $('#payment_mode_drop_down').attr('disabled', false);
				 $('#payment_mode_drop_down2').attr('disabled', false);
				 
				 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
		        return false;   
		    }
		    
		    else if($('#cvv_no').val().length < 3)
		    {
		        $('#cvv_no').focus();
		        $('.c-card-content input').removeClass('error');
		        $('#cvv_no').addClass('error');
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
		  //  alert(myCardType);
		  document.getElementById('cardType').value = myCardType;
		  
		  var len_cvv = $('#cvv_no').val().length;
		  
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
			    // $('#expMonthSelect').addClass('error'); 
				 $( ".red-btn" ).css( "pointer-events", "auto" );
				 $('#payment_mode_drop_down').attr('disabled', false);
				 $('#payment_mode_drop_down2').attr('disabled', false);
				 
				 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
				 }
			 
			 	else if($('#expYearSelect').val()=="")
				 {
				 $('#expYearSelect').focus();
				 $('.c-card-content input').removeClass('error');
			     // $('#expYearSelect').addClass('error'); 
				 $( ".red-btn" ).css( "pointer-events", "auto" );
			     
				 }  
				 
			 else if(len_cvv < 3)
				 {
				    $('#cvv_no').focus();
			        $('.c-card-content input').removeClass('error');
			        $('#cvv_no').addClass('error');
			        $( ".red-btn" ).css( "pointer-events", "auto" );
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
			
			if (myCardType == 'Amex')
			{
				expYY = expYYYY.toString();
			}
			
			/* document.getElementById("expiry").value = expMM+"/"+expYY;  */
			var expEnc = expMM+"/"+expYY; 
			// 1
			 //alert("myCardNo  >>>>> validate card=="+myCardNo);
			/*var encryptedCard = CryptoJS.AES.encrypt(myCardNo+"|"+txnId,CryptoJS.enc.Hex.parse(iv),
			 		{
		                     iv : CryptoJS.enc.Hex.parse(iv),
		                     mode : CryptoJS.mode.CBC,
		                     padding : CryptoJS.pad.Pkcs7
	               	}); 
			
			 var encryptedCardInHex = encryptedCard.ciphertext.toString(CryptoJS.enc.Hex);*/
			 
			var encryptedCardInHex=encrypt(myCardNo+"|"+txnId);
	
			 // 2
			 // alert("encryptedCardInHex >>> "+encryptedCardInHex);
			 /*var encryptedExp = CryptoJS.AES.encrypt(expEnc,CryptoJS.enc.Hex.parse(iv),
					 		{
				                     iv : CryptoJS.enc.Hex.parse(iv),
				                     mode : CryptoJS.mode.CBC,
				                     padding : CryptoJS.pad.Pkcs7
			               	}); 
					
					 var encryptedExpInHex = encryptedExp.ciphertext.toString(CryptoJS.enc.Hex);*/
				
			var encryptedExpInHex=encrypt(expEnc);
			
			
			 // 3
			/* var encryptedCvv = CryptoJS.AES.encrypt($('#cvv_no').val(),CryptoJS.enc.Hex.parse(iv),
				 		{
			                     iv : CryptoJS.enc.Hex.parse(iv),
			                     mode : CryptoJS.mode.CBC,
			                     padding : CryptoJS.pad.Pkcs7
		               	}); 
				
			var encryptedCvvInHex = encryptedCvv.ciphertext.toString(CryptoJS.enc.Hex);*/
			
			var encryptedCvvInHex=encrypt($('#cvv_no').val());
			
			
			  
			//alert("Enc :: "+encryptedCardInHex);
			
			$('#card_no').val(encryptedCardInHex);
			$('#cvv_no').val(encryptedCvvInHex);
			$('#expiry').val(encryptedExpInHex);
			$('#cName').val(cName);
			$('#expMonthSelect').val('00');
			$('#expYearSelect').val('0000');
			
			 // document.getElementById("expiry").value = encryptedExpInHex;
			
			//$('.red-btn').attr('disabled', true);
			 return true;	
	  } 
	  
	  else {
		   swal(ccErrors[ccErrorNo]);
		 
		  // $('#card_no').focus();    // ajax calling on focus 
		  $('#card_no').addClass('error');
		  $( ".red-btn" ).css( "pointer-events", "auto" );
		  $('#payment_mode_drop_down').attr('disabled', false);
			 $('#payment_mode_drop_down2').attr('disabled', false);
			 
			 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
	    return false;
	  }
}
	
	function ValidateCardSaveCard(insId) 
	{
	
		chkCardNumber();
		 $( ".red-btn" ).css( "pointer-events", "none" );
		 $('#payment_mode_drop_down').attr('disabled', true);
		 $('#payment_mode_drop_down2').attr('disabled', true);
		 
		 $( ".payment_dropdown" ).css( "pointer-events", "none" );
		 var cName=$('#name').val();
	var currentDate = new Date();
		 	
    if($('#card_no').val()==""){
        $('#card_no').focus();
        $('.c-card-content input').removeClass('error');
        $('#card_no').addClass('error');
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
    
	else if($('#capacha').val()=="" && $('#isRemember').val()!='Y')
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
	else if($('#capacha').val() != document.getElementById("lblCaptcha").innerHTML.trim() && $('#isRemember').val()!='Y')
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
    	
	else if($('#capacha').val()=="" && document.getElementById("otpDiv").style.display == 'block')
	{
        $('#capacha').focus();
        $('.c-card-content input').removeClass('error');
        $('#capacha').addClass('error');
        $( ".red-btn" ).css( "pointer-events", "auto" );
        $('#payment_mode_drop_down').attr('disabled', false);
		 $('#payment_mode_drop_down2').attr('disabled', false);
		 
		 $(".payment_dropdown" ).css( "pointer-events", "auto" );
        return false;   
    }
    
	//else if($('#capacha').val() != "alchemy")
	else if($('#capacha').val() != document.getElementById("lblCaptcha").innerHTML.trim() && document.getElementById("otpDiv").style.display == 'block')
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
	else if( $('#isRemember').val()=='Y' && document.getElementById("otpDiv").style.display == 'none'){
		
		if($('#mobileNo').val()!="" && $('#emailId').val()!="" ){
		var cmobileNo= 	$('#mobileNo').val();
		var cemailId= 	$('#emailId').val();
		var txnId= 	$('#txnId').val();
		var merchantName= 	$('#merchantName').val();
	 var regexemail=/^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	 var regxmobile = /^[6-9]\d{9}$/;
	 var custMail= $('#emailId').val();
	 var custMobile= $('#mobileNo').val();
	 
	 if(!regexemail.test(custMail)) {

	 $('#emailId').focus();
	
	
        $('.c-card-content input').removeClass('error');
    
           $('#emailId').addClass('error');
        $( ".red-btn" ).css( "pointer-events", "auto" );
        $('#payment_mode_drop_down').attr('disabled', false);
		 $('#payment_mode_drop_down2').attr('disabled', false);
		 
		 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
       return false;
        
	 } if(!regxmobile.test(custMobile)) {
		
	 $('#mobileNo').focus();
	
        $('.c-card-content input').removeClass('error');
        $('#mobileNo').addClass('error');
         
        $( ".red-btn" ).css( "pointer-events", "auto" );
        $('#payment_mode_drop_down').attr('disabled', false);
		 $('#payment_mode_drop_down2').attr('disabled', false);
		 
		 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
       return false;
        
	 } 
	 	 
			//$("#otpDiv").show();
			document.getElementById("otpDiv").style.display = 'block';
			$("#mobileSpan").text("+91 "+cmobileNo);
			$("#captchaDiv").show();
			 	$('#otp').focus();
			 	$("#mobileEmailDiv").hide();
			 	
			 	sendOtp(cmobileNo, cemailId, txnId, merchantName);
			 	resendNowTimer();
			    $(".red-btn" ).css( "pointer-events", "auto" );
			    $('#payment_mode_drop_down').attr('disabled', false);
		 $('#payment_mode_drop_down2').attr('disabled', false);
		 
		 $(".payment_dropdown" ).css( "pointer-events", "none" );
		  return false;

   }else if($('#mobileNo').val()=="" || $('#emailId').val()==""){
	  $('#emailId').focus();
	 $('#mobileNo').focus();
	
        $('.c-card-content input').removeClass('error');
        $('#mobileNo').addClass('error');
           $('#emailId').addClass('error');
        $( ".red-btn" ).css( "pointer-events", "auto" );
        $('#payment_mode_drop_down').attr('disabled', false);
		 $('#payment_mode_drop_down2').attr('disabled', false);
		 
		 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
       return false; 
}
		
}	else if(document.getElementById("otpDiv").style.display == 'block'){
	

		var cmobileNo= 	$('#mobileNo').val();
		var cemailId= 	$('#emailId').val();
		var otp=  $('#otp').val();
		var txnId= 	$('#txnId').val(); 
		
	
		if($('#otp').val()==""){
			 $('#otp').focus();
	//  $('#emailId').focus();
        $('.c-card-content input').removeClass('error');
        $('#otp').addClass('error');
        
        $( ".red-btn" ).css( "pointer-events", "auto" );
        $('#payment_mode_drop_down').attr('disabled', false);
		 $('#payment_mode_drop_down2').attr('disabled', false);
		 
		 $(".payment_dropdown" ).css( "pointer-events", "auto" );
        return false;
			 	
		}else if($('#otp').val()!=""){ 
		
			var flag = 0;
		
			$.ajax
    		({				
    			type: 'POST',
    			data: { mobileNo : cmobileNo, otp: otp,  emailId : cemailId, txnId : txnId},
    			url: "/payment/ajaxAction?type=validateOtp",
    			async:false,
    			success: function(data)
    			{
				   $(".red-btn").css( "pointer-events", "auto" );
    			if(data=='OTP expired')
	 				{
				$('#otp').val("");
			 	 flag= 1;
	 			swal("OTP has been expired. Please resend the OTP.");
				
				}else if(data=='OTP not verified successfully'){
					$('#otp').val("");
				 	 flag= 1;
						swal("OTP not verified successfully. Please enter correct OTP.");
				
				}else{
					document.getElementById("otpDiv").style.display == 'none';
				}	
    			
    			},
    			error: function (err)
    			{
    				alert("Error in otp validation "+err);
    		    }
    			
    		});
    				
  if(flag==1){
return false;	
}
}
}
   
	document.getElementById('instrumentId').value = insId;	
    myCardNo = document.getElementById("card_no").value;     
//  alert("myCardNo >>>>> in ValidateCard method >>>>>> "+myCardNo);
	  if(myCardNo.startsWith('34') || myCardNo.startsWith('37'))
	  {
	  	myCardType='Amex';
	  }

	  else if(myCardNo.startsWith('6011') || myCardNo.startsWith('622') || myCardNo.startsWith('64'))
	  {
	 	myCardType='Discover';
	  }

	  else if(myCardNo.startsWith('36') || myCardNo.startsWith('38') || myCardNo.startsWith('305'))
	  {
	  	myCardType='DinersClub';
	  }

	  else if(myCardNo.startsWith('51') || myCardNo.startsWith('52') || myCardNo.startsWith('53') || myCardNo.startsWith('54') || myCardNo.startsWith('55') || myCardNo.startsWith('222'))
	  {
	  	myCardType='MasterCard';
	  }
														
	  else if(myCardNo.startsWith('5018') || myCardNo.startsWith('5020') || myCardNo.startsWith('5038') || myCardNo.startsWith('6304') || myCardNo.startsWith('6759') || myCardNo.startsWith('6761') || myCardNo.startsWith('6762') || myCardNo.startsWith('6763') )
	  {
	    myCardType='Maestro';
	  }
	  else if(myCardNo.startsWith('4'))
	  {
	  	myCardType='Visa'; 
	  }
	  else if(myCardNo.startsWith('353') || myCardNo.startsWith('356'))
	  {
	  	myCardType='JCB'; 
	  }
	 
	  else if(myCardNo.startsWith('606') || myCardNo.startsWith('607') || myCardNo.startsWith('608') || myCardNo.startsWith('65') || myCardNo.startsWith('508') || myCardNo.startsWith('817'))
	  {
		myCardType='Rupay';
	  }
	  else
	  {
		    myCardType='Unknown';
	  }
	  
	  
	  
	  
	//  alert("CardType :: "+myCardType);
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
		    
		    else if($('#cvv_no').val()=="")
		    {
		        $('#cvv_no').focus();
		        $('.c-card-content input').removeClass('error');
		        $('#cvv_no').addClass('error');
		        $( ".red-btn" ).css( "pointer-events", "auto" );
		        $('#payment_mode_drop_down').attr('disabled', false);
				 $('#payment_mode_drop_down2').attr('disabled', false);
				 
				 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
		        return false;   
		    }
		    
		    else if($('#cvv_no').val().length < 3)
		    {
		        $('#cvv_no').focus();
		        $('.c-card-content input').removeClass('error');
		        $('#cvv_no').addClass('error');
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
		  //  alert(myCardType);
		  document.getElementById('cardType').value = myCardType;
		  
		  var len_cvv = $('#cvv_no').val().length;
		  
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
			    // $('#expMonthSelect').addClass('error'); 
				 $( ".red-btn" ).css( "pointer-events", "auto" );
				 $('#payment_mode_drop_down').attr('disabled', false);
				 $('#payment_mode_drop_down2').attr('disabled', false);
				 
				 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
				 }
			 
			 	else if($('#expYearSelect').val()=="")
				 {
				 $('#expYearSelect').focus();
				 $('.c-card-content input').removeClass('error');
			     // $('#expYearSelect').addClass('error'); 
				 $( ".red-btn" ).css( "pointer-events", "auto" );
			     
				 }  
				 
			 else if(len_cvv < 3)
				 {
				    $('#cvv_no').focus();
			        $('.c-card-content input').removeClass('error');
			        $('#cvv_no').addClass('error');
			        $( ".red-btn" ).css( "pointer-events", "auto" );
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
			
			if (myCardType == 'Amex')
			{
				expYY = expYYYY.toString();
			}
			
			/* document.getElementById("expiry").value = expMM+"/"+expYY;  */
			var expEnc = expMM+"/"+expYY; 
			// 1
			 //alert("myCardNo  >>>>> validate card=="+myCardNo);
			/*var encryptedCard = CryptoJS.AES.encrypt(myCardNo+"|"+txnId,CryptoJS.enc.Hex.parse(iv),
			 		{
		                     iv : CryptoJS.enc.Hex.parse(iv),
		                     mode : CryptoJS.mode.CBC,
		                     padding : CryptoJS.pad.Pkcs7
	               	}); 
			
			 var encryptedCardInHex = encryptedCard.ciphertext.toString(CryptoJS.enc.Hex);*/
			 
			var encryptedCardInHex=encrypt(myCardNo+"|"+txnId);
	
			 // 2
			 // alert("encryptedCardInHex >>> "+encryptedCardInHex);
			 /*var encryptedExp = CryptoJS.AES.encrypt(expEnc,CryptoJS.enc.Hex.parse(iv),
					 		{
				                     iv : CryptoJS.enc.Hex.parse(iv),
				                     mode : CryptoJS.mode.CBC,
				                     padding : CryptoJS.pad.Pkcs7
			               	}); 
					
					 var encryptedExpInHex = encryptedExp.ciphertext.toString(CryptoJS.enc.Hex);*/
				
			var encryptedExpInHex=encrypt(expEnc);
			
			
			 // 3
			/* var encryptedCvv = CryptoJS.AES.encrypt($('#cvv_no').val(),CryptoJS.enc.Hex.parse(iv),
				 		{
			                     iv : CryptoJS.enc.Hex.parse(iv),
			                     mode : CryptoJS.mode.CBC,
			                     padding : CryptoJS.pad.Pkcs7
		               	}); 
				
			var encryptedCvvInHex = encryptedCvv.ciphertext.toString(CryptoJS.enc.Hex);*/
			
			var encryptedCvvInHex=encrypt($('#cvv_no').val());
			var encryptedMobile=encrypt($('#mobileNo').val());
			var encryptedEmailId=encrypt($('#emailId').val());
			
			
			  
	//		alert("Enc :: "+encryptedCardInHex);
			
			$('#card_no').val(encryptedCardInHex);
			$('#cvv_no').val(encryptedCvvInHex);
			$('#expiry').val(encryptedExpInHex);
			$('#cName').val(cName);
			$('#expMonthSelect').val('00');
			$('#expYearSelect').val('0000');
			$('#mobileNo').val(encryptedMobile);
			$('#emailId').val(encryptedEmailId);
			
			 // document.getElementById("expiry").value = encryptedExpInHex;
			
			//$('.red-btn').attr('disabled', true);
			 return true;	
	  } 
	  
	  else {
		   swal(ccErrors[ccErrorNo]);
		 
		  // $('#card_no').focus();    // ajax calling on focus 
		  $('#card_no').addClass('error');
		  $( ".red-btn" ).css( "pointer-events", "auto" );
		  $('#payment_mode_drop_down').attr('disabled', false);
			 $('#payment_mode_drop_down2').attr('disabled', false);
			 
			 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
	    return false;
	  }
	  
	  
}


	function ValidateCVVSaveCardWC()
 	{
	
	var val_i = document.getElementById("wcvalue_of_iCC").value;
	
	//alert($('#cookienotexist').css('display'));
	var getSelectedValue = document.querySelector( 'input[name="chb"]:checked');   
	  
if(getSelectedValue == null && $("#cookieexist").is(':visible')) {   
     	swal("Please select anyone!"); 
    				return false;
		}
 		
 	
  		if(val_i =='' || val_i =="")
 		{
 		  
 			swal("Please enter the cvv!");
 			return false;
 		}
  		else
 		{		
  			var len = document.getElementById("wccvv_"+val_i).value.length;
  	  		 var cvvVal = document.getElementById("wccvv_"+val_i).value;
  	  		
 		if(len < 3) 
 		{
 			swal("Please enter the cvv must be 3 digit !");
 			 return false;
 		}
 		
 		if($('#capacha1').val()=="" && $("#cookieexist").is(':visible'))
	{
        $('#capacha1').focus();
        $('.c-card-content input').removeClass('error');
        $('#capacha1').addClass('error');
        $( ".red-btn" ).css( "pointer-events", "auto" );
       $('#payment_mode_drop_down').attr('disabled', false);
		 $('#payment_mode_drop_down2').attr('disabled', false);
		 
		 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
        return false;   
    }
    
	//else if($('#capacha').val() != "alchemy")
	else if($('#capacha1').val() != document.getElementById("lblCaptcha1").innerHTML.trim() && $("#cookieexist").is(':visible'))
	{
        $('#capacha1').focus();
        $('.c-card-content input').removeClass('error');
        $('#capacha1').addClass('error');
        swal("Please entered the correct capacha");
        $( ".red-btn" ).css( "pointer-events", "auto" );
        $('#payment_mode_drop_down').attr('disabled', false);
		 $('#payment_mode_drop_down2').attr('disabled', false);
		 
		 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
        return false;   
    } 
    
    	
 		//	var myCardNo = document.getElementById("decCardNoDC").value;
 		//	var expEnc = document.getElementById("SCExpiryC").value;
 			
 		//	let expMMYY = expEnc; 
		//	let encMMYY = expMMYY.substr(0, 2)+"/"+expMMYY.substr(2);
 			
 		//	var encryptedExpInHex=encrypt(encMMYY);
			
			var encryptedCvvInHex=encrypt(cvvVal);
 			
 			$('#wccvv').val(encryptedCvvInHex);
 		//	$('#SCExpiryC').val(encryptedExpInHex);
 			
 		//	$('#payNowDC').attr('disabled', true);
 			document.getElementById("payinfoformwithcookie").submit();
 			return true;
 		} 
 	  }
 		
 		
/*save card transaction without cookie*/ 		
function ValidateCVVSaveCardWOC()
 	{
	
	var val_i = document.getElementById("wocvalue_of_iCC").value;
	
	 
		var getSelectedValue1 = document.querySelector( 'input[name="chb1"]:checked');   

 		
 		if(getSelectedValue1 == null && $("#cookienotexist1").is(':visible')) {   
     	swal("Please select anyone!"); 
    				return false;
		}
  		if(val_i =='' || val_i =="")
 		{
 		  
 			swal("Please enter the cvv!");
 			return false;
 		}
  		else
 		{		
  			var len = document.getElementById("woccvv_"+val_i).value.length;
  	  		 var cvvVal = document.getElementById("woccvv_"+val_i).value;
  	  		
 		if(len < 3) 
 		{
 			swal("Please enter the cvv must be 3 digit !");
 			 return false;
 		}
 		
 		
    
    	if($('#capacha2').val()=="" &&  $('#cookienotexist').css('display') == 'block')
	{
        $('#capacha2').focus();
        $('.c-card-content input').removeClass('error');
        $('#capacha2').addClass('error');
        $( ".red-btn" ).css( "pointer-events", "auto" );
        $('#payment_mode_drop_down').attr('disabled', false);
		 $('#payment_mode_drop_down2').attr('disabled', false);
		 
		 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
        return false;   
    }
    
	//else if($('#capacha').val() != "alchemy")
	else if($('#capacha2').val() != document.getElementById("lblCaptcha2").innerHTML.trim() && $('#cookienotexist').css('display') == 'block')
	{
        $('#capacha2').focus();
        $('.c-card-content input').removeClass('error');
        $('#capacha2').addClass('error');
        swal("Please entered the correct capacha");
        $( ".red-btn" ).css( "pointer-events", "auto" );
        $('#payment_mode_drop_down').attr('disabled', false);
		 $('#payment_mode_drop_down2').attr('disabled', false);
		 
		 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
        return false;   
    } 
 		
 		
 			
 		//	var myCardNo = document.getElementById("decCardNoDC").value;
 		//	var expEnc = document.getElementById("SCExpiryC").value;
 			
 		//	let expMMYY = expEnc; 
		//	let encMMYY = expMMYY.substr(0, 2)+"/"+expMMYY.substr(2);
 			
 		//	var encryptedExpInHex=encrypt(encMMYY);
			
			var encryptedCvvInHex=encrypt(cvvVal);
 			
 			$('#woccvv').val(encryptedCvvInHex);
 		//	$('#SCExpiryC').val(encryptedExpInHex);
 			
 		//	$('#payNowDC').attr('disabled', true);
 			document.getElementById("payinfoformwithoutcookie").submit();
 			return true;
 		} 
 	  }





 function sendOtp(cmobileNo, cemailId, txnId, merchantName)
    {
      	
    	var mobileNo= cmobileNo;
    	var emailId= cemailId;
    	var txnId= txnId;
    	//var jsessionId = '${cookie.JSESSIONID.value}';
    		$.ajax
    		({				
    			type: 'POST',
    			data: { mobileNo : mobileNo,  emailId : emailId, txnId : txnId, merchantName: merchantName},
    			url: "/payment/ajaxAction?type=sendOtp",
    			
    			success: function(result)
    			{
    				
    			
    			},
    			error: function (err)
    			{
    				alert("Error in sending otp "+err);
    		    }
    			
    		});
    		 
    		 }

function ValidateNB()
{
	
	 $('#payment_mode_drop_down').attr('disabled', true);
	 $('#payment_mode_drop_down2').attr('disabled', true);
	 
	 $( ".payment_dropdown" ).css( "pointer-events", "none" );
	 $( ".red-btn" ).css( "pointer-events", "none" );
	if(document.getElementById("bank-select").selectedIndex != 0)
	{
	var instrument = document.getElementById('instrumentId').value
	document.getElementById('instrumentId').value = "NB";
//	alert("Validate instrumentId "+document.getElementById('instrumentId').value);
	document.getElementById("nbForm").submit();
	}
	else 
		{
		swal("Please Select Bank.. !");
		 $( ".red-btn" ).css( "pointer-events", "auto" );
		 $('#payment_mode_drop_down').attr('disabled', false);
		 $('#payment_mode_drop_down2').attr('disabled', false);
		 
		 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
		return false;
		}
}


function storedcardFill(cardno){
	//alert("storedcardFill  :: "+cardno);
  	
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
	var len =  strArray[12];
	
	// alert("length  :: "+len);
	
	for (var j = 0; j<len;j++) {
		$("#card_cvv"+j).hide();
    }
	
	$("#card_cvv"+value_i).show()
	
	//$('span[ID!="x"]').attr('style', "display: none;");
	// var cMid = strArray[9];
	// var cMail = strArray[10];
	// var cMob = strArray[11];
	// document.getElementById("email").value = cMail;
	//  document.getElementById("phone").value = cMob;
	
	
		$.ajax
		({				
			type: 'POST',
			data: { encCardNo : cNumber , encKey : encKey , keKey : keKey },
			url: "/payment/ajaxAction?type=decryptCard",
			success: function(result)
			{
				// alert("Result ::: "+result);
				// array[x] = [result,maskedCNumber,cExpiry,cName,cType,cInstrument];
				document.getElementById("decCardNo").value = result;
				document.getElementById("card-number").value = maskedCNumber;
				document.getElementById("SCExpiry").value = cExpiry
			
				document.getElementById("card-holder").value = cName;
				
				document.getElementById("sccardType").value = cType;
				document.getElementById("scinstrumentId").value = cInstrument;
				
				// alert("sccardType :: "+cType+"  scinstrumentId "+cInstrument);
				
				document.forms['scForm']['scinstrumentId'].value = cInstrument;
				document.forms['scForm']['value_of_i'].value = value_i;
				
				// alert("cInstrument  ::  "+document.getElementById("instrumentId").value);
				
				
				// $("#scForm")[0].reset();
			},
			error: function (err)
			{
				alert("Error in getting Card Number "+err);
		    }
			
		});
		
		
		}
	

function removeCard(cardno)
{	 
	var strArray = cardno.split(",");
 	var value_i = strArray[0];
 	var cNo = strArray[1];
 	var cMid = strArray[9];
	var cMail = strArray[10];
	var cMob = strArray[11];
  	
	swal({
	  	  title: "Are you sure ?",
	  	  text: "you want to delete ?",
	  	  icon: "warning",
	  	  buttons: true,
	  	  dangerMode: true
	
	},
	function(isConfirm) {
	  if (isConfirm) {
	    $.ajax
		    	({				
		    		type: 'POST',
		    		data: {cNo : cNo, cMid : cMid, cMail : cMail, cMob : cMob},
		    		url: "/payment/ajaxAction?type=removeCard",
		    		success: function(result)
		    		{
		    			$("#li"+value_i).remove()
		    			
		    			swal("Poof! Your stored card has been deleted!", { icon: "success",});
		    			
		    		},
		    		error: function (err)
		    		{
		    			alert("Error in removing card "+err);
		    	    }
		    	});
	  } else {
	    swal("Your stored card is safe!");
	  }
	});

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
	
$('#card_no').on('keyup', function() {
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

$('#card_no').on('keyup', function () {     
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
	var current_year = new Date().getFullYear();
	 if($("#expYearSelect").val() == new Date().getFullYear()){
	//	alert("check for month--");
	if($("#expMonthSelect").val() != "" && $("#expMonthSelect").val() != null){
		var currentmonth =new Date().getMonth()+1;
		if($("#expMonthSelect").val() < currentmonth){
			 $('#expMonthSelect').val('');
			 
			  alert("pls enter valid month you have entered past month's date");
		}
		// $("#expMonthSelect option").each(function(){
		//	
		//});
		}
	}
                                 
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
   	$( ".red-btn" ).css( "pointer-events", "auto" );
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

function refreshCaptcha1()
{
	//alert('test');
	 $.ajax({
	     type:"GET",    
	     url: "captcha.jsp",
	           
	       success: function(data) {
			$("#lblCaptcha1").html(data);
			//alert("lblCaptcha"+document.getElementById("lblCaptcha").innerHTML);
			
	       }
	     }); 
}

function refreshCaptcha2()
{
	//alert('test');
	 $.ajax({
	     type:"GET",    
	     url: "captcha.jsp",
	           
	       success: function(data) {
			$("#lblCaptcha2").html(data);
			//alert("lblCaptcha"+document.getElementById("lblCaptcha").innerHTML);
			
	       }
	     }); 
}

$('#pay').click(function (){
 	var txnId = $('#txnId').val();
 	var instrumentId = "UPI";
  	var cardType = "UPI";
	if(upiIdValue != 'otherApp'){
		var inputString = document.getElementById("inputUPI").value;
		var upiId = document.getElementById("inputUPI23").value;
  		var VPAValue = inputString+upiId;
		document.forms['UPIForm'].otherAppValue.value = "false"
	}else{
		var VPAValue = document.getElementById("inputUPIOtherApp").value;
		document.forms['UPIForm'].otherAppValue.value = "true"
	}
	
	console.log("VPA Value"+VPAValue);
  	var checksumValue = document.getElementById("checksum").value;
  	var MTrackid = $('#txnId').val();

	document.getElementById("pay").disabled = true;
	document.getElementById("footer-btn").disabled = true;
 
	$( "#payNowUPI" ).css( "pointer-events", "none" );
	
	$.ajax
 		({				
 			type: 'POST',
 			data: { txnId : txnId , instrumentId : instrumentId , cardType : cardType, VPA : VPAValue,checksum : checksumValue,MTrackid : MTrackid},
 			url: "/payment/UpiCheckServlet",
 			success: function(result)
 			{
 				console.log(result);
				if (result.match("failure"))
				{
 					document.getElementById("inputUPI").value="";
					document.getElementById("inputUPIOtherApp").value="";
 					//alert("Enter the Valid VPA");
					document.getElementById("errorMessage").innerHTML = "&#9888; Enter the Valid VPA";
					document.getElementById("errorMessage").style.display = "block";
					document.getElementById("pay").disabled = false;
					document.getElementById("footer-btn").disabled = false;
				}
 				else if(result.match("success"))
 				{
 					document.getElementById("UPIForm").submit();
 				}
				else if(result.match("null"))
 				{
 					document.getElementById("inputUPI").value="";
					document.getElementById("inputUPIOtherApp").value="";
 					//alert("Enter the Valid VPA");
					document.getElementById("errorMessage").innerHTML = "&#9888; Enter the Valid VPA";
					document.getElementById("errorMessage").style.display = "block";
					document.getElementById("pay").disabled = false;
					document.getElementById("footer-btn").disabled = false;
 				}
 			},
 			error: function (err)
 			{
 				document.getElementById("inputUPI").value="";
				document.getElementById("inputUPIOtherApp").value="";
 				$(".red-btn").attr('disabled', false);
				document.getElementById("errorMessage").innerHTML = "&#9888; Error While Processing your request, Please try again later";
				document.getElementById("errorMessage").style.display = "block";
				$( ".red-btn" ).css( "pointer-events", "auto" );
				  $('#payment_mode_drop_down').attr('disabled', false);
					 $('#payment_mode_drop_down2').attr('disabled', false);
					 
					 $( ".payment_dropdown" ).css( "pointer-events", "auto" );

 		    }
 		});

});

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


function resendNowTimer() {
	 
	
    document.getElementById("resendNow").disabled = true;
  
      setTimeout(function(){document.getElementById("resendNow").disabled = false;}, 60000);  
     
}
   
function clearUPIError()
{
	document.getElementById("errorMessage").innerHTML = "";
	document.getElementById("errorMessage").style.display = "none";
	 $( ".red-btn" ).css( "pointer-events", "auto" );
	 
	  $('#payment_mode_drop_down').attr('disabled', false);
		 $('#payment_mode_drop_down2').attr('disabled', false);
		 
		 $( ".payment_dropdown" ).css( "pointer-events", "auto" );
}
}catch(err)
{
	alert(err);
}

function ValidateWallet()
{
	
	if(document.getElementById("wallet-bank-select").selectedIndex > 0)
	{
		var e = document.getElementById("wallet-bank-select");
		var strUser = e.options[e.selectedIndex].value;
		
	}
	else 
	{
		swal("Please Select wallet");
		return false;
	}
}
function ValidateWallet2()
{
	
	if(document.getElementById("wallet-bank-select2").selectedIndex > 0)
	{
		var e = document.getElementById("wallet-bank-select2");
		var strUser = e.options[e.selectedIndex].value;
		
	}
	else 
	{
		swal("Please Select wallet");
		return false;
	}
}

function ValidateWallet1()
{
	
	if(document.getElementById("wallet-bank-select1").selectedIndex > 0)
	{
		var e = document.getElementById("wallet-bank-select1");
		var strUser = e.options[e.selectedIndex].value;
		
	}
	else 
	{
		swal("Please Select wallet");
		return false;
	}
}



function startTimerForBQR()
{

i = 0;
 	document.getElementById('timer').innerHTML =
		  04 + ":" + 55;

		
	          startTimer();
                
		

		function startTimer() {
		  var presentTime = document.getElementById('timer').innerHTML;
		  var timeArray = presentTime.split(/[:]+/);
		  var m = timeArray[0];
		  var s = checkSecond((timeArray[1] - 1));
		  if(s==59){m=m-1}
		  
		document.getElementById('timer').innerHTML =
				    m + ":" + s;
		 timer  =  setTimeout(startTimer, 1000);
		}

		function checkSecond(sec) {
		  if (sec < 10 && sec >= 0) {sec = "0" + sec}; // add zero in front of numbers < 10
		  if (sec < 0) {sec = "59"};
		  return sec;
		} 
		//alert(i);


		
	checkStatus();

		

}

function clearTimer()
{
	//alert("clearTimer");
 	i = -1;
}

function checkStatus()
{
	//check the status
	
var auto = setInterval(function ()
{
 //alert("test"+i);
//alert("valid :"+(i !== -1));
if (i !== -1)
{

		i++;
		
		

		var transactionId =$('#txnId').val();
		//var transactionId ="1001927010100480032";
	 $.ajax
	({				
		
		type: 'POST',
	    data: {transactionId :transactionId, val_i : i},
		url: "/payment/ajaxAction?type=getStatusBQRTransaction",
		
		success: function(result)
		{
			// alert("result "+result);
			if(result != null && result !="" && result!="N" )  /* && i == 30 condition in ajaxAction*/ 
			{
				var obj = $.parseJSON(result);
				

				submitme(obj.url,obj.encRespXML,obj.merchantId,obj.pgid);
			}
		
		},
		error: function (err)
	{
		alert("Getting error : "+merchantRefNo);
		console.log("Getting error : "+err);
    }
		
		
	});
 }
}, 10000); 

// refresh every 5000 milliseconds

}
function submitme(url,respData,mid,pgid)
{
	document.getElementById("respDataID").value = respData;
	document.getElementById("merchantId").value = mid;
	document.getElementById("pgid").value = pgid;
	document.getElementById("MyForm").action = url;
	document.forms["MyForm"].submit();
}

function loadQRData()
{
	var transactionId =$('#txnId').val();
	 $.ajax
	({				
		
		type: 'GET',
	    data: {transactionId :transactionId},
		url: "/payment/BQRProcessor",
		
		success: function(result)
		{
			if(result != null && result !="") 
			{
				$("#bqrImage").attr("src", result);
				startTimerForBQR();
			}
		
		},
		error: function (err)
		{
			 alert("Getting error : "+err);
			 console.log("Getting error : "+err);
	    }
	});
}


function getSurcharge()
{
	var transactionId = $('#txnId').val();
	var cardType = $('#cardType').val();
	var instrumentId = $('#instrumentId').val();
	var amount = amt;
	var merchantId = merId;
	var emioption= $('#emioption').val();
	var bank = $('#bank').val();
	var cardNo_val = $('#card_no').val();
	var cardNumber_val = cardNo_val.replace(/\s+/g, '');
	var cardNumber = cardNumber_val.substring(0, 6);

	 $.ajax
	({				
		
		type: 'GET',
	    data: {transactionId :transactionId,cardType:cardType,instrumentId:instrumentId,amount:amount,merchantId:merchantId,emioption:emioption,bank:bank,cardNo:cardNumber},
		url: "/payment/SurchargeCalculation",
		
		success: function(result)
		{
			if(result != null && result !="") 
			{
				var obj = $.parseJSON(result);
				//alert(result);
				$('#txnFee').text(obj.txnFee);
				$('#gst').text(obj.gst);
				$('#totalAmount').text(obj.total);
			}
		
		},
		error: function (err)
		{
			console.log("Getting error : "+err);
	    }
	});
}

function resetSurcharge()
{
	$('#txnFee').text("0.00");
	$('#gst').text("0.00");
	$('#totalAmount').text(document.getElementById('baseAmount').innerHTML);
}

$("#bank-select").change(function () {
        var end = this.value;
		$('#bank').val(end);
		getSurcharge();
    });
    
    $("#wallet-bank-select").change(function () {
        var end = this.value;
		$('#bank').val(end);
		getSurcharge();
    });
    
     $("#wallet-bank-select2").change(function () {
        var end = this.value;
		$('#bank').val(end);
		getSurcharge();
    });
    
     $("#wallet-bank-select1").change(function () {
        var end = this.value;
		$('#bank').val(end);
		getSurcharge();
    });

$('#modal').click(function () {     
	 $('#myModal').css('display','block'); 
	 $('#myModal').addClass('ModalBackdrop');
	 $('#myModal').show();
});

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
	$('.vpa-other-app').css('color','#54A8EC');
	upiIdValue = 'otherApp';
	$('#hr').show();

 }
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
	$('.vpa-other-app').css('color','#54A8EC');
	document.getElementById('upiIdInput').className="form-inline2";
	document.getElementById('otherAppInput').className="form-inline-otherApp";
	upiIdValue = 'otherApp';
	$('#hr').show();
}
	
}


$('#cancelPayment').click(function () {     
	 $('#exampleModalLong').css('display','block'); 
	 $('#exampleModalLong').show();
});

$('#cancelBtn').click(function (){
	 $("#UPIpayment").hide();
	 $(".upi_submit_btn").hide();
	 $('#myModal').css('display','none'); 
	 $('.showPayementDiv').hide();
	 $('.upiLastScreen').hide();
	 $('.upi-content').hide();
});

$('#modalClose').click(function(){
	$("#myModal").hide();
});