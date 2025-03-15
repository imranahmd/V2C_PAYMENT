<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
function redirectRequest() {
	document.getElementById("RedirectForm").submit();
}
</script>
</head>
<!-- <center><img alt="" src="img/download.jpg""><br>
 -->
 
 <h2 style="color: #2e6c80;"><center>SIMULATOR TEST PAGE</center></h2>
	
	<body>	
	<center>
		<!-- <a href="http://localhost:8080/IYMware/PayProcessor?invoiceId=IY12349&amount=100.00&studentId=st001&merchantId=M0002&studentName=Rahul Singh"> 
		Click here for the payment</a> -->
	
	
	<form method="post" name="frmPay" id="frmPay" action="LOYRTPayProcessor">
	
	<table>
	<tr>
	<td>Invoice Number:</td>
	<td><input type="text" name="invoiceId" value="IY1234911901"/></td>
	</tr>
	
	<tr>
	<td>Amount:</td>
	<td><input type="text" name="amount" value="10.00"/></td>
	</tr>
	
	<tr>
	<td>StudentId:</td>
	<td><input type="text" name="studentId" value="st001"/></td>
	</tr>
	
	<tr>
	<td>MerchantId:</td>	
	<td><input type="text" name="merchantId" value="M0000298"/></td>
	</tr>
	
	<tr>
	<td>Student's Name:</td>
	<td><input type="text" name="studentName" value="Rahul Singh"/></td>
	</tr>
	
	<tr>
	<td>Center Code:</td>
	<td><input type="text" name="centerCode" value="DEFAULT"/></td>
	</tr>
	
	<tr>
	<td>Expiery Date:</td>
	<td><input type="text" name="expDate" value="20191231"/></td>
	</tr>
	
	<tr>
	<td>Email:</td>
	<td><input type="text" name="emailid" value="abc@gmail.com"/></td>
	</tr>

	<tr>
	<td>Mobile no:</td>
	<td><input type="text" name="Mobileno" value="8888888888"/></td>
	</tr>
	
	<tr>
	<td></td>
	<td></td>
	</tr>
	<tr>
	<td></td>
	<td></td>
	</tr>
	<tr>
	<td></td>
	<td></td>
	</tr>
	
	<tr>
	<td></td>
	<td><input type="submit" name="btnSubmit" value="Submit to Pay"></td>
	</tr>
	
	</table> 
	</form>
	</center>
</body>

</html>



<style type="text/css">

* {
  box-sizing: border-box;
}

input[type=text], select, textarea {
  width: 75%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  resize: vertical;
}

label {
  padding: 12px 12px 12px 0;
  display: inline-block;
  color: green;

}

input[type=submit] {
  background-color: #4CAF50;
  color: white;
  padding: 12px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  float: center;
}

input[type=submit]:hover {
  background-color: #45a049;
}

.container {
  border-radius: 5px;
  background-color: #f2f2f2;
  padding: 20px;
}

.col-25 {
  float: left;
  width: 25%;
  margin-top: 6px;
}

.col-75 {
  float: left;
  width: 75%;
  margin-top: 6px;
}

/* Clear floats after the columns */
.row:after {
  content: "";
  display: table;
  clear: both;
}

/* Responsive layout - when the screen is less than 600px wide, make the two columns stack on top of each other instead of next to each other */
@media screen and (max-width: 100px) {
  .col-25, .col-75, input[type=submit] {
    width: 100%;
    margin-top: 0;
  }
}
            
</style>
