package com.dan.backend.service.impl;

import com.dan.backend.service.EmailService;
import lombok.Builder;
import lombok.Data;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Data
@Builder
public class EmailServiceImpl implements EmailService {
    private static final String SENDER_EMAIL = "waaboom123@outlook.com";
    private final JavaMailSender javaMailSender;




    @Override
    public void send(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(SENDER_EMAIL);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            javaMailSender.send(message);

        } catch (MailException mailException) {
            mailException.printStackTrace();
        }
    }

}
