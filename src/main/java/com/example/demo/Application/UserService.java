package com.example.demo.Application;

import org.springframework.stereotype.Service;

import com.example.demo.Domain.Entities.User;
import com.example.demo.Domain.Interfaces.UserRepository;

import com.example.demo.Domain.ValueObjects.Name;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private  UserRepository userRepository;

    public UserService(UserRepository userRepository ){
        this.userRepository=userRepository;
    }

    // create
    @Transactional
    public boolean createUser(Name name,String email,String password){
        // this will retrun true if email it finds email in the repository
        boolean EmailfoundIndb=userRepository.findByEmail(email);
        boolean EmailIsUnique=!EmailfoundIndb;
        User user=User.create(name,email,password,EmailIsUnique);
        userRepository.save(user);
        return true;
    }

    // delete


}