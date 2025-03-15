package com.rew.payment.utility;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.pg.db.DataManager;


public class SurchargeCalculation extends HttpServlet
{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private static Logger logger = LoggerFactory.getLogger(SurchargeCalculation.class);
	  
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		String transactionId = (String) request.getParameter("transactionId");
		String instrumentId = (String) request.getParameter("instrumentId");
		String amount =(String) request.getParameter("amount");
		String merchantId = (String) request.getParameter("merchantId");
		String emioption= (String) request.getParameter("emioption");
		String cardTypeVal = request.getParameter("cardType");
		
		logger.info("cardTypeVal"+cardTypeVal);
		
		if(null==emioption || emioption=="" )
		{
			emioption="NA";
		}

		String bankId = request.getParameter("bank");


		if ((instrumentId != null) && (instrumentId.equals("NB")))
		{
			bankId = request.getParameter("bank");
		}
		else
		{
			if (cardTypeVal.equalsIgnoreCase("Rupay"))
			{
				bankId = "Rupay";
			}
			else if (instrumentId.equalsIgnoreCase("UPI") && cardTypeVal.equalsIgnoreCase("UPI"))
			{
				bankId = "UPI";
			}
			else if (instrumentId.equalsIgnoreCase("WALLET")) 
			{
				bankId = request.getParameter("bank");
			} 
			else if (cardTypeVal.equalsIgnoreCase("ATOMUPI"))
			{
				bankId = "ATOMUPI";
			}
			else if(bankId != null && bankId.contains("EMI"))
			{
				bankId= request.getParameter("bank");
			}
			else
			{
				bankId = "FINPG";
			}
		}
		
		String sCardNumber = request.getParameter("cardNo");
		logger.info("transactionId : "+transactionId);
		logger.info("bankId : "+bankId);
		logger.info("instrumentId : "+instrumentId);
		logger.info("amount : "+amount);
		logger.info("merchantId : "+merchantId);
		logger.info("emioption : "+emioption);
		logger.info("sCardNumber :","Hide Card number");
		
		DataManager db=new DataManager();
		List<String> surchargeData=db.getTransactionAmountDetails(bankId, instrumentId, 
				amount,merchantId, emioption, transactionId);
		String txnFee=surchargeData.get(0);
		String gst = surchargeData.get(3);

		logger.info("mdr txnFee "+txnFee +" gst "+gst);
		boolean isHdfcCard = false;
		
		if ( sCardNumber != null && !sCardNumber.isEmpty())
		{
			isHdfcCard = db.isHDFCCard(sCardNumber.substring(0, 6));
		}
		logger.info("ishdfcCard"+isHdfcCard);
		
		List<String> surchargeDataHDFCCards=db.getTransactionAmountDetailsHDFCCards(bankId, instrumentId, 
				amount,merchantId, emioption, transactionId);
		logger.info("surchargeDataHDFCCards"+surchargeDataHDFCCards.size());
		if (isHdfcCard && surchargeDataHDFCCards.size() > 0 )
		{
				txnFee=surchargeDataHDFCCards.get(0);
				gst = surchargeDataHDFCCards.get(1);
		}
		
		logger.info("hdfc mdr txnFee "+txnFee +" gst "+gst);
		
		double total = Double.valueOf(amount) + Double.valueOf(txnFee) + Double.valueOf(gst);
		String subTotal = String.format("%.2f", total);
		
		JSONObject result = new JSONObject();
		result.put("txnFee", txnFee);
		result.put("gst", gst);
		result.put("total", subTotal);
		
		try {
			out.print(result.toString());
		} catch (Exception excp) {
			excp.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
}
