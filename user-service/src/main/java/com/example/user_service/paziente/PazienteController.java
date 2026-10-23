package com.example.user_service.paziente;

import com.example.user_service.security.RequireRead;
import com.example.user_service.security.RequireWrite;
import com.example.user_service.utente.UtenteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class PazienteController {

    private final PazienteService pazienteService;

    @PostMapping("/pazienti")
    public PazienteResponse create(@Valid @RequestBody PazienteRequest request) {
        return pazienteService.create(request);
    }

    @PutMapping("/pazienti/{pazienteId}")
    @RequireWrite
    public PazienteResponse update(@PathVariable Long pazienteId, @Valid @RequestBody PazienteRequest request) {
        return pazienteService.update(pazienteId, request);
    }

    @GetMapping("/utenti/{utenteId}/pazienti")
    public List<PazienteResponse> pazientiDi(@PathVariable Long utenteId) {
        return pazienteService.pazientiDi(utenteId);
    }

    @GetMapping("/pazienti/{pazienteId}/utenti")
    @RequireRead
    public List<UtenteResponse> utentiDelPaziente(@PathVariable Long pazienteId) {
        return pazienteService.utentiDelPaziente(pazienteId);
    }
}
