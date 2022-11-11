package com.dor.cbn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dor.cbn.model.ConfigEntity;

public interface ConfigRepository extends JpaRepository<ConfigEntity, Integer>{
	
	public ConfigEntity findByUserType(String userType);
}

