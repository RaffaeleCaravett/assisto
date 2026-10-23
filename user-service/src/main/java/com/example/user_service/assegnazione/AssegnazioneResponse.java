package com.example.user_service.assegnazione;

import com.example.user_service.enums.RuoloAssegnazione;
import com.example.user_service.enums.StatoAssegnazione;

import java.time.LocalDate;

public record AssegnazioneResponse(
        Long id,
        Long pazienteId,
        Long utenteId,
        Long creatoreId,
        RuoloAssegnazione ruolo,
        StatoAssegnazione stato,
        LocalDate validaDal
) {
    public static AssegnazioneResponse from(Assegnazione assegnazione) {
        return new AssegnazioneResponse(
                assegnazione.getId(),
                assegnazione.getPazienteId(),
                assegnazione.getUtenteId(),
                assegnazione.getCreataDaUtenteId(),
                assegnazione.getRuolo(),
                assegnazione.getStato(),
                assegnazione.getValidaDal()
        );
    }
}
