package com.example.user_service.utente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ProfiloRequest(
        @NotBlank @Email String email,
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
