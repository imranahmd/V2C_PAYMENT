/**
 *  Bharat
 */

/*    IS Remember for CC and DC  Start  */

  $("#isDCRemember").click(function ()
    		{
    			$(this).prop("checked") ? $(this).val("Y") : $(this).val("N");
    		});
    					
     $("#isCCRemember").click(function ()
			{
				$(this).prop("checked") ? $(this).val("Y") : $(this).val("N");
			});	
     
     $("#isCCCRemember").click(function ()
 			{
 				$(this).prop("checked") ? $(this).val("Y") : $(this).val("N");
 			});	
     
     /*    IS Remember for CC and DC  END  */  
     
     
     
   /*   Net Banking Top 6 Bank for select Start */  
     
     $('.radio-group .selectBank').click(function(){
    	 
 	    $(this).parent().find('.selectBank').removeClass('selected');
 	    $(this).addClass('selected');
 	    var val = $(this).attr('data-value');
 	   // alert("val = "+val);
 	     // document.getElementById("bank-select").value = val; 
 	    $('#bank-select').val(val);
 	});
     
     /*   Net Banking Top 6 Bank for select END */ 
     /*  allow only Text for CardHolderName Start	*/
     
     $(function() {
      	  $('.txtOnly').keydown(function(e) {
      	    if ( e.ctrlKey || e.altKey) {
      	      e.preventDefault();
      	    } else {
      	      var key = e.keyCode;
      	      if (!((key == 8) || (key == 9) || (key == 32) || (key == 46) || (key >= 35 && key <= 40) || (key >= 65 && key <= 90))) {
      	        e.preventDefault();
      	      }
      	    }
      	  });
      	});



/*  allow only Text for CardHolderName Start	*/

 /*    check selected value for stored card Option list  Start  */ 
function CheckSelectValue()
	{
			// var cvv_len = document.getElementById("card-cvc3").value.length;
			// alert("Cvv Length ::: "+cvv_len);
		 if(document.getElementById("selectSC").selectedIndex == 0 || document.getElementById("selectSC").value == "")
        	{
        		document.getElementById("selectSC").focus();
        		valueBackground("selectSC");
        		return false;      			
        	}
        	
		  else if(document.getElementById("card-cvc3").value.length < 3 )
     	{
     		document.getElementById("card-cvc3").focus();
     		return false;      			
     	} 
		 
        	else
        		{
        		// alert("Submit");
        		document.forms[0].submit();
        		 return true;
        		}
		}

/*    check selected value for stored card Option list END    */ 

    
 function appendMMYYSC(cardFieldName) {
		 // alert("Hello");
	 // var mm = document.getElementsByName("card-month")[0].value;
	//  var yy = document.getElementsByName("card-year")[0].value;
	
	var abc = document.getElementById("card-month3").value;
	var xyz = document.getElementById("card-year3").value;
	
	// alert("Expiry :: "+abc+"/"+xyz);
	 document.getElementById("SCExpiry").value = abc+"/"+xyz;
	//  document.getElementByName("expiry").value = abc+"/"+xyz;
	 // alert("Hi set a value ::: "+document.getElementById("SCExpiry").value);
	 // document.getElementsByName("expiry")[0].value = abc+"/"+xyz;
		 return true;
 }
 
 
 
     
 function testCreditCard(cardFieldName) {
 
		 var ccmm = document.getElementsByName("card-month")[0].value;
		 var ccyy = document.getElementsByName("card-year")[0].value;
		 
		 document.getElementsByName("expiry")[0].value = ccmm+"/"+ccyy; 
		 var ex = document.getElementsByName("expiry")[0].value;
		 
		 var dcmm = document.getElementById("card-month1").value;
		 var dcyy = document.getElementById("card-year1").value;

		// alert("Expiry :: "+abc+"/"+xyz);
		document.getElementById("DCExpiry").value = dcmm+"/"+dcyy;
		 
		  myCardNo = document.getElementById(cardFieldName).value;
		  if(myCardNo.startsWith('34') || myCardNo.startsWith('37')){
		  myCardType='AmEx';
		  }
  
		  else if(myCardNo.startsWith('6011') || myCardNo.startsWith('622') || myCardNo.startsWith('64') || myCardNo.startsWith('65')){
		  myCardType='Discover';
		  }
  
		  else if(myCardNo.startsWith('36') || myCardNo.startsWith('38')){
		  myCardType='DinersClub';
		  }
  
		  else if(myCardNo.startsWith('51') || myCardNo.startsWith('52') || myCardNo.startsWith('53') || myCardNo.startsWith('54') || myCardNo.startsWith('55')){
		  myCardType='MasterCard';
		  }
  														
		  else if(myCardNo.startsWith('5018') || myCardNo.startsWith('5020') || myCardNo.startsWith('5038') || myCardNo.startsWith('6304') || myCardNo.startsWith('6759') || myCardNo.startsWith('6761') || myCardNo.startsWith('6762') || myCardNo.startsWith('6763') ){
		    myCardType='Maestro';
		  }
		  else if(myCardNo.startsWith('4')){
		  myCardType='Visa'; 
		  }
		 
		  else if(myCardNo.startsWith('606') || myCardNo.startsWith('607') || myCardNo.startsWith('608') ){
			    myCardType='Rupay';
			  }
		  else{
			    myCardType='Unknow';
			  }
		  
		  if (checkCreditCard(myCardNo, myCardType)) {
		  //  alert("Credit card has a valid format")
		    return true;
		  } 
		  else {
		    alert(ccErrors[ccErrorNo])
		    //$('#card-number').focus();
		    document.getElementById(cardFieldName).focus();
		    return false;
		  };
}
    
    
  
     
     
    