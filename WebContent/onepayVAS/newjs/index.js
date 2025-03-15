
function openpage(evt, cityName) {
	
	// alert("EVT  "+evt+"   cityName "+cityName);
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace("active", "");
    }
    document.getElementById(cityName).style.display = "block";
    evt.currentTarget.className += " active";
    
   /* adding by Bharat */
   /* $("#dcForm")[0].reset();
	$("#ccForm")[0].reset();
	$("#nbForm")[0].reset();
	$("#scForm")[0].reset();*/
    
    
    if(cityName == 'credit')
    	{
    	
    	$("#dcForm")[0].reset();
		$("#nbForm")[0].reset();
		$("#scForm")[0].reset();
    	}
    else if(cityName == 'debit')
    	{
    	
		$("#ccForm")[0].reset();
		$("#nbForm")[0].reset();
		$("#scForm")[0].reset();
    	}
    else if(cityName == 'banking')
    	{
    	
    	$("#dcForm")[0].reset();
		$("#ccForm")[0].reset();
		$("#scForm")[0].reset();
    	}
    
    else if(cityName == 'home')
	{
	
	$("#dcForm")[0].reset();
	$("#ccForm")[0].reset();
	$("#nbForm")[0].reset();
	/*$("#scForm")[0].reset();*/
	}
    
    else
    	{
    	$("#dcForm")[0].reset();
		$("#ccForm")[0].reset();
		$("#nbForm")[0].reset();
		$("#scForm")[0].reset();
    	}
}



