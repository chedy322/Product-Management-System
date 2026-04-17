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
import com.example.demo.Infrastructure.config.Enum.UserRole;


import jakarta.transaction.Transactional;

import com.example.demo.Domain.Interfaces.DomainEventPublisher;
import com.example.demo.Domain.refreshToken.interfaces.RefreshTokenRepository;
import com.example.demo.Domain.shared.Error;
import com.example.demo.Domain.shared.Result;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    public UserService(UserRepository userRepository,RefreshTokenRepository refreshTokenRepository){
        this.userRepository=userRepository;
        this.refreshTokenRepository=refreshTokenRepository;
    }

    // thse methods are for ADMIN 
       public Result<List<User>> findAll(){
        return Result.Success(userRepository.findAll());
    }

    public Result<User> findById(UUID userId){
        //find user in db
        Optional<User> existingUser=userRepository.findById(userId);
        if(existingUser.isEmpty()){
            return Result.Failure(Error.NOT_FOUND("User not found"));
        }
        // user domain
        User userDomain=existingUser.get();

        return Result.Success(userDomain);
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
     public Result<Boolean> changeRoleStatus(UUID userToUpgradeId,UserRole upgradedRole){
        // 1. check if the user exists in db
        Optional<User> existingUser=userRepository.findById(userToUpgradeId);
        if(existingUser.isEmpty()){
            return Result.Failure(Error.NOT_FOUND("User not found"));
        }
        // 2. get  user data
        User existingUserData=existingUser.get();
        // 3. change the status and save to db
        existingUserData.ChangeRole(upgradedRole);
        userRepository.save(existingUserData);
        // 4. Invalidate all refreshTokens from db for security
        refreshTokenRepository.deleteByUserId(userToUpgradeId);
        return Result.Success(true);


    }

    // this method is for ADMIN and USER
    @Transactional
    public Result<User> updateUser(UUID userId,UpdateUserRequest userRequest){
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
       
        return Result.Success(user);


    }

    // public booelan findCurrentUserData()
}
