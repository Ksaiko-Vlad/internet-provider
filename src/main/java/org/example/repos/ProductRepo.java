package org.example.repos;

import org.example.domain.Product;
import org.example.domain.Type;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();
    List<Product> findByActiveTrue(Sort sort);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByType(Type type);
    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name, Sort sort);
    List<Product> findByTypeAndActiveTrue(Type type, Sort sort);

    boolean existsByName(String name);
}
