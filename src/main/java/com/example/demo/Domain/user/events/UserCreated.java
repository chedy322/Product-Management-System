package com.example.demo.Domain.user.events;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.Domain.Interfaces.DomainEvent;



public record UserCreated(
    UUID aggregateId,
    LocalDateTime createdAt,
    String email,
    String username
) implements DomainEvent{

}
