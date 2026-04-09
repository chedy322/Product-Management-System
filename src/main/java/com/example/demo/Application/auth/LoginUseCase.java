package com.example.demo.Application.auth;


import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.Application.auth.dto.JwtPayload;
import com.example.demo.Application.auth.dto.LoginResponse;
import com.example.demo.Application.auth.dto.RefreshTokenPayload;
import com.example.demo.Application.auth.dto.UserLoginRequest;
import com.example.demo.Application.user.dto.UserRequest;
import com.example.demo.Application.user.dto.UserResponse;
import com.example.demo.Domain.Interfaces.DomainEventPublisher;

import com.example.demo.Domain.Interfaces.PasswordEncoder;
import com.example.demo.Domain.Interfaces.TokenProvider;
import com.example.demo.Domain.refreshToken.entities.RefreshToken;
import com.example.demo.Domain.refreshToken.interfaces.RefreshTokenRepository;
import com.example.demo.Domain.shared.Error;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.user.entities.User;
import com.example.demo.Domain.user.interfaces.UserRepository;
import com.example.demo.Domain.user.services.CheckUserEmailUniqueness;
import com.example.demo.Domain.user.services.CheckUsernameUniqueness;

import jakarta.transaction.Transactional;


@Service
public class LoginUseCase {
   private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
     public LoginUseCase(UserRepository userRepository,
    PasswordEncoder passwordEncoder,TokenProvider tokenProvider,RefreshTokenRepository refreshTokenRepository){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.tokenProvider=tokenProvider;
        this.refreshTokenRepository=refreshTokenRepository;

    }

  
    public Result<LoginResponse> login(UserLoginRequest userRequest){
        // 1. check for user existing in db
        Optional<User> existingUserResult=userRepository.findByEmail(userRequest.email());
        // Check the password and the existig user
        Boolean isValid=existingUserResult.isPresent() && passwordEncoder.matches(userRequest.password(), existingUserResult.get().getPassword());
        if(!isValid){
            return Result.Failure(Error.UNAUTHORIZED("Invalid credentials.Please try again"));
        }
        User existingUser=existingUserResult.get();
        // 5.Create the payload for access token and refreshtkooen
        UUID refreshTokenId=UUID.randomUUID();
        Date createdAt=new Date();
        Date expireAt=new Date(createdAt.getTime()+(7L*24*60*60*1000));
        JwtPayload jwtPayloadData=JwtPayload.map(existingUser.getEmail(), existingUser.getUsername(), existingUser.getId());
        RefreshTokenPayload refreshTokenData=RefreshTokenPayload.map(existingUser.getId(), refreshTokenId,
             createdAt, expireAt);

        // 7. generate the access token and refresh token
        String accessToken=tokenProvider.generateToken(jwtPayloadData);
        String refreshToken=tokenProvider.genereateRefreshToken(refreshTokenData);

        // 8.Store the refresh token in db
         Result<RefreshToken> refreshTokenEntityResult=RefreshToken.create(refreshTokenId,
             refreshToken, existingUser.getId(), createdAt, expireAt);
            if(refreshTokenEntityResult.isFailure()){
                return Result.Failure(refreshTokenEntityResult.getError());
            }
        RefreshToken refreshTokenEntity=refreshTokenEntityResult.getValue();
        saveSession(refreshTokenEntity);
        
        //9.Map result to login resposne 
        LoginResponse loginResponse=LoginResponse.map(accessToken, refreshToken);
        return Result.Success(loginResponse);
    }

    @Transactional
    private void saveSession(RefreshToken refreshTokenEntity){
         refreshTokenRepository.save(refreshTokenEntity);
    }
}
