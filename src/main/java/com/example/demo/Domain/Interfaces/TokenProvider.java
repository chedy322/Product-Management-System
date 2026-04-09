package com.example.demo.Domain.Interfaces;

import com.example.demo.Application.auth.dto.JwtPayload;
import com.example.demo.Application.auth.dto.RefreshTokenPayload;


public interface TokenProvider {
    public String generateToken(JwtPayload jwtTokenData);
    public String genereateRefreshToken(RefreshTokenPayload refreshTokenPayload );
    public RefreshTokenPayload RefreshTokenDecodedPayload(String refreshToken);
    // public Boolean isTokenValid();
}
