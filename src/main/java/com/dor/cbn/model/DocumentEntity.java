package com.dor.cbn.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="m_document", schema="cbn")

public class DocumentEntity {
	
	@Id
	@Column(name="serial_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer serialNo;
	
	@Column(name="document_name")
	private String documentName;
	
	@Column(name = "document_short_code")
	private String documentShortCode;
	
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
