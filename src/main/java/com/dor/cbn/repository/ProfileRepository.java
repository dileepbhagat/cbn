package com.dor.cbn.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dor.cbn.model.ProfileEntity;


public interface ProfileRepository extends JpaRepository<ProfileEntity, String>{
	
	public ProfileEntity findByUserId(String userId);
}

