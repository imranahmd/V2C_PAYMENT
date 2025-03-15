package com.rew.payment.utility;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.pg.db.DataManager;
import com.rew.pg.dto.TransactionMaster;

public class GetTxnDetails extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(GetTxnDetails.class);

	private static final long serialVersionUID = 1L;

	public GetTxnDetails() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Get Method not supported");
	}

	protected void doOption(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Option Method not Allowed.");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Cache-control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "-1");
		PrintWriter out = response.getWriter();

		
		String mid = request.getParameter("AuthID");
		String txnid = request.getParameter("CustRefNum");
		String respJSON = null;
		String finalRU=null;
		logger.info("GetTxnDetails.java ::: Merchant Id : " + mid + " and Merchant Order No. : " + txnid);
		try {
			logger.error("Start try block to get transaction details for merchantId= " + mid + " and TxnId= " + txnid);
			DataManager dm = new DataManager();
			TransactionMaster TM = dm.getTxnVerification(txnid, mid);
			if (TM != null) {
				respJSON = PGUtils.generateVerificationResponse(TM);
			}

			if ((TM.getReturn_url() != null) && (!TM.getReturn_url().isEmpty())
					&& (!TM.getReturn_url().equalsIgnoreCase("NA"))) {
				logger.info("transaction master is not null or empty or na");
				logger.info("response=====>" + respJSON);
                
				String alternateRU=dm.getAlternateRUPresent(mid);
				if(alternateRU != null && !alternateRU.isEmpty()) {
					finalRU=alternateRU;
				}else {
					finalRU=TM.getReturn_url();
				}
				int status = dm.insertTBLGetTxnDetails(mid, txnid, TM.getId(), respJSON, finalRU,
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				logger.info("posting url=======>" + finalRU);

			/*	if (status != 0) { added on 29-10-22
					logger.info("In block of push response to merchant");
					pushResponseToMerchant(finalRU, respJSON);
				} else {
					logger.error("insertion failed response not send");
				}*/
			}
			logger.error("End try block to get transaction details for merchantId= " + mid + " and TxnId= " + txnid);
		} catch (Exception ex) {
			logger.error("Exception to get transaction details for merchantId= " + mid + " and TxnId= " + txnid);
			logger.error("Exception is " + ex.getMessage());
		} finally {
			out.write(respJSON);
			out.flush();
			out.close();
			logger.error("Finally response to get transaction details for merchantId= " + mid + " and TxnId= " + txnid);
		}

	}

	private void pushResponseToMerchant(String pushURL, String respJSON) {
		URL url = null;
		HttpURLConnection con = null;
		OutputStreamWriter wr = null;
		try {
			url = new URL(pushURL);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setReadTimeout(30000);
			con.setDoOutput(true);
			con.connect();

			wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(respJSON);

			if ((con.getResponseCode() == 200) || (con.getResponseCode() == 202)) {
				logger.info("GetTxnDetails.java ::: pushResponseToMerchant() :: Response sent Successfully on Push URL : "
						+ pushURL);
			} else {
				logger.info("GetTxnDetails.java ::: pushResponseToMerchant() :: Response Code : " + con.getResponseCode());
			}
		} catch (Exception e) {
			logger.info(e.getMessage() + " , pushURL=" + pushURL + " , reponse=" + respJSON);
			logger.error(
					"GetTxnDetails.java ::: pushResponseToMerchant() :: Error Occurred while Posting Response to Push URL : ",
					e);
		} finally {
			if (wr != null) {
				try {
					wr.flush();
					wr.close();
				} catch (IOException e) {
					logger.error("Problem with OutputStreamWriter closing",e);
				}
			}
			if (con != null) {
				con.disconnect();
			}
		}
	}

}
