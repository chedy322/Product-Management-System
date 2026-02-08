package com.example.demo.Infrastructure.Web.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.demo.Domain.exceptions.DomainExceptions;
import com.example.demo.Domain.shared.Error;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class globalExceptionHandler {
    @ExceptionHandler(value=DomainExceptions.class)
    public ResponseEntity<String> handleException(DomainExceptions ex) {
        return ResponseEntity.status(ex.getError().httpStatus()).body(ex.getError().errorMsg()); 
    }

}
