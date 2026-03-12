package com.example.demo.Application.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.Application.user.dto.UserResponse;
import com.example.demo.Application.user.dto.UpdateUserRequest;
import com.example.demo.Application.user.dto.UserRequest;
import com.example.demo.Domain.user.entities.User;
import com.example.demo.Domain.user.interfaces.UserRepository;
import com.example.demo.Domain.user.services.CheckUserEmailUniqueness;
import com.example.demo.Domain.user.services.CheckUsernameUniqueness;

import jakarta.transaction.Transactional;

import com.example.demo.Domain.Interfaces.DomainEventPublisher;
import com.example.demo.Domain.shared.Error;
import com.example.demo.Domain.shared.Result;


@Service
public class UserService {
    private UserRepository userRepository;
    private CheckUserEmailUniqueness checkUserEmailUniqueness;
    private CheckUsernameUniqueness checkUsernameUniqueness;
    private DomainEventPublisher domainEventPublisher;
    public UserService(UserRepository userRepository,CheckUserEmailUniqueness checkUserEmailUniqueness,CheckUsernameUniqueness checkUsernameUniqueness, DomainEventPublisher domainEventPublisher){
        this.userRepository=userRepository;
        this.checkUserEmailUniqueness=checkUserEmailUniqueness;
        this.checkUsernameUniqueness=checkUsernameUniqueness;
        this.domainEventPublisher=domainEventPublisher;
    }


    @Transactional
    public Result<UserResponse> create(UserRequest userRequest){
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

    public Result<UserResponse> findById(UUID userId){
        //find user in db
        Optional<User> existingUser=userRepository.findById(userId);
        if(existingUser.isEmpty()){
            return Result.Failure(Error.NOT_FOUND("User not found"));
        }
        // user domain
        User userDomain=existingUser.get();
        // DTO
        UserResponse userResultResponse=UserResponse.UserMapper(userDomain);

        return Result.Success(userResultResponse);
    }

    public Result<Boolean> deleteById(UUID userId){
        // 1. check user exists in db
        Optional<User> existingUser=userRepository.findById(userId);
        if(existingUser.isEmpty()){
            return Result.Failure(Error.NOT_FOUND("User not found"));
        }
        // 2. delete user from db
        userRepository.deleteById(userId);
        return Result.Success(true);
    }

    @Transactional
    public Result<UserResponse> updateUser(UUID userId,UpdateUserRequest userRequest){
        // 1. check if user exists in db
          Optional<User> existingUser=userRepository.findById(userId);
        if(existingUser.isEmpty()){
            return Result.Failure(Error.NOT_FOUND("User not found"));
        }
        // 2. get User entity
        User user=existingUser.get();
        // 3. update based on fields existing
        if(userRequest.email()!=null){
            user.changeEmail(userRequest.email());
        }
        if(userRequest.password()!=null){
            user.changePassword(userRequest.password());
        }
        if(userRequest.username()!=null){
            user.changeUsername(userRequest.username());
        }
        
        // 4. save changes to db
        userRepository.save(user);
        // 5. change return to UserResposne
        UserResponse userResponse=UserResponse.UserMapper(user);
        return Result.Success(userResponse);


    }
}
