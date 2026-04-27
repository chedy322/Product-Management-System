package com.example.demo.Infrastructure.Web.dto.adminController;

public record ResponseDeleteUser (
    String message
)
{
    public static ResponseDeleteUser map(String msg){
        return new ResponseDeleteUser(msg);
    }
}
