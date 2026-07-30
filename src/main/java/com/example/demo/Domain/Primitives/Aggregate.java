package com.example.demo.Domain.Primitives;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.example.demo.Domain.Interfaces.DomainEvent;

// abstract so we cannot create instance of this entity
public abstract class Aggregate extends Entity {
    private final List<DomainEvent> domainEvents=new ArrayList<>();
    protected Aggregate(UUID id){
        super(id);
    }

    public List<Object> getDomainEvents(){
        return Collections.unmodifiableList(domainEvents);
    }

    public void registerEvent(DomainEvent domainEvent){
        domainEvents.add(domainEvent);
    }

    public void deleteEvents(){
        domainEvents.clear();
    }


}