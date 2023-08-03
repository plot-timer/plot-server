package com.plot.plotserver.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;

@Configuration
public class mailConfig {

    @Bean
    public MimeMessageHelper mimeMessageHelper(JavaMailSender mailSender) throws MessagingException {
        return new MimeMessageHelper(mailSender.createMimeMessage(), true, "UTF-8");
    }
}
