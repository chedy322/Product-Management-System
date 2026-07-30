package com.example.demo.Domain.Interfaces;

public interface PasswordEncoder{
    String encode(String inputPassword);
    Boolean matches(String inputPassword,String encodedPassword);
}