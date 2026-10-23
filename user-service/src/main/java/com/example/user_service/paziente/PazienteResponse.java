package com.example.user_service.paziente;

import java.time.LocalDate;

public record PazienteResponse(
        Long id,
        String nome,
        String cognome,
        LocalDate dataNascita,
        String telefono,
        String fotoUrl,
        String citta,
        String indirizzo,
        String cap,
        String fuso
) {
    public static PazienteResponse from(Paziente paziente) {
        return new PazienteResponse(
                paziente.getId(),
                paziente.getNome(),
                paziente.getCognome(),
                paziente.getDataNascita(),
                paziente.getTelefono(),
                paziente.getFotoUrl(),
                paziente.getCitta(),
                paziente.getIndirizzo(),
                paziente.getCap(),
                paziente.getFuso()
        );
    }
}
