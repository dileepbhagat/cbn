package com.dor.cbn.dto;

import java.util.Date;

public class DocumentRequestDTO {
	
	private Integer serialNo;
	private String documentName;
	private String documentShortCode;
	private Date dateOfCreation;
	public Integer getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}
	
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public String getDocumentShortCode() {
		return documentShortCode;
	}
	public void setDocumentShortCode(String documentShortCode) {
		this.documentShortCode = documentShortCode;
	}
	public Date getDateOfCreation() {
		return dateOfCreation;
	}
	public void setDateOfCreation(Date dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}
	
}
