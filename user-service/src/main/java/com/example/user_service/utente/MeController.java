package com.example.user_service.utente;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MeController {

    private final UtenteService utenteService;

    @PutMapping(value = "/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UtenteResponse update(@Valid @RequestBody ProfiloRequest request) {
        return utenteService.update(request, null);
    }

    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UtenteResponse updateWithFoto(
            @RequestPart("dati") @Valid ProfiloRequest request,
            @RequestPart(value = "foto", required = false) MultipartFile foto
    ) {
        return utenteService.update(request, foto);
    }
}
