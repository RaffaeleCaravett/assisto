package com.example.user_service.assegnazione;

import com.example.user_service.enums.StatoAssegnazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssegnazioneRepository extends JpaRepository<Assegnazione, Long> {

    List<Assegnazione> findByUtenteIdAndPazienteIdAndStato(Long utenteId, Long pazienteId, StatoAssegnazione stato);

    List<Assegnazione> findByUtenteIdAndStato(Long utenteId, StatoAssegnazione stato);

    List<Assegnazione> findByPazienteIdAndStato(Long pazienteId, StatoAssegnazione stato);

    boolean existsByPazienteIdAndUtenteIdAndStato(Long pazienteId, Long utenteId, StatoAssegnazione stato);
}
