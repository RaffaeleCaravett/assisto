package com.example.user_service.utente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

    Optional<Utente> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<Utente> findByCodiceAccesso(String codiceAccesso);

    boolean existsByCodiceAccesso(String codiceAccesso);
}
