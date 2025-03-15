package com.rew.payment.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.rew.pg.db.DataManager;
import com.rew.pg.dto.MerchantDTO;
import com.rew.pg.dto.TransactionMaster;

public class IndexVASDTO {
	
	private String keyEncKey=null;
	private TransactionMaster TM=null;
	private MerchantDTO MT=null;
	private List returnList=null;
	private List<String> MerchantPageConfige=null;
	private List<String> banklist=null;
	private Vector<List<String>> storedCardDetails=null;
	private ArrayList<String> walletlist2=null;
	
	private ArrayList<String> walletlist1=null;
	
	private ArrayList<String> ecmsBanks=null;
	
	public ArrayList<String> getEcmsBanks() {
		return ecmsBanks;
	}
	public void setEcmsBanks(ArrayList<String> ecmsBanks) {
		this.ecmsBanks = ecmsBanks;
	}
	public String getKeyEncKey() {
		return keyEncKey;
	}
	public void setKeyEncKey(String keyEncKey) {
		this.keyEncKey = keyEncKey;
	}
	public TransactionMaster getTM() {
		return TM;
	}
	public void setTM(TransactionMaster tM) {
		TM = tM;
	}
	public MerchantDTO getMT() {
		return MT;
	}
	public void setMT(MerchantDTO mT) {
		MT = mT;
	}
	public List getReturnList() {
		return returnList;
	}
	public void setReturnList(List returnList) {
		this.returnList = returnList;
	}
	public List<String> getMerchantPageConfige() {
		return MerchantPageConfige;
	}
	public void setMerchantPageConfige(List<String> merchantPageConfige) {
		MerchantPageConfige = merchantPageConfige;
	}
	public List<String> getBanklist() {
		return banklist;
	}
	public void setBanklist(List<String> banklist) {
		this.banklist = banklist;
	}
	public Vector<List<String>> getStoredCardDetails() {
		return storedCardDetails;
	}
	public void setStoredCardDetails(Vector<List<String>> storedCardDetails) {
		this.storedCardDetails = storedCardDetails;
	}
	
	
	public ArrayList<String> getWalletlist2() {
		return walletlist2;
	}
	public void setWalletlist(ArrayList<String> walletlist2) {
		this.walletlist2 = walletlist2;
	}
	
	
	public ArrayList<String> getWalletlist1() {
		return walletlist1;
	}
	public void setWalletlist1(ArrayList<String> walletlist1) {
		this.walletlist1 = walletlist1;
	}
	
	
	
	public IndexVASDTO getIndexVASDTOData(String txnid){
		DataManager dm=new DataManager();
		this.keyEncKey=dm.getKeyEncKey();
		this.setKeyEncKey(this.keyEncKey);
		
		
		this.TM=dm.getTxnMaster(txnid);
		this.setTM(this.TM);
		
		this.MT=dm.getMerchant(this.TM.getMerchantId());
		this.setMT(this.MT);
		
		this.returnList=dm.getInstrumentBankList(this.TM.getMerchantId(),this.TM.getAmount());
		this.setReturnList(this.returnList);
		
		this.MerchantPageConfige=dm.getPageConfige(this.TM.getMerchantId());
		this.setMerchantPageConfige(this.MerchantPageConfige);
		
		this.banklist=dm.getEMIBankList(this.TM.getMerchantId());
		this.setBanklist(this.banklist);
		
		this.storedCardDetails=dm.getStoredCards(this.TM.getCustMail(),this.TM.getCustMobile(),this.TM.getMerchantId());
		this.setStoredCardDetails(this.storedCardDetails);
		
		this.walletlist2=dm.getWalletList2(this.TM.getMerchantId());
		this.walletlist1=dm.getWalletList1(this.TM.getMerchantId());
		this.ecmsBanks=dm.getECMSBankList(this.TM.getMerchantId());
		return this;
	}
	
	
}
