$(window).load(function(e) {
    $('#preloader').fadeOut();
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
            $('.card_submit_btn, .c-card-content').hide();
           
            $('.total_amount').removeClass('card-payment');
            $("#pay_info")[0].reset();
         
        }
        
        else if($(this).hasClass('netbanking')){
        	
        	$("#pay_info")[0].reset();
            $('.payzap-content').hide();
            $('.nbpayzap-content').show();
			$('.cc-emi').hide();
			$('.stored-card').hide();
            $('.card_submit_btn, .c-card-content').hide();
           
            $('.total_amount').removeClass('card-payment');
          
            $("#pay_info")[0].reset();
            $("#nbForm")[0].reset();
        }
        else if($(this).hasClass('card')){     // default
            $('.payzap-content').hide();
            $('.nbpayzap-content').hide();
			$('.cc-emi').hide();
			$('.stored-card').hide();
			$('.card_submit_btn, .c-card-content').hide();  // bharat
			$('.sc_card_submit_btn').hide();
            $('.total_amount').addClass('card-payment');
            
            $("#pay_info")[0].reset();
            $("#nbForm")[0].reset();
        }
        
        else if($(this).hasClass('c-card')){
            $('.payzap-content').hide();
            $('.nbpayzap-content').hide();
			$('.cc-emi').hide();
			$('.stored-card').hide();
            $('.total_amount').addClass('card-payment');
            $('.dc_card_submit_btn').hide();
            $('.occ_card_submit_btn').hide();
            $('.odc_card_submit_btn').hide();
            $('.cc_card_submit_btn, .c-card-content').show();
            
            $("#pay_info")[0].reset();
            $("#nbForm")[0].reset();
        }
        
        else if($(this).hasClass('d-card')){
            $('.payzap-content').hide();
            $('.nbpayzap-content').hide();
			$('.cc-emi').hide();
			$('.stored-card').hide();
			$('.cc_card_submit_btn').hide();
            $('.occ_card_submit_btn').hide();
            $('.odc_card_submit_btn').hide();
			 $('.dc_card_submit_btn, .c-card-content').show();   // bharat
            $('.total_amount').addClass('card-payment');
           
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
            $('.dc_card_submit_btn').hide();
			
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
            $('.occ_card_submit_btn').hide();
            
			$('.odc_card_submit_btn, .c-card-content').show();  // bharat
            $('.total_amount').addClass('card-payment');
        
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
			 
             $('.dc_card_submit_btn').hide();
             $('.occ_card_submit_btn').hide();
             $('.odc_card_submit_btn').hide();
			 $('.cc_card_submit_btn, .c-card-content').hide();
			 
             $('.total_amount').addClass('card-payment');
             $("#pay_info")[0].reset();
             $("#nbForm")[0].reset();
        }
		else if($(this).hasClass('emi')){
			  $('.payzap-content').hide();
			  $('.nbpayzap-content').hide();
			  
			$('.stored-card').hide();
			$('.cc-emi').show();
            $('.total_amount').addClass('card-payment');
            
            $('.dc_card_submit_btn').hide();
            $('.occ_card_submit_btn').hide();
            $('.odc_card_submit_btn').hide();
            
            $('.cc_card_submit_btn, .c-card-content').hide();
            $("#pay_info")[0].reset();
            $("#nbForm")[0].reset();
        }
        else{
			$('.cc-emi').hide();
            $('.payzap-content').hide();
            $('.nbpayzap-content').hide();
            
			$('.stored-card').hide();
			
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