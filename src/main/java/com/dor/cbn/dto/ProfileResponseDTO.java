package com.dor.cbn.dto;

import java.util.List;
import com.dor.cbn.model.SubstanceEntity;

public class ProfileResponseDTO {
	
	private List<SubstanceEntity> psychotropicSubstancesEntities;
	private List<SubstanceEntity> narcoticsSubstancesEntities;
	private List<SubstanceEntity> controlledSubstancesEntities;
	private Boolean status;
	private String msg;
	private String plantId;
	
	public String getPlantId() {
		return plantId;
	}
	public void setPlantId(String plantId) {
		this.plantId = plantId;
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
	public List<SubstanceEntity> getPsychotropicSubstancesEntities() {
		return psychotropicSubstancesEntities;
	}
	public void setPsychotropicSubstancesEntities(List<SubstanceEntity> psychotropicSubstancesEntities) {
		this.psychotropicSubstancesEntities = psychotropicSubstancesEntities;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
