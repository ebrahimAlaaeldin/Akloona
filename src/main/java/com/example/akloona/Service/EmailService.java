package com.example.akloona.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            // SimpleMailMessage message = new SimpleMailMessage();
            // message.setTo(to);
            // message.setSubject(subject);
            // message.setText(text);
            // mailSender.send(message);MimeMessage message=javaMailSender.createMimeMessage();
            logger.info("sendSimpleEmail: INSIDE" );
            MimeMessage message=mailSender.createMimeMessage();
            MimeMessageHelper helper;
            helper=new MimeMessageHelper(message,true);
            helper.setTo(to);
            logger.info("sendSimpleEmail: try to send email to: {}",to );
            helper.setSubject(subject);
            logger.info("sendSimpleEmail: try to send email subject: {}",subject );
            helper.setText(text);
            logger.info("sendSimpleEmail: try to send email message: {}",text );
            helper.setFrom("reservation@ab-group.host");
            mailSender.send(message);
            logger.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            logger.error("Failed to send email to {}. Error: {}", to, e.getMessage());
            // You can throw a custom exception here if you want
        }
    }
}
