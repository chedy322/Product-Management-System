package com.example.demo.Application.product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.Application.dto.authenticatedUser.AuthenticatedUser;
import com.example.demo.Application.product.dto.ProductRequest;
import com.example.demo.Application.product.dto.ProductResponse;
import com.example.demo.Application.product.dto.UpdateProductRequest;
import com.example.demo.Domain.Interfaces.AppLogger;
import com.example.demo.Domain.Interfaces.DomainEventPublisher;

import com.example.demo.Domain.product.Entities.Product;
import com.example.demo.Domain.product.ValueObjects.Name;
import com.example.demo.Domain.product.ValueObjects.Stock;
import com.example.demo.Domain.product.interfaces.ProductRepository;
import com.example.demo.Domain.product.service.CheckProductNameUniqueness;
import com.example.demo.Domain.shared.Result;

import io.micrometer.core.instrument.MeterRegistry;

import com.example.demo.Domain.shared.Error;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CheckProductNameUniqueness checkProductNameUniqueness;
    private final DomainEventPublisher domainEventPublisher;
    private final MeterRegistry meterRegistry;
    // create
    @Transactional
    public Result<ProductResponse> create(ProductRequest productRequest,AuthenticatedUser authenticatedUser){
        return meterRegistry.timer("product.create.time").record(()->{

        log.info("Atemptting to create a product with name:{} by user:{} ",productRequest.name(),authenticatedUser.username());
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
        log.info("Product saved succesfully in database with id:{} for user:{} ",savedProduct.getId(),authenticatedUser.username());
        // dispatch product created event 
        domainEventPublisher.dispatch(savedProduct);
        meterRegistry.counter("product.created.total", 
        "type", "physical",
        "status","success").increment();
        ProductResponse productResponse=ProductResponse.mapToResponse(savedProduct);
        return Result.Success(productResponse);
        });
       
    }

    // delete by id onlly for admin
    // we need delete by userId and id
    @Transactional 
    public Result<Boolean> deleteById(UUID id,AuthenticatedUser authenticatedUser){
        return meterRegistry.timer("product.delete.time").record(()->{

        log.info("User:{} attempting to delete product with id:{}",authenticatedUser.username(),id);
        // check if the product exists or no
        Optional<Product> productResult=productRepository.findById(id);
        if(productResult.isEmpty()){
            return Result.Failure(Error.NOT_FOUND("Product not found"));
        }
        
        Product product=productResult.get();
        // Prevent BOLA: Explicit ownership authorization check
        if(!product.getOwnerId().equals(authenticatedUser.id())){
            return Result.Failure(Error.UNAUTHORIZED("You do not have permission to delete this product"));
        }
        
        boolean productDeleted= productRepository.deleteById(id);
        product.deleteProduct(id);
        // if success dispatch the event 
        if (productDeleted){
            domainEventPublisher.dispatch(product);
        }
        log.info("Product with id :{} for user :{} is deleted successfully from database",id,authenticatedUser.username());
        meterRegistry.counter("product.deleted", 
            "type", "physical",
            "status","success"
        ).increment();
       return Result.Success(productDeleted);
        });
    
    }


    // update by id by field 
    @Transactional
    public Result<ProductResponse> updateProduct(UUID productId,UpdateProductRequest request,AuthenticatedUser authenticatedUser){
        return meterRegistry.timer("product.update.time").record(()->{

        log.info("User:{} attempting to update product with id:{}",authenticatedUser.username(),authenticatedUser.id());
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
        // dispatch event for product changes
        domainEventPublisher.dispatch(product);
        log.info("Product with id :{} for user :{} is updated succesfully from database",authenticatedUser.id(),authenticatedUser.username());
        meterRegistry.counter("product.update",
        "type", "physical",
        "status","success").increment();
    
        // DTO
        ProductResponse productResponse=ProductResponse.mapToResponse(product); 

        // send success result
        return Result.Success(productResponse);
        });


    }

    
}
