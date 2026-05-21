package com.example.demo.Infrastructure.persistence.product.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import com.example.demo.Domain.product.interfaces.ProductCacheInvalidationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisProductCacheInvalidationService implements ProductCacheInvalidationService{
    private final RedisTemplate<String,Object> redisTemplate;

    @Override
    public void evictAllProducts() {
        // SCAN all the fields in the cache with products:list:* and delete them
        String pattern="products:list:*";
        List<String> keys=new ArrayList<>();
        try(Cursor<String> cursor=redisTemplate.scan(ScanOptions.scanOptions().match(pattern).count(100).build())){
            while(cursor.hasNext()){
                String key=cursor.next();
                // redisTemplate.delete(key);
                // add the key to the array 
                keys.add(key);
                // check if the keys hit batch limit to delete 100
                if(keys.size()>=100){
                    // bacth delete
                    redisTemplate.delete(keys);
                    log.debug("Evicted all keys from the array list.");
                    // clear the keys after deleting all from the cache
                    keys.clear();
                }
            }
            // Run cleanup in case still in the keys 
            if(!keys.isEmpty()){
                redisTemplate.delete(keys);
                log.debug("Evicted remaining keys from the array list");
            }

        }
        // In case failure add retry logic later not now
        catch (Exception e){
            log.error("Error during batch list cache invalidation", e);
        }
    }

    @Override
    public void evictProduct(UUID productId) {
        // 1. Look for the exact product with thge key with products:{id}:v1
        String key=String.format("products:%s:v1", productId);
        try{
            redisTemplate.delete(key);
             log.debug("Evicted product cache key: {}", key);
        }catch(Exception e){
            log.error("Failed to evict cache key: {}", key, e);
        }
        
    }
}
