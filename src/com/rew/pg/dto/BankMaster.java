package com.rew.pg.dto;

public class BankMaster {
  private String bankId;
  private String bankName;
  
  public BankMaster() {}
  
  public String getBankId() { return bankId; }
  
  public void setBankId(String bankId) {
    this.bankId = bankId;
  }
  
  public String getBankName() { return bankName; }
  
  public void setBankName(String bankName) {
    this.bankName = bankName;
  }
}
