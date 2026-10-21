package com.example.ricettario.esecuzione;

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
@Table(name = "esecuzione")
public class Esecuzione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long taskId;

    @Column(nullable = false)
    private Long eseguitoDaUtenteId;

    private OffsetDateTime eseguitoAt;

    @Column(columnDefinition = "text")
    private String nota;

    @Column(columnDefinition = "text")
    private String provaUrl;

    @Column(columnDefinition = "text")
    private String snapshotNome;

    @Column(columnDefinition = "text")
    private String snapshotEmail;

    @Column(columnDefinition = "text")
    private String snapshotTelefono;

    private Long verificataDaUtenteId;

    private OffsetDateTime verificataAt;

    @Column(columnDefinition = "text")
    private String esitoVerifica;
}
