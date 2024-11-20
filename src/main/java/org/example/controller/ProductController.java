package org.example.controller;

import org.example.domain.Product;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin_product_list")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Отображение списка продуктов
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin_product_list";
    }

    // Форма добавления нового продукта
    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin_add_product";
    }

    // Обработка сохранения нового продукта
    @PostMapping("/add")
    public String addProduct(
            @ModelAttribute Product product,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        productService.addProduct(product, file);
        return "redirect:/admin_product_list";
    }

    // Форма редактирования продукта
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "admin_product_edit";
    }

    // Обработка сохранения изменений продукта
    @PostMapping("/edit/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @ModelAttribute Product product, // Продукт с обновленными текстовыми данными
            @RequestParam(value = "file", required = false) MultipartFile file // Новый файл (если выбран)
    ) throws IOException {
        productService.updateProduct(id, product, file);
        return "redirect:/admin_product_list";
    }


    // Удаление продукта
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin_product_list";
    }
}
