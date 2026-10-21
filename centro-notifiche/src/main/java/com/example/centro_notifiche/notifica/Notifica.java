package com.example.centro_notifiche.notifica;

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
@Table(name = "notifica")
public class Notifica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long utenteId;

    private OffsetDateTime emessaAt;

    @Column(columnDefinition = "text")
    private String testo;

    @Column(columnDefinition = "text")
    private String tipo;

    private OffsetDateTime lettoAt;

    private Long taskId;

    private Long esecuzioneId;

    private Long assegnazioneId;
}
