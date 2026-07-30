package com.example.demo.Application.queries.get_user_products;

import java.util.UUID;

public record GetUserProductsDTO(
    UUID  id, 
    String name, 
    String ownerName, 
    Integer price
) {
} 