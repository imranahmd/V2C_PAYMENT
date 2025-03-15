//package com.rew.payment.api;
//
//import java.io.Serializable;
//
//public class YBLPayReqPojo implements Serializable{
//
//	private static final long serialVersionUID = 1L;
//	
//	private String pgMerchantId = "";
//	private String orderNo = "";
//	private String transactionNote = "";
//	private String amount = "";
//	private String currency = "INR";
//	private String paymentType = "P2P";
//	private String transactionType = "PAY";
//	private String merchantCategoryCode = "";
//	private String expiryTime = ""; //optional (YYYY:MM:DD HH:mm:ss)
//	private String payeeAccoountNo = ""; //cond optional
//	private String payeeIFSC = ""; //cond optional
//	private String payeeAadhaarNo = ""; //optional
//	private String payeeMobileNo = ""; //optional
//	private String payeeVPA = ""; //mandatory
//	private String subMerchantId = ""; //not to be used
//	private String whitelistAccount = ""; // not to be used
//	private String payeeMmid = ""; //optional
//	private String refUrl = "";
//	private String transferType = "UPI";
//	private String payeeName = "";
//	private String payeeAddress = "";
//	private String payeeEmail = "";
//	private String payerAccountNo = "";
//	private String payerIFSC = "";
//	private String payerMobNo = "";
//	private String payeeVPAType = ""; //Account or VPA
//	private String addF1 = "";
//	private String addF2 = "";
//	private String addF3 = "";
//	private String addF4 = "";
//	private String addF5 = "";
//	private String addF6 = "";
//	private String addF7 = "";
//	private String addF8 = "";
//	private String addF9 = "";
//	private String addF10 = "";
//	
//	public String getPgMerchantId() {
//		return pgMerchantId;
//	}
//	public void setPgMerchantId(String pgMerchantId) {
//		this.pgMerchantId = pgMerchantId;
//	}
//	public String getOrderNo() {
//		return orderNo;
//	}
//	public void setOrderNo(String orderNo) {
//		this.orderNo = orderNo;
//	}
//	public String getTransactionNote() {
//		return transactionNote;
//	}
//	public void setTransactionNote(String transactionNote) {
//		this.transactionNote = transactionNote;
//	}
//	public String getAmount() {
//		return amount;
//	}
//	public void setAmount(String amount) {
//		this.amount = amount;
//	}
//	public String getCurrency() {
//		return currency;
//	}
//	public void setCurrency(String currency) {
//		this.currency = currency;
//	}
//	public String getPaymentType() {
//		return paymentType;
//	}
//	public void setPaymentType(String paymentType) {
//		this.paymentType = paymentType;
//	}
//	public String getTransactionType() {
//		return transactionType;
//	}
//	public void setTransactionType(String transactionType) {
//		this.transactionType = transactionType;
//	}
//	public String getMerchantCategoryCode() {
//		return merchantCategoryCode;
//	}
//	public void setMerchantCategoryCode(String merchantCategoryCode) {
//		this.merchantCategoryCode = merchantCategoryCode;
//	}
//	public String getExpiryTime() {
//		return expiryTime;
//	}
//	public void setExpiryTime(String expiryTime) {
//		this.expiryTime = expiryTime;
//	}
//	public String getPayeeAccoountNo() {
//		return payeeAccoountNo;
//	}
//	public void setPayeeAccoountNo(String payeeAccoountNo) {
//		this.payeeAccoountNo = payeeAccoountNo;
//	}
//	public String getPayeeIFSC() {
//		return payeeIFSC;
//	}
//	public void setPayeeIFSC(String payeeIFSC) {
//		this.payeeIFSC = payeeIFSC;
//	}
//	public String getPayeeAadhaarNo() {
//		return payeeAadhaarNo;
//	}
//	public void setPayeeAadhaarNo(String payeeAadhaarNo) {
//		this.payeeAadhaarNo = payeeAadhaarNo;
//	}
//	public String getPayeeMobileNo() {
//		return payeeMobileNo;
//	}
//	public void setPayeeMobileNo(String payeeMobileNo) {
//		this.payeeMobileNo = payeeMobileNo;
//	}
//	public String getPayeeVPA() {
//		return payeeVPA;
//	}
//	public void setPayeeVPA(String payeeVPA) {
//		this.payeeVPA = payeeVPA;
//	}
//	public String getSubMerchantId() {
//		return subMerchantId;
//	}
//	public void setSubMerchantId(String subMerchantId) {
//		this.subMerchantId = subMerchantId;
//	}
//	public String getWhitelistAccount() {
//		return whitelistAccount;
//	}
//	public void setWhitelistAccount(String whitelistAccount) {
//		this.whitelistAccount = whitelistAccount;
//	}
//	public String getPayeeMmid() {
//		return payeeMmid;
//	}
//	public void setPayeeMmid(String payeeMmid) {
//		this.payeeMmid = payeeMmid;
//	}
//	public String getRefUrl() {
//		return refUrl;
//	}
//	public void setRefUrl(String refUrl) {
//		this.refUrl = refUrl;
//	}
//	public String getTransferType() {
//		return transferType;
//	}
//	public void setTransferType(String transferType) {
//		this.transferType = transferType;
//	}
//	public String getPayeeName() {
//		return payeeName;
//	}
//	public void setPayeeName(String payeeName) {
//		this.payeeName = payeeName;
//	}
//	public String getPayeeAddress() {
//		return payeeAddress;
//	}
//	public void setPayeeAddress(String payeeAddress) {
//		this.payeeAddress = payeeAddress;
//	}
//	public String getPayeeEmail() {
//		return payeeEmail;
//	}
//	public void setPayeeEmail(String payeeEmail) {
//		this.payeeEmail = payeeEmail;
//	}
//	public String getPayerAccountNo() {
//		return payerAccountNo;
//	}
//	public void setPayerAccountNo(String payerAccountNo) {
//		this.payerAccountNo = payerAccountNo;
//	}
//	public String getPayerIFSC() {
//		return payerIFSC;
//	}
//	public void setPayerIFSC(String payerIFSC) {
//		this.payerIFSC = payerIFSC;
//	}
//	public String getPayerMobNo() {
//		return payerMobNo;
//	}
//	public void setPayerMobNo(String payerMobNo) {
//		this.payerMobNo = payerMobNo;
//	}
//	public String getPayeeVPAType() {
//		return payeeVPAType;
//	}
//	public void setPayeeVPAType(String payeeVPAType) {
//		this.payeeVPAType = payeeVPAType;
//	}
//	public String getAddF1() {
//		return addF1;
//	}
//	public void setAddF1(String addF1) {
//		this.addF1 = addF1;
//	}
//	public String getAddF2() {
//		return addF2;
//	}
//	public void setAddF2(String addF2) {
//		this.addF2 = addF2;
//	}
//	public String getAddF3() {
//		return addF3;
//	}
//	public void setAddF3(String addF3) {
//		this.addF3 = addF3;
//	}
//	public String getAddF4() {
//		return addF4;
//	}
//	public void setAddF4(String addF4) {
//		this.addF4 = addF4;
//	}
//	public String getAddF5() {
//		return addF5;
//	}
//	public void setAddF5(String addF5) {
//		this.addF5 = addF5;
//	}
//	public String getAddF6() {
//		return addF6;
//	}
//	public void setAddF6(String addF6) {
//		this.addF6 = addF6;
//	}
//	public String getAddF7() {
//		return addF7;
//	}
//	public void setAddF7(String addF7) {
//		this.addF7 = addF7;
//	}
//	public String getAddF8() {
//		return addF8;
//	}
//	public void setAddF8(String addF8) {
//		this.addF8 = addF8;
//	}
//	public String getAddF9() {
//		return addF9;
//	}
//	public void setAddF9(String addF9) {
//		this.addF9 = addF9;
//	}
//	public String getAddF10() {
//		return addF10;
//	}
//	public void setAddF10(String addF10) {
//		this.addF10 = addF10;
//	}
//
//	public String getRequestData()
//	{
//		StringBuilder data = new StringBuilder(getPgMerchantId()).append("|");
//		data.append(getOrderNo()).append("|")
//		.append(getTransactionNote()).append("|")
//		.append(getAmount()).append("|")
//		.append(getCurrency()).append("|")
//		.append(getPaymentType()).append("|")
//		.append(getTransactionType()).append("|")
//		.append(getMerchantCategoryCode()).append("|")
//		.append(getExpiryTime()).append("|")
//		.append(getPayeeAccoountNo()).append("|")
//		.append(getPayeeIFSC()).append("|")
//		.append(getPayeeAadhaarNo()).append("|")
//		.append(getPayeeMobileNo()).append("|")
//		.append(getPayeeVPA()).append("|")
//		.append(getSubMerchantId()).append("|")
//		.append(getWhitelistAccount()).append("|")
//		.append(getPayeeMmid()).append("|")
//		.append(getRefUrl()).append("|")
//		.append(getTransferType()).append("|")
//		.append(getPayeeName()).append("|")
//		.append(getPayeeAddress()).append("|")
//		.append(getPayeeEmail()).append("|")
//		.append(getPayerAccountNo()).append("|")
//		.append(getPayerIFSC()).append("|")
//		.append(getPayerMobNo()).append("|")
//		.append(getPayeeVPAType()).append("|")
//		.append(getAddF1()).append("|").append(getAddF2()).append("|").append(getAddF3()).append("|")
//		.append(getAddF4()).append("|").append(getAddF5()).append("|").append(getAddF6()).append("|")
//		.append(getAddF7()).append("|").append(getAddF8()).append("|").append(getAddF9()).append("|")
//		.append(getAddF10()).append("|");
//		
//		return data.toString();
//		
//	}
//	
//}
