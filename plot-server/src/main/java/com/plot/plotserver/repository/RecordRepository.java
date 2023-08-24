package com.plot.plotserver.repository;

import com.plot.plotserver.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



public interface RecordRepository extends JpaRepository<Record, Long> {



}
