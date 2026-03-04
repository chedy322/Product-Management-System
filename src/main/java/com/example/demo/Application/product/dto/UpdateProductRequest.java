package com.example.demo.Application.product.dto;

public record UpdateProductRequest(
    String name,
    Integer price,
    Integer stock
) {
}
