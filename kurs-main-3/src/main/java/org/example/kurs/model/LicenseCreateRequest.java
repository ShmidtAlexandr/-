package org.example.kurs.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LicenseCreateRequest {

    private Long productId, ownerId, licenseTypeId;
    private String description;
    private Integer deviceCount;

}
