package com.rew.pg.dto;

import java.math.BigDecimal;
import java.util.Arrays;

public class MerchantDTO {
  private String merchantId;
  private String merchantPass;
  private String transactionKey;
  private String name;
  private String status;
  private String[] ip;
  private String merchReturnURL;
  private String merchWebsiteURL;
  private BigDecimal maxTokenSize;
  private String integrationType;
  private String isVAS;
  private String is_push_url;
  private String pushUrl;
  private String isRetry;
  private String mcc;
  private String isSaveCard;
  private String reseller_Id;
  private String CustIP;

	private String is_loader_access;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantPass() {
		return merchantPass;
	}

	public void setMerchantPass(String merchantPass) {
		this.merchantPass = merchantPass;
	}

	public String getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(String transactionKey) {
		this.transactionKey = transactionKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String[] getIp() {
		return ip;
	}

	public void setIp(String[] ip) {
		this.ip = ip;
	}

	public String getMerchReturnURL() {
		return merchReturnURL;
	}

	public void setMerchReturnURL(String merchReturnURL) {
		this.merchReturnURL = merchReturnURL;
	}

	public String getMerchWebsiteURL() {
		return merchWebsiteURL;
	}

	public void setMerchWebsiteURL(String merchWebsiteURL) {
		this.merchWebsiteURL = merchWebsiteURL;
	}

	public BigDecimal getMaxTokenSize() {
		return maxTokenSize;
	}

	public void setMaxTokenSize(BigDecimal maxTokenSize) {
		this.maxTokenSize = maxTokenSize;
	}

	public String getIntegrationType() {
		return integrationType;
	}

	public void setIntegrationType(String integrationType) {
		this.integrationType = integrationType;
	}

	public String getIsVAS() {
		return isVAS;
	}

	public void setIsVAS(String isVAS) {
		this.isVAS = isVAS;
	}

	public String getIs_push_url() {
		return is_push_url;
	}

	public void setIs_push_url(String is_push_url) {
		this.is_push_url = is_push_url;
	}

	public String getPushUrl() {
		return pushUrl;
	}

	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}

	public String getIsRetry() {
		return isRetry;
	}

