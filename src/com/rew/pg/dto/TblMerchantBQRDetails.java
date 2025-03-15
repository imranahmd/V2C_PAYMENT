package com.rew.pg.dto;

import java.io.Serializable;

public class TblMerchantBQRDetails implements Serializable {
  private int id;
  
  private String tag_52;
  
  private String tag_62b;
  
  private String tag_26a;
  
  private String tag_26b;
  
  private String npci_pan;
  
  private String tag_53;
  
  private String pan_ifsc;
  
  private String visa_pan;
  
  private String master_pan;
  
  private String merchant_id;
  
  private String tag_60;
  
  private String tag_61;
  
  private String tag_27a;
  
  private String aggregator_name;
  
  public String getAggregator_name() {
    return this.aggregator_name;
  }
  
  public void setAggregator_name(String aggregator_name) {
    this.aggregator_name = aggregator_name;
  }
  
  public int getId() {
    return this.id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public String getMerchant_id() {
    return this.merchant_id;
  }
  
  public void setMerchant_id(String merchant_id) {
    this.merchant_id = merchant_id;
  }
  
  public String getTag_60() {
    return this.tag_60;
  }
  
  public void setTag_60(String tag_60) {
    this.tag_60 = tag_60;
  }
  
  public String getTag_61() {
    return this.tag_61;
  }
  
  public void setTag_61(String tag_61) {
    this.tag_61 = tag_61;
  }
  
  public String getTag_27a() {
    return this.tag_27a;
  }
  
  public void setTag_27a(String tag_27a) {
    this.tag_27a = tag_27a;
  }
  
  public String getTag_52() {
    return this.tag_52;
  }
  
  public void setTag_52(String tag_52) {
    this.tag_52 = tag_52;
  }
  
  public String getTag_62b() {
    return this.tag_62b;
  }
  
  public void setTag_62b(String tag_62b) {
    this.tag_62b = tag_62b;
  }
  
  public String getTag_26a() {
    return this.tag_26a;
  }
  
  public void setTag_26a(String tag_26a) {
    this.tag_26a = tag_26a;
  }
  
  public String getTag_26b() {
    return this.tag_26b;
  }
  
  public void setTag_26b(String tag_26b) {
    this.tag_26b = tag_26b;
  }
  
  public String getNpci_pan() {
    return this.npci_pan;
  }
  
  public void setNpci_pan(String npci_pan) {
    this.npci_pan = npci_pan;
  }
  
  public String getTag_53() {
    return this.tag_53;
  }
  
  public void setTag_53(String tag_53) {
    this.tag_53 = tag_53;
  }
  
  public String getPan_ifsc() {
    return this.pan_ifsc;
  }
  
  public void setPan_ifsc(String pan_ifsc) {
    this.pan_ifsc = pan_ifsc;
  }
  
  public String getVisa_pan() {
    return this.visa_pan;
  }
  
  public void setVisa_pan(String visa_pan) {
    this.visa_pan = visa_pan;
  }
  
  public String getMaster_pan() {
    return this.master_pan;
  }
  
  public void setMaster_pan(String master_pan) {
    this.master_pan = master_pan;
  }
  
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("tag_52 = " + this.tag_52);
    sb.append("tag_62b = " + this.tag_62b);
    sb.append("tag_26a = " + this.tag_26a);
    sb.append("tag_26b = " + this.tag_26b);
    sb.append("npci_pan = " + this.npci_pan);
    sb.append("tag_53 = " + this.tag_53);
    sb.append("pan_ifsc = " + this.pan_ifsc);
    sb.append("visa_pan = " + this.visa_pan);
    sb.append("master_pan = " + this.master_pan);
    sb.append("aggregator_name = " + this.aggregator_name);
    return sb.toString();
  }
}
