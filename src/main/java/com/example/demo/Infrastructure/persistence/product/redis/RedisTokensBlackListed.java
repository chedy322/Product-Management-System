package com.example.demo.Infrastructure.persistence.product.redis;

import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.Domain.Interfaces.TokensBlackList;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisTokensBlackListed implements TokensBlackList{
    // Key in redis: key->blackList:<jti> 
    private final RedisTemplate<String,Object> redisTemplate;
    
    @Override
    public boolean getBlacksListedTokenbyId(UUID tokenId) {
       
        return null;
    }

    @Override
    public void addtBlackListedToken(UUID tokenId) {
        // TODO Auto-generated method stub
        
    }
}
