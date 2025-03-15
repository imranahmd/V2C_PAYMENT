$(window).load(function(e) {
    $('#preloader').fadeOut();
    $('.wallet-content').hide();
     $('.wallet-content1').hide();
      $('.wallet-content2').hide();
    $(document).on('click', '.stage', function(){
        $('.drop_options').slideToggle();
        $(this).toggleClass('open');
    });
    
    $(document).on('click', '.drop_options .payment_mode li', function(){
        var getHTML = $(this).html();
        //console.log(getHTML);
        $('.stage').find('ul.payment_mode li').html(getHTML);
        $('.drop_options').slideUp();
        $('.stage').removeClass('open');
        if($(this).hasClass('payzapp')){
            $('.payzap-content').show();
            $('.nbpayzap-content').hide();
			$('.cc-emi').hide();
			$('.stored-card').hide();
			$('.wallet-content').hide();
			$('.wallet-content2').hide();
			$('.wallet-content1').hide();
            $('.card_submit_btn, .c-card-content').hide();
            $('.upi_submit_btn, .upi-content').hide();
            $('.atomupi_submit_btn, .atomupi-content').hide();
            $('.bqr-content').hide();
            $('.offline-content').hide();
            $('.total_amount').removeClass('card-payment');
            $("#pay_info")[0].reset();
         
        }
        
        else if($(this).hasClass('netbanking')){
        	
        	$("#pay_info")[0].reset();
            $('.payzap-content').hide();
            $('.nbpayzap-content').show();
			$('.cc-emi').hide();
			$('.wallet-content').hide();
			$('.wallet-content1').hide();
			$('.wallet-content2').hide();
			$('.stored-card').hide();
            $('.card_submit_btn, .c-card-content').hide();
            $('.upi_submit_btn, .upi-content').hide();
            $('.atomupi_submit_btn, .atomupi-content').hide();
            $('.bqr-content').hide();
            $('.total_amount').removeClass('card-payment');
          	$('.offline-content').hide();
          
            $("#pay_info")[0].reset();
            $("#nbForm")[0].reset();
        }
        else if($(this).hasClass('card')){     // default
            $('.payzap-content').hide();
            $('.nbpayzap-content').hide();
			$('.cc-emi').hide();
			$('.stored-card').hide();
			$('.wallet-content').hide();
			$('.wallet-content1').hide();
			$('.wallet-content2').hide();
			$('.card_submit_btn, .c-card-content').hide();  // bharat
			$('.sc_card_submit_btn').hide();
            $('.total_amount').addClass('card-payment');
            $('.upi_submit_btn, .upi-content').hide();
            $('.offline-content').hide();
            $('.atomupi_submit_btn, .atomupi-content').hide();
            $('.bqr-content').hide();
            $("#pay_info")[0].reset();
            $("#nbForm")[0].reset();
        }
        
        else if($(this).hasClass('c-card')){
            $('.payzap-content').hide();
            $('.nbpayzap-content').hide();
			$('.cc-emi').hide();
			$('.stored-card').hide();
			$('.wallet-content').hide();
			$('.wallet-content1').hide();
			$('.wallet-content2').hide();
            $('.total_amount').addClass('card-payment');
            $('.dc_card_submit_btn').hide();
            $('.occ_card_submit_btn').hide();
            $('.odc_card_submit_btn').hide();
            $('.upi_submit_btn, .upi-content').hide();
            $('.offline-content').hide();
            $('.atomupi_submit_btn, .atomupi-content').hide();
            $('.bqr-content').hide();
            $('.cc_card_submit_btn, .c-card-content').show();
            
            $("#pay_info")[0].reset();
            $("#nbForm")[0].reset();
        }
        else if ($(this).hasClass('upi'))
        {
            $('.payzap-content').hide();
            $('.nbpayzap-content').hide();
			$('.cc-emi').hide();
			$('.stored-card').hide();
            $('.total_amount').addClass('card-payment');
            $('.wallet-content').hide();
            $('.wallet-content1').hide();
            $('.wallet-content2').hide();
            $('.dc_card_submit_btn').hide();
            $('.occ_card_submit_btn').hide();
            $('.odc_card_submit_btn').hide();
            //$('upi_submit_btn').show();
            $('.upi_submit_btn, .upi-content').show();
            $('.offline-content').hide();
            $('.atomupi_submit_btn, .atomupi-content').hide();
            $('.cc_card_submit_btn, .c-card-content').hide();
            $('.bqr-content').hide();
            $("#pay_info")[0].reset();
            $("#nbForm")[0].reset();
			getSurchargeUPI();
        }
        else if ($(this).hasClass('atomupi'))
        {
            $('.payzap-content').hide();
            $('.nbpayzap-content').hide();
			$('.cc-emi').hide();
			$('.stored-card').hide();
            $('.total_amount').addClass('card-payment');
            $('.wallet-content').hide();
            $('.wallet-content1').hide();
            $('.wallet-content2').hide();
            $('.dc_card_submit_btn').hide();
            $('.occ_card_submit_btn').hide();
            $('.odc_card_submit_btn').hide();
            //$('upi_submit_btn').show();
            $('.offline-content').hide();
            $('.upi_submit_btn, .upi-content').hide();
            $('.cc_card_submit_btn, .c-card-content').hide();
            $('.atomupi_submit_btn, .atomupi-content').show();
            $('.bqr-content').hide();
            $("#pay_info")[0].reset();
            $("#nbForm")[0].reset();
			getSurchargeUPI();
        }
        else if ($(this).hasClass('bqr'))
        {
            $('.payzap-content').hide();
            $('.nbpayzap-content').hide();
			$('.cc-emi').hide();
			$('.stored-card').hide();
            $('.total_amount').addClass('card-payment');
            $('.dc_card_submit_btn').hide();
            $('.occ_card_submit_btn').hide();
            $('.wallet-content').hide();
            $('.wallet-content1').hide();
            $('.wallet-content2').hide();
            $('.odc_card_submit_btn').hide();
            //$('upi_submit_btn').show();
            $('.upi_submit_btn, .upi-content').hide();
            $('.offline-content').hide();
            $('.atomupi_submit_btn, .atomupi-content').hide();
            $('.cc_card_submit_btn, .c-card-content').hide();
            $('.bqr-content').show();
            $("#pay_info")[0].reset();
            $("#nbForm")[0].reset();
        }
        else if($(this).hasClass('d-card')){
            $('.payzap-content').hide();
            $('.nbpayzap-content').hide();
			$('.cc-emi').hide();
			$('.stored-card').hide();
			$('.wallet-content').hide();
			$('.wallet-content1').hide();
			$('.wallet-content2').hide();
			$('.cc_card_submit_btn').hide();
            $('.occ_card_submit_btn').hide();
            $('.odc_card_submit_btn').hide();
            $('.upi_submit_btn, .upi-content').hide();
            $('.atomupi_submit_btn, .atomupi-content').hide();
            $('.bqr-content').hide();
			 $('.dc_card_submit_btn, .c-card-content').show();   // bharat
            $('.total_amount').addClass('card-payment');
            $('.offline-content').hide();
            $("#pay_info")[0].reset();
            $("#nbForm")[0].reset();
        }
        
        
        else if($(this).hasClass('oc-card')){   
        	// alert("Other Credit Card.. !!");
            $('.payzap-content').hide();
            $('.nbpayzap-content').hide();
			$('.cc-emi').hide();
			$('.stored-card').hide();
			$('.odc_card_submit_btn').hide();
            $('.cc_card_submit_btn').hide();
            $('.wallet-content').hide();
            $('.wallet-content1').hide();
            $('.wallet-content2').hide();
            $('.dc_card_submit_btn').hide();
            $('.upi_submit_btn, .upi-content').hide();
            $('.atomupi_submit_btn, .atomupi-content').hide();
            $('.bqr-content').hide();
            $('.offline-content').hide();
			$('.occ_card_submit_btn, .c-card-content').show();  // bharat
            $('.total_amount').addClass('card-payment');
            
            $("#pay_info")[0].reset();
            $("#nbForm")[0].reset();
        }
        
        else if($(this).hasClass('od-card')){   
        	// alert("Other Debit Card.. !!");
            $('.payzap-content').hide();
            $('.nbpayzap-content').hide();
			$('.cc-emi').hide();
			$('.stored-card').hide();
			
			$('.sc_card_submit_btn').hide();
			$('.cc_card_submit_btn').hide();
            $('.dc_card_submit_btn').hide();
            $('.wallet-content').hide();
             $('.wallet-content1').hide();
             $('.wallet-content2').hide();
            $('.occ_card_submit_btn').hide();
            $('.upi_submit_btn, .upi-content').hide();
            $('.atomupi_submit_btn, .atomupi-content').hide();
            $('.bqr-content').hide();
			$('.odc_card_submit_btn, .c-card-content').show();  // bharat
            $('.total_amount').addClass('card-payment');
            $('.offline-content').hide();
        
            $("#pay_info")[0].reset();
            $("#nbForm")[0].reset();
        }
        
		 else if($(this).hasClass('stored')){
			 $('.cc-emi').hide();
			 $('.stored-card').show();
            // $('.payzap-content').show();
			 $('.sc_card_submit_btn').show();
			 $('.payzap-content').hide();
			 $('.nbpayzap-content').hide();
			 $('.wallet-content').hide();
			$('.wallet-content1').hide();
			$('.wallet-content2').hide();
             $('.dc_card_submit_btn').hide();
             $('.occ_card_submit_btn').hide();
             $('.odc_card_submit_btn').hide();
			 $('.cc_card_submit_btn, .c-card-content').hide();
			 $('.upi_submit_btn, .upi-content').hide();
			 $('.atomupi_submit_btn, .atomupi-content').hide();
			 $('.offline-content').hide();
			 $('.bqr-content').hide();
             $('.total_amount').addClass('card-payment');
             $("#pay_info")[0].reset();
             $("#nbForm")[0].reset();
             $('.upi-content').hide();
        }
		else if($(this).hasClass('emi')){
			  $('.payzap-content').hide();
			  $('.nbpayzap-content').hide();
			  
			$('.stored-card').hide();
			$('.cc-emi').show();
            $('.total_amount').addClass('card-payment');
            $('.upi_submit_btn, .upi-content').hide();
            $('.atomupi_submit_btn, .atomupi-content').hide();
            $('.bqr-content').hide();
            $('.wallet-content').hide();
            	 $('.wallet-content1').hide();
            	 $('.wallet-content2').hide();
            $('.dc_card_submit_btn').hide();
            $('.occ_card_submit_btn').hide();
            $('.odc_card_submit_btn').hide();
            $('.offline-content').hide();
            $('.cc_card_submit_btn, .c-card-content').hide();
            $("#pay_info")[0].reset();
            $("#nbForm")[0].reset();
        }
		else if($(this).hasClass('wallet')){
	        	
	        	$("#pay_info")[0].reset();
	            $('.payzap-content').hide();
	            $('.wallet-content').show();
	             $('.wallet-content1').hide();
	             $('.wallet-content2').hide();
				$('.nbpayzap-content').hide();
				$('.cc-emi').hide();
				$('.stored-card').hide();
	            $('.card_submit_btn, .c-card-content').hide();
	            $('.upi_submit_btn, .upi-content').hide();
	            $('.atomupi_submit_btn, .atomupi-content').hide();
	            $('.bqr-content').hide();
	            $('.offline-content').hide();
	            $('.total_amount').removeClass('card-payment');
	          
	            $("#pay_info")[0].reset();
	            $("#nbForm")[0].reset();
	        }
	        
	        else if($(this).hasClass('wallet1')){
	        	
	        	$("#pay_info")[0].reset();
	            $('.payzap-content').hide();
	            $('.wallet-content1').show();
	            $('.wallet-content2').hide();
	             $('.wallet-content').hide();
				$('.nbpayzap-content').hide();
				$('.cc-emi').hide();
				$('.stored-card').hide();
	            $('.card_submit_btn, .c-card-content').hide();
	            $('.upi_submit_btn, .upi-content').hide();
	            $('.atomupi_submit_btn, .atomupi-content').hide();
	            $('.bqr-content').hide();
	            $('.offline-content').hide();
	            $('.total_amount').removeClass('card-payment');
	          
	            $("#pay_info")[0].reset();
	            $("#nbForm")[0].reset();
	        }
	        
	         else if($(this).hasClass('wallet2')){
	        	
	        	$("#pay_info")[0].reset();
	            $('.payzap-content').hide();
	            $('.wallet-content1').hide();
	            $('.wallet-content2').show();
	             $('.wallet-content').hide();
				$('.nbpayzap-content').hide();
				$('.cc-emi').hide();
				$('.stored-card').hide();
	            $('.card_submit_btn, .c-card-content').hide();
	            $('.upi_submit_btn, .upi-content').hide();
	            $('.atomupi_submit_btn, .atomupi-content').hide();
	            $('.bqr-content').hide();
	            $('.offline-content').hide();
	            $('.total_amount').removeClass('card-payment');
	          
	            $("#pay_info")[0].reset();
	            $("#nbForm")[0].reset();
	        }
	        
	        else if($(this).hasClass('Offline')){
			$('.payzap-content').hide();
			$('.nbpayzap-content').hide();
			$('.offline-content').show();
			$('.stored-card').hide();
			$('.cc-emi').hide();
			$('.total_amount').addClass('card-payment');
			$('.upi_submit_btn, .upi-content').hide();
			$('.bqr-content').hide();
			$('.dc_card_submit_btn').hide();
			$('.occ_card_submit_btn').hide();
			$('.odc_card_submit_btn').hide();
              
              $('.wallet-content').hide();
            	 $('.wallet-content1').hide();
            	 $('.wallet-content2').hide();


			$('.cc_card_submit_btn, .c-card-content').hide();
			$("#pay_info")[0].reset();
			$("#nbForm")[0].reset();
		}
	        
        else{
			$('.cc-emi').hide();
            $('.payzap-content').hide();
            $('.nbpayzap-content').hide();
            $('.wallet-content').hide();
            	 $('.wallet-content1').hide();
            	 $('.wallet-content2').hide();
			$('.stored-card').hide();
			 $('.upi_submit_btn, .upi-content').hide();
			 $('.atomupi_submit_btn, .atomupi-content').hide();
			 $('.bqr-content').hide();
			$('.dc_card_submit_btn').hide();
            $('.occ_card_submit_btn').hide();
            $('.odc_card_submit_btn').hide();
            $('.cc_card_submit_btn, .c-card-content').hide();
            $('.total_amount').removeClass('card-payment');
            $("#pay_info")[0].reset();
            $("#nbForm")[0].reset();
            
        }
    });
    
});




