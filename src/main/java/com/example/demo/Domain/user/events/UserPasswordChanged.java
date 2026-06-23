package com.example.demo.Domain.user.events;

import java.util.UUID;

import com.example.demo.Domain.Interfaces.DomainEvent;
import com.example.demo.Domain.user.entities.User;

public record UserPasswordChanged(String userEmail,UUID userId) implements DomainEvent {
} 
