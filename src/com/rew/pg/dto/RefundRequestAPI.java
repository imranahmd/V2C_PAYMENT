package com.rew.pg.dto;

public class RefundRequestAPI {
  private String id;
  private String merchantId;
  private String txnId;
  private String dateTime;
  private String amount;
  private String custMobile;
  private String custMail;
  private String returnURL;
  private String checksum;
  private String apiKey;
  private String udf1;
  private String udf2;
  private String udf3;
  private String udf4;
  private String udf5;
  private String channelId;
  private String instrumentId;
  private String bankId;
  private String txnType;
  private String productId;
  private String return_url;
  private String serviceRRN;
  private String serviceTxnId;
  private String transStatus;
  private String respStatus;
  private String respMessage;
  private String respDateTime;
  private String hostAddress;
  private String processId;
  private String surcharge;
  private String serviceAuthId;
  private String uploadedOn;
  private String uploadedBy;
  private String modified_On;
  private String modified_By;
  private String isMultiSettlement;
  private String cardType;
  private String cardDetails;
  private String errorCode;
  private String spErrorCode;
  private String rmsScore;
  private String rmsReason;
  private String reconStaus;
  private String refund_amount;
  private String refund_type;
  private String refundRequestId;
  public RefundRequestAPI() {}
  
  public String getRmsScore() {
    return rmsScore;
  }
  
  public void setRmsScore(String rmsScore) { this.rmsScore = rmsScore; }
  
  public String getRmsReason() {
    return rmsReason;
  }
  
  public void setRmsReason(String rmsReason) { this.rmsReason = rmsReason; }
  
  public String getProductId() {
    return productId;
  }
  
  public void setProductId(String productId) { this.productId = productId; }
  
  public String getMerchantId() {
    return merchantId;
  }
  
  public void setMerchantId(String merchantId) { this.merchantId = merchantId; }
  
  public String getTxnId() {
    return txnId;
  }
  
  public void setTxnId(String txnId) { this.txnId = txnId; }
  
  public String getDateTime() {
    return dateTime;
  }
  
  public void setDateTime(String dateTime) { this.dateTime = dateTime; }
  
  public String getAmount() {
    return amount;
  }
  
  public void setAmount(String amount) { this.amount = amount; }
  
  public String getCustMobile()
  {
    return custMobile;
  }
  
  public void setCustMobile(String custMobile) { this.custMobile = custMobile; }
  
  public String getCustMail() {
    return custMail;
  }
  
  public void setCustMail(String custMail) { this.custMail = custMail; }
  
  public String getReturnURL() {
    return returnURL;
  }
  
  public void setReturnURL(String returnURL) { this.returnURL = returnURL; }
  
  public String getApiKey() {
    return apiKey;
  }
  
  public void setApiKey(String apiKey) { this.apiKey = apiKey; }
  
  public String getChannelId() {
    return channelId;
  }
  
  public void setChannelId(String channelId) { this.channelId = channelId; }
  
  public String getBankId() {
    return bankId;
  }
  
  public void setBankId(String bankId) { this.bankId = bankId; }
  
  public String getTxnType() {
    return txnType;
  }
  
  public void setTxnType(String txnType) { this.txnType = txnType; }
  
  public String getHostAddress() {
    return hostAddress;
  }
  
  public String getRespDateTime() { return respDateTime; }
  
  public void setRespDateTime(String respDateTime) {
    this.respDateTime = respDateTime;
  }
  
  public String getTransStatus() { return transStatus; }
  
  public void setTransStatus(String transStatus) {
    this.transStatus = transStatus;
  }
  
  public String getId() { return id; }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getReturn_url() {
    return return_url;
  }
  
  public void setReturn_url(String return_url) { this.return_url = return_url; }
  
  public String getUdf1()
  {
    return udf1;
  }
  
  public void setUdf1(String udf1) { this.udf1 = udf1; }
  
  public String getUdf2() {
    return udf2;
  }
  
