package org.example.kurs.controller;

import org.example.kurs.model.LicenseType;
import org.example.kurs.service.impl.LicenseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO: 1. Нет разграничения доступа

@RestController
@RequestMapping("/api/license-types")
public class LicenseTypeController {

    private final LicenseTypeService licenseTypeService;

    @Autowired
    public LicenseTypeController(LicenseTypeService licenseTypeService) {
        this.licenseTypeService = licenseTypeService;
    }

    // Создание или обновление типа лицензии
    @PostMapping
    public ResponseEntity<LicenseType> createOrUpdateLicenseType(@RequestBody LicenseType licenseType) {
        LicenseType savedLicenseType = licenseTypeService.saveLicenseType(licenseType);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLicenseType);
    }

    // Получение типа лицензии по ID
    @GetMapping("/{id}")
    public ResponseEntity<LicenseType> getLicenseTypeById(@PathVariable Long id) {
        return licenseTypeService.getLicenseTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Получение всех типов лицензий
    @GetMapping
    public ResponseEntity<List<LicenseType>> getAllLicenseTypes() {
        List<LicenseType> licenseTypes = licenseTypeService.getAllLicenseTypes();
        return ResponseEntity.ok(licenseTypes);
    }

    // Удаление типа лицензии по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLicenseType(@PathVariable Long id) {
        if (licenseTypeService.getLicenseTypeById(id).isPresent()) {
            licenseTypeService.deleteLicenseType(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}