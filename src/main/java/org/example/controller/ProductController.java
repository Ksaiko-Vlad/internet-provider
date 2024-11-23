package org.example.controller;

import org.example.domain.Product;
import org.example.repos.ProductRepo;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/admin_product_list")
public class ProductController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepo productRepo;

    // Отображение списка продуктов
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin_product_list";
    }

    // Форма редактирования продукта
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "admin_product_edit";
    }

    // Обработка сохранения изменений продукта
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/edit/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @ModelAttribute Product product,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Model model) throws IOException {

        // Проверка на существование продукта с таким же названием, кроме текущего
        Product existingProduct = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Продукт не найден"));
        if (!existingProduct.getName().equals(product.getName()) && productRepo.existsByName(product.getName())) {
            model.addAttribute("errorMessage", "Продукт с таким названием уже существует.");
            model.addAttribute("product", existingProduct);
            return "admin_product_edit"; // Вернуться на страницу редактирования с сообщением об ошибке
        }

        // Обновление текстовых данных
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setType(product.getType());

        // Обновление файла (если есть)
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            existingProduct.setFilename(resultFilename);
        }

        productRepo.save(existingProduct);
        return "redirect:/admin_product_list";
    }



    // Удаление продукта
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin_product_list";
    }
}
