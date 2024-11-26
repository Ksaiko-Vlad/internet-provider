package org.example.controller;

import org.example.domain.Feedback;
import org.example.domain.Product;
import org.example.domain.Type;
import org.example.repos.FeedbackRepo;
import org.example.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/main")
    public String menu() {
        return "main";
    }

    @GetMapping("/information")
        public String information(){
        return "information";
    }

    @GetMapping("/productslist")
    public String listProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            Model model) {

        List<Product> products;

        if (search != null && !search.isEmpty()) {
            products = productRepo.findByNameContainingIgnoreCase(search);
        } else if (type != null && !type.isEmpty()) {
            products = productRepo.findByType(Type.valueOf(type));
        } else if ("price".equals(sortBy)) {
            Sort sort = Sort.by("price");
            sort = "desc".equals(direction) ? sort.descending() : sort.ascending();
            products = productRepo.findAll(sort);
        } else {
            products = productRepo.findAll();
        }

        model.addAttribute("products", products);
        model.addAttribute("search", search);
        model.addAttribute("type", type);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        // Добавляем список всех типов для выбора в фильтре
        model.addAttribute("types", Type.values());

        return "product_list";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/addproduct")
    public String addproduct(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "admin_add_product";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/addproduct")
    public String add(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Type type,
            @RequestParam Integer price,
            @RequestParam("file") MultipartFile file,
            Model model) throws IOException {

        // Проверка на существование продукта с таким же названием
        if (productRepo.existsByName(name)) {
            model.addAttribute("errorMessage", "Продукт с таким названием уже существует.");
            model.addAttribute("products", productRepo.findAll());
            return "admin_add_product"; // Вернуться на страницу добавления с сообщением об ошибке
        }

        Product product = new Product(name, description, price, type);

        if (file != null && !file.getOriginalFilename().isEmpty()) {
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
