package org.example.kurs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "licenses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private ApplicationUser user;  // Ссылка на владельца/пользователя, кому принадлежит лицензия

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private ApplicationUser owner;  // Ссылка на владельца лицензии

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;  // Ссылка на продукт, к которому прикреплена лицензия

    // Ссылка на объект LicenseType, связь с таблицей типов лицензий
    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id", nullable = false)
    private LicenseType licenseType;  // Ссылка на объект типа лицензии (LicenseType)

    @Column(name = "first_activation_date", nullable = false)
    private Date firstActivationDate;  // Дата первой активации лицензии

    @Column(name = "ending_date", nullable = false)
    private Date endingDate;  // Дата окончания срока действия лицензии

    @Column(name = "blocked")
    private Boolean blocked;  // Статус блокировки лицензии (true/false)

    @Column(name = "device_count")
    private Integer deviceCount;  // Количество устройств, на которые лицензия может быть установлена

    @Column(name = "duration")
    private Integer duration;  // Длительность лицензии (в днях)

    @Column(name = "description")
    private String description;  // Описание лицензии
}
