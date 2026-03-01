package com.example.demo.Application.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserRequest (
@NotBlank(message = "Username is mandatory")
String username,

@Email
@NotBlank(message = "Email is mandatory")
String email,

@NotBlank(message = "Password is mandatory")
String password



){
    
}
