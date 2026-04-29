package com.example.demo.Infrastructure.external.redis;

import com.example.demo.Domain.product.service.CheckProductNameUniqueness;
import com.example.demo.Infrastructure.external.messages.EventsDispatcher;

import jakarta.validation.constraints.Min;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.Domain.Interfaces.RateLimiter;
// ISSUE IN THIS FLOW CONCURRENRY
@Component
public class RedisTokenBucketRateLimiter implements RateLimiter {
    private static final long MAX_TOKENS=5;
    private static final long REFILL_INTERVAL=60000;
    private static final long REFILL_TOKENS=2;
   private final StringRedisTemplate redisTemplate;

    public RedisTokenBucketRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isAllowed(String userId) {
        String key="redis_limit:"+userId;
        long now=System.currentTimeMillis();
        try{
            // Check the redis if the bukect for user already exists or create new bucket 
            TokenBucket tokenBucket=getOrCreateBucket(key, now);
            if(tokenBucket.tokensAvailable>=1){
                tokenBucket.tokensAvailable--;
                // save  to redis 
                saveToRedis(key,tokenBucket);
                return true;
            }
            // not allowed
            return false;
        }catch(Exception e){
            return true;
        }
     
    }

    private TokenBucket getOrCreateBucket(String key,long time){
        // look for the bueckt in redis 
        String storedBucket=redisTemplate.opsForValue().get(key);
        
        // if it doesnt exists create new one 
        if(storedBucket==null){
            // create new bucket 
            TokenBucket newBucket = new TokenBucket();
            newBucket.lastRefillTime=time;
            newBucket.tokensAvailable=MAX_TOKENS;
            return newBucket;

        }

        // if it exists return the data in form of bucket 
        // Parse the String 
        String[] parts= storedBucket.split(":");
        Long tokensAvailable=Long.parseLong(parts[0]);
        Long lastRefillTime=Long.parseLong(parts[1]);

        // Make the checks 
        // Check how long has been since the last time the request was make 
        Long ellipseMs=time-lastRefillTime;
        Long tokensToAdd = (ellipseMs*REFILL_TOKENS)/REFILL_INTERVAL;
        // Min between the already available token and the max tokens 
        long newTokens=Math.min(MAX_TOKENS,tokensToAdd+tokensAvailable);

        TokenBucket bucket = new TokenBucket();
        bucket.lastRefillTime=time;
        bucket.tokensAvailable= newTokens;
        return bucket;
    }
    private void saveToRedis(String key,TokenBucket bucket){
         String value = bucket.tokensAvailable + ":" + bucket.lastRefillTime;
        // Expire after 2 hours of inactivity (prevents stale entries)
        redisTemplate.opsForValue().set(key, value, 2, TimeUnit.HOURS);
    }
    static class TokenBucket {
        long tokensAvailable;
        long lastRefillTime;
    }
}
