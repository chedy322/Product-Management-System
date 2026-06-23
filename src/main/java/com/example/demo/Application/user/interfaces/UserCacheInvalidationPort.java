package com.example.demo.Application.user.interfaces;

import java.util.UUID;

/**
 * Port/Interface for cache invalidation.
 * Defines contract for Application layer.
 * 
 * Implementation can be Redis, Memcached, or any caching strategy.
 * Listeners depend on this interface, not concrete implementations.
 */
public interface UserCacheInvalidationPort {
    
    /**
     * Invalidate user cache when profile is updated.
     * Called when: username, email, profile info changes
     */
    void invalidateUserProfile(UUID userId);
    
    /**
     * Evict user cache when profile is deleted.
     * Called when: user account is deleted
     */
    void evictUserProfile(UUID userId);
    
    /**
     * Invalidate when user role/permissions change.
     * Called when: admin changes user role or permissions
     */
    void invalidateUserAuthorization(UUID userId);
    
    /**
     * Invalidate when user password changes.
     * Called when: user changes password for security
     */
    void invalidateUserPassword(UUID userId);
}