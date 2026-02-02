package com.example.demo.Infrastructure.Web;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

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
import com.example.demo.Domain.shared.Result;





@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;
    public ProductController(ProductService productService){
        this.productService=productService;
    }

    @GetMapping("/")
    public List<ProductResponse> getProducts() {
        // Implementation for creating a product
        Result<List<ProductResponse>> productsResult= productService.findAll();
        if(productsResult.isFailure()){
            throw new RuntimeException("Could not fetch products: " + productsResult.getError());
        }

        // Convert the result to List<ProductResponse>
        List<ProductResponse> productsEntities=productsResult.getValue();
        return productsEntities;
        
    }   
    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable("id") UUID productId) {
        Result<ProductResponse> productResult = productService.findById(productId);
        if(productResult.isFailure()){
            throw new RuntimeException("Product not found: " + productResult.getError());
        }
        ProductResponse product = productResult.getValue();
        return product;
    }

    @PostMapping("/")
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest entity) {
        Result<ProductResponse> createdProduct = productService.create(entity);
        if(createdProduct.isFailure()){
            throw new RuntimeException("Product not created: " + createdProduct.getError());
        }
        return createdProduct.getValue();
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") UUID productId) {
        Result<Boolean> productResult=productService.deleteById(productId);
        if(productResult.isFailure()){
            throw new RuntimeException("Product not deleted: " + productResult.getError());
        }
        return "Product deleted successfully";
    }

    

     

}
