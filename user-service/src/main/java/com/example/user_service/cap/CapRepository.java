package com.example.user_service.cap;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CapRepository extends JpaRepository<Cap, Long> {

    List<Cap> findByCittaId(Long cittaId);
}
