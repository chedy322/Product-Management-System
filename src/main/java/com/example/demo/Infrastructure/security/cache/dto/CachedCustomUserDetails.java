package com.example.demo.Infrastructure.security.cache.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CachedCustomUserDetails {
    public UUID userId;
    public String username;
    public String email;
    public List<String> authorities;

}
