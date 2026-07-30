package com.example.demo.Application.user.dto;

import jakarta.validation.constraints.Email;

public record UpdateUserRequest (
    @Email
    String email,
    String password,
    String username
){
    
}
