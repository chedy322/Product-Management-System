package com.example.demo.Infrastructure.persistence.product.redis;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Component;

import com.example.demo.Application.queries.ProductQueryService;
import com.example.demo.Application.queries.get_all_products.GetAllProductsDTO;
import com.example.demo.Application.queries.get_product_by_id.GetProductByIdDTO;
import com.example.demo.Application.queries.get_user_products.GetUserProductsDTO;
import com.example.demo.Application.queries.get_user_products.GetUserProductsService;
import com.example.demo.Infrastructure.persistence.product.ProductPersistenceAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Primary
@RequiredArgsConstructor
public class RedisProductQueryServiceCache implements ProductQueryService,GetUserProductsService{
    private final ProductPersistenceAdapter productPersistenceAdapter;
    private final RedisTemplate<String,Object> redisTemplate;


    @Override
    public Optional<GetProductByIdDTO> getProductViewById(UUID id) {
        // Redis: key->products:{id}:v1 and value 
        // 1. Create the key for the product  
        String key=String.format("products:%s:v1", id);
        // 2. Check the redis cache 
        try{
            GetProductByIdDTO cache=(GetProductByIdDTO) redisTemplate.opsForValue().get(key);
            if(cache!=null){
                return Optional.of(cache);
            }
        }catch(Exception e){
            log.warn("Redis is down, falling back to SQL: {}",e.getMessage());
        }
        // 3. find the data in the sql db 
        Optional<GetProductByIdDTO> productDataById=productPersistenceAdapter.getProductViewById(id);
        // 3. Store the data in the cache if present
        productDataById.ifPresent(dto->{
            try{ 
                redisTemplate.opsForValue().set(key, dto,Duration.ofMinutes(10));
                 log.debug("Saved list to Redis key={} TTL=10m", key);
            }catch(Exception e){
                log.warn("Failed to save key={} to Redis", key,e.getMessage());
            }
        });
        return productDataById;
    } 
     


    @Override
    public List<GetAllProductsDTO> getProductsView(int offset, int limit) {
        // Redis Key->products:list:page=1:limit=5:v1  value 
        // 1. Prepare the key 
        String key= String.format("products:list:page%d:limit%d:v1",offset,limit);
        // 2. Check the cache 
        try{
            List<GetAllProductsDTO> cacheResult=(List<GetAllProductsDTO>) redisTemplate.opsForValue().get(key);
            if(cacheResult!=null){
                log.debug("Cache hit for key:{}",key);
                return cacheResult;
            }
            log.debug("Cache Miss for key={}",key);
        }catch(Exception e){
            log.warn("Failed to save to Redis: {}", e.getMessage());
        }
        // 3. Cache miss hit the db
        List<GetAllProductsDTO> productData=productPersistenceAdapter.getProductsView(offset, limit);
        // 4. Store the result in redis 
        if(!productData.isEmpty()){
            try{
                redisTemplate.opsForValue().set(key, productData,Duration.ofMinutes(10)); 
                 log.debug("Saved list to Redis key={} TTL=10m", key);
            }catch(Exception e){
                log.warn("Failed to save to Redis: {}", e.getMessage());
            }
        }
        return productData;
    
    }

    @Override
    public List<GetUserProductsDTO> findProductsByUserId(UUID userId,int offset,int limit) {
        // Redis Key->products:list:user:{userId}:page=1:limit=5:v1
        // 1. Prepare the key 
        String key= String.format("products:list:user:%s:page%d:limit%d:v1",userId,offset,limit);
        try{
            List<GetUserProductsDTO> cacheResult=(List<GetUserProductsDTO>) redisTemplate.opsForValue().get(key);
            if(cacheResult!=null){
                log.debug("Cache hit for key:{}",key);
                return cacheResult;
            }
            log.debug("Cache Miss for key={}",key);
        }catch(Exception e){
             log.warn("Failed to save to Redis: {}", e.getMessage());
        }

        // 3. Cache miss hit the db
        List<GetUserProductsDTO> productData=productPersistenceAdapter.findProductsByUserId(userId, offset, limit);
        // 4. Store the result in redis 
        if(!productData.isEmpty()){
            try{
                redisTemplate.opsForValue().set(key,productData,Duration.ofMinutes(10)); 
                log.debug("Saved list to Redis key={} TTL=10m", key);
            }catch(Exception e){
                log.warn("Failed to save to Redis: {}", e.getMessage());
            }
        }
        return productData;
    }
}
