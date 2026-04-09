package com.example.demo.Application.auth.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record JwtPayload (
String email,
String username,
UUID id

){
    // map function 
    public static JwtPayload map(String email,String username,UUID userid)
    {
        return new JwtPayload(email, username, userid);
    }
}
