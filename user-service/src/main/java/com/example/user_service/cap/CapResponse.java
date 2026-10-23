package com.example.user_service.cap;

public record CapResponse(Long id, String codice, Long cittaId) {
    public static CapResponse from(Cap cap) {
        return new CapResponse(cap.getId(), cap.getCodice(), cap.getCittaId());
    }
}
