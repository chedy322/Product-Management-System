package com.example.demo.Domain.Interfaces;

public interface RateLimiter {
    public boolean isAllowed(String userId);
}
