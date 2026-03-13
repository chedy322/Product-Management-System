package com.example.demo.Infrastructure.config.Enum;


public enum UserRole{
    USER,ADMIN;
    public String getAuthority(){
        return "ROLE_"+this.name();
    }
}
