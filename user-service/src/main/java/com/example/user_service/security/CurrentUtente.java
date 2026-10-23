package com.example.user_service.security;

import com.example.security.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUtente {

    public Long id() {
        Authentication authentication = authentication();
        try {
            return Long.valueOf(authentication.getName());
        } catch (NumberFormatException ex) {
            throw new UnauthorizedException("Utente non autenticato");
        }
    }

    public boolean admin() {
        return authentication().getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    private Authentication authentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new UnauthorizedException("Utente non autenticato");
        }
        return authentication;
    }
}
