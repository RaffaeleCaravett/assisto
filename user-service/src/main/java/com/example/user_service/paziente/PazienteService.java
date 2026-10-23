package com.example.user_service.paziente;

import com.example.security.exception.ForbiddenException;
import com.example.security.exception.NotFoundException;
import com.example.user_service.assegnazione.Assegnazione;
import com.example.user_service.assegnazione.AssegnazioneRepository;
import com.example.user_service.enums.RuoloAssegnazione;
import com.example.user_service.enums.StatoAssegnazione;
import com.example.user_service.security.CurrentUtente;
import com.example.user_service.utente.Utente;
import com.example.user_service.utente.UtenteRepository;
import com.example.user_service.utente.UtenteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PazienteService {

    private final PazienteRepository pazienti;
    private final AssegnazioneRepository assegnazioni;
    private final UtenteRepository utenti;
    private final CurrentUtente currentUtente;

    @Transactional
    public PazienteResponse create(PazienteRequest request) {
        Utente utente = utenti.findById(currentUtente.id())
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));
        OffsetDateTime now = OffsetDateTime.now();
        Paziente paziente = new Paziente();
        apply(paziente, request);
        paziente.setCreatedAt(now);
        paziente.setUpdatedAt(now);
        paziente = pazienti.save(paziente);

        Assegnazione assegnazione = new Assegnazione();
        assegnazione.setPazienteId(paziente.getId());
        assegnazione.setUtenteId(utente.getId());
        assegnazione.setCreataDaUtenteId(utente.getId());
        assegnazione.setRuolo(RuoloAssegnazione.READWRITE);
        assegnazione.setStato(StatoAssegnazione.ATTIVA);
        assegnazione.setDestinatarioEmail(utente.getEmail());
        assegnazione.setDestinatarioTelefono(utente.getTelefono());
        assegnazione.setValidaDal(LocalDate.now());
        assegnazione.setCreatedAt(now);
        assegnazioni.save(assegnazione);
        return PazienteResponse.from(paziente);
    }

    @Transactional
    public PazienteResponse update(Long pazienteId, PazienteRequest request) {
        Paziente paziente = pazienti.findById(pazienteId)
                .orElseThrow(() -> new NotFoundException("Paziente non trovato"));
        apply(paziente, request);
        paziente.setUpdatedAt(OffsetDateTime.now());
        return PazienteResponse.from(pazienti.save(paziente));
    }

    @Transactional(readOnly = true)
    public List<PazienteResponse> pazientiDi(Long utenteId) {
        if (!currentUtente.admin() && !currentUtente.id().equals(utenteId)) {
            throw new ForbiddenException("Non puoi vedere i pazienti di un altro utente");
        }
        if (!utenti.existsById(utenteId)) {
            throw new NotFoundException("Utente non trovato");
        }
        List<Long> ids = assegnazioni.findByUtenteIdAndStato(utenteId, StatoAssegnazione.ATTIVA).stream()
                .map(Assegnazione::getPazienteId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        return pazienti.findAllById(ids).stream().map(PazienteResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<UtenteResponse> utentiDelPaziente(Long pazienteId) {
        if (!pazienti.existsById(pazienteId)) {
            throw new NotFoundException("Paziente non trovato");
        }
        List<Long> ids = assegnazioni.findByPazienteIdAndStato(pazienteId, StatoAssegnazione.ATTIVA).stream()
                .map(Assegnazione::getUtenteId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        return utenti.findAllById(ids).stream().map(UtenteResponse::from).toList();
    }

    private static void apply(Paziente paziente, PazienteRequest request) {
        paziente.setNome(request.nome());
        paziente.setCognome(request.cognome());
        paziente.setDataNascita(request.dataNascita());
        paziente.setTelefono(request.telefono());
        paziente.setFotoUrl(request.fotoUrl());
        paziente.setCitta(request.citta());
        paziente.setIndirizzo(request.indirizzo());
        paziente.setCap(request.cap());
        paziente.setFuso(request.fuso());
    }
}
