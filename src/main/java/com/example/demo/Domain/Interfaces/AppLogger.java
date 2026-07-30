package com.example.demo.Domain.Interfaces;


public interface AppLogger {
       void info(String message);
        void warn(String message);
        void error(String message, Throwable t);
    
}