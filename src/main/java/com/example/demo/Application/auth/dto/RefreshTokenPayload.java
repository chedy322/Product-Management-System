package com.example.demo.Application.auth.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record RefreshTokenPayload(
    UUID userId,
    UUID id,
    Date createdAt,
    Date expireAt
    
    
) {
    // map fucntion to create the refrehstoken instance
    public static RefreshTokenPayload map(UUID userId,UUID refreshTokenId,
            Date createdAt,
    Date expireAt
   
    ){
        return new RefreshTokenPayload(userId,
    refreshTokenId,
     createdAt,
     expireAt
    
        );
    }
}
