package com.example.user_service.utente;

import java.time.LocalDate;

public record UtenteResponse(
        Long id,
        String email,
        String nome,
        String cognome,
        String telefono,
        String fotoUrl,
        LocalDate dataNascita,
        String citta,
        String indirizzo,
        String cap
) {
    public static UtenteResponse from(Utente utente) {
        return new UtenteResponse(
                utente.getId(),
                utente.getEmail(),
                utente.getNome(),
                utente.getCognome(),
                utente.getTelefono(),
                utente.getFotoUrl(),
                utente.getDataNascita(),
                utente.getCitta(),
                utente.getIndirizzo(),
                utente.getCap()
        );
    }
}
