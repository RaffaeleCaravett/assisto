package com.example.user_service.assegnazione;

import com.example.user_service.enums.RuoloAssegnazione;
import jakarta.validation.constraints.NotNull;

public record AssegnazioneRequest(
        @NotNull Long pazienteId,
        @NotNull Long utenteId,
        @NotNull RuoloAssegnazione ruolo
) {
}
