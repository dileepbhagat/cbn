package com.dor.cbn.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_role_token", schema="cbn")
public class UserRoleTokenEntity {
	
	@Id
	@Column(name="user_role")
	private String userRole;
	
	@Column(name="token_validity")
	private Integer tokenValidity;

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public Integer getTokenValidity() {
		return tokenValidity;
	}

	public void setTokenValidity(Integer tokenValidity) {
		this.tokenValidity = tokenValidity;
	}
	
	

}
