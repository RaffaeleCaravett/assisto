package com.example.user_service.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record SignupRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 72) String password,
        @NotBlank String nome,
        @NotBlank String cognome,
        String telefono,
        String fotoUrl,
        LocalDate dataNascita,
        String citta,
        String indirizzo,
        String cap
) {
}
