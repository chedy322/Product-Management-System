package com.example.demo.Domain.product.events;

import java.util.UUID;

import com.example.demo.Domain.Interfaces.DomainEvent;
import com.example.demo.Domain.product.ValueObjects.Name;

public record ProductCreatedEvent(
    UUID aggregateId,
    Name productName
)  implements DomainEvent
{

}
