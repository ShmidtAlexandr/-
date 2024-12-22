package org.example.kurs.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.kurs.model.*;
import org.example.kurs.repository.*;
import org.example.kurs.configuration.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/licensing")
@RequiredArgsConstructor
public class LicensingUpdateController {

    private final JwtTokenProvider tokenProvider;
    private final LicenseRepository licenseRepo;
    private final DeviceLicenseRepository deviceLicenseRepo;
    private final ApplicationUserRepository userRepo;

    @PostMapping("/update")
    public ResponseEntity<String> updateLicense(HttpServletRequest httpRequest, @RequestBody LicenseUpdateRequest updateRequest) {
        try {
            // Проверка роли пользователя
            Set<String> roles = extractRoles(httpRequest);
            if (roles.isEmpty()) {
                return new ResponseEntity<>("Ошибка аутентификации: отсутствуют роли", HttpStatus.UNAUTHORIZED);
            }

            // Проверяем корректность кода лицензии
            String licenseCode = validateLicenseCode(updateRequest.getCode());

            // Получаем лицензию и связанные данные
            License license = fetchLicenseByCode(licenseCode);
            validateLicenseOwnership(httpRequest, license);

            // Проверяем состояние лицензии
            validateLicenseState(license);

            // Парсим новую дату окончания
            Date newExpirationDate = parseExpirationDate(updateRequest.getNewExpirationDate());
            validateNewExpirationDate(newExpirationDate, license.getEndingDate());

            // Обновляем лицензию
            updateLicenseExpiration(license, newExpirationDate);

            // Обрабатываем данные устройства
            String deviceMessage = processDeviceLicense(license);

            return ResponseEntity.ok("Лицензия успешно обновлена. " + deviceMessage + " Новая дата окончания: " + newExpirationDate);
        } catch (IllegalArgumentException | ParseException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла ошибка при обновлении лицензии: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Set<String> extractRoles(HttpServletRequest request) {
        return tokenProvider.getRolesFromRequest(request);
    }

    private String validateLicenseCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Код лицензии не может быть пустым");
        }
        return code.trim();
    }

    private License fetchLicenseByCode(String code) {
        return licenseRepo.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Лицензия с указанным кодом не найдена"));
    }

    private void validateLicenseOwnership(HttpServletRequest request, License license) {
        String userEmail = tokenProvider.getEmailFromRequest(request);
        ApplicationUser user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + userEmail));

        if (!license.getOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Пользователь не является владельцем лицензии");
        }
    }

    private void validateLicenseState(License license) {
        if (license.getBlocked()) {
            throw new IllegalArgumentException("Лицензия заблокирована, продление невозможно");
        }

        if (license.getEndingDate().before(new Date())) {
            throw new IllegalArgumentException("Лицензия истекла, продление невозможно");
        }
    }

    private Date parseExpirationDate(String expirationDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.parse(expirationDate);
    }

    private void validateNewExpirationDate(Date newDate, Date currentDate) {
        if (!newDate.after(currentDate)) {
            throw new IllegalArgumentException("Новая дата окончания лицензии должна быть больше текущей");
        }
    }

    private void updateLicenseExpiration(License license, Date newDate) {
        int newDuration = calculateDaysBetween(newDate);
        license.setEndingDate(newDate);
        license.setDuration(newDuration);
        licenseRepo.save(license);
    }

    private int calculateDaysBetween(Date newDate) {
        LocalDate now = LocalDate.now();
        LocalDate endDate = newDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return (int) ChronoUnit.DAYS.between(now, endDate);
    }

    private String processDeviceLicense(License license) {
        Optional<DeviceLicense> deviceLicenseOpt = deviceLicenseRepo.findByLicenseId(license.getId());
        if (deviceLicenseOpt.isPresent()) {
            DeviceLicense deviceLicense = deviceLicenseOpt.get();
            return "Лицензия активирована на устройстве с ID: " + deviceLicense.getDeviceId();
        }
        return "Устройство не привязано к лицензии.";
    }
}