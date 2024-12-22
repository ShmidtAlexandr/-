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
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/licensing")
@RequiredArgsConstructor
public class LicensingActivationController {

    private final JwtTokenProvider tokenProvider;
    private final ApplicationUserRepository userRepository;
    private final LicenseRepository licenseRepository;
    private final DeviceLicenseRepository deviceLicenseRepository;
    private final DeviceRepository deviceRepository;
    private final LicenseHistoryService historyService;

    private static final Logger log = LoggerFactory.getLogger(LicensingActivationController.class);

    @PostMapping("/activation")
    public ResponseEntity<?> activateLicense(HttpServletRequest request, @RequestBody LicenseActivationRequest requestPayload) {
        try {
            // Извлечение ролей пользователя из токена
            Set<String> userRoles = tokenProvider.getRolesFromRequest(request);
            log.debug("Извлеченные роли пользователя: {}", userRoles);

            if (userRoles.isEmpty()) {
                log.warn("Попытка активации без роли");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Доступ запрещен: у пользователя нет ролей.");
            }

            // Получение лицензии по коду
            License license = getLicenseByCode(requestPayload.getCode());

            // Проверка текущего пользователя
            ApplicationUser user = getAuthenticatedUser(request);

            // Проверка прав на использование лицензии
            validateLicenseOwnership(license, user);

            // Регистрация устройства, если оно не существует
            Device device = findOrRegisterDevice(requestPayload, user);

            // Проверка доступных активаций и привязки лицензии к устройству
            checkDeviceLicenseAvailability(license, device);

            // Активация лицензии
            activateDeviceLicense(license, device);

            // Создание тикета и возврат успешного ответа
            Ticket ticket = Ticket.createTicket(null, false, license.getEndingDate());
            log.info("Активация завершена успешно, создан тикет: {}", ticket);

            return ResponseEntity.ok("Лицензия успешно активирована.");
        } catch (IllegalArgumentException e) {
            log.error("Ошибка активации: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Системная ошибка: {}", e.getMessage(), e);
            Ticket errorTicket = Ticket.createTicket(null, true, null);
            log.info("Создан тикет с ошибкой: {}", errorTicket);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Произошла ошибка при обработке запроса.");
        }
    }

    // Метод получения лицензии по коду
    private License getLicenseByCode(String code) {
        return licenseRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Лицензия с кодом " + code + " не найдена."));
    }

    // Метод получения текущего аутентифицированного пользователя
    private ApplicationUser getAuthenticatedUser(HttpServletRequest request) {
        String email = tokenProvider.getEmailFromRequest(request);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с email " + email + " не найден."));
    }

    // Проверка принадлежности лицензии пользователю
    private void validateLicenseOwnership(License license, ApplicationUser user) {
        if (license.getUser() != null && !license.getUser().getEmail().equals(user.getEmail())) {
            throw new IllegalArgumentException("Эта лицензия принадлежит другому пользователю.");
        }
        license.setUser(user);
    }

    // Метод поиска или регистрации устройства
    private Device findOrRegisterDevice(LicenseActivationRequest requestPayload, ApplicationUser user) {
        return deviceRepository.findByMacAddressAndName(requestPayload.getMacAddress(), requestPayload.getDeviceName())
                .orElseGet(() -> {
                    Device newDevice = new Device();
                    newDevice.setMacAddress(requestPayload.getMacAddress());
                    newDevice.setName(requestPayload.getDeviceName());
                    newDevice.setUserId(user.getId());
                    deviceRepository.save(newDevice);

                    log.info("Зарегистрировано новое устройство: MAC {} | Имя {}", requestPayload.getMacAddress(), requestPayload.getDeviceName());
                    return newDevice;
                });
    }

    // Проверка на доступные активации и отсутствие дублирования
    private void checkDeviceLicenseAvailability(License license, Device device) {
        if (license.getDeviceCount() <= 0) {
            throw new IllegalArgumentException("Нет доступных активаций для этой лицензии.");
        }

        if (deviceLicenseRepository.findByDeviceIdAndLicenseId(device.getId(), license.getId()).isPresent()) {
            throw new IllegalArgumentException("Эта лицензия уже активирована на данном устройстве.");
        }
    }

    // Активация лицензии
    private void activateDeviceLicense(License license, Device device) {
        DeviceLicense newDeviceLicense = new DeviceLicense();
        newDeviceLicense.setDeviceId(device.getId());
        newDeviceLicense.setLicenseId(license.getId());
        newDeviceLicense.setActivationDate(new Date());
        deviceLicenseRepository.save(newDeviceLicense);

        license.setDeviceCount(license.getDeviceCount() - 1);
        licenseRepository.save(license);

        historyService.recordLicenseChange(
                license.getId(),
                license.getUser().getId(),
                "Активирована",
                new Date(),
                "Лицензия активирована на устройстве."
        );

        log.info("Активация лицензии завершена. Осталось мест: {}", license.getDeviceCount());
    }
}