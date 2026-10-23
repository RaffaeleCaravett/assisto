package com.example.user_service.citta;

import com.example.security.exception.NotFoundException;
import com.example.user_service.cap.CapRepository;
import com.example.user_service.cap.CapResponse;
import com.example.user_service.regione.RegioneRepository;
import com.example.user_service.regione.RegioneResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CittaService {

    private final CittaRepository citta;
    private final RegioneRepository regioni;
    private final CapRepository cap;

    @Transactional(readOnly = true)
    public List<CittaResponse> all() {
        return citta.findAll().stream().map(CittaResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public RegioneResponse regione(Long cityId) {
        Citta found = citta.findById(cityId)
                .orElseThrow(() -> new NotFoundException("Città non trovata"));
        return regioni.findById(found.getRegioneId())
                .map(RegioneResponse::from)
                .orElseThrow(() -> new NotFoundException("Regione non trovata"));
    }

    @Transactional(readOnly = true)
    public List<CapResponse> cap(Long cityId) {
        if (!citta.existsById(cityId)) {
            throw new NotFoundException("Città non trovata");
        }
        return cap.findByCittaId(cityId).stream().map(CapResponse::from).toList();
    }
}
