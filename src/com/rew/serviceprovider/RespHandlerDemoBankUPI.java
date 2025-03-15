package com.rew.serviceprovider;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.payment.rms.ReconMangamentSystem;
import com.rew.payment.utility.PGUtils;
import com.rew.pg.db.DataManager;
import com.rew.pg.dto.TransactionMaster;


@WebServlet("/respHandlerDemoBankUPI")
public class RespHandlerDemoBankUPI extends HttpServlet{

	private static Logger logger = LoggerFactory.getLogger(RespHandlerDemoBankUPI.class);
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	protected void doOption(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getWriter().append("Option Method not Allowed.");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		response.setHeader("Cache-control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "-1");

		logger.info("RespHandlerDemoBankUPI.java ::: Start of DEMO Bank UPI Response..");

		String tranStatus = null;String atrn = null;String bankRefNo = null;String message = null;String errorCode = null;String spErrorCode = null;

		String resp_date_Time = new Timestamp(new Date().getTime())+"";

		Map<String, String> fields = new HashMap();
		DataManager dm=new DataManager();
		String AutoRecon = PGUtils.getPropertyValue("AutoRecon");
		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();)
		{
			String fieldName = (String)e.nextElement();
			String fieldValue = request.getParameter(fieldName);

			logger.info("RespHandlerDemoBankUPI.java ::: Parameter Name - " + fieldName + ", Value - " + fieldValue);

			if ((fieldValue != null) && (fieldValue.length() > 0))
			{
				fields.put(fieldName, fieldValue);
			}
		}

		if ((fields != null) && (fields.size() >= 4))
		{
			tranStatus = (String)fields.get("PAID");
			atrn = (String)fields.get("PRN");


			TransactionMaster TM = dm.getTxnMaster(atrn);

			if (!TM.getRespStatus().equalsIgnoreCase("1"))
			{
				if ((tranStatus != null) && (tranStatus.equals("Y")))
				{
					bankRefNo = (String)fields.get("BID");

					logger.info("RespHandlerDemoBankUPI.java ::: ===================== Success =======================================");

					tranStatus = "Ok";
					message = "Transaction Successful.";
					errorCode = "00000";
					spErrorCode = "00000";

				
				}
				else
				{
					tranStatus = "F";
					bankRefNo = "NA";
					errorCode = "FFFFF";
					spErrorCode = "FFFFF";
					message = "Transaction Failed.";
				}

				TM.setTransStatus(tranStatus);
				TM.setServiceRRN(bankRefNo);
				TM.setServiceAuthId(bankRefNo);
				TM.setServiceTxnId(bankRefNo);
				TM.setRespStatus("1");
				TM.setRespMessage(message);
				TM.setErrorCode(errorCode);
				TM.setSpErrorCode(spErrorCode);
				TM.setRespDateTime(resp_date_Time);
				TM.setModified_By("RespHandlerDemoBankUPI");
				TM.setModified_On(resp_date_Time);

				try
				{
					dm.updateTxn(TM);
					if(TM.getTransStatus().equalsIgnoreCase("Ok") || TM.getTransStatus().equalsIgnoreCase("F")) {
					ReconMangamentSystem Recon = new ReconMangamentSystem();
					
					logger.info("AutoRecon::::::::::::::is::"+AutoRecon);
					if (AutoRecon.equalsIgnoreCase("On")) 
					{
						logger.info("if AutoRecon::::::::::::::is::"+AutoRecon);
						Recon.ReconService(TM.getId(), TM.getTransStatus(),TM.getBankId(), TM.getMerchantId(), TM.getAmount(), TM.getTxnId(), TM.getProcessId(),TM.getInstrumentId());
	
					}
					else
					{
						logger.info("else:::AutoRecon::::::::::::::is::"+AutoRecon);
					}
					
					
										}
					String encRespXML = PGUtils.generateResponse(TM);

					final String txnType="RESPHANDLER";
					String respRetry=new PGUtils().postDataRetry(TM, encRespXML, request, dm,txnType);
					if(!respRetry.equalsIgnoreCase("NA"))
					{
						out.println(respRetry.toString());
						out.flush();
						out.close();
					}
					else
					{
						if ((encRespXML != null) && ((TM.getReturn_url() != null) || (!TM.getReturn_url().isEmpty())))
						{
							logger.info("RespHandlerDemoBankUPI.java ::: inside if--------------------------------" + TM.getReturn_url());
							StringBuilder resp = new StringBuilder();
							resp.append("<HTML>");
							resp.append("<BODY>");
							resp.append("<FORM ACTION=\"" + TM.getReturn_url() + "\"  METHOD='POST'>");
							resp.append("<input type=\"hidden\" name=\"AuthID\" value=\"" + TM.getMerchantId() + "\">");
							resp.append("<input type=\"hidden\" name=\"respData\" value=\"" + encRespXML + "\">");
							resp.append("<input type=\"hidden\" name=\"AggRefNo\" value=\"" + TM.getId() + "\">");
							resp.append("</FORM>");
							resp.append("</BODY>");

							resp.append("<SCRIPT>");
							resp.append("document.forms[0].submit();");
							resp.append("</SCRIPT>");
							resp.append("</HTML>");

							out.println(resp.toString());
							out.flush();
							out.close();
						}
						else
						{
							logger.info("RespHandlerDemoBankUPI.java ::: Error while generating Response OR fetching Return URL for Txn Id '" + TM.getId() + "'.");
							request.setAttribute("errorMsg", "Error 10052 : Error while Processing Transcation Response to Merchant.");
							request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
						}
					}
				}
				catch (Exception e)
				{
					logger.error("RespHandlerDemoBankUPI.java ::: Error while updating Transaction Response Details for PG Txn Id " + TM.getId() + " :: ", e);
				}


			}
			else
			{
				logger.info("RespHandlerDemoBankUPI.java ::: Transaction is already Processed.Response already updated for Txn Id '" + TM.getId() + "'.");
				request.setAttribute("errorMsg", "Error 10054 : Transaction is already Processed.Response already updated for Txn Id '" + TM.getId() + "'.");
				request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
			}
		}
		else
		{
			logger.info("RespHandlerDemoBankUPI.java ::: Invalid Response from PMC.");
			request.setAttribute("errorMsg", "Error FFFFF : Invalid Response from PMC.");
			request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
		}
	}

}
