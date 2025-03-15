$(document).ready(function(){
///////////////////////////////NUMERICS///////////////////////////////////////
   $("#card-number1, #card-number, #card-year, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob,#card-cvc,#card-cvc1,#card-month,#card-month1,#card-year1").keydown(function (e) {
        // Allow: backspace, delete, tab, escape, enter and .
        if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110]) !== -1 ||
             // Allow: Ctrl+A, Command+A
            (e.keyCode === 65 && (e.ctrlKey === true || e.metaKey === true)) || 
             // Allow: home, end, left, right, down, up
            (e.keyCode >= 35 && e.keyCode <= 40)) {
                 // let it happen, don't do anything
                 return;
        }
        // Ensure that it is a number and stop the keypress
        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
            e.preventDefault();
        }
    });

    //////// Bharat /////////  -- 17-01
    
   $('.cdnumberOnly').on('keypress change blur', function () 
	{
        $(this).val(function (index, value) {

            return value.replace(/[^a-z0-9]+/gi, '').replace(/(.{4})/g, '$1-');
      });
    });
    
       $(function() 
    	{
      	  $('.txtOnly').keydown(function(e) {
      		 // alert("TEXT ONLY");
      	    if ( e.ctrlKey || e.altKey) 
      	    {
      	      e.preventDefault();
      	    }
      	    else 
      	    {
      	      var key = e.keyCode;
      	      if (!((key == 8) || (key == 9) || (key == 32) || (key == 46) || (key >= 35 && key <= 40) || (key >= 65 && key <= 90))) 
      	      {
      	        e.preventDefault();
      	      }
      	    }
      	  });
      	});
       
       
       
       $(function () {
           $( ".month" ).change(function() {
              var max = parseInt($(this).attr('max'));
              var min = parseInt($(this).attr('min'));
              if ($(this).val() > max)
              {
                  $(this).val(max);
              }
              else if ($(this).val() < min)
              {
                  $(this).val(min);
              }       
            }); 
        });


    $(function () {
           $( ".year" ).change(function() {
              var max = parseInt($(this).attr('max'));
              var min = parseInt($(this).attr('min'));
              if ($(this).val() > max)
              {
                  $(this).val(max);
              }
              else if ($(this).val() < min)
              {
                  $(this).val(min);
              }       
            }); 
        });



//////////////////////////////////////////////////////////////////////////////
/*    $('#card-number, #card-number1, #debit-card-no-1').keyup(function() {
   var foo = $(this).val().split("-").join(""); // remove hyphens
   if (foo.length > 0) {
     foo = foo.match(new RegExp('.{1,4}', 'g')).join("-");
   }
   $(this).val(foo);
 });*/

    
//////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
// Add active class to the current button (highlight it)
var header = document.getElementById("mobile-menu");
var btns = header.getElementsByClassName("nav-item");
for (var i = 0; i < btns.length; i++) {
  btns[i].addEventListener("click", function() {
    var current = document.getElementsByClassName("active");
    current[0].className = current[0].className.replace(" active", "");
    this.className += " active";
  });
}
//////////////////////////////////////////////////////////////////
 ////////
    
    
/* Modified 01/01/2019 */
	$( '#tab-menu' ).click(function() {
		
	    /* Reset all Input texts to '' */        
	   /* $('.tab-1').find('input:text').val('');
	    $('.tab-1').find('input:password').val('');
	    $('.tab-1').find('input:number').val('');*/
	    
	    $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');
	 
	    
        $('#cr-manage, #dr-manage, #emi-manage').show();
        $('#cr-done, #dr-done, #emi-done').hide();
        $('.inputGroup3, .inputGroup3 label, .inputGroup3 input:checked ~ label').show();
	    $('.icon-delete').hide();
		$('.visible').hide();
        
      /* if (indexed==0) { $('#cr-card-01').css('border', '1px solid #2075b8'); }  
        if (indexed==1) { $('#dr-card-01').css('border', '1px solid #2075b8'); } 
         if (indexed==4) { $('#dr-card-07').css('border', '1px solid #2075b8'); } 
		*/

    });
	
	
	
