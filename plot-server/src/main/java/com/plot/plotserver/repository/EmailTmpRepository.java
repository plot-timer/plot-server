package com.plot.plotserver.repository;

import com.plot.plotserver.domain.EmailTmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;


@Repository
public interface EmailTmpRepository extends JpaRepository<EmailTmp, Long> {

    public void deleteByCreatedAtGreaterThanEqual(LocalDateTime time);

    public Optional<EmailTmp> findByUserEmail(String userEmail);



}
