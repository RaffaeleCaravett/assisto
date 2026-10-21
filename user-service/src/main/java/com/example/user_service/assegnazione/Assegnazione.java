package com.example.user_service.assegnazione;

import com.example.user_service.enums.RuoloAssegnazione;
import com.example.user_service.enums.StatoAssegnazione;
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
@Table(name = "assegnazione")
public class Assegnazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pazienteId;

    private Long utenteId;

    @Column(nullable = false)
    private Long creataDaUtenteId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "text")
    private RuoloAssegnazione ruolo;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "text")
    private StatoAssegnazione stato;

    @Column(columnDefinition = "text")
    private String destinatarioEmail;

    @Column(columnDefinition = "text")
    private String destinatarioTelefono;

    @Column(columnDefinition = "text")
    private String codiceAccessoHash;

    private OffsetDateTime codiceScadeAt;

    private OffsetDateTime codiceUsatoAt;

    private LocalDate validaDal;

    private LocalDate validaAl;

    private OffsetDateTime createdAt;

    private OffsetDateTime revocataAt;
}
