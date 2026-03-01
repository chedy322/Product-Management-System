package com.example.demo.Domain.user.entities;

import com.example.demo.Infrastructure.config.Enum.UserRole;
import java.time.LocalDateTime;

<<<<<<< HEAD

public class User {
    private final Long id; // Final because the identity shouldn't change
=======
import com.example.demo.Domain.Primitives.Aggregate;
import com.example.demo.Domain.shared.Error;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.user.events.UserCreated;

public class User extends Aggregate{
    // private final UUID id; 
    private String username;
>>>>>>> 52f4d37 (Feat:Adde user service)
    private String email;
    private String password;
    private UserRole role=UserRole.USER;
    private boolean blocked=false;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Comprehensive Constructor for the Mapper/Repository to use
<<<<<<< HEAD
    private User(Long id, String email, String password, UserRole role, 
                boolean blocked, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
=======
    private User(UUID id, String email, String password, UserRole role, 
                boolean blocked, LocalDateTime createdAt, LocalDateTime updatedAt,String username) {
            super(id);
>>>>>>> 52f4d37 (Feat:Adde user service)
        this.email = email;
        this.password = password;
        this.role = role;
        this.blocked = blocked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.username=username;
    }

<<<<<<< HEAD
    public static User create(Long id, String email, String password, UserRole role, 
                boolean blocked, LocalDateTime createdAt, LocalDateTime updatedAt){
                    // TODO 
                    // create validate email method
                    // check if email exists in the db 
                User user= new User(id,email,password,role,blocked,createdAt,updatedAt);
                return user;
=======
    public static Result<User> create(String email, String password,String username){
        LocalDateTime currenDateTime=LocalDateTime.now();
                User user= new User(UUID.randomUUID(),email,password,UserRole.USER,false,currenDateTime,currenDateTime,username);
                // register user created event
                user.registerEvent(new UserCreated(user.getId(),user.getCreatedAt(),user.getEmail(),user.getUsername()));
                return Result.Success(user);
>>>>>>> 52f4d37 (Feat:Adde user service)
    }

    public static Result<User> reconstruct(UUID id, String email, String password, UserRole role, 
                boolean blocked, LocalDateTime createdAt, LocalDateTime updatedAt,String username){
                User user= new User(id,email,password,role,blocked,createdAt,updatedAt,username);
                return Result.Success(user);
    }

    public Result<Void> changeEmail(String newEmail) {
        if (newEmail == null || !newEmail.contains("@")) {
            // throw new IllegalArgumentException("Invalid email format");
            return Result.Failure(Error.VALIDATION_ERROR("Invalid email format"));
        }
        this.email = newEmail;
        this.updatedAt = LocalDateTime.now();
        return Result.Success(null);
    }
// change username
    public void changeUsername(String username){
        this.username=username;
        this.updatedAt=LocalDateTime.now();
    }
// change password
// change this in future to value object
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

<<<<<<< HEAD
    public Long getId() { return id; }
=======
    public UUID getId() { return super.getId(); }
>>>>>>> 52f4d37 (Feat:Adde user service)
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public UserRole getRole() { return role; }
    public boolean isBlocked() { return blocked; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getUsername(){return username;}
}
