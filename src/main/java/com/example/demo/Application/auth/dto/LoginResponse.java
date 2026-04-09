package com.example.demo.Application.auth.dto;

public record LoginResponse(
    String accessToken,
    String refreshToken
) {
    public static LoginResponse map(String accessToken,String refreshToken){
        return new LoginResponse(accessToken, refreshToken);
    }
}
