<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Error Txn Receipt Page</title>

<link href="./css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="txn_receipt.css" rel="stylesheet" type="text/css" />
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="container">
	<div class="row header_box" style="height:25%;background: #e44f4f;">
	<div class="row header_component" style="height:60px;color:#ffffff;background: #e44f4f;">
		<div class="col-xs-4" style="left:0px;"><img style="width: 100px; height: 40px; margin:5px;float:left;" src="./img/pnb_logo.png"/></div>
		<div class="col-xs-4" style="height:100%;position: relative;padding-top:20px;font-size:14px; ">State Bank Of India</div>
		<div class="col-xs-4 " style="right:0px;"><img style="width: 100px; height: 40px; margin:5px;float:right;" src="./img/rbl_logo.png"/></div>
	</div>
	<div class="row header_component" style="background: #e44f4f;height: 20%">
		TRANSACTION FAILED
	</div>
	<div class="row header_component" style="background:#f0f0f0;padding-top: 13px;height:35%;">
		<span style="font-size:1.2em;color:#e44f4f;"><i class="fa fa-exclamation-circle" aria-hidden="true"></i></span>
		<span style="color:#606060">Adhaar Details Doesn't match with finger captured</span>
	</div>
	</div>
	<div class ="row content_box" style="height:75%">
		<div class="txn_history_box">
			<table class="table table-responsive" >
			<caption style="background: white;color:#255460;text-align: center;">Transaction Details</caption>
			  <tbody>
			    <tr>
			      <td class="left_column">Transaction ID</td>
			      <td class="right_column">49434348734834</td>
			    </tr>
			    <tr>
			      <td class="left_column">Transaction Date </td>
			      <td class="right_column">feb 8,2017</td>
			    </tr>
			     <tr>
			      <td class="left_column">Agent ID </td>
			      <td class="right_column">feb 8,2017</td>
			    </tr>
			     <tr>
			      <td class="left_column">BC Name </td>
			      <td class="right_column">feb 8,2017</td>
			    </tr>
			     <tr>
			      <td class="left_column">BC Location </td>
			      <td class="right_column">feb 8,2017</td>
			    </tr>
			     <tr>
			      <td class="left_column">Adhaar No. </td>
			      <td class="right_column">feb 8,2017</td>
			    </tr>
			     <tr>
			      <td class="left_column">RRN </td>
			      <td class="right_column">feb 8,2017</td>
			    </tr>
			     <tr>
			      <td class="left_column">STAN</td>
			      <td class="right_column">feb 8,2017</td>
			    </tr>
			     <tr>
			      <td class="left_column">mATM Req ID </td>
			      <td class="right_column">feb 8,2017</td>
			    </tr>
			     <tr>
			      <td class="left_column">UIDAI Auth Code </td>
			      <td class="right_column">feb 8,2017</td>
			    </tr>
			     <tr>
			      <td class="left_column">Transaction Status </td>
			      <td class="right_column">feb 8,2017</td>
			    </tr>
			     <tr>
			      <td class="left_column">Transaction Amt </td>
			      <td class="right_column">feb 8,2017</td>
			    </tr>
			     <tr>
			      <td class="left_column">Terminal ID </td>
			      <td class="right_column">feb 8,2017</td>
			    </tr>
			     <tr>
			      <td class="left_column">A/c Bal </td>
			      <td class="right_column">feb 8,2017</td>
			    </tr>
			  </tbody>
			</table>
		</div>
		<!-- div class="buttons_box">
			<button type="button" style="border-radius: 10px; width: 90%; font-size: 1.2em; margin-top: 5px; background-color: #255460;font-weight:normal;" class="btn btn-info">TRY AGAIN
			</button>
			<button type="button" style="border-radius: 10px; width: 90%; font-size: 1.2em; margin-top: 5px; background-color: #255460;font-weight:normal;" class="btn btn-info">Done
			</button>
		</div -->
	</div>

</div>
</body>
</html>