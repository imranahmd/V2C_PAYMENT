package com.rew.pg.dto;

import java.util.List;

public class NPSTCallResponse {

	private String source;
	private String channel;
	private String extTransactionId;
	private String status;
	private String txnId;
	private String checksum;
	private String rrn;
	private String merchant_vpa;
	private String errCode;
	private List<Data> data;
	private String customer_vpa;
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	
	
	public String getExtTransactionId() {
		return extTransactionId;
	}
	public void setExtTransactionId(String extTransactionId) {
		this.extTransactionId = extTransactionId;
	}
	
	
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	
	
	public String getRrn() {
		return rrn;
	}
	public void setRrn(String rrn) {
		this.rrn = rrn;
	}
	
	
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	
	public String getMerchant_vpa() {
		return merchant_vpa;
	}
	public void setMerchant_vpa(String merchant_vpa) {
		this.merchant_vpa = merchant_vpa;
	}
	public String getCustomer_vpa() {
		return customer_vpa;
	}
	public void setCustomer_vpa(String customer_vpa) {
		this.customer_vpa = customer_vpa;
	}
	@Override
	public String toString() {
		return "NPSTCallResponse [source=" + source + ", channel=" + channel + ", extTransactionId=" + extTransactionId
				+ ", status=" + status + ", txnId=" + txnId + ", checksum=" + checksum + ", rrn=" + rrn + ", errCode="
				+ errCode + ", data=" + data + ", customer_vpa=" + customer_vpa +"]";
	}

}

