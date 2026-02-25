package com.example.demo.Infrastructure.persistence.product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.demo.Domain.product.Entities.Product;
import com.example.demo.Domain.product.ValueObjects.Name;
import com.example.demo.Domain.product.ValueObjects.Stock;
import com.example.demo.Domain.product.interfaces.ProductRepository;

@Repository
public class ProductPersistenceAdapter implements ProductRepository {
    private JpaProductRepository jpaProductRepository;
    public ProductPersistenceAdapter(JpaProductRepository jpaProductRepository){
        this.jpaProductRepository=jpaProductRepository;
    }
    @Override
    public Product save(Product product) {
        ProductEntity productEntity=ProductMapper.toProductEntity(product);
        jpaProductRepository.save(productEntity);
        return Product.reconstruct(
        productEntity.getId(),
        Name.create(productEntity.getName()).getValue(),
        productEntity.getDescription(),
        productEntity.getPrice(),
        Stock.create(productEntity.getStock()).getValue()
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
}
