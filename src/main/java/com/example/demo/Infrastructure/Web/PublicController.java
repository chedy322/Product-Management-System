package com.example.demo.Infrastructure.Web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/")
public class PublicController {
    @GetMapping("/main")
    public String homePage(Model model){
         model.addAttribute("message", "Welcome to our website!");
        return "home";
    }
}
