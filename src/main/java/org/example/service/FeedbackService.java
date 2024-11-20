package org.example.service;

import org.example.domain.Feedback;
import org.example.domain.Product;
import org.example.domain.User;
import org.example.repos.FeedbackRepo;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FeedbackService {
    private final FeedbackRepo feedbackRepository;

    public FeedbackService(FeedbackRepo feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    // Добавить новый отзыв
    public Feedback addFeedback(String description, Integer mark, User user, Product product) {
        Feedback feedback = new Feedback();
        feedback.setDescription(description);
        feedback.setMark(mark);
        feedback.setUser(user);
        feedback.setProduct(product);
        return feedbackRepository.save(feedback);
    }

    // Получить отзывы для конкретного продукта
    public List<Feedback> getFeedbackForProduct(Product product) {
        return feedbackRepository.findByProduct(product);
    }

    // Получить все отзывы пользователя
    public List<Feedback> getFeedbackByUser(User user) {
        return feedbackRepository.findByUser(user);
    }

    public List<Feedback> findAll() {
        return feedbackRepository.findAll(); // Предполагается, что используется JpaRepository
    }

    public void deleteById(Long feedbackId) {
        feedbackRepository.deleteById(feedbackId);
    }

}
