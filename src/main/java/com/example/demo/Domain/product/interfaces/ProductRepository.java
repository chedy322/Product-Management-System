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
    Optional<Product> findByName(Name name);
    List<Product> findAll();

    // Delete
    boolean deleteById(UUID id);
}
