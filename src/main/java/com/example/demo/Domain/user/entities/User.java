package com.example.demo.Domain.user.entities;

import com.example.demo.Infrastructure.config.Enum.UserRole;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.demo.Domain.Primitives.Aggregate;
import com.example.demo.Domain.shared.Error;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.user.events.UserCreated;

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
        this.email = newEmail;
        this.updatedAt = LocalDateTime.now();
        return Result.Success(true);
    }
// change username
    public void changeUsername(String username){
        this.username=username;
        this.updatedAt=LocalDateTime.now();
    }
// change password
// change this in future to value object with validation 
    public void changePassword(String password){
        this.password=password;
        this.updatedAt=LocalDateTime.now();
    }

// change role
    public void ChangeRole(UserRole userRole){
        this.role=userRole;
        this.updatedAt=LocalDateTime.now();
    }

// change blocked
    public void block(Boolean blockStatus) {
        this.blocked = blockStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void promoteToAdmin() {
        this.role = UserRole.ADMIN;
        this.updatedAt = LocalDateTime.now();
    }

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
