package com.example.user_service.regione;

public record RegioneResponse(Long id, String nome) {
    public static RegioneResponse from(Regione regione) {
        return new RegioneResponse(regione.getId(), regione.getNome());
    }
}
