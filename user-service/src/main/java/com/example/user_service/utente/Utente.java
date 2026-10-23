package com.example.user_service.utente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "utente")
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "citext", unique = true, nullable = false)
    private String email;

    @Column(nullable = false, columnDefinition = "text")
    private String passwordHash;

    @Column(columnDefinition = "text")
    private String nome;

    @Column(columnDefinition = "text")
    private String cognome;

    @Column(columnDefinition = "text")
    private String telefono;

    @Column(columnDefinition = "text")
    private String fotoUrl;

    private LocalDate dataNascita;

    @Column(columnDefinition = "text")
    private String citta;

    @Column(columnDefinition = "text")
    private String indirizzo;

    @Column(columnDefinition = "text")
    private String cap;

    @Column(columnDefinition = "text", unique = true)
    private String codiceAccesso;

    private OffsetDateTime codiceAccessoScadeAt;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
