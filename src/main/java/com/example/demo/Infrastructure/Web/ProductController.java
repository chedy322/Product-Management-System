package com.example.demo.Infrastructure.Web;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Application.dto.authenticatedUser.AuthenticatedUser;
import com.example.demo.Application.product.ProductService;
import com.example.demo.Application.product.dto.ProductRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.Application.product.dto.ProductResponse;
import com.example.demo.Application.product.dto.UpdateProductRequest;
import com.example.demo.Application.queries.get_all_products.GetAllProductsDTO;
import com.example.demo.Application.queries.get_all_products.GetAllProductsHandler;
import com.example.demo.Application.queries.get_product_by_id.GetProductByIdDTO;
import com.example.demo.Application.queries.get_product_by_id.GetProductByIdHandler;
import com.example.demo.Application.queries.get_user_products.GetUserProductsDTO;
import com.example.demo.Application.queries.get_user_products.GetUserProductsHandler;
import com.example.demo.Domain.exceptions.DomainExceptions;
import com.example.demo.Domain.product.Entities.Product;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Infrastructure.Web.helper.ApiController;
import com.example.demo.Infrastructure.security.jwt.CustomUserDetails;
import com.example.demo.Domain.shared.Error;





@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController extends ApiController{
    private final ProductService productService;
    private final GetUserProductsHandler getUserProductsService;
    private final GetAllProductsHandler getAllProductsHandler;
    private final GetProductByIdHandler getProductByIdHandler;
    @GetMapping("")
    public ResponseEntity<?> getProducts() {
        // Implementation for creating a product
        Result<List<GetAllProductsDTO>> productsResult= getAllProductsHandler.handle();
        if(productsResult.isFailure()){
            // throw new RuntimeException("Could not fetch products: " + productsResult.getError());
            Error error=productsResult.getError();
            // return ResponseEntity.status(error.httpStatus()).body(error.errorMsg());
            throw new DomainExceptions(error);
        }
        return ResponseEntity.ok().body(productsResult.getValue());

    }   
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") UUID productId) {
        Result<GetProductByIdDTO> productResult = getProductByIdHandler.handle(productId);
        if(productResult.isFailure()){
            // throw new RuntimeException("Product not found: " + productResult.getError());
            Error error=productResult.getError();
            // return ResponseEntity.status(error.httpStatus()).body(error.errorMsg());
            throw new DomainExceptions(error);
        }
        return ResponseEntity.ok().body(productResult.getValue());
    }

    @PostMapping("")
    public ResponseEntity<?> createProduct(@AuthenticationPrincipal CustomUserDetails user,@Valid @RequestBody ProductRequest entity) {
        // map the current user to authenticatedUser 
        AuthenticatedUser authenticatedUser=AuthenticatedUser.map(user.getUserId(), user.getUserEmail(), user.getUsername());

        Result<ProductResponse> createdProduct = productService.create(entity,authenticatedUser);
        if(createdProduct.isFailure()){
            Error error=createdProduct.getError();
            // return ResponseEntity.status(error.httpStatus()).body(error.errorMsg());
            throw new DomainExceptions(error);
        }
        return ResponseEntity.status(201).body(createdProduct.getValue());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@AuthenticationPrincipal CustomUserDetails user,@PathVariable("id") UUID productId) {
        AuthenticatedUser authenticatedUser=AuthenticatedUser.map(user.getUserId(), user.getUserEmail(), user.getUsername());
        Result<Boolean> productResult=productService.deleteById(productId,authenticatedUser);
        if(productResult.isFailure()){
            // throw new RuntimeException("Product not deleted: " + productResult.getError());
            Error error=productResult.getError();
            // return ResponseEntity.status(error.httpStatus()).body(error.errorMsg());
            throw new DomainExceptions(error);
        }
        return ResponseEntity.status(201).body("Product deleted successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProduct(@AuthenticationPrincipal CustomUserDetails user,@PathVariable("id") UUID productId,@RequestBody(required = false) UpdateProductRequest productRequest) {
                AuthenticatedUser authenticatedUser=AuthenticatedUser.map(user.getUserId(), user.getUserEmail(), user.getUsername());
            Result<ProductResponse> productUpdateResult=productService.updateProduct(productId, productRequest,authenticatedUser);
            if(productUpdateResult.isFailure()){
                Error error=productUpdateResult.getError();
                 throw new DomainExceptions(error);
            }
            return ResponseEntity.status(200).body(productUpdateResult.getValue());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserProducts(@AuthenticationPrincipal CustomUserDetails user) {
         AuthenticatedUser authenticatedUser=AuthenticatedUser.map(user.getUserId(), user.getUserEmail(), user.getUsername());
        Result<List<GetUserProductsDTO>> productResult=getUserProductsService.handle(authenticatedUser);
        return handleResult(productResult);
    }
    
    

    

     

}
