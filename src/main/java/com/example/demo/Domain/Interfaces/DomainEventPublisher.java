package com.example.demo.Domain.Interfaces;

import com.example.demo.Domain.Primitives.Aggregate;

public interface DomainEventPublisher {
     public void dispatch(Aggregate aggregate);
}
