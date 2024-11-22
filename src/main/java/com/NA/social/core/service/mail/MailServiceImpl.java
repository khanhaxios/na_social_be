package com.NA.social.core.service.mail;

import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;
    ExecutorService executorService = Executors.newCachedThreadPool();

    @Value("${spring.mail.username}")
    public String sender;

    @Override
    public void sendMailAsync(String to, String body, String subject) {
        SimpleMailMessage simpleMailMessage = createMail(to, body, subject);
        executorService.execute(() -> javaMailSender.send(simpleMailMessage));
    }

    @Override
    public SimpleMailMessage createMail(String to, String body, String subject) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        return simpleMailMessage;
    }
}
