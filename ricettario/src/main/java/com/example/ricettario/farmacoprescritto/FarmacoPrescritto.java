package com.example.ricettario.farmacoprescritto;

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
@Table(name = "farmaco_prescritto")
public class FarmacoPrescritto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long prescrizioneId;

    @Column(columnDefinition = "text")
    private String nomeMedicina;

    @Column(columnDefinition = "text")
    private String categoria;

    @Column(columnDefinition = "text")
    private String posologia;

    private OffsetDateTime createdAt;
}
