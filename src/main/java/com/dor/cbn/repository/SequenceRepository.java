package com.dor.cbn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dor.cbn.model.SequenceEntity;

public interface SequenceRepository extends JpaRepository<SequenceEntity, Long> {

	@Query(value = "SELECT nextval('sequence_table_serial_no_seq')", nativeQuery =true)
    Long getNextSeriesId();
}

