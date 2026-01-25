package com.example.demo.Domain.ValueObjects;

import java.util.List;

import com.example.demo.Domain.Primitives.ValueObject;

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
    public static Name create (String value){
        if(value.length()<4){
            throw new IllegalArgumentException("Name cannot be less than 4");
        }
        if(value.length()>10){
            throw new IllegalArgumentException("Name cannot be longer than 10");
        }
        Name name=new Name(value);
        return name;
    }

    public String getValue(){return value;}

    

}
