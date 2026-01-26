package com.example.demo.Domain.Interfaces;

import com.example.demo.Domain.Entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
   // Create & Update
    void save(User user);

    // Read
    Optional<User> findById(UUID id); 
    boolean findByEmail(String email);
    List<User> findAll();

    // Delete
    void deleteById(UUID id);
}