$(document).ready(function () {

    Stripe.setPublishableKey('pk_test_9D43kM3d2vEHZYzPzwAblYXl');

    var cardNumber, cardMonth, cardYear, cardCVC, cardHolder;

    // check for any empty inputs
    function findEmpty() {
        var emptyText = $('#form-container input').filter(function () {

            return $(this).val == null;
        });

        // add invalid class to empty inputs
        console.log(emptyText.prevObject);
        emptyText.prevObject.addClass('invalid');
    }

    // check for card type and display corresponding icon
    function checkCardType() {
        

        cardNumber1 = $('#card-number').val();

        cardNumber = cardNumber1.replace(/\s+/g, '');

        var cardType = Stripe.card.cardType(cardNumber);
        
       // alert("CardType For Logo : "+cardType);   // BHARAT
        
        console.log(cardType);
        switch (cardType) {
            case 'Visa':
                $('#card-image').html('<img src="js/visa.svg" height="100%">');
                $("#card-back").css("background", "linear-gradient(to bottom, rgba(252, 190, 27, 1) 1%, rgba(248, 86, 72, 1) 99%)");
                 $("#card-front").css("background", "linear-gradient(to bottom, rgba(252, 190, 27, 1) 1%, rgba(248, 86, 72, 1) 99%)");
                
                break;

            case 'MasterCard':
                $('#card-image').html('<img src="js/master.svg" height="100%">');
                $("#card-back").css("background", "linear-gradient(to bottom, rgba(183,71,247,1) 0%,rgba(108,83,220,1) 100%)");
                 $("#card-front").css("background", "linear-gradient(to bottom, rgba(183,71,247,1) 0%,rgba(108,83,220,1) 100%)");
                
                break;
            case 'Discover':
                $('#card-image').html('<img src="js/discover.svg" height="100%">');
                $("#card-back").css("background", "-webkit-linear-gradient(top, rgba(255, 86, 65, 1) 0%, rgba(253, 50, 97, 1) 100%)");
                 $("#card-front").css("background", "-webkit-linear-gradient(top, rgba(255, 86, 65, 1) 0%, rgba(253, 50, 97, 1) 100%)");
                
                break;

            case 'American Express':
                $('#card-image').html('<img src="js/american.svg" height="100%">');
                 $("#card-back").css("background", "linear-gradient(to bottom, rgb(25, 88, 224) 0%, rgba(253, 50, 97, 0.58) 100%)");
                 $("#card-front").css("background", "linear-gradient(to bottom, rgb(25, 88, 224) 0%, rgba(253, 50, 97, 0.58) 100%)");
                
                break;
             case 'Diners Club':
                $('#card-image').html('<img src="js/dinner.svg" height="100%">');
                break;
                // bharat  Discover
             case 'Rupay':
                 $('#card-image').html('<img src="js/rupay.svg" height="100%">');
                 break;

            case 'Unknown':
                $('#card-image').html('<img src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiA/PjxzdmcgZGF0YS1uYW1lPSJMYXllciAxIiBpZD0iTGF5ZXJfMSIgdmlld0JveD0iMCAwIDYwIDYwIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIj48ZGVmcz48c3R5bGU+LmNscy0xLC5jbHMtMTAsLmNscy0xMSwuY2xzLTIsLmNscy02LC5jbHMtN3tmaWxsOm5vbmU7fS5jbHMtMXtjbGlwLXJ1bGU6ZXZlbm9kZDt9LmNscy0yLC5jbHMtNXtmaWxsLXJ1bGU6ZXZlbm9kZDt9LmNscy0ze2NsaXAtcGF0aDp1cmwoI2NsaXAtcGF0aCk7fS5jbHMtNHtjbGlwLXBhdGg6dXJsKCNjbGlwLXBhdGgtMik7fS5jbHMtNXtmaWxsOiNmZTg2NTc7fS5jbHMtMTAsLmNscy0xMSwuY2xzLTZ7c3Ryb2tlOiNmZTg2NTc7fS5jbHMtMTAsLmNscy02e3N0cm9rZS1saW5lY2FwOnJvdW5kO30uY2xzLTEwLC5jbHMtMTEsLmNscy02LC5jbHMtN3tzdHJva2UtbGluZWpvaW46cm91bmQ7fS5jbHMtNntzdHJva2Utd2lkdGg6NHB4O30uY2xzLTd7c3Ryb2tlOiNlMDZjM2U7fS5jbHMtMTEsLmNscy03e3N0cm9rZS1saW5lY2FwOnNxdWFyZTt9LmNscy0xMCwuY2xzLTExLC5jbHMtN3tzdHJva2Utd2lkdGg6MnB4O30uY2xzLTh7Y2xpcC1wYXRoOnVybCgjY2xpcC1wYXRoLTQpO30uY2xzLTl7ZmlsbDojZmZkYzgyO308L3N0eWxlPjxjbGlwUGF0aCBpZD0iY2xpcC1wYXRoIj48cGF0aCBjbGFzcz0iY2xzLTEiIGQ9Ik0xLDQ2VjE4YTUsNSwwLDAsMSw1LTVINTRhNSw1LDAsMCwxLDUsNVY0NmE1LDUsMCwwLDEtNSw1SDZBNSw1LDAsMCwxLDEsNDZabTIsMGEzLDMsMCwwLDAsMywzSDU0YTMsMywwLDAsMCwzLTNWMThhMywzLDAsMCwwLTMtM0g2YTMsMywwLDAsMC0zLDNWNDZaTS0xOSw3MUg3OVYtN0gtMTlWNzFaIi8+PC9jbGlwUGF0aD48Y2xpcFBhdGggaWQ9ImNsaXAtcGF0aC0yIj48cGF0aCBjbGFzcz0iY2xzLTEiIGQ9Ik0yLDQ2YTQsNCwwLDAsMCw0LDRINTRhNCw0LDAsMCwwLDQtNFYxOGE0LDQsMCwwLDAtNC00SDZhNCw0LDAsMCwwLTQsNFY0NloiLz48L2NsaXBQYXRoPjxjbGlwUGF0aCBpZD0iY2xpcC1wYXRoLTQiPjxwYXRoIGNsYXNzPSJjbHMtMSIgZD0iTTksMjlhMiwyLDAsMCwwLDIsMkgyMmEyLDIsMCwwLDAsMi0yVjIzYTIsMiwwLDAsMC0yLTJIMTFhMiwyLDAsMCwwLTIsMnY2WiIvPjwvY2xpcFBhdGg+PC9kZWZzPjx0aXRsZS8+PGcgY2xhc3M9ImNscy0zIj48ZyBjbGFzcz0iY2xzLTQiPjxwYXRoIGNsYXNzPSJjbHMtNSIgZD0iTTIsNDZhNCw0LDAsMCwwLDQsNEg1NGE0LDQsMCwwLDAsNC00VjE4YTQsNCwwLDAsMC00LTRINmE0LDQsMCwwLDAtNCw0VjQ2WiIvPjwvZz48L2c+PGcgY2xhc3M9ImNscy00Ij48cGF0aCBjbGFzcz0iY2xzLTYiIGQ9Ik0yLDQ2YTQsNCwwLDAsMCw0LDRINTRhNCw0LDAsMCwwLDQtNFYxOGE0LDQsMCwwLDAtNC00SDZhNCw0LDAsMCwwLTQsNFY0NloiLz48L2c+PGxpbmUgY2xhc3M9ImNscy03IiB4MT0iOSIgeDI9IjI4IiB5MT0iMzkiIHkyPSIzOSIvPjxsaW5lIGNsYXNzPSJjbHMtNyIgeDE9IjMyIiB4Mj0iNTEiIHkxPSIzOSIgeTI9IjM5Ii8+PGcgY2xhc3M9ImNscy04Ij48cmVjdCBjbGFzcz0iY2xzLTkiIGhlaWdodD0iMjAiIHdpZHRoPSIyNSIgeD0iNCIgeT0iMTYiLz48L2c+PHBhdGggY2xhc3M9ImNscy0xMCIgZD0iTTksMjlhMiwyLDAsMCwwLDIsMkgyMmEyLDIsMCwwLDAsMi0yVjIzYTIsMiwwLDAsMC0yLTJIMTFhMiwyLDAsMCwwLTIsMnY2WiIvPjxsaW5lIGNsYXNzPSJjbHMtMTEiIHgxPSIxOCIgeDI9IjE4IiB5MT0iMzEiIHkyPSIyMSIvPjxsaW5lIGNsYXNzPSJjbHMtMTEiIHgxPSI5IiB4Mj0iMTciIHkxPSIyNyIgeTI9IjI3Ii8+PGxpbmUgY2xhc3M9ImNscy0xMSIgeDE9IjE4IiB4Mj0iMjQiIHkxPSIyNSIgeTI9IjI1Ii8+PC9zdmc+" height="55px">');
                break;
        }
    }

    // check card type on card number input blur 
    $('#card-number').blur(function (event) {
        event.preventDefault();
        checkCardType();
    });

    // on button click: 
    $('#card-btn').click(function (event) {

        // get each input value and use Stripe to determine whether they are valid
        var cardNumber = $('#card-number').val();
        var isValidNo = Stripe.card.validateCardNumber(cardNumber);
        var expMonth = $('#card-month').val();
        var expYear = $('#card-year').val();
        var isValidExpiry = Stripe.card.validateExpiry(expMonth, expYear);
        var cardCVC = $('#card-cvc').val();
        var isValidCVC = Stripe.card.validateCVC(cardCVC);
        var cardHolder = $('#card-holder').val();
        event.preventDefault();

        // alert the user if any fields are missing
        if (!cardNumber || !cardCVC || !cardHolder || !expMonth || !expYear) {
            console.log(cardNumber + cardCVC + cardHolder + cardMonth + cardYear);
            $('#form-errors').addClass('hidden');
            $('#card-success').addClass('hidden');
            $('#form-errors').removeClass('hidden');
            $('#card-error').text('Please complete all fields.');
            findEmpty();
        } else {

            // alert the user if any fields are invalid
            if (!isValidNo || !isValidExpiry || !isValidCVC) {
                $('#form-errors').css('display', 'block');
                if (!isValidNo) {
                    $('#card-error').text('Invalid credit card number.');
                } else if (!isValidExpiry) {
                    $('#card-error').text('Invalid expiration date.')
                } else if (!isValidCVC) {
                    $('#card-error').text('Invalid CVC code.')
                }

            } else {
                $('#card-success').removeClass('hidden');
            }
        }
    })

});

















