package com.example.demo.Application.queries.get_all_products;

import java.util.UUID;
public record GetAllProductsDTO(
    UUID id,
    String name,
    Integer price,
    Integer stock,
    String ownership
) {
}

