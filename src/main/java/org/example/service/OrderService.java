package org.example.service;

import org.example.domain.Order;
import org.example.domain.Product;
import org.example.domain.User;
import org.example.repos.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    // Используем CopyOnWriteArrayList для потокобезопасности
    private final List<Product> cart = new CopyOnWriteArrayList<>();

    public List<Order> getOrdersByUser(User user) {
        return orderRepo.findByUser(user);
    }

    // Добавить товар в корзину
    public void addToCart(Product product) {
        if (product != null) {
            cart.add(product);
        } else {
            throw new IllegalArgumentException("Продукт не может быть null.");
        }
    }

    // Удалить товар из корзины
    public void removeFromCart(Product product) {
        if (!cart.contains(product)) {
            throw new IllegalArgumentException("Продукт отсутствует в корзине.");
        }
        cart.remove(product);
    }

    // Получить текущую корзину
    public List<Product> getCart() {
        return Collections.unmodifiableList(cart); // Возвращаем неизменяемый список
    }

    // Получить общую стоимость корзины
    public int getTotalPrice() {
        return cart.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }

    // Создать заказ на основе корзины
    public void createOrder(User user) {
        if (cart.isEmpty()) {
            throw new IllegalStateException("Корзина пуста!");
        }
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден.");
        }
        Order order = new Order(new ArrayList<>(cart), LocalDateTime.now(), user);
        orderRepo.save(order);
        cart.clear(); // Очистить корзину после оформления заказа
    }
}
