<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@ page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="com.rew.payment.utility.XSSFilter"%>
<%
Enumeration<String> params = request.getParameterNames();
String paramName = null;
String paramValue = null;
String retValue = null;
while (params.hasMoreElements()) {
	paramName = (String) params.nextElement();
	paramValue = request.getParameter(paramName);
	System.out.println("paramName: " + paramName + " paramValue: " + paramValue);
	XSSFilter xss = new XSSFilter();
	retValue = xss.stripXSSJSP(paramValue);
	if ((retValue == null) || (!retValue.equals(paramValue))) {
		request.getRequestDispatcher("xssErrorPage.jsp").forward(request, response);
	}
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript">
	function buttonDisabled() {
		thisForm = document.posting;
		thisForm.success.disabled = true;
		thisForm.failure.disabled = true;
	}

	function setSuccess() {
		document.posting.PAID.value = 'Y';
		document.posting.BID.value = document.posting.traceNo.value;
	}

	function setFailure() {
		document.posting.PAID.value = 'N';
		document.posting.BID.value = '';
	}
</script>
</head>
<body>
	<form name="posting" action="respHandlerDemoBank" method="post"
		onsubmit="buttonDisabled();">
		<table border="0" align="center" cellspacing="1" cellpadding="3"
			width="90%">
			<%
				String traceNo = new SimpleDateFormat("ddMMYYYY").format(new Date()) + new Date().getTime();
			%>
			<tbody>
				<tr>
					<td align="left"><img src="images/logo.png"
						style="height: 100px; width: 230px"></td>
				</tr>
				<tr>
					<td height="50" align="center"></td>
				</tr>
				<tr>
					<td align="center">Complete Your Transaction</td>
				</tr>
				<tr>
					<td align="center"><input type="hidden" name="AMT"
						value='<%=request.getParameter("AMT")%>'> <input
						type="hidden" name="PRN" value='<%=request.getParameter("PRN")%>'>
						<input type="hidden" name="CRN"
						value='<%=request.getParameter("CRN")%>'> <input
						type="submit" name="success"
						value="Generate a Success Transaction" onclick="setSuccess();">
						<input type="submit" name="failure"
						value="Generate a Fail Transaction" onclick="setFailure();">
						<!-- <input type="hidden" name="RU" value="https://test.timesofmoney.com/direcpay/secure/transactionResponse.jsp?token=2025797968487739-Y"> -->
						<input type="hidden" name="PAID" value=""> <input
						type="hidden" name="traceNo" value="<%=traceNo%>"> <input
						type="hidden" name="BID" value=""></td>
				</tr>
			</tbody>
		</table>
	</form>
</body>
</html>