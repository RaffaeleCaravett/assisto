package com.example.user_service.paziente;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record PazienteRequest(
        @NotBlank String nome,
        @NotBlank String cognome,
        LocalDate dataNascita,
        String telefono,
        String fotoUrl,
        String citta,
        String indirizzo,
        String cap,
        String fuso
) {
}
