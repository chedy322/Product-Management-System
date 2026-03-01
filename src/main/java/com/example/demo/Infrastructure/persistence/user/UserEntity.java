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
<<<<<<< HEAD
=======
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
>>>>>>> 52f4d37 (Feat:Adde user service)

@Entity
@NoArgsConstructor
@Data
@Table(name="users")
public class UserEntity {
<<<<<<< HEAD
    public UserEntity(){}
   
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
=======
    
     public UserEntity(String username,String email,String password){
        this.email=email;
        this.password=password;
        this.username=username;
    }
    // Fix this to UUID 
    @Id
    private UUID id;
>>>>>>> 52f4d37 (Feat:Adde user service)

    @Column(unique = true,length =100,nullable = false)
    private String email;

    @Column(nullable = false,length = 100,unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role=UserRole.USER;

    @Column(name = "blocked")
    private boolean blocked=false;
    
    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    

<<<<<<< HEAD
    public UserEntity(String email,String password){
        this.email=email;
        this.password=password;
    }
    public long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}
    public UserRole getRole(){return role;}
    public boolean getBlocked(){return blocked;}
    public void setRole(UserRole role){this.role=role;}
    public void setBlocked(boolean blocked){this.blocked=blocked;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public LocalDateTime getUpDatedAt(){return updatedAt;}
=======
   
    // public UUID getId(){return id;}
    // public void setId(UUID id){this.id=id;}
    // public String getEmail(){return email;}
    // public String getPassword(){return password;}
    // public UserRole getRole(){return role;}
    // public boolean getBlocked(){return blocked;}
    // public String getUsername(){return username;}
    // public void setRole(UserRole role){this.role=role;}
    // public void setBlocked(boolean blocked){this.blocked=blocked;}
    // public LocalDateTime getCreatedAt(){return createdAt;}
    // public LocalDateTime getUpDatedAt(){return updatedAt;}
>>>>>>> 52f4d37 (Feat:Adde user service)
}