  public void setUdf2(String udf2) { this.udf2 = udf2; }
  
  public String getUdf3() {
    return udf3;
  }
  
  public void setUdf3(String udf3) { this.udf3 = udf3; }
  
  public String getUdf4() {
    return udf4;
  }
  
  public void setUdf4(String udf4) { this.udf4 = udf4; }
  
  public String getUdf5() {
    return udf5;
  }
  
  public void setUdf5(String udf5) { this.udf5 = udf5; }
  
  public String getServiceRRN() {
    return serviceRRN;
  }
  
  public void setServiceRRN(String serviceRRN) { this.serviceRRN = serviceRRN; }
  
  public String getServiceTxnId() {
    return serviceTxnId;
  }
  
  public void setServiceTxnId(String serviceTxnId) { this.serviceTxnId = serviceTxnId; }
  
  public String getChecksum() {
    return checksum;
  }
  
  public void setChecksum(String checksum) { this.checksum = checksum; }
  
  public String getRespStatus() {
    return respStatus;
  }
  
  public void setRespStatus(String respStatus) { this.respStatus = respStatus; }
  
  public String getRespMessage() {
    return respMessage;
  }
  
  public void setRespMessage(String respMessage) { this.respMessage = respMessage; }
  
  public void setHostAddress(String hostAddress) {
    this.hostAddress = hostAddress;
  }
  
  public String getInstrumentId() {
    return instrumentId;
  }
  
  public void setInstrumentId(String instrumentId) { this.instrumentId = instrumentId; }
  
  public String getProcessId() {
    return processId;
  }
  
  public void setProcessId(String processId) { this.processId = processId; }
  
  public String getSurcharge() {
    return surcharge;
  }
  
  public void setSurcharge(String surcharge) { this.surcharge = surcharge; }
  
  public String getServiceAuthId() {
    return serviceAuthId;
  }
  
  public void setServiceAuthId(String serviceAuthId) { this.serviceAuthId = serviceAuthId; }
  
  public String getUploadedOn() {
    return uploadedOn;
  }
  
  public void setUploadedOn(String uploadedOn) { this.uploadedOn = uploadedOn; }
  
  public String getUploadedBy() {
    return uploadedBy;
  }
  
  public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }
  
  public String getModified_On() {
    return modified_On;
  }
  
  public void setModified_On(String modified_On) { this.modified_On = modified_On; }
  
  public String getModified_By() {
    return modified_By;
  }
  
  public void setModified_By(String modified_By) { this.modified_By = modified_By; }
  
  public String getIsMultiSettlement() {
    return isMultiSettlement;
  }
  
  public void setIsMultiSettlement(String isMultiSettlement) { this.isMultiSettlement = isMultiSettlement; }
  
  public String getCardType() {
    return cardType;
  }
  
  public void setCardType(String cardType) { this.cardType = cardType; }
  
  public String getCardDetails() {
    return cardDetails;
  }
  
  public void setCardDetails(String cardDetails) { this.cardDetails = cardDetails; }
  
  public String getErrorCode() {
    return errorCode;
  }
  
  public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
  
  public String getSpErrorCode() {
    return spErrorCode;
  }
  
  public void setSpErrorCode(String spErrorCode) { this.spErrorCode = spErrorCode; }
  
  public String getReconStaus() {
    return reconStaus;
  }
  
  public void setReconStaus(String reconStaus) { this.reconStaus = reconStaus; }
  
  public String getRefund_amount() {
    return refund_amount;
  }
  
  public void setRefund_amount(String refund_amount) { this.refund_amount = refund_amount; }
  
  public String getRefund_type() {
    return refund_type;
  }
  
  public void setRefund_type(String refund_type) { this.refund_type = refund_type; }

public String getRefundRequestId() {
	return refundRequestId;
}

public void setRefundRequestId(String refundRequestId) {
	this.refundRequestId = refundRequestId;
}
}
