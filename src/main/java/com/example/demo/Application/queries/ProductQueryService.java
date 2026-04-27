package com.example.demo.Application.queries;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.demo.Application.queries.get_all_products.GetAllProductsDTO;
import com.example.demo.Application.queries.get_product_by_id.GetProductByIdDTO;
import com.example.demo.Domain.product.Entities.Product;
import com.example.demo.Domain.product.ValueObjects.Name;

public interface ProductQueryService {
      // Read
    Optional<GetProductByIdDTO> getProductViewById(UUID id); 
    // Optional<Product> getProductViewByName(Name name);
    List<GetAllProductsDTO> getProductsView();
}
