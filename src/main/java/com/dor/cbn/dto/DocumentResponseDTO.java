package com.dor.cbn.dto;

import java.util.List;

import com.dor.cbn.model.DocumentEntity;

public class DocumentResponseDTO {
	
	private List<DocumentEntity> documentEntities;
	private Boolean status;
	private String msg;
	public List<DocumentEntity> getDocumentEntities() {
		return documentEntities;
	}
	public void setDocumentEntities(List<DocumentEntity> documentEntities) {
		this.documentEntities = documentEntities;
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
