package com.example.demo.Application.auth.dto;

public record AuthTokenResponse(
    String accessToken,
    String tokenType
    
) {
    public static AuthTokenResponse map(String token){
        return new AuthTokenResponse(token,"Bearer");
    }
}
