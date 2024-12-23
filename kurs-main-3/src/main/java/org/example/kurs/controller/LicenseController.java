package org.example.kurs.controller;

import org.example.kurs.model.License;
import org.example.kurs.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO: 1. Нет разграничения доступа

@RestController
@RequestMapping("/api/licenses")
public class LicenseController {

    private final LicenseRepository licenseRepository;

    @Autowired
    public LicenseController(LicenseRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    @GetMapping
    public ResponseEntity<List<License>> getAllLicenses() {
        List<License> licenses = licenseRepository.findAll();
        return ResponseEntity.ok(licenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<License> getLicenseById(@PathVariable Long id) {
        return licenseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestBody License license) {
        License savedLicense = licenseRepository.save(license);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLicense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<License> updateLicense(@PathVariable Long id, @RequestBody License license) {
        if (!licenseRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        license.setId(id);
        License updatedLicense = licenseRepository.save(license);
        return ResponseEntity.ok(updatedLicense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLicense(@PathVariable Long id) {
        if (!licenseRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        licenseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}