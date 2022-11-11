package com.dor.cbn.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="login_log", schema="cbn")
public class LoginLogEntity {
	
	@Id
	@Column(name="serial_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer serialNo;
	
	@Column(name="login_id")
	private String loginId;
	
	@Column(name = "loggged_in_timestamp")
	private LocalDateTime loggedInTimestamp;
	
	@Column(name = "logged_out_timestamp")
	private LocalDateTime loggedOutTimestamp;
	
	@Column(name = "ip_address")
	private String ipAddress;
	
	@Column(name = "is_set")
	private Boolean isSet;
	
	@Column(name = "logged_in")
	private Boolean loggedIn;

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public String getLoginId() {
		return loginId;
	}

	public Boolean getLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(Boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public LocalDateTime getLoggedInTimestamp() {
		return loggedInTimestamp;
	}

	public void setLoggedInTimestamp(LocalDateTime loggedInTimestamp) {
		this.loggedInTimestamp = loggedInTimestamp;
	}

	public LocalDateTime getLoggedOutTimestamp() {
		return loggedOutTimestamp;
	}

	public void setLoggedOutTimestamp(LocalDateTime loggedOutTimestamp) {
		this.loggedOutTimestamp = loggedOutTimestamp;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Boolean getIsSet() {
		return isSet;
	}

	public void setIsSet(Boolean isSet) {
		this.isSet = isSet;
	}

}
