package com.dor.cbn.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "sequence_table")
public class SequenceEntity {
    @Id
    @SequenceGenerator(name = "id_seq", sequenceName = "sequence_table_serial_no_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq")
    @Column(name = "serial_no", updatable = false)
    protected Long id;
}

