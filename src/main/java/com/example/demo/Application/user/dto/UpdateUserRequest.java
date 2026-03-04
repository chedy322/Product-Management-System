package com.example.demo.Application.user.dto;

public record UpdateUserRequest (
    String email,
    String password,
    String username
){
    
}
