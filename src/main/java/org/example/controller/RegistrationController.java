package org.example.controller;

import org.example.domain.Role;
import org.example.domain.User;
import org.example.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        // Проверяем, существует ли пользователь с таким именем
        if (userRepo.existsByUsername(user.getUsername())) {
            model.put("usernameError", "Имя пользователя уже занято!");
            return "registration";
        }

        // Проверяем, существует ли пользователь с таким номером телефона
        if (userRepo.existsByPhone(user.getPhone())) {
            model.put("phoneError", "Номер телефона уже используется!");
            return "registration";
        }

        // Устанавливаем стандартные параметры для нового пользователя
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user); // Сохраняем пользователя в БД

        // Перенаправляем на страницу входа после успешной регистрации
        return "redirect:/login";
    }

}
