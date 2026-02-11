package com.example.demo.Domain.Primitives;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.example.demo.Domain.shared.DomainEvent;

public abstract class Aggregate extends Entity {
    private final List<DomainEvent> domainEvents=new ArrayList<>();
    protected Aggregate(UUID id){
        super(id);
    }

    protected List<Object> getDomainEvents(){
        return Collections.unmodifiableList(domainEvents);
    }

    protected void registerEvent(DomainEvent domainEvent){
        domainEvents.add(domainEvent);
    }

    protected void deleteEvents(){
        domainEvents.clear();
    }


}