$(document).ready(function () {

    Stripe.setPublishableKey('pk_test_9D43kM3d2vEHZYzPzwAblYXl');

    var cardNumber, cardMonth, cardYear, cardCVC, cardHolder;

    // check for any empty inputs
    function findEmpty() {
        var emptyText = $('#form-container1 input').filter(function () {

            return $(this).val == null;
        });

        // add invalid class to empty inputs
        console.log(emptyText.prevObject);
        emptyText.prevObject.addClass('invalid');
    }

    // check for card type and display corresponding icon
    function checkCardType() {
        
        cardNumber1 = $('#card-number1').val();

        cardNumber = cardNumber1.replace(/\s+/g, '');
        var cardType = Stripe.card.cardType(cardNumber);

        console.log(cardType);
        switch (cardType) {
            case 'Visa':
                $('#card-image1').html('<img src="js/visa.svg" height="100%">');
                $("#card-back1").css("background", "linear-gradient(to bottom, rgba(252, 190, 27, 1) 1%, rgba(248, 86, 72, 1) 99%)");
                 $("#card-front1").css("background", "linear-gradient(to bottom, rgba(252, 190, 27, 1) 1%, rgba(248, 86, 72, 1) 99%)");
                
                break;

            case 'MasterCard':
                $('#card-image1').html('<img src="js/master.svg" height="100%">');
                $("#card-back1").css("background", "linear-gradient(to bottom, rgba(183,71,247,1) 0%,rgba(108,83,220,1) 100%)");
                 $("#card-front1").css("background", "linear-gradient(to bottom, rgba(183,71,247,1) 0%,rgba(108,83,220,1) 100%)");
                
                break;
            case 'Discover':
                $('#card-image1').html('<img src="js/discover.svg" height="100%">');
                $("#card-back1").css("background", "-webkit-linear-gradient(top, rgba(255, 86, 65, 1) 0%, rgba(253, 50, 97, 1) 100%)");
                 $("#card-front1").css("background", "-webkit-linear-gradient(top, rgba(255, 86, 65, 1) 0%, rgba(253, 50, 97, 1) 100%)");
                
                break;

            case 'American Express':
                $('#card-image1').html('<img src="js/american.svg" height="100%">');
                 $("#card-back1").css("background", "linear-gradient(to bottom, rgb(25, 88, 224) 0%, rgba(253, 50, 97, 0.58) 100%)");
                 $("#card-front1").css("background", "linear-gradient(to bottom, rgb(25, 88, 224) 0%, rgba(253, 50, 97, 0.58) 100%)");
                
                break;
             case 'Diners Club':
                $('#card-image1').html('<img src="js/dinner.svg" height="100%">');
                break;
             case 'Rupay':
                 $('#card-image1').html('<img src="js/rupay.svg" height="100%">');
                 break;

            case 'Unknown':
                $('#card-image1').html('<img src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiA/PjxzdmcgZGF0YS1uYW1lPSJMYXllciAxIiBpZD0iTGF5ZXJfMSIgdmlld0JveD0iMCAwIDYwIDYwIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIj48ZGVmcz48c3R5bGU+LmNscy0xLC5jbHMtMTAsLmNscy0xMSwuY2xzLTIsLmNscy02LC5jbHMtN3tmaWxsOm5vbmU7fS5jbHMtMXtjbGlwLXJ1bGU6ZXZlbm9kZDt9LmNscy0yLC5jbHMtNXtmaWxsLXJ1bGU6ZXZlbm9kZDt9LmNscy0ze2NsaXAtcGF0aDp1cmwoI2NsaXAtcGF0aCk7fS5jbHMtNHtjbGlwLXBhdGg6dXJsKCNjbGlwLXBhdGgtMik7fS5jbHMtNXtmaWxsOiNmZTg2NTc7fS5jbHMtMTAsLmNscy0xMSwuY2xzLTZ7c3Ryb2tlOiNmZTg2NTc7fS5jbHMtMTAsLmNscy02e3N0cm9rZS1saW5lY2FwOnJvdW5kO30uY2xzLTEwLC5jbHMtMTEsLmNscy02LC5jbHMtN3tzdHJva2UtbGluZWpvaW46cm91bmQ7fS5jbHMtNntzdHJva2Utd2lkdGg6NHB4O30uY2xzLTd7c3Ryb2tlOiNlMDZjM2U7fS5jbHMtMTEsLmNscy03e3N0cm9rZS1saW5lY2FwOnNxdWFyZTt9LmNscy0xMCwuY2xzLTExLC5jbHMtN3tzdHJva2Utd2lkdGg6MnB4O30uY2xzLTh7Y2xpcC1wYXRoOnVybCgjY2xpcC1wYXRoLTQpO30uY2xzLTl7ZmlsbDojZmZkYzgyO308L3N0eWxlPjxjbGlwUGF0aCBpZD0iY2xpcC1wYXRoIj48cGF0aCBjbGFzcz0iY2xzLTEiIGQ9Ik0xLDQ2VjE4YTUsNSwwLDAsMSw1LTVINTRhNSw1LDAsMCwxLDUsNVY0NmE1LDUsMCwwLDEtNSw1SDZBNSw1LDAsMCwxLDEsNDZabTIsMGEzLDMsMCwwLDAsMywzSDU0YTMsMywwLDAsMCwzLTNWMThhMywzLDAsMCwwLTMtM0g2YTMsMywwLDAsMC0zLDNWNDZaTS0xOSw3MUg3OVYtN0gtMTlWNzFaIi8+PC9jbGlwUGF0aD48Y2xpcFBhdGggaWQ9ImNsaXAtcGF0aC0yIj48cGF0aCBjbGFzcz0iY2xzLTEiIGQ9Ik0yLDQ2YTQsNCwwLDAsMCw0LDRINTRhNCw0LDAsMCwwLDQtNFYxOGE0LDQsMCwwLDAtNC00SDZhNCw0LDAsMCwwLTQsNFY0NloiLz48L2NsaXBQYXRoPjxjbGlwUGF0aCBpZD0iY2xpcC1wYXRoLTQiPjxwYXRoIGNsYXNzPSJjbHMtMSIgZD0iTTksMjlhMiwyLDAsMCwwLDIsMkgyMmEyLDIsMCwwLDAsMi0yVjIzYTIsMiwwLDAsMC0yLTJIMTFhMiwyLDAsMCwwLTIsMnY2WiIvPjwvY2xpcFBhdGg+PC9kZWZzPjx0aXRsZS8+PGcgY2xhc3M9ImNscy0zIj48ZyBjbGFzcz0iY2xzLTQiPjxwYXRoIGNsYXNzPSJjbHMtNSIgZD0iTTIsNDZhNCw0LDAsMCwwLDQsNEg1NGE0LDQsMCwwLDAsNC00VjE4YTQsNCwwLDAsMC00LTRINmE0LDQsMCwwLDAtNCw0VjQ2WiIvPjwvZz48L2c+PGcgY2xhc3M9ImNscy00Ij48cGF0aCBjbGFzcz0iY2xzLTYiIGQ9Ik0yLDQ2YTQsNCwwLDAsMCw0LDRINTRhNCw0LDAsMCwwLDQtNFYxOGE0LDQsMCwwLDAtNC00SDZhNCw0LDAsMCwwLTQsNFY0NloiLz48L2c+PGxpbmUgY2xhc3M9ImNscy03IiB4MT0iOSIgeDI9IjI4IiB5MT0iMzkiIHkyPSIzOSIvPjxsaW5lIGNsYXNzPSJjbHMtNyIgeDE9IjMyIiB4Mj0iNTEiIHkxPSIzOSIgeTI9IjM5Ii8+PGcgY2xhc3M9ImNscy04Ij48cmVjdCBjbGFzcz0iY2xzLTkiIGhlaWdodD0iMjAiIHdpZHRoPSIyNSIgeD0iNCIgeT0iMTYiLz48L2c+PHBhdGggY2xhc3M9ImNscy0xMCIgZD0iTTksMjlhMiwyLDAsMCwwLDIsMkgyMmEyLDIsMCwwLDAsMi0yVjIzYTIsMiwwLDAsMC0yLTJIMTFhMiwyLDAsMCwwLTIsMnY2WiIvPjxsaW5lIGNsYXNzPSJjbHMtMTEiIHgxPSIxOCIgeDI9IjE4IiB5MT0iMzEiIHkyPSIyMSIvPjxsaW5lIGNsYXNzPSJjbHMtMTEiIHgxPSI5IiB4Mj0iMTciIHkxPSIyNyIgeTI9IjI3Ii8+PGxpbmUgY2xhc3M9ImNscy0xMSIgeDE9IjE4IiB4Mj0iMjQiIHkxPSIyNSIgeTI9IjI1Ii8+PC9zdmc+" height="55px">');
                break;
        }
    }

    // check card type on card number input blur 
    $('#card-number1').blur(function (event) {
        event.preventDefault();
        checkCardType();
    });

    // on button click: 
    $('#card-btn1').click(function (event) {

        // get each input value and use Stripe to determine whether they are valid
        var cardNumber = $('#card-number1').val();
        var isValidNo = Stripe.card.validateCardNumber(cardNumber);
        var expMonth = $('#card-month1').val();
        var expYear = $('#card-year1').val();
        var isValidExpiry = Stripe.card.validateExpiry(expMonth, expYear);
        var cardCVC = $('#card-cvc1').val();
        var isValidCVC = Stripe.card.validateCVC(cardCVC);
        var cardHolder = $('#card-holder1').val();
        event.preventDefault();

        // alert the user if any fields are missing
        if (!cardNumber || !cardCVC || !cardHolder || !expMonth || !expYear) {
            console.log(cardNumber + cardCVC + cardHolder + cardMonth + cardYear);
            $('#form-errors1').addClass('hidden');
            $('#card-success1').addClass('hidden');
            $('#form-errors1').removeClass('hidden');
            $('#card-error1').text('Please complete all fields.');
            findEmpty();
        } else {

            // alert the user if any fields are invalid
            if (!isValidNo || !isValidExpiry || !isValidCVC) {
                $('#form-errors1').css('display', 'block');
                if (!isValidNo) {
                    $('#card-error1').text('Invalid credit card number.');
                } else if (!isValidExpiry) {
                    $('#card-error1').text('Invalid expiration date.')
                } else if (!isValidCVC) {
                    $('#card-error1').text('Invalid CVC code.')
                }

            } else {
                $('#card-success1').removeClass('hidden');
            }
        }
    })

});























