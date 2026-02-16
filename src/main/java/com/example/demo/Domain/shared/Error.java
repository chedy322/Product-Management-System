package com.example.demo.Domain.shared;



public record Error(
    String code,
    String errorMsg,
    int httpStatus
) {
    public static Error NOT_FOUND(String errorString){return new Error("NOT_FOUND",errorString, 404);}
    public static Error VALIDATION_ERROR(String errorString){return new Error("VALIDATION_ERROR",errorString, 400);}
    public static Error CONFLICT(String errorString){return new Error("CONFLICT",errorString, 409);}
    // public static Error SERVER_ERROR(String errorString){return new Error("",errorString, 500);}
    
}
