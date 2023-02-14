package com.dor.cbn.model;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="admin", schema="cbn")

public class AdminEntity {
	
	@Id
	@Column(name="user_id")
	private String userId;
	
	@Column(name="login_id")
	private String loginId;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "email_verified")
	private Boolean emailVerified;
	
	@Column(name = "mobile_verified")
	private Boolean mobileVerified;
	
	@Column(name = "otp")
	private String otp;
	
	@Column(name = "otp_request_no")
	private String otpRequestNo;
	
	@Column(name = "otp_timestamp")
	private Timestamp otpTimestamp;
	
	@Column(name = "email_verified_on")
	private Date emailVerifiedOn;
	
	@Column(name = "created_on")
	private Date createdOn;
	
	@Column(name = "updated_on")
	private Date updatedOn;
	
	@Column(name = "enabled")
	private Boolean enabled;
	
	@Column(name = "mobile_no")
	private String mobileNo;
	
	@Column(name = "password_code")
	private String passwordCode;
	
	@Column(name = "verification_code")
	private String verificationCode;
	
	@Column(name = "last_password_change_time")
	private Date lastPasswordChangeTime;
	
	@Column(name = "last_three_passwords")
	private ArrayList<String> lastThreePasswords;
	
	@Column(name = "first_time_password_set")
	private Boolean firstTimePasswordSet;
	
	public Boolean getFirstTimePasswordSet() {
		return firstTimePasswordSet;
	}

	public void setFirstTimePasswordSet(Boolean firstTimePasswordSet) {
		this.firstTimePasswordSet = firstTimePasswordSet;
	}

	public Date getLastPasswordChangeTime() {
		return lastPasswordChangeTime;
	}

	public void setLastPasswordChangeTime(Date lastPasswordChangeTime) {
		this.lastPasswordChangeTime = lastPasswordChangeTime;
	}

	public ArrayList<String> getLastThreePasswords() {
		return lastThreePasswords;
	}

	public void setLastThreePasswords(ArrayList<String> lastThreePasswords) {
		this.lastThreePasswords = lastThreePasswords;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getPasswordCode() {
		return passwordCode;
	}

	public void setPasswordCode(String passwordCode) {
		this.passwordCode = passwordCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getOtpRequestNo() {
		return otpRequestNo;
	}

	public void setOtpRequestNo(String otpRequestNo) {
		this.otpRequestNo = otpRequestNo;
	}

	public Timestamp getOtpTimestamp() {
		return otpTimestamp;
	}

	public void setOtpTimestamp(Timestamp otpTimestamp) {
		this.otpTimestamp = otpTimestamp;
	}

	public Boolean getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(Boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public Boolean getMobileVerified() {
		return mobileVerified;
	}

	public void setMobileVerified(Boolean mobileVerified) {
		this.mobileVerified = mobileVerified;
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

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Date getEmailVerifiedOn() {
		return emailVerifiedOn;
	}

	public void setEmailVerifiedOn(Date emailVerifiedOn) {
		this.emailVerifiedOn = emailVerifiedOn;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	

}
