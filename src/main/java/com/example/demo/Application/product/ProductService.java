package com.example.demo.Application.product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.jar.Attributes;

import org.springframework.stereotype.Service;

import com.example.demo.Application.product.dto.ProductRequest;
import com.example.demo.Application.product.dto.ProductResponse;
import com.example.demo.Application.product.dto.UpdateProductRequest;
import com.example.demo.Domain.Entities.Product;
import com.example.demo.Domain.Interfaces.ProductRepository;
import com.example.demo.Domain.ValueObjects.Name;
import com.example.demo.Domain.ValueObjects.Stock;
import com.example.demo.Domain.service.CheckProductNameUniqueness;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.shared.Error;


@Service
public class ProductService {
    private ProductRepository productRepository;
    private CheckProductNameUniqueness checkProductNameUniqueness;

    public ProductService(ProductRepository productRepository,CheckProductNameUniqueness CheckProductNameUniqueness ){
        this.productRepository=productRepository;
        this.checkProductNameUniqueness=CheckProductNameUniqueness;
    }

    // create
    public Result<ProductResponse> create(ProductRequest productRequest){
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
            stock
        );
        if(productResult.isFailure()){
            return Result.Failure(productResult.getError());
        }
        Product product=productResult.getValue();

        // Save the prodct to db
        Product savedProduct= productRepository.save(product);

        ProductResponse productResponse=ProductResponse.mapToResponse(savedProduct);
        return Result.Success(productResponse);
    }

    // delete
    public Result<Boolean> deleteById(UUID id){
        // check if the product exists or no
        Optional<Product> productResult=productRepository.findById(id);
        if(productResult.isEmpty()){
            return Result.Failure(Error.NOT_FOUND("Product not found"));
        }
       boolean productDeleted= productRepository.deleteById(id);
    //    if(!productDeleted){
    //      return Result.Failure("Product was not deleted");
    //    }
       return Result.Success(true);
    }



    // findall
    public Result<List<ProductResponse>> findAll(){
       return Result.Success(productRepository.findAll().stream().map(ProductResponse::mapToResponse).toList());
    }

    // findbyId
    public Result<ProductResponse> findById(UUID productId){
        // check if the product exists in db
        Optional<Product> productResult=productRepository.findById(productId);
        if(productResult.isEmpty()){
            return Result.Failure(Error.NOT_FOUND("Product does not exists"));
        }

        // DTO
        ProductResponse productResponse=ProductResponse.mapToResponse(productResult.get());
        return Result.Success(productResponse);

    }
    // update by id by field 
    public Result<ProductResponse> updateProduct(UUID productId,UpdateProductRequest request){
        // check for the product in db
        Optional<Product> productResult=productRepository.findById(productId);
        if(productResult.isEmpty()){
            // return Result.Failure("Product does not exist");
            return Result.Failure(Error.NOT_FOUND("Product does not exist"));
        }
        if(request.name()!=null){
            
        }
        if(request.name()!=null){

        }
        if(request.name()!=null){

        }

        // save the changes to db
        Product product=productRepository.save(productResult.get());
        // DTO
        ProductResponse productResponse=ProductResponse.mapToResponse(product); 

        // send success result
        return Result.Success(productResponse);


    }










    
}
