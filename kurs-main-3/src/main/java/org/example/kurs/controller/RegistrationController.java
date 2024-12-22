package org.example.kurs.controller;

import lombok.RequiredArgsConstructor;
import org.example.kurs.model.ApplicationUser;
import org.example.kurs.model.ApplicationRole;
import org.example.kurs.model.RegistrationRequest;
import org.example.kurs.repository.ApplicationUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RegistrationController {

    private final ApplicationUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest request) {
        try {
            validateEmailUniqueness(request.getEmail());

            ApplicationUser newUser = createNewUser(request);

            saveUser(newUser);

            logger.info("Пользователь успешно зарегистрирован: {}", newUser.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body("Пользователь успешно зарегистрирован");
        } catch (IllegalArgumentException e) {
            logger.warn("Ошибка регистрации: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Ошибка во время регистрации: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка при регистрации");
        }
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            logger.warn("Email уже используется: {}", email);
            throw new IllegalArgumentException("Email уже используется");
        }
    }

    private ApplicationUser createNewUser(RegistrationRequest request) {
        ApplicationUser user = new ApplicationUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(ApplicationRole.USER); // Устанавливаем роль по умолчанию
        logger.info("Создан новый пользователь: {}", user.getEmail());
        return user;
    }

    private void saveUser(ApplicationUser user) {
        userRepository.save(user);
        logger.info("Пользователь сохранен в базе данных: {}", user.getEmail());
    }
}