   
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@ page import="java.util.List"%>


<style type = "text/css">


.label{
  text-align: left;
  text-decoration: none;
  display: inline-block;
  #font-size: 14px;
  color: #f44336;
  font: 14px/1.4 "Helvetica Neue", Helvetica, Arial, sans-serif;
}

.buttong {
  background-color: #080874; /* BLUE */
  border: none;
  color: white;
  padding: 9px 20px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 14px;
}

        table {
  border-style: solid;
  border-spacing: 0;
  color: #4a4a4d;
  font: 14px/1.4 "Helvetica Neue", Helvetica, Arial, sans-serif;
}
table tr#ROW1  {background-color:white; color:black;}
th,
td {
  padding: 10px 15px;
  vertical-align: middle;
}
thead {
  background: #395870;
  background: linear-gradient(#49708f, #293f50);
  color: #fff;
  font-size: 11px;
  text-transform: uppercase;
}
th:first-child {
  border-top-left-radius: 5px;
  text-align: left;
}
th:last-child {
  border-top-right-radius: 5px;
}
tbody tr:nth-child(even) {
  background: #f0f0f2;
}
td {
  border-bottom: 0px solid #cecfd5;
  border-right: 0px solid #cecfd5;
}
td:first-child {
  border-left: 0px solid #cecfd5;
}
.book-title {
  color: #395870;
  display: block;
}
.text-offset {
  color: #7c7c80;
  font-size: 12px;
}
.item-stock,
.item-qty {
  text-align: center;
}
.item-price {
  text-align: right;
}
.item-multiple {
  display: block;
}
tfoot {
  text-align: right;
}
tfoot tr:last-child {
  background: #f0f0f2;
  color: #395870;
  font-weight: bold;
}
tfoot tr:last-child td:first-child {
  border-bottom-left-radius: 5px;
}
tfoot tr:last-child td:last-child {
  border-bottom-right-radius: 5px;
}
            
</style>

	
	<%   
	
	 Logger log = LoggerFactory.getLogger("JSPS.viewchallan.jsp");
	log.info("VIEW CHALLAN PAGE RENDERED...");
			
	String reference_no = request.getParameter("reference_no");

	String beneficiaryName = request.getParameter("beneficiaryName");
	String acc_no = request.getParameter("acc_no");
	String ifsc = request.getParameter("ifsc");
	String bank = request.getParameter("bank");
	String branch = request.getParameter("branch");
	String amount = request.getParameter("amount");
	String cust_name = request.getParameter("cust_name");
	String txn_date = request.getParameter("txn_date");
	String email = request.getParameter("email");
	String phone = request.getParameter("phone");
	String utrNo = request.getParameter("utrNo");
	String ru = request.getParameter("ru");
	String paymode = request.getParameter("paymode");
	request.setAttribute("paymode", paymode);
	//String sRandomCustId = request.getParameter("sRandomCustId");
	//request.setAttribute("sRandomCustId", sRandomCustId);
	
	
	log.info("reference_no = "+reference_no);
	log.info("beneficiaryName = "+beneficiaryName);
	log.info("acc_no = "+acc_no);
	log.info("ifsc = "+ifsc);
	log.info("bank = "+bank);
	log.info("branch = "+branch);
	log.info("amount = "+amount);
	log.info("cust_name = "+cust_name);
	log.info("txn_date = "+txn_date);
	log.info("email = "+email);
	log.info("phone = "+phone);
	log.info("utrNo = "+utrNo);
	log.info("paymode = "+paymode);
	//log.info("sRandomCustId = "+sRandomCustId);
	
	
	
	if(reference_no==null){
		reference_no="";
	}
	if(beneficiaryName==null){
		beneficiaryName="";
	}
	if(acc_no==null){
		acc_no="";
	}
	if(bank==null){
		bank="";
	}
	if(ifsc==null){
		ifsc="";
	}
	if(cust_name==null){
		cust_name="";
	}
	if(branch==null){
		branch="";
	}
	if(amount==null){
		amount="";
	}
	if(txn_date==null){
		txn_date="";
	}
	if(email==null){
		email="";
	}
	if(phone==null){
		phone="";
	}
	%>

<html>
<body>
	<table align="center" border="0" width="40%" cellpadding="0">
	<tr>	
	<td align="left"><img src="images/hdfclogo.png" align="top"></td>
<!-- 	<td align="left">CHALLAN DETAILS</td>  -->
	</tr>
	<tr id="ROW1">
	<td align="left">CHALLAN DETAILS</td>

	</tr>
	</table>

	
	<form  name="myform" method="post" action="SaveChallan" >
	<table align="center" border="4" width="40%" cellpadding="1">
    <tr>
    <td id="reference_no">Merchant Transaction Reference Number</td>
    <td id="reference_no"><%=reference_no %></td>
    	
    </tr>    
     <%-- <input type="hidden" id="id" name="id" value="${ch.id}"> --%>
    <tr>
    <td>Beneficiery Name</td>
    <td><%=beneficiaryName %></td>
    </tr>    
	<tr>
    <td>Account Number</td>
    <td><%=acc_no %></td>
    </tr>    
    <tr>
    <td>IFSC </td>
    <td><%=ifsc %></td>
    </tr>
    <tr>
    <td>Bank</td>
    <td><%=bank %></td>
    </tr>
    <tr>
    <td>Branch</td>
    <td><%=branch %></td>
    </tr>
    <tr>
    <td>Amount</td>
    <td><%=amount %></td>
    </tr>
    <%-- <tr>
    <td>Customer ID</td>
    <td>${ch.customerId}</td>
    </tr> --%>
    <tr>
    <td>Customer Name</td>
    <td><%=cust_name %></td>
    </tr>
    <tr>
    <td>Transaction Date</td>
    <td><%=txn_date %></td>
    </tr>
    <tr>
    <td>Email Id</td>
    <td><%=email %></td>
    </tr>
    <tr>
    <td>Phone Number</td>
    <td><%=phone %></td>
    </tr>
    <%-- <tr>
    <td>Challan Number</td>
    <td><%=utrNo %></td>
    </tr> --%>
    
    <input type="hidden" id="paymode" name="paymode" value="<%=paymode %>">
    <input type="hidden" id="reference_no" name="reference_no" value="<%=reference_no %>">
	<input type="hidden" id="beneficiaryName" name="beneficiaryName" value="<%=beneficiaryName %>">
	<input type="hidden" id="acc_no" name="acc_no" value="<%=acc_no %>">
	<input type="hidden" id="ifsc" name="ifsc" value="<%=ifsc %>">
	<input type="hidden" id="bank" name="bank" value="<%=bank%>">
	<input type="hidden" id="branch" name="branch" value="<%=branch%>">
	<input type="hidden" id="amount" name="amount" value="<%=amount%>">
	<input type="hidden" id="cust_name" name="cust_name" value="<%=cust_name%>">
	<input type="hidden" id="txn_date" name="txn_date" value="<%=txn_date%>">
	<input type="hidden" id="email" name="email" value="<%=email%>">
	<input type="hidden" id="phone" name="phone" value="<%=phone%>">
	<input type="hidden" id="utrNo" name="utrNo" value="<%=utrNo%>">    

    </table>
    
    <table align="center" border="0" width="60%" cellpadding="2">
    <tr>    
    <td align="center">  
    <input type ="submit" id="submit" name="submit"  value="Download" class="buttong" /> 
    <!-- <button   id='submit' name='submit' value='PAY'>DOWNLOAD</button> -->
     <%-- <a href="RespCancelRequestHandler?txnId=<%=utrNo%>" class="buttong">Cancel</a>     --%>
<!-- 	<a href="https://payfipgtest.in/payment/" class="buttong" onclick="return submitForm(this,event);">Back to merchant page</a> -->
<%--     	<a href="<%=request.getContextPath() %>/" class="buttong" onclick="return submitForm(this,event);">Back to merchant page</a> --%>
      <a href="#" class="buttong" onclick="return submitForm(this,event);">Back to merchant page</a>
    
    
    <%log.info("request.getContextPath........"+request.getContextPath()); %>
     </td>
       
	   
    </tr>
	
	<tr cellpedding="2" class="label"><td>Please check your details properly before downloading e-challan.</td></tr> 
    </table>    
    </form>
    <br/>
	
    </body>
    <script>
    function submitForm(thisObj, thisEvent) 
    {	
    	debugger
    	var reference_no = document.forms["myform"]["reference_no"].value;
    		alert("Challan Id= "+reference_no);
    		history.go(-2);
    		
    }
    </script>

  

</html>