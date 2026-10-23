package com.example.user_service.assegnazione;

import com.example.security.exception.ConflictException;
import com.example.security.exception.ForbiddenException;
import com.example.security.exception.NotFoundException;
import com.example.user_service.enums.StatoAssegnazione;
import com.example.user_service.paziente.PazienteRepository;
import com.example.user_service.security.AssegnazioneAuth;
import com.example.user_service.security.CurrentUtente;
import com.example.user_service.utente.Utente;
import com.example.user_service.utente.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AssegnazioneService {

    private final AssegnazioneRepository assegnazioni;
    private final PazienteRepository pazienti;
    private final UtenteRepository utenti;
    private final CurrentUtente currentUtente;
    private final AssegnazioneAuth assegnazioneAuth;

    @Transactional
    public AssegnazioneResponse create(AssegnazioneRequest request) {
        if (!assegnazioneAuth.canWrite(request.pazienteId())) {
            throw new ForbiddenException("Permesso di scrittura mancante sul paziente");
        }
        if (!pazienti.existsById(request.pazienteId())) {
            throw new NotFoundException("Paziente non trovato");
        }
        Utente utente = utenti.findById(request.utenteId())
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));
        if (assegnazioni.existsByPazienteIdAndUtenteIdAndStato(
                request.pazienteId(), request.utenteId(), StatoAssegnazione.ATTIVA)) {
            throw new ConflictException("Assegnazione già presente");
        }
        Long creatoreId = currentUtente.id();
        OffsetDateTime now = OffsetDateTime.now();
        Assegnazione assegnazione = new Assegnazione();
        assegnazione.setPazienteId(request.pazienteId());
        assegnazione.setUtenteId(utente.getId());
        assegnazione.setCreataDaUtenteId(creatoreId);
        assegnazione.setRuolo(request.ruolo());
        assegnazione.setStato(StatoAssegnazione.ATTIVA);
        assegnazione.setDestinatarioEmail(utente.getEmail());
        assegnazione.setDestinatarioTelefono(utente.getTelefono());
        assegnazione.setValidaDal(LocalDate.now());
        assegnazione.setCreatedAt(now);
        return AssegnazioneResponse.from(assegnazioni.save(assegnazione));
    }

    @Transactional
    public void delete(Long assegnazioneId) {
        Assegnazione assegnazione = assegnazioni.findById(assegnazioneId)
                .orElseThrow(() -> new NotFoundException("Assegnazione non trovata"));
        if (!currentUtente.id().equals(assegnazione.getCreataDaUtenteId())) {
            throw new ForbiddenException("Solo il creatore può cancellare l'assegnazione");
        }
        if (assegnazione.getStato() == StatoAssegnazione.REVOCATA) {
            throw new ConflictException("Assegnazione già cancellata");
        }
        assegnazione.setStato(StatoAssegnazione.REVOCATA);
        assegnazione.setRevocataAt(OffsetDateTime.now());
        assegnazioni.save(assegnazione);
    }
}
