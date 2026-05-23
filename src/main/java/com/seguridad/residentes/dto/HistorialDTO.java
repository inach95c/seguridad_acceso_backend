/*// HistorialDTO.java
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
*/


package com.seguridad.residentes.dto;

public class HistorialDTO {

    private String visitante;
    private String destino;
    private String fechaHoraEntrada;
    private String fechaHoraSalida;

    // Nuevos campos
    private String tipoVisitante;
    private String motivoVisita;
    private String placaVehiculo;
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

