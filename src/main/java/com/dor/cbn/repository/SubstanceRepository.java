package com.dor.cbn.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dor.cbn.model.SubstanceEntity;

public interface SubstanceRepository extends JpaRepository<SubstanceEntity, String>{
	
	public List<SubstanceEntity> findByEnabledAndSubstanceType(Boolean enabled, String substanceType);
}
