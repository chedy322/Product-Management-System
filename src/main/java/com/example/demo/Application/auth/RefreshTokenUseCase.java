package com.example.demo.Application.auth;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Application.auth.dto.JwtPayload;
import com.example.demo.Application.auth.dto.RefreshTokenPayload;
import com.example.demo.Application.auth.dto.RefreshTokenResponse;
import com.example.demo.Domain.Interfaces.TokenProvider;
import com.example.demo.Domain.refreshToken.entities.RefreshToken;
import com.example.demo.Domain.refreshToken.interfaces.RefreshTokenRepository;
import com.example.demo.Domain.shared.Error;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.user.entities.User;
import com.example.demo.Domain.user.interfaces.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public Result<RefreshTokenResponse> refreshToken(String oldRefreshToken){
        RefreshTokenPayload refreshTokenPayload=tokenProvider.RefreshTokenDecodedPayload(oldRefreshToken);
        //1. extract the refreshtoken id 
        UUID refreshTokenId=refreshTokenPayload.id();
        // 2.extract the user id 
        UUID refreshTokenUserId=refreshTokenPayload.userId();
        // 4. check for the user in db
        Optional<User> existingUser=userRepository.findById(refreshTokenUserId);
        if(existingUser.isEmpty()){
            return Result.Failure(Error.UNAUTHORIZED("Invalid session or user. Please log in again."));
        }
        // 4. check if the refresh token exists for that user
        Optional<RefreshToken> existingRefreshToken=refreshTokenRepository.findByIdAndUserId(refreshTokenId,refreshTokenUserId);
        if(existingRefreshToken.isEmpty()){
            // Invalidate all tokens for that user
            refreshTokenRepository.deleteByUserId(refreshTokenUserId);
            return Result.Failure(Error.UNAUTHORIZED("Invalid session or user. Please log in again."));
        }
        // 5. invalidate the previous refresh token
        refreshTokenRepository.deleteById(refreshTokenId);
        // 6. generate the new refrehsToken domain entity time and id
        Date currentDate=new Date();
        Date expiresAt=new Date(currentDate.getTime()+(7L * 24 * 60 * 60 * 1000));
        UUID newrefreshTokenId=UUID.randomUUID();

        // 7. prepare the payload for the accessToken and refreshToken
        String username=existingUser.get().getUsername();
        String email=existingUser.get().getEmail();
        UUID userId=existingUser.get().getId();
        JwtPayload jwtPayload=JwtPayload.map(email, username, userId);
        
        RefreshTokenPayload newrefreshTokenPayload=RefreshTokenPayload.map(userId, 
            newrefreshTokenId, 
            currentDate, expiresAt);

        // 8. generate new refresh token and access token
        String newaccessToken=tokenProvider.generateToken(jwtPayload);
        String newrefreshToken=tokenProvider.genereateRefreshToken(newrefreshTokenPayload);
        
        // 8. create domain entity of refresh token and save it to db
        Result<RefreshToken> newRefreshTokenEntityResult=RefreshToken.create(newrefreshTokenId,
             newrefreshToken, userId, currentDate, expiresAt);
            if(newRefreshTokenEntityResult.isFailure()){
                return Result.Failure(newRefreshTokenEntityResult.getError());
            }
        RefreshToken newRefreshTokenEntity=newRefreshTokenEntityResult.getValue();
        refreshTokenRepository.save(newRefreshTokenEntity);
        // 9. return the results in dto object
        return Result.Success(RefreshTokenResponse.map(newrefreshToken, newaccessToken));
    }   
}
