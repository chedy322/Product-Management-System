package com.example.demo.Infrastructure.persistence.product;



import com.example.demo.Domain.exceptions.DomainExceptions;
import com.example.demo.Domain.product.Entities.Product;
import com.example.demo.Domain.product.ValueObjects.Name;
import com.example.demo.Domain.product.ValueObjects.Stock;
import com.example.demo.Domain.shared.Error;
import com.example.demo.Domain.shared.Result;

public class ProductMapper {


    public static Product toDomain(ProductEntity entity){
        // create the value objects
        Result<Name> name=Name.create(entity.getName());
        Result<Stock> stock=Stock.create(entity.getStock());

        if(name.isFailure()){
            throw new DomainExceptions(Error.CONFLICT("Data Integrity Error: Product has an invalid name"));
        }
        if(stock.isFailure()){
           throw new DomainExceptions(Error.CONFLICT("Data Integrity Error: Product has an invalid stock"));
    }
        // create the new Domain Entity
        Result<Product> productResult=Product.reconstruct(entity.getId(),name.getValue(), entity.getDescription(), entity.getPrice(), stock.getValue());
        if (productResult.isFailure()) {
             throw new DomainExceptions(Error.CONFLICT("Data Integrity Error: Could not reconstruct Product aggregate"));
        }
        return productResult.getValue();
    }



    public static ProductEntity toProductEntity(Product product){
        // create the productEntity for the db
        ProductEntity productEntity=new ProductEntity(product.getName().getValue(), 
        product.getPrice(), product.getStock().getValue(), product.getDescription());

        if(product.getId()!=null){
            productEntity.setId(product.getId());
        }

        return productEntity;
    }
}
