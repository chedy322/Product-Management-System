package com.example.demo.Domain.product.interfaces;

import com.example.demo.Domain.product.Entities.Product;
import com.example.demo.Domain.product.ValueObjects.Name;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
   // Create & Update
    Product save(Product product);

    // Read
    Optional<Product> findById(UUID id); 
    List<Product> findByUserId(UUID userId);
    Optional<Product> findByName(Name name);
    // Delete
    boolean deleteById(UUID id);
    // findBu userId
    boolean deleteByIdAndUserId(UUID Id,UUID userId);
    Optional<Product> findByIdAndUserId(UUID id,UUID userId);
}
