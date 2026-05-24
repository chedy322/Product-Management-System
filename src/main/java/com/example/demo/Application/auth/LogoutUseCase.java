package com.example.demo.Application.auth;

import java.util.UUID;

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

@Service
@RequiredArgsConstructor
@Transactional
public class LogoutUseCase {
    
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokensBlackList tokensBlackList;
    private final TokenProvider tokenProvider;

    public Result<LogoutOutput> logout(UUID userId,String accessToken,String refreshToken){
            // 1. delete all teh refreshtokens from the db related to that user
                refreshTokenRepository.deleteByUserId(userId);
            // Decode the token 
            RefreshTokenPayload refreshTokenPayload= tokenProvider.RefreshTokenDecodedPayload(refreshToken);
            AccessTokenPayload accessTokenPayload=tokenProvider.AccessTokenDecodedPayload(accessToken);
                // Blacklist the accessToken and refreshToken
                tokensBlackList.addtBlackListedToken(refreshTokenPayload.id());
                tokensBlackList.addtBlackListedToken(accessTokenPayload.id());
            // 2.return the success message
            return Result.Success(LogoutOutput.map("User logged out successfully"));
    }   
}
