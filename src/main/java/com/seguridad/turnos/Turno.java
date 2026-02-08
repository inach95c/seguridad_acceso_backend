package com.seguridad.turnos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "turnos", indexes = {
        @Index(name = "idx_turnos_activo", columnList = "activo")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(name = "inicio_hora", nullable = false)
    private LocalTime inicioHora;

    @Column(name = "fin_hora", nullable = false)
    private LocalTime finHora;

    @Column(nullable = false)
    private Boolean activo = true;
}
