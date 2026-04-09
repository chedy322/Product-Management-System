package com.example.demo.Infrastructure.persistence.product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.demo.Domain.product.Entities.Product;
import com.example.demo.Domain.product.ValueObjects.Name;
import com.example.demo.Domain.product.ValueObjects.Stock;
import com.example.demo.Domain.product.interfaces.ProductRepository;
import com.example.demo.Infrastructure.persistence.user.JpaUserRepository;
import com.example.demo.Infrastructure.persistence.user.UserEntity;

@Repository
public class ProductPersistenceAdapter implements ProductRepository {
    private JpaProductRepository jpaProductRepository;
    private JpaUserRepository jpaUserRepository;
    public ProductPersistenceAdapter(JpaProductRepository jpaProductRepository,JpaUserRepository jpaUserRepository){
        this.jpaProductRepository=jpaProductRepository;
        this.jpaUserRepository=jpaUserRepository;
    }
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
    public Optional<Product> findById(UUID id) {
        Optional<ProductEntity> productEntity=jpaProductRepository.findById(id);
        Optional<Product> product=productEntity.map(ProductMapper::toDomain);
        return product;
    }
    @Override
    public Optional<Product> findByName(Name name) {
        Optional<ProductEntity> productEntity=jpaProductRepository.findByName(name.getValue());
        Optional<Product> product=productEntity.map(ProductMapper::toDomain);
        return product;
    }
    @Override
    public List<Product> findAll() {
         List<ProductEntity> productsEntity=jpaProductRepository.findAll();
        List<Product> products=productsEntity.stream().map(ProductMapper::toDomain).toList();
        return products;
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
    public Optional<Product> findByUserId(UUID userId) {
        Optional<ProductEntity> productEntity=jpaProductRepository.findByUserId(userId);
        // map the productEntity into product domain entity
        Optional<Product> productDomainEntity=productEntity.map(ProductMapper::toDomain);
        return productDomainEntity;
    }

    @Override
    public boolean deleteByIdAndUserId(UUID Id, UUID userId) {
        if(jpaProductRepository.existsByIdAndUserId(Id, userId)){
            jpaProductRepository.deleteByIdAndUserId(Id, userId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Product> findByIdAndUserId(UUID id, UUID userId) {
        Optional<ProductEntity> productEntity=jpaProductRepository.findByIdAndUserId(id, userId);
        Optional<Product> productDomainEntity=productEntity.map(ProductMapper::toDomain);
        return productDomainEntity;
    }

}