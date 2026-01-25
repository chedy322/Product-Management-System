package com.example.demo.Domain.Entities;

import java.util.UUID;

import com.example.demo.Domain.Primitives.Entity;
import com.example.demo.Domain.ValueObjects.Name;

public class Product extends Entity{
    // make value object for the name 
    private Name name;
    private String description;
    // make value object here
    private int price;
    private int stock;

    private Product (UUID id,Name name,String description,int price,int stock){
        // generate random uuid for the entity
        super(id);
        this.name=name;
        this.description=description;
        this.price=price;
        this.stock=stock;
    }
     public static Product create (Name name,String description,int price,int stock){
        if(stock<=0){
            throw new IllegalArgumentException("Stock cannot be zero");
        }
        Product product=new Product(UUID.randomUUID(),name, description, price, stock);
        return product;
    }

    // getter
    public String getName() {return name.getValue();}
    public String getDescription(){ return description;}
    public int getPrice(){ return price;}
    public int getStock(){ return stock;}
    // setter
    public void updateStock(int amount) {
        if (this.stock + amount < 0) throw new IllegalStateException("Insufficient stock");
        this.stock += amount;
    }   
    




}
