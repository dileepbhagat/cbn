package com.dor.cbn.dto;

public class OTPValidationRequestDTO {
	
	private String otp;
	private String mobNo;
	private String emailId;
	private String adminUser;
	private String adminOtp;
	public String getAdminUser() {
		return adminUser;
	}
	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}
	public String getAdminOtp() {
		return adminOtp;
	}
	public void setAdminOtp(String adminOtp) {
		this.adminOtp = adminOtp;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
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
