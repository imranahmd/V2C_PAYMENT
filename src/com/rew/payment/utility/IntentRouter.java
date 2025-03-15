package com.rew.payment.utility;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.payment.router.IDynamicPGSelector;
import com.rew.pg.db.DataManager;
import com.rew.pg.dto.TransactionMaster;

public class IntentRouter {

    private static Logger logger = LoggerFactory.getLogger(IntentRouter.class);

	
	public String RoutingSystem(String Id, String amount,String MId,TransactionMaster TM,HttpServletRequest request)
	{
		String redirectPage =null;

		try {
		logger.info("Inside in Routing Intent SYSTEM:::::::::: "+Id);
String retamount,sClassFileLoader,spId;
int payout;
		String bankId="UPI";
		String sInstrumentId="UPI";
		DataManager datamanager = new DataManager();
		List<String> surchargeData = datamanager.getCalculateSurcharge(bankId, sInstrumentId, TM.getAmount(),
				TM.getMerchantId(), "", TM.getId());
		retamount = surchargeData.get(0);
		sClassFileLoader = surchargeData.get(1);
		spId = surchargeData.get(2);
		payout = Integer.valueOf(surchargeData.get(3));
		logger.info("Inside in Routing Intent SYSTEM::::SPID:::::: "+spId);
		logger.info("Inside in Routing Intent SYSTEM::::sClassFileLoader:::::: "+sClassFileLoader);
		request.setAttribute("id", TM.getId());
		request.setAttribute("txnId", TM.getId());
		request.setAttribute("amount", amount + "");
		request.setAttribute("custMob", TM.getCustMobile());
		request.setAttribute("custMail", TM.getCustMail());
		request.setAttribute("txnType", "nonseamless");
		request.setAttribute("Mid", TM.getMerchantId());
		request.setAttribute("bankId", bankId);
		request.setAttribute("spId", spId);
		request.setAttribute("instrumentId", sInstrumentId);
		request.setAttribute("Merchant_txn_id", TM.getTxnId());
		request.setAttribute("VPA", "INTENT");
		request.setAttribute("dateTime", TM.getDateTime());
		request.setAttribute("isVas", "0");
	
		
		
		if(spId!=null) {
			TM.setProcessId(spId);
			datamanager.updateTxn(TM);
		IDynamicPGSelector pgSelector = null;
		pgSelector = (IDynamicPGSelector) Class.forName(sClassFileLoader).newInstance();

		redirectPage = pgSelector.redirectToPG(request);
		
		}else {
			logger.info("SPID NotFound:::::::::::::::::::::: ");
		}
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return redirectPage;
		
	}
}
