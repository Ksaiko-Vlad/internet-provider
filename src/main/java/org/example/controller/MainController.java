package org.example.controller;

import org.example.domain.Feedback;
import org.example.domain.Product;
import org.example.domain.Type;
import org.example.repos.FeedbackRepo;
import org.example.repos.ProductRepo; // Переименуйте репозиторий для большей ясности
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private FeedbackRepo feedbackRepo;

    @Value("${upload.path}")
private String uploadPath;

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("feedbacks", feedbackRepo.findAll()); // Передаём отзывы в модель
        return "greeting";
    }

    @GetMapping("/main")
    public String menu() {
        return "main";
    }

    @GetMapping("/information")
        public String information(){
        return "information";
    }

    @GetMapping("/productslist")
    public String productslist(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "product_list";
    }

    @GetMapping("/addproduct")
    public String addproduct(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "admin_add_product";
    }

    @PostMapping("/addproduct")
    public String add(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Type type,
            @RequestParam Integer price,
            @RequestParam("file") MultipartFile file,
            Model model) throws IOException {

        Product product = new Product(name, description, price, type);// Конструктор без id

        if (file != null && !file.getOriginalFilename().isEmpty()){
File uploadDir = new File(uploadPath);
if (!uploadDir.exists()) {
    uploadDir.mkdir();
}
           String uuidFile = UUID.randomUUID().toString();
String resultFilename = uuidFile + "." + file.getOriginalFilename();
file.transferTo(new File(uploadPath + "/" + resultFilename));
product.setFilename(resultFilename);
        }

        productRepo.save(product);
        model.addAttribute("products", productRepo.findAll());
        return "admin_add_product";
    }
}
