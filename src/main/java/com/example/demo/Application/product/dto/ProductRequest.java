package com.example.demo.Application.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;



public record ProductRequest(
@NotBlank(message = "Name is mandatory")
String name,

@NotBlank(message = "Description is mandatory")
String description,

@NotNull(message = "Price is mandatory")
@Positive(message = "Price must be positive")
int price,

@NotNull(message = "Stock is mandatory")
@Min(value = 0, message = "Stock cannot be negative")
int stock
){}
