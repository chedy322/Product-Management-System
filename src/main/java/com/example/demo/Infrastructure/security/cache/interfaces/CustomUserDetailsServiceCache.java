package com.example.demo.Infrastructure.security.cache.interfaces;

import java.util.Optional;
import java.util.UUID;

import com.example.demo.Infrastructure.security.cache.dto.CachedCustomUserDetails;


public interface CustomUserDetailsServiceCache {
    public void storeUserDetails(CachedCustomUserDetails cachedCustomUserDetails);
    public Optional<CachedCustomUserDetails> getUserDetailsById(UUID userId);
}