$('.r').click(function() 
{	
	$("#cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-6").hide();
	   if($('input[name=tab-menu]:checked').attr('id') == 'radio1')
		   {
		  
		   if ($('#cr-card-01').is(':visible')) 
		   {
		    	$('#cc-radio-0').prop('checked', true);
			    $("#cc-cvv-col-0,#cr-pay-01").show();
			    $('#cr-pay-02').hide();
			    $('#credit-card-info').hide();
			    
			    $('#addtl-cr-cards').css('display', 'none');
		         $('.hide-cards-cr').hide();
		         $('.two-more-cr').show();
		    }
		   
		   else if ($('#cr-card-02').is(':visible')) 
		    {
		    	$('#cc-radio-1').prop('checked', true);
			    $("#cc-cvv-col-1,#cr-pay-01").show();
			    $('#cr-pay-02').hide();
			    $('#credit-card-info').hide();
			    
			     $('#addtl-cr-cards').css('display', 'none');
		         $('.hide-cards-cr').hide();
		         $('.two-more-cr').show();
		    }
		   
		   else if ($('#cr-card-03').is(':visible') && $("#cc-radio-2").prop("checked")) 
		    {
				   $("#cc-cvv-col-2").show();
		    }
		   
		   else if ($('#cr-card-04').is(':visible') && $("#cc-radio-3").prop("checked")) 
		    {
				   $("#cc-cvv-col-3").show();
		    }
		   
		   else if ($('#cr-card-05').is(':visible') && $("#cc-radio-4").prop("checked")) 
		    {
				   $("#cc-cvv-col-4").show();
		    }
		   
		   else if ($('#cr-card-06').is(':visible') && $("#cc-radio-5").prop("checked")) 
		    {
				   $("#cc-cvv-col-5").show();
		    }
		   
		   /*  else if ($('#cr-card-03').is(':visible')) 
		    {
		    	$('#cc-radio-2').prop('checked', true);
		    	$("#cc-cvv-col-2").show();
			    //$("#cc-cvv-col-2 , #cr-pay-01").show();
			    // $('#cr-pay-02').hide();
			    // $('#credit-card-info').hide();
			    
			   // $('#addtl-cr-cards').css('display', 'block');
		       //  $('.hide-cards-cr').show();
		       //  $('.two-more-cr').hide();
		    }
		 	   else if ($('#cr-card-04').is(':visible')) 
		    {
		    	$('#cc-radio-3').prop('checked', true);
			    $("#cc-cvv-col-3").show();
			    // $('#credit-card-info').hide();
		    }
		   else if ($('#cr-card-05').is(':visible')) 
		    {
		    	$('#cc-radio-4').prop('checked', true);
			    $("#cc-cvv-col-4").show();
			   // $('#credit-card-info').hide(); 
		    }
		   else if ($('#cr-card-06').is(':visible')) 
		    {
		    	$('#cc-radio-5').prop('checked', true);
			    $("#cc-cvv-col-5").show();
			    // $('#credit-card-info').hide();
		    }
		   */
		   }
	   
	   else if($('input[name=tab-menu]:checked').attr('id') == 'radio2')
	   {
		   
		   if ($('#dr-card-01').is(':visible')) 
		   {
		    	$('#dc-radio-0').prop('checked', true);
		    	 $("#dc-cvv-col-0,#dr-pay-01").show();
		    	 $('#dr-pay-02').hide();
		    	 $('#debit-card-info').hide();
		    	 
		    	 $('#addtl-dr-cards').css('display', 'none');
		         $('.hide-cards-dr').hide();
		         $('.two-more-dr').show();
		    }
		   
		   else if ($('#dr-card-02').is(':visible')) 
		    {
			    $('#dc-radio-1').prop('checked', true);
			    $("#dc-cvv-col-1,#dr-pay-01").show();
			    $('#dr-pay-02').hide();
			    $('#debit-card-info').hide();
			    
			    $('#addtl-dr-cards').css('display', 'none');
		         $('.hide-cards-dr').hide();
		         $('.two-more-dr').show();
		    }   
		   
		   else if ($('#dr-card-03').is(':visible') && $("#dc-radio-2").prop("checked")) 
		    {
				   $("#dc-cvv-col-2").show();
		    }
		   
		   else if ($('#dr-card-04').is(':visible') && $("#dc-radio-3").prop("checked")) 
		    {
				   $("#dc-cvv-col-3").show();
		    }
		   else if ($('#dr-card-05').is(':visible') && $("#dc-radio-4").prop("checked")) 
		    {
				   $("#dc-cvv-col-4").show();
		    }
		   
		   else if ($('#dr-card-06').is(':visible') && $("#dc-radio-5").prop("checked")) 
		    {
				   $("#dc-cvv-col-5").show();
		    }
		   
		  /* else if ($('#dr-card-03').is(':visible')) 
		    {
			    $('#dc-radio-2').prop('checked', true);
			    $("#dc-cvv-col-2").show();
			    // $('#debit-card-info').hide(); 
		    }   
		   
		   else if ($('#dr-card-04').is(':visible')) 
		    {
			    $('#dc-radio-3').prop('checked', true);
			    $("#dc-cvv-col-3").show();
			  // $('#debit-card-info').hide(); 
		    }   
		   
		   else if ($('#dr-card-05').is(':visible')) 
		    {
			    $('#dc-radio-4').prop('checked', true);
			    $("#dc-cvv-col-4").show();
			  //$('#debit-card-info').hide(); 
		    } 
		   else if ($('#dr-card-06').is(':visible')) 
		    {
			    $('#dc-radio-5').prop('checked', true);
			    $("#dc-cvv-col-5").show();
			  //$('#debit-card-info').hide(); 
		    } */
	   }
	   
    });
	
	$('#mobile-menu').click(function() 
	{
		
	    $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob , #card-month,#card-year,#card-month1,#card-year1").val('');
	
	  /*  $('#cr-manage, #dr-manage, #emi-manage').show();
        $('#cr-done, #dr-done, #emi-done').hide();
        $('.inputGroup3 label, .inputGroup3 input:checked ~ label').show();
	    $('.icon-delete').hide();*/
    

    });
	
	
	
	
	$( "#radio1, #radio2, #radio3, #radio4, #radio5, #radio6" ).click(function() 
	{
		 $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');
	 });
    
    $( '.nav-item' ).click(function() {
        $('#cr-manage, #dr-manage, #emi-manage').show();
        $('#cr-done, #dr-done, #emi-done').hide();
        $('.inputGroup3, .inputGroup3 label, .inputGroup3 input:checked ~ label').show();
	    $('.icon-delete').hide();
		$('.visible').hide();
     });
        