$(window).load(function(e) {
    $('#preloader').fadeOut();
    $(document).on('click', '#payment', function(){
        $('#payment_opt').slideToggle();
        $(this).toggleClass('open');
    });
    
    $(document).on('click', '#payment_opt .payment_modes li', function(){
        var getHTML = $(this).html();
        console.log(getHTML);
        $('#payment').find('ul.payment_modes li').html(getHTML);
        $('#payment_opt').slideUp();
        $('#payment').removeClass('open');
        
    });
    
});


$(window).load(function(e) {
    $('#preloader').fadeOut();
    $(document).on('click', '#bank', function(){
        $('#bank_opt').slideToggle();
        $(this).toggleClass('open');
    });
    
    $(document).on('click', '#bank_opt .payment_modes li', function(){
        var getHTML = $(this).html();
        console.log(getHTML);
        $('#bank').find('ul.payment_modes li').html(getHTML);
        $('#bank_opt').slideUp();
        $('#bank').removeClass('open');
        
    });
    
});

$(window).load(function(e) {
    $('#preloader').fadeOut();
    $(document).on('click', '#action', function(){
        $('#action_opt').slideToggle();
        $(this).toggleClass('open');
    });
    
    $(document).on('click', '#action_opt .payment_modes li', function(){
        var getHTML = $(this).html();
        console.log(getHTML);
        $('#action').find('ul.payment_modes li').html(getHTML);
        $('#action_opt').slideUp();
        $('#action').removeClass('open');
        
    });
    
});

	function getSurchargeUPI()
	{
		var transactionId = $('#txnId').val();
		var cardType = $('#cardType').val();
		var instrumentId = "UPI";
		var amount = amt;
		var merchantId = merId;
		var emioption= "NA";
		var bank = $('#bank').val();
		
		var x = document.getElementById("atomupi-content");
		  if (window.getComputedStyle(x).display === "none") {
				bank = "UPI";
				cardType = "UPI";
		  } else {
				bank = "ATOMUPI";
				cardType = "ATOMUPI";
            }

		 $.ajax
		({				
			
			type: 'GET',
		    data: {transactionId :transactionId,cardType:cardType,instrumentId:instrumentId,amount:amount,merchantId:merchantId,emioption:emioption,bank:bank},
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
					$('#totalAmtupi').text(obj.total);
				}
			
			},
			error: function (err)
			{
				alert("Getting error : "+err);
		    }
		});
	}