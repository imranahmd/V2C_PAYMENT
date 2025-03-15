<%@ include file="/include.jsp"%>
<%@page import="com.rew.pg.dto.TransactionMaster"%>
<%@page import="com.rew.pg.db.DataManager"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

 <%	
 
 response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
 response.setHeader("Pragma", "no-cache");
 response.setHeader("Expires", "0");
 response.setDateHeader("Expires", -1);
 response.setDateHeader("Last-Modified", 0);
 
    Logger log = LoggerFactory.getLogger("JSPS.Retry.jsp");
  	log.info("<<<<<<<<<<<<inside retry >>>>>>>>>>>>>>>>>>>>>");
	 String txnRefNo = request.getParameter("txnRefNo");
	DataManager dm=new DataManager();
	TransactionMaster TM = dm.getTxnMaster(txnRefNo);
	String encRespXML =  request.getParameter("respData");
	
	String merchantId=TM.getMerchantId();
	
	log.info("for id="+ TM.getMerchantId());
	
%>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Retry Page</title>
 <link href="css/retrystyle.css" rel="stylesheet">
</head>

<body>
    <div class="container">
        <div class="inner-wrap">
            <header class="header">
                <div class="logo">
                    <img src="images/retry/hdfc-logo.png" alt="HDFC Bank" title="HDFC Bank">
                </div>
            </header>
            <div class="action-outer">
                <div class="action-box">
                    <div class="error-message"><span class="error-icon"></span>Your transaction failed!</div>
                    <div class="buttns-wrap">
                        <button class="btn btn-cancel"  onclick="cancelRetry()">Cancel</button>
                        <button class="btn btn-retry"  onclick="doRetry()">Retry Payment</button>
                    </div>
                </div>
            </div>
        </div>
        <footer class="footer">
            <div class="pci-dss-logo">
               <!--  <img src="images/retry/pci-dss.png" alt="PCI DSS" title="PCI DSS"> -->
               <a href="https://seal.panaceainfosec.com/index.php?certid=CERTD981AABAF2" onclick="window.open(this.href,'Panacea Certificate','height=500,width=650,scrollbar=yes,status=no,menubar=no,toolbar=no,resizable'); return false" style="width: 100px !important;">
         <img src="https://seal.panaceainfosec.com/images/seal1.gif" style="width: 100px;"></a>
            </div>
            <div class="ssl-secure-logo">
                <img src="images/retry/ssl-secure-logo.png" alt="SSL" title="SSL">
            </div>
            <div class="pay-logo">
                <img src="images/retry/logo.png" alt="1 Pay" title="Payment">
            </div>
        </footer>
    </div>
    
    <FORM name="form1" ACTION="<%=request.getContextPath()%>/RespHandlerRetryCancel"  METHOD='POST'>
				  <input type="hidden" id="encData" name="respData" value="<%=encRespXML.trim()%>">
				<input type="hidden" id="pgid" name="txnRefNo" value="<%=TM.getId()%>"> 
				
				<!-- <input type="hidden" id="encData" name="respData" value="">
				<input type="hidden" id="pgid" name="txnRefNo" value=""> -->
				</FORM>
				
				
				<FORM name="form2" ACTION="<%=request.getContextPath()%>/RespHandlerRetry"  METHOD='POST'>
				<input type="hidden" id="txnRefNo" name="txnRefNo" value="">
				 <input type="hidden" id="encData" name="respData" value="<%=encRespXML%>">
				</FORM>
<script>
 
 try{
		function cancelRetry()
		{
		document.forms["form1"].submit();
		}
		
		 function doRetry()
		{
			var txnRefNo="<%=TM.getId()%>";
			document.getElementById("txnRefNo").value=txnRefNo;
		
			document.forms["form2"].submit();

		} 
		 	
	}catch(err)
	{
		alert(err);
	}

</script>
    
</body>

</html>