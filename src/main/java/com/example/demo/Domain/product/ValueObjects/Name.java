package com.example.demo.Domain.product.ValueObjects;

import java.util.List;

import com.example.demo.Domain.Primitives.ValueObject;
import com.example.demo.Domain.shared.Result;
import com.example.demo.Domain.shared.Error;

public class Name extends ValueObject{
    private final String value;
    private Name(String value){
        this.value=value;
    }

    @Override
    protected List<Object> getAtomicValues() {
        return List.of(value);
    }

    // creating the constructor
    public static Result<Name> create (String value){
        if(value==null || value.isEmpty()){
            // return Result.Failure("Name cannot be null or empty");
            return Result.Failure(Error.VALIDATION_ERROR("Name cannot be null or empty"));
        }
        if(value.length()<4){
            // return Result.Failure("Name cannot be less than 4 characters");
            return Result.Failure(Error.VALIDATION_ERROR("Name cannot be less than 4 characters"));
        }
        if(value.length()>10){
            // return Result.Failure("Name cannot be longer than 10");
            return Result.Failure(Error.VALIDATION_ERROR("Name cannot be longer than 10"));
        }
        Name name=new Name(value);
        return Result.Success(name);
    }

    public String getValue(){return value;}

    

}
