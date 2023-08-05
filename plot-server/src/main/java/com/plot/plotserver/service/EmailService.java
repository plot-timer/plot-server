package com.plot.plotserver.service;

import com.plot.plotserver.domain.EmailTmp;
import com.plot.plotserver.domain.Message;
import com.plot.plotserver.dto.request.email.EmailCodeReqDto;
import com.plot.plotserver.dto.request.email.EmailRequestDto;
import com.plot.plotserver.exception.email.EmailCodeExpiredException;
import com.plot.plotserver.exception.email.EmailCodeMismatchException;
import com.plot.plotserver.exception.email.EmailCodeSendingFailureException;
import com.plot.plotserver.repository.EmailTmpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final EmailTmpRepository emailTmpRepository;

    private final EmailSendingService emailSendingService;


    @Transactional
    public void save(EmailTmp emailTmp) {
        emailTmpRepository.save(emailTmp);
    }

    @Transactional(rollbackFor = EmailCodeSendingFailureException.class)
    public String sendMailCodeAndSave(EmailRequestDto emailRequestDto) throws MessagingException {

        String authCode = emailSendingService.sendEmail(emailRequestDto.getEmail());//인증번호 전송과, emailTmp 저장을 transaction으로 묶음.

        //db에 사용자가 이미 먼저 보낸 인증 코드가 있으면, 그 레코드 삭제하고, 새로 생성.


        EmailTmp emailTmp = EmailTmp.builder()
                .userEmail(emailRequestDto.getEmail())
                .code(authCode)
                .createdAt(LocalDateTime.now())
                .build();
        save(emailTmp);
        return authCode;
    }


    public void mailCodeAuthenticate(EmailCodeReqDto emailCodeReqDto) {

        Optional<EmailTmp> findUserEmail = emailTmpRepository.findByUserEmail(emailCodeReqDto.getEmail());

        if (!findUserEmail.isPresent()) {
            throw new EmailCodeExpiredException("인증번호가 만료되었습니다.! 다시 전송해주세요.");
        } else if (findUserEmail.isPresent()) {
            log.info("code={}",findUserEmail.get().getCode());
            if (!emailCodeReqDto.getCode().equals(findUserEmail.get().getCode())) {//인증번호 틀림
                throw new EmailCodeMismatchException("이메일 코드 틀림. 다시 입력해주세요");
            }

        }


    }

    @Transactional
    public void handlerNewCode(EmailRequestDto emailRequestDto) throws MessagingException {

        Optional<EmailTmp> findByUserEmail = emailTmpRepository.findByUserEmail(emailRequestDto.getEmail());

        if (findByUserEmail.isPresent()) {
            emailTmpRepository.delete(findByUserEmail.get());
        }

    }

}
