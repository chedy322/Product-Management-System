package com.example.demo.Domain.user.entities;

import com.example.demo.Infrastructure.config.Enum.UserRole;
import java.time.LocalDateTime;


public class User {
    private final Long id; // Final because the identity shouldn't change
    private String email;
    private String password;
    private UserRole role;
    private boolean blocked=false;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Comprehensive Constructor for the Mapper/Repository to use
    private User(Long id, String email, String password, UserRole role, 
                boolean blocked, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.blocked = blocked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(Long id, String email, String password, UserRole role, 
                boolean blocked, LocalDateTime createdAt, LocalDateTime updatedAt){
                    // TODO 
                    // create validate email method
                    // check if email exists in the db 
                User user= new User(id,email,password,role,blocked,createdAt,updatedAt);
                return user;
    }

    public void changeEmail(String newEmail) {
        if (newEmail == null || !newEmail.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = newEmail;
        this.updatedAt = LocalDateTime.now();
    }

    public void block() {
        this.blocked = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void promoteToAdmin() {
        this.role = UserRole.ADMIN;
        this.updatedAt = LocalDateTime.now();
    }

    // --- GETTERS ---

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public UserRole getRole() { return role; }
    public boolean isBlocked() { return blocked; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
