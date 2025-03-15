package com.rew.pg.dto;



import java.util.Date;

public class ServiceProviderDto {
    private long sp_id;
    private String sp_name;
    private String master_mid;
    private String master_tid;
    private String sp_class_invoker;
    private String api_key;
    private String udf_1;
    private String udf_2;
    private String udf_3;
    private String udf_4;
    private String udf_5;
    private Date CreatedOn;
    private String CreatedBy;
    private Date ModifiedOn;
    private String ModifiedBy;
    private char IsDeleted;
    private String refund_processor;
    private String bankIds;
    private String instrumentIds;
    private char isRefundAPI;
    private String cutoff;
	public long getSp_id() {
		return sp_id;
	}
	public void setSp_id(long sp_id) {
		this.sp_id = sp_id;
	}
	public String getSp_name() {
		return sp_name;
	}
	public void setSp_name(String sp_name) {
		this.sp_name = sp_name;
	}
	public String getMaster_mid() {
		return master_mid;
	}
	public void setMaster_mid(String master_mid) {
		this.master_mid = master_mid;
	}
	public String getMaster_tid() {
		return master_tid;
	}
	public void setMaster_tid(String master_tid) {
		this.master_tid = master_tid;
	}
	public String getSp_class_invoker() {
		return sp_class_invoker;
	}
	public void setSp_class_invoker(String sp_class_invoker) {
		this.sp_class_invoker = sp_class_invoker;
	}
	public String getApi_key() {
		return api_key;
	}
	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}
	public String getUdf_1() {
		return udf_1;
	}
	public void setUdf_1(String udf_1) {
		this.udf_1 = udf_1;
	}
	public String getUdf_2() {
		return udf_2;
	}
	public void setUdf_2(String udf_2) {
		this.udf_2 = udf_2;
	}
	public String getUdf_3() {
		return udf_3;
	}
	public void setUdf_3(String udf_3) {
		this.udf_3 = udf_3;
	}
	public String getUdf_4() {
		return udf_4;
	}
	public void setUdf_4(String udf_4) {
		this.udf_4 = udf_4;
	}
	public String getUdf_5() {
		return udf_5;
	}
	public void setUdf_5(String udf_5) {
		this.udf_5 = udf_5;
	}
	public Date getCreatedOn() {
		return CreatedOn;
	}
	public void setCreatedOn(Date createdOn) {
		CreatedOn = createdOn;
	}
	public String getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}
	public Date getModifiedOn() {
		return ModifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		ModifiedOn = modifiedOn;
	}
	public String getModifiedBy() {
		return ModifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		ModifiedBy = modifiedBy;
	}
	public char getIsDeleted() {
		return IsDeleted;
	}
	public void setIsDeleted(char isDeleted) {
		IsDeleted = isDeleted;
	}
	public String getRefund_processor() {
		return refund_processor;
	}
	public void setRefund_processor(String refund_processor) {
		this.refund_processor = refund_processor;
	}
	public String getBankIds() {
		return bankIds;
	}
	public void setBankIds(String bankIds) {
		this.bankIds = bankIds;
	}
	public String getInstrumentIds() {
		return instrumentIds;
	}
	public void setInstrumentIds(String instrumentIds) {
		this.instrumentIds = instrumentIds;
	}
	public char getIsRefundAPI() {
		return isRefundAPI;
	}
	public void setIsRefundAPI(char isRefundAPI) {
		this.isRefundAPI = isRefundAPI;
	}
	public String getCutoff() {
		return cutoff;
	}
	public void setCutoff(String cutoff) {
		this.cutoff = cutoff;
	}

    
}
