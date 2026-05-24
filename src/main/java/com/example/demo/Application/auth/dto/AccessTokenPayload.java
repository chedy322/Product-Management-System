package com.example.demo.Application.auth.dto;


import java.util.Date;
import java.util.UUID;

public record AccessTokenPayload ( UUID userId,
    UUID id,
    Date createdAt,
    Date expireAt
    
    
) {
    // map fucntion to create the refrehstoken instance
    public static AccessTokenPayload map(UUID userId,
        UUID accessTokenId,
        Date createdAt,
        Date expireAt
   
    ){
        return new AccessTokenPayload(userId,
    accessTokenId,
     createdAt,
     expireAt
        );
    }
    
}
