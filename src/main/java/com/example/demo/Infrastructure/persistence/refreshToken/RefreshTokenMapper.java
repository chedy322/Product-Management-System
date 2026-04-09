package com.example.demo.Infrastructure.persistence.refreshToken;

import com.example.demo.Domain.exceptions.DomainExceptions;
import com.example.demo.Domain.refreshToken.entities.RefreshToken;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Infrastructure.persistence.user.UserEntity;
import com.example.demo.Domain.shared.Error;

public class RefreshTokenMapper {
    public static RefreshToken toDomain(RefreshTokenEntity refreshTokenEntity){
        // 1. Create the domain refresh token
        Result<RefreshToken> refreshTokenDomainResult=RefreshToken.create(refreshTokenEntity.getId(), refreshTokenEntity.getRefreshToken(), refreshTokenEntity.getUser().getId(),
         refreshTokenEntity.getCreatedAt(), refreshTokenEntity.getExpireAt());
        if(refreshTokenDomainResult.isFailure()){
            throw new DomainExceptions(Error.CONFLICT("Data Integrity Error: Could not reconstruct refreshToken aggregate"));
        }
        // 2. Return results
        return refreshTokenDomainResult.getValue();

    }

    public static RefreshTokenEntity toEntity(RefreshToken refreshTokenDomain,UserEntity userRef){
            RefreshTokenEntity refreshTokenEntity=new RefreshTokenEntity(refreshTokenDomain.getId(), refreshTokenDomain.getRefreshToken(),
                 userRef, refreshTokenDomain.getCreatedAt(), refreshTokenDomain.getExpireAt());
            return refreshTokenEntity;
    }
}
