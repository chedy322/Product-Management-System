package com.example.demo.Infrastructure.persistence.product;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Data
@Table(name="products")
public class ProductEntity {



    public ProductEntity(){}
    public ProductEntity(String name,Integer price,Integer stock,String description){
        this.name=name;
        this.price=price;
        this.stock=stock;
        this.description=description;
    }
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",updatable = false,nullable = false)
    private UUID id;

    @NotBlank(message = "Product name is required")
    @Column(length = 100,unique = true,nullable = false)
    private String name;

    @NotNull(message = "Product price is required")
    @PositiveOrZero(message = "Product price cannot be negative")
    private Integer price;

    @Min(value = 0,message = "Product stock cannot be less than zero")
    private Integer stock;

    @NotBlank(message = "Product description cannot be empty")
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;





}
