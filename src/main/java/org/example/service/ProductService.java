package org.example.service;

import org.example.domain.Product;
import org.example.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    // Найти продукт по ID
    public Product findById(Long id) {
        return productRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Продукт не найден"));
    }

    // Получить все продукты
    public List<Product> findAll() {
        return (List<Product>) productRepo.findAll();
    }

    // Обновить продукт
    public void updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = findById(id);
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setType(updatedProduct.getType());
        productRepo.save(existingProduct);
    }

    // Удалить продукт
    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }
}
