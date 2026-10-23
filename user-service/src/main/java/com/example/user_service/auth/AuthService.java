package com.example.user_service.auth;

import com.example.security.Roles;
import com.example.security.dtos.Token;
import com.example.security.exception.ConflictException;
import com.example.security.exception.ServiceUnavailableException;
import com.example.security.exception.UnauthorizedException;
import com.example.user_service.mail.MailService;
import com.example.user_service.security.JWTTools;
import com.example.user_service.utente.Utente;
import com.example.user_service.utente.UtenteRepository;
import com.example.user_service.utente.UtenteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final int CODE_MINUTES = 10;

    private final UtenteRepository utenti;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final JWTTools jwtTools;
    private final SecureRandom random = new SecureRandom();

    @Transactional
    public UtenteResponse signup(SignupRequest request) {
        String email = normalize(request.email());
        if (utenti.existsByEmail(email)) {
            throw new ConflictException("Email già registrata");
        }
        OffsetDateTime now = OffsetDateTime.now();
        Utente utente = new Utente();
        utente.setEmail(email);
        utente.setPasswordHash(passwordEncoder.encode(request.password()));
        utente.setNome(request.nome());
        utente.setCognome(request.cognome());
        utente.setTelefono(request.telefono());
        utente.setFotoUrl(request.fotoUrl());
        utente.setDataNascita(request.dataNascita());
        utente.setCitta(request.citta());
        utente.setIndirizzo(request.indirizzo());
        utente.setCap(request.cap());
        utente.setCreatedAt(now);
        utente.setUpdatedAt(now);
        return UtenteResponse.from(utenti.save(utente));
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        String email = normalize(request.email());
        Utente utente = utenti.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Credenziali non valide"));
        if (!passwordEncoder.matches(request.password(), utente.getPasswordHash())) {
            throw new UnauthorizedException("Credenziali non valide");
        }
        String code = newCode();
        utente.setCodiceAccesso(code);
        utente.setCodiceAccessoScadeAt(OffsetDateTime.now().plusMinutes(CODE_MINUTES));
        utenti.save(utente);
        try {
            mailService.sendAccessCode(utente.getEmail(), utente.getNome(), code);
        } catch (MailException ex) {
            throw new ServiceUnavailableException("Invio del codice non riuscito");
        }
        return new LoginResponse("Codice inviato all'indirizzo email indicato");
    }

    @Transactional(noRollbackFor = UnauthorizedException.class)
    public Token confirmAccessCode(String code) {
        if (code == null || code.isBlank()) {
            throw new UnauthorizedException("Codice non valido o scaduto");
        }
        Utente utente = utenti.findByCodiceAccesso(code.trim())
                .orElseThrow(() -> new UnauthorizedException("Codice non valido o scaduto"));
        OffsetDateTime scadenza = utente.getCodiceAccessoScadeAt();
        utente.setCodiceAccesso(null);
        utente.setCodiceAccessoScadeAt(null);
        utente.setUpdatedAt(OffsetDateTime.now());
        utenti.save(utente);
        if (scadenza == null || scadenza.isBefore(OffsetDateTime.now())) {
            throw new UnauthorizedException("Codice non valido o scaduto");
        }
        return jwtTools.createTokens(utente.getId(), Roles.USER);
    }

    private String newCode() {
        for (int attempt = 0; attempt < 5; attempt++) {
            String code = String.format("%06d", random.nextInt(1_000_000));
            if (!utenti.existsByCodiceAccesso(code)) {
                return code;
            }
        }
        throw new ServiceUnavailableException("Impossibile generare il codice di accesso");
    }

    private static String normalize(String email) {
        return email.trim().toLowerCase(Locale.ITALY);
    }
}
