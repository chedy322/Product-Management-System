package com.example.demo.Infrastructure.Web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Application.user.UserService;
import com.example.demo.Application.user.dto.UpdateUserRequest;
import com.example.demo.Application.user.dto.UserRequest;
import com.example.demo.Application.user.dto.UserResponse;
import com.example.demo.Domain.exceptions.DomainExceptions;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.shared.Error;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
 private UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }

    @GetMapping("")
    public ResponseEntity<?> getUsers() {
        // Implementation for creating a user
        Result<List<UserResponse>> usersResult= userService.findAll();
        if(usersResult.isFailure()){
          
            Error error=usersResult.getError();
            
            throw new DomainExceptions(error);
        }

        // Convert the result to List<UserResponse>
        List<UserResponse> userssEntities=usersResult.getValue();
        return ResponseEntity.ok().body(userssEntities);
        
    }   
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") UUID userId) {
        Result<UserResponse> userResult = userService.findById(userId);
        if(userResult.isFailure()){
            
            Error error=userResult.getError();
            
            throw new DomainExceptions(error);
        }
        UserResponse user = userResult.getValue();
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("")
    public ResponseEntity<?> createProduct(@Valid @RequestBody UserRequest entity) {
        Result<UserResponse> createdUser = userService.create(entity);
        if(createdUser.isFailure()){
            Error error=createdUser.getError();
            throw new DomainExceptions(error);
        }
        return ResponseEntity.status(201).body(createdUser.getValue());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") UUID userId) {
        Result<Boolean> userResult=userService.deleteById(userId);
        if(userResult.isFailure()){
       
            Error error=userResult.getError();
          
            throw new DomainExceptions(error);
        }
        return ResponseEntity.status(201).body("User deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") UUID userId,@RequestBody(required = false) @Valid UpdateUserRequest userRequest ) {
            Result<UserResponse> userUpdateResult=userService.updateUser(userId, userRequest);
            if(userUpdateResult.isFailure()){
                Error error=userUpdateResult.getError();
                 throw new DomainExceptions(error);
            }
            return ResponseEntity.status(200).body(userUpdateResult.getValue());
    }
    

    

}
