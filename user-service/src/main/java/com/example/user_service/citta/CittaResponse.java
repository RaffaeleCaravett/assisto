package com.example.user_service.citta;

public record CittaResponse(Long id, String nome, Long regioneId) {
    public static CittaResponse from(Citta citta) {
        return new CittaResponse(citta.getId(), citta.getNome(), citta.getRegioneId());
    }
}
