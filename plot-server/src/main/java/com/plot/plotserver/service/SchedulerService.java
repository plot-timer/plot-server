package com.plot.plotserver.service;

import com.plot.plotserver.repository.EmailTmpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final EmailTmpRepository emailTmpRepository;

    @Transactional
    @Scheduled(cron = "0 0/2 * * * *")//매 5분 마다 실행
    public void authDelete(){
        emailTmpRepository.deleteByCreatedAtGreaterThanEqual(LocalDateTime.now().minusMinutes(2));
    }
}
