package com.example.centro_notifiche.consegna;

import com.example.centro_notifiche.enums.CanaleConsegna;
import com.example.centro_notifiche.enums.StatoConsegna;
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

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "consegna")
public class Consegna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long notificaId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "text")
    private CanaleConsegna canale;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "text")
    private StatoConsegna stato;

    private Integer tentativi;

    private OffsetDateTime inviataAt;

    @Column(columnDefinition = "text")
    private String errore;
}
