package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class DemoApplication {
    
	public static void main(String[] args) {
	try {
        Dotenv dotenv = Dotenv.configure()
                .directory("./shop")
                .ignoreIfMissing()
                .ignoreIfMalformed()
                .load();
 
        dotenv.entries().forEach(e -> 
            { 
            System.out.println(e.getValue());
            System.setProperty(e.getKey(), e.getValue());
        } 
    );   
    
        SpringApplication.run(DemoApplication.class, args);
        
    } catch (Exception e) {
        e.printStackTrace();
    
	}
    }
}
