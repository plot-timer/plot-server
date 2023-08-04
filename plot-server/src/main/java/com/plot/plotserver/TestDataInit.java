//package com.plot.plotserver;
//
//import com.plot.plotserver.domain.EmailTmp;
//import com.plot.plotserver.repository.EmailTmpRepository;
//import com.plot.plotserver.service.SchedulerService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.IOException;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class TestDataInit {
//
//    private final SchedulerService schedulerService;
//
//    private final EmailTmpRepository emailTmpRepository;
//    /**
//     * 확인용 초기 데이터 추가
//     */
//    @EventListener(ApplicationReadyEvent.class)
//    public void initData() {
//        log.info("test data init");
//
//        EmailTmp emailTmp1=new EmailTmp();
//        emailTmp1.setEmail_address("gntjd135@naver.com");
//        emailTmp1.setCode("123456");
//
//        EmailTmp emailTmp2=new EmailTmp();
//        emailTmp2.setEmail_address("hus135@naver.com");
//        emailTmp2.setCode("654321");
//
//        try {
//            emailTmpRepository.save(emailTmp1);
//            emailTmpRepository.save(emailTmp2);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}
