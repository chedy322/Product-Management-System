package com.example.demo.Domain.user.services;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.shared.Error;
import com.example.demo.Domain.user.interfaces.UserRepository;
import com.example.demo.Domain.user.entities.User;

@Component
public class CheckUsernameUniqueness {
    private final UserRepository userRepository;

    public CheckUsernameUniqueness(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public Result<Boolean> CheckUsername(String username){
        Optional<User> existingUser=userRepository.findByUsername(username);
        if(!existingUser.isEmpty()){
            return Result.Failure(Error.CONFLICT("Username already in use please sign-in"));
        }
        return Result.Success(true);

    }
}
