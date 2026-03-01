package com.example.demo.Application.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.Domain.user.entities.User;

public record UserResponse(
    UUID userId,
    String username,
    String email,
    LocalDateTime createdAt,
    LocalDateTime updatedAt

) {
    // map input to userResponse
    public static UserResponse UserMapper(User user){
        return new UserResponse(user.getId(),user.getUsername(),user.getEmail(),user.getCreatedAt(),user.getUpdatedAt());
    }
}
