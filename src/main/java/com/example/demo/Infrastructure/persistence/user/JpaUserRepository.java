package com.example.demo.Infrastructure.persistence.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Domain.user.entities.User;

public interface JpaUserRepository extends JpaRepository<UserEntity,UUID>{
    Optional<UserEntity> findByEmail(String email);
}
