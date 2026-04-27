package com.example.demo.Infrastructure.persistence.product;


import com.example.demo.Infrastructure.Web.AdminController;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.demo.Application.queries.ProductQueryService;
import com.example.demo.Application.queries.get_all_products.GetAllProductsDTO;

import com.example.demo.Application.queries.get_product_by_id.GetProductByIdDTO;
import com.example.demo.Application.queries.get_user_products.GetUserProductsDTO;
import com.example.demo.Application.queries.get_user_products.GetUserProductsService;

import com.example.demo.Domain.product.Entities.Product;
import com.example.demo.Domain.product.ValueObjects.Name;
import com.example.demo.Domain.product.ValueObjects.Stock;
import com.example.demo.Domain.product.interfaces.ProductRepository;
import com.example.demo.Infrastructure.persistence.user.JpaUserRepository;
import com.example.demo.Infrastructure.persistence.user.UserEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductRepository,GetUserProductsService,ProductQueryService {
    private final JpaProductRepository jpaProductRepository;
    private final JpaUserRepository jpaUserRepository;

    
    
    @Override
    public Product save(Product product) {
        // get the userRef
        UserEntity userRef=jpaUserRepository.getReferenceById(product.getOwnerId());
        ProductEntity productEntity=ProductMapper.toProductEntity(product,userRef);

        ProductEntity savedEntity=jpaProductRepository.save(productEntity);

        return Product.reconstruct(
        savedEntity.getId(),
        Name.create(savedEntity.getName()).getValue(),
        savedEntity.getDescription(),
        savedEntity.getPrice(),
        Stock.create(savedEntity.getStock()).getValue(),
        savedEntity.getUser().getId()
    ).getValue();
    }

    // Read
      @Override
    public List<GetAllProductsDTO> getProductsView() {
         List<GetAllProductsDTO> products=jpaProductRepository.findAllProductsSummary();
        return products;
    }
    
    @Override
    public Optional<GetProductByIdDTO> getProductViewById(UUID id) {
        Optional<GetProductByIdDTO> product=jpaProductRepository.findProductById(id);
        return product;
    }
    
    @Override
    public Optional<Product> findById(UUID id) {
        Optional<ProductEntity> productEntity=jpaProductRepository.findById(id);
        // map the prooduct Entity to product domadminController
        Optional<Product> product=productEntity.map(ProductMapper::toDomain);
        return product;
    }
    @Override
    public List<Product> findByUserId(UUID userId) {
        List<ProductEntity> productsEntity=jpaProductRepository.findAll();
        List<Product> products=productsEntity.stream().map(ProductMapper::toDomain).toList();
        return products;
    }

    @Override
    public Optional<Product> findByName(Name name) {
        Optional<ProductEntity> productEntity=jpaProductRepository.findByName(name.getValue());
        Optional<Product> product=productEntity.map(ProductMapper::toDomain);
        return product; 

    }
     @Override
    public Optional<Product> findByIdAndUserId(UUID id, UUID userId) {
        Optional<ProductEntity> productEntity=jpaProductRepository.findByIdAndUserId(id, userId);
        Optional<Product> productDomainEntity=productEntity.map(ProductMapper::toDomain);
        return productDomainEntity;
    }

    @Override
    public List<GetUserProductsDTO> findProductsByUserId(UUID userId) {
        return jpaProductRepository.findAllUserProductsSummary(userId);
    }
   

    // Delete
    @Override
    public boolean deleteById(UUID id) {
        if (jpaProductRepository.existsById(id)) {
            jpaProductRepository.deleteById(id);
            return true;
        }
        return false;
    }


    @Override
    public boolean deleteByIdAndUserId(UUID Id, UUID userId) {
        if(jpaProductRepository.existsByIdAndUserId(Id, userId)){
            jpaProductRepository.deleteByIdAndUserId(Id, userId);
            return true;
        }
        return false;
    }


}