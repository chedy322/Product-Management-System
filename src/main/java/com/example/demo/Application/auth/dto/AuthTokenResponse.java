package com.example.demo.Application.auth.dto;

public record AuthTokenResponse(
    String accessToken,
    String refreshToken
    
) {
    public static AuthTokenResponse map(String accessToken,String refreshToken){
        return new AuthTokenResponse(
            accessToken,refreshToken
        );
    }
}
