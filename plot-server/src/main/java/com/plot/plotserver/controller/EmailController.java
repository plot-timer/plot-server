package com.plot.plotserver.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.plot.plotserver.domain.EmailTmp;
import com.plot.plotserver.domain.Message;
import com.plot.plotserver.dto.request.email.EmailCodeReqDto;
import com.plot.plotserver.dto.request.email.EmailRequestDto;
import com.plot.plotserver.exception.email.EmailCodeExpiredException;
import com.plot.plotserver.exception.email.EmailCodeMismatchException;
import com.plot.plotserver.exception.email.EmailCodeSendingFailureException;
import com.plot.plotserver.service.EmailSendingService;
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
    public void mailConfirm(HttpServletResponse response,@RequestBody EmailRequestDto emailDto) throws MessagingException, IOException {


        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        String authCode = emailService.sendMailCodeAndSave(emailDto);

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

       emailService.mailCodeAuthenticate(emailCodeReqDto);

       Message message = Message.builder()//정상 응답
               .status(HttpStatus.OK)
               .message("success")
               .build();
       om.writeValue(response.getOutputStream(), message);

    }

}
