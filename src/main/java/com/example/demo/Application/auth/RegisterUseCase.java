package com.example.demo.Application.auth;


import org.springframework.stereotype.Service;

import com.example.demo.Application.user.dto.UserRequest;
import com.example.demo.Application.user.dto.UserResponse;
import com.example.demo.Domain.Interfaces.DomainEventPublisher;
import com.example.demo.Domain.Interfaces.PasswordEncoder;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.user.entities.User;
import com.example.demo.Domain.user.interfaces.UserRepository;
import com.example.demo.Domain.user.services.CheckUserEmailUniqueness;
import com.example.demo.Domain.user.services.CheckUsernameUniqueness;

import jakarta.transaction.Transactional;

@Service
public class RegisterUseCase {
    private final UserRepository userRepository;
    private final CheckUserEmailUniqueness checkUserEmailUniqueness;
    private final CheckUsernameUniqueness checkUsernameUniqueness;
    private final DomainEventPublisher domainEventPublisher;
    private final PasswordEncoder passwordEncoder;
    
    public RegisterUseCase(UserRepository userRepository,CheckUserEmailUniqueness checkUserEmailUniqueness, 
        CheckUsernameUniqueness checkUsernameUniqueness,DomainEventPublisher domainEventPublisher,
    PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.checkUserEmailUniqueness=checkUserEmailUniqueness;
        this.checkUsernameUniqueness=checkUsernameUniqueness;
        this.domainEventPublisher=domainEventPublisher;
        this.passwordEncoder=passwordEncoder;

    }

    @Transactional
    public Result<UserResponse>  register(UserRequest userRequest){
        // check user email uniqueness
        Result<Boolean> emailIsUniqueResult=checkUserEmailUniqueness.CheckEmailUniqueness(userRequest.email());
        if(emailIsUniqueResult.isFailure()){
            return Result.Failure(emailIsUniqueResult.getError());
        }
        // check user name is unique TODO 
        Result<Boolean> usernameIsUniqueResult=checkUsernameUniqueness.CheckUsername(userRequest.username());
        if(usernameIsUniqueResult.isFailure()){
            return Result.Failure(usernameIsUniqueResult.getError());
        }
        // encode password  
        String encodedPassword=passwordEncoder.encode(userRequest.password());

        Result<User> userResult=User.create(userRequest.email(),encodedPassword,userRequest.username());
        if(userResult.isFailure()){
            return Result.Failure(userResult.getError());
        }
        // get the user value from the result
        User user=userResult.getValue();
        // save to db
        User savedUser=userRepository.save(user);
        // dispathc the user created event
        domainEventPublisher.dispatch(savedUser);
        // map the user data to userResponse data format
        UserResponse userReturnedData=UserResponse.UserMapper(savedUser);
        return Result.Success(userReturnedData);

    }

}
