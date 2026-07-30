package com.example.demo.Domain.product.events;

import java.util.UUID;

import com.example.demo.Domain.Interfaces.DomainEvent;

public record ProductDeletedEvent(UUID productId) implements DomainEvent{

}
