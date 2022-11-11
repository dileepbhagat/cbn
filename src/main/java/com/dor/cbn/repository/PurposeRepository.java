package com.dor.cbn.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dor.cbn.model.PurposeEntity;

public interface PurposeRepository extends JpaRepository<PurposeEntity, Integer>{
	
	public List<PurposeEntity> findByValid(Boolean valid);
}

