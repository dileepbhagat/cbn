package com.dor.cbn.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="m_purpose", schema="cbn")

public class PurposeEntity {
	
	@Id
	@Column(name="serial_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer serialNo;
	
	@Column(name="purpose_name")
	private String purposeName;
	
	@Column(name = "purpose_short_code")
	private String purposeShortCode;
	
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

