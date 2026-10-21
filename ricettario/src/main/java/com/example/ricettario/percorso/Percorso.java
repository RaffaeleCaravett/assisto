package com.example.ricettario.percorso;

import com.example.ricettario.enums.StatoPercorso;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "percorso")
public class Percorso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long prescrizioneId;

    @Column(nullable = false)
    private Long creatoDaUtenteId;

    @Column(columnDefinition = "text")
    private String nome;

    private LocalDate dataInizio;

    private LocalDate dataFine;

    private Integer durataGiorni;

    private Integer cadenzaGiorni;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "text")
    private StatoPercorso stato;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
