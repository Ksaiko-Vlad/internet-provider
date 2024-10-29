package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting() {
        return "greeting";
    }

    @GetMapping
    public String main (Model model) {
        model.addAttribute("some", "Hello, Kostia");
        return "main";
    }

}