$(document).ready(function () {

    Stripe.setPublishableKey('pk_test_9D43kM3d2vEHZYzPzwAblYXl');

    var cardNumber, cardMonth, cardYear, cardCVC, cardHolder;

    // check for any empty inputs
    function findEmpty() {
        var emptyText = $('#form-container2 input').filter(function () {

            return $(this).val == null;
        });

        // add invalid class to empty inputs
        console.log(emptyText.prevObject);
        emptyText.prevObject.addClass('invalid');
    }

    // check for card type and display corresponding icon
    function checkCardType() {
        
        cardNumber2 = $('#card-number2').val();

        cardNumber = cardNumber2.replace(/\s+/g, '');
        var cardType = Stripe.card.cardType(cardNumber);

        console.log(cardType);
        switch (cardType) {
            case 'Visa':
                $('#card-image2').html('<img src="js/visa.svg" height="100%">');
                $("#card-back2").css("background", "linear-gradient(to bottom, rgba(252, 190, 27, 1) 1%, rgba(248, 86, 72, 1) 99%)");
                 $("#card-front2").css("background", "linear-gradient(to bottom, rgba(252, 190, 27, 1) 1%, rgba(248, 86, 72, 1) 99%)");
                
                break;

            case 'MasterCard':
                $('#card-image2').html('<img src="js/master.svg" height="100%">');
                $("#card-back2").css("background", "linear-gradient(to bottom, rgba(183,71,247,1) 0%,rgba(108,83,220,1) 100%)");
                 $("#card-front2").css("background", "linear-gradient(to bottom, rgba(183,71,247,1) 0%,rgba(108,83,220,1) 100%)");
                
                break;
            case 'Discover':
                $('#card-image2').html('<img src="js/discover.svg" height="100%">');
                $("#card-back2").css("background", "-webkit-linear-gradient(top, rgba(255, 86, 65, 1) 0%, rgba(253, 50, 97, 1) 100%)");
                 $("#card-front2").css("background", "-webkit-linear-gradient(top, rgba(255, 86, 65, 1) 0%, rgba(253, 50, 97, 1) 100%)");
                
                break;

            case 'American Express':
                $('#card-image2').html('<img src="js/american.svg" height="100%">');
                 $("#card-back2").css("background", "linear-gradient(to bottom, rgb(25, 88, 224) 0%, rgba(253, 50, 97, 0.58) 100%)");
                 $("#card-front2").css("background", "linear-gradient(to bottom, rgb(25, 88, 224) 0%, rgba(253, 50, 97, 0.58) 100%)");
                
                break;
             case 'Diners Club':
                $('#card-image2').html('<img src="js/dinner.svg" height="100%">');
                break;
             case 'Rupay':
                 $('#card-image2').html('<img src="js/rupay.svg" height="100%">');
                 break;

            case 'Unknown':
                $('#card-image2').html('<img src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiA/PjxzdmcgZGF0YS1uYW1lPSJMYXllciAxIiBpZD0iTGF5ZXJfMSIgdmlld0JveD0iMCAwIDYwIDYwIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIj48ZGVmcz48c3R5bGU+LmNscy0xLC5jbHMtMTAsLmNscy0xMSwuY2xzLTIsLmNscy02LC5jbHMtN3tmaWxsOm5vbmU7fS5jbHMtMXtjbGlwLXJ1bGU6ZXZlbm9kZDt9LmNscy0yLC5jbHMtNXtmaWxsLXJ1bGU6ZXZlbm9kZDt9LmNscy0ze2NsaXAtcGF0aDp1cmwoI2NsaXAtcGF0aCk7fS5jbHMtNHtjbGlwLXBhdGg6dXJsKCNjbGlwLXBhdGgtMik7fS5jbHMtNXtmaWxsOiNmZTg2NTc7fS5jbHMtMTAsLmNscy0xMSwuY2xzLTZ7c3Ryb2tlOiNmZTg2NTc7fS5jbHMtMTAsLmNscy02e3N0cm9rZS1saW5lY2FwOnJvdW5kO30uY2xzLTEwLC5jbHMtMTEsLmNscy02LC5jbHMtN3tzdHJva2UtbGluZWpvaW46cm91bmQ7fS5jbHMtNntzdHJva2Utd2lkdGg6NHB4O30uY2xzLTd7c3Ryb2tlOiNlMDZjM2U7fS5jbHMtMTEsLmNscy03e3N0cm9rZS1saW5lY2FwOnNxdWFyZTt9LmNscy0xMCwuY2xzLTExLC5jbHMtN3tzdHJva2Utd2lkdGg6MnB4O30uY2xzLTh7Y2xpcC1wYXRoOnVybCgjY2xpcC1wYXRoLTQpO30uY2xzLTl7ZmlsbDojZmZkYzgyO308L3N0eWxlPjxjbGlwUGF0aCBpZD0iY2xpcC1wYXRoIj48cGF0aCBjbGFzcz0iY2xzLTEiIGQ9Ik0xLDQ2VjE4YTUsNSwwLDAsMSw1LTVINTRhNSw1LDAsMCwxLDUsNVY0NmE1LDUsMCwwLDEtNSw1SDZBNSw1LDAsMCwxLDEsNDZabTIsMGEzLDMsMCwwLDAsMywzSDU0YTMsMywwLDAsMCwzLTNWMThhMywzLDAsMCwwLTMtM0g2YTMsMywwLDAsMC0zLDNWNDZaTS0xOSw3MUg3OVYtN0gtMTlWNzFaIi8+PC9jbGlwUGF0aD48Y2xpcFBhdGggaWQ9ImNsaXAtcGF0aC0yIj48cGF0aCBjbGFzcz0iY2xzLTEiIGQ9Ik0yLDQ2YTQsNCwwLDAsMCw0LDRINTRhNCw0LDAsMCwwLDQtNFYxOGE0LDQsMCwwLDAtNC00SDZhNCw0LDAsMCwwLTQsNFY0NloiLz48L2NsaXBQYXRoPjxjbGlwUGF0aCBpZD0iY2xpcC1wYXRoLTQiPjxwYXRoIGNsYXNzPSJjbHMtMSIgZD0iTTksMjlhMiwyLDAsMCwwLDIsMkgyMmEyLDIsMCwwLDAsMi0yVjIzYTIsMiwwLDAsMC0yLTJIMTFhMiwyLDAsMCwwLTIsMnY2WiIvPjwvY2xpcFBhdGg+PC9kZWZzPjx0aXRsZS8+PGcgY2xhc3M9ImNscy0zIj48ZyBjbGFzcz0iY2xzLTQiPjxwYXRoIGNsYXNzPSJjbHMtNSIgZD0iTTIsNDZhNCw0LDAsMCwwLDQsNEg1NGE0LDQsMCwwLDAsNC00VjE4YTQsNCwwLDAsMC00LTRINmE0LDQsMCwwLDAtNCw0VjQ2WiIvPjwvZz48L2c+PGcgY2xhc3M9ImNscy00Ij48cGF0aCBjbGFzcz0iY2xzLTYiIGQ9Ik0yLDQ2YTQsNCwwLDAsMCw0LDRINTRhNCw0LDAsMCwwLDQtNFYxOGE0LDQsMCwwLDAtNC00SDZhNCw0LDAsMCwwLTQsNFY0NloiLz48L2c+PGxpbmUgY2xhc3M9ImNscy03IiB4MT0iOSIgeDI9IjI4IiB5MT0iMzkiIHkyPSIzOSIvPjxsaW5lIGNsYXNzPSJjbHMtNyIgeDE9IjMyIiB4Mj0iNTEiIHkxPSIzOSIgeTI9IjM5Ii8+PGcgY2xhc3M9ImNscy04Ij48cmVjdCBjbGFzcz0iY2xzLTkiIGhlaWdodD0iMjAiIHdpZHRoPSIyNSIgeD0iNCIgeT0iMTYiLz48L2c+PHBhdGggY2xhc3M9ImNscy0xMCIgZD0iTTksMjlhMiwyLDAsMCwwLDIsMkgyMmEyLDIsMCwwLDAsMi0yVjIzYTIsMiwwLDAsMC0yLTJIMTFhMiwyLDAsMCwwLTIsMnY2WiIvPjxsaW5lIGNsYXNzPSJjbHMtMTEiIHgxPSIxOCIgeDI9IjE4IiB5MT0iMzEiIHkyPSIyMSIvPjxsaW5lIGNsYXNzPSJjbHMtMTEiIHgxPSI5IiB4Mj0iMTciIHkxPSIyNyIgeTI9IjI3Ii8+PGxpbmUgY2xhc3M9ImNscy0xMSIgeDE9IjE4IiB4Mj0iMjQiIHkxPSIyNSIgeTI9IjI1Ii8+PC9zdmc+" height="55px">');
                break;
        }
    }

    // check card type on card number input blur 
    $('#card-number2').blur(function (event) {
        event.preventDefault();
        checkCardType();
    });

    // on button click: 
    $('#card-btn2').click(function (event) {

        // get each input value and use Stripe to determine whether they are valid
        var cardNumber = $('#card-number2').val();
        var isValidNo = Stripe.card.validateCardNumber(cardNumber);
        var expMonth = $('#card-month2').val();
        var expYear = $('#card-year2').val();
        var isValidExpiry = Stripe.card.validateExpiry(expMonth, expYear);
        var cardCVC = $('#card-cvc2').val();
        var isValidCVC = Stripe.card.validateCVC(cardCVC);
        var cardHolder = $('#card-holder2').val();
        event.preventDefault();

        // alert the user if any fields are missing
        if (!cardNumber || !cardCVC || !cardHolder || !expMonth || !expYear) {
            console.log(cardNumber + cardCVC + cardHolder + cardMonth + cardYear);
            $('#form-errors2').addClass('hidden');
            $('#card-success2').addClass('hidden');
            $('#form-errors2').removeClass('hidden');
            $('#card-error2').text('Please complete all fields.');
            findEmpty();
        } else {

            // alert the user if any fields are invalid
            if (!isValidNo || !isValidExpiry || !isValidCVC) {
                $('#form-errors2').css('display', 'block');
                if (!isValidNo) {
                    $('#card-error2').text('Invalid credit card number.');
                } else if (!isValidExpiry) {
                    $('#card-error2').text('Invalid expiration date.')
                } else if (!isValidCVC) {
                    $('#card-error2').text('Invalid CVC code.')
                }

            } else {
                $('#card-success2').removeClass('hidden');
            }
        }
    })

});




















