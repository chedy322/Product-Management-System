package com.example.demo.Application.auth.dto.logout;

public record LogoutOutput(
    String message 
) {
    public static LogoutOutput map(String msg){
        return new LogoutOutput(msg);
    }
}
