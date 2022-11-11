package com.dor.cbn.dto;

import java.util.List;

import com.dor.cbn.model.PurposeEntity;

public class AdminPurposeResponseDTO {
	
	private List<PurposeEntity> purposeEntities;
	private Boolean status;
	private String msg;
	
	public List<PurposeEntity> getPurposeEntities() {
		return purposeEntities;
	}
	public void setPurposeEntities(List<PurposeEntity> purposeEntities) {
		this.purposeEntities = purposeEntities;
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
