package com.example.demo.Domain.Interfaces;

import com.example.demo.Domain.Entities.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
   // Create & Update
    Product save(Product product);

    // Read
    Optional<Product> findById(UUID id); 
    
    List<Product> findAll();

    // Delete
    boolean deleteById(UUID id);
}
