package com.example.demo.Domain.refreshToken.interfaces;

import java.util.Optional;
import java.util.UUID;

import com.example.demo.Domain.refreshToken.entities.RefreshToken;

public interface RefreshTokenRepository {
    public void save(RefreshToken refreshToken);
    public Optional<RefreshToken> findById(UUID id);
    public void deleteById(UUID id);
    public Optional<RefreshToken> findByIdAndUserId(UUID refreshTokenId,UUID userId);
    public void deleteByUserId(UUID userId );
}
