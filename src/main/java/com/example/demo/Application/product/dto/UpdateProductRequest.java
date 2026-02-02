package com.example.demo.Application.product.dto;

public record UpdateProductRequest(
    String name,
    int price,
    int stock
) {
}
