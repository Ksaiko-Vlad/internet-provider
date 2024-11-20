package org.example.service;

import ch.qos.logback.core.util.StringUtil;
import org.example.domain.Role;
import org.example.domain.User;
import org.example.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username); // Метод в репозитории
    }

    public boolean existsByPhone(String phone) {
        return userRepo.existsByPhone(phone); // Метод в репозитории
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);
    }

    public void updateProfile(User user, String username, String password, String phone) {
        // Обновляем телефон, если новый номер предоставлен и отличается от текущего
        if (phone != null && !phone.equals(user.getPhone())) {
            user.setPhone(phone);
        }

        // Обновляем имя пользователя, если оно предоставлено и отличается
        if (!StringUtils.isEmpty(username) && !username.equals(user.getUsername())) {
            user.setUsername(username);
        }

        // Обновляем пароль, если он предоставлен и отличается
        if (!StringUtils.isEmpty(password) && !password.equals(user.getPassword())) {
            user.setPassword(password);
        }

        userRepo.save(user);
    }
}
