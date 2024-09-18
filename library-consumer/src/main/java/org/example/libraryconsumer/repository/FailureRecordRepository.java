package org.example.libraryconsumer.repository;

import org.example.libraryconsumer.entity.FailureRecord;
import org.example.libraryconsumer.entity.FailureRecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FailureRecordRepository extends JpaRepository<FailureRecord, Integer> {

    List<FailureRecord> findAllByStatus(FailureRecordStatus status);
}