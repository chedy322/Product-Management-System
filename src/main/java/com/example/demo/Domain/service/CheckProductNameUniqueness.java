package com.example.demo.Domain.service;

import java.util.Optional;

import com.example.demo.Domain.Entities.Product;
import com.example.demo.Domain.Interfaces.ProductRepository;
import com.example.demo.Domain.ValueObjects.Name;
import com.example.demo.Domain.shared.Result;

public class CheckProductNameUniqueness {
    private final ProductRepository productRepository;
    public CheckProductNameUniqueness(ProductRepository ProductRepository){
        this.productRepository=ProductRepository;
    }
    public Result<Boolean> productNameIsUnique(Name name){
        Optional <Product> product=productRepository.findByName(name);
        if(!product.isEmpty()){
            return Result.Failure("Product name already exists");
        }
        return Result.Success(true);
    }
}
