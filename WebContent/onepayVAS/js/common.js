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
        if($(this).hasClass('payzapp') || $(this).hasClass('netbanking')){
            $('.payzap-content').show();
			$('.cc-emi').hide();
			$('.stored-card').hide();
            $('.card_submit_btn, .c-card-content').hide();
            $('.total_amount').removeClass('card-payment');
            alert("reset netbanking");
            $("#pay_info").reset();
        	
        	
        }
        else if($(this).hasClass('card')){
            $('.payzap-content').hide();
			$('.cc-emi').hide();
			$('.stored-card').hide();
            $('.total_amount').addClass('card-payment');
            $('.card_submit_btn, .c-card-content').show();
            alert("reset card");
        }
		 else if($(this).hasClass('stored')){
			 $('.cc-emi').hide();
			 $('.stored-card').show();
             $('.payzap-content').show();
			 $('.card_submit_btn, .c-card-content').hide();
             $('.total_amount').addClass('card-payment');
             alert("reset stored");
        }
		else if($(this).hasClass('emi')){
			  $('.payzap-content').hide();
			$('.stored-card').hide();
			$('.cc-emi').show();
            $('.total_amount').addClass('card-payment');
            $('.card_submit_btn, .c-card-content').show();
            alert("reset emi");
        }
        else{
			$('.cc-emi').hide();
            $('.payzap-content').hide();
			$('.stored-card').hide();
            $('.card_submit_btn, .c-card-content').hide();
            $('.card_submit_btn, .card-content').hide();
            $('.total_amount').removeClass('card-payment');
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