package com.rew.payment.utility;


import com.rew.pg.db.DataManager;
import com.rew.pg.dto.MerchantDTO;
import com.rew.pg.dto.TransactionMaster;

public class IndexVASIRCDTO {

	private String keyEncKey=null;
	private TransactionMaster TM=null;
	private MerchantDTO MT=null;
	
	
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
	
	
	
	
	
	public IndexVASIRCDTO getIndexVASIRCDTOData(String id){
		
		
		DataManager dm=new DataManager();
		this.keyEncKey=dm.getKeyEncKey();
		this.setKeyEncKey(this.keyEncKey);
		
		
		this.TM=dm.getTxnMaster(id);
		this.setTM(this.TM);
		
		this.MT=dm.getMerchant(this.TM.getMerchantId());
		this.setMT(this.MT);
		
		return this;
	}

	
	
}
