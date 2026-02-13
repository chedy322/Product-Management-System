package com.example.demo.Application.product.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.demo.Domain.events.ProductCreated;

@Component
public class SendEmailOnProductCreated {
    public SendEmailOnProductCreated(){
    }

    @Async
    @EventListener
    public void handle(ProductCreated event){
        System.out.println(event.productName()+"Is created succesfully.Sending Email");
    }
}
