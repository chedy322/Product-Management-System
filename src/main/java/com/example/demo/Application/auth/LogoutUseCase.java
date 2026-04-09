package com.example.demo.Application.auth;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Application.auth.dto.logout.LogoutOutput;
import com.example.demo.Domain.refreshToken.interfaces.RefreshTokenRepository;
import com.example.demo.Domain.shared.Result;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LogoutUseCase {
    
    private final RefreshTokenRepository refreshTokenRepository;
    public Result<LogoutOutput> logout(UUID userId){
            // 1. delete all teh refreshtokens from the db related to that user
                refreshTokenRepository.deleteByUserId(userId);
            // 2.return the success message
            return Result.Success(LogoutOutput.map("User deleted successfully"));
    }   
}
