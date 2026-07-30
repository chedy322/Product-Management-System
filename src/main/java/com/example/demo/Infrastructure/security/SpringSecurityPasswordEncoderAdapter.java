package com.example.demo.Infrastructure.security;

import org.springframework.stereotype.Service;

import com.example.demo.Domain.Interfaces.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpringSecurityPasswordEncoderAdapter implements PasswordEncoder {
 private final org.springframework.security.crypto.password.PasswordEncoder springPasswordEncoder;
 
 @Override
 public String encode(String inputPassword) {
     return springPasswordEncoder.encode(inputPassword);
 }

 @Override
 public Boolean matches(String inputPassword, String encodedPassword) {
     return springPasswordEncoder.matches(inputPassword, encodedPassword);
 }
}
