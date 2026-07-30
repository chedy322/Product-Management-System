package com.example.demo.Infrastructure.Web.dto.adminController;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.Domain.user.entities.User;
import com.example.demo.Infrastructure.config.Enum.UserRole;

public record UserSummaryResponse (
     UUID id,
    String email,
    String username

){
    // map function user to the ResponseGetAllUsers
    public static UserSummaryResponse map(User user){
        return new UserSummaryResponse(
            user.getId(),
            user.getEmail(),
            user.getUsername()
        );
    }
}
