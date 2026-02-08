package com.example.demo.Infrastructure.Web;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Application.product.ProductService;
import com.example.demo.Application.product.dto.ProductRequest;
import com.example.demo.Domain.Entities.Product;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.Application.product.dto.ProductResponse;
import com.example.demo.Domain.exceptions.DomainExceptions;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.shared.Error;





@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;
    public ProductController(ProductService productService){
        this.productService=productService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getProducts() {
        // Implementation for creating a product
        Result<List<ProductResponse>> productsResult= productService.findAll();
        if(productsResult.isFailure()){
            // throw new RuntimeException("Could not fetch products: " + productsResult.getError());
            Error error=productsResult.getError();
            // return ResponseEntity.status(error.httpStatus()).body(error.errorMsg());
            throw new DomainExceptions(error);
        }

        // Convert the result to List<ProductResponse>
        List<ProductResponse> productsEntities=productsResult.getValue();
        return ResponseEntity.ok().body(productsEntities);
        
    }   
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") UUID productId) {
        Result<ProductResponse> productResult = productService.findById(productId);
        if(productResult.isFailure()){
            // throw new RuntimeException("Product not found: " + productResult.getError());
            Error error=productResult.getError();
            // return ResponseEntity.status(error.httpStatus()).body(error.errorMsg());
            throw new DomainExceptions(error);
        }
        ProductResponse product = productResult.getValue();
        return ResponseEntity.ok().body(product);
    }

    @PostMapping("/")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest entity) {
        Result<ProductResponse> createdProduct = productService.create(entity);
        if(createdProduct.isFailure()){
            Error error=createdProduct.getError();
            // return ResponseEntity.status(error.httpStatus()).body(error.errorMsg());
            throw new DomainExceptions(error);
        }
        return ResponseEntity.status(201).body(createdProduct.getValue());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") UUID productId) {
        Result<Boolean> productResult=productService.deleteById(productId);
        if(productResult.isFailure()){
            // throw new RuntimeException("Product not deleted: " + productResult.getError());
            Error error=productResult.getError();
            // return ResponseEntity.status(error.httpStatus()).body(error.errorMsg());
            throw new DomainExceptions(error);
        }
        return ResponseEntity.status(201).body("Product deleted successfully");
    }

    

     

}
