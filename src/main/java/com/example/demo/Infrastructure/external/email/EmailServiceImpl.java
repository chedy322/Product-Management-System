package com.example.demo.Infrastructure.external.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.Domain.Interfaces.EmailService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;

    // Method 1
    // To send a simple email
    public void sendSimpleMail(String details)
    {

        // Try block to check for exceptions
        try {

            // Creating a simple mail message
            SimpleMailMessage mailMessage
                = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo("chbouountito@gmail.com");
            mailMessage.setText(details);
            mailMessage.setSubject("Test");

            // Sending the mail
            javaMailSender.send(mailMessage);
          
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            log.warn( "Error while Sending Mail");
        }
    }

}
