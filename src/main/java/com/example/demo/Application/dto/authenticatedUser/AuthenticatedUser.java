package com.example.demo.Application.dto.authenticatedUser;

import java.util.UUID;

public record AuthenticatedUser(
    UUID id,
    String email,
    String username
) {

    public static AuthenticatedUser map(
  UUID id,
    String email,
    String username
    ){
        return new AuthenticatedUser(id, email, username);
    }
}