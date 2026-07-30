package com.example.demo.Infrastructure.persistence.product.redis;

import java.time.Duration;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.Domain.Interfaces.TokensBlackList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisTokensBlackListed implements TokensBlackList{
    // Key in redis: key->blackList:<jti> 
    private final RedisTemplate<String,Object> redisTemplate;
    
    @Override
    public boolean getBlacksListedTokenbyId(String tokenId) {
        String key=String.format("blackList:%s", tokenId);
        try{
            // 1. Lookup for key in Redis
            Boolean existingObject=redisTemplate.hasKey(key);
            // 2. Check if the token is not blacklisted
            if(Boolean.FALSE.equals(existingObject)){
                log.info("Token {} doesn't exist in the blacklist ",key);
                return false;
            }

        }catch(Exception e){
            // Here we use fail-closed to not allow access in case Redis is down
            log.warn("Failed to find blacklisted token for key {} with error {}",key,e);
            return false;
            // return true;
        }
        return true;
    }

    @Override
    public void addtBlackListedToken(String tokenId,long remainTimeForTokenToExpire) {
        if (remainTimeForTokenToExpire <= 0) {
        log.warn("Attempted to blacklist an expired token: {}", tokenId);
        return;
    }
        String key=String.format("blackList:%s",tokenId);
        try{
            redisTemplate.opsForValue().set(key, "True",Duration.ofMillis(remainTimeForTokenToExpire));
            log.debug("Token with key {} added to blackList successfully",key);
        }catch(Exception err){
             log.warn("Failed to add blacklisted token for key {} with error {}",key,err);
            //  Throw global exception to let user try again and not let open-closed
        }
        
    }
}
