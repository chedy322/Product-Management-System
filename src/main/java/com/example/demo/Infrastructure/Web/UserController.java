package com.example.demo.Infrastructure.Web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import com.example.demo.Application.product.userService;
import com.example.demo.Application.product.dto.ProductRequest;
// import com.example.demo.Application.product.dto.UserResponse;
import com.example.demo.Application.product.dto.UpdateProductRequest;
import com.example.demo.Application.user.UserService;
import com.example.demo.Application.user.dto.UserRequest;
import com.example.demo.Application.user.dto.UserResponse;
import com.example.demo.Domain.exceptions.DomainExceptions;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.shared.Error;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
 private UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }

    @GetMapping("")
    public ResponseEntity<?> getProducts() {
        // Implementation for creating a product
        Result<List<UserResponse>> usersResult= userService.findAll();
        if(usersResult.isFailure()){
            // throw new RuntimeException("Could not fetch products: " + productsResult.getError());
            Error error=usersResult.getError();
            // return ResponseEntity.status(error.httpStatus()).body(error.errorMsg());
            throw new DomainExceptions(error);
        }

        // Convert the result to List<UserResponse>
        List<UserResponse> userssEntities=usersResult.getValue();
        return ResponseEntity.ok().body(userssEntities);
        
    }   
    // @GetMapping("/{id}")
    // public ResponseEntity<?> getProduct(@PathVariable("id") UUID productId) {
    //     // Result<UserResponse> productResult = userService.findById(productId);
    //     if(productResult.isFailure()){
    //         // throw new RuntimeException("Product not found: " + productResult.getError());
    //         Error error=productResult.getError();
    //         // return ResponseEntity.status(error.httpStatus()).body(error.errorMsg());
    //         throw new DomainExceptions(error);
    //     }
    //     // UserResponse product = productResult.getValue();
    //     return ResponseEntity.ok().body(product);
    // }

    @PostMapping("")
    public ResponseEntity<?> createProduct(@Valid @RequestBody UserRequest entity) {
        Result<UserResponse> createdUser = userService.create(entity);
        if(createdUser.isFailure()){
            Error error=createdUser.getError();
            // return ResponseEntity.status(error.httpStatus()).body(error.errorMsg());
            throw new DomainExceptions(error);
        }
        return ResponseEntity.status(201).body(createdUser.getValue());
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<?> deleteProduct(@PathVariable("id") UUID productId) {
    //     Result<Boolean> productResult=userService.deleteById(productId);
    //     if(productResult.isFailure()){
    //         // throw new RuntimeException("Product not deleted: " + productResult.getError());
    //         Error error=productResult.getError();
    //         // return ResponseEntity.status(error.httpStatus()).body(error.errorMsg());
    //         throw new DomainExceptions(error);
    //     }
    //     return ResponseEntity.status(201).body("Product deleted successfully");
    // }

    // @PutMapping("/{id}")
    // public ResponseEntity<?> updateProduct(@PathVariable("id") UUID productId,@RequestBody(required = false) UpdateProductRequest productRequest) {
    //         // Result<UserResponse> productUpdateResult=userService.updateProduct(productId, productRequest);
    //         if(productUpdateResult.isFailure()){
    //             Error error=productUpdateResult.getError();
    //              throw new DomainExceptions(error);
    //         }
    //         return ResponseEntity.status(200).body(productUpdateResult.getValue());
    // }
    

    

}
