package com.plot.plotserver.service;


import com.plot.plotserver.domain.EmailTmp;
import com.plot.plotserver.repository.EmailTmpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    private final SpringTemplateEngine templateEngine;

    private final MimeMessageHelper mimeMessageHelper;

    private final EmailTmpRepository emailTmpRepository;

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

    @Transactional
    public void save(EmailTmp emailTmp) {
        emailTmpRepository.save(emailTmp);
    }


    public MimeMessage createEmailForm(String email) throws MessagingException {
        createCode();
        String setFrom = "PLOT";
        String toEmail = email; //받는 사람
        String title = "PLOT 회원가입 인증 번호 " + "[" + authNum + "]";

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");

        mimeMessageHelper.setTo(toEmail); // 수신자 설정
        mimeMessageHelper.setSubject(title);
        mimeMessageHelper.setFrom(setFrom);
        mimeMessageHelper.setText(setContext(authNum), true); // true: HTML 포맷 사용

        String imageResourceName = "plot_logo.png"; // 이미지 파일명
        ClassPathResource imageResource = new ClassPathResource("templates/" + imageResourceName);
        mimeMessageHelper.addInline(imageResourceName, imageResource);

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
        return templateEngine.process("mail2", context);//mail.html
    }


}