package com.example.demo.Infrastructure.persistence.user;


import java.time.LocalDateTime;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.demo.Infrastructure.config.Enum.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class UserEntity {
    public UserEntity(){}
   
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column(unique = true,length =100,nullable = false)
    private String email;


    @Column(nullable = false)
    private String password;

    // 0 for users 1 for admin 
    @Enumerated(EnumType.STRING)
    private UserRole role=UserRole.USER;

    
    private boolean blocked=false;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    

    public UserEntity(String email,String password){
        this.email=email;
        this.password=password;
    }
    public long getId(){return id;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}
    public UserRole getRole(){return role;}
    public boolean getBlocked(){return blocked;}
    public void setRole(UserRole role){this.role=role;}
    public void setBlocked(boolean blocked){this.blocked=blocked;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public LocalDateTime getUpDatedAt(){return updatedAt;}
}
