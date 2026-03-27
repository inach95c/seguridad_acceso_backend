// HistorialDTO.java
package com.seguridad.residentes.dto;

public class HistorialDTO {
    private String visitante;
    private String destino;
    private String fechaHoraEntrada;
    private String fechaHoraSalida;

    public String getVisitante() {
        return visitante;
    }
    public void setVisitante(String visitante) {
        this.visitante = visitante;
    }

    public String getDestino() {
        return destino;
    }
    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getFechaHoraEntrada() {
        return fechaHoraEntrada;
    }
    public void setFechaHoraEntrada(String fechaHoraEntrada) {
        this.fechaHoraEntrada = fechaHoraEntrada;
    }

    public String getFechaHoraSalida() {
        return fechaHoraSalida;
    }
    public void setFechaHoraSalida(String fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }
}
