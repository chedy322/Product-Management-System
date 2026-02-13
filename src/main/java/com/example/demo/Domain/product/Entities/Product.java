package com.example.demo.Domain.product.Entities;

import java.util.UUID;

import com.example.demo.Domain.Primitives.Entity;
import com.example.demo.Domain.Primitives.Aggregate;
import com.example.demo.Domain.product.ValueObjects.Name;
import com.example.demo.Domain.product.ValueObjects.Stock;
import com.example.demo.Domain.product.events.ProductCreated;
import com.example.demo.Domain.shared.Result;

public class Product extends Aggregate{
    // make value object for the name 
    private Name name;
    private String description;
    // make value object here
    private int price;
    private Stock stock; 
    private Product (UUID id,Name name,String description,int price,Stock stock){
        // generate random uuid for the entity
        super(id);
        this.name=name;
        this.description=description;
        this.price=price;
        this.stock=stock;
    }
     public static Result<Product> create (Name name,String description,int price,Stock stock){
        Product product=new Product(UUID.randomUUID(),name, description, price, stock);
        // register event
        product.registerEvent(new ProductCreated(product.getId(),product.getName()));
        return Result.Success(product);
    }

    // getter
    public Name getName() {return name;}
    public String getDescription(){ return description;}
    public int getPrice(){ return price;}
    public Stock getStock(){ return stock;}

    // setter
    public Result<Stock> updateStock(int amount) {
        Result<Stock> stockResult=stock.addStock(amount);
        if(stockResult.isFailure())
        {
            return Result.Failure(stockResult.getError());
        }
        this.stock=stockResult.getValue();
        return Result.Success(stock);

    }   
    




}
