package org.example.kurs.model;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String username, email, password;
}