//////////////////////////////////////////////////////////////////////////// Credit Card Functions

   $('#cr-card-01').addClass('resp-tab-active');
    $('#cr-card-01').click(function (e) {   
        $('#cc-radio-0').prop('checked', true);
        $('#credit-card-info, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3, #cc-cvv-col-4, #cc-cvv-col-5 , #cr-pay-02 , #cvv-cr-col-11, #cvv-cr-col-12').hide();
        $('#credit-card-info, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3, #cc-cvv-col-4, #cc-cvv-col-5 , #cr-pay-02 , #cvv-cr-col-11, #cvv-cr-col-12').val('');
	 	$('#cc-cvv-col-0, #cr-pay-01').show();
        $('#cr-card-01').addClass('resp-tab-active');
        $('#cr-card-02,#cr-card-03, #cr-card-04, #cr-card-05, #cr-card-06, #cr-card-new').removeClass('resp-tab-active');
        
        $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');
        e.preventDefault();
	});
    $('#cr-card-02').click(function (e) {   
          $('#cc-radio-1').prop('checked', true);
          $('#credit-card-info, #cc-cvv-col-0, #cc-cvv-col-2, #cc-cvv-col-3, #cc-cvv-col-4, #cc-cvv-col-5 , #cr-pay-02, #cvv-cr-col-11, #cvv-cr-col-12').hide();
          $('#credit-card-info, #cc-cvv-col-0, #cc-cvv-col-2, #cc-cvv-col-3, #cc-cvv-col-4, #cc-cvv-col-5 , #cr-pay-02, #cvv-cr-col-11, #cvv-cr-col-12').val('');
          $('#cc-cvv-col-1, #cr-pay-01').show();
          $('#cr-card-02').addClass('resp-tab-active');
          $('#cr-card-01,#cr-card-03,#cr-card-04,  #cr-card-05, #cr-card-06, #cr-card-new').removeClass('resp-tab-active');
         $('#cr-card-01').css('border', '');
         
         $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');
         e.preventDefault();
	});
    $('#cr-card-03').click(function (e) {   
          $('#cc-radio-2').prop('checked', true);
          $('#credit-card-info, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-3, #cc-cvv-col-4, #cc-cvv-col-5 , #cr-pay-02, #cvv-cr-col-12').hide();
          $('#credit-card-info, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-3, #cc-cvv-col-4, #cc-cvv-col-5 , #cr-pay-02, #cvv-cr-col-12').val('');
          $('#cc-cvv-col-2, #cr-pay-01').show();
          $('#cr-card-03').addClass('resp-tab-active');
          $('#cr-card-01, #cr-card-02, #cr-card-04, #cr-card-05, #cr-card-06, #cr-card-new').removeClass('resp-tab-active');
           
          $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');
          e.preventDefault();
	});
    $('#cr-card-04').click(function (e) {   
          $('#cc-radio-3').prop('checked', true);
          $('#credit-card-info, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-4, #cc-cvv-col-5 , #cr-pay-02, #cvv-cr-col-11').hide();
          $('#credit-card-info, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-4, #cc-cvv-col-5 , #cr-pay-02, #cvv-cr-col-11').val('');
          $('#cc-cvv-col-3, #cr-pay-01').show();
          $('#cr-card-04').addClass('resp-tab-active');
          $('#cr-card-01, #cr-card-02, #cr-card-03, #cr-card-05, #cr-card-06, #cr-card-new').removeClass('resp-tab-active');
          
          $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');
          e.preventDefault();
	});
    
    
    $('#cr-card-05').click(function (e) {   
        $('#cc-radio-4').prop('checked', true);
        $('#credit-card-info, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3, #cc-cvv-col-5 , #cr-pay-02 , #cvv-cr-col-11').hide();
        $('#credit-card-info, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3, #cc-cvv-col-5 , #cr-pay-02 , #cvv-cr-col-11').val('');
        $('#cc-cvv-col-4, #cr-pay-01').show();
        $('#cr-card-05').addClass('resp-tab-active');
        $('#cr-card-01, #cr-card-02, #cr-card-03, #cr-card-04 ,#cr-card-06 ,#cr-card-new').removeClass('resp-tab-active');
        
        $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');
        e.preventDefault();
	});
    
    $('#cr-card-06').click(function (e) {   
        $('#cc-radio-5').prop('checked', true);
        $('#credit-card-info, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3, #cc-cvv-col-4 , #cr-pay-02 , #cvv-cr-col-11').hide();
        $('#credit-card-info, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3, #cc-cvv-col-4 , #cr-pay-02 , #cvv-cr-col-11').val('');
        $('#cc-cvv-col-5, #cr-pay-01').show();
        $('#cr-card-06').addClass('resp-tab-active');
        $('#cr-card-01, #cr-card-02, #cr-card-03,#cr-card-04, #cr-card-05, #cr-card-new').removeClass('resp-tab-active');
        
        $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');
        e.preventDefault();
	});
    
    
    $('#cr-card-new').click(function (e) {  
        tt = $(this).offset().top;
        $('html, body').animate({scrollTop:tt}, 'slow');
        $('#radio13').prop('checked', true);
		$('#credit-card-info').show();
		$('#cr-pay-02').css('display', 'table');
    $('#cr-card-01').css('border', '');
        $('#cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3, #cc-cvv-col-4 , #cc-cvv-col-5, #cr-pay-01, #cvv-cr-col-11, #cvv-cr-col-12').hide();
        $('#cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3, #cc-cvv-col-4 , #cc-cvv-col-5, #cr-pay-01, #cvv-cr-col-11, #cvv-cr-col-12').val('');
        $('#cr-card-new').addClass('resp-tab-active');
        $('#cr-card-01, #cr-card-02, #cr-card-03, #cr-card-04, #cr-card-05, #cr-card-06').removeClass('resp-tab-active');
        // bharat
       /* $('#addtl-cr-cards').css('display', 'none');
          $('.hide-cards-cr').hide();
          $('.two-more-cr').show();
        */
       
        $("#card-number1, #card-holder1, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');
        e.preventDefault();
	});

        
/* Debit Card functions */

$('#dr-card-01').addClass('resp-tab-active');
   $('#dr-card-01').click(function (e) {    
        $('#dc-radio-0').prop('checked', true);
        $('#debit-card-info,#dc-cvv-col-1, #dc-cvv-col-2,#dc-cvv-col-3, #dc-cvv-col-4, #dc-cvv-col-5 , #dr-pay-02 , #cvv-dr-col-13, #cvv-dr-col-14').hide();
        $('#debit-card-info,#dc-cvv-col-1, #dc-cvv-col-2,#dc-cvv-col-3, #dc-cvv-col-4, #dc-cvv-col-5 , #dr-pay-02 , #cvv-dr-col-13, #cvv-dr-col-14').val('');
	   	$('#dc-cvv-col-0, #dr-pay-01').show();
        $('#dr-card-01').addClass('resp-tab-active');
        $('#dr-card-02, #dr-card-03, #dr-card-04, #dr-card-05, #dr-card-06, #dr-card-new').removeClass('resp-tab-active');
         
        $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');
        e.preventDefault();
	});
    $('#dr-card-02').click(function (e) {    
        $('#dc-radio-1').prop('checked', true);
        $('#debit-card-info, #dc-cvv-col-0, #dc-cvv-col-2,#dc-cvv-col-3, #dc-cvv-col-4, #dc-cvv-col-5 , #dr-pay-02 , #cvv-dr-col-13, #cvv-dr-col-14').hide();
        $('#debit-card-info, #dc-cvv-col-0, #dc-cvv-col-2,#dc-cvv-col-3, #dc-cvv-col-4, #dc-cvv-col-5 , #dr-pay-02 , #cvv-dr-col-13, #cvv-dr-col-14').val('');
        $('#dc-cvv-col-1, #dr-pay-01').show();
        $('#dr-card-02').addClass('resp-tab-active');
       $('#dr-card-01, #dr-card-03, #dr-card-04, #dr-card-05, #dr-card-06, #dr-card-new').removeClass('resp-tab-active');
         $('#dr-card-01').css('border', '');
         
         $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');
         e.preventDefault();
	});

     $('#dr-card-03').click(function (e) {    
        $('#dc-radio-2').prop('checked', true);
        $('#debit-card-info, #dc-cvv-col-0, #dc-cvv-col-1,#dc-cvv-col-3, #dc-cvv-col-4, #dc-cvv-col-5 , #dr-pay-02 , #cvv-dr-col-14 ').hide();
        $('#debit-card-info, #dc-cvv-col-0, #dc-cvv-col-1,#dc-cvv-col-3, #dc-cvv-col-4, #dc-cvv-col-5 , #dr-pay-02 , #cvv-dr-col-14 ').val('');
        $('#dc-cvv-col-2, #dr-pay-01').show();
        $('#dr-card-03').addClass('resp-tab-active');
        $('#dr-card-01, #dr-card-02, #dr-card-04, #dr-card-05, #dr-card-06, #dr-card-new').removeClass('resp-tab-active');
         
        $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');
        e.preventDefault();
	});
    
    $('#dr-card-04').click(function (e) 
    {    
        $('#dc-radio-3').prop('checked', true);
        $('#debit-card-info,  #dc-cvv-col-0, #dc-cvv-col-1,#dc-cvv-col-2, #dc-cvv-col-4, #dc-cvv-col-5 , #dr-pay-02 , #cvv-dr-col-13 ').hide();
        $('#debit-card-info,  #dc-cvv-col-0, #dc-cvv-col-1,#dc-cvv-col-2, #dc-cvv-col-4, #dc-cvv-col-5 , #dr-pay-02 , #cvv-dr-col-13 ').val('');
        $('#dc-cvv-col-3, #dr-pay-01').show();
        $('#dr-card-04').addClass('resp-tab-active');
        $('#dr-card-01, #dr-card-02, #dr-card-03, #dr-card-05, #dr-card-06, #dr-card-new').removeClass('resp-tab-active');
        
        $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');
        e.preventDefault();
	});
    
    
    $('#dr-card-05').click(function (e) 
    {    
        $('#dc-radio-4').prop('checked', true);
        $('#debit-card-info,  #dc-cvv-col-0, #dc-cvv-col-1,#dc-cvv-col-2, #dc-cvv-col-3, #dc-cvv-col-5 , #dr-pay-02 , #cvv-dr-col-13 ').hide();
        $('#debit-card-info,  #dc-cvv-col-0, #dc-cvv-col-1,#dc-cvv-col-2, #dc-cvv-col-3, #dc-cvv-col-5 , #dr-pay-02 , #cvv-dr-col-13 ').val('');
        $('#dc-cvv-col-4, #dr-pay-01').show();
        $('#dr-card-05').addClass('resp-tab-active');
        $('#dr-card-01, #dr-card-02, #dr-card-03, #dr-card-04, #dr-card-06, #dr-card-new').removeClass('resp-tab-active');
        
        $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');
        e.preventDefault();
	});
    
    $('#dr-card-06').click(function (e) 
    {    
        $('#dc-radio-5').prop('checked', true);
        $('#debit-card-info,  #dc-cvv-col-0, #dc-cvv-col-1,#dc-cvv-col-2, #dc-cvv-col-3, #dc-cvv-col-4 , #dr-pay-02 , #cvv-dr-col-13 ').hide();
        $('#debit-card-info,  #dc-cvv-col-0, #dc-cvv-col-1,#dc-cvv-col-2, #dc-cvv-col-3, #dc-cvv-col-4 , #dr-pay-02 , #cvv-dr-col-13 ').val('');
        $('#dc-cvv-col-5, #dr-pay-01').show();
        $('#dr-card-06').addClass('resp-tab-active');
        $('#dr-card-01, #dr-card-02, #dr-card-03, #dr-card-04, #dr-card-05, #dr-card-new').removeClass('resp-tab-active');
        
        $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob, #card-month,#card-year,#card-month1,#card-year1").val('');
        e.preventDefault();
	});
    
    
     
     
    $('#dr-card-new').click(function (e) {    
        tt = $(this).offset().top;
        $('html, body').animate({scrollTop:tt}, 'slow');
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
	});
    
   
    
    
    /* EMI  functions */
   $('#dr-card-07').click(function (e) {    
        $('#radio17').prop('checked', true);
        $('#debit-card-info-1, #cvv-col-7, #cvv-dr-col-15,  #cvv-dr-col-16').hide();
	   	$('#cvv-col-6').show();
        $('#dr-card-07').addClass('resp-tab-active');
        $('#dr-card-08, #dr-card-09, #dr-card-10, #dr-card-new-emi').removeClass('resp-tab-active');
        
        $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob").val('');
          e.preventDefault();
	});
    
    $('#dr-card-08').click(function (e) {  
        $('#radio18').prop('checked', true);
        $('#debit-card-info-1, #cvv-col-6, #cvv-dr-col-15,  #cvv-dr-col-16').hide();
        $('#cvv-col-7').show();
        $('#dr-card-08').addClass('resp-tab-active');
        $('#dr-card-07, #dr-card-09, #dr-card-10, #dr-card-new-emi').removeClass('resp-tab-active');
         $('#dr-card-07').css('border', '');       
         $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob").val('');
          e.preventDefault();
	});
    
     $('#dr-card-09').click(function (e) {    
        $('#radio35').prop('checked', true);
        $('#debit-card-info-1, #cvv-col-6, #cvv-col-7,  #cvv-dr-col-16').hide();
        $('#cvv-dr-col-15').show();
        $('#dr-card-09').addClass('resp-tab-active');
        $('#dr-card-07, #dr-card-08, #dr-card-10, #dr-card-new-emi').removeClass('resp-tab-active');
        
        $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob").val('');
        e.preventDefault();
	});
    
     $('#dr-card-10').click(function (e) {    
        $('#radio36').prop('checked', true);
       $('#debit-card-info-1, #cvv-col-6, #cvv-col-7,  #cvv-dr-col-15').hide();
        $('#cvv-dr-col-16').show();
        $('#dr-card-10').addClass('resp-tab-active');
        $('#dr-card-07, #dr-card-08, #dr-card-09, #dr-card-new-emi').removeClass('resp-tab-active');
        
        $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob").val('');
        e.preventDefault();
	});
    

    $('#dr-card-new-emi').click(function (e) { 
          tt = $(this).offset().top;
        $('html, body').animate({scrollTop:tt}, 'slow');
       $('#radio19').prop('checked', true);
        $('#emi-pay-02').css('display', 'table');
       $('#cr-card-01').css('border', '');
       $('#cvv-col-6, #cvv-col-7, #cvv-dr-col-15,  #cvv-dr-col-16, #emi-pay-01').hide();
        $('#debit-card-info-1').show();
        $('#dr-card-new-emi').addClass('resp-tab-active');
        $('#dr-card-07, #dr-card-08, #dr-card-09, #dr-card-10').removeClass('resp-tab-active');
        
        $("#card-number1, #card-number, #card-holder, #card-holder1, #card-cvc, #card-cvc1, #cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cr-card-no, #dr-card-no, #dr-card-no-1,#dc-cvv-mob,#cc-cvv-mob").val('');
           e.preventDefault();
	});
    
       /* Wallet  functions */
    
    $('#wallet-01').click(function (e) {    
        $('#radio25').prop('checked', true);
        $('#wallet-01').addClass('resp-tab-active');
        $('#wallet-02, #wallet-03, #wallet-04, #wallet-05, #wallet-06').removeClass('resp-tab-active');
           e.preventDefault();
	});
     $('#wallet-02').click(function (e) {    
        $('#radio26').prop('checked', true);
        $('#wallet-02').addClass('resp-tab-active');
        $('#wallet-01, #wallet-03, #wallet-04, #wallet-05, #wallet-06').removeClass('resp-tab-active');
            e.preventDefault();
	});
     $('#wallet-03').click(function (e) {    
        $('#radio27').prop('checked', true);
        $('#wallet-03').addClass('resp-tab-active');
        $('#wallet-01, #wallet-02, #wallet-04, #wallet-05, #wallet-06').removeClass('resp-tab-active');
            e.preventDefault();
	});
     $('#wallet-04').click(function (e) {    
        $('#radio28').prop('checked', true);
        $('#wallet-04').addClass('resp-tab-active');
        $('#wallet-01, #wallet-02, #wallet-03, #wallet-05, #wallet-06').removeClass('resp-tab-active');
            e.preventDefault();
	});
     $('#wallet-05').click(function (e) {    
        $('#radio29').prop('checked', true);
         $('#wallet-05').addClass('resp-tab-active');
        $('#wallet-01, #wallet-02, #wallet-03, #wallet-04, #wallet-06').removeClass('resp-tab-active');
            e.preventDefault();
	});
     $('#wallet-06').click(function (e) {    
        $('#radio30').prop('checked', true);
        $('#wallet-06').addClass('resp-tab-active');
        $('#wallet-01, #wallet-02, #wallet-03, #wallet-04, #wallet-05').removeClass('resp-tab-active');
        e.preventDefault();
	});
    
    /*  MANAGE CARD SECTION */
    
    $('.icon-delete').css('display', 'none');
    /* Cards Manage & Done  functions */
    $('#cr-manage, #dr-manage, #emi-manage').click(function (e) {    
       // $(this).toggle();
		// $('#cr-done, #dr-done, #emi-done').toggle();
    	$('#cr-done, #dr-done, #emi-done').show();
    	$('#cr-manage, #dr-manage, #emi-manage').hide();
        $('.inputGroup3, .inputGroup3 label, .inputGroup3 input:checked ~ label, #cvv-col-0, #cvv-col-3, #cvv-col-6').hide();
        $('.icon-delete').show();
		$('.visible').show();
        e.preventDefault();
	});

    $('#cr-done, #dr-done, #emi-done').click(function (e) {
       // $(this).toggle();
		// $('#cr-manage, #dr-manage, #emi-manage').toggle();
    	$('#cr-manage, #dr-manage, #emi-manage').show();
    	$('#cr-done, #dr-done, #emi-done').hide();
        $('.inputGroup3, .inputGroup3 label, .inputGroup3 input:checked ~ label, #cvv-col-0, #cvv-col-3, #cvv-col-6').show();
	    $('.icon-delete').hide();
		$('.visible').hide();
        e.preventDefault();
	});
    
    $('.two-more-cr').click(function (e) {
        $('#addtl-cr-cards').css('display', 'block');
        $('.hide-cards-cr').toggle();
        $('.two-more-cr').toggle();
          e.preventDefault();
    });
    $('.hide-cards-cr').click(function (e) {
        $('#addtl-cr-cards').css('display', 'none');
        $('.hide-cards-cr').toggle();
        $('.two-more-cr').toggle();
       e.preventDefault();
    });
    $('.two-more-dr').click(function (e) {
        $('#addtl-dr-cards').css('display', 'block');
        $('.hide-cards-dr').toggle();
        $('.two-more-dr').toggle();
          e.preventDefault();
    });
    $('.hide-cards-dr').click(function (e) {
        $('#addtl-dr-cards').css('display', 'none');
        $('.hide-cards-dr').toggle();
        $('.two-more-dr').toggle();
       e.preventDefault();
    });
    
    $('.two-more-emi').click(function (e) {
        $('#addtl-emi-dr-cards').css('display', 'block');
        $('.hide-cards-emi').toggle();
        $('.two-more-emi').toggle();
          e.preventDefault();
    });
    $('.hide-cards-emi').click(function (e) {
        $('#addtl-emi-dr-cards').css('display', 'none');
        $('.hide-cards-emi').toggle();
        $('.two-more-emi').toggle();
       e.preventDefault();
    });
 
    /*  MANAGE CARD SECTION */
    
   /* $('icon-delete').click(function (e) { 
           $(this).toggle();
        $('#cvv-col-0, #cvv-col-3, #cvv-col-6').css('display', 'none ');
        e.preventDefault();
	});*/
    
    $('.icon-delete').click(function (e) 
    { 
//      $(this).toggle();
    	
       $('#cc-cvv-col-0, #cc-cvv-col-1, #cc-cvv-col-2, #cc-cvv-col-3,#cc-cvv-col-4, #cc-cvv-col-5, #cc-cvv-col-5, #cc-cvv-col-6, #dc-cvv-col-0, #dc-cvv-col-1, #dc-cvv-col-2, #dc-cvv-col-3,#dc-cvv-col-4, #dc-cvv-col-5, #dc-cvv-col-5, #dc-cvv-col-6, #cvv-col-6, #cvv-col-7, #cvv-col-8, #cvv-cr-col-11, #cvv-cr-col-12, #cvv-dr-col-13, #cvv-dr-col-14, #cvv-dr-col-15, #cvv-dr-col-16, #dr-card-no-1, #cr-crd-cvv,  #dr-crd-cvv').css('display', 'none ');
      $('#cvv-col-0, #cvv-col-3, #cvv-col-6').css('display', 'none ');
      e.preventDefault();
	});
    
	
});	
