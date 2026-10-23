package com.example.user_service.security;

import com.example.security.Roles;
import com.example.user_service.assegnazione.Assegnazione;
import com.example.user_service.assegnazione.AssegnazioneRepository;
import com.example.user_service.enums.RuoloAssegnazione;
import com.example.user_service.enums.StatoAssegnazione;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component("assegnazioneAuth")
@RequiredArgsConstructor
public class AssegnazioneAuth {

    private final AssegnazioneRepository assegnazioni;

    public boolean canRead(Long pazienteId) {
        return allowed(pazienteId, false);
    }

    public boolean canWrite(Long pazienteId) {
        return allowed(pazienteId, true);
    }

    private boolean allowed(Long pazienteId, boolean write) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || pazienteId == null) {
            return false;
        }
        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> ("ROLE_" + Roles.ADMIN).equals(authority.getAuthority()))) {
            return true;
        }
        Long utenteId = parseId(authentication.getName());
        if (utenteId == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return assegnazioni.findByUtenteIdAndPazienteIdAndStato(utenteId, pazienteId, StatoAssegnazione.ATTIVA)
                .stream()
                .anyMatch(assegnazione -> vigente(assegnazione, today) && ruoloOk(assegnazione, write));
    }

    private static boolean vigente(Assegnazione assegnazione, LocalDate today) {
        LocalDate dal = assegnazione.getValidaDal();
        LocalDate al = assegnazione.getValidaAl();
        if (dal != null && dal.isAfter(today)) {
            return false;
        }
        return al == null || !al.isBefore(today);
    }

    private static boolean ruoloOk(Assegnazione assegnazione, boolean write) {
        RuoloAssegnazione ruolo = assegnazione.getRuolo();
        if (ruolo == RuoloAssegnazione.READWRITE) {
            return true;
        }
        return !write && ruolo == RuoloAssegnazione.READ;
    }

    private static Long parseId(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
