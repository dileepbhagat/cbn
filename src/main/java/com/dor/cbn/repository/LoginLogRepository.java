package com.dor.cbn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dor.cbn.model.LoginLogEntity;

public interface LoginLogRepository extends JpaRepository<LoginLogEntity, Integer>{
	
	public LoginLogEntity findByLoginIdAndIsSet(String loginId, Boolean isSet);
	public List<LoginLogEntity> findByLoginId(String loginId);
	
}

