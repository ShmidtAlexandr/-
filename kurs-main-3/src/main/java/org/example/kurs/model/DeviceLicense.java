package org.example.kurs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

//TODO: 1. Таблица реализована неверно. Она должна реализовывать связь многие ко многим между лицензией и устройствами, а получилось 1 к 1

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "device_license")
public class DeviceLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "license_id")
    private Long licenseId;

    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "activation_date")
    private Date activationDate;
}
