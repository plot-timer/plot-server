package com.plot.plotserver.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.plot.plotserver.domain.EmailTmp;
import com.plot.plotserver.domain.Message;
import com.plot.plotserver.domain.RefreshToken;
import com.plot.plotserver.dto.request.email.EmailCodeReqDto;
import com.plot.plotserver.dto.request.email.EmailRequestDto;
import com.plot.plotserver.exception.email.EmailCodeExpiredException;
import com.plot.plotserver.exception.email.EmailCodeMismatchException;
import com.plot.plotserver.exception.email.EmailCodeSendingFailureException;
import com.plot.plotserver.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;


    @PostMapping("login/mailConfirm")//인증 코드 전송
    @Transactional
    public void mailConfirm(HttpServletResponse response,@RequestBody EmailRequestDto emailDto) throws MessagingException, IOException {


        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        try {
            String authCode = emailService.sendEmail(emailDto.getEmail());//인증번호 전송과, emailTmp 저장을 transaction으로 묶음.

            EmailTmp emailTmp = EmailTmp.builder()
                    .userEmail(emailDto.getEmail())
                    .code(authCode)
                    .createdAt(LocalDateTime.now())
                    .build();
            emailService.save(emailTmp);
        } catch (Exception e) {
            throw new EmailCodeSendingFailureException("인증번호 전송에 실패하였습니다.");
        }

        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }

    @PostMapping("login/authenticate")//사용자 인즌코드 입력으로, 유효한 인증번호인지 authenticate함.
    public void mailCodeAuthenticate(HttpServletResponse response,@RequestBody EmailCodeReqDto emailCodeReqDto) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        Optional<EmailTmp> findUserEmail = emailService.findByUserEmail(emailCodeReqDto.getEmail());

        if (!findUserEmail.isPresent()) {
            throw new EmailCodeExpiredException("인증번호가 만료되었습니다.! 다시 전송해주세요.");
        } else if (findUserEmail.isPresent()) {
                log.info("code={}",findUserEmail.get().getCode());
            if (!emailCodeReqDto.getCode().equals(findUserEmail.get().getCode())) {//인증번호 틀림
                throw new EmailCodeMismatchException("이메일 코드 틀림. 다시 입력해주세요");
            }
            else{
                Message message = Message.builder()//정상 응답
                        .status(HttpStatus.OK)
                        .message("success")
                        .build();
                om.writeValue(response.getOutputStream(), message);
            }

        }

    }



}
