package org.example.repos;

import org.example.domain.Feedback;
import org.example.domain.Product;
import org.example.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepo extends JpaRepository<Feedback,Long> {
    List<Feedback> findByProduct(Product product); // Найти отзывы по продукту
    List<Feedback> findByUser(User user); // Найти отзывы пользователя

}
