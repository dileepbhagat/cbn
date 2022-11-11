package com.dor.cbn.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="substance", schema="cbn")

public class SubstanceEntity {
	
	@Id
	@Column(name="substance_name")
	private String substanceName;
	
	@Column(name="substance_type")
	private String substanceType;
	
	@Column(name = "substance_np_name")
	private String substanceNPName;
	
	@Column(name = "substance_other_name")
	private String substanceOtherName;
	
	@Column(name = "substance_chemical_name")
	private String substanceChemicalName;
	
	@Column(name = "enabled")
	private Boolean enabled;
	
	@Column(name = "created_on")
	private Date createdOn;
	
	@Column(name = "updated_on")
	private Date updatedOn;
	
	@Column(name = "serial_no")
	private Integer serialNo;

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public String getSubstanceName() {
		return substanceName;
	}

	public void setSubstanceName(String substanceName) {
		this.substanceName = substanceName;
	}

	public String getSubstanceType() {
		return substanceType;
	}

	public void setSubstanceType(String substanceType) {
		this.substanceType = substanceType;
	}

	public String getSubstanceNPName() {
		return substanceNPName;
	}

	public void setSubstanceNPName(String substanceNPName) {
		this.substanceNPName = substanceNPName;
	}

	public String getSubstanceOtherName() {
		return substanceOtherName;
	}

	public void setSubstanceOtherName(String substanceOtherName) {
		this.substanceOtherName = substanceOtherName;
	}

	public String getSubstanceChemicalName() {
		return substanceChemicalName;
	}

	public void setSubstanceChemicalName(String substanceChemicalName) {
		this.substanceChemicalName = substanceChemicalName;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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
