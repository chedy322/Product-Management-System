package com.example.demo.Domain.user.services;

import java.util.Optional;


import org.springframework.stereotype.Service;

import com.example.demo.Domain.shared.Error;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.user.entities.User;
import com.example.demo.Domain.user.interfaces.UserRepository;


@Service
public class CheckUserEmailUniqueness {
    public final UserRepository userRepository;

    public CheckUserEmailUniqueness (UserRepository userRepository ){
        this.userRepository=userRepository;
    }

    public Result<Boolean> CheckEmailUniqueness(String userInputEmail){
        // find user by email in db
        Optional<User> existingUser=userRepository.findByEmail(userInputEmail);
        if(!existingUser.isEmpty())
        {
            return Result.Failure(Error.CONFLICT("User already exists,please login."));
        }
        return Result.Success(true);

    }


}
