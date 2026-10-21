package com.example.ricettario.task;

import com.example.ricettario.enums.StatoTask;
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
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long percorsoId;

    private OffsetDateTime previstoAt;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "text")
    private StatoTask stato;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
