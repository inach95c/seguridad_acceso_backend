// SolicitudDTO.java
package com.seguridad.residentes.dto;

public class SolicitudDTO {
    private String visitante;
    private Long destinoId;   // 👈 nuevo campo
    private String fechaHora; // formato ISO desde el frontend

    // Getters y Setters
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