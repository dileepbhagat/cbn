package com.dor.cbn.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dor.cbn.model.AdminEntity;

public interface AdminRepository extends JpaRepository<AdminEntity, String>{
	
	public List<AdminEntity> findByEnabled(Boolean enabled);
	public AdminEntity findByLoginId(String loginId);
	public AdminEntity findByPasswordCode(String passwordCode);
	public AdminEntity findByVerificationCode(String verificationCode);
}
