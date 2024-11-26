package org.example.repos;

import org.example.domain.Product;
import org.example.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByType(Type type);

    boolean existsByName(String name);
}
