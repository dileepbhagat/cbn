package com.dor.cbn.dto;

public class CommonResponseDTO {
	
	private String msg;
	private Boolean status;
	private String key;
	private String firmName;
	private String token;
	private Long tokenExpTimestamp;
	private Integer validity;
	
	public Integer getValidity() {
		return validity;
	}
	public void setValidity(Integer validity) {
		this.validity = validity;
	}
	public Long getTokenExpTimestamp() {
		return tokenExpTimestamp;
	}
	public void setTokenExpTimestamp(Long tokenExpTimestamp) {
		this.tokenExpTimestamp = tokenExpTimestamp;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getFirmName() {
		return firmName;
	}
	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

}
