package com.dor.cbn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dor.cbn.model.DocumentStorageEntity;

public interface DocumentStorageRepository extends JpaRepository<DocumentStorageEntity, Long>{
	
}

