package org.example.controller;

import org.example.domain.Product;
import org.example.domain.Type;
import org.example.repos.ProductRepo; // Переименуйте репозиторий для большей ясности
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @Autowired
    private ProductRepo productRepo;

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "main";
    }

    @PostMapping("/main")
    public String add(@RequestParam String name, @RequestParam Type type, @RequestParam Integer price, Model model) {
        Product product = new Product(name, price, type); // Конструктор без id

        productRepo.save(product);
        model.addAttribute("products", productRepo.findAll());
        return "main";
    }
}