	public void setIsRetry(String isRetry) {
		this.isRetry = isRetry;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getIsSaveCard() {
		return isSaveCard;
	}

	public void setIsSaveCard(String isSaveCard) {
		this.isSaveCard = isSaveCard;
	}

	public String getReseller_Id() {
		return reseller_Id;
	}

	public void setReseller_Id(String reseller_Id) {
		this.reseller_Id = reseller_Id;
	}

	public String getCustIP() {
		return CustIP;
	}

	public void setCustIP(String custIP) {
		CustIP = custIP;
	}

	public String getIs_loader_access() {
		return is_loader_access;
	}

	public void setIs_loader_access(String is_loader_access) {
		this.is_loader_access = is_loader_access;
	}

	@Override
	public String toString() {
		return "MerchantDTO [merchantId=" + merchantId + ", merchantPass=" + merchantPass + ", transactionKey="
				+ transactionKey + ", name=" + name + ", status=" + status + ", ip=" + Arrays.toString(ip)
				+ ", merchReturnURL=" + merchReturnURL + ", merchWebsiteURL=" + merchWebsiteURL + ", maxTokenSize="
				+ maxTokenSize + ", integrationType=" + integrationType + ", isVAS=" + isVAS + ", is_push_url="
				+ is_push_url + ", pushUrl=" + pushUrl + ", isRetry=" + isRetry + ", mcc=" + mcc + ", isSaveCard="
				+ isSaveCard + ", reseller_Id=" + reseller_Id + ", CustIP=" + CustIP + ", is_loader_access="
				+ is_loader_access + "]";
	}
	
	
	
//	public String getCustIP() {
//		return CustIP;
//	}
//
//	public void setCustIP(String custIP) {
//		CustIP = custIP;
//	}
//
//	public String getIs_loader_access() {
//		return is_loader_access;
//	}
//
//	public void setIs_loader_access(String is_loader_access) {
//		this.is_loader_access = is_loader_access;
//	}
//
//	public MerchantDTO() {
//	
//    }
//  
//  public String getReseller_Id() {
//	return reseller_Id;
//}
//
//
//
//
//public void setReseller_Id(String reseller_Id) {
//	this.reseller_Id = reseller_Id;
//}
//
//
//
//
//public String getIsRetry() {
//		return isRetry;
//	}
//	public void setIsRetry(String isRetry) {
//		this.isRetry = isRetry;
//	}
//  
//  public String getName() { return name; }
//  
//  public void setName(String name) {
//    this.name = name;
//  }
//  
//  public String getStatus() { return status; }
//  
//  public void setStatus(String status) {
//    this.status = status;
//  }
//  
//  public String getMerchantId() { return merchantId; }
//  
//  public void setMerchantId(String merchantId) {
//    this.merchantId = merchantId;
//  }
//  
//  public String getMerchantPass() { return merchantPass; }
//  
//  public void setMerchantPass(String merchantPass) {
//    this.merchantPass = merchantPass;
//  }
//  
//  public String getTransactionKey() { return transactionKey; }
//  
//  public void setTransactionKey(String transactionKey) {
//    this.transactionKey = transactionKey;
//  }
//  
//  public String[] getIp() { return ip; }
//  
//  public void setIp(String[] ip) {
//    this.ip = ip;
//  }
//  
//  public String getMerchReturnURL() { return merchReturnURL; }
//  
//  public void setMerchReturnURL(String merchReturnURL) {
//    this.merchReturnURL = merchReturnURL;
//  }
//  
//  public String getMerchWebsiteURL() { return merchWebsiteURL; }
//  
//  public void setMerchWebsiteURL(String merchWebsiteURL) {
//    this.merchWebsiteURL = merchWebsiteURL;
//  }
//  
//  public BigDecimal getMaxTokenSize() { return maxTokenSize; }
//  
//  public void setMaxTokenSize(BigDecimal maxTokenSize) {
//    this.maxTokenSize = maxTokenSize;
//  }
//  
//  public String getIntegrationType() { return integrationType; }
//  
//  public void setIntegrationType(String integrationType) {
//    this.integrationType = integrationType;
//  }
//  
//  public String getIsVAS() { return isVAS; }
//  
//  public void setIsVAS(String isVAS) {
//    this.isVAS = isVAS;
//  }
//  
//  public String getIsPushEnabled() { return isPushEnabled; }
//  
//  public void setIsPushEnabled(String isPushEnabled) {
//    this.isPushEnabled = isPushEnabled;
//  }
//  
//  public String getPushUrl() { return pushUrl; }
//  
//  public void setPushUrl(String pushUrl) {
//    this.pushUrl = pushUrl;
//  }
//
//  public String getMcc() {
//    return mcc;
//  }
//
//  public void setMcc(String mcc) {
//    this.mcc = mcc;
//  }
//
//public String getIsSaveCard() {
//	return isSaveCard;
//}
//
//public void setIsSaveCard(String isSaveCard) {
//	this.isSaveCard = isSaveCard;
//}
//
//@Override
//public String toString() {
//	return "MerchantDTO [merchantId=" + merchantId + ", merchantPass=" + merchantPass + ", transactionKey="
//			+ transactionKey + ", name=" + name + ", status=" + status + ", ip=" + Arrays.toString(ip)
//			+ ", merchReturnURL=" + merchReturnURL + ", merchWebsiteURL=" + merchWebsiteURL + ", maxTokenSize="
//			+ maxTokenSize + ", integrationType=" + integrationType + ", isVAS=" + isVAS + ", isPushEnabled="
//			+ isPushEnabled + ", pushUrl=" + pushUrl + ", isRetry=" + isRetry + ", mcc=" + mcc + ", isSaveCard="
//			+ isSaveCard + ", reseller_Id=" + reseller_Id + ", CustIP=" + CustIP + ", is_loader_access="
//			+ is_loader_access + "]";
//}
//
//
//
//


}
