package com.rew.upi.hdfc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import com.rew.payment.utility.PGUtils;
import com.rew.payment.utility.S2SCall;
import com.rew.pg.db.DataManager;
import com.rew.pg.dto.TransactionMaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/UpiCheckServlet")
public class UpiCheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(UpiCheckServlet.class);

	private static String upiCheckVPAUrlHDFC = PGUtils.getPropertyValue("upiCheckVPAUrlHDFC").trim(); //"https://upitest.hdfcbank.com/upi/checkMeVirtualAddress"


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpiCheckServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out =response.getWriter();
		String mTrackId = request.getParameter("MTrackid"); // our transaction id column name id in transaction_master

		logger.debug("UpiCheckServlet.java  our id in trnsaction master ::: MtaackId :: "+mTrackId);

		logger.debug("UpiCheckServlet.java ::::::: fetch from property files with trim function "+upiCheckVPAUrlHDFC.length());


		logger.debug("UpiCheckServlet.java ::::::: fetch from property files "+PGUtils.getPropertyValue("upiCheckVPAUrlHDFC").length());
		DataManager dm=new DataManager();
		TransactionMaster TM = dm.getTxnMaster(mTrackId);
		String resp = null;
		if(TM.getRespStatus().equalsIgnoreCase("99") && TM.getModified_By().equalsIgnoreCase("PGTransaction"))
		{

			String merchantRefNo = TM.getTxnId(); // merchant transaction id column name txn_id in transaction_master
			String amount =TM.getAmount();
			String vpa = request.getParameter("VPA");
			String merchantId  = TM.getMerchantId();

			logger.debug("merchantId"+merchantId);
			String Mid = null;
			String Tid = null;
			ArrayList<String> spMidKey = dm.getSPMidKey(merchantId,"UPI","UPI",PGUtils.getPropertyValue("spIdHDFCUPI"));			
			if(spMidKey.size()==0) {
		       logger.info("Merchant is not enabled for HDFC UPI as sp_id is not 3 hence skipping validation.");
			  resp = "success";
			  out.println(resp);
	          out.close();
	          return;
			}
			if(spMidKey != null && spMidKey.size() > 0)
			{
				Mid = spMidKey.get(0).toString();  // "HDFC000000648372"; // "M0002"; Mid get merchant mdr
				Tid = spMidKey.get(1).toString(); //   upiHDFCKey 
				logger.debug("UpiCheckServlet.java ::: Merchant Id : "+merchantId+" , Mid : "+Mid+" , Tid : "+Tid);
			}

			String status = "T";

			try
			{
				String custMail = TM.getCustMail();
				String custMobile = TM.getCustMobile();
				String sresp = this.checkVPA(Mid, vpa, mTrackId, status, Tid,custMail,custMobile,merchantRefNo);
				logger.debug("===========>"+sresp);
				JSONObject json = new JSONObject(sresp);
				// logger.debug(" sresp  :: "+sresp);
				String pstatus = json.getString("PARAM3"); //.equalsIgnoreCase("VE"))
				String pFinalStatus = "SUCCESS";      // comment get s2s call
				logger.debug("UpiCheckServlet.java   :: mTrackId : "+mTrackId + " , merchantRefNo : "+ merchantRefNo + "  ,  amount : " + amount+ " , vpa : " + vpa + " , Mid : " + Mid+"  , PARAM3 Status :: "+pstatus);

				if (pstatus.equalsIgnoreCase("VE"))
				{	
					resp = "success";
				}
				else if (pstatus.equalsIgnoreCase("F"))
				{
					resp = "failure";
					logger.debug("UpiCheckServlet.java   :: pstatus  ::"+pstatus);
				}
			}
			catch (Exception e)
			{
				logger.debug("Error"+e.getMessage());
			}

			logger.debug("-------------------"+resp);

			out.println(resp);
			out.close();
		}
		else
		{
			logger.debug("UpiCheckServlet.java  ::: MtaackId :: "+mTrackId+" Brute force Attack");
			request.setAttribute("errorMsg", "Error 10052 : Error while Processing Transaction Request.");
			request.getRequestDispatcher("txnValidationErrors.jsp").include(request, response);
			
		}

	}

	public String checkVPA(String merchantId,String vpa, String merchantRefNo,String status,String upiKeyHDFC,String custMail,String custMobile,String orderID) throws Exception
	{
		// merchant id as a mid 
		String sData = merchantId+"|"+merchantRefNo+"|"+vpa+"|"+status+"|"+orderID+"|"+custMail +"|"+custMobile+"||||||NA|NA";
		logger.debug("UpiCheckServlet.java ::: checkVPA() :: UPI Request Normal Text :: "+sData);

		logger.debug("UpiCheckServlet.java ::: encrypt sData" + upiKeyHDFC);
		sData = new UPISecurity().encrypt(sData, upiKeyHDFC); 
		logger.debug("UpiCheckServlet.java ::: encrypt sData" + sData);

		JSONObject json = new JSONObject();
		json.put("requestMsg", sData);
		json.put("pgMerchantId", merchantId);

		sData = S2SCall.secureServerCall(upiCheckVPAUrlHDFC, json.toString());
		logger.debug("UpiCheckServlet.java ::: checkVPA() :: Decrypted Data : " + sData);
		if(sData != null)
		{
			sData = new UPISecurity().decrypt(sData, upiKeyHDFC);
			logger.debug("UpiCheckServlet.java ::: checkVPA() :: Decrypted Data : " + sData);
			sData = this.createJsonResponse(sData);
			logger.debug("UpiCheckServlet.java ::: checkVPA() :: Json Response Data : " + sData);
		}

		return sData;

	}

	private String createJsonResponse(String pipeSeperatedValue)
	{
		JSONObject json = new JSONObject();

		String[] strResp = pipeSeperatedValue.split("\\|");
		for (int i = 0; i < strResp.length; i++) 
		{
			String value = strResp[i];
			json.put("PARAM"+i, value);
		}

		return json.toString();
	}
}
