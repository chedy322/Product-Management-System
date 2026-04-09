package com.example.demo.Infrastructure.security.jwt;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService extends UserDetailsService{
    public CustomUserDetails loadUserById(UUID userId);
}
