package com.example.demo.Domain.user.entities;


import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.Domain.Primitives.Aggregate;
import com.example.demo.Domain.shared.Error;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.user.events.UserAuthorizationChanged;
import com.example.demo.Domain.user.events.UserCreated;
import com.example.demo.Domain.user.events.UserDeleted;
import com.example.demo.Domain.user.events.UserPasswordChanged;
import com.example.demo.Domain.user.events.UserProfileChanged;
import com.example.demo.Infrastructure.config.Enum.UserRole;

public class User extends Aggregate{ 
    private String username;
    private String email;
    private String password;
    private UserRole role=UserRole.USER;
    private boolean blocked=false;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Comprehensive Constructor for the Mapper/Repository to use
    private User(UUID id, String email, String password, UserRole role, 
                boolean blocked, LocalDateTime createdAt, LocalDateTime updatedAt,String username) {
            super(id);
        this.email = email;
        this.password = password;
        this.role = role;
        this.blocked = blocked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.username=username;
    }

    public static Result<User> create(String email, String password,String username){
                LocalDateTime currenDateTime=LocalDateTime.now();
                User user= new User(UUID.randomUUID(),email,password,UserRole.USER,false,currenDateTime,currenDateTime,username);
                // register user created event
                user.registerEvent(new UserCreated(user.getId(),user.getCreatedAt(),user.getEmail(),user.getUsername()));
                return Result.Success(user);
    }

    public static Result<User> reconstruct(UUID id, String email, String password, UserRole role, 
                boolean blocked, LocalDateTime createdAt, LocalDateTime updatedAt,String username){
                User user= new User(id,email,password,role,blocked,createdAt,updatedAt,username);
                return Result.Success(user);
    }

    public Result<Boolean> changeEmail(String newEmail) {
        if (newEmail == null || !newEmail.contains("@")) {
            return Result.Failure(Error.VALIDATION_ERROR("Invalid email format"));
        }
        if(newEmail.equals(this.email)){
             return Result.Success(false);
        }
        this.email = newEmail;
        this.updatedAt = LocalDateTime.now();
        // store userProfileCHanged event
        this.registerEvent(new UserProfileChanged(this.getId()));
        return Result.Success(true);
    }
// change username
    public Result<Boolean> changeUsername(String username){
        if(username.equals(this.username)){
            return Result.Success(false);
        }
        this.username=username;
        this.updatedAt=LocalDateTime.now();
        this.registerEvent(new UserProfileChanged(this.getId()));
        return Result.Success(true);
    }
// change password
// change this in future to value object with validation 
    public void changePassword(String password){
        // Make check for password

        this.password=password;
        this.updatedAt=LocalDateTime.now();
        this.registerEvent(new UserPasswordChanged(this.email,this.getId()));
    }

// change role
// Additionally, there is no validation on who can change roles or if the role is actually changing.
//  If a user is already a USER and you call ChangeRole(UserRole.USER),
//  you will prematurely update updatedAt and fire an unnecessary cache eviction event.
    public Result<Boolean> ChangeRole(UserRole userRole){
        if (userRole.equals(this.role)){
            return Result.Success(false);
        }
        this.role=userRole;
        this.updatedAt=LocalDateTime.now();
        this.registerEvent(new UserAuthorizationChanged(this.getId()));
        return Result.Success(true);
    }

// change blocked
//This function is implimented but later i would need to ad the event here 
    public void block(Boolean blockStatus) {
        this.blocked = blockStatus;
        this.updatedAt = LocalDateTime.now();
      
    }
    public void deletedUser(){
        this.registerEvent(new UserDeleted(this.email, this.getId()));
    }

    // public void promoteToAdmin() {
    //     this.role = UserRole.ADMIN;
    //     this.updatedAt = LocalDateTime.now();
    //     this.registerEvent(new UserAuthorizationChanged(this.getId()));
    // }

    // --- GETTERS ---

    // public UUID getId() { return super.getId(); }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public UserRole getRole() { return role; }
    public boolean isBlocked() { return blocked; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getUsername(){return username;}
}
