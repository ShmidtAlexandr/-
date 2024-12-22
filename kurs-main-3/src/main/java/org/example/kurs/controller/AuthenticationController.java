package org.example.kurs.controller;

import lombok.RequiredArgsConstructor;
import org.example.kurs.configuration.JwtTokenProvider;
import org.example.kurs.model.ApplicationUser;
import org.example.kurs.model.AuthenticationRequest;
import org.example.kurs.model.AuthenticationResponse;
import org.example.kurs.repository.ApplicationUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final ApplicationUserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest request) {
        try {
            String email = request.getEmail();

            ApplicationUser user = findUserByEmail(email);

            authenticate(email, request.getPassword());

            String token = generateJwtToken(email, user);

            logger.info("Пользователь успешно аутентифицирован: {}", email);
            return ResponseEntity.ok(new AuthenticationResponse(email, token));
        } catch (UsernameNotFoundException ex) {
            logger.warn("Аутентификация не удалась: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        } catch (AuthenticationException ex) {
            logger.warn("Некорректные учетные данные для пользователя: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный email или пароль");
        } catch (Exception ex) {
            logger.error("Ошибка при аутентификации: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла внутренняя ошибка сервера");
        }
    }

    private ApplicationUser findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с email " + email + " не найден"));
    }

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        logger.debug("Успешная аутентификация для пользователя с email: {}", email);
    }

    private String generateJwtToken(String email, ApplicationUser user) {
        String token = jwtTokenProvider.createToken(email, user.getRole().getGrantedAuthorities());
        logger.debug("JWT-токен создан для пользователя: {}", email);
        return token;
    }
}