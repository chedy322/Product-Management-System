package com.example.demo.Application;

import org.springframework.stereotype.Service;

import com.example.demo.Domain.Interfaces.ProductRepository;


@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository ){
        this.productRepository=productRepository;
    }

    // create
    

    // delete


    // findall

    // findbyId

    
}
