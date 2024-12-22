package org.example.kurs.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.kurs.model.*;
import org.example.kurs.repository.*;
import org.example.kurs.configuration.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@RestController
@RequestMapping("/licensing")
@RequiredArgsConstructor
public class LicensingCheckController {

    private final JwtTokenProvider tokenProvider;
    private final LicenseRepository licenseRepo;
    private final DeviceLicenseRepository deviceLicenseRepo;
    private final DeviceRepository deviceRepo;
    private static final Logger logger = LoggerFactory.getLogger(LicensingCheckController.class);

    @PostMapping("/check")
    public ResponseEntity<String> checkLicense(HttpServletRequest httpRequest, @RequestBody LicenseCheckRequest checkRequest) {
        try {
            // Проверка прав пользователя
            Set<String> userRoles = getUserRoles(httpRequest);
            if (userRoles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ошибка аутентификации: роли отсутствуют");
            }

            // Поиск устройства
            Device device = findDeviceByRequest(checkRequest);

            // Проверка привязки лицензии к устройству
            DeviceLicense deviceLicense = findDeviceLicense(device.getId());

            // Поиск лицензии
            License license = findLicenseById(deviceLicense.getLicenseId());

            // Формирование тикета и возврат успешного результата
            Ticket confirmationTicket = createConfirmationTicket(license, deviceLicense.getDeviceId());
            return ResponseEntity.ok("Лицензия активна на устройстве. Тикет: " + confirmationTicket.getId());

        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Ошибка при проверке лицензии: {}", ex.getMessage());
            Ticket errorTicket = createErrorTicket();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка при проверке лицензии. Тикет: " + errorTicket.getId());
        }
    }


    private Set<String> getUserRoles(HttpServletRequest request) {
        Set<String> roles = tokenProvider.getRolesFromRequest(request);
        logger.info("Извлечены роли пользователя: {}", roles);
        return roles;
    }

    private Device findDeviceByRequest(LicenseCheckRequest checkRequest) {
        return deviceRepo.findByMacAddressAndName(checkRequest.getMacAddress(), checkRequest.getDeviceName())
                .orElseThrow(() -> {
                    logger.error("Устройство не найдено: MAC-адрес {}, имя {}", checkRequest.getMacAddress(), checkRequest.getDeviceName());
                    return new NoSuchElementException("Устройство с указанными параметрами не найдено");
                });
    }

    private DeviceLicense findDeviceLicense(Long deviceId) {
        return deviceLicenseRepo.findByDeviceId(deviceId)
                .orElseThrow(() -> {
                    logger.warn("Лицензия не найдена для устройства с ID {}", deviceId);
                    return new NoSuchElementException("Активная лицензия для устройства не найдена");
                });
    }

    private License findLicenseById(Long licenseId) {
        return licenseRepo.findById(licenseId)
                .orElseThrow(() -> {
                    logger.error("Лицензия с ID {} не найдена", licenseId);
                    return new NoSuchElementException("Лицензия с указанным ID не найдена");
                });
    }

    private Ticket createConfirmationTicket(License license, Long deviceId) {
        Ticket ticket = Ticket.createTicket(license.getOwner().getId(), false, license.getEndingDate());
        ticket.setDeviceId(deviceId);
        logger.info("Тикет подтверждения создан: {}", ticket);
        return ticket;
    }

    private Ticket createErrorTicket() {
        Ticket ticket = Ticket.createTicket(null, false, null);
        logger.info("Тикет ошибки создан: {}", ticket);
        return ticket;
    }
}