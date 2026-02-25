package com.example.demo.Domain.user.interfaces;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.demo.Domain.user.entities.User;
import com.example.demo.Infrastructure.config.Enum.UserRole;

public interface UserRepository {
    public Optional<User> findById(UUID id);
    public List<User> findAll();
    public void deleteById(UUID id);
    public User save(User user);
    public Optional<User> findByEmail(String email);
}
