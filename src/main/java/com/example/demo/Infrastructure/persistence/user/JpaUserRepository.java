package com.example.demo.Infrastructure.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository  extends JpaRepository<UserEntity,Long>{
    
}
