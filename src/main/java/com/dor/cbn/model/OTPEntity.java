package com.dor.cbn.model;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="otp", schema="cbn")

public class OTPEntity {
	
	@Id
	@Column
	private String mobNo;
	
	@Column(name = "email_id")
	private String emailId;
	
	@Column(name = "otp")
	private String otp;
	
	@Column(name = "request_id")
	private String requestId;
	
	@Column(name = "time_of_generation")
	private LocalDateTime timeOfGeneration;
	
	@Column(name = "enabled")
	private Boolean enabled;

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

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public LocalDateTime getTimeOfGeneration() {
		return timeOfGeneration;
	}

	public void setTimeOfGeneration(LocalDateTime timeOfGeneration) {
		this.timeOfGeneration = timeOfGeneration;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	
}
