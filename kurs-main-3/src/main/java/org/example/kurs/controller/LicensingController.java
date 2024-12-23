package org.example.kurs.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.kurs.model.*;
import org.example.kurs.repository.*;
import org.example.kurs.service.LicenseHistoryService;
import org.example.kurs.configuration.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

//TODO: 1. createLicense - проверку прав доступа сделать при помощи security, а не использовать кривую проверку Данилина
//TODO: 2. assembleLicense - даты первой активации и окончания должны устанавливаться только при первой активации лицензии

@RestController
@RequestMapping("/licensing")
@RequiredArgsConstructor
public class LicensingController {

    private final JwtTokenProvider tokenProvider;
    private final ProductRepository productRepo;
    private final ApplicationUserRepository userRepo;
    private final LicenseTypeRepository licenseTypeRepo;
    private final LicenseRepository licenseRepo;
    private final LicenseHistoryService historyService;

    @PostMapping("/create")
    public ResponseEntity<String> createLicense(HttpServletRequest httpRequest, @RequestBody LicenseCreateRequest licenseRequest) {
        try {
            // Проверяем права пользователя
            Set<String> roles = extractRoles(httpRequest);
            if (!roles.contains("ROLE_ADMIN")) {
                return new ResponseEntity<>("Доступ запрещен: требуется роль ADMIN", HttpStatus.FORBIDDEN);
            }

            // Получаем связанные сущности
            Product product = fetchProduct(licenseRequest.getProductId());
            validateProductNotBlocked(product);

            ApplicationUser user = fetchUser(licenseRequest.getOwnerId());
            LicenseType licenseType = fetchLicenseType(licenseRequest.getLicenseTypeId());

            // Создаем лицензию
            License license = assembleLicense(product, user, licenseType, licenseRequest);
            licenseRepo.save(license);

            // Записываем историю изменений
            recordHistory(license, user, "Лицензия успешно создана");

            return new ResponseEntity<>("Лицензия успешно создана", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Ошибка сервера: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Set<String> extractRoles(HttpServletRequest request) {
        return tokenProvider.getRolesFromRequest(request);
    }

    private Product fetchProduct(Long productId) {
        return productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Продукт не найден: ID=" + productId));
    }

    private void validateProductNotBlocked(Product product) {
        if (product.isBlocked()) {
            throw new IllegalArgumentException("Продукт заблокирован, лицензия не может быть создана");
        }
    }

    private ApplicationUser fetchUser(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: ID=" + userId));
    }

    private LicenseType fetchLicenseType(Long licenseTypeId) {
        return licenseTypeRepo.findById(licenseTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Тип лицензии не найден: ID=" + licenseTypeId));
    }

    private License assembleLicense(Product product, ApplicationUser user, LicenseType licenseType, LicenseCreateRequest request) {
        License license = new License();
        license.setCode(generateLicenseCode());
        license.setOwner(user);
        license.setProduct(product);
        license.setLicenseType(licenseType);
        license.setDeviceCount(request.getDeviceCount());
        license.setFirstActivationDate(convertToDate(LocalDate.now()));
        license.setEndingDate(calculateEndingDate(licenseType.getDefaultDuration()));
        license.setBlocked(false);
        license.setDuration(licenseType.getDefaultDuration());
        license.setDescription(Optional.ofNullable(request.getDescription()).orElse("Приятного пользования!"));
        return license;
    }

    private Date calculateEndingDate(int durationDays) {
        LocalDate endDate = LocalDate.now().plusDays(durationDays);
        return convertToDate(endDate);
    }

    private void recordHistory(License license, ApplicationUser user, String description) {
        Date now = convertToDate(LocalDate.now());
        historyService.recordLicenseChange(license.getId(), user.getId(), "Создана", now, description);
    }

    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private String generateLicenseCode() {
        return UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
    }
}