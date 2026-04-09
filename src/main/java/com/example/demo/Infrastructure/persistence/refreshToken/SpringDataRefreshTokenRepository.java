package com.example.demo.Infrastructure.persistence.refreshToken;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataRefreshTokenRepository extends JpaRepository<RefreshTokenEntity,UUID> {
    public Optional<RefreshTokenEntity> findByIdAndUserId(UUID refreshTokenId,UUID userId);
    @Modifying
    @Query("DELETE FROM RefreshTokenEntity r WHERE r.user.id= :userId ")
    public void deleteByUserId(UUID userId);
}
