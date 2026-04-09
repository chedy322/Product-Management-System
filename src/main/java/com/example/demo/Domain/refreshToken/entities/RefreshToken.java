package com.example.demo.Domain.refreshToken.entities;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import org.springframework.cglib.core.Local;

import com.example.demo.Domain.Primitives.Entity;
import com.example.demo.Domain.shared.Error;
import com.example.demo.Domain.shared.Result;

public class RefreshToken extends Entity{
    private final String refreshToken;
    private final UUID userId;
    private final Date createdAt;
    private final Date expireAt;
    private RefreshToken(
        UUID  id,
        String refreshToken,
        UUID userId,
        Date createdAt,
        Date expireAt
    ){
        super(id);
        this.createdAt=createdAt;
        this.expireAt=expireAt;
        this.refreshToken=refreshToken;
        this.userId=userId;
    }
    public static Result<RefreshToken> create(
        UUID refreshTokenId,
        String refreshToken,
        UUID userId,
        Date createdAt,
        Date expireAt
    ){
        // Validation check
        if(expireAt.before(createdAt)){
            return Result.Failure(Error.VALIDATION_ERROR("Expiration cannot be before creation"));
        }
        return Result.Success(new RefreshToken(refreshTokenId, refreshToken, userId, createdAt, expireAt));

    }
    //GETTERS
    public UUID getId(){return super.getId();}
    public String getRefreshToken(){return this.refreshToken;}
    public UUID getUserId(){return this.userId;}
    public Date getCreatedAt(){return this.createdAt;}
    public Date getExpireAt(){return this.expireAt;}
    public Boolean tokenIsExpired(Date currentTime){return  currentTime.after(this.expireAt);}
    





}
