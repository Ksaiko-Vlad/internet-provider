package org.example.controller;

import org.example.domain.Product;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    // Форма редактирования продукта
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "admin_product_edit";
    }

    // Обработка сохранения изменений продукта
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product) {
        productService.updateProduct(id, product);
        return "redirect:/admin_product_list";
    }

    // Удаление продукта
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin_product_list";
    }
}
