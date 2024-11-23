package org.example.controller;

import org.example.domain.Product;
import org.example.domain.User;
import org.example.service.OrderService;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    // Показать корзину
    @GetMapping("/basket")
    public String basket(Model model) {
        model.addAttribute("cart", orderService.getCart());
        model.addAttribute("totalPrice", orderService.getTotalPrice()); // Общая стоимость
        return "basket";
    }

    @GetMapping("/orders")
    public String allOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin_order_list"; // Название шаблона для отображения всех заказов
    }

    // Добавить продукт в корзину
    @PostMapping("/basket/add")
    public String addToBasket(@RequestParam Long productId) {
        Product product = productService.findById(productId);
        if (product != null) {
            orderService.addToCart(product);
        }
        return "redirect:/productslist"; // Перенаправление на список продуктов
    }

    // Удалить продукт из корзины
    @PostMapping("/basket/remove")
    public String removeFromBasket(@RequestParam Long productId, Model model) {
        Product product = productService.findById(productId);
        if (product != null) {
            try {
                orderService.removeFromCart(product);
            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                model.addAttribute("cart", orderService.getCart());
                model.addAttribute("totalPrice", orderService.getTotalPrice());
                return "basket"; // Вернуться на страницу корзины с ошибкой
            }
        }
        return "redirect:/basket"; // Перенаправление на страницу корзины
    }

    // Оформить заказ
    @PostMapping("/basket/order")
    public String createOrder(@AuthenticationPrincipal User user) {
        orderService.createOrder(user);
        return "redirect:/basket"; // Перенаправление на профиль после оформления заказа
    }
}
