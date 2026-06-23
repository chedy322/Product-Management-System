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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.example.demo.Domain.Interfaces.DomainEventPublisher;
import com.example.demo.Domain.refreshToken.interfaces.RefreshTokenRepository;
import com.example.demo.Domain.shared.Error;
import com.example.demo.Domain.shared.Result;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final DomainEventPublisher domainEventPublisher;

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

    public Result<String> deleteById(UUID userId){
        // 1. check user exists in db
        Optional<User> existingUser=userRepository.findById(userId);
        if(existingUser.isEmpty()){
            return Result.Failure(Error.NOT_FOUND("User not found"));
        }
        // 2. delete user from db
        userRepository.deleteById(userId);
        return Result.Success("User deleted succesfully");
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
        // 4. dispatch the event on role changed
        domainEventPublisher.dispatch(existingUserData);
        // 4. Invalidate all refreshTokens from db for security
        refreshTokenRepository.deleteByUserId(userToUpgradeId);
        return Result.Success(true);


    }

    // this method is for ADMIN and USER
    // Fix returning type to DTO
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
            Result<Boolean> userEmailChangeResponse=user.changeEmail(userRequest.email());
            if(userEmailChangeResponse.isFailure()){
                return Result.Failure(userEmailChangeResponse.getError());
            }
        
            Result<Boolean> userUsernameChangeResponse=user.changeUsername(userRequest.username());
            if(userUsernameChangeResponse.isFailure()){
                 return Result.Failure(userUsernameChangeResponse.getError());
            }
        
        Boolean wasMutated=userUsernameChangeResponse.getValue() || userEmailChangeResponse.getValue();
        if(wasMutated){
            // 4. publish the registered events
            domainEventPublisher.dispatch(user);
            // 5. save changes to db
            userRepository.save(user);
        }else{
            log.info("User with id={} made profile update requested, but data was identical. Safely ignoring database rewrite.",user.getId());
        }

        return Result.Success(user);


    }
    // add change password as it s more secure implimentation needed

    // public booelan findCurrentUserData()
}
