package org.example.repos;

import org.example.domain.Order;
import org.example.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order,Long> {
    List<Order> findByUser(User user);
    List<Order> findAll();
}
