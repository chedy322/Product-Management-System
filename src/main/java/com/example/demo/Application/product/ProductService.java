package com.example.demo.Application.product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.Application.dto.authenticatedUser.AuthenticatedUser;
import com.example.demo.Application.product.dto.ProductRequest;
import com.example.demo.Application.product.dto.ProductResponse;
import com.example.demo.Application.product.dto.UpdateProductRequest;

import com.example.demo.Domain.Interfaces.DomainEventPublisher;

import com.example.demo.Domain.product.Entities.Product;
import com.example.demo.Domain.product.ValueObjects.Name;
import com.example.demo.Domain.product.ValueObjects.Stock;
import com.example.demo.Domain.product.interfaces.ProductRepository;
import com.example.demo.Domain.product.service.CheckProductNameUniqueness;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.shared.Error;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ProductService {
    private ProductRepository productRepository;
    private CheckProductNameUniqueness checkProductNameUniqueness;
    private DomainEventPublisher domainEventPublisher;


    public ProductService(ProductRepository productRepository,CheckProductNameUniqueness checkProductNameUniqueness,DomainEventPublisher domainEventPublisher){
        this.productRepository=productRepository;
        this.checkProductNameUniqueness=checkProductNameUniqueness;
        this.domainEventPublisher=domainEventPublisher;
    }

    // create
    @Transactional
    public Result<ProductResponse> create(ProductRequest productRequest,AuthenticatedUser authenticatedUser){
        // Create name valeu object
        Result<Name> nameResult=Name.create(productRequest.name()); 
        if(nameResult.isFailure()){
            return Result.Failure(nameResult.getError());
        }
        Name name=nameResult.getValue();

        // check the product name uniqueness first
        Result<Boolean> productNameisUnique=checkProductNameUniqueness.productNameIsUnique(name);
        if(productNameisUnique.isFailure()){
            return Result.Failure(productNameisUnique.getError());
        }

        // Create stock value object
        Result<Stock> stockResult=Stock.create(productRequest.stock());
        if(stockResult.isFailure()){
            return Result.Failure(stockResult.getError());
        }
        Stock stock=stockResult.getValue();

    
        // Create product instance
        Result<Product> productResult = Product.create(
            name,
            productRequest.description(),
            productRequest.price(),
            stock,
            authenticatedUser.id()
        );
        if(productResult.isFailure()){
            return Result.Failure(productResult.getError());
        }
        Product product=productResult.getValue();

        // Save the prodct to db
        Product savedProduct= productRepository.save(product);

        // dispatch product created event 
        domainEventPublisher.dispatch(savedProduct);

        ProductResponse productResponse=ProductResponse.mapToResponse(savedProduct);
        return Result.Success(productResponse);
    }

    // delete by id onlly for admin
    // we need delete by userId and id
    @Transactional 
    public Result<Boolean> deleteById(UUID id,AuthenticatedUser authenticatedUser){
        // check if the product exists or no
        Optional<Product> productResult=productRepository.findById(id);
        if(productResult.isEmpty()){
            return Result.Failure(Error.NOT_FOUND("Product not found"));
        }
        // get user id
        UUID userId=authenticatedUser.id();
       boolean productDeleted= productRepository.deleteByIdAndUserId(id,userId);
       return Result.Success(productDeleted);
    }



    // // findall products is public route
    // public Result<List<ProductResponse>> findAll(){
    //    return Result.Success(productRepository.findAll().stream().map(ProductResponse::mapToResponse).toList());
    // }


    // findbyId
    // public Result<ProductResponse> findById(UUID productId){
    //     // check if the product exists in db
    //     Optional<Product> productResult=productRepository.findById(productId);
    //     if(productResult.isEmpty()){
    //         return Result.Failure(Error.NOT_FOUND("Product does not exists"));
    //     }
    //     System.err.println("Product is converting to dto");
    //     // DTO
    //     ProductResponse productResponse=ProductResponse.mapToResponse(productResult.get());
    //     return Result.Success(productResponse);

    // }
    // update by id by field 
    @Transactional
    public Result<ProductResponse> updateProduct(UUID productId,UpdateProductRequest request,AuthenticatedUser authenticatedUser){
        // check for the product in db
        Optional<Product> productResult=productRepository.findByIdAndUserId(productId,authenticatedUser.id());
        if(productResult.isEmpty()){
            return Result.Failure(Error.NOT_FOUND("Unable to update product."));
        }
        Product productDomain=productResult.get();
        if(request.name()!=null){
            Result<Name> productDomainName=productDomain.updateName(request.name());
            if(productDomainName.isFailure()){
                 return Result.Failure(productDomainName.getError());

            }
        }
      
        if(request.stock()!=null){
             Result<Stock> productDomainStock=productDomain.updateStock(request.stock());
            if(productDomainStock.isFailure()){
                 return Result.Failure(productDomainStock.getError());

            }
        }
   
        // save the changes to db
        Product product=productRepository.save(productResult.get());
        // DTO
        ProductResponse productResponse=ProductResponse.mapToResponse(product); 

        // send success result
        return Result.Success(productResponse);


    }










    
}
