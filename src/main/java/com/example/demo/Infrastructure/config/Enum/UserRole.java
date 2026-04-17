package com.example.demo.Infrastructure.config.Enum;

import java.lang.classfile.ClassFile.Option;
import java.util.Optional;

public enum UserRole{
    USER,ADMIN;
    public String getAuthority(){
        return "ROLE_"+this.name();
    }
    public static Optional<UserRole> map(String role){
        if(role==null) return Optional.empty();
        try{
            return Optional.of(UserRole.valueOf(role.toUpperCase(null).trim()));
        }catch(Exception e){
            return Optional.empty();
        }


    }
}
