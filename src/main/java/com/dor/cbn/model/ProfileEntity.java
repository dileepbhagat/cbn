package com.dor.cbn.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="profile", schema="cbn")

public class ProfileEntity {
	
	@Id
	@Column(name="serial_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer serialNo;
	
	@Column(name="profile_id")
	private String profileId;
	
	@Column(name = "entity_type")
	private String entityType;
	
	@Column(name = "entity_name")
	private String entityName;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "pincode")
	private String pincode;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "designated_person")
	private String designatedPerson;
	
	@Column(name = "designated_person_name")
	private String designatedPersonName;
	
	@Column(name = "designated_person_tele_no")
	private String designatedPersonTeleNo;
	
	@Column(name = "designated_person_fax")
	private String designatedPersonFax;
	
	@Column(name = "sales_tax_no")
	private String salesTaxNo;
	
	@Column(name = "central_excise_no")
	private String centralExciseNo;
	
	@Column(name = "pan_no")
	private String panNo;
	
	@Column(name = "commissionerate_name")
	private String commissionerateName;
	
	@Column(name = "commissionerate_city")
	private String commissionerateCity;
	
	@Column(name = "amount_of_excise_duties")
	private String amountOfExciseDuties;
	
	@Column(name = "gst_no")
	private String gstNo;
	
	@Column(name = "iec_code")
	private String iecCode;
	
	@Column(name = "ncb_urn_no")
	private String ncbUrnNo;
	
	@Column(name = "enabled")
	private Boolean enabled;
	
	@Column(name = "created_on")
	private Date createdOn;
	
	@Column(name = "updated_on")
	private Date updatedOn;
	
	@Column(name = "user_id")
	private String userId;

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDesignatedPerson() {
		return designatedPerson;
	}

	public void setDesignatedPerson(String designatedPerson) {
		this.designatedPerson = designatedPerson;
	}

	public String getDesignatedPersonName() {
		return designatedPersonName;
	}

	public void setDesignatedPersonName(String designatedPersonName) {
		this.designatedPersonName = designatedPersonName;
	}

	public String getDesignatedPersonTeleNo() {
		return designatedPersonTeleNo;
	}

	public void setDesignatedPersonTeleNo(String designatedPersonTeleNo) {
		this.designatedPersonTeleNo = designatedPersonTeleNo;
	}

	public String getDesignatedPersonFax() {
		return designatedPersonFax;
	}

	public void setDesignatedPersonFax(String designatedPersonFax) {
		this.designatedPersonFax = designatedPersonFax;
	}

	public String getSalesTaxNo() {
		return salesTaxNo;
	}

	public void setSalesTaxNo(String salesTaxNo) {
		this.salesTaxNo = salesTaxNo;
	}

	public String getCentralExciseNo() {
		return centralExciseNo;
	}

	public void setCentralExciseNo(String centralExciseNo) {
		this.centralExciseNo = centralExciseNo;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getCommissionerateName() {
		return commissionerateName;
	}

	public void setCommissionerateName(String commissionerateName) {
		this.commissionerateName = commissionerateName;
	}

	public String getCommissionerateCity() {
		return commissionerateCity;
	}

	public void setCommissionerateCity(String commissionerateCity) {
		this.commissionerateCity = commissionerateCity;
	}

	public String getAmountOfExciseDuties() {
		return amountOfExciseDuties;
	}

	public void setAmountOfExciseDuties(String amountOfExciseDuties) {
		this.amountOfExciseDuties = amountOfExciseDuties;
	}

	public String getGstNo() {
		return gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public String getIecCode() {
		return iecCode;
	}

	public void setIecCode(String iecCode) {
		this.iecCode = iecCode;
	}

	public String getNcbUrnNo() {
		return ncbUrnNo;
	}

	public void setNcbUrnNo(String ncbUrnNo) {
		this.ncbUrnNo = ncbUrnNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
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
