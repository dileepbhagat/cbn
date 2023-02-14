package com.dor.cbn.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="m_drug_type", schema="cbn")

public class DrugTypeEntity {
	
	@Id
	@Column(name="serial_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer serialNo;
	
	@Column(name="drug_type_name")
	private String drugTypeName;
	
	@Column(name = "drug_type_short_code")
	private String drugTypeShortCode;
	
	@Column(name = "valid")
	private Boolean valid;
	
	@Column(name = "created_on")
	private Date createdOn;
	
	@Column(name = "updated_on")
	private Date updatedOn;

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

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

}
