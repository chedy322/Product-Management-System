package com.example.demo.Domain.events;

import java.util.UUID;

// import javax.lang.model.element.Name;
import com.example.demo.Domain.ValueObjects.Name;

import com.example.demo.Domain.shared.DomainEvent;

public record ProductCreated(
    UUID aggregateId,
    Name productName
)  implements DomainEvent
{

}