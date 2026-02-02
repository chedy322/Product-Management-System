package com.example.demo.Domain.ValueObjects;

import java.util.List;

import com.example.demo.Domain.Primitives.ValueObject;
import com.example.demo.Domain.shared.Result;

public final class Stock extends ValueObject{
    private final int value;

    private Stock(int value){
        this.value=value;
    }
    
    @Override
    protected List<Object> getAtomicValues() {
        return List.of(value);
    }

    public int getStock(){ return value;}

    public static Result<Stock> create(int stockValue){
        if(stockValue<0){
            return Result.Failure("Stock cannot be Negative");
        }
        Stock stock=new Stock(stockValue);
        return Result.Success(stock);

    }

    public Result<Stock> addStock(int amount){
        if(this.value+amount<0){
            return Result.Failure("Insufficient stock");
        }
        Stock stock=new Stock(this.value+amount);
        return Result.Success(stock);
    }

}
