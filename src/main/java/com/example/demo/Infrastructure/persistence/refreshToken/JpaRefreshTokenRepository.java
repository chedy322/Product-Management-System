package com.example.demo.Infrastructure.persistence.refreshToken;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.demo.Domain.refreshToken.entities.RefreshToken;
import com.example.demo.Domain.refreshToken.interfaces.RefreshTokenRepository;
import com.example.demo.Infrastructure.persistence.user.JpaUserRepository;
import com.example.demo.Infrastructure.persistence.user.UserEntity;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor
public class JpaRefreshTokenRepository  implements RefreshTokenRepository{
    private  final SpringDataRefreshTokenRepository springDataRefreshTokenRepository;
    private final JpaUserRepository JpaUserRepository;
    @Override
    public void save(RefreshToken refreshToken) {
        //1. Load user ref for the UserEntity in RefreshTokenEntity since business logic is using UUID
        UUID userId=refreshToken.getUserId();
        UserEntity userRef=JpaUserRepository.getReferenceById(userId);

        // 2. Map refresh token from domain->Db
        RefreshTokenEntity refreshTokenEntity=RefreshTokenMapper.toEntity(refreshToken,userRef);

        //3. Save the user to db
        springDataRefreshTokenRepository.save(refreshTokenEntity);

    }

@Override
public Optional<RefreshToken> findById(UUID id) {
    // 1. Find the refrehs token entity from db
    Optional<RefreshTokenEntity> refreshTokenEntity=springDataRefreshTokenRepository.findById(id);
    // 2. Map the refresh token entity from db->domain
    Optional<RefreshToken> refreshTokenDomain=refreshTokenEntity.map(RefreshTokenMapper::toDomain);
    return refreshTokenDomain;
}


    @Override
    public void deleteById(UUID id) {
        springDataRefreshTokenRepository.deleteById(id);
    }

    @Override
    public Optional<RefreshToken> findByIdAndUserId(UUID refreshTokenId, UUID userId) {
        Optional<RefreshTokenEntity> refreshTokenEntity=springDataRefreshTokenRepository.findByIdAndUserId(refreshTokenId,userId);
        // make the db entity to domain entity
        Optional<RefreshToken> refreshTokenDomainEntity=refreshTokenEntity.map(RefreshTokenMapper::toDomain);
        return refreshTokenDomainEntity;
    }

    @Override
    public void deleteByUserId(UUID userId) {
        springDataRefreshTokenRepository.deleteByUserId(userId);
        
    }
}
