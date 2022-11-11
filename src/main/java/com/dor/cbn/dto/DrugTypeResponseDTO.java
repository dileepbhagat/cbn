package com.dor.cbn.dto;
import java.util.List;

import com.dor.cbn.model.DrugTypeEntity;

public class DrugTypeResponseDTO {
	
	private List<DrugTypeEntity> drugTypeEntities;
	private Boolean status;
	private String msg;
	
	public List<DrugTypeEntity> getDrugTypeEntities() {
		return drugTypeEntities;
	}
	public void setDrugTypeEntities(List<DrugTypeEntity> drugTypeEntities) {
		this.drugTypeEntities = drugTypeEntities;
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
