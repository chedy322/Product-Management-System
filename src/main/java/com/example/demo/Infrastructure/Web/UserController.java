package com.example.demo.Infrastructure.Web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.example.demo.Domain.user.entities.User;
import com.example.demo.Infrastructure.Web.dto.adminController.ResponseUserDetails;
import com.example.demo.Infrastructure.security.jwt.CustomUserDetails;
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
    public ResponseEntity<?> getUserById(@AuthenticationPrincipal CustomUserDetails  authenticatedUser) {
        Result<User> userResult = userService.findById(authenticatedUser.getUserId());
        if(userResult.isFailure()){
            
            Error error=userResult.getError();
            
            throw new DomainExceptions(error);
        }
        User user = userResult.getValue();
        ResponseUserDetails userResponse=ResponseUserDetails.map(user);
        return ResponseEntity.ok().body(userResponse);
    }

   

    // @DeleteMapping("/{id}")
    // public ResponseEntity<?> deleteUser(@PathVariable("id") UUID userId) {
    //     Result<Boolean> userResult=userService.deleteById(userId);
    //     if(userResult.isFailure()){
       
    //         Error error=userResult.getError();
          
    //         throw new DomainExceptions(error);
    //     }
    //     return ResponseEntity.status(201).body("User deleted successfully");
    // }

    @PutMapping("")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal CustomUserDetails authenticatedUser,@RequestBody(required = false) @Valid UpdateUserRequest userRequest ) {
            Result<User> userUpdateResult=userService.updateUser(authenticatedUser.getUserId(), userRequest);
            if(userUpdateResult.isFailure()){
                Error error=userUpdateResult.getError();
                 throw new DomainExceptions(error);
            }
            User updatedUser=userUpdateResult.getValue();
            // 2.Convert the userUpdatedResult to dto
             ResponseUserDetails userResponse=ResponseUserDetails.map(updatedUser);
            return ResponseEntity.status(200).body(userResponse);
    }
    

    

}
