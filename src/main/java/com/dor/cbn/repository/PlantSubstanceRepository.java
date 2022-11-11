package com.dor.cbn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dor.cbn.model.PlantSubstanceEntity;

public interface PlantSubstanceRepository extends JpaRepository<PlantSubstanceEntity, Long>{
	
	//public PlantSubstanceEntity findByPlantId(String plantId);
	public List<PlantSubstanceEntity> findByPlantId(String plantId);
}
