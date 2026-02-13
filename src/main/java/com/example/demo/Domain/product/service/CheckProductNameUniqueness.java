package com.example.demo.Domain.product.service;

import java.util.Optional;

import com.example.demo.Domain.product.Entities.Product;
import com.example.demo.Domain.product.ValueObjects.Name;
import com.example.demo.Domain.product.interfaces.ProductRepository;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.shared.Error;

public class CheckProductNameUniqueness {
    private final ProductRepository productRepository;
    public CheckProductNameUniqueness(ProductRepository ProductRepository){
        this.productRepository=ProductRepository;
    }
    public Result<Boolean> productNameIsUnique(Name name){
        Optional <Product> product=productRepository.findByName(name);
        if(!product.isEmpty()){
            return Result.Failure(Error.CONFLICT("Product name already exists"));
        }
        return Result.Success(true);
    }
}
