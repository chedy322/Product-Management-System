package com.example.demo.Infrastructure.Web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Application.auth.LoginUseCase;
import com.example.demo.Application.auth.RegisterUseCase;
import com.example.demo.Application.auth.dto.AuthTokenResponse;
import com.example.demo.Application.auth.dto.UserLoginRequest;
import com.example.demo.Application.user.dto.UserRequest;
import com.example.demo.Application.user.dto.UserResponse;
import com.example.demo.Domain.exceptions.DomainExceptions;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Infrastructure.security.jwt.JwtTokenProvider;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthTokenResponse> login(@RequestBody @Valid UserLoginRequest userRequest){
        Result<UserResponse> loginUserResult=loginUseCase.login(userRequest);
        if(loginUserResult.isFailure()){
            throw new DomainExceptions(loginUserResult.getError());
        }
        // create the token 
        String token=jwtTokenProvider.generateToken(loginUserResult.getValue().email());
        // DTO for the token
        AuthTokenResponse authTokenResponse=AuthTokenResponse.map(token);

        return ResponseEntity.status(200).body(authTokenResponse);
    }
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest userRequest){
        Result<UserResponse> registerUserResult=registerUseCase.register(userRequest);
        if(registerUserResult.isFailure()){
            throw new DomainExceptions(registerUserResult.getError());
        }
        return ResponseEntity.status(201).body(registerUserResult.getValue());
    }


}
