package com.example.demo.Infrastructure.Web.dto.adminController;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.Domain.user.entities.User;
import com.example.demo.Infrastructure.config.Enum.UserRole;

public record ResponseUserDetails(
    UUID id,
    String email,
    String username,
    String  role,
    boolean blocked,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
        public static ResponseUserDetails map(User user){
            // change the UserRole to string 
            String userRole=user.getRole().toString();
            return new ResponseUserDetails(user.getId(), user.getEmail(),
             user.getUsername(), userRole, user.isBlocked(), 
             user.getCreatedAt(), user.getUpdatedAt());
        }
}
