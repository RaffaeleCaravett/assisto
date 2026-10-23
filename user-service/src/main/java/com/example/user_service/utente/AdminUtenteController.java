package com.example.user_service.utente;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/utenti")
@RequiredArgsConstructor
public class AdminUtenteController {

    private final UtenteService utenteService;

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UtenteResponse update(@PathVariable Long userId, @Valid @RequestBody ProfiloRequest request) {
        return utenteService.updateById(userId, request);
    }

    @PutMapping("/{userId}/password")
    @PreAuthorize("hasRole('ADMIN')")
    public UtenteResponse updatePassword(@PathVariable Long userId, @Valid @RequestBody PasswordRequest request) {
        return utenteService.updatePassword(userId, request.password());
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long userId) {
        utenteService.deleteById(userId);
    }
}
