package com.dor.cbn.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dor.cbn.model.LoginEntity;

public interface LoginRepository extends JpaRepository<LoginEntity, String>{
	
	public LoginEntity findByUserId(String userId);
	public void deleteByUserId(String userId);
	public LoginEntity findByLoginId(String loginId);
}