<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Code Cocktail - Directpay integration sample</title>
<script type="text/javascript" src="dpEncodeRequest.js"></script>
<script>
function encodeTxnRequest()
{
document.ecom.requestparameter.value =
encodeValue(document.ecom.requestparameter.value);
document.ecom.submit();
}
</script>
</head>
<body>
<h2>Code Cocktail - Directpay integration sample</h2>
<em>Hit submit button to process Transaction</em>
<form name="ecom" method="post"
action="https://test.timesofmoney.com/direcpay/secure/PaymentTxnServlet"
onSubmit="encodeTxnRequest();">
<input type="hidden" name="custName" value="Code Cocktail Test User">
<input type="hidden" name="custAddress" value="Mumbai">
<input type="hidden" name="custCity" value="Mumbai">
<input type="hidden" name="custState" value="Maharashtra">
<input type="hidden" name="custPinCode" value="400001">
<input type="hidden" name="custCountry" value="IN">
<input type="hidden" name="custPhoneNo1" value="91">
<input type="hidden" name="custPhoneNo2" value="022">
<input type="hidden" name="custPhoneNo3" value="28000000">
<input type="hidden" name="custMobileNo" value="9820000000">
<input type="hidden" name="custEmailId" value="yourmail@gmail.com">
<input type="hidden" name="deliveryName" value="Test User">
<input type="hidden" name="deliveryAddress" value="Mumbai">
<input type="hidden" name="deliveryCity" value="Mumbai">
<input type="hidden" name="deliveryState" value="Maharashtra">
<input type="hidden" name="deliveryPinCode" value="400234">
<input type="hidden" name="deliveryCountry" value="IN">
<input type="hidden" name="deliveryPhNo1" value="91">
<input type="hidden" name="deliveryPhNo2" value="022">
<input type="hidden" name="deliveryPhNo3" value="28000000">
<input type="hidden" name="deliveryMobileNo" value="9920000000">
<input type="hidden" name="otherNotes" value="Code Cocktail test transaction for direcpay">
<input type="hidden" name="editAllowed" value="N">

<input type="hidden" name="requestparameter"
value="201611151000001|DOM|IND|INR|12|cocktail-order|others|http://yoursite.com/success.html|
http://yoursite.com/fail.html|TOML">
<input type="submit" name="submit" value="Submit">
</form>
</body>
</html>
