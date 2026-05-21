package com.example.demo.Application.user.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.demo.Domain.Interfaces.EmailService;
import com.example.demo.Domain.user.events.UserCreated;

@Component
public class SendEmailOnUserCreated {
    public EmailService emailService;
    public SendEmailOnUserCreated(EmailService emailService){
        this.emailService=emailService;
    }   

    @Async
    @EventListener
    public void handle(UserCreated event){
        emailService.sendSimpleMail(String.format("Welcome to your Entreprise shop management ", event.username()));
    }


}
