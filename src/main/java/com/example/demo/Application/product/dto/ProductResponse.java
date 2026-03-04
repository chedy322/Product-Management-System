package com.example.demo.Application.product.dto;

import java.util.UUID;

import com.example.demo.Domain.product.Entities.Product;

public record ProductResponse(
    UUID id,
    String name,
    String description,
    int price,
    int stock
){
    public static ProductResponse mapToResponse(Product product){
        return new ProductResponse(
            product.getId(),
            product.getName().getValue(),
            product.getDescription(),
            product.getPrice(),
            product.getStock().getValue()
        );
    }
}
