package org.example.kurs.service.impl;

import org.example.kurs.model.Device;
import org.example.kurs.model.DeviceLicense;
import org.example.kurs.model.License;
import org.example.kurs.repository.DeviceLicenseRepository;
import org.example.kurs.repository.DeviceRepository;
import org.example.kurs.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceLicenseRepository deviceLicenseRepository;
    private final LicenseRepository licenseRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository,
                         DeviceLicenseRepository deviceLicenseRepository,
                         LicenseRepository licenseRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceLicenseRepository = deviceLicenseRepository;
        this.licenseRepository = licenseRepository;
    }

    // Создание или обновление устройства
    public Device saveDevice(Device device) {
        return deviceRepository.save(device);
    }

    // Получение устройства по ID
    public Optional<Device> getDeviceById(Long id) {
        return deviceRepository.findById(id);
    }

    // Получение устройства по MAC-адресу
    public Device getDeviceByMacAddress(String macAddress) {
        return deviceRepository.findByMacAddress(macAddress);
    }

    // Получение всех устройств
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    // Удаление устройства по ID с дополнительной логикой
    public void deleteDevice(Long id) {
        Optional<Device> deviceOpt = deviceRepository.findById(id);

        if (deviceOpt.isPresent()) {
            Device device = deviceOpt.get();

            // 1. Удаляем запись из DeviceLicense
            Optional<DeviceLicense> deviceLicenseOpt = deviceLicenseRepository.findByDeviceId(id);
            if (deviceLicenseOpt.isPresent()) {
                DeviceLicense deviceLicense = deviceLicenseOpt.get();

                Optional<License> licenseOpt = licenseRepository.findById(deviceLicense.getLicenseId());

                // 2. Получаем лицензию, которая привязана к устройству
                License license = licenseOpt.get();

                // 3. Увеличиваем количество устройств в лицензии
                license.setDeviceCount(license.getDeviceCount() + 1);
                licenseRepository.save(license);

                // Удаляем запись о привязке устройства к лицензии
                deviceLicenseRepository.delete(deviceLicense);
            }

            // 4. Удаляем само устройство
            deviceRepository.delete(device);
        }
    }
}
