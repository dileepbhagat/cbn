package com.dor.cbn.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dor.cbn.model.PlantEntity;

public interface PlantRepository extends JpaRepository<PlantEntity, Long>{
	
	public List<PlantEntity> findByUserId(String userId);
	public PlantEntity findByUserIdAndPlantId(String userId, String plantId);
}
