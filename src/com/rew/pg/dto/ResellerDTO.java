package com.rew.pg.dto;

public class ResellerDTO {
	
	private String reseller_Id;
	private String reseller_name;
	private String contact_person;
	private String email_id;
	private String contact_number;
	private String legal_name;
	private String brand_name;
	private String business_type;
	private String pin_code;
	private String return_url;
	private String status;
	private String integration_type;
	
	
	public String getReseller_Id() {
		return reseller_Id;
	}
	public void setReseller_Id(String reseller_Id) {
		this.reseller_Id = reseller_Id;
	}
	public String getReseller_name() {
		return reseller_name;
	}
	public void setReseller_name(String reseller_name) {
		this.reseller_name = reseller_name;
	}
	public String getContact_person() {
		return contact_person;
	}
	public void setContact_person(String contact_person) {
		this.contact_person = contact_person;
	}
	public String getEmail_id() {
		return email_id;
	}
	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}
	public String getContact_number() {
		return contact_number;
	}
	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}
	public String getLegal_name() {
		return legal_name;
	}
	public void setLegal_name(String legal_name) {
		this.legal_name = legal_name;
	}
	public String getBrand_name() {
		return brand_name;
	}
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}
	public String getBusiness_type() {
		return business_type;
	}
	public void setBusiness_type(String business_type) {
		this.business_type = business_type;
	}
	public String getPin_code() {
		return pin_code;
	}
	public void setPin_code(String pin_code) {
		this.pin_code = pin_code;
	}
	public String getReturn_url() {
		return return_url;
	}
	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIntegration_type() {
		return integration_type;
	}
	public void setIntegration_type(String integration_type) {
		this.integration_type = integration_type;
	}
	@Override
	public String toString() {
		return "ResellerDTO [reseller_Id=" + reseller_Id + ", reseller_name=" + reseller_name + ", contact_person="
				+ contact_person + ", email_id=" + email_id + ", contact_number=" + contact_number + ", legal_name="
				+ legal_name + ", brand_name=" + brand_name + ", business_type=" + business_type + ", pin_code="
				+ pin_code + ", return_url=" + return_url + ", status=" + status + ", integration_type="
				+ integration_type + "]";
	}
	
	
	
}
