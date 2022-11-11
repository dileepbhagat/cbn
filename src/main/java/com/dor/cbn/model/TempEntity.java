package com.dor.cbn.model;

import java.time.LocalDateTime;

public class TempEntity {
	private String OTP;
	private String requestId;
	private String mobNo;
	private String emailId;
	private LocalDateTime generationTimestamp;
	public LocalDateTime getGenerationTimestamp() {
		return generationTimestamp;
	}
	public void setGenerationTimestamp(LocalDateTime generationTimestamp) {
		this.generationTimestamp = generationTimestamp; 
	}
	public String getOTP() {
		return OTP;
	}
	public void setOTP(String oTP) {
		OTP = oTP;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getMobNo() {
		return mobNo;
	}
	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
}
