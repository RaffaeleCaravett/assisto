package com.example.user_service.paziente;

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
@Table(name = "paziente")
public class Paziente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String nome;

    @Column(columnDefinition = "text")
    private String cognome;

    private LocalDate dataNascita;

    @Column(columnDefinition = "text")
    private String telefono;

    @Column(columnDefinition = "text")
    private String fotoUrl;

    @Column(columnDefinition = "text")
    private String citta;

    @Column(columnDefinition = "text")
    private String indirizzo;

    @Column(columnDefinition = "text")
    private String cap;

    @Column(columnDefinition = "text")
    private String fuso;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
