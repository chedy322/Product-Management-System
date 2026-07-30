package com.example.demo.Application.product.dto;

import jakarta.validation.constraints.Null;

public record UpdateProductRequest(
    String name,
    Integer price,
    Integer stock
) {
}
