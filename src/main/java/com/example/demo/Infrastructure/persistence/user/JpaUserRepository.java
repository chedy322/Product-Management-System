package com.example.demo.Infrastructure.persistence.user;

<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository  extends JpaRepository<UserEntity,Long>{
    
=======
import java.lang.classfile.ClassFile.Option;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Domain.user.entities.User;
import java.util.List;


public interface JpaUserRepository extends JpaRepository<UserEntity,UUID>{
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByUsername(String username);
>>>>>>> 52f4d37 (Feat:Adde user service)
}
