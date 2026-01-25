package com.example.demo.Application;

import org.springframework.stereotype.Service;

import main.java.com.example.demo.Domain.Interfaces.ProductRepository;


@Service
public class ProductService {
    private ProductRepository ProductRepository;

    public ProductService(ProductRepository){
        this.ProductRepository=ProductRepository;
    }

    // create
    

    // delete


    // findall

    // findbyId

    
}
