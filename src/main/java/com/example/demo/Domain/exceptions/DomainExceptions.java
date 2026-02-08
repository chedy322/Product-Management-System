package com.example.demo.Domain.exceptions;

import com.example.demo.Domain.shared.Error;

public class DomainExceptions extends RuntimeException {
    private final Error error;
    public DomainExceptions(Error error){
        super(error.errorMsg());
        this.error=error;
    }
    public Error getError(){
        return error;
    }


}
