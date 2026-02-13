package com.example.demo.Application.product.dto;

import com.example.demo.Domain.Entities.Product;

public record ProductResponse(
    String name,
    String description,
    int price,
    int stock
){
    public static ProductResponse mapToResponse(Product product){
        return new ProductResponse(
            product.getName().getValue(),
            product.getDescription(),
            product.getPrice(),
            product.getStock().getValue()
        );
    }
}
