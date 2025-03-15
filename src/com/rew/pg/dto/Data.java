package com.rew.pg.dto;

public class Data{
	
	private String upiTxnId;
	private String respCode;
	private String respMessge;
	private String txnTime;
	private String requestTime;
	private String amount;
	private String upiId;
	private String customerName;
	private String terminalId;
	private String custRefNo;
	public String getUpiTxnId() {
		return upiTxnId;
	}
	public void setUpiTxnId(String upiTxnId) {
		this.upiTxnId = upiTxnId;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	
	public String getRespMessge() {
		return respMessge;
	}
	public void setRespMessge(String respMessge) {
		this.respMessge = respMessge;
	}
	public String getTxnTime() {
		return txnTime;
	}
	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getUpiId() {
		return upiId;
	}
	public void setUpiId(String upiId) {
		this.upiId = upiId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getCustRefNo() {
		return custRefNo;
	}
	public void setCustRefNo(String custRefNo) {
		this.custRefNo = custRefNo;
	}
	@Override
	public String toString() {
		return "data [upiTxnId=" + upiTxnId + ", respCode=" + respCode
				+ ", respMessge=" + respMessge + ", txnTime=" + txnTime + ", requestTime=" + requestTime + ", amount="
				+ amount + ", upiId=" + upiId + ", customerName=" + customerName + ", terminalId=" + terminalId
				+ ", custRefNo=" + custRefNo + "]";
	}
}

