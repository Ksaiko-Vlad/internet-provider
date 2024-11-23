package org.example.controller;

import org.example.domain.Feedback;
import org.example.domain.Order;
import org.example.domain.Product;
import org.example.domain.User;
import org.example.service.FeedbackService;
import org.example.service.OrderService;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final ProductService productService;
    private final OrderService orderService;

    public FeedbackController(FeedbackService feedbackService, ProductService productService, OrderService orderService) {
        this.feedbackService = feedbackService;
        this.productService = productService;
        this.orderService = orderService;

    }

    // Страница добавления отзыва
    @GetMapping("/add-feedback/{productId}")
    public String showAddFeedbackPage(@PathVariable Long productId, Model model) {
        Product product = productService.findById(productId);
        model.addAttribute("product", product);
        return "feedback"; // Шаблон для формы отзыва
    }

    @GetMapping("/add/{orderId}")
    public String showProductsForFeedback(@PathVariable Long orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        model.addAttribute("products", order.getProducts());
        return "select_product_for_feedback"; // Шаблон для выбора продукта
    }


    // Обработка формы добавления отзыва
    @PostMapping("/add")
    public String addFeedback(
            @RequestParam String description,
            @RequestParam Integer mark,
            @RequestParam Long productId,
            @AuthenticationPrincipal User user
    ) {
        Product product = productService.findById(productId);
        feedbackService.addFeedback(description, mark, user, product);
        return "redirect:/";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/list")
    public String listFeedbacks(Model model) {
        model.addAttribute("feedbacks", feedbackService.findAll());
        return "admin_feedback_list"; // Имя нового шаблона
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/delete/{feedbackId}")
    public String deleteFeedback(@PathVariable Long feedbackId) {
        feedbackService.deleteById(feedbackId);
        return "redirect:/feedback/list"; // Перенаправление на список после удаления
    }


}


