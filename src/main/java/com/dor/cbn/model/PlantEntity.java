package com.dor.cbn.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="plant_list", schema="cbn")

public class PlantEntity {
	
	@Id
	@Column
	private Integer serialNo;
	
	@Column(name = "plant_id")
	private String plantId;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "license_no")
	private String licenseNo;
	
	@Column(name = "license_validity_from")
	private Date licenseValidityFrom;
	
	@Column(name = "license_validity_to")
	private Date licenseValidityTo;
	
	@Column(name = "created_on")
	private Date createdOn;
	
	@Column(name = "updated_on")
	private Date updatedOn;
	
	@Column(name = "valid")
	private Boolean valid;
	
	@Column(name = "plant_name")
	private String plantName;
	
	@Column(name = "psycho_sub_presented")
	private Boolean psychoSubPresented;
	
	@Column(name = "narco_sub_presented")
	private Boolean narcoSubPresented;
	
	@Column(name = "is_contr_sub_presented")
	private Boolean contrSubPresented;
	
	@Column(name = "plant_address")
	private String plantAddress;
	
	@Column(name = "plant_pincode")
	private String plantPincode;

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public String getPlantAddress() {
		return plantAddress;
	}

	public void setPlantAddress(String plantAddress) {
		this.plantAddress = plantAddress;
	}

	public String getPlantPincode() {
		return plantPincode;
	}

	public void setPlantPincode(String plantPincode) {
		this.plantPincode = plantPincode;
	}

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public String getPlantId() {
		return plantId;
	}

	public void setPlantId(String plantId) {
		this.plantId = plantId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public Date getLicenseValidityFrom() {
		return licenseValidityFrom;
	}

	public void setLicenseValidityFrom(Date licenseValidityFrom) {
		this.licenseValidityFrom = licenseValidityFrom;
	}

	public Date getLicenseValidityTo() {
		return licenseValidityTo;
	}

	public void setLicenseValidityTo(Date licenseValidityTo) {
		this.licenseValidityTo = licenseValidityTo;
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

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public Boolean getPsychoSubPresented() {
		return psychoSubPresented;
	}

	public void setPsychoSubPresented(Boolean psychoSubPresented) {
		this.psychoSubPresented = psychoSubPresented;
	}

	public Boolean getNarcoSubPresented() {
		return narcoSubPresented;
	}

	public void setNarcoSubPresented(Boolean narcoSubPresented) {
		this.narcoSubPresented = narcoSubPresented;
	}

	public Boolean getContrSubPresented() {
		return contrSubPresented;
	}

	public void setContrSubPresented(Boolean contrSubPresented) {
		this.contrSubPresented = contrSubPresented;
	}

}
