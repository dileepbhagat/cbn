package com.dor.cbn.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dor.cbn.model.OTPEntity;

public interface OTPRepository extends JpaRepository<OTPEntity, String>{
	
}

