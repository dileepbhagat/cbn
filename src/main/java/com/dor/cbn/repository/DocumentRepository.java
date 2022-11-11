package com.dor.cbn.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dor.cbn.model.DocumentEntity;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer>{
	
	public List<DocumentEntity> findByValid(Boolean valid);
}
