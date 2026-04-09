package com.example.demo.Infrastructure.persistence.product;

// import com.example.demo.Domain.product.Entities.Product;
import com.example.demo.Infrastructure.persistence.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository  extends JpaRepository<ProductEntity,UUID>{
    Optional<ProductEntity> findByName(String name);
    void deleteByIdAndUserId(UUID id,UUID userId);
    Optional<ProductEntity> findByUserId(UUID userId);
    boolean existsByIdAndUserId(UUID id,UUID userId);
    Optional<ProductEntity> findByIdAndUserId(UUID id ,UUID userId);
    
}