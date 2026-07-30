package com.example.demo.Application.auth.dto;

public record RefreshTokenResponse (
    String refreshToken,
    String accessToken
){
    public static RefreshTokenResponse map(
          String refreshToken,
    String accessToken
    ){
        return new RefreshTokenResponse(refreshToken, accessToken);
    }
}
