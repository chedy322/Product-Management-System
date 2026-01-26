package com.example.demo.Domain.Entities;

import java.util.UUID;

import com.example.demo.Domain.Primitives.Entity;
import com.example.demo.Domain.ValueObjects.Name;

public class User extends Entity{
    private Name name;
    private String email;
    private String password;

    private User(UUID id,Name name,String email,String password){
        super(id);
        this.name=name;
        this.email=email;
        this.password=password;

    }

    // getter and setter
  // ===== GETTERS =====

    public Name getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // ===== SETTERS =====
    // (Be careful with setters in domain entities)

    private void setName(Name name) {
        this.name = name;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public static User create (Name name,String email,String password,boolean EmailIsUnique){
        if(!EmailIsUnique){
             throw new IllegalArgumentException("Email should be unique");
        }
        User user=new User(UUID.randomUUID(),name,email,password);
        return user;

    }


}