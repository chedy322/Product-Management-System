package com.example.demo.Application.common;

import org.springframework.context.ApplicationEventPublisher;

import com.example.demo.Domain.Primitives.Aggregate;

public class EventsDispatcher {
    private final ApplicationEventPublisher publisher;
    public EventsDispatcher(ApplicationEventPublisher publisher){
        this.publisher=publisher;
    }
    public void dispatch(Aggregate aggregate){
        // fire the events
        aggregate.getDomainEvents.forEach(publisher::publishEvent);
        // clear the evetns array
        aggregate.deleteEvents(); 

    }
}
