package com.dor.cbn.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dor.cbn.model.DrugTypeEntity;

public interface DrugTypeRepository extends JpaRepository<DrugTypeEntity, Integer>{
	
	public List<DrugTypeEntity> findByValid(Boolean valid);
}

