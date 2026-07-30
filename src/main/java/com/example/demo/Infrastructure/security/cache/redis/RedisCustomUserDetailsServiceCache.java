package com.example.demo.Infrastructure.security.cache.redis;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.Infrastructure.security.cache.dto.CachedCustomUserDetails;
import com.example.demo.Infrastructure.security.cache.interfaces.CustomUserDetailsServiceCache;
import com.example.demo.Infrastructure.security.jwt.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
@RequiredArgsConstructor
public class RedisCustomUserDetailsServiceCache implements CustomUserDetailsServiceCache{
    private final RedisTemplate<String,Object> redisTemplate;
    private static final Duration CACHE_TTL = Duration.ofMinutes(30);
    private static final String USER_DETAILS_KEY_PREFIX = "users";
    private static final String DETAILS_SUFFIX = "details";
    private static final String VERSION = "v1";
    @Override
    public Optional<CachedCustomUserDetails> getUserDetailsById(UUID userId) {
        String cacheKey = buildKeyById(userId);
        try {
            // 1. Try to get from cache
            CachedCustomUserDetails cached =(CachedCustomUserDetails) redisTemplate
                .opsForValue()
                .get(cacheKey);
            
            if (cached != null) {
                log.debug("Cache HIT for user: {}", userId);
                return Optional.of(cached);
            }
            
            log.debug("Cache MISS for user: {}", userId);
        } catch (Exception e) {
            log.warn("Redis is unavailable, falling back to database. Error: {}", 
                e.getMessage());
        }
        // Return null in case cache miss or Redis is down
        return null;
       
    }
    

    @Override
    public void storeUserDetails(CachedCustomUserDetails cachedCustomUserDetails) {
        // Get the userid for the key from the customUserDetails
        String cachedKey=buildKeyById(cachedCustomUserDetails.getUserId());
        try{
            redisTemplate.opsForValue().set(cachedKey, cachedCustomUserDetails,CACHE_TTL);
              log.debug("Cached user details for userId: {} (password excluded) TTL: 30m", 
                cachedCustomUserDetails.getUserId());
        }catch(Exception e){
            log.warn("Redis is unavailable,Couldn't store key {}. Error: {}",cachedKey,e.getMessage());
        }
        
    }


   private String buildKeyById(UUID userId) {
        return String.format("%s:%s:%s:%s", 
            USER_DETAILS_KEY_PREFIX, userId, DETAILS_SUFFIX, VERSION);
    }

}
