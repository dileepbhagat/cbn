package com.dor.cbn.dto;

import java.util.Date;
import java.util.List;

import com.dor.cbn.model.SubstanceEntity;

public class ProfilePlantRequestDTO {
	private String loginId;
	private String plantName;
	private String plantAddress;
	private String drugLicenseNo;
	private Date validityFrom;
	private Date validityTo;
	private Boolean isPsychoSubPresent;
	private Boolean isNarcoSubPresent;
	private Boolean isContrSubPresent;
	private List<SubstanceEntity> psychotropicSubstancesEntities;
	private List<SubstanceEntity> narcoticsSubstancesEntities;
	private List<SubstanceEntity> controlledSubstancesEntities;
	private String plantId;
	public String getPlantId() {
		return plantId;
	}
	public void setPlantId(String plantId) {
		this.plantId = plantId;
	}
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
	public String getDrugLicenseNo() {
		return drugLicenseNo;
	}
	public void setDrugLicenseNo(String drugLicenseNo) {
		this.drugLicenseNo = drugLicenseNo;
	}
	public Date getValidityFrom() {
		return validityFrom;
	}
	public void setValidityFrom(Date validityFrom) {
		this.validityFrom = validityFrom;
	}
	public Date getValidityTo() {
		return validityTo;
	}
	public void setValidityTo(Date validityTo) {
		this.validityTo = validityTo;
	}
	public Boolean getIsPsychoSubPresent() {
		return isPsychoSubPresent;
	}
	public void setIsPsychoSubPresent(Boolean isPsychoSubPresent) {
		this.isPsychoSubPresent = isPsychoSubPresent;
	}
	public Boolean getIsNarcoSubPresent() {
		return isNarcoSubPresent;
	}
	public void setIsNarcoSubPresent(Boolean isNarcoSubPresent) {
		this.isNarcoSubPresent = isNarcoSubPresent;
	}
	public Boolean getIsContrSubPresent() {
		return isContrSubPresent;
	}
	public void setIsContrSubPresent(Boolean isContrSubPresent) {
		this.isContrSubPresent = isContrSubPresent;
	}
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public List<SubstanceEntity> getPsychotropicSubstancesEntities() {
		return psychotropicSubstancesEntities;
	}
	public void setPsychotropicSubstancesEntities(List<SubstanceEntity> psychotropicSubstancesEntities) {
		this.psychotropicSubstancesEntities = psychotropicSubstancesEntities;
	}
	public List<SubstanceEntity> getNarcoticsSubstancesEntities() {
		return narcoticsSubstancesEntities;
	}
	public void setNarcoticsSubstancesEntities(List<SubstanceEntity> narcoticsSubstancesEntities) {
		this.narcoticsSubstancesEntities = narcoticsSubstancesEntities;
	}
	public List<SubstanceEntity> getControlledSubstancesEntities() {
		return controlledSubstancesEntities;
	}
	public void setControlledSubstancesEntities(List<SubstanceEntity> controlledSubstancesEntities) {
		this.controlledSubstancesEntities = controlledSubstancesEntities;
	}

}
