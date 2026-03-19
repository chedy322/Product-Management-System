package com.example.demo.Domain.Interfaces;

public interface JwtFilter {
    public String generateToken(String email);
    // public Boolean isTokenValid();
}
