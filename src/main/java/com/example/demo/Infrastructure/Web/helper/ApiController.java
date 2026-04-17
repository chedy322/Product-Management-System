package com.example.demo.Infrastructure.Web.helper;

import java.util.function.Function;

import org.springframework.http.ResponseEntity;

import com.example.demo.Domain.shared.Result;



import  com.example.demo.Domain.shared.Error;


// This class is used 
public abstract class ApiController {
    // this method is for custom status 
     protected <T,R> ResponseEntity<?> handleResult(Result<T> result, Function<T, R> mapper,int status){
        if(result.isFailure()){
            mapFailureToResult(result.getError());
        }
        R responseResult=mapper.apply(result.getValue());
        return ResponseEntity.status(status).body(responseResult);
    }
    // this method is for default ok
    protected <T,R> ResponseEntity<?> handleResult(Result<T> result, Function<T, R> mapper){
        if(result.isFailure()){
            mapFailureToResult(result.getError());
        }
        R responseResult=mapper.apply(result.getValue());
        return ResponseEntity.ok(responseResult);
    }
    // this is for return T as result(NOT USED)
    protected <T> ResponseEntity<?> handleResult(Result<T> result){
        if(result.isFailure()){
           mapFailureToResult(result.getError());
        }
        return ResponseEntity.ok(result.getValue());
    }

    // This is for handling error 
    protected<T> ResponseEntity<?> mapFailureToResult(Error error){
        return ResponseEntity.status(error.httpStatus()).body(error.errorMsg());
    }

}
