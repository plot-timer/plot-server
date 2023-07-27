package com.plot.plotserver.web.email;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    private final SpringTemplateEngine templateEngine;

    private String authNum;//랜덤 인증 코드

    //렌덤 인증 코드 생성
    public void createCode(){
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 6; i++) {
            key.append(random.nextInt(9));

        }
        authNum=key.toString();
    }


    public MimeMessage createEmailForm(String email) throws MessagingException {

        createCode();
        String setFrom="gntjd135@gmail.com";
        String toEmail=email;//받는 사람
        String title="PLAN PLAYER 회원가입 인증 번호";//제목


        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);//보낼 이메일 설정.
        message.setSubject(title);
        message.setFrom(setFrom);
        message.setText(setContext(authNum),"utf-8","html");

        return message;
    }

    //실제 이메일 전송
    public String sendEmail(String toEmail) throws MessagingException {

        //메일 전송에 필요한 정보 설정.
        MimeMessage emailForm = createEmailForm(toEmail);
        //실제 이메일 전송
        emailSender.send(emailForm);

        return authNum;//인증 코드 반환.
    }


    //타임 리프를 이용한 context 설정
    public String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("mail", context);//mail.html
    }


}