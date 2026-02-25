package com.example.demo.Domain.user.services;
import com.example.demo.Infrastructure.persistence.user.JpaUserRepository;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Domain.shared.Error;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.user.entities.User;
import com.example.demo.Domain.user.interfaces.UserRepository;

@Service
public class CheckUserEmailUniqueness {


    private final UserRepository userRepository;

    public CheckUserEmailUniqueness(UserRepository userRepository){
        this.userRepository=userRepository;
        
    } 
     public Result<Boolean> UserEmailIsUnique(String userEmail){
        Optional<User> user=userRepository.findByEmail(userEmail);
        if(!user.isEmpty()){
            return Result.Failure(Error.CONFLICT("Email already in use please sign-in"));
        }
        return Result.Success(true);

     }
}
