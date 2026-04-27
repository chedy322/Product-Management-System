package com.example.demo.Application.queries.get_product_by_id;

import java.time.LocalDateTime;
import java.util.UUID;
public record GetProductByIdDTO(
    UUID id,
    Integer price,
    String name,
    String description,
    String ownership,
    LocalDateTime createdAt,
    LocalDateTime updatedAt

) {
}

