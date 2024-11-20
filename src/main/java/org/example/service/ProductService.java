package org.example.service;

import org.example.domain.Product;
import org.example.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Value("${upload.path}") // Путь для загрузки файлов, берется из application.properties
    private String uploadPath;

    // Найти продукт по ID
    public Product findById(Long id) {
        return productRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Продукт не найден"));
    }

    // Получить все продукты
    public List<Product> findAll() {
        return (List<Product>) productRepo.findAll();
    }

    // Добавить новый продукт
    public void addProduct(Product product, MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            String resultFilename = saveFile(file);
            product.setFilename(resultFilename);
        }
        productRepo.save(product);
    }

    // Обновить существующий продукт
    public void updateProduct(Long id, Product updatedProduct, MultipartFile file) throws IOException {
        Product existingProduct = findById(id); // Находим текущий продукт по ID

        // Обновляем текстовые поля
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setType(updatedProduct.getType());

        // Если загружен новый файл
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            // Удаляем старое изображение, если оно существует
            deleteFile(existingProduct.getFilename());

            // Сохраняем новый файл
            String resultFilename = saveFile(file);
            existingProduct.setFilename(resultFilename); // Устанавливаем новое имя файла
        }

        // Сохраняем обновленный продукт в базе
        productRepo.save(existingProduct);
    }


    // Удалить продукт
    public void deleteProduct(Long id) {
        Product product = findById(id);

        // Удаляем файл изображения, если он существует
        deleteFile(product.getFilename());

        // Удаляем продукт из базы данных
        productRepo.deleteById(id);
    }

    // Сохранение файла
    private String saveFile(MultipartFile file) throws IOException {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getOriginalFilename();
        file.transferTo(new File(uploadPath + "/" + resultFilename));
        return resultFilename;
    }

    // Удаление файла
    private void deleteFile(String filename) {
        if (filename != null) {
            File file = new File(uploadPath + "/" + filename);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
