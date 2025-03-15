<%@ include file="/include.jsp"%>
<%@page contentType="text/html; charset=ISO-8859-1" language="java" %>
<%@page import="java.util.* , java.io.* , java.net.*"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%  
Logger log = LoggerFactory.getLogger("JSPS.ibpsRequestProcessor.jsp");

//add the Posting URL & Checksum URL

String URL = "http://domain/pay/payprocessor";
String checkSumURL = "http://domain:8080/pay/checksumgenerator";

//Parameters required for Checksum Generation
	String amount = request.getParameter("amount");
	String apiKey = request.getParameter("apiKey");
	String dateTime = request.getParameter("dateTime");
	String merchantId = request.getParameter("merchantId");
	String txnId = request.getParameter("txnId");
	String custMobile = request.getParameter("custMobile");
	String custMail = request.getParameter("custMail");
	String channelId = request.getParameter("channelId");
	String txnType = request.getParameter("txnType");
	String checksum = null;
	String checksumParams = "merchantId="+merchantId+"&txnId="+txnId+"&dateTime="+dateTime+"&"
			+ "amount="+amount+"&apiKey="+apiKey+"&custMobile="+custMobile+"&custMail="+custMail+"&channelId="+channelId+"&txnType="+txnType;
	String line = null;
	
	//Checksum Generation Code starts here
	try
	{				
		log.info("ibpsRequestProcessor.jsp ::: Checksum Parameters :\n" +checksumParams);
		URL url = new URL(checkSumURL);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		/* Another method to send Checksum parameters
    	URL url = new URL(checkSumURL+"?"+checksumParams);
    	*/
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.connect();

		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		log.info("ibpsRequestProcessor.jsp ::: Output Stream Open");
		wr.write(checksumParams);
		wr.flush();
		
		StringBuffer checksumResp = new StringBuffer();
		log.info("ibpsRequestProcessor.jsp ::: Response Code : "+con.getResponseCode());
		if (con.getResponseCode() == HttpURLConnection.HTTP_OK) 
		{
			log.info("ibpsRequestProcessor.jsp ::: Response Code "+con.getResponseCode()+" is HTTP OK....");
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

			while((line = br.readLine()) != null) 
			{
				log.info("ibpsRequestProcessor.jsp ::: Line : "+line);
				checksumResp.append(line+"\n");
			}
			br.close();
		}

		con.disconnect();
		checksum = checksumResp.toString();
		wr.write(checksum);
		wr.flush();
		wr.close();
		checksum = checksum.substring(checksum.indexOf("<checksum>")+10,checksum.indexOf("</checksum>"));
	  	log.info("ibpsRequestProcessor.jsp ::: Checksum Generated : "+checksum);
		
	}
	catch (Exception e) 
	{
		log.info("ibpsRequestProcessor.jsp ::: Error Occurred : "+e);
		e.printStackTrace();
	}
  	
	
    //retrieve all the incoming parameters into a hash map
    
	Map fields = new HashMap();
    Enumeration e = request.getParameterNames();
    while (e.hasMoreElements())
		{
			String fieldName = (String) e.nextElement();
			String fieldValue = request.getParameter(fieldName);
			log.info("ibpsRequestProcessor.jsp ::: Request Field Name : "+fieldName+" And Request Field Value : "+fieldValue);
			if ((fieldValue != null) && (fieldValue.length() > 0)) 
			{
				fields.put(fieldName, fieldValue); 
			}
		}
	
	response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Expires", "0");
	response.setDateHeader("Expires", -1);
	response.setDateHeader("Last-Modified", 0);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

    <head>
    	<title>Test Merchant Redirector</title>
        <script type="text/javascript">
            //history.forward();
            /* function redirectRequest() 
            {
            	document.RedirectForm.submit();
            } */
        </script>
    </head>
	
	<body>
    <!-- <body onload="redirectRequest();"> -->
 		
        <center><h1>Redirect Page</h1></center>
        <form name="RedirectForm" action="<%=URL%>" method="post">
		<table width="80%" align="center" border="0" cellpadding='0' cellspacing='0'>         

                <tr>
                     <td colspan="2">&nbsp;</td>
                </tr>
                	
                <tr>
                     <td colspan="2">&nbsp;</td>
                </tr>
                
                <tr bgcolor='#FCF3CF' >
                     <td colspan="2" align="center" ><h5>Please wait while redirecting to Payment Page powered by PAYMENT.</h5></td>
                </tr>
                			
                <tr>
                	<td colspan="2">&nbsp;</td>
                </tr>
                
                <tr>
                     <td colspan="2" align="center" bgcolor='#FCF3CF'><h5><strong>Please do not click Back/Refresh button while Redirecting.</strong></h5></td>
                </tr>
                
                <tr>
                     <td><input type="hidden" name="checksum" value="<%=checksum%>"></td>
                </tr>
                
				<tr>
				<%
					for (Iterator itr = fields.keySet().iterator(); itr.hasNext(); ) 
					{
            			String fieldName = (String) itr.next();
						String fieldvalue=(String)fields.get(fieldName);	
				%>                     
                     <td><input type="hidden" name="<%=fieldName%>" value="<%=fieldvalue%>"></td>
				<%	
					}
				%>
				</tr>
                <tr>
                	<td colspan="2">&nbsp;</td>
                </tr>
                 
                <tr>
                	<td colspan="2" align="center"><input type="submit" name="submit" value="Submit Invoice" /></td>
                </tr>
                
            </table>
        </form>
    </body>

</html>