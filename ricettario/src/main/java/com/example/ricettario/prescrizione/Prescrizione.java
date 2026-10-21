package com.example.ricettario.prescrizione;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "prescrizione")
public class Prescrizione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pazienteId;

    @Column(nullable = false)
    private Long caricataDaUtenteId;

    @Column(columnDefinition = "text")
    private String nomeDottore;

    @Column(columnDefinition = "text")
    private String testo;

    @Column(columnDefinition = "text")
    private String documentoUrl;

    private OffsetDateTime uploadedAt;
}
