package com.dor.cbn.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dor.cbn.model.RegistrationEntity;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, String>{
	
	public RegistrationEntity findByVerificationCode(String verificationCode);
	public RegistrationEntity findByUserId(String userId);
	public RegistrationEntity findByPanNo(String panNo);
	public RegistrationEntity findByPasswordCode(String passwordCode);
	
}

