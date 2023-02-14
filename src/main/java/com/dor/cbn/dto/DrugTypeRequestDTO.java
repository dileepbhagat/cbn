package com.dor.cbn.dto;
import java.util.Date;

public class DrugTypeRequestDTO {
	
	private Integer serialNo;
	private String drugTypeName;
	private String drugTypeShortCode;
	private Date dateOfCreation;
	
	public Integer getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}
	public String getDrugTypeName() {
		return drugTypeName;
	}
	public void setDrugTypeName(String drugTypeName) {
		this.drugTypeName = drugTypeName;
	}
	public String getDrugTypeShortCode() {
		return drugTypeShortCode;
	}
	public void setDrugTypeShortCode(String drugTypeShortCode) {
		this.drugTypeShortCode = drugTypeShortCode;
	}
	public Date getDateOfCreation() {
		return dateOfCreation;
	}
	public void setDateOfCreation(Date dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}
	
}
