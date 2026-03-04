package com.example.demo.Application.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.Application.user.dto.UserResponse;
import com.example.demo.Application.user.dto.UserRequest;
import com.example.demo.Domain.user.entities.User;
import com.example.demo.Domain.user.interfaces.UserRepository;
import com.example.demo.Domain.user.services.CheckUsernameUniqueness;

import jakarta.transaction.Transactional;

import com.example.demo.Domain.Interfaces.DomainEventPublisher;
import com.example.demo.Domain.shared.Result;


@Service
public class UserService {
    private UserRepository userRepository;
    private CheckUsernameUniqueness checkUserEmailUniqueness;
    private CheckUsernameUniqueness checkUsernameUniqueness;
    private DomainEventPublisher domainEventPublisher;
    public UserService(UserRepository userRepository,CheckUsernameUniqueness checkUserEmailUniqueness,CheckUsernameUniqueness checkUsernameUniqueness){
        this.userRepository=userRepository;
        this.checkUserEmailUniqueness=checkUserEmailUniqueness;
        this.checkUsernameUniqueness=checkUsernameUniqueness;
    }


    @Transactional
    public Result<UserResponse> create(UserRequest userRequest){
        Result<User> userResult=User.create(userRequest.email(), userRequest.password(),userRequest.username());
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


    public Result<List<UserResponse>> findAll(){
        return Result.Success(userRepository.findAll().stream().map(UserResponse::UserMapper).toList());
    }

    // public Result<User> findById(UUID userId){
    //     // Optional<User> userRe
    // }

    // public Result<boolean> deleById(){

    // }
    // public Result<UserResponse> updateUser(){

    // }
}
