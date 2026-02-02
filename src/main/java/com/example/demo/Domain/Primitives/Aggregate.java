package com.example.demo.Domain.Primitives;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class Aggregate extends Entity {
    private final List<Object> domainEvents=new ArrayList<>();
    protected Aggregate(UUID id){
        super(id);
    }

    protected List<Object> getDomainEvents(){
        return Collections.unmodifiableList(domainEvents);
    }
}