$(document).ready(function () {

    Stripe.setPublishableKey('pk_test_9D43kM3d2vEHZYzPzwAblYXl');

    var cardNumber, cardMonth, cardYear, cardCVC, cardHolder;

    // check for any empty inputs
    function findEmpty() {
        var emptyText = $('#form-container3 input').filter(function () {

            return $(this).val == null;
        });

        // add invalid class to empty inputs
        console.log(emptyText.prevObject);
        emptyText.prevObject.addClass('invalid');
    }

    // check for card type and display corresponding icon
    function checkCardType() {
        
        cardNumber3 = $('#card-number3').val();

        cardNumber = cardNumber3.replace(/\s+/g, '');
        var cardType = Stripe.card.cardType(cardNumber);

        console.log(cardType);
        switch (cardType) {
            case 'Visa':
                $('#card-image3').html('<img src="js/visa.svg" height="100%">');
                $("#card-back3").css("background", "linear-gradient(to bottom, rgba(252, 190, 27, 1) 1%, rgba(248, 86, 72, 1) 99%)");
                 $("#card-front3").css("background", "linear-gradient(to bottom, rgba(252, 190, 27, 1) 1%, rgba(248, 86, 72, 1) 99%)");
                
                break;

            case 'MasterCard':
                $('#card-image3').html('<img src="js/master.svg" height="100%">');
                $("#card-back3").css("background", "linear-gradient(to bottom, rgba(183,71,247,1) 0%,rgba(108,83,220,1) 100%)");
                 $("#card-front3").css("background", "linear-gradient(to bottom, rgba(183,71,247,1) 0%,rgba(108,83,220,1) 100%)");
                
                break;
            case 'Discover':
                $('#card-image3').html('<img src="js/discover.svg" height="100%">');
                $("#card-back3").css("background", "-webkit-linear-gradient(top, rgba(255, 86, 65, 1) 0%, rgba(253, 50, 97, 1) 100%)");
                 $("#card-front3").css("background", "-webkit-linear-gradient(top, rgba(255, 86, 65, 1) 0%, rgba(253, 50, 97, 1) 100%)");
                
                break;

            case 'American Express':
                $('#card-image3').html('<img src="js/american.svg" height="100%">');
                 $("#card-back3").css("background", "linear-gradient(to bottom, rgb(25, 88, 224) 0%, rgba(253, 50, 97, 0.58) 100%)");
                 $("#card-front3").css("background", "linear-gradient(to bottom, rgb(25, 88, 224) 0%, rgba(253, 50, 97, 0.58) 100%)");
                
                break;
             case 'Diners Club':
                $('#card-image3').html('<img src="js/dinner.svg" height="100%">');
                break;
             case 'Rupay':
                 $('#card-image3').html('<img src="js/rupay.svg" height="100%">');
                 break;

            case 'Unknown':
                $('#card-image3').html('<img src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiA/PjxzdmcgZGF0YS1uYW1lPSJMYXllciAxIiBpZD0iTGF5ZXJfMSIgdmlld0JveD0iMCAwIDYwIDYwIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIj48ZGVmcz48c3R5bGU+LmNscy0xLC5jbHMtMTAsLmNscy0xMSwuY2xzLTIsLmNscy02LC5jbHMtN3tmaWxsOm5vbmU7fS5jbHMtMXtjbGlwLXJ1bGU6ZXZlbm9kZDt9LmNscy0yLC5jbHMtNXtmaWxsLXJ1bGU6ZXZlbm9kZDt9LmNscy0ze2NsaXAtcGF0aDp1cmwoI2NsaXAtcGF0aCk7fS5jbHMtNHtjbGlwLXBhdGg6dXJsKCNjbGlwLXBhdGgtMik7fS5jbHMtNXtmaWxsOiNmZTg2NTc7fS5jbHMtMTAsLmNscy0xMSwuY2xzLTZ7c3Ryb2tlOiNmZTg2NTc7fS5jbHMtMTAsLmNscy02e3N0cm9rZS1saW5lY2FwOnJvdW5kO30uY2xzLTEwLC5jbHMtMTEsLmNscy02LC5jbHMtN3tzdHJva2UtbGluZWpvaW46cm91bmQ7fS5jbHMtNntzdHJva2Utd2lkdGg6NHB4O30uY2xzLTd7c3Ryb2tlOiNlMDZjM2U7fS5jbHMtMTEsLmNscy03e3N0cm9rZS1saW5lY2FwOnNxdWFyZTt9LmNscy0xMCwuY2xzLTExLC5jbHMtN3tzdHJva2Utd2lkdGg6MnB4O30uY2xzLTh7Y2xpcC1wYXRoOnVybCgjY2xpcC1wYXRoLTQpO30uY2xzLTl7ZmlsbDojZmZkYzgyO308L3N0eWxlPjxjbGlwUGF0aCBpZD0iY2xpcC1wYXRoIj48cGF0aCBjbGFzcz0iY2xzLTEiIGQ9Ik0xLDQ2VjE4YTUsNSwwLDAsMSw1LTVINTRhNSw1LDAsMCwxLDUsNVY0NmE1LDUsMCwwLDEtNSw1SDZBNSw1LDAsMCwxLDEsNDZabTIsMGEzLDMsMCwwLDAsMywzSDU0YTMsMywwLDAsMCwzLTNWMThhMywzLDAsMCwwLTMtM0g2YTMsMywwLDAsMC0zLDNWNDZaTS0xOSw3MUg3OVYtN0gtMTlWNzFaIi8+PC9jbGlwUGF0aD48Y2xpcFBhdGggaWQ9ImNsaXAtcGF0aC0yIj48cGF0aCBjbGFzcz0iY2xzLTEiIGQ9Ik0yLDQ2YTQsNCwwLDAsMCw0LDRINTRhNCw0LDAsMCwwLDQtNFYxOGE0LDQsMCwwLDAtNC00SDZhNCw0LDAsMCwwLTQsNFY0NloiLz48L2NsaXBQYXRoPjxjbGlwUGF0aCBpZD0iY2xpcC1wYXRoLTQiPjxwYXRoIGNsYXNzPSJjbHMtMSIgZD0iTTksMjlhMiwyLDAsMCwwLDIsMkgyMmEyLDIsMCwwLDAsMi0yVjIzYTIsMiwwLDAsMC0yLTJIMTFhMiwyLDAsMCwwLTIsMnY2WiIvPjwvY2xpcFBhdGg+PC9kZWZzPjx0aXRsZS8+PGcgY2xhc3M9ImNscy0zIj48ZyBjbGFzcz0iY2xzLTQiPjxwYXRoIGNsYXNzPSJjbHMtNSIgZD0iTTIsNDZhNCw0LDAsMCwwLDQsNEg1NGE0LDQsMCwwLDAsNC00VjE4YTQsNCwwLDAsMC00LTRINmE0LDQsMCwwLDAtNCw0VjQ2WiIvPjwvZz48L2c+PGcgY2xhc3M9ImNscy00Ij48cGF0aCBjbGFzcz0iY2xzLTYiIGQ9Ik0yLDQ2YTQsNCwwLDAsMCw0LDRINTRhNCw0LDAsMCwwLDQtNFYxOGE0LDQsMCwwLDAtNC00SDZhNCw0LDAsMCwwLTQsNFY0NloiLz48L2c+PGxpbmUgY2xhc3M9ImNscy03IiB4MT0iOSIgeDI9IjI4IiB5MT0iMzkiIHkyPSIzOSIvPjxsaW5lIGNsYXNzPSJjbHMtNyIgeDE9IjMyIiB4Mj0iNTEiIHkxPSIzOSIgeTI9IjM5Ii8+PGcgY2xhc3M9ImNscy04Ij48cmVjdCBjbGFzcz0iY2xzLTkiIGhlaWdodD0iMjAiIHdpZHRoPSIyNSIgeD0iNCIgeT0iMTYiLz48L2c+PHBhdGggY2xhc3M9ImNscy0xMCIgZD0iTTksMjlhMiwyLDAsMCwwLDIsMkgyMmEyLDIsMCwwLDAsMi0yVjIzYTIsMiwwLDAsMC0yLTJIMTFhMiwyLDAsMCwwLTIsMnY2WiIvPjxsaW5lIGNsYXNzPSJjbHMtMTEiIHgxPSIxOCIgeDI9IjE4IiB5MT0iMzEiIHkyPSIyMSIvPjxsaW5lIGNsYXNzPSJjbHMtMTEiIHgxPSI5IiB4Mj0iMTciIHkxPSIyNyIgeTI9IjI3Ii8+PGxpbmUgY2xhc3M9ImNscy0xMSIgeDE9IjE4IiB4Mj0iMjQiIHkxPSIyNSIgeTI9IjI1Ii8+PC9zdmc+" height="55px">');
                break;
        }
    }

    // check card type on card number input blur 
    $('#card-number3').blur(function (event) {
        event.preventDefault();
        checkCardType();
    });

    // on button click: 
    $('#card-btn3').click(function (event) {

        // get each input value and use Stripe to determine whether they are valid
        var cardNumber = $('#card-number3').val();
        var isValidNo = Stripe.card.validateCardNumber(cardNumber);
        var expMonth = $('#card-month3').val();
        var expYear = $('#card-year3').val();
        var isValidExpiry = Stripe.card.validateExpiry(expMonth, expYear);
        var cardCVC = $('#card-cvc3').val();
        var isValidCVC = Stripe.card.validateCVC(cardCVC);
        var cardHolder = $('#card-holder3').val();
        event.preventDefault();

        // alert the user if any fields are missing
        if (!cardNumber || !cardCVC || !cardHolder || !expMonth || !expYear) {
            console.log(cardNumber + cardCVC + cardHolder + cardMonth + cardYear);
            $('#form-errors3').addClass('hidden');
            $('#card-success3').addClass('hidden');
            $('#form-errors3').removeClass('hidden');
            $('#card-error3').text('Please complete all fields.');
            findEmpty();
        } else {

            // alert the user if any fields are invalid
            if (!isValidNo || !isValidExpiry || !isValidCVC) {
                $('#form-errors3').css('display', 'block');
                if (!isValidNo) {
                    $('#card-error3').text('Invalid credit card number.');
                } else if (!isValidExpiry) {
                    $('#card-error3').text('Invalid expiration date.')
                } else if (!isValidCVC) {
                    $('#card-error3').text('Invalid CVC code.')
                }

            } else {
                $('#card-success3').removeClass('hidden');
            }
        }
    })

});