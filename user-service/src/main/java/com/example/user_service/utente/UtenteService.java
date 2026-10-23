package com.example.user_service.utente;

import com.example.security.exception.ConflictException;
import com.example.security.exception.NotFoundException;
import com.example.user_service.assegnazione.AssegnazioneRepository;
import com.example.user_service.enums.StatoAssegnazione;
import com.example.user_service.security.CurrentUtente;
import com.example.user_service.storage.CloudflareImageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UtenteService {

    private final UtenteRepository utenti;
    private final AssegnazioneRepository assegnazioni;
    private final PasswordEncoder passwordEncoder;
    private final CloudflareImageClient images;
    private final CurrentUtente currentUtente;

    @Transactional
    public UtenteResponse update(ProfiloRequest request, MultipartFile foto) {
        return saveProfilo(currentUtente.id(), request, foto);
    }

    @Transactional
    public UtenteResponse updateById(Long userId, ProfiloRequest request) {
        return saveProfilo(userId, request, null);
    }

    @Transactional
    public UtenteResponse updatePassword(Long userId, String password) {
        Utente utente = utenti.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));
        utente.setPasswordHash(passwordEncoder.encode(password));
        utente.setUpdatedAt(OffsetDateTime.now());
        return UtenteResponse.from(utenti.save(utente));
    }

    @Transactional
    public void deleteById(Long userId) {
        if (!utenti.existsById(userId)) {
            throw new NotFoundException("Utente non trovato");
        }
        OffsetDateTime now = OffsetDateTime.now();
        assegnazioni.findByUtenteIdAndStato(userId, StatoAssegnazione.ATTIVA)
                .forEach(assegnazione -> {
                    assegnazione.setStato(StatoAssegnazione.REVOCATA);
                    assegnazione.setRevocataAt(now);
                });
        utenti.deleteById(userId);
    }

    private UtenteResponse saveProfilo(Long userId, ProfiloRequest request, MultipartFile foto) {
        Utente utente = utenti.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));
        String email = request.email().trim().toLowerCase(Locale.ITALY);
        if (utenti.existsByEmailAndIdNot(email, utente.getId())) {
            throw new ConflictException("Email già registrata");
        }
        utente.setEmail(email);
        utente.setNome(request.nome());
        utente.setCognome(request.cognome());
        utente.setTelefono(request.telefono());
        utente.setDataNascita(request.dataNascita());
        utente.setCitta(request.citta());
        utente.setIndirizzo(request.indirizzo());
        utente.setCap(request.cap());
        if (foto != null && !foto.isEmpty()) {
            utente.setFotoUrl(images.upload(foto));
        } else if (request.fotoUrl() != null) {
            utente.setFotoUrl(request.fotoUrl());
        }
        utente.setUpdatedAt(OffsetDateTime.now());
        return UtenteResponse.from(utenti.save(utente));
    }
}
