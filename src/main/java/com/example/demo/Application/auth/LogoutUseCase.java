package com.example.demo.Application.auth;

import java.util.UUID;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Application.auth.dto.AccessTokenPayload;
import com.example.demo.Application.auth.dto.RefreshTokenPayload;
import com.example.demo.Application.auth.dto.logout.LogoutOutput;
import com.example.demo.Domain.Interfaces.TokenProvider;
import com.example.demo.Domain.Interfaces.TokensBlackList;
import com.example.demo.Domain.refreshToken.interfaces.RefreshTokenRepository;
import com.example.demo.Domain.shared.Result;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LogoutUseCase {
    
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokensBlackList tokensBlackList;
    private final TokenProvider tokenProvider;

    public Result<LogoutOutput> logout(UUID userId,String accessToken,String refreshToken){
    log.info("User with Id:{} is attempting to logout...",userId);
    if (userId != null) {
        refreshTokenRepository.deleteByUserId(userId);
        }

    // 2. Only decode and blacklist the refresh token if it was actually provided
    // No need to blacklist as i deleted all refresh tokens for that user 

    // if (refreshToken != null && !refreshToken.isBlank()) {
    //     try {
    //         RefreshTokenPayload refreshTokenPayload = tokenProvider.RefreshTokenDecodedPayload(refreshToken);
    //         long refreshTokenRemainingTime = getRemainingTimeForToken(refreshTokenPayload.expireAt());
    //         //
    //     } catch (Exception e) {
    //         // Log it but don't crash, the token might be malformed or already expired
    //         System.err.println("Failed to blacklist refresh token: " + e.getMessage());
    //     }
    // }

    // 3. Only decode and blacklist the access token if it was actually provided
    if (accessToken != null && !accessToken.isBlank()) {
        try {
            AccessTokenPayload accessTokenPayload = tokenProvider.AccessTokenDecodedPayload(accessToken);
            long accessTokenRemainingTime = getRemainingTimeForToken(accessTokenPayload.expireAt());
            // BlackList the access Token
            tokensBlackList.addtBlackListedToken(accessToken, accessTokenRemainingTime);
        } catch (Exception e) {
            log.warn("Failed to blacklist accessToken for user with Id:{} with error: " , userId,e.getMessage());
        }
    }

    // 4. Return success message (the cookies will still be wiped out by the controller)
    log.info("User with Id:{} logged out successfully",userId);
    return Result.Success(LogoutOutput.map("User logged out successfully"));
    }   
    private  long getRemainingTimeForToken(Date expiresAt){
        return new Date().getTime()-expiresAt.getTime();
    }
}
