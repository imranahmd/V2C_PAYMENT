package com.rew.pg.dto;

import java.util.Arrays;

public class PayModeSpecificData {
	private String subChannel;
	private BankDetails bankDetails;
	public String getSubChannel() {
		return subChannel;
	}
	
	public void setSubChannel(String subChannel) {
		this.subChannel = subChannel;
	}

	public BankDetails getBankDetails() {
		return bankDetails;
	}
	public void setBankDetails(BankDetails bankDetails) {
		this.bankDetails = bankDetails;
	}

	@Override
	public String toString() {
		return "PayModeSpecificData [subChannel=" + subChannel + ", bankDetails=" + bankDetails + "]";
	}
	
	
}
