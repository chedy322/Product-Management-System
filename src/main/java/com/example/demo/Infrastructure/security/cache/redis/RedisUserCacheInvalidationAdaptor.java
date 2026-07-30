package com.example.demo.Infrastructure.security.cache.redis;

import java.time.Duration;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.Application.user.interfaces.UserCacheInvalidationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
@RequiredArgsConstructor
public class RedisUserCacheInvalidationAdaptor implements UserCacheInvalidationPort {

       private final RedisTemplate<String,Object> redisTemplate;
    private static final String USER_DETAILS_KEY_PREFIX = "users";
    private static final String DETAILS_SUFFIX = "details";
    private static final String VERSION = "v1";

   @Override
public void invalidateUserAuthorization(UUID userId) {
    helper(userId, "Invalidated auth cache for userId: {}", "Failed to invalidate auth for userId: {}. Error: {}");
}

@Override
public void invalidateUserPassword(UUID userId) {
    helper(userId, "Invalidated password cache for userId: {}", "Failed to invalidate password for userId: {}. Error: {}");
}

@Override
public void invalidateUserProfile(UUID userId) {
    helper(userId, "Invalidated profile cache for userId: {}", "Failed to invalidate profile for userId: {}. Error: {}");
}

@Override
public void evictUserProfile(UUID userId) {
    helper(userId, "Evicted profile cache for userId: {}", "Failed to evict profile for userId: {}. Error: {}");
}

    private void helper(UUID userId,String logDebugMsg,String logWarnMsg){
        String key=buildKUserDetailey(userId);
        try{
            redisTemplate.delete(key);
            log.debug(logDebugMsg, userId);
        }catch(Exception err){
            log.warn(logWarnMsg, userId, err.getMessage());
        }

    }
    private String buildKUserDetailey(UUID userId){
        return String.format("%s:%s:%s:%s", 
            USER_DETAILS_KEY_PREFIX, userId, DETAILS_SUFFIX, VERSION);
    }

}
