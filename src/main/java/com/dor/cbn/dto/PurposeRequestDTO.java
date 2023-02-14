package com.dor.cbn.dto;

import java.util.Date;

public class PurposeRequestDTO {
	private Integer serialNo;
	private String purposeName;
	private String purposeShortCode;
	private Date dateOfCreation;
	public String getPurposeName() {
		return purposeName;
	}
	public void setPurposeName(String purposeName) {
		this.purposeName = purposeName;
	}
	public String getPurposeShortCode() {
		return purposeShortCode;
	}
	public void setPurposeShortCode(String purposeShortCode) {
		this.purposeShortCode = purposeShortCode;
	}
	public Date getDateOfCreation() {
		return dateOfCreation;
	}
	public void setDateOfCreation(Date dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}
	public Integer getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}
	
	
}
