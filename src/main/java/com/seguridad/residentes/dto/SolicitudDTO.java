/*package com.seguridad.residentes.dto;

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
}*/


package com.seguridad.residentes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SolicitudDTO {

    @NotBlank(message = "El nombre del visitante es obligatorio")
    private String visitante;

    @NotNull(message = "El destino es obligatorio")
    private Long destinoId;

    @NotBlank(message = "La fecha y hora son obligatorias")
    private String fechaHora; // formato ISO enviado desde Angular

    // =========================
    // Nuevos campos
    // =========================

    @NotBlank(message = "El tipo de visitante es obligatorio")
    private String tipoVisitante; // FAMILIAR, TECNICO, DELIVERY, etc.

    @NotBlank(message = "El motivo de la visita es obligatorio")
    private String motivoVisita;

    @Size(max = 20, message = "La placa no puede exceder 20 caracteres")
    private String placaVehiculo;

    @Size(max = 30, message = "El DNI no puede exceder 30 caracteres")
    private String dniVisitante;

    private Integer numeroAcompanantes;

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

    public String getTipoVisitante() {
        return tipoVisitante;
    }
    public void setTipoVisitante(String tipoVisitante) {
        this.tipoVisitante = tipoVisitante;
    }

    public String getMotivoVisita() {
        return motivoVisita;
    }
    public void setMotivoVisita(String motivoVisita) {
        this.motivoVisita = motivoVisita;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }
    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }

    public String getDniVisitante() {
        return dniVisitante;
    }
    public void setDniVisitante(String dniVisitante) {
        this.dniVisitante = dniVisitante;
    }

    public Integer getNumeroAcompanantes() {
        return numeroAcompanantes;
    }
    public void setNumeroAcompanantes(Integer numeroAcompanantes) {
        this.numeroAcompanantes = numeroAcompanantes;
    }
}

