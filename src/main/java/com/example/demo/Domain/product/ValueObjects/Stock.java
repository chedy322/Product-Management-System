package com.example.demo.Domain.product.ValueObjects;

import java.util.List;

import com.example.demo.Domain.Primitives.ValueObject;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.shared.Error;

public final class Stock extends ValueObject{
    private final int value;

    private Stock(int value){
        this.value=value;
    }
    
    @Override
    protected List<Object> getAtomicValues() {
        return List.of(value);
    }

    public int getValue(){ return value;}

    public static Result<Stock> create(int stockValue){
        if(stockValue<0){
            // return Result.Failure("Stock cannot be Negative");
            return Result.Failure(Error.VALIDATION_ERROR("Stock cannot be Negative"));
        }
        Stock stock=new Stock(stockValue);
        return Result.Success(stock);

    }

    public Result<Stock> addStock(int amount){
        if(this.value+amount<0){
            return Result.Failure(Error.VALIDATION_ERROR("Insufficient stock"));
        }
        Stock stock=new Stock(this.value+amount);
        return Result.Success(stock);
    }

}
