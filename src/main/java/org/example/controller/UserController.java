package org.example.controller;

import org.example.domain.Order;
import org.example.domain.Role;
import org.example.domain.User;
import org.example.service.OrderService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());

        return "admin_user_list";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "admin_user_edit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam ("userId") User user)
    {
       userService.saveUser(user, username, form);

        return "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        // Используем объект orderService для вызова метода
        List<Order> userOrders = orderService.getOrdersByUser(user);
        model.addAttribute("orders", userOrders);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("phone", user.getPhone());

        return "profile";
    }


    @PostMapping("profile")
    public String updateProfile(
    @AuthenticationPrincipal User user,
    @RequestParam String username,
    @RequestParam String password,
    @RequestParam Integer phone){

        userService.updateProfile(user, username, password, phone);
        return "redirect:/user/profile";
    }

}
