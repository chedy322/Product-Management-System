package com.example.demo.Application.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
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
    
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
        
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
