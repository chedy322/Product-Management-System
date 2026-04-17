package com.example.demo.Infrastructure.Web.common;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.demo.Domain.exceptions.DomainExceptions;
import com.example.demo.Domain.shared.Error;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class globalExceptionHandler {

    // Handled Erros for domainExceptions
    @ExceptionHandler(value=DomainExceptions.class)
    public ResponseEntity<Map<String,Object>> handleException(DomainExceptions ex) {
        return ResponseEntity.status(ex.getError().httpStatus()).
        body(Map.of(
            "status", ex.getError().httpStatus(),
            // "error", ex.getError().getClass().getSimpleName(), // e.g., "NOT_FOUND"
            "message", ex.getError().errorMsg(),
            "timestamp", LocalDateTime.now()
        )); 
    }

    // Handled Error for @Value
    @ExceptionHandler(value=MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String,Object>> handleValueException(MethodArgumentTypeMismatchException ex){
        String message = String.format("Parameter '%s' must be of type '%s'.", 
                                        ex.getName(), ex.getRequiredType().getSimpleName());
        return ResponseEntity.badRequest().body(Map.of(
            "error", "BAD_REQUEST", 
            "message", message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllUncaughtException(Exception ex) {
        return ResponseEntity.internalServerError().body(Map.of(
            "error", "SERVER_ERROR",
            "message", "Internal system failure."
        ));
    }

}
