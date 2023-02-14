package com.dor.cbn.model;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users", schema="public")
public class UsersEntity {
	
	@Id
	@Column
	private Integer userid;
	
	@Column(name = "loginid")
	private String loginid;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "date_last_login")
	private LocalDateTime dateLastLogin;
	
	@Column(name = "date_last_password_change")
	private LocalDateTime dateLastPasswordChange;
	
	@Column(name = "date_firstfailloginattempt")
	private LocalDateTime dateFirstfailloginattempt;
	
	@Column(name = "date_secondfailloginattempt")
	private LocalDateTime dateSecondfailloginattempt;
	
	@Column(name = "failed_login_attempts")
	private Integer failedLoginAttempts;
	
	@Column(name = "role")
	private Integer role;
	
	@Column(name = "oldpassword1")
	private String oldpassword1;
	
	@Column(name = "oldpassword2")
	private String oldpassword2;
	
	@Column(name = "oldpassword3")
	private String oldpassword3;
	
	@Column(name = "password_change_status")
	private String passwordChangeStatus;
	
	@Column(name = "locked")
	private Boolean locked;
	
	@Column(name = "creation_date")
	private LocalDateTime creationDate;
	
	@Column(name = "otpattempt")
	private Integer otpattempt;
	
	@Column(name = "agencyid")
	private Integer agencyid;
	
	@Column(name = "dscflag")
	private String dscflag;
	
	@Column(name = "user_creation_id")
	private Integer userCreationId;
	
	@Column(name = "user_type_id")
	private String userTypeId;

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getLoginid() {
		return loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDateTime getDateLastLogin() {
		return dateLastLogin;
	}

	public void setDateLastLogin(LocalDateTime dateLastLogin) {
		this.dateLastLogin = dateLastLogin;
	}

	public LocalDateTime getDateLastPasswordChange() {
		return dateLastPasswordChange;
	}

	public void setDateLastPasswordChange(LocalDateTime dateLastPasswordChange) {
		this.dateLastPasswordChange = dateLastPasswordChange;
	}

	public LocalDateTime getDateFirstfailloginattempt() {
		return dateFirstfailloginattempt;
	}

	public void setDateFirstfailloginattempt(LocalDateTime dateFirstfailloginattempt) {
		this.dateFirstfailloginattempt = dateFirstfailloginattempt;
	}

	public LocalDateTime getDateSecondfailloginattempt() {
		return dateSecondfailloginattempt;
	}

	public void setDateSecondfailloginattempt(LocalDateTime dateSecondfailloginattempt) {
		this.dateSecondfailloginattempt = dateSecondfailloginattempt;
	}

	public Integer getFailedLoginAttempts() {
		return failedLoginAttempts;
	}

	public void setFailedLoginAttempts(Integer failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getOldpassword1() {
		return oldpassword1;
	}

	public void setOldpassword1(String oldpassword1) {
		this.oldpassword1 = oldpassword1;
	}

	public String getOldpassword2() {
		return oldpassword2;
	}

	public void setOldpassword2(String oldpassword2) {
		this.oldpassword2 = oldpassword2;
	}

	public String getOldpassword3() {
		return oldpassword3;
	}

	public void setOldpassword3(String oldpassword3) {
		this.oldpassword3 = oldpassword3;
	}

	public String getPasswordChangeStatus() {
		return passwordChangeStatus;
	}

	public void setPasswordChangeStatus(String passwordChangeStatus) {
		this.passwordChangeStatus = passwordChangeStatus;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getOtpattempt() {
		return otpattempt;
	}

	public void setOtpattempt(Integer otpattempt) {
		this.otpattempt = otpattempt;
	}

	public Integer getAgencyid() {
		return agencyid;
	}

	public void setAgencyid(Integer agencyid) {
		this.agencyid = agencyid;
	}

	public String getDscflag() {
		return dscflag;
	}

	public void setDscflag(String dscflag) {
		this.dscflag = dscflag;
	}

	public Integer getUserCreationId() {
		return userCreationId;
	}

	public void setUserCreationId(Integer userCreationId) {
		this.userCreationId = userCreationId;
	}

	public String getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(String userTypeId) {
		this.userTypeId = userTypeId;
	}
	
}

