package com.seguridad.residentes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SolicitudDTO {

    @NotBlank(message = "El nombre del visitante es obligatorio")
    private String visitante;

    @NotNull(message = "El destino es obligatorio")
    private Long destinoId;

    @NotBlank(message = "La fecha y hora son obligatorias")
    private String fechaHora; // formato ISO enviado desde Angular

    // =========================
    // Getters y Setters
    // =========================
    public String getVisitante() {
        return visitante;
    }
    public void setVisitante(String visitante) {
        this.visitante = visitante;
    }

    public Long getDestinoId() {
        return destinoId;
    }
    public void setDestinoId(Long destinoId) {
        this.destinoId = destinoId;
    }

    public String getFechaHora() {
        return fechaHora;
    }
    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }
}
