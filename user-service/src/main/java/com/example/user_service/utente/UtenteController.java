package com.example.user_service.utente;

import com.example.user_service.auth.AuthService;
import com.example.user_service.auth.SignupRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/utenti")
@RequiredArgsConstructor
public class UtenteController {

    private final AuthService authService;

    @PostMapping
    public UtenteResponse create(@Valid @RequestBody SignupRequest request) {
        return authService.signup(request);
    }
}
