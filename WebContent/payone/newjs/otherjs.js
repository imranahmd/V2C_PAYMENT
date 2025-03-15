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





 function changecard1(cardno){

        var value = cardno.replace(/ /g,'');
    var number =value;
    var vv = number.replace(/\B(?=(\d{4})+(?!\d))/g, " ");

    $('#card-number3').val(vv);
    
    $('#card-number3').focus();
    $('#image-container3').trigger("click");
    

    }



     function changecard(cardno){

        var value = cardno.replace(/ /g,'');
    var number =value;
    var vv = number.replace(/\B(?=(\d{4})+(?!\d))/g, " ");
    $('#card-number2').val(vv);
    
    $('#card-number2').focus();
    $('#image-container2').trigger("click");
    

    }
     
    


$('.cdnumber').on('keypress change blur', function () {
    $(this).val(function (index, value) {

        return value.replace(/[^a-z0-9]+/gi, '').replace(/(.{4})/g, '$1 ');

    });
   var cv=$(this).val();
   if(cv.length==18){
   
$('.nameofcard').focus();

}
});

$('.cdnumber').on('copy cut paste', function () {
    setTimeout(function () {
        $('.cdnumber').trigger("change");
    });
});



/*function storedcard(cardno){

    var value = cardno.replace(/ /g,'');
var number =value;
var vv = number.replace(/\B(?=(\d{4})+(?!\d))/g, " ");
$('#card-number3').val(vv);

$('#card-number3').focus();
$('#image-container3').trigger("click");


}*/


   function validate(evt) {
  var theEvent = evt || window.event;

  // Handle paste
  if (theEvent.type === 'paste') {
      key = event.clipboardData.getData('text/plain');
  } else {
  // Handle key press
      var key = theEvent.keyCode || theEvent.which;
      key = String.fromCharCode(key);
  }
  var regex = /[0-9]|\./;
  if( !regex.test(key) ) {
    theEvent.returnValue = false;
    if(theEvent.preventDefault) theEvent.preventDefault();
  }
}