package com.example.demo.Application.auth;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Application.auth.dto.UserLoginRequest;
import com.example.demo.Application.user.dto.UserRequest;
import com.example.demo.Application.user.dto.UserResponse;
import com.example.demo.Domain.Interfaces.DomainEventPublisher;
import com.example.demo.Domain.Interfaces.JwtFilter;
import com.example.demo.Domain.Interfaces.PasswordEncoder;
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
    private final JwtFilter jwtFilter;

     public LoginUseCase(UserRepository userRepository,
    PasswordEncoder passwordEncoder,JwtFilter jwtFilter){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtFilter=jwtFilter;

    }

    @Transactional
    public Result<UserResponse> login(UserLoginRequest userRequest){
        // 1. check for user existing in db
        Optional<User> existingUserResult=userRepository.findByEmail(userRequest.email());
        if(existingUserResult.isEmpty()){
            return Result.Failure(Error.VALIDATION_ERROR("Wrong email or password"));
        }

        User existingUser=existingUserResult.get();
        // 2. compare passwords
        Boolean passwordMatches=passwordEncoder.matches(userRequest.password(),existingUser.getPassword());
        // 3. if not return invalid email or password
        if(!passwordMatches){
            return Result.Failure(Error.VALIDATION_ERROR("Wrong email or password"));
        }


        // 4.Map user to userResponse
        UserResponse userResponse=UserResponse.UserMapper(existingUser);
        // 5.if passwrod is correct return user for now
        return Result.Success(userResponse);
    }
}
