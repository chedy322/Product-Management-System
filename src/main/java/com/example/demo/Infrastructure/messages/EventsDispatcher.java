package com.example.demo.Infrastructure.messages;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.demo.Domain.Interfaces.DomainEventPublisher;
import com.example.demo.Domain.Primitives.Aggregate;

@Component
public class EventsDispatcher implements DomainEventPublisher{
    private final ApplicationEventPublisher publisher;

    public EventsDispatcher(ApplicationEventPublisher publisher){
        this.publisher=publisher;
    }

    @Override
    public void dispatch(Aggregate aggregate){
        // fire the events
        aggregate.getDomainEvents().forEach(publisher::publishEvent);
        // clear the evetns array
        aggregate.deleteEvents(); 

    }
}
