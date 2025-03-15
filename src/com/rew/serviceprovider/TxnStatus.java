package com.rew.serviceprovider;

public enum TxnStatus {
	TIMED_OUT("To"), FAILED("F");

	public final String value;

	private TxnStatus(String value) {
		this.value = value;
	}

}
