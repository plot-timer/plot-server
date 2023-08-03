package com.plot.plotserver.controller;


import com.plot.plotserver.domain.email.EmailAuthRequestDto;
import com.plot.plotserver.dto.request.email.EmailRequestDto;
import com.plot.plotserver.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("login/mailConfirm")
    public String mailConfirm(@RequestBody EmailRequestDto emailDto) throws MessagingException {

        String authCode = emailService.sendEmail(emailDto.getEmail());
        return authCode;
    }


}
