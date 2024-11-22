package com.NA.social.core.service.mail;


import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;

public interface MailService {
    public void sendMailAsync(String to, String body, String subject);

    public SimpleMailMessage createMail(String to, String body, String subject);
}
