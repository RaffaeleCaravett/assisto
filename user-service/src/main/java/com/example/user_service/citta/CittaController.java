package com.example.user_service.citta;

import com.example.user_service.cap.CapResponse;
import com.example.user_service.regione.RegioneResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users/citta")
@RequiredArgsConstructor
public class CittaController {

    private final CittaService cittaService;

    @GetMapping
    public List<CittaResponse> all() {
        return cittaService.all();
    }

    @GetMapping("/{cityId}/regione")
    public RegioneResponse regione(@PathVariable Long cityId) {
        return cittaService.regione(cityId);
    }

    @GetMapping("/{cityId}/cap")
    public List<CapResponse> cap(@PathVariable Long cityId) {
        return cittaService.cap(cityId);
    }
}
