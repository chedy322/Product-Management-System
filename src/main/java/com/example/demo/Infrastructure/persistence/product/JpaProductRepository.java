package com.example.demo.Infrastructure.persistence.product;


import com.example.demo.Application.queries.get_all_products.GetAllProductsDTO;

import com.example.demo.Application.queries.get_product_by_id.GetProductByIdDTO;
import com.example.demo.Application.queries.get_user_products.GetUserProductsDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaProductRepository  extends JpaRepository<ProductEntity,UUID>{
    Optional<ProductEntity> findByName(String name);
    void deleteByIdAndUserId(UUID id,UUID userId);
    List<ProductEntity> findByUserId(UUID userId);
    boolean existsByIdAndUserId(UUID id,UUID userId);
    Optional<ProductEntity> findByIdAndUserId(UUID id ,UUID userId);
    
    @Query("SELECT  new com.example.demo.Application.queries.get_user_products.GetUserProductsDTO(p.id, p.name, u.username, p.price)"+
        "FROM ProductEntity p JOIN p.user u "+
        "WHERE u.id = :userId "
    )
    List<GetUserProductsDTO> findAllUserProductsSummary(UUID userId);
    @Query("SELECT  new com.example.demo.Application.queries.get_all_products.GetAllProductsDTO(p.id,p.name, p.price, p.stock, u.username)"+
        "FROM ProductEntity p JOIN p.user u "
    )
    List<GetAllProductsDTO> findAllProductsSummary();
     @Query("SELECT  new com.example.demo.Application.queries.get_product_by_id.GetProductByIdDTO(p.id,p.price, p.name, p.description, u.username,p.createdAt,p.updatedAt)"+
        "FROM ProductEntity p JOIN p.user u "+
        "WHERE p.id = :id "        
    )
    Optional<GetProductByIdDTO> findProductById(UUID id);

}