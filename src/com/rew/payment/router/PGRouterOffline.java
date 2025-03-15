package com.rew.payment.router;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.rew.payment.utility.PGUtils;
import com.rew.pg.db.DataManager;
import com.rew.pg.dto.TransactionMaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PGRouterOffline extends HttpServlet {

	static Logger log = LoggerFactory.getLogger(PGRouterOffline.class.getName());

	private static final long serialVersionUID = 1L;


	public PGRouterOffline()
	{
		super();

	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.getWriter().append("Get Method not allowed: ").append(request.getContextPath());
	}

	protected void doOption(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.getWriter().append("Option Method not Allowed.");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		response.setHeader("Cache-control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "-1");
		response.setHeader("checksum", request.getParameter("checksum"));


		String referer =PGUtils.getPropertyValue("referer");
		String refererv2 =PGUtils.getPropertyValue("referer2");

		HttpSession session  = request.getSession();

		String txnId = request.getParameter("txnId");
		DataManager dm=new DataManager();
		TransactionMaster TM =dm.getTxnMaster(txnId);	
//		if(request.getHeader("Referer") != null && request.getHeader("Referer").endsWith(referer) && session.getAttribute("cToken") != null && request.getParameter("checksum") != null && session.getAttribute("cToken").toString().equals(request.getParameter("checksum").toString()))
		
		if(request.getHeader("Referer") != null && (request.getHeader("Referer").endsWith(referer) || request.getHeader("Referer").endsWith(refererv2) ) && session.getAttribute("cToken") != null && request.getParameter("checksum") != null && session.getAttribute("cToken").toString().equals(request.getParameter("checksum").toString()))

		{
			
			log.info("txnId <<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>   "+txnId);
			

			String sInstrumentId = request.getParameter("instrumentId");			
			String bankId = request.getParameter("paymode");

			log.info("Before check Instrument Id >>>>>>> "+sInstrumentId);
			log.info("Instrument Id >>>>>>> "+sInstrumentId);
			log.info("bankId Id >>>>>>> "+bankId);

					
			log.info("Instrument Id >>>>>>> "+TM.getInstrumentId());

			List<String> lstSurCharge=dm.getCalculateSurcharge(bankId, sInstrumentId, TM.getAmount(), TM.getMerchantId(), "NA",TM.getId());


			TM.setInstrumentId(sInstrumentId);
			TM.setRespStatus("0");
			TM.setServiceRRN("NA");
			TM.setServiceTxnId("NA");
			TM.setServiceAuthId("NA");
			TM.setRespMessage("NA");
			TM.setRmsReason("NA");
			TM.setRmsScore("NA");
			TM.setProcessId(lstSurCharge.get(2));
			TM.setBankId(bankId);
			TM.setSurcharge(lstSurCharge.get(0)+"");
			log.info("retamount set to TransactionMaster :::::: "+TM.getSurcharge());
			TM.setModified_By("PGRouterOffline");
			TM.setModified_On(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));

			try
			{
				log.info("Updating database :::::  "+TM.getSurcharge());
				dm.updateTxn(TM);

				log.info("After Updating database :::::  "+TM.getSurcharge());


				Double amount = Double.valueOf(TM.getAmount())+Double.parseDouble(TM.getSurcharge());
				DecimalFormat df = new DecimalFormat("#.##");      
				amount = Double.valueOf(df.format(amount));
				log.info("amount >>>>>>>    "+amount);
				request.setAttribute("id", TM.getId());   
				request.setAttribute("amount", amount+"");
				log.info("amount >>>>>>>    "+amount);
				request.setAttribute("custMob", TM.getCustMobile());
				request.setAttribute("custMail", TM.getCustMail());
				request.setAttribute("txnType", "DIRECT");
				log.info("2 >>>>>>>    "+TM.getCustMobile() + TM.getCustMail());
				request.setAttribute("Mid", TM.getMerchantId());
				request.setAttribute("bankId", bankId);
				log.info("3 >>>>>>>    "+bankId);
				request.setAttribute("checksum", request.getParameter("checksum") );
				log.info("4 >>>>>>>    "+request.getParameter("checksum"));
				request.setAttribute("instrumentId", request.getParameter("instrumentId") );
				log.info("5 >>>>>>>    "+request.getParameter("instrumentId"));
				request.setAttribute("spId", TM.getProcessId());

				request.setAttribute("Merchant_txn_id", TM.getTxnId());  
				log.info("amount >>>>>>>    "+amount);
				IDynamicPGSelector pgSelector = null;
				String redirectPage = null;


				pgSelector = (IDynamicPGSelector)Class.forName("com.payone.offline.ecms.ECMSChallanProcessor").newInstance();

				log.info("Dynamic classLoader :::: "+pgSelector);
				redirectPage = pgSelector.redirectToPG(request);						
				out.println(redirectPage);
				out.flush();
				out.close();

			}
			catch (Exception e)
			{
				log.info ("PGRouter.java ::: " + PGUtils.generateLog(TM.getMerchantId(), TM.getTxnId(), TM.getId(), TM.getAmount()));
				log.error("PGRouter.java ::: Error Occurred :: "+e);
				request.setAttribute("errorMsg", "Error 10052 : Error while Processing Transaction Request.");	
				request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
			}    	        	  	        				        
		}
		else
		{
			log.info ("PGRouter.java ::: " + PGUtils.generateLog(TM.getMerchantId(), TM.getTxnId(), TM.getId(), TM.getAmount()));
			log.info("PGRouter.java ::: Token Mismatch :: Token in Session --> "+session.getAttribute("cToken")+" And Token in Request --> "+request.getParameter("checksum"));
			request.setAttribute("errorMsg", "Error 10050 : Invalid Request Parameters.Direct Parameter Posting is not allowed.");					
			request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);	
		}
	}